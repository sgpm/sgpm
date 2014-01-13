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

import java.text.SimpleDateFormat
import static Math.max

class Sprint {
	/** Date format used to represent the sprint as a string. */
	private static OUTPUT_DATE_FORMAT = "yyyy-MM-dd"

	/** The date the sprint starts at. */
	Date startDate
	/** The date the sprint ends at (FIXME: inclusive?). */
	Date endDate
	/** List of names of the repositories belonging to the sprint. */
	List repos
	/** List of email addresses of the contributors for the sprint. */
	List emails

	static hasMany = [repos: String, emails: String]
	static belongsTo = [team: Team]

	static constraints = { 
		/* if end date isn't set the sprint is open-ended */ 
		endDate nullable: true 
	}

	/**
	 * Return a string representation of the sprint.
	 * 
	 * Sprints are represented by their start and end date in the format:
	 * "Start: YYYY-MM-DD End: YYYY-MM-DD"
	 */
	String toString() {
		return "Start: ${startDate.format(OUTPUT_DATE_FORMAT)} End: ${endDate?.format(OUTPUT_DATE_FORMAT) ?: 'Open-ended'}"
	}

	Date actualEndDate() {
		return (endDate) ? endDate : new Date()
	}

	/**
	 * Get all sprints between (and including) a start and end sprint for a team (Team object or name).
	 * 
	 * The sprints will be ordered by date.
	 */
	static List<Sprint> sprintsBetween(Team team, startSprintId, endSprintId) {
		/* get a list of sprints */
		return withCriteria {
			eq 'team', team /* belonging to the selected team */
			between 'id', startSprintId as long, endSprintId as long /* between the first and last sprint */
		}
	}

	/**
	 * Get the previous (most recent) sprint for a team.
	 */
	static Sprint previousSprint(Team team) {
		/* find one sprint, take the most recent sorted by end date */
		def sprints = Sprint.allSprints(team)
		if(sprints.isEmpty()) {
			return null
		} else {
			return sprints.last()
		}
	}

	/**
	 * Return the length of a sprint in days.
	 */
	int getLength() {
		return max(endDate - startDate, 0)
	}

	static List<Sprint> allSprints(Team team) {
		def sprints = Sprint.findAllWhere(team: team)
		Collections.sort(sprints, new SprintComparator())

		return sprints
	}

	static class SprintComparator implements Comparator<Sprint> {
		@Override
		public int compare(Sprint s1, Sprint s2) {
			return s1.startDate.compareTo(s2.startDate)
		}
	}
}
