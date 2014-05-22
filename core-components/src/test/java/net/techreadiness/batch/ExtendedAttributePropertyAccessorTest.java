package net.techreadiness.batch;

import net.techreadiness.service.object.BaseObject;
import net.techreadiness.service.object.BaseObjectWithExts;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.OrgType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.google.common.collect.Maps;

public class ExtendedAttributePropertyAccessorTest {
	private PropertyAccessor accessor;
	private BaseObjectWithExts<?> objWithExt;
	private BaseObject<?> obj;
	private EvaluationContext evalContext;

	@Before
	public void setup() {
		objWithExt = new Org();
		objWithExt.setExtendedAttributes(Maps.<String, String> newHashMap());
		obj = new OrgType();
		evalContext = new StandardEvaluationContext(objWithExt);
		accessor = new ExtendedAttributePropertyAccessor();
	}

	@Test
	public void testCanReadNullName() throws AccessException {
		boolean canRead = accessor.canRead(evalContext, objWithExt, null);
		Assert.assertFalse(canRead);
	}

	@Test
	public void testCanReadNullTarget() throws AccessException {
		boolean canRead = accessor.canRead(evalContext, null, null);
		Assert.assertFalse(canRead);
	}

	@Test
	public void testCanReadNonExt() throws AccessException {
		boolean canRead = accessor.canRead(evalContext, obj, "name");
		Assert.assertFalse(canRead);
	}

	@Test
	public void testCanReadNonExtNullName() throws AccessException {
		boolean canRead = accessor.canRead(evalContext, obj, null);
		Assert.assertFalse(canRead);
	}

	@Test
	public void testCanReadExtCoreName() throws AccessException {
		boolean canRead = accessor.canRead(evalContext, objWithExt, "code");
		Assert.assertFalse(canRead);
	}

	@Test
	public void testCanReadExtName() throws AccessException {
		boolean canRead = accessor.canRead(evalContext, objWithExt, "someExtAttribute");
		Assert.assertTrue(canRead);
	}

	@Test
	public void testCanWriteNullName() throws AccessException {
		boolean canWrite = accessor.canWrite(evalContext, objWithExt, null);
		Assert.assertFalse(canWrite);
	}

	@Test
	public void testCanWriteNullTarget() throws AccessException {
		boolean canWrite = accessor.canWrite(evalContext, null, null);
		Assert.assertFalse(canWrite);
	}

	@Test
	public void testCanWriteNonExt() throws AccessException {
		boolean canWrite = accessor.canWrite(evalContext, obj, "name");
		Assert.assertFalse(canWrite);
	}

	@Test
	public void testCanWriteNonExtNullName() throws AccessException {
		boolean canWrite = accessor.canWrite(evalContext, obj, null);
		Assert.assertFalse(canWrite);
	}

	@Test
	public void testCanWriteExtCoreName() throws AccessException {
		boolean canWrite = accessor.canWrite(evalContext, objWithExt, "code");
		Assert.assertFalse(canWrite);
	}

	@Test
	public void testCanWriteExtName() throws AccessException {
		boolean canWrite = accessor.canWrite(evalContext, objWithExt, "someExtAttribute");
		Assert.assertTrue(canWrite);
	}

	@Test
	public void testRead() throws AccessException {
		String key = "someKey";
		String value = "someValue";
		objWithExt.getExtendedAttributes().put(key, value);
		TypedValue readValue = accessor.read(evalContext, objWithExt, key);
		Assert.assertEquals(value, readValue.getValue());
	}

	@Test
	public void testWrite() throws AccessException {
		String key = "someKey";
		String value = "someValue";
		accessor.write(evalContext, objWithExt, key, value);
		Assert.assertEquals(value, objWithExt.getExtendedAttributes().get(key));
	}
}
