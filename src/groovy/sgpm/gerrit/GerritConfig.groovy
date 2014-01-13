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

import static grails.util.Holders.getGrailsApplication

/**
 * Methods for getting Gerrit server configuration data from SGPMConfig.
 */
class GerritConfig {
	/** Default fallback server URL. */
	private static final DEFAULT_URL = 'http://127.0.0.1'
	/** Default fallback username. */
	private static final DEFAULT_USERNAME = ''
	/** Default fallback password. */
	private static final DEFAULT_PASSWORD = ''

	/**
	 * Get server URL from configuration.
	 * 
	 * Falls back on the fallback parameter or a default value.
	 */
	static def getUrl(String fallback=null) {
		grailsApplication.config.sgpm.gerrit.url ?: fallback ?: DEFAULT_URL
	}

	/**
	 * Get username from configuration.
	 * 
	 * Falls back on the fallback parameter or a default value.
	 */
	static def getUsername(String fallback=null) {
		grailsApplication.config.sgpm.gerrit.username ?: fallback ?: DEFAULT_USERNAME
	}

	/**
	 * Get password from configuration.
	 * 
	 * Falls back on the fallback parameter or a default value.
	 */
	static def getPassword(String fallback=null) {
		grailsApplication.config.sgpm.gerrit.password ?: fallback ?: DEFAULT_PASSWORD
	}
	
	/**
	 * Get proxy server URL from configuration.
	 * 
	 * Falls back on the fallback parameter.
	 */
	static def getProxyUrl(String fallback=null) {
		grailsApplication.config.sgpm.gerrit.proxy.url ?: fallback
	}
	
	/**
	 * Get proxy server port number from configuration.
	 * 
	 * Falls back on the fallback parameter.
	 */
	static def getProxyPort(int fallback=-1) {
		grailsApplication.config.sgpm.gerrit.proxy.port as int ?: fallback
	}
	
	/**
	 * Get proxy server scheme from configuration.
	 * 
	 * Falls back on the fallback parameter.
	 */
	static def getProxyScheme(String fallback=null) {
		grailsApplication.config.sgpm.gerrit.proxy.scheme ?: fallback
	}
}
