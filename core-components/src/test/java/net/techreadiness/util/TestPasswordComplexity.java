package net.techreadiness.util;

import org.junit.Assert;
import org.junit.Test;

public class TestPasswordComplexity {

	@Test
	public void blankGivesZero() {
		Assert.assertEquals(0, PasswordComplexityEvaluator.getPasswordComplexity(null));
		Assert.assertEquals(0, PasswordComplexityEvaluator.getPasswordComplexity(""));
	}

	@Test(expected = IllegalArgumentException.class)
	public void tooBigThrowsException() {
		// should be too long ...
		Assert.assertEquals(0, PasswordComplexityEvaluator.getPasswordComplexity("123456789012345678901234567890123"));
	}

	@Test
	public void tooShortOK() {
		// min numeric
		Assert.assertEquals(1, PasswordComplexityEvaluator.getPasswordComplexity("1"));
	}

	@Test
	public void minLengthSingleType() {
		// min numeric, min length
		Assert.assertEquals(2, PasswordComplexityEvaluator.getPasswordComplexity("12345678"));
	}

	@Test
	public void threeComplexExamples() {
		// min numeric, min lower, min length
		Assert.assertEquals(3, PasswordComplexityEvaluator.getPasswordComplexity("a2345678"));
		// min numeric, min upper, min length
		Assert.assertEquals(3, PasswordComplexityEvaluator.getPasswordComplexity("A2345678"));
		// min numeric, min special, min length
		Assert.assertEquals(3, PasswordComplexityEvaluator.getPasswordComplexity("~2345678"));
	}

	@Test
	public void fourComplexExamples() {
		// min numeric, min lower, min upper, min length
		Assert.assertEquals(4, PasswordComplexityEvaluator.getPasswordComplexity("aZ345678"));
	}

	@Test
	public void fiveComplexExamples() {
		// min numeric, min lower, min upper, min special, min length
		Assert.assertEquals(5, PasswordComplexityEvaluator.getPasswordComplexity("aZ+45678"));
	}

	@Test
	public void specialExamples() {
		char[] specials = { '\'', '"', '~', '`', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '+', '=', '}',
				'{', '[', ']', '|', '\\', ':', ';', '<', ',', '>', '.', '?', '/' };
		for (int i = 0; i < specials.length; i++) {
			Assert.assertEquals(1, PasswordComplexityEvaluator.getPasswordComplexity(String.valueOf(specials[i])));
		}
	}

}
