/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Andreas Alanko, Emil Nilsson, Fredrik Larsson, Joakim Strand, Sandra Frid�lv
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

/**
 * A Gerrit project fetched from the API.
 */
class GerritProject {
	/** Project name. */
	final String name
	/** Project ID. */
	final String id
	/** Project description. */
	final String description

	/**
	 * Create a project from JSON data.
	 */
	GerritProject(String name, Map data) {
		this.name = name
		id = data.id
		description = data.description
	}
	
	/**
	 * Return String representation.
	 */
	String toString() {
		/* represent by name */
		name
	}
}
