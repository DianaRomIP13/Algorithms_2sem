import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Main {
    public static final String // основні путі до файлів
            main_dir = "C:\\Lab1\\",
            sort_file = "to_sort.txt",
            out_file = "sorted.txt",
            temp_dir = "temp\\";
    static int file_name_counter = 0; // лічильник імен тимчасових файлів
    public static final int memory_size = 100_000; // розмір одного файлу

    // для назв фалів
    static final String ch = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final int chl = ch.length();

    // для перевірки часу виконання
    static String SPLITTING, SORTING, DONE;

    public static void main(String... a) {

        createUnsorted(main_dir + sort_file, 1_000_000);

        polyphaseSort(main_dir + sort_file, main_dir + out_file, main_dir + temp_dir);

        System.out.println("\n\n\n\n");

        System.out.println(SPLITTING + " > SPLITTING");
        System.out.println(SORTING + " > SORTING");
        System.out.println(DONE + " > DONE");
    }

    /**
     * сортує файли
     */
    public static void polyphaseSort(String in_path, String out_path, String temp_dir_path) {
        file_name_counter = 0;
        SPLITTING = new Date().toString();  // відбиток часу
        splitFileBySizeAndQuickSort(in_path, temp_dir_path);

        SORTING = new Date().toString();    // відбиток часу
        while (!mergeSort(out_path, temp_dir_path)) ;

        DONE = new Date().toString();       // відбиток часу
    }

    /**
     * сортує файли
     */
    public static boolean mergeSort(String out_path, String temp_dir_path) {
        // список фалів що підлягають сортуванню на даному етапі
        File[] files = new File(temp_dir_path).listFiles();
        boolean is_last = files.length == 2;
        int count = files.length - 1;
        for (int i = 0; i < count; i += 2) {
            // пара файлів, що будуть зливатися
            File f1 = files[i], f2 = files[i + 1];
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(is_last ? out_path : (temp_dir_path + nextName()), false))) {
                BufferedReader r1 = new BufferedReader(new FileReader(f1)), r2 = new BufferedReader(new FileReader(f2));
                String l1 = r1.readLine(), l2 = r2.readLine();
                // якщо один з фалів закінчився - дописуемо все з залишившогося
                while (l1 != null && l2 != null) {
                    // злиття файлів
                    if (Integer.parseInt(l1) < Integer.parseInt(l2)) {
                        writer.write(l1 + "\n");
                        l1 = r1.readLine();
                    } else {
                        writer.write(l2 + "\n");
                        l2 = r2.readLine();
                    }
                }
                if (l1 == null && l2 != null) {
                    do writer.write(l2 + "\n");
                    while ((l2 = r2.readLine()) != null);
                }
                if (l1 != null && l2 == null) {
                    do writer.write(l1 + "\n");
                    while ((l1 = r1.readLine()) != null);
                }
                r1.close();
                r2.close();
                f1.delete();
                f2.delete();
                writer.flush();
                writer.close();
            } catch (Exception ie) {
                ie.printStackTrace();
            }
        }
        return is_last;
    }


    /**
     * очистка папки
     */
    public static void clearDir(String dir_path) {
        System.out.println("deleting...");
        for (File file : Objects.requireNonNull(new File(dir_path).listFiles()))
            if (!file.isDirectory()) file.delete();
        System.out.println("done!");
    }


    /**
     * розподіляє  великий фал на дрібніші і сортує їх
     *
     * @param file_path file_path
     * @param temp_dir  temp_dir
     */
    public static void splitFileBySizeAndQuickSort(String file_path, String temp_dir) {
        try {
            clearDir(temp_dir);

            BufferedReader reader = new BufferedReader(new FileReader(file_path));
            List<Integer> int_list = new ArrayList<>();

            String line = reader.readLine();
            // читаємо файл по лініі
            while (line != null) try {
                // якщо прочитали стільки, що переткнули рівень пам'яті - пишемо новий файл
                if (int_list.size() == memory_size) {
                    sortAndWrite(temp_dir + nextName(), int_list.stream().mapToInt(a -> a).toArray());
                    int_list.clear();
                }

                int_list.add(Integer.parseInt(line));
                line = reader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //якщо не вся інформація дописана - дописуємо
            if (int_list.size() > 0) {
                sortAndWrite(temp_dir + nextName(), int_list.stream().mapToInt(a -> a).toArray());
                int_list.clear();
            }
        } catch (Exception ie) {
            ie.printStackTrace();
        }
        System.out.println("split done!");
    }

    /**
     * наступне їм'я для файлу
     */
    public static String nextName() {
        int num = file_name_counter++;
        String ret = "";
        while (num > 0) {
            ret = ch.charAt(num % chl) + ret;
            num /= chl;
        }
        ret = ("0000" + ret).substring(ret.length()) + ".txt";
        return ret;
    }

    /**
     * спочатку сортує массив, потім записує його до файлу
     */
    static void sortAndWrite(String file_path, int[] arr) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file_path, false))) {
            quickSort(arr, 0, arr.length - 1);
            for (int num : arr) writer.write(num + "\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * сортує массив методом швидкого сортування
     */
    public static void quickSort(int[] array, int low, int high) {
        // завершити якщо нічого сортувати
        if (array.length == 0 || low >= high) return;

        // обрати головний елемент
        int middle = low + (high - low) / 2;
        int head = array[middle];

        // розділяємо на підмассиви
        int i = low, j = high;
        while (i <= j) {
            while (array[i] < head) i++;

            while (array[j] > head) j--;
            if (i <= j) {
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }

        // сортуємо ліву та праву частини
        if (low < j) quickSort(array, low, j);
        if (high > i) quickSort(array, i, high);
    }

    /**
     * створює файл з випадковими числами
     * createUnsorted(main_dir + sort_file, Integer.MAX_VALUE);
     */
    public static void createUnsorted(String file_path, int numbers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file_path, false))) {
            while (numbers-- > 0) {
                writer.write((int) (Math.random() * Integer.MAX_VALUE) + "\n");
                if (numbers % 100_000 == 0) System.out.println(numbers / 100_000 + " left");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
