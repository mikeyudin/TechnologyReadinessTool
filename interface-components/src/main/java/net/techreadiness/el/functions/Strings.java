package net.techreadiness.el.functions;

public class Strings {

	public static String abbreviate(String text, int size) {
		if (text == null) {
			return null;
		}
		text = text.trim();
		if (text.length() > size) {
			text = text.substring(0, size).trim() + "...";
		}
		return text;
	}
}
