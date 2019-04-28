package study.leetcode;

public class No9IsPalindrome {
	public static void main(String[] args) {
		No9IsPalindrome no9IsPalindrome = new No9IsPalindrome();
		System.out.println(no9IsPalindrome.isPalindrome(10));
	}

	public boolean isPalindrome1(int x) {
		char[] xc = new Integer(x).toString().toCharArray();
		for (int i = 0, l = xc.length; i < l; i++) {
			if (xc[i] != xc[l - i - 1]) {
				return false;
			}
		}
		return true;
	}

	public boolean isPalindrome(int x) {
		if (x < 0 || (x % 10 == 0 && x != 0)) {
			return false;
		}
		int revertedNumber = 0;
		while (x > revertedNumber) {
			revertedNumber = revertedNumber * 10 + x % 10;
			x /= 10;
		}
		return x == revertedNumber || x == revertedNumber / 10;
	}
}
