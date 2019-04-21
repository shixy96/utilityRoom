package study.leetcode;

public class No5LongestPalindrome {

	public static void main(String[] args) {
		No5LongestPalindrome no5LongestPalindrome = new No5LongestPalindrome();
		System.out.println(no5LongestPalindrome.longestPalindrome("babad"));
		// aabb
	}

	public String longestPalindrome(String s) {
		if (s == null || s.length() <= 1) {
			return s;
		}
		int start = 0, end = 0;
		// 轮循每个字符向外拓展的最大回文字符串长度
		for (int i = 0; i < s.length(); i++) {
			int lengthO = lengthOfExpandPalindrome(s, i, i);
			int lengthE = lengthOfExpandPalindrome(s, i, i + 1);
			int length = Math.max(lengthO, lengthE);
			if (length > end - start + 1) {
				start = i - (length - 1) / 2;
				end = i + length / 2;
			}
		}
		return s.substring(start, end + 1);
	}

	private int lengthOfExpandPalindrome(String s, int left, int right) {
		while (left >= 0 && right < s.length() && s.charAt(right) == s.charAt(left)) {
			left--;
			right++;
		}
		return right - left - 1;
	}
}
