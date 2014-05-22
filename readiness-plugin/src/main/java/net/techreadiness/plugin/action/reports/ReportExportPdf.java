package net.techreadiness.plugin.action.reports;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseTextField;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.beanutils.BeanUtils;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.conditionalStyle.ConditionalStyle;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ReportExportPdf implements ReportExport {

	protected final Style detailStyle;
	protected final Style detailOddStyle;
	protected final Style headerStyle;

	protected List<String> columnLabels;
	protected List<String> columnKeys;
	protected List<Boolean> useConditionalStyles;
	protected Collection<Map<String, String>> data;
	protected Map<String, String> params;
	protected String template;

	protected List<ConditionalStyle> conditionalStyles;
	protected boolean suppressConditionalStyles;
	protected String legendTitle;

	public static class Builder {
		private final Map<String, String> params = Maps.newHashMap();
		private final List<String> columnLabels;
		private final List<String> columnKeys;
		private List<Boolean> useConditionalStyles;
		private final Collection<Map<String, String>> data;

		public Builder(List<String> columnLabels, List<String> columnKeys, Collection<Map<String, String>> data) {
			this.columnLabels = columnLabels;
			this.columnKeys = columnKeys;
			this.data = data;
		}

		public Builder(List<String> columnLabels, List<String> columnKeys, List<Boolean> useConditionalStyles,
				Collection<Map<String, String>> data) {
			this.columnLabels = columnLabels;
			this.columnKeys = columnKeys;
			this.useConditionalStyles = useConditionalStyles;
			this.data = data;
		}

		public Builder title(String value) {
			params.put("title", value);
			return this;
		}

		public Builder subtitle(String value) {
			params.put("subtitle", value);
			return this;
		}

		public Builder consortium(String value) {
			params.put("consortium", value);
			return this;
		}

		public Builder aggregationMessage(String value) {
			params.put("aggregationMessage", value);
			return this;
		}

		public ReportExportPdf build() {
			return new ReportExportPdf(this);
		}

	}

	private ReportExportPdf(Builder builder) {
		columnKeys = builder.columnKeys;
		columnLabels = builder.columnLabels;
		useConditionalStyles = builder.useConditionalStyles;
		data = builder.data;
		params = builder.params;

		headerStyle = new StyleBuilder(false, "headerStyle").setBackgroundColor(new Color(238, 239, 239))
				.setPaddingBottom(4).setFont(new Font(8, "SansSerif", true)).setTextColor(new Color(88, 88, 90))
				.setVerticalAlign(VerticalAlign.TOP).setHorizontalAlign(HorizontalAlign.LEFT)
				.setBorderBottom(Border.PEN_2_POINT).setBorderColor(new Color(222, 223, 224)).setTransparent(false).build();

		detailStyle = new StyleBuilder(false, "detailStyle").setBackgroundColor(Color.WHITE)
				.setFont(new Font(8, "SansSerif", false)).setVerticalAlign(VerticalAlign.MIDDLE)
				.setHorizontalAlign(HorizontalAlign.LEFT).setPaddingLeft(2).setBorder(Border.NO_BORDER).build();

		detailOddStyle = new StyleBuilder(false, "detailOddStyle").setBackgroundColor(new Color(247, 248, 248)).build();
	}

	public void setMapUrl(String value) {
		params.put("mapUrl", value);
	}

	public void setDCMapUrl(String value) {
		params.put("dcMapUrl", value);
	}

	public void setTemplate(String value) {
		template = value;
	}

	public List<ConditionalStyle> getConditionalStyles() {
		return conditionalStyles;
	}

	public void setConditionalStyles(List<ConditionalStyle> conditionalStyles) {
		this.conditionalStyles = conditionalStyles;
	}

	public Style getDetailStyle() {
		return detailStyle;
	}

	public void setSuppressConditionalStyles(boolean suppressConditionalStyles) {
		this.suppressConditionalStyles = suppressConditionalStyles;
	}

	public void setLegendTitle(String legendTitle) {
		params.put("legendTitle", legendTitle);
	}

	@Override
	public byte[] getReport() throws Exception {

		if (data == null || columnKeys == null) {
			throw new Exception("No data to report.");
		}

		// build report
		DynamicReport report = buildReport();
		// build reporting data source

		// fill report
		JasperReport jasperReport = DynamicJasperHelper.generateJasperReport(report, new ClassicLayoutManager(), params);
		adjustTextMarkup(jasperReport);

		if (data == null || data.isEmpty()) {
			data = Lists.newArrayList();
			if (columnKeys.size() > 0) {
				Map<String, String> noData = Maps.newHashMap();
				noData.put(columnKeys.get(0), "No Results");
				data.add(noData);
			}
		}

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRMapCollectionDataSource(data));

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

	public void adjustTextMarkup(JasperReport jr) {
		JRSection detailSection = jr.getDetailSection();
		for (JRBand band : detailSection.getBands()) {
			if (band.getElements().length > 0) {
				JRBaseTextField text = (JRBaseTextField) band.getElements()[0];
				text.setBold(true);
				for (JRElement element : band.getElements()) {
					JRBaseTextField column = (JRBaseTextField) element;
					column.setMarkup(JRCommonText.MARKUP_HTML);
				}
			}
		}
	}

	private DynamicReport buildReport() throws Exception {

		FastReportBuilder report = new FastReportBuilder();
		report.setPrintBackgroundOnOddRows(true).setOddRowBackgroundStyle(detailOddStyle)
		.setDefaultStyles(new Style(), new Style(), headerStyle, detailStyle).setUseFullPageWidth(true)
		.setDetailHeight(48).setHeaderHeight(20);

		if (useConditionalStyles == null || useConditionalStyles.isEmpty()) {
			for (int i = 0; i < columnKeys.size() - 1; i++) {
				report.addColumn(columnLabels.get(i), columnKeys.get(i), String.class.getName(), 50);
			}

			AbstractColumn columnAmount;
			if (!suppressConditionalStyles) {
				columnAmount = ColumnBuilder
						.getNew()
						.setColumnProperty(columnKeys.get(columnKeys.size() - 1), String.class.getName())
						.setTitle(columnLabels.get(columnLabels.size() - 1))
						.setWidth(50)
						.addConditionalStyles(
								conditionalStyles != null ? conditionalStyles : createConditionalStyles(detailStyle))
								.build();
			} else {
				columnAmount = ColumnBuilder.getNew()
						.setColumnProperty(columnKeys.get(columnKeys.size() - 1), String.class.getName())
						.setTitle(columnLabels.get(columnLabels.size() - 1)).setWidth(50).build();
			}

			report.addColumn(columnAmount);
		} else {
			// this for loop is used if you use the "useConditionalStyles" list
			// normally, the last column is the only one to use conditional styles
			// but the progress report needed multiple
			for (int i = 0; i < columnKeys.size(); i++) {
				if (useConditionalStyles.get(i)) {
					AbstractColumn columnAmount = ColumnBuilder
							.getNew()
							.setColumnProperty(columnKeys.get(i), String.class.getName())
							.setTitle(columnLabels.get(i))
							.setWidth(50)
							.addConditionalStyles(
									conditionalStyles != null ? conditionalStyles : createConditionalStyles(detailStyle))
									.build();
					report.addColumn(columnAmount);
				} else {
					report.addColumn(columnLabels.get(i), columnKeys.get(i), String.class.getName(), 50);
				}
			}
		}

		if (template != null) {
			report.setTemplateFile(template);
		} else if (params.get("mapUrl") != null) {
			report.setTemplateFile("reports/wrapper-map.jrxml");
		} else {
			report.setTemplateFile("reports/wrapper.jrxml");
		}

		return report.build();
	}

	private static List<ConditionalStyle> createConditionalStyles(Style baseStyle) throws IllegalAccessException,
	InstantiationException, InvocationTargetException, NoSuchMethodException {
		Style reportLevel1 = (Style) BeanUtils.cloneBean(baseStyle);
		reportLevel1.setBackgroundColor(new Color(184, 31, 75));
		reportLevel1.setTextColor(Color.white);
		reportLevel1.setTransparent(false);
		Style reportLevel2 = (Style) BeanUtils.cloneBean(baseStyle);
		reportLevel2.setBackgroundColor(new Color(246, 133, 31));
		reportLevel2.setTransparent(false);
		Style reportLevel3 = (Style) BeanUtils.cloneBean(baseStyle);
		reportLevel3.setBackgroundColor(new Color(252, 185, 19));
		reportLevel3.setTransparent(false);
		Style reportLevel4 = (Style) BeanUtils.cloneBean(baseStyle);
		reportLevel4.setBackgroundColor(new Color(0, 161, 94));
		reportLevel4.setTransparent(false);

		ReportPdfHighlightCondition status1 = new ReportPdfHighlightCondition(0, .25);
		ReportPdfHighlightCondition status2 = new ReportPdfHighlightCondition(.26, .5);
		ReportPdfHighlightCondition status3 = new ReportPdfHighlightCondition(.51, .75);
		ReportPdfHighlightCondition status4 = new ReportPdfHighlightCondition(.76, 1);
		ReportPdfBooleanCondition statusYes = new ReportPdfBooleanCondition("Yes");
		ReportPdfBooleanCondition statusNo = new ReportPdfBooleanCondition("No");

		List<ConditionalStyle> conditionalStyles = Lists.newArrayList();
		conditionalStyles.add(new ConditionalStyle(status1, reportLevel1));
		conditionalStyles.add(new ConditionalStyle(status2, reportLevel2));
		conditionalStyles.add(new ConditionalStyle(status3, reportLevel3));
		conditionalStyles.add(new ConditionalStyle(status4, reportLevel4));
		conditionalStyles.add(new ConditionalStyle(statusYes, reportLevel4));
		conditionalStyles.add(new ConditionalStyle(statusNo, reportLevel1));

		return conditionalStyles;
	}

}
