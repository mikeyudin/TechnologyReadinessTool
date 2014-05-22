package net.techreadiness.text;

import java.util.List;

import javax.inject.Named;

import com.google.common.collect.Lists;

@Named
public class TestResourceBundleNameProvider implements ResourceBundleNameProvider {

	@Override
	public List<String> getBundleNames() {
		return Lists.newArrayList("net.techreadiness.text.test1", "test2");
	}

}
