import java.util.ArrayList;
import java.util.List;

public class Main {
    final int size;

    public Main(int size) {
        this.size = size;
    }

    //жадібний алгоритм
    public ItemStack[] fillBestCost(Item[] items) {
        ArrayList<ItemStack> list = new ArrayList<>();
        Item.sort(items, 0, 99);
        int free_space = this.size;
        for (Item i : items) {
            int fit_in = (int) (free_space / i.weight);
            if (fit_in > 0) {
                free_space -= fit_in * i.weight;
                list.add(new ItemStack(i, fit_in));
            }
        }
        return list.toArray(new ItemStack[0]);
    }

    public static int totalCost(ItemStack[] itemStack) {
        int total = 0;
        for (ItemStack i : itemStack)
            total += i.item().cost * i.count();
        return total;
    }

    public static void main(String[] a) {
        Item.refresh();
        Item[] items = Item.createRandomItems(100);
        for (Item i : items) System.out.println(i);

        ItemStack[] itemStacks = new Main(500).fillBestCost(items);

        System.out.println();
        System.out.println("\u001b[35mTOTAL COST:\u001b[0m " + totalCost(itemStacks));
        for (ItemStack is : itemStacks) System.out.println(is);

    }
}

record ItemStack(Item item, int count) {
    public String toString() {
        return item.toString() + "\t\u001b[31mx" + count;
    }
}

class Item {
    static private int id_counter;
    final int id;
    final double weight, cost, c_per_w;

    public static void refresh() {
        id_counter = 0;
    }

    public String toString() {
        return "\u001b[32mID:\u001b[0m " + id +
                "; \u001b[32mC:\u001b[0m " + cost +
                "; \u001b[32mW:\u001b[0m " + weight +
                "; \u001b[32mC/W:\u001b[0m " + c_per_w;
    }

    public Item(double weight, double cost) {
        this.id = id_counter++;
        this.weight = weight;
        this.cost = cost;
        this.c_per_w = cost / weight;
    }

    public static Item[] createRandomItems(int count) {
        Item[] items = new Item[count];
        for (int i = 0; i < count; i++)
            items[i] = new Item((int) (1 + Math.random() * 19), (int) (2 + Math.random() * 28));
        return items;
    }

    public static void sort(Item[] array, int low, int high) {
        // завершити якщо нічого сортувати
        if (array.length == 0 || low >= high) return;

        // обрати головний елемент
        int middle = low + (high - low) / 2;
        double head = array[middle].c_per_w;

        // розділяємо на підмассиви
        int i = low, j = high;
        while (i <= j) {
            while (array[i].c_per_w > head) i++;
            while (array[j].c_per_w < head) j--;
            if (i <= j) {
                Item temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }

        // сортуємо ліву та праву частини
        if (low < j) sort(array, low, j);
        if (high > i) sort(array, i, high);
    }
}