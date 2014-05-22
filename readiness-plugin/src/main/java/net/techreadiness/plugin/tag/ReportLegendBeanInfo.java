package net.techreadiness.plugin.tag;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReportLegendBeanInfo extends SimpleBeanInfo {

	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		List<PropertyDescriptor> descriptors = new ArrayList<>();

		Method getter;
		try {
			getter = ReportLegend.class.getMethod("getCssClass");
			Method setter = ReportLegend.class.getMethod("setCssClass", String.class);
			descriptors.add(new PropertyDescriptor("class", getter, setter));
			descriptors.add(new PropertyDescriptor("title", ReportLegend.class));
			descriptors.add(new PropertyDescriptor("style", ReportLegend.class));
			return descriptors.toArray(new PropertyDescriptor[descriptors.size()]);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
	}
}
