package com.puzzle;

public class Data {
	public final int fn, hn, dep, index;
	public final String path;
	public final int[] state;
	public final boolean solved;

	public Data(int fn, int hn, int dep, int index, boolean solved) {
		this.fn = fn;
		this.hn = hn;
		this.dep = dep;
		this.path = "#";
		this.state = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		this.solved = solved;
		this.index = index;
	}

	public Data(Astar d) {
		PuzzleNode node = d.getCurrent();
		fn = node.getFn();
		hn = node.getHn();
		dep = node.getDep();
		path = node.getPath();
		state = node.getState();
		solved = d.getSolved();
		index = d.getIndex();
	}

	public Data(BFS d) {
		PuzzleNode node = d.getCurrent();
		fn = node.getFn();
		hn = node.getHn();
		dep = node.getDep();
		path = node.getPath();
		state = node.getState();
		solved = d.getSolved();
		index = d.getIndex();
	}

	public static Data[] cast(Astar... arr) {
		int i = 0;
		Data[] data = new Data[arr.length];
		for (Astar a : arr) data[i++] = new Data(a);
		return data;
	}

	public static Data[] cast(BFS... arr) {
		int i = 0;
		Data[] data = new Data[arr.length];
		for (BFS a : arr) data[i++] = new Data(a);
		return data;
	}

	public static Data getAverage(Data... data_arr) {
		int fn = 0, hn = 0, dep = 0, index = 0, solved_count = 0;
		for (Data data : data_arr)
			if (data.solved) {
				solved_count++;
				fn += data.fn;
				hn += data.hn;
				dep += data.dep;
				index += data.index;
			}

		return new Data(fn / solved_count, hn / solved_count, dep / solved_count, index / solved_count, true);
	}

	public String toString() {
		return "Вартість: " + fn + "\nГлухі: " + hn + "\nГлибина: " + dep + "\nШлях:" + path+ "\nІтерацій:" + index;
	}
}
