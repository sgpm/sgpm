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

import groovy.time.TimeCategory
import java.text.SimpleDateFormat

/**
 * Helper methods for Gerrit API.
 */
class GerritUtils {
	/** Date format for timestamps in the Gerrit API. */
	private static final DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	static {
		/* parse timestamps as UTC */
		DATE_FORMAT.timeZone = TimeZone.getTimeZone('UTC')
	}

	/**
	 * Parses a date string from a Gerrit API response.
	 * 
	 * returns null if the string is null.
	 */
	static Date parseDateString(String dateString) {
		if (dateString) {
			DATE_FORMAT.parse(dateString)
		}
	}

	/**
	 * Calculates the number of minutes between two dates.
	 * 
	 * The seconds are truncated.
	 */
	static int minutesBetween(Date endDate, Date startDate) {
		def duration = TimeCategory.minus(endDate, startDate)
		
		duration.toMilliseconds() / 1000 / 60
	}
}
