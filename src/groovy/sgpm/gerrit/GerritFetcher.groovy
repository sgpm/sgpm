/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Andreas Alanko, Emil Nilsson, Fredrik Larsson, Joakim Strand, Sandra Fridälv
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package sgpm.gerrit

import groovy.json.JsonSlurper
import groovyx.net.http.*
import static groovyx.net.http.ContentType.TEXT
import static sgpm.Debug.*

/**
 * Class for fetching data from a Gerrit API.
 */
class GerritFetcher {
	/* inject debug logging service */
	def debugService

	/**
	 * Fetch changes from the Gerrit API.
	 * Parameters in params:
	 * * fileChanges: whether to fetch information about changed files (optional).
	 * * codeReviews: whether to fetch information about code reviews (optional).
	 * * searchQuery: search query, an object or collection of objects (optional).
	 * * limit: max number of changes to fetch (optional).
	 * @throws HttpResponseException If request fails.
	 */
	static def fetchChanges(Map params) {
		def rest = createRESTClient(params + [authenticate: true])
		/* generate query (include email addresses and comments) */
		def query = [o: ['DETAILED_ACCOUNTS', 'MESSAGES']]
		
		if (params.fileChanges) {
			/* fetch changed files for each revision */
			/* TODO: should we take changes from all revisions or only the latest? */
			query.o << 'ALL_REVISIONS' << 'CURRENT_FILES'
		}
		if (params.codeReviews) {
			/* fetch code reviews */
			query.o << 'DETAILED_LABELS'
		}
		if (params.searchQuery && !searchQueryIsEmpty(params.searchQuery)) {
			query.q = translateSearchQuery(params.searchQuery)
		}
		if (params.limit) {
			query.n = params.limit
		}
		log("fetchChanges: query=$query")
		def resp = rest.get(path: '/changes/', query: query)
		/* parse JSON data from response */
		def json = jsonFromResponse(resp)

		/* create and return list of Gerrit changed from each entity in the JSON data */
		def changes = json.collect { data -> new GerritChange(data) }
		log("fetchChanges: Fetched ${changes.size} changes")
		return changes
	}

	/**
	 * Creates a change search query string from an object or a collection of objects.
	 */
	private static def translateSearchQuery(searchQuery) {
		if (searchQuery == null) {
			return null
		} else if (searchQuery instanceof Collection) {
			/* join non-null queries by wrapping them in parentheses and separating with AND */
			/* FIXME: what if query.toString() returns an empty string? */
			return searchQuery.findResults { query -> query ? "($query)" : null }.join(" AND ")
		} else {
			return searchQuery.toString()
		}
	}
	
	/**
	 * Check if a search query is empty (list with no or only null values, empty string).
	 * @param searchQuery
	 * @return
	 */
	private static def searchQueryIsEmpty(searchQuery) {
		if (searchQuery == null) {
			return true
		} else if (searchQuery instanceof Collection && !searchQuery.any()) {
			/* all items where null */
			return true
		} else {
			return !searchQuery.toString().asBoolean()
		}
	}

	/**
	 * Creates a REST client for fetching data from the Gerrit API.
	 * Parameters in params (optional):
	 * * authenticate: whether autentication is needed (default: false).
	 * * url: a server url overriding the configured settings.
	 * * username: a username overriding the configured settings.
	 * * password: a password overriding the configured settings.
	 */
	private static def createRESTClient(Map params) {
		/* create request with the server URL (parse as TEXT since returned body isn't plain JSON) */
		def rest = new RESTClient(params?.url ?: GerritConfig?.url, TEXT)

		if (params?.authenticate) {
			/* authenticate with basic HTTP authentication */
			rest.auth.basic(params.username ?: GerritConfig.username, params.password ?: GerritConfig.password)
		}
		
		if (GerritConfig.proxyUrl) {
			/* use proxy server */
			rest.setProxy(GerritConfig.proxyUrl, GerritConfig.proxyPort, GerritConfig.proxyScheme)
		}
		return rest
	}

	/**
	 * Extract JSON data from a response.
	 */
	private static def jsonFromResponse(HttpResponseDecorator resp) {
		/* is null if wrong repo or email */
		if(resp) {	
		def reader = new BufferedReader(resp?.data)
		/* remove the signature line */
		reader.readLine()

		/* parse and return JSON data */
		return new JsonSlurper().parseText(reader.text)
		}
		return null
	}
}
