package study.leetcode;

public class No3LengthOfLongestSubstring {

	public static void main(String[] args) {
		No3LengthOfLongestSubstring no3LengthOfLongestSubstring = new No3LengthOfLongestSubstring();
		System.out.println(no3LengthOfLongestSubstring.lengthOfLongestSubstring("pwekwkekwc"));
		// pwekwkekwc
		// pwek ekwkek kekwc
		// pwek
	}

	public int lengthOfLongestSubstring(String s) {
		if (s.length() <= 1) {
			return s.length();
		}
		int start = 0, result = 0;
		for (int current = 1; current < s.length(); current++) {
			for (int j = start; j < current; j++) {
				if (s.charAt(j) == s.charAt(current)) {
					start = j + 1;
					break;
				}
			}
			if (current - start + 1 > result) {
				result = current - start + 1;
			}
		}
		return result;
	}
}
