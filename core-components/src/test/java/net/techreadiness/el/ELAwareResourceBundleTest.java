package net.techreadiness.el;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import net.techreadiness.text.ELAwareResourceBundle;

import org.junit.Before;
import org.junit.Test;

public class ELAwareResourceBundleTest {
	private ResourceBundle wrapped;
	private ResourceBundle bundleUnderTest;
	private Properties props;

	@Before
	public void setup() throws IOException {
		wrapped = ResourceBundle.getBundle("net.techreadiness.el.ELAwareResourceBundleTest");
		bundleUnderTest = new ELAwareResourceBundle(wrapped);
		props = new Properties();
		try (InputStream is = getClass().getResourceAsStream("/net/techreadiness/el/ELAwareResourceBundleTest.properties")) {
			props.load(is);
		}
	}

	@Test
	public void test1() {
		String key = "test1";
		String value = bundleUnderTest.getString(key);
		assertEquals("message", value, props.getProperty(key));
	}

	@Test
	public void test2() {
		String key = "test2";
		String value = bundleUnderTest.getString(key);
		assertEquals("message", value, props.getProperty(key));
	}

	@Test
	public void test3() {
		String value = bundleUnderTest.getString("test3");
		assertEquals("message", value, props.getProperty("test1"));
	}

	@Test
	public void test4() {
		String value = bundleUnderTest.getString("test4");
		assertEquals("message", value, props.getProperty("test1") + " " + props.getProperty("test2"));
	}

	@Test
	public void test5() {
		String value = bundleUnderTest.getString("test5");
		assertEquals("message", value, props.getProperty("test5"));
	}

	@Test
	public void testIndirectReference() {
		String value = bundleUnderTest.getString("core.indirectReference");
		assertEquals("message", value, props.getProperty("core.nestedTest"));
	}

	@Test(expected = MissingResourceException.class)
	public void testPropertyNotInBundle() {
		bundleUnderTest.getString("testPropertyNotInBundle");
	}
}
