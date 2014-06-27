package net.techreadiness.plugin.action.reports;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

/**
 * Wrapper class around the Google Visualization GeoChart.
 *
 * For this Bean to be used, you must include the following in the <head> tag on your page:
 * {@code <script type='text/javascript' src='https://www.google.com/jsapi'></script>}
 *
 * You will also need to add a div to the page where the chart will go, like
 * this: <div id='mapDiv'></div>
 *
 * There is an option onSelect event that can be captured. You can set the
 * onSelectMethodName in this bean. Then you would define that method on the
 * page. Use code similar to this to view the selection: var selection =
 * geoChart.getSelection(); alert('You selected ' +
 * geoData.getRowProperty(selection[0].row, 'stateCode'));
 *
 * geoChart is the variable name of the chart object and geoData is the name of
 * the DataTable used in the chart.
 *
 */
public class GeoChartBean {

	private static final String GOOGLE_URL = "http://chart.apis.google.com/chart?";

	protected String chartDiv;
	protected int width;
	protected int height;

	protected String hoverLabel;

	protected String backgroundColor;
	protected List<String> colors;
	protected String datalessRegionColor;

	protected List<Data> data;

	protected String onSelectMethodName;

	protected boolean legend;

	private static final List<String> ST_WHITE_LIST = Lists.newArrayList("AL",
			"AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID",
			"IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN",
			"MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND",
			"OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT",
			"VA", "WA", "WV", "WI", "WY");

	public GeoChartBean() {
		width = 1060;
		height = 650;
		chartDiv = "mapDiv";
		backgroundColor = "#FFFFFF";
		colors = new ArrayList<>();
		addColor("#b81f4b", "#f6851f", "#fcb913", "#00a15e", "#9a9a9a");
		datalessRegionColor = "#E5E5E5";
		data = new ArrayList<>();
		onSelectMethodName = "geoChartSelectHandler";
	}

	public String getChartDiv() {
		return chartDiv;
	}

	/**
	 * This is the id of the element the chart will be placed in. Defaults to
	 * "mapDiv"
	 * 
	 * @param chartDiv
	 *            HTML id attribute of the element the map should be created in.
	 *
	 */
	public void setChartDiv(String chartDiv) {
		this.chartDiv = chartDiv;
	}

	public int getWidth() {
		return width;
	}

	/**
	 * The width of the chart in px
	 * 
	 * @param width
	 *            Width of the chart in pixels.
	 *
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * The height of the chart in px
	 * 
	 * @param height
	 *            Height of the chart in pixels.
	 *
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * The background color for the chart. Must be in the format "#RRGGBB"
	 * Defaults to "#FFFFFF"
	 * 
	 * @param backgroundColor
	 *            Hex value of the background color.
	 *
	 */
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public List<String> getColors() {
		return colors;
	}

	/**
	 * Adds colors to be used in the gradient. Calling this method also resets
	 * old color assignment. Colors must be in the format "#RRGGBB"
	 * 
	 * @param colors
	 *            Hex color values to be used for the chart.
	 *
	 */
	public void addColor(String... colors) {
		this.colors = new ArrayList<>();
		for (String color : colors) {
			this.colors.add(color);
		}
	}

	public void setColors(List<String> colors) {
		this.colors = colors;
	}

	public String getDatalessRegionColor() {
		return datalessRegionColor;
	}

	public String getHoverLabel() {
		return hoverLabel;
	}

	/**
	 * Used to set the label on the hover-over
	 * 
	 * @param hoverLabel
	 *            Label the chart should display.
	 *
	 */
	public void setHoverLabel(String hoverLabel) {
		this.hoverLabel = hoverLabel;
	}

	/**
	 * The color that the chart assigns to the regions not specified in the
	 * data. Must be in the format "#RRGGBB" Defaults to "#E5E5E5"
	 * 
	 * @param datalessRegionColor
	 *            Hex color value for regions that do not have data.
	 *
	 */
	public void setDatalessRegionColor(String datalessRegionColor) {
		this.datalessRegionColor = datalessRegionColor;
	}

	public List<Data> getData() {
		return data;
	}

	public void addData(String id, String state, String code, int data) {
		Data d = new Data();
		d.id = id;
		d.state = state;
		d.code = code;
		d.data = data;
		this.data.add(d);
	}

	public void addBooleanData(String id, String state, String code,
			String label) {
		Data d = new Data();
		d.id = id;
		d.state = state;
		d.code = code;
		d.data = 1;
		d.formattedValue = label;
		data.add(d);
	}

	/**
	 * Adds data to be used in the chart.
	 * 
	 * @param id
	 *            Identifier for the piece of data.
	 *
	 * @param state
	 *            Name of the state, ex. Iowa, Ohio, Texas
	 * @param code
	 *            Name of the state's code, ex. IA, OH, TX
	 * @param data
	 *            The data that you are trying to plot
	 */
	public void addPercentData(String id, String state, String code, String data) {
		try {
			Data d = new Data();
			d.id = id;
			d.code = code;
			d.state = state;
			if ("TBD".equalsIgnoreCase(data)) {
				d.data = getData(-1);
				d.formattedValue = "TBD";
			} else {
				if ("".equalsIgnoreCase(data)) {
					d.data = getData(-1);
					d.formattedValue = "";
				} else {
					d.data = getData(Long
							.valueOf(StringUtils.remove(data, '%')));
					d.formattedValue = data;
				}
			}
			this.data.add(d);
		} catch (Exception e) {
			// Ignore error
		}
	}

	/**
	 * Adds data to be used in the chart.
	 *
	 * @param id
	 *            Id of the organization for the data
	 * @param state
	 *            Name of the state, ex. Iowa, Ohio, Texas
	 * @param code
	 *            Name of the state's code, ex. IA, OH, TX
	 * @param data
	 *            The data that you are trying to plot
	 */
	public void addSurveyData(String id, String state, String code, String data) {
		try {
			Data d = new Data();
			d.id = id;
			d.code = code;
			d.state = state;
			if ("TBD".equals(data)) {
				d.data = -1;
				d.formattedValue = "TBD";
			} else if ("(missing)".equals(data)) {
				d.data = getSurveyData(11);
				d.formattedValue = "(missing)";
			} else {
				d.data = getSurveyData(Double.parseDouble(data));
				d.formattedValue = data;
			}
			this.data.add(d);
		} catch (Exception e) {
			// Ignore error
		}
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public String getOnSelectMethodName() {
		return onSelectMethodName;
	}

	/**
	 * The name of the function called when a region is selected. To disable
	 * this functionality call onSelectMethodName(null). This method name
	 * defaults to "geoChartSelectHandler"
	 * @param onSelectMethodName JavaScript function name that will be called when a region is clicked.
	 *
	 */
	public void setOnSelectMethodName(String onSelectMethodName) {
		this.onSelectMethodName = onSelectMethodName;
	}

	public boolean isLegend() {
		return legend;
	}

	/**
	 * If true a legend is rendered with the chart. Default is true.
	 *
	 * @param legend
	 *            New value for the legend property.
	 */
	public void setLegend(boolean legend) {
		this.legend = legend;
	}

	/**
	 * You will call this method on the page. It builds the JavaScript needed
	 * for the chart. It will create its own <script> tags.
	 *
	 * @return JavaScript that loads the Google GeoChart.
	 */
	public String getChart() {
		final String LB = "\r\n";
		StringBuilder sb = new StringBuilder(512);
		sb.append("var geoChart, geoData;").append(LB);
		sb.append(
				"google.load('visualization', '1', {'packages': ['geochart']});")
				.append(LB);
		sb.append("google.setOnLoadCallback(drawMap);").append(LB).append(LB);

		sb.append("function drawMap() {").append(LB);
		sb.append("geoData = new google.visualization.DataTable();").append(LB);
		sb.append("geoData.addRows(").append(data.size()).append(");")
				.append(LB);
		sb.append("geoData.addColumn('string', 'State');").append(LB);
		sb.append(
				"geoData.addColumn('number', '"
						+ (hoverLabel == null ? "Percent" : hoverLabel) + "');")
				.append(LB);

		int count = 0;
		for (Data data : this.data) {
			sb.append("geoData.setValue(").append(count).append(", 0, '")
					.append(data.state).append("');").append(LB);
			sb.append("geoData.setValue(").append(count).append(", 1, ")
					.append(data.data).append(");").append(LB);
			sb.append("geoData.setFormattedValue(").append(count)
					.append(", 1, '").append(data.formattedValue).append("');")
					.append(LB);
			sb.append("geoData.setRowProperty(").append(count)
					.append(", 'stateCode', '").append(data.id).append("');")
					.append(LB);
			count++;
		}

		sb.append("var options = {};").append(LB);
		sb.append("options['region'] = 'US';").append(LB);
		sb.append("options['resolution'] = 'provinces';").append(LB);
		sb.append("options['colorAxis'] = { minValue : 0, maxValue : ")
				.append(colors.size() - 1).append(", colors : [");

		for (String color : colors) {
			sb.append("'").append(color).append("',");
		}
		if (colors.size() > 0) {
			sb.setLength(sb.length() - 1);
		}
		sb.append("]};").append(LB);
		sb.append("options['backgroundColor'] = '").append(backgroundColor)
				.append("';").append(LB);
		sb.append("options['datalessRegionColor'] = '")
				.append(datalessRegionColor).append("';").append(LB);
		sb.append("options['width'] = ").append(width).append(";").append(LB);
		sb.append("options['height'] = ").append(height).append(";").append(LB);
		if (!legend) {
			sb.append("options['legend'] = 'none';").append(LB);
		}
		sb.append(
				"geoChart = new google.visualization.GeoChart(document.getElementById('")
				.append(chartDiv).append("'));").append(LB);
		sb.append("geoChart.draw(geoData, options);").append(LB);
		if (onSelectMethodName != null && !onSelectMethodName.equals("")) {
			sb.append(
					"google.visualization.events.addListener(geoChart, 'select', ")
					.append(onSelectMethodName).append("); ").append(LB);
		}
		sb.append("}");

		return sb.toString();
	}

	public String getUrl() {
		StringBuilder sb = new StringBuilder(GOOGLE_URL);
		// append size
		sb.append("chs=").append(350).append("x").append(215);
		sb.append("&cht=t");
		// append colors
		if (colors != null && !colors.isEmpty()) {
			sb.append("&chco=")
					.append(datalessRegionColor.startsWith("#") ? datalessRegionColor
							.substring(1) : datalessRegionColor).append(",");
			for (String color : colors) {
				sb.append(color.startsWith("#") ? color.substring(1) : color)
						.append(",");
			}
			sb.setLength(sb.length() - 1);
		}
		// append data
		if (data != null && !data.isEmpty()) {
			sb.append("&chld=");
			for (Data data : this.data) {
				if (ST_WHITE_LIST.contains(data.code)) {
					sb.append(data.code);
				}
			}
			sb.append("&chd=t:");
			for (Data data : this.data) {
				if (ST_WHITE_LIST.contains(data.code)) {
					sb.append(data.data).append(",");
				}
			}
			sb.setLength(sb.length() - 1);
			if (colors.size() > 1) {
				sb.append("&chds=0,").append(colors.size() - 1);
			}
		}
		sb.append("&chtm=usa");
		return sb.toString();
	}

	public String getDCUrl() {
		String color = getDatalessRegionColor();
		for (Data d : data) {
			if (d.id.equalsIgnoreCase("DC")) {
				color = colors.get(getData(d.data));
				break;
			}
		}
		return GOOGLE_URL + "cht=map&chld=US-DC&chs=75x75&chco=FFFFFF|"
				+ (color.startsWith("#") ? color.substring(1) : color);
	}

	private static int getData(double data) {
		if (data < 0) {
			return 4;
		}
		if (data < 26) {
			return 0;
		}
		if (data < 51) {
			return 1;
		}
		if (data < 76) {
			return 2;
		}
		return 3;
	}

	private static int getSurveyData(double data) {
		if (data < 0) {
			return 4;
		}
		if (data <= 3) {
			return 3;
		}
		if (data <= 5) {
			return 2;
		}
		if (data <= 7) {
			return 1;
		}
		return 0;
	}

	private class Data {
		public String id;
		public String state;
		public String code;
		public int data;
		public String formattedValue;
	}

}
