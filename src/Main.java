import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static final int size =1000000;
    static final int h = size / 2;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("Выберите режим: 1 - программа работает в 1 поток / 2 - программа работает в 2 потока");
            switch (scanner.nextInt()) {
                case 1:
                    oneThread(createArray());
                    break;
                case 2:
                    twoThreads(createArray());
                    break;
                default:return;
            }
        }
    }

    private static float[] createArray () {
        float [] array = new float[size];
        Arrays.fill(array, 1);//вместо цикла for
        return array;
    }

    private static void oneThread (float[] array) {
        long startTime = System.currentTimeMillis();
        modificateArray(array);
        System.out.println("Время выполнения программы в 1 поток(в милисекундах):");
        System.out.println(System.currentTimeMillis()- startTime);
    }

    private static void twoThreads(float[] array) {
        float[] firstArray = new float[h];
        float[] secondArray = new float[h];
        long start = System.currentTimeMillis();
        System.arraycopy(array, 0, firstArray, 0, h);
        System.arraycopy(array, h, secondArray, 0, h);
        Thread calculateFirst = new Thread(() -> { //IDEA считает, что лямбда выражение лучше
          modificateArray(firstArray); //IDEA не нравился дублирующийся код
        });
        Thread calculateSecond = new Thread(() -> {
            modificateArray(secondArray);
        });
        calculateFirst.start();
        calculateSecond.start();


        try {
            calculateFirst.join();
            calculateSecond.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.arraycopy(firstArray,0,array,0,h);
        System.arraycopy(secondArray,0,array,h,h);
        System.out.println("Время выполнения программы в 2 потока(в милисекундах):");
        System.out.println(System.currentTimeMillis()-start);
    }

    private static void modificateArray(float[] array) {
        for (int i =0; i<array.length; i++) {
            array[i] = (float)(array[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }

    }
}
