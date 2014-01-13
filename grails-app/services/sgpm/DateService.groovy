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

import static java.util.Calendar.*

/**
 * Service for getting dates.
 */
class DateService {
	/** First day of week. */
	private static final FIRST_DAY_OF_WEEK = MONDAY

	/**
	 * Return the date of the start of a week.
	 */
	def startOfWeek(week) {
		def cal = calendar
		/* select 00:00 of the monday of the chosen week */
		cal.set(WEEK_OF_YEAR, week)
		cal.set(DAY_OF_WEEK, MONDAY)
		cal.clearTime()
		
		/* convert to Date */
		cal.time
	}
	
	/**
	 * Get the current week.
	 */
	def getCurrentWeek() {
		calendar.get(WEEK_OF_YEAR)
	}
	
	/**
	 * Return a calendar instance.
	 */
	private def getCalendar() {
		def cal = Calendar.getInstance()
		cal.firstDayOfWeek = FIRST_DAY_OF_WEEK
		
		cal
	}
}
