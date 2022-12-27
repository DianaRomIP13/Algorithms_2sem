package com.puzzle;

public class Action {
	private static Position[] validMoves(Position zero) {
		return switch (zero) {
			case TOP_LEFT -> new Position[]{Position.TOP_CENTER, Position.MIDDLE_LEFT};
			case TOP_CENTER -> new Position[]{Position.TOP_RIGHT, Position.TOP_LEFT, Position.MIDDLE_CENTER};
			case TOP_RIGHT -> new Position[]{Position.TOP_CENTER, Position.MIDDLE_RIGHT};
			case MIDDLE_LEFT -> new Position[]{Position.TOP_LEFT, Position.MIDDLE_CENTER, Position.BOTTOM_LEFT};
			case MIDDLE_CENTER -> new Position[]{Position.TOP_CENTER, Position.MIDDLE_RIGHT, Position.MIDDLE_LEFT, Position.BOTTOM_CENTER};
			case MIDDLE_RIGHT -> new Position[]{Position.TOP_RIGHT, Position.MIDDLE_CENTER, Position.BOTTOM_RIGHT};
			case BOTTOM_LEFT -> new Position[]{Position.MIDDLE_LEFT, Position.BOTTOM_CENTER};
			case BOTTOM_CENTER -> new Position[]{Position.MIDDLE_CENTER, Position.BOTTOM_RIGHT, Position.BOTTOM_LEFT};
			case BOTTOM_RIGHT -> new Position[]{Position.MIDDLE_RIGHT, Position.BOTTOM_CENTER};
		};
	}

	public static PuzzleNode[] genChildren(PuzzleNode parent, Position zero, Heuristic h) {
		return genChildren(parent, zero, h, validMoves(zero));
	}

	private static PuzzleNode[] genChildren(PuzzleNode parent, Position zero, Heuristic heuristic, Position... num) {
		int zero_index = zero.getIndex();
		PuzzleNode[] children = new PuzzleNode[num.length];
		String path;
		int counter = 0;
		int[] temp;
		for (Position i : num) {
			temp = parent.getState().clone();
			int n = i.getIndex();
			// міняємо нуль та обране число (напрямок) місцями
			temp[zero_index] = temp[zero_index] ^ temp[n] ^ (temp[n] = temp[zero_index]);
			path = parent.getPath();
			if (zero_index + 1 == n) path += "→";
			else if (zero_index - 1 == n) path += "←";
			else if (zero_index + 3 == n) path += "↓";
			else if (zero_index - 3 == n) path += "↑";
			children[counter] = new PuzzleNode(temp, parent.getDep() + 1, path, heuristic, parent.getFn() + 1);
			counter++;
		}
		return children;
	}
}
