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

package sgpm

import sgpm.gerrit.GerritFetcher
import static sgpm.gerrit.GerritUtils.minutesBetween
import groovy.json.JsonSlurper
import groovyx.net.http.*
import static groovyx.net.http.ContentType.TEXT
import static java.lang.Math.max

/**
 * Service for fetching API data from a Gerrit service.
 */
class GerritService {
	/**
	 * Fetch changes from the Gerrit API.
	 * Parameters in params (optional):
	 * * fileChanges: whether to fetch file changes.
	 * * codeReviews: whether to fetch code reviews.
	 * * searchQuery: search query string.
	 * * interval: an interval between two dates to fetch from.
	 * * projects: a list of projects.
	 * * owners: a list of owners of changes.
	 * * status: the status of the changes.
	 * * limit: max number of changes to fetch.
	 * @throws HttpResponseException If request fails.
	 */
	def fetchChanges(Map params) {
		/* list of search query components */
		def searchQuery = [params.searchQuery]

		if (params.interval) {
			def now = new Date()
			/* calculate duration (in minutes) since the start and end of interval */
			def sinceStart = max(minutesBetween(now, params.interval.from), 0)
			def sinceEnd = max(minutesBetween(now, params.interval.to), 0)

			/* query changes created/updated between start and end of interval */
			searchQuery << "-age:${sinceStart}m AND age:${sinceEnd}m"
		}
		if (params.projects) {
			/* query changes from any of the projects (join queries for projects with OR) */
			searchQuery << "${params.projects.collect { project -> "project:{$project}" }.join(' OR ')}"
		}
		if (params.owners) {
			/* query changes by any of the owners (join queries for owners with OR) */
			searchQuery << "${params.owners.collect { name-> "owner:{$name}" }.join(' OR ')}"
		}
		if (params.status) {
			/* query changes with the status */
			searchQuery << "status:{${params.status}}"
		}
		return GerritFetcher.fetchChanges([searchQuery: searchQuery] + params.subMap(['url', 'username', 'password', 'limit', 'fileChanges', 'codeReviews']))
	}
	
	/**
	 * Fetch changes for a sprint from the Gerrit API.
	 * 
	 * A sprint must be provided and is used to determine the
	 * interval, owners and projects for the changes.
	 * Parameters in params (optional):
	 * * fileChanges: whether to fetch file changes.
	 * * codeReviews: whether to fetch code reviews.
	 * * searchQuery: search query string.
	 * * status: the status of the changes.
	 * * limit: max number of changes to fetch.
	 * @throws HttpResponseException If request fails.
	 */
	def fetchSprintChanges(Map params, Sprint sprint) {
		/* fetch between the start and end date of the sprint */
		params.interval = sprint.startDate..sprint.actualEndDate()
		/* fetch from the repositories belonging to the sprint */
		params.projects = sprint.repos
		/* fetch for the contributors belonging to the sprint */
		params.owners = sprint.emails
		return fetchChanges(params)
	}
}
