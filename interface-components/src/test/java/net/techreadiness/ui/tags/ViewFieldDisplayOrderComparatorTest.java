package net.techreadiness.ui.tags;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Comparator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ViewFieldDisplayOrderComparatorTest {
	private static final Comparator<ViewFieldTag> comparator = new ViewFieldDisplayOrderComparator();

	private ViewFieldTag o1;
	private ViewFieldTag o2;

	@Before
	public void beforeTest() {
		o1 = mock(ViewFieldTag.class);
		o2 = mock(ViewFieldTag.class);
	}

	@Test
	public void testNullDisplayOrder() {
		when(o1.getDisplayOrder()).thenReturn(null);
		when(o2.getDisplayOrder()).thenReturn(null);

		when(o1.getPageOrder()).thenReturn(Integer.valueOf(1));
		when(o2.getPageOrder()).thenReturn(Integer.valueOf(2));

		int result = comparator.compare(o1, o2);
		Assert.assertEquals("If the display order is not specified then the page order spedifies the order.", -1, result);
	}

	@Test
	public void testDisplayOrderFirstAndNull() {
		when(o1.getDisplayOrder()).thenReturn("first");
		when(o2.getDisplayOrder()).thenReturn(null);

		when(o1.getPageOrder()).thenReturn(Integer.valueOf(1));
		when(o2.getPageOrder()).thenReturn(Integer.valueOf(2));

		int result = comparator.compare(o1, o2);
		Assert.assertEquals(
				"First and 'null' are equal.  If the display order is not specified then the page order spedifies the order.",
				-1, result);
	}

	@Test
	public void testDisplayOrderNullAndFirst() {
		when(o1.getDisplayOrder()).thenReturn(null);
		when(o2.getDisplayOrder()).thenReturn("first");

		when(o1.getPageOrder()).thenReturn(Integer.valueOf(1));
		when(o2.getPageOrder()).thenReturn(Integer.valueOf(2));

		int result = comparator.compare(o1, o2);
		Assert.assertEquals(
				"First and 'null' are equal.  If the display order is not specified then the page order spedifies the order.",
				-1, result);
	}

	@Test
	public void testDisplayOrderLastAndNull() {
		when(o1.getDisplayOrder()).thenReturn("last");
		when(o2.getDisplayOrder()).thenReturn(null);

		int result = comparator.compare(o1, o2);
		Assert.assertEquals("Null values should come before last values.", 1, result);
	}

	@Test
	public void testDisplayOrderNullAndLast() {
		when(o1.getDisplayOrder()).thenReturn(null);
		when(o2.getDisplayOrder()).thenReturn("last");

		int result = comparator.compare(o1, o2);
		Assert.assertEquals("Null values should come before last values.", -1, result);
	}

	@Test
	public void testDisplayOrderNullPageOrderNullAndInteger() {
		when(o1.getDisplayOrder()).thenReturn(null);
		when(o2.getDisplayOrder()).thenReturn(null);

		when(o1.getPageOrder()).thenReturn(null);
		when(o2.getPageOrder()).thenReturn(Integer.valueOf(2));

		int result = comparator.compare(o1, o2);
		Assert.assertEquals("Null page orders should come before integer values.", -1, result);
	}

	@Test
	public void testDisplayOrderNullPageOrderIntegerAndNull() {
		when(o1.getDisplayOrder()).thenReturn(null);
		when(o2.getDisplayOrder()).thenReturn(null);

		when(o1.getPageOrder()).thenReturn(Integer.valueOf(3));
		when(o2.getPageOrder()).thenReturn(null);

		int result = comparator.compare(o1, o2);
		Assert.assertEquals("Null page orders should come before integer values.", 1, result);
	}

	@Test
	public void testDisplayOrderFirstAndLast() {
		when(o1.getDisplayOrder()).thenReturn("first");
		when(o2.getDisplayOrder()).thenReturn("last");

		int result = comparator.compare(o1, o2);
		Assert.assertEquals("First values should come before last values.", -1, result);
	}

	@Test
	public void testDisplayOrderLastAndFirst() {
		when(o1.getDisplayOrder()).thenReturn("last");
		when(o2.getDisplayOrder()).thenReturn("first");

		int result = comparator.compare(o1, o2);
		Assert.assertEquals("First values should come before last values.", 1, result);
	}

	@Test
	public void testDisplayOrderFirstAndInteger() {
		when(o1.getDisplayOrder()).thenReturn("first");
		when(o2.getDisplayOrder()).thenReturn("5");

		int result = comparator.compare(o1, o2);
		Assert.assertEquals("First values should come before integer values.", -1, result);
	}

	@Test
	public void testDisplayOrderIntegerAndFirst() {
		when(o1.getDisplayOrder()).thenReturn("1");
		when(o2.getDisplayOrder()).thenReturn("first");

		int result = comparator.compare(o1, o2);
		Assert.assertEquals("First values should come before integer values.", 1, result);
	}

	@Test
	public void testDisplayOrderLastAndInteger() {
		when(o1.getDisplayOrder()).thenReturn("last");
		when(o2.getDisplayOrder()).thenReturn("5");

		int result = comparator.compare(o1, o2);
		Assert.assertEquals("Last values should come after integer values.", 1, result);
	}

	@Test
	public void testDisplayOrderIntegerAndLast() {
		when(o1.getDisplayOrder()).thenReturn("1");
		when(o2.getDisplayOrder()).thenReturn("last");

		int result = comparator.compare(o1, o2);
		Assert.assertEquals("Last values should come after integer values.", -1, result);
	}

	@Test
	public void testDisplayOrderNullAndInteger() {
		when(o1.getDisplayOrder()).thenReturn(null);
		when(o2.getDisplayOrder()).thenReturn("5");

		int result = comparator.compare(o1, o2);
		Assert.assertEquals("Null values should come before integer values.", -1, result);
	}

	@Test
	public void testDisplayOrderIntegerAndNull() {
		when(o1.getDisplayOrder()).thenReturn("1");
		when(o2.getDisplayOrder()).thenReturn(null);

		int result = comparator.compare(o1, o2);
		Assert.assertEquals("Null values should come before integer values.", 1, result);
	}

	@Test
	public void testDisplayOrderLastPageOrderIntegerAndInteger() {
		when(o1.getDisplayOrder()).thenReturn("last");
		when(o2.getDisplayOrder()).thenReturn("last");

		when(o1.getPageOrder()).thenReturn(Integer.valueOf(1));
		when(o2.getPageOrder()).thenReturn(Integer.valueOf(2));

		int result = comparator.compare(o1, o2);
		Assert.assertEquals("Non-null page orders should have natural ordering.", -1, result);
	}

	@Test
	public void testDisplayOrderLastPageOrderIntegerAndInteger2() {
		when(o1.getDisplayOrder()).thenReturn("last");
		when(o2.getDisplayOrder()).thenReturn("last");

		when(o1.getPageOrder()).thenReturn(Integer.valueOf(3));
		when(o2.getPageOrder()).thenReturn(Integer.valueOf(2));

		int result = comparator.compare(o1, o2);
		Assert.assertEquals("Non-null page orders should have natural ordering.", 1, result);
	}
}
