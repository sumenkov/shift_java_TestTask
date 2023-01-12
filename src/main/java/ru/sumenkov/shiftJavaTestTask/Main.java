package ru.sumenkov.shiftJavaTestTask;

import ru.sumenkov.shiftJavaTestTask.service.MergeSort;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] src = {
            6, 10, 5, 8, 5, 13, 87, 109, 1, 34, 11
        };

        System.out.println(Arrays.toString(src));

        MergeSort ms = new MergeSort();
        ms.mergeSort(src);

        System.out.println(Arrays.toString(src));
    }
}