package net.techreadiness.util;

import java.util.Comparator;

/**
 * 
 * 
 * This is a case-insensitive comparator to compare strings alphanumerically. Normally when sorting grades, you would see:
 * Grade 1 Grade 12 Grade 13 Grade 2 Grade 21 Grade 3 ... etc.
 * 
 * This comparator compares the entire number, so you get Grade 1 Grade 2 Grade 3 Grade 12 Grade 13 Grade 21 ... etc.
 * 
 */
public class NumericStringComparator implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		return compareStrings(o1, o2);
	}

	public static int compareStrings(String s1, String s2) {
		if (s1 == null) {
			s1 = "";
		}
		if (s2 == null) {
			s2 = "";
		}
		int i1 = 0, i2 = 0;
		while (i1 < s1.length() && i2 < s2.length()) {
			@SuppressWarnings("rawtypes")
			Comparable c1, c2;
			int j1 = isNumeric(s1, i1);
			int j2 = isNumeric(s2, i2);
			if (j1 > 9) {
				j1 = 9;
			}
			if (j2 > 9) {
				j2 = 9;
			}
			if (j1 != 0 && j2 != 0) {
				c1 = new Integer(s1.substring(i1, i1 + j1));
				c2 = new Integer(s2.substring(i2, i2 + j2));
				i1 += j1;
				i2 += j2;
			} else {
				c1 = s1.substring(i1, i1 + 1).toUpperCase();
				c2 = s2.substring(i1, i1 + 1).toUpperCase();
				i1++;
				i2++;
			}
			@SuppressWarnings("unchecked")
			int c = c1.compareTo(c2);
			if (c != 0) {
				return c;
			}
		}
		return s1.length() - s2.length();
	}

	/**
	 * int isNumeric(String s, int index)
	 * 
	 * @params: s - the to check index - location in s to check
	 * 
	 * @returns: number of sequential numeric characters in s, starting at index
	 */
	private static int isNumeric(String s, int index) {
		int ret = 0;
		if (index < s.length()) {
			char c = s.charAt(index);
			while (Character.isDigit(c)) {
				ret++;
				if (index + ret < s.length()) {
					c = s.charAt(index + ret);
				} else {
					c = (char) -1;
				}
			}
		}
		return ret;
	}

}