package com.puzzle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class BFS {
	private final PuzzleNode root;
	private final Queue<PuzzleNode> queue;
	private final ArrayList<int[]> processed;
	private int index;
	private boolean solved = false;
	static PuzzleNode current = null;

	public BFS(PuzzleNode r) {
		root = r;
		queue = new LinkedList<>();
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
		queue.add(root);
		long start_at = System.currentTimeMillis();
		while (!queue.isEmpty()) {

			if (System.currentTimeMillis() - start_at > 1800000) {
				System.out.println("\u001b[31m30 хвилин вичерпано\u001b[0m");
				return current;
			}
			current = queue.peek();
			processed.add(current.getState());
			queue.remove();

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
			if (unique) queue.add(node);
		}
	}
}
