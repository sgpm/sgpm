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

class HighchartsTagLib {
	/* prefix tags with namespace hc */
	static namespace = 'hc'
	/* A transformer for turning a map of attributes from a tag into an Option attribute. */
	static final optionsTransformer = MapTransformer.create(targetAlias: 'options') {
		/* set option value with the provided path by default */
		defaultAction = { path -> options."$path" = value }

		/* map from parameter name to path in options object */
		type				'chart.type'
		title				'title.text'
		subtitle			'subtitle.text'
		xAxisTitle			'xAxis.title.text'
		xAxisCategories		'xAxis.categories'
		xAxisLabelsEnabled	'xAxis.labels.enabled'
		yAxisCategories		'yAxis.categories'
		yAxisTitle			'yAxis.title.text'
		yAxisLabelsEnabled	'yAxis.labels.enabled'
		colors				'colors'
		columnStacking		'plotOptions.column.stacking'
		reversedLegend		'legend.reversed'
		barStacking			'plotOptions.bar.stacking'

		/* map from series map to options object for every series */
		series {
			if (value instanceof Map) {
				/* convert every entry in the map (name to data) to an options object */
				options.series = value.collect { name, data ->
					Options.create(name: name, data: data)
				}
			} else if (value instanceof Collection) {
				/* convert every element in the collection to an options object */
				options.series = value.collect { series -> Options.create(series)  }
			}
		}
	}

	/**
	 * Create a chart for a div.
	 * 
	 * See Highcharts API for more detailed description.
	 * @attr id REQUIRED ID of the element to place the chart in.
	 * @attr options A Options object with default options for the chart.
	 * @attr type The chart type.
	 * @attr title The title.
	 * @attr subtitle The subtitle.
	 * @attr xAxisCategories Categories on the X axis.
	 * @attr xAxisTitle Title of the X axis.
	 * @attr xAxisLabelsAreEnabled Whether to show labels on the X axis.
	 * @attr yAxisCategories Categories on the Y axis.
	 * @attr yAxisTitle Title of the Y axis.
	 * @attr xAxisLabelsAreEnabled Whether to show labels on the X axis.
	 * @attr colors Default colors used by the series.
	 * @attr columnStacking Stacking option for column charts (normal or percent).
	 * @attr barStacking Stacking option for bar charts (normal or percent).
	 * @attr series A map with name and data of the series.
	 */
	def chart = { attrs ->
		def id = attrs.id
		def options = makeOptions(attrs)

		/* add javascript for the chart */
		r.script(disposition: 'defer') { "jQuery(function() { jQuery('#$id').highcharts(${options.toJson()}); });" }
		/* require highcharts resources */
		requireHighcharts()
	}

	/**
	 * Set global chart options.
	 * 
	 * See Highcharts API for more detailed description.
	 * @attr options A Options object with default options for the chart.
	 * @attr type The chart type.
	 * @attr title The title.
	 * @attr subtitle The subtitle.
	 * @attr xAxisCategories Categories on the X axis.
	 * @attr xAxisTitle Title of the X axis.
	 * @attr xAxisLabelsAreEnabled Whether to show labels on the X axis.
	 * @attr yAxisCategories Categories on the Y axis.
	 * @attr yAxisTitle Title of the Y axis.
	 * @attr xAxisLabelsAreEnabled Whether to show labels on the X axis.
	 * @attr colors Default colors used by the series.
	 * @attr columnStacking Stacking option for column charts (normal or percent).
	 * @attr barStacking Stacking option for bar charts (normal or percent).
	 * @attr series A map with name and data of the series.
	 */
	def options = { attrs ->
		def options = makeOptions(attrs)

		r.script(disposition: 'defer') { "jQuery(function() { Highcharts.setOptions(${options.toJson()}); });" }
		/* require highcharts resources */
		requireHighcharts()
	}

	/**
	 * Create an options object from a parameter list.
	 */
	private makeOptions(Map params) {
		/* transform parameters onto the options (options from params or empty options object) */
		optionsTransformer.transform(params, params.options ?: new Options())
	}

	/**
	 * Require highcharts module.
	 */
	private requireHighcharts() {
		r.require(module: 'highcharts')
	}
}
