package net.techreadiness.util;

/**
 * PasswordComplexityValidator determines how complex a password is based on the following requirements:
 * 
 * Standard Password rules:
 * <ul>
 * <li>Complexity 0 = none of the rules below need to be met to be considered a valid password</li>
 * <li>Complexity 1 = one of the rules below need to be met to be considered a valid password</li>
 * <li>Complexity 2 = two of the rules below need to be met to be considered a valid password</li>
 * <li>Complexity 3 = three of the rules below need to be met to be considered a valid password</li>
 * </ul>
 * 
 * Uppercase: 1 Lowercase: 1 Number: 1 Special Chars: 1 Min Length: 8 Max Length: 32 Special Chars:
 * ~`!@#$%^&*()_-+=}{[]|\:;<,>.?/
 * 
 */
public class PasswordComplexityEvaluator {

	// For character determinations
	private static final int CHAR_LOWER_A = 'a';
	private static final int CHAR_LOWER_Z = 'z';
	private static final int CHAR_UPPER_A = 'A';
	private static final int CHAR_UPPER_Z = 'Z';
	private static final int CHAR_NUMERIC_ZERO = '0';
	private static final int CHAR_NUMERIC_NINE = '9';

	// Since the alpha and numeric checks handle the [a-zA-Z0-9] case in
	// earlier if statement checks, we can then assume the surrounding
	// characters
	// within the range of the special char lower and upper values are in fact
	// special characters. If it extends past the range, then it's no longer a
	// symbol.
	private static final int CHAR_LOWER_SPECIAL_CHAR = ' ';
	private static final int CHAR_UPPER_SPECIAL_CHAR = '~';

	// Configurable variables
	private static int minPasswordLength = 8;
	private static int maxPasswordLength = 32;
	private static int minLowerAlphaChars = 1;
	private static int minUpperAlphaChars = 1;
	private static int minSpecialChars = 1;
	private static int minNumericalChars = 1;

	public static int getPasswordComplexity(String password) throws IllegalArgumentException {

		int complexity = 0;

		if (password == null || password.isEmpty()) {
			return 0;
		}

		int passwordLen = password.length();

		if (passwordLen > maxPasswordLength) {
			throw new IllegalArgumentException("Maximum password length is required to be less than or equal to: "
					+ maxPasswordLength);
		}
		if (passwordLen >= minPasswordLength) {
			complexity++;
		}

		int alphaLowerCharsCount = 0;
		int alphaUpperCharsCount = 0;
		int numericCharsCount = 0;
		int specialCharsCount = 0;

		// Count the characters
		char passwordChar;
		for (int i = 0; i < passwordLen; i++) {
			passwordChar = password.charAt(i);
			if (passwordChar >= CHAR_LOWER_A && passwordChar <= CHAR_LOWER_Z) {
				alphaLowerCharsCount++;
			} else if (passwordChar >= CHAR_UPPER_A && passwordChar <= CHAR_UPPER_Z) {
				alphaUpperCharsCount++;
			} else if (passwordChar >= CHAR_NUMERIC_ZERO && passwordChar <= CHAR_NUMERIC_NINE) {
				numericCharsCount++;
			} else if (passwordChar >= CHAR_LOWER_SPECIAL_CHAR && passwordChar <= CHAR_UPPER_SPECIAL_CHAR) {
				specialCharsCount++;

			}
		}

		if (alphaLowerCharsCount >= minLowerAlphaChars) {
			complexity++;
		}
		if (alphaUpperCharsCount >= minUpperAlphaChars) {
			complexity++;
		}
		if (numericCharsCount >= minNumericalChars) {
			complexity++;
		}
		if (specialCharsCount >= minSpecialChars) {
			complexity++;
		}
		return complexity;
	}
}
