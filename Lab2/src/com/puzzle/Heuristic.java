package com.puzzle;

public class Heuristic {
	private int[] GOAL = {1, 2, 3, 4, 5, 6, 7, 8, 0};
	int mode = -1; // 1 = Manhattan ; -1 = no heuristic

	public Heuristic(int[] g, int m) {
		GOAL = g;
		if (m == 1 || m == -1) mode = m;
	}

	int getHeuristic(int[] a) {
		if (mode  == 1) return Manhattan(a);
		else return 0;
	}

	private int Manhattan(int[] a) {
		int sum = 0;
		for (int i = 0; i < 9; i++) {
			int d = Math.abs(index(a, i) - index(GOAL, i));
			sum += Math.abs((d / 3) + (d % 3));
		}
		return sum;
	}

	private int index(int[] a, int n) {
		int i;
		for (i = 0; i < a.length; i++)
			if (a[i] == n)
				return i;
		return i;
	}

	public int[] getGOAL() {
		return GOAL;
	}
}
