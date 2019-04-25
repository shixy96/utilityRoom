package course.util;

public class StringUtil {

	public static boolean isEmpty(String text) {
		if (text == null || text.length() == 0 || text.hashCode() == 65279) {
			return true;
		}
		char[] chars = text.toCharArray();
		for (char c : chars) {
			if (!Character.isWhitespace(c) && !(c + "").equals(" ")) {
				return false;
			}
		}
		return true;
	}

	public static String replaceWildcard(String string) {
		if (string == null || string.length() == 0) {
			return string;
		}
		StringBuffer sb = new StringBuffer(string);
		int index = sb.indexOf("_");
		while (index > -1) {
			sb.replace(index, index + 1, "\\_");
			if (index + 2 > sb.length()) {
				break;
			}
			index = sb.indexOf("_", index + 2);
		}
		index = sb.indexOf("%");
		while (index > -1) {
			sb.replace(index, index + 1, "\\%");
			if (index + 2 > sb.length()) {
				break;
			}
			index = sb.indexOf("%", index + 2);
		}
		return sb.toString();
	}

}
