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

package sgpm.chart

import groovy.json.JsonOutput

class Options {
	private static final PATH_SEPARATOR = /\./
	private final options = [:]
	
	/**
	 * Create an options object.
	 */
	Options(Map options=[:]) {
		/* add all options from the parameters */
		options.each { name, value -> this."$name" = value }
	}
	
	/**
	 * Create a new options object with a builder.
	 */
	static create(Map params=[:], Closure closure) {
		closure.delegate = new Options(params)
		closure.resolveStrategy = Closure.DELEGATE_FIRST

		closure.call()
		closure.delegate
	}
	
	/**
	 * Create a new options object.
	 */
	static create(Map params=[:]) {
		new Options(params)
	}
	
	static injectJsonConverter() {
		/* inject static toJson(Options) method into JsonOutput for saving Options objects as maps */
		JsonOutput.metaClass.static.toJson = {
			Options o -> JsonOutput.toJson(o.options)
		}
	}

	/**
	 * Return a new options map.
	 */
	def map(Closure closure) {
		create(closure)
	}
	
	/**
	 * Return a new array.
	 */
	def array(List array) {
		array
	}
	
	/**
	 * Return a new array.
	 */
	def array(...params) {
		array(params as List)
	}
	
	/**
	 * Convert options to a JSON string.
	 * 
	 * injectJsonConverter() must have been called in order for this to work.
	 */
	String toJson() {
		JsonOutput.toJson(this)
	}
	
	@Override
	def methodMissing(String name, args) {
		if (args.size() == 1) {
			if (args[0] instanceof Closure) {
				def closure = args[0] as Closure
				/* create suboptions using the closure parameter */
				this."$name" = create(closure)
			} else {
				def value = args[0]
				/* set option to the value parameter */
				this."$name" = value
			}
		} else {
			def values = args as List
			/* set option to array of values */
			this."$name" = values
		}
	}

	@Override
	def propertyMissing(String name) {
		if (isPath(name)) {
			def (child, path) = splitPath(name)
			/* return property of direct child in the path (recursively) */
			this."$child"."$path"
		} else if (name in options) {
			/* return existing options */
			options[name]
		} else {
			/* automatically create suboptions */
			options[name] = new Options()
		}
	}

	@Override
	def propertyMissing(String name, value) {
		if (isPath(name)) {
			def (child, path) = splitPath(name)
			/* set property of direct child in the path (recursively) */
			this."$child"."$path" = value
		} else {
			/* set option */
			options[name] = value
		}
	}

	/**
	 * Check if a property name (key) is a path.
	 */
	private boolean isPath(String key) {
		key =~ PATH_SEPARATOR
	}

	/**
	 * Separate the first key and the rest from a path.
	 */
	private List splitPath(String key) {
		key.split(PATH_SEPARATOR, 2)
	}
}
