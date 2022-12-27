package com.puzzle;

import java.util.ArrayList;
import java.util.Arrays;

public class Astar {
	private PuzzleNode root;
	private SortedStack<PuzzleNode> stack;
	private ArrayList<int[]> processed;
	private int index;
	private boolean solved = false;
	static PuzzleNode current = null;

	public Astar(PuzzleNode r) {
		root = r;
		stack = new SortedStack<>();
		processed = new ArrayList<>();
		index = 0;
	}

	public PuzzleNode getCurrent() {
		return current;
	}

	public boolean getSolved() {
		return solved;
	}

	public int getIndex() {
		return index;
	}

	public Data getData() {
		return new Data(this);
	}

	public PuzzleNode getRoot() {
		return root;
	}


	public PuzzleNode solve() {
		current = null;
		stack.push(root);
		long start_at = System.currentTimeMillis();
		while (!stack.isEmpty()) {

			if (System.currentTimeMillis() - start_at > 1800000) {
				System.out.println("\u001b[31m30 хвилин вичерпано\u001b[0m");
				return current;
			}

			current = (PuzzleNode) stack.pop();
			processed.add(current.getState());

			if (current.isGoal()) {
				solved = true;
				return current;
			}

			index++;
			addChildren(current.generateChildren());
		}

		return current;
	}

	private void addChildren(PuzzleNode[] children) {
		boolean unique = true;
		for (PuzzleNode node : children) {
			if (node == null) break;
			for (int[] a : processed)
				if (Arrays.equals(a, node.getState())) {
					unique = false;
					break;
				} else unique = true;
			if (unique) stack.push(node);
		}
	}
}
