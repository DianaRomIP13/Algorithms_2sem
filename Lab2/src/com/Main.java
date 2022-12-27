package com;

import com.puzzle.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
	static final int[][] states = {
		{1, 2, 3, 0, 4, 7, 5, 6,  8},
		{1, 3, 4, 2, 5, 8, 6, 7, 0},
		{1, 2, 3, 4, 5, 6, 7, 8, 0}
	};

	public static void main(String[] args) {
		int[] goal = {1, 2, 3, 4, 5, 6, 7, 8, 0};
		long start_at = System.currentTimeMillis();


		BFS[] bfs_arr = new BFS[states.length];
		for (int i = 0; i < bfs_arr.length; i++)
			if (System.currentTimeMillis() - start_at > 1800000) { //30 * 60 * 1000 = 1800000
				System.out.println("\u001b[31m30 хвилин вичерпано\u001b[0m");
				break;
			} else bfs_arr[i] = solveBFS(states[i], goal);

		Astar[] astar_arr = new Astar[states.length];
		for (int i = 0; i < astar_arr.length; i++)
			if (System.currentTimeMillis() - start_at > 1800000) {
				System.out.println("\u001b[31m30 хвилин вичерпано\u001b[0m");
				break;
			} else astar_arr[i] = solveAStar(states[i], goal);

		System.out.println("\n\u001b[32mСередня BFS:\n" + Data.getAverage(Data.cast(bfs_arr)));
		System.out.println("\n\u001b[32mСередня A*:\n" + Data.getAverage(Data.cast(astar_arr)));
	}

	/* Генерація випадкової плитки
	* BFS[] bfs_arr = new BFS[20];
			for (int i = 0; i < bfs_arr.length; i++)
				if (System.currentTimeMillis() - start_at > 1800000) { //30 * 60 * 1000 = 1800000
					System.out.println("\u001b[31m30 хвилин вичерпано\u001b[0m");
					break;
				} else bfs_arr[i] = solveBFS(randomState(), goal);

			Astar[] astar_arr = new Astar[20];
			for (int i = 0; i < astar_arr.length; i++)
				if (System.currentTimeMillis() - start_at > 1800000) {
					System.out.println("\u001b[31m30 хвилин вичерпано\u001b[0m");
					break;
				} else astar_arr[i] = solveAStar(randomState(), goal);

	* */
	public static int[] randomState() {
		int[] output = new int[9];
		int[] state = {0, 1, 2, 3, 4, 5, 6, 7, 8};

		List<Integer> integers = new ArrayList<>();
		for (int c : state) integers.add(c);

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				output[i * 3 + j] = integers.remove((int) (Math.random() * integers.size()));

		return output;
	}

	private static BFS solveBFS(int[] inital, int[] goal) {
		Heuristic heuristic = new Heuristic(goal, -1);
		PuzzleNode start = new PuzzleNode(inital, heuristic);
		System.out.println("\u001b[36m\\ BFS\u001b[0m");
		start.printState();
		BFS bfs = new BFS(start);
		System.out.println("\u001b[35m");
		bfs.solve().printState();
		System.out.println("Ітерацій:" + bfs.getIndex());
		System.out.println("\u001b[36m/\u001b[35m");
		return bfs;
	}

	private static Astar solveAStar(int[] inital, int[] goal) {
		Heuristic heuristic = new Heuristic(goal, 1);
		PuzzleNode start = new PuzzleNode(inital, heuristic);
		System.out.println("\u001b[36m\\ A*\u001b[0m");
		start.printState();
		Astar astar = new Astar(start);
		System.out.println("\u001b[35m");
		astar.solve().printState();
		System.out.println("Ітерацій:" + astar.getIndex());
		System.out.println("\u001b[36m/\u001b[35m");
		return astar;
	}
}
