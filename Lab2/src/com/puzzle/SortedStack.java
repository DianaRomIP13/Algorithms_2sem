package com.puzzle;

import java.util.Stack;

public class SortedStack<K> extends Stack {
	private final Stack<PuzzleNode> Past = new Stack<>();

	@Override
	public Object push(Object item) {
		int node = ((PuzzleNode) item).getFn();
		while (!this.isEmpty())
			if (((PuzzleNode) this.peek()).getFn() > node)
				break;
			else Past.push((PuzzleNode)
				this.pop());

		super.push(item);
		while (!Past.isEmpty())
			super.push(Past.pop());

		return 1;
	}

}
