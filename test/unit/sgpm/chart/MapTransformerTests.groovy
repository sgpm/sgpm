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

import static org.junit.Assert.*

import static java.lang.ProcessEnvironment.Value;

import grails.test.mixin.TestMixin
import grails.test.mixin.support.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class MapTransformerTests {
	final map = [
		a: 1,
		b: '2',
		c: 3
	]

	/**
	 * Test transforming a map with the transform method.
	 */
	void testTransform() {
		def target = []

		def transformer = MapTransformer.create {
			a { target << (value as String) }
			b { target << (value as int) }
		}

		def result = transformer.transform(map, target)

		assertEquals(['1', 2], result)
	}

	/**
	 * Test transforming a map using a default transformation action.
	 */
	void testDefaultAction() {
		def target = []

		def transformer = MapTransformer.create {
			/* concatenate path, key and value as a string */
			defaultAction = { path ->
				target << "$path:$key:$value".toString() /* XXX: must be Java string (not GString) to make JUnit equals comparison work */
			}

			a 'A'
			b 'B'
		}

		def result = transformer.transform(map, target)

		assertEquals(['A:a:1', 'B:b:2'], result)
	}
	
	/**
	 * Test using an alias for the target.
	 */
	void testTargetAlias() {
		def transformer = MapTransformer.create {
			targetAlias = 'output'
			
			a { output << value }
			b { output << (value as int) }
		}
		
		def result = transformer.transform(map, [])

		assertEquals([1, 2], result)
	}
}
