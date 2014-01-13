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

package sgpm.measurement

import sgpm.Sprint
import sgpm.GerritService
import sgpm.gerrit.GerritChange
import sgpm.gerrit.GerritChange.CodeReview
import sgpm.gerrit.GerritChange.Message
import sgpm.gerrit.GerritChange.Revision

class SprintData {
	/** A gerrit service for fetching changes. */
	private final static gerritService = new GerritService()

	/** The sprint for the sprint data. */
	final Sprint sprint

	/** Time (in minutes) from a change has been uploaded until it has been merged for all merged changes. */
	List<Integer> mergeTimes = []

	int totalNumberOfComments
	
	int totalNumberOfPatchSets
	Map commits = [merged: 0, open: 0, abandoned: 0]

	Map lines = [inserted: 0, deleted: 0]

	Map reviewValues = [upTwo: 0, upOne: 0, zero: 0, downOne: 0, downTwo: 0]

	/**
	 * Create a sprint data object for a sprint.
	 */
	SprintData(Sprint sprint) {
		this.sprint = sprint
		/* fetch data and calculate measurements */
		collectData()
	}

	/**
	 * Return the sum of the number of commits (all open, merged and abandoned commits) during the sprint.
	 */
	int getTotalNumberOfCommits() {
		return commits.merged+commits.open+commits.abandoned
	}

	/**
	 * Return the average number of comments per each commit during the sprint.
	 */
	double getAverageNumberOfCommentsPerCommit() {
		return (double)totalNumberOfComments/(totalNumberOfCommits ?: 1) /* avoid division by 0 */
	}
	
	/**
	 * Return the average number of patch sets per each commit during the sprint.
	 */
	double getAverageNumberOfPatchSetsPerCommit() {
		return (double)totalNumberOfPatchSets/(totalNumberOfCommits ?: 1) /* avoid division by 0 */
	}
	
	/**
	 * Return the shortest time from a change has been uploaded until it has been merged.
	 */
	int getMinTimeUntilMerge() {
		/* get the shortest merge time (or 0 if no merges) */
		return mergeTimes.min() ?: 0
	}

	/**
	 * Return the longest time from a change has been uploaded until it has been merged.
	 */
	int getMaxTimeUntilMerge() {
		/* get the longest merge time (or 0 if no merges) */
		return mergeTimes.max() ?: 0
	}

	/**
	 * Return the average time from a change has been uploaded until it has been merged.
	 */
	double getAverageTimeUntilMerge() {
		/* get the average merge time (or 0 if no merges) */
		return (double)(mergeTimes.sum() ?: 0)/(commits.merged ?: 1) /* avoid division by 0 */
	}
	
	/**
	 * Collect data for the sprint from the Gerrit API.
	 */
	private collectData() {
		/* fetch changes from API and calculate measurements */
		readData(gerritService.fetchSprintChanges(sprint, fileChanges: true, codeReviews: true))
	}

	/**
	 * Process the data from a list of changes from the Gerrit API.
	 */
	private void readData(List changes) {
		changes.each { change ->
			processStatus(change.status)
			processComments(change.messages, change.createdDate)
			processFiles(change.fileChanges)
			processCodeReviews(change.codeReviews)
			processRevisions(change.revisions)
		}
	}
	
	private void processRevisions(List revisions) {
		/* add the total number of revisions for the change (e.g. the maximum revision number for the change) */
		totalNumberOfPatchSets += revisions*.number.max()
	}
	
	private void processCodeReviews(List codeReviews) {
		for (CodeReview cr : codeReviews) {
			switch (cr.value) {
				case 2:
					reviewValues.upTwo++
					break
				case 1:
					reviewValues.upOne++
					break
				case 0:
					reviewValues.zero++
					break
				case -1:
					reviewValues.downOne++
					break
				case -2:
					reviewValues.downTwo++
					break
			}
		}
	}

	private void processComments(List messages, Date createdDate) {
		messages.each { message ->
			if(isWithinSprintDates(message.createdDate)) {
				
				if(message.message.matches(/^Change has been successfully merged into the git repository.$/)) {
					use(groovy.time.TimeCategory) {
						/* FIXME: GerritUtils has a minutesBetween() method, could use this (move to DateService) */
						def duration = message.createdDate - createdDate
						mergeTimes << (duration.days * 1440) + (duration.hours * 60) + duration.minutes + Math.round(duration.seconds/60)
					}
				} 
				// Gerrit generated comments doesn't have an author it seems.
				else if(message.authorName != null) {
					totalNumberOfComments++
				}
			}
		}
	}
	
	private void processFiles(List fileChanges) {
		/* add the total number of added lines of the changed files */
		lines.inserted += fileChanges*.linesInserted.sum() ?: 0
		/* add the total number of deleted lines of the changed files */
		lines.deleted += fileChanges*.linesDeleted.sum() ?: 0
	}

	private void processStatus(String status) {
		switch (status) {
			case "MERGED":
				commits.merged++
				break
			case "NEW":
				commits.open++
				break
			case "ABANDONED":
				commits.abandoned++
				break
		}
	}
	
	
	/**
	 * Return true if the date was between start and end date or on one of them.
	 */
	private boolean isWithinSprintDates(Date date) {
		return (date.after(sprint.startDate) || date.equals(sprint.startDate)) && (date.before(sprint.actualEndDate()) || date.equals(sprint.actualEndDate()))
	}
}
