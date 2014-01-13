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
import static org.hamcrest.CoreMatchers.*
import static groovy.json.JsonOutput.toJson

import grails.test.mixin.TestMixin
import grails.test.mixin.support.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class OptionsTests {
	Options options

	void setUp() {
		Options.injectJsonConverter()
		options = new Options()
	}
	
	/**
	 * Tests assigning options using the constructor.
	 */
	void testConstructor() {
		options = new Options(key1: 'value1', key2: 'value2')

		assertEquals('value1', options.key1)
		assertEquals('value2', options.key2)
	}

	/**
	 * Test assigning and reading options.
	 */
	void testAssignment() {
		options.key1 = 'value1'
		options.key2 = 'value2'

		assertEquals('value1', options.key1)
		assertEquals('value2', options.key2)
	}
	
	/**
	 * Test assignment of lists.
	 */
	void testVariableAssignment() {
		options.key1 'value1', 'value2', 'value3'
		options.key2 = ['value1', 'value2', 'value3']
		
		assertEquals(['value1', 'value2', 'value3'], options.key1)
		assertEquals(['value1', 'value2', 'value3'], options.key2)
	}

	/**
	 * Test assigning and reading suboptions.
	 */
	void testSuboptions() {
		options.key1a.key1b = 'value1'
		options.'key2a.key2b' = 'value2'

		assertEquals('value1', options.key1a.key1b)
		assertEquals('value2', options.key2a.key2b)

		assertThat(options.key1a, instanceOf(Options.class))
		assertThat(options.key2a, instanceOf(Options.class))
	}

	/**
	 * Test assigning and reading suboptions using a path.
	 */
	void testPath() {
		options.key1a.key1b = 'value1'
		assertEquals('value1', options.'key1a.key1b')

		options.key2a.key2b = 'value2'
		assertEquals('value2', options.key2a.key2b)
	}

	/**
	 * Test the static create method using a builder.
	 */
	void testBuilder() {
		options = Options.create {
			key1 {
				key1a 'value1a'
				key1b 'value1b'
			}
			key2 'value2'
			key3 = 'value3'
		}

		assertEquals('value1a', options.key1.key1a)
		assertEquals('value1b', options.key1.key1b)
		assertEquals('value2', options.key2)
		assertEquals('value3', options.key3)
	}
	
	/**
	 * Test the array method.
	 */
	void testArray() {
		options = Options.create {
			key1 array('value1', 'value2')
			key2 array(['value1', 'value2'])
		}
		
		assertEquals(['value1', 'value2'], options.key1)
		assertEquals(['value1', 'value2'], options.key2)
	}
	
	/**
	 * Test the map method.
	 */
	void testMap() {
		options = Options.create {
			key1 map {
				key1a 'value1'
			}
			
			key2 = [
				map { key2a 'value2a' },
				map { key2b 'value2b' }
			]
		}
		
		assertEquals('value1', options.key1.key1a)
		assertEquals('value2a', options.key2[0].key2a)
		assertEquals('value2b', options.key2[1].key2b)
	}

	/**
	 * Tests converting options into a JSON string.
	 */
	void testJsonConvertion() {
		options = Options.create {
			key1 {
				key1a 'value1a'
				key1b 'value1b'
			}
			key2 'value2'
		}
		def json = options.toJson()

		assertEquals(toJson([key1: [key1a: 'value1a', key1b: 'value1b'], key2: 'value2']), json)
	}
}
