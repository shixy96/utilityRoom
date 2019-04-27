package study.leetcode;

public class No7IntReverse {

	public static void main(String[] args) {
		No7IntReverse no7IntReverse = new No7IntReverse();
		System.out.println(no7IntReverse.reverse1(1534236469));
	}

	public int reverse1(int x) {
		int result = 0;
		try {
			char[] xc = new Integer(Math.abs(x)).toString().toCharArray();
			char tmp;
			for (int i = 0, l = xc.length; i < l / 2; i++) {
				tmp = xc[i];
				xc[i] = xc[l - i - 1];
				xc[l - i - 1] = tmp;
			}
			result = new Integer(new String(xc));
			result = x < 0 ? -result : result;
		} catch (Exception e) {
		}
		return result;
	}

	public int reverse(int x) {
		int rev = 0;
		while (x != 0) {
			int pop = x % 10;
			x /= 10;
			if (rev > Integer.MAX_VALUE / 10 || (rev == Integer.MAX_VALUE / 10 && pop > 7))
				return 0;
			if (rev < Integer.MIN_VALUE / 10 || (rev == Integer.MIN_VALUE / 10 && pop < -8))
				return 0;
			rev = rev * 10 + pop;
		}
		return rev;
	}
}
