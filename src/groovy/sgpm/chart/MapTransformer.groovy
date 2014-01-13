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

import groovy.lang.Closure;

import java.util.Map;

class MapTransformer {
	/** Alias used to reference the target object from actions. */
	private String targetAlias
	/** Transformation actions (closures or values for default action). */
	private final transformationActions = [:]
	/** Default transformation action (takes a value and returns an action). */
	private Closure defaultAction

	/**
	 * Create a new map transformer object.
	 */
	static MapTransformer create(Map params = [:], Closure block) {
		def transformer = block.delegate = new MapTransformer(params)
		block.resolveStrategy = Closure.DELEGATE_FIRST
		block()

		transformer
	}

	/**
	 * Set a transformation action.
	 */
	def transformation(String name, action) {
		transformationActions[name] = action
	}

	/**
	 * Apply transformations from a map onto a target object.
	 */
	def transform(source, target) {
		/* apply transformations */
		transformationActions.each { key, transformation ->
			if (key in source) {
				def value = source[key]
				def action

				if (transformation instanceof Closure) {
					/* use transformation action */
					action = transformation as Closure
				} else {
					/* apply default action */
					action = defaultAction.curry(transformation)
				}

				/* define variables passed to the action closure */
				action.delegate = [
					/* make the target accessible by the variable "target" or the name defined in targetAlias */
					(targetAlias ?: 'target'): target,
					/* make the current value accessible with the variable "value" */
					value: value,
					/* make the current key accessible with the variable "key" */
					key: key
				]
				action.resolveStrategy = Closure.DELEGATE_FIRST
				/* apply action */
				action.call()
			}
		}

		/* return the transformed target */
		target
	}
	
	def methodMissing(String name, args) {
		if (args.size() == 1) {
			/* add transformation action */
			transformation(name, args[0])
		} else {
			throw new MissingMethodException(name, MapTransformer, args)
		}
	}
}
