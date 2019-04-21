package study.leetcode;

public class No2AddTwoNumbers {
	public static void main(String args[]) {
		No2AddTwoNumbers no2AddTwoNumbers = new No2AddTwoNumbers();
		ListNode l1 = no2AddTwoNumbers.initListNode(new int[] { 1 });
		no2AddTwoNumbers.printListNode(l1);
		ListNode l2 = no2AddTwoNumbers.initListNode(new int[] { 9,9 });
		no2AddTwoNumbers.printListNode(l2);

		ListNode head = no2AddTwoNumbers.addTwoNumbers(l1, l2);
		no2AddTwoNumbers.printListNode(head);
	}

	public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		if(l1 ==null && l2 == null) {
			throw new IllegalArgumentException();
		}
		ListNode head = new ListNode((l1.val + l2.val) % 10);
		int carry = (l1.val + l2.val) / 10;
		ListNode node = head;
		ListNode next;
		while (l1.next != null || l2.next != null) {
			l1 = l1.next != null ? l1.next : new ListNode(0);
			l2 = l2.next != null ? l2.next : new ListNode(0);
			next = new ListNode((l1.val + l2.val + carry) % 10);
			node.next = next;
			node = next;
			carry = (l1.val + l2.val + carry) / 10;
		}
		if (carry > 0) {
			next = new ListNode(carry);
			node.next = next;
			node = next;
		}
		return head;
	}

	private void printListNode(ListNode node) {
		while (node != null) {
			System.out.print(node.val + " -> ");
			node = node.next;
		}
		System.out.println();
	}

	private ListNode initListNode(int[] args) {
		ListNode head = new ListNode(args[0]);
		ListNode node;
		ListNode next;
		node = head;
		for (int i = 1; i < args.length; i++) {
			next = new ListNode(args[i]);
			node.next = next;
			node = next;
			next = null;
		}
		return head;
	}
}

// Definition for singly-linked list.
class ListNode {
	int val;
	ListNode next;

	ListNode(int x) {
		val = x;
	}
}
