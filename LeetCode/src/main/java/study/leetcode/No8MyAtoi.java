package study.leetcode;

public class No8MyAtoi {
	public static void main(String[] args) {
		No8MyAtoi no8MyAtoi = new No8MyAtoi();
		System.out.println(no8MyAtoi.myAtoi("-91283472332"));
	}

	public int myAtoi(String str) {
		int result = 0;
		char[] value = str.toCharArray();
		int len = value.length;
		int index = 0;
		int start = 0;
		int end = 0;
		boolean isFu = false;
		while ((index < len) && (value[index] <= ' ')) {
			index++;
		}
		if (index < len && (value[index] == '+' || value[index] == '-')) {
			isFu = value[index++] == '-';
		}
		start = index;
		while (index < len && value[index] <= '9' && value[index] >= '0') {
			index++;
		}
		end = index;
		if (start < end && end <= len) {
			try {
				result = new Integer(str.substring(start, end));
				result = isFu ? -result : result;
			} catch (Exception e) {
				result = isFu ? Integer.MIN_VALUE : Integer.MAX_VALUE;
			}
		}
		return result;
	}
}
