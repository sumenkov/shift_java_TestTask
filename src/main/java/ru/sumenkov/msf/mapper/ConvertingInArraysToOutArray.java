package ru.sumenkov.msf.mapper;

import ru.sumenkov.msf.model.InputArrays;
import ru.sumenkov.msf.model.OutputArray;
import ru.sumenkov.msf.service.ArrayCheck;
import ru.sumenkov.msf.service.ArrayCheckImpl;
import ru.sumenkov.msf.service.MergeSort;
import ru.sumenkov.msf.service.MergeSortImpl;

import java.util.List;

public class ConvertingInArraysToOutArray {

    public static OutputArray convertI(InputArrays inputArrays, String sortingDirection) {

        List<int[]> inArraysI = inputArrays.getInArraysI();
        int length = 0;
        int[] tmp = new int[length];

        ArrayCheck check = new ArrayCheckImpl();
        MergeSort mergeSort = new MergeSortImpl();

        for (int[] array: inArraysI) {
            if (sortingDirection.equals("a")) {
                if (!check.isSortedA(array)) {
                    mergeSort.mergeSort(array, sortingDirection);
                }
            } else {
                if (!check.isSortedD(array)) {
                    mergeSort.mergeSort(array, sortingDirection);
                }
            }

            length += array.length;
        }

        OutputArray outputArray = new OutputArray();
        outputArray.setOutArrayI(new int[length]);

        MergeSort ms = new MergeSortImpl();

        for (int[] array : inArraysI) {
            ms.mergeA(outputArray.getOutArrayI(), tmp, array); // Слияние предварительно отсартированных массивов
        }

        return outputArray;
    }
}
