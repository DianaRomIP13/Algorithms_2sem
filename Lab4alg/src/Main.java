import java.util.*;

public class Main {
    public static final int limit = 50;

    public static void main(String... args) {
        Beehive hive = new Beehive(Graph.random(500, 1, limit));
        Graph solved = hive.solve();
        solved.draw();
        System.out.println();
        System.out.println(countUnique(solved.colormap));
    }

    public static int countUnique(int[] arr) {
        Set<Integer> s = new HashSet<>();
        for (int x : arr) s.add(x);
        return s.size();
    }
}

class Beehive {
    static final int
            SCOUTS_N = 5, BEST_SCOUTS_N = 2,
            RANDOM_SCOUTS_N = SCOUTS_N - BEST_SCOUTS_N,
            BEST_FORAGERS_N = 20,
            RANDOM_FORAGERS_N = 5;

    public final Queue<Bee> scouts;
    public final Graph graph;

    public Beehive(Graph graph) {
        this.graph = graph;
        scouts = new PriorityQueue<>();
        scoutsGen(Beehive.SCOUTS_N);
    }

    void scoutsGen(int count) {
        while (scouts.size() < count) {
            Bee bee = new Bee(graph.colorizeRandomly(), Integer.MAX_VALUE);
            if (!scouts.contains(bee))
                scouts.add(bee);
        }
    }

    void addBest(Bee[] best) {
        Collections.addAll(scouts, best);
    }

    Bee[] getBest() {
        Bee[] best = new Bee[BEST_SCOUTS_N];
        for (int i = 0; i < BEST_SCOUTS_N; i++)
            best[i] = scouts.poll();
        return best;
    }

    public Graph solve() {
        for (int i = 0; i < 1000; i++) {
            if (i % 20 == 0)
                System.out.println("Iteration: " + i +
                        "\n\tMin: " + scouts.peek().f);

            Bee[] best = getBest();
            Bee[] random = scouts.toArray(best);
            scouts.clear();
            localSearch(best, BEST_FORAGERS_N);
            localSearch(random, RANDOM_FORAGERS_N);

            best = getBest();
            scouts.clear();
            scoutsGen(RANDOM_SCOUTS_N);
            addBest(best);
        }
        return new Graph(graph.matrix, scouts.peek().colormap);
    }

    Bee discover(Bee scout, int count, int max) {
        Queue<Integer[]> queue = new LinkedList<>();
        int[] vertex_id = new int[max + 1];
        Arrays.fill(vertex_id, -1);
        int len = graph.matrix.length;
        for (int i = 0; i < len; i++) {
            int c = 0;
            for (int j = 0; j < len; j++)
                if (graph.matrix[i][j] == 1) c++;
            vertex_id[c] = i;
        }
        for (int i = max; i >= 0; i--) {
            if (queue.size() >= count) break;
            if (vertex_id[i] > -1)
                for (int vertex : graph.neighbors(vertex_id[i]))
                    queue.add(new Integer[]{vertex_id[i], vertex});
        }
        List<Bee> result = new ArrayList<>();
        for (Integer[] el : queue) {
            int[] colormap = Arrays.copyOf(scout.colormap, scout.colormap.length);
            int buf = colormap[el[1]];
            colormap[el[1]] = colormap[el[0]];
            colormap[el[0]] = buf;
            if (graph.isRepeating(el[0], colormap[el[0]], colormap) || graph.isRepeating(el[1], colormap[el[1]], colormap)) continue;
            for (int color = 0; color <= scout.f; color++) {
                if (!graph.isRepeating(el[1], color,colormap)) {
                    colormap[el[1]] = color;
                    result.add(new Bee(colormap, Main.countUnique(colormap)));
                }
            }
        }
        int id = 0;

        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).f < result.get(id).f)
                id = i;
        }
        return result.size() > 0 ? result.get(id) : scout;
    }

    private void localSearch(Bee[] scouts, int count) {
        for (Bee scout : scouts)
            this.scouts.add(discover(scout, count, Main.limit));
    }
}

class Bee implements Comparable<Bee> {
    public int[] colormap;
    public int f;

    @Override
    public int compareTo(Bee bee) {
        return this.f - bee.f;
    }

    Bee(int[] colormap, int f) {
        this.colormap = colormap;
        this.f = f;
    }

}


class Graph {
    public final int[][] matrix;
    public final int[] colormap;

    public Graph(int[][] matrix) {
        this.matrix = matrix;
        this.colormap = new int[matrix.length];
        Arrays.fill(this.colormap, Integer.MAX_VALUE);
    }

    public Graph(int[][] matrix, int[] colormap) {
        this.matrix = matrix;
        this.colormap = colormap;
    }

    public boolean isRepeating(int index, int color) {
        return isRepeating(index, color, this.colormap);
    }

    public boolean isRepeating(int index, int color, int[] colormap) {
        for (int i : this.neighbors(index))
            if (colormap[i] == color) return true;
        return false;
    }

    public int[] colorizeRandomly() {
        int max_colors = 0;
        int len = this.matrix.length;
        int[] colormap = new int[len];

        for (int i = 0; i < len; i++)
            for (int color = 0; color < len; color++) {
                if (isRepeating(i, color))
                    continue;

                if (color > max_colors)
                    max_colors++;

                colormap[i] = color;
                this.colormap[i] = color;
                break;
            }
        return colormap;
    }

    public int[] neighbors(int index) {
        List<Integer> list = new ArrayList<>();
        int len = this.matrix.length;
        for (int i = 0; i < len; i++)
            if (this.matrix[index][i] == 1)
                list.add(i);
        return list.stream().mapToInt(x -> x).toArray();
    }

    public static Graph random(int count, int min, int max) {
        int[][] matrix = new int[count][count];
        for (int x = 0; x < count; x++)
            for (int i = 0; i < (max - min) >> 1; i++) {
                int y = (int) (Math.random() * count);
                if (y > x) matrix[x][y] = matrix[y][x] = 1;
            }
        return new Graph(matrix);
    }

    public void draw() {
        int len = this.matrix.length;
        System.out.print(" ");
        for (int i = 0; i < len; i++)
            System.out.print("\u001b[30;47m" + (char) (this.colormap[i] + 'A'));
        System.out.println("\u001b[0m");
        for (int i = 0; i < len; i++) {
            System.out.print("\u001b[30;47m" + (char) (this.colormap[i] + 'A'));
            for (int j = 0; j < len; j++) {
                System.out.print(this.matrix[i][j] == 1 ? "\u001b[34;40m1" :
                        i == j ? "\u001b[31;40m\\" : "\u001b[37;40m0");
            }
            System.out.print("\u001b[30;47m" + (char) (this.colormap[i] + 'A'));
            System.out.println("\u001b[0m");
        }
        System.out.print(" ");
        for (int i = 0; i < len; i++)
            System.out.print("\u001b[30;47m" + (char) (this.colormap[i] + 'A'));
        System.out.println("\u001b[0m");
        for (int i = 0; i < len; i++)
            System.out.print(this.colormap[i] + " ");
    }

}
