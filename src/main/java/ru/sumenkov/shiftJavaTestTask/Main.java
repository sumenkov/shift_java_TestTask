package ru.sumenkov.shiftJavaTestTask;

import ru.sumenkov.shiftJavaTestTask.service.MergeSort;
import ru.sumenkov.shiftJavaTestTask.service.MergeSortImpl;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        int[] src = {
            6, 10, 5, 8, 5, 13, 87, 109, 1, 34, 11
        };
        String[] src2 = {
                "a", "f", "sd", "r", "ty", "j", "u", "yu", "n", "r", "s"
        };

        MergeSort ms = new MergeSortImpl();

        System.out.println(Arrays.toString(src));
        ms.mergeSort(src);
        System.out.println(Arrays.toString(src));

        System.out.println(Arrays.toString(src2));
        ms.mergeSort(src2);
        System.out.println(Arrays.toString(src2));
    }
}