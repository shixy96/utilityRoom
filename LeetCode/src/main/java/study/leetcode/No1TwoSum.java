package study.leetcode;

public class No1TwoSum {
	public static void main(String args[]) {
		No1TwoSum No1TwoSum = new No1TwoSum();
		for (int i : No1TwoSum.twoSum(new int[] { 3, 3, 4 }, 6)) {
			System.out.println(i);
		}
	}

	public int[] twoSum(int[] nums, int target) {
		for (int i = 1; i < nums.length; i++) {
			for (int j = 0; j < i; j++) {
				if (nums[i] + nums[j] == target) {
					return new int[] { j, i };
				}
			}
		}
		throw new IllegalArgumentException("no such No1TwoSum");
	}
}