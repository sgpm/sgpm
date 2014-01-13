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

import sgpm.GerritService
import sgpm.Sprint

/**
 * An ordered collection of sprints.
 * 
 * Used for creating a collection of sprints to be visualized.
 */
class SprintDataCollection {
	/** Date format for the X axis. */
	private final static X_AXIS_DATE_FORMAT = "dd/MM"

	/** The sprints. */
	private final List<SprintData> sprintData
	
	/**
	 * Create a sprint data collection from a collection of sprints.
	 */
	SprintDataCollection(Collection sprintData) {
		/* make a copy of the sprint list */
		this.sprintData = new ArrayList<SprintData>(sprintData)
	}
	
	/**
	 * Create a sprint data collection for a range of sprints.
	 * Parameters in params:
	 * * team: the team owning the sprints (required).
	 * * start: the ID of the start sprint (required).
	 * * end: the ID of the end sprint (required).
	 */
	static SprintDataCollection forSprints(Map params) {
		/* create a sprint data object for every sprint between the start and end sprint */
		return forSprints(Sprint.sprintsBetween(params.team, params.start, params.end))
	}
	
	/**
	 * Create a sprint data collection with a list of sprints.
	 */
	static SprintDataCollection forSprints(List sprints) {
		/* create a sprint data object for every sprint in the list */
		return new SprintDataCollection(sprints.collect { sprint -> new SprintData(sprint) })
	}
	
	/**
	 * Return the sprintData.
	 */
	List<SprintData> getSprintData() {
		return Collections.unmodifiableList(sprintData)
	}
	
	/**
	 * Create a map of data used by charts in views.
	 */
	Map getCharts() {
		def charts = [:]
		
		/* create series data from all the sprints */
		charts.reviews = collectChartSeries('reviewValues', downTwo: '-2', downOne: '-1', upOne: '+1', upTwo: '+2')
		charts.commits = collectChartSeries('commits', open: 'Open', merged: 'Merged', abandoned: 'Abandoned')
		charts.comments = collectChartSeries(totalNumberOfComments: 'Comments')
		charts.lines = collectChartSeries('lines', inserted: 'Lines inserted', deleted: 'Lines deleted')
		charts.avgPatchSetsPerCommit = collectChartSeries(averageNumberOfPatchSetsPerCommit: 'Average')
		charts.avgCommentsPerCommit = collectChartSeries(averageNumberOfCommentsPerCommit: 'Average')
		charts.mergeTimes = collectChartSeries(minTimeUntilMerge: 'Min time (minutes)', averageTimeUntilMerge: 'Average time (minutes)', maxTimeUntilMerge: 'Max time (minutes)')

		return charts
	}
	
	/**
	 * Create a list of categories for the X axis of the charts
	 */
	List getCategories() {
		/* format each start date for each sprint */
		sprintData*.sprint.collect { sprint -> "${sprint.startDate.format(X_AXIS_DATE_FORMAT)}" }
	}
	
	/**
	 * Collect all series for a chart with data from all sprints.
	 * 
	 * Takes a string of the name of the map in the sprint data to
	 * gather the series data from and a map from the properties
	 * in the map in the sprint data to the labels used in the series.
	 * 
	 * If the name is null properties of the sprint data itself will
	 * be used.
	 */
	private collectChartSeries(Map labeling, String name=null) {
		/* go through all the keys in the mapping and collect the data for each key */
		return labeling.collectEntries { key, label ->
			/* go through all sprints and add the values from them */
			[label, sprintData.collect { sprint -> (name ? sprint[name] : sprint)[key] } ]
		}
	}
}
