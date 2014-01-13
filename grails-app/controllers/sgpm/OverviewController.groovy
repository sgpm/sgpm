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

import sgpm.measurement.SprintDataCollection
import sgpm.chart.Options

class OverviewController {
	static defaultAction = 'overview'
	
	/** Options for all charts at the overview page. */
	private static CHART_OPTIONS = Options.create {
		/* chibi charts ^_^ */
		chart.height = 200
		title.align = 'left'
		/* don't show a credit link */
		credits.enabled = false
		
		xAxis {
			title.text = "Start of Sprint"
			/* alternating background color */
			alternateGridColor = '#fafafa'
		}

		yAxis {
			/* hide grid and labels on the Y axis */
			labels.enabled = false
			title.text = null
			gridLineWidth = 0
		}
		
		legend {
			/* show legend to the right in the middle */
			align = 'right'
			layout = 'vertical'
			verticalAlign = 'middle'
			/* make the width constant and add some space */
			width = 155
			margin = 40
			borderWidth = 0
		}
	}
	
	/**
	 * The overview page.
	 * 
	 * This page lets the user select and visualize sprints.
	 */
	def overview() {
		def team = Team.findByUrl(params.id)
		
		if (team == null) {
			response.sendError(404)
		}
		
		/* get all sprints belonging to the team */
		def sprints = Sprint.allSprints(team)
		/* remove future sprints */
		def today = new Date()
		sprints.removeAll { sprint -> sprint.startDate.after(today) }
		
		/* check if the user has selected a range of sprints to visualize */
		if (params.startSprint && params.endSprint) {
			/* get data for all sprints between the start and end sprint */
			def data = SprintDataCollection.forSprints(team: team, start: params.startSprint, end: params.endSprint)

			return [sprints: sprints, data: data, chartOptions: CHART_OPTIONS]
		} else {
			return [sprints: sprints]
		}
	}

}
