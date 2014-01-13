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

import static GerritUtils.parseDateString

/**
 * A change in a project fetched from the API.
 */
class GerritChange {
	/** The change ID. */
	final String id
	/** The name of the project of the change. */
	final String projectName
	/** The owner name. */
	final String ownerName
	/** The owner email. */
	final String ownerEmail
	/** The commit message. */
	final String subject
	/** The change status. */
	final String status
	/** The creation date. */
	final Date createdDate
	/** The modified date. */
	final Date updatedDate
	/** The change messages. */
	final List messages
	/** The revisions. */
	final List revisions
	/** The code review values */
	final List codeReviews

	/**
	 * Create a change from JSON data.
	 */
	GerritChange(Map data) {
		id = data.id
		projectName = data.project
		ownerName = data.owner.name
		ownerEmail = data.owner.email
		subject = data.subject
		status = data.status
		createdDate = parseDateString(data.created)
		updatedDate = parseDateString(data.updated)
		/* extract change messages (if available) */
		messages = data.messages?.collect { messageData ->
			new Message(messageData)
		}
		/* extract revision data (if available) */
		revisions = data.revisions?.collect { commitId, revisionData ->
			new Revision(revisionData)
		}
		
		/* extract code review data (if available) */
		if (data.labels) {
			codeReviews = []
			/* get verifications */
			codeReviews += data.labels.Verfied?.all?.collect { reviewData ->
				new CodeReview(reviewData, CodeReview.LabelType.VERIFIED)
			} ?: []
			/* get code reviews */
			codeReviews += data.labels?.'Code-Review'?.all?.collect { reviewData ->
				new CodeReview(reviewData, CodeReview.LabelType.CODE_REVIEW)
			} ?: []
		}
	}

	/**
	 * Get all file changes from all revisions.
	 */
	List getFileChanges() {
		/* concatenate the lists of file changes from all revisions */
		revisions*.fileChanges?.sum() ?: [] /* default to empty list */
	}

	/**
	 * Return String representation.
	 */
	String toString() {
		/* represent by id */
		subject
	}

	static class Message {
		/** The message ID. */
		final String id
		/** The message text. */
		final String message
		/** The author name. */
		final String authorName
		/** The author email. */
		final String authorEmail
		/** The creation date. */
		final Date createdDate
	
		/**
		 * Create a message from JSON data.
		 */
		Message(Map data) {
			id = data.id
			message = data.message
			authorName = data.author?.name
			authorEmail = data.author?.email
			createdDate = parseDateString(data.date)
		}
	}
	
	static class Revision {
		/** The patch set number. */
		final int number
		/** A list of all file changes (maps containing the keys name, linesInserted and linesDeleted). */
		final List fileChanges
	
		/**
		 * Create a revision from JSON data.
		 */
		Revision(Map data) {
			number = data._number
			/* create a list of file changes and the number of changed lines */
			fileChanges = data.files?.collect { fileName, fileData ->
				[
					name: fileName,
					linesInserted: fileData.lines_inserted ?: 0, /* default to 0 */
					linesDeleted: fileData.lines_deleted ?: 0 /* default to 0 */
				]
			} ?: [] /* default to empty list (if revision has no file changes) */
		}
	}
	
	static class CodeReview {
		/** Types of reviews. */
		enum LabelType { CODE_REVIEW, VERIFIED }
	
		/** The code review value (-2, -1, 0, 1, 2). */
		final int value
		/** The date of the review. */
		final Date createdDate
		
		/**
		 * Create a code review from JSON data.
		 */
		CodeReview(Map data, LabelType type) {
			value = data.value
			createdDate = parseDateString(data.date)
		}
	}
}