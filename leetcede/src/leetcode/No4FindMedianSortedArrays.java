package leetcode;

public class No4FindMedianSortedArrays {
	public static void main(String[] args) {
		No4FindMedianSortedArrays no4FindMedianSortedArrays = new No4FindMedianSortedArrays();
		no4FindMedianSortedArrays.findMedianSortedArrays(new int[] { 1, 3, 7, 10, 16 }, new int[] { 4, 6, 8, 9 });
	}

	public double findMedianSortedArrays(int[] nums1, int[] nums2) {
		int index = 0;
		int mLength = nums1.length;
		int nLength = nums2.length;
		if (mLength < nLength) {
			int[] tmps = nums1;
			nums1 = nums2;
			nums2 = tmps;
			int tmpl = mLength;
			mLength = nLength;
			nLength = tmpl;
		}
		int iMin = 0, iMax = mLength;
		
	}
}
