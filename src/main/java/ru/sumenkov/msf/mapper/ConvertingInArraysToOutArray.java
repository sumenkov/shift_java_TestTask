package ru.sumenkov.msf.mapper;

import ru.sumenkov.msf.model.InputArrays;
import ru.sumenkov.msf.model.OutputArray;
import ru.sumenkov.msf.service.MergeSort;
import ru.sumenkov.msf.service.MergeSortImpl;

import java.util.List;

public class ConvertingInArraysToOutArray {

    public static OutputArray convertI(InputArrays inputArrays, String sortingDirection) {

        List<int[]> inArraysI = inputArrays.getInArraysI();
        int length = 0;

        for (int[] array: inputArrays.getInArraysI()) {
            length += array.length;
        }

        OutputArray outputArray = new OutputArray();
        outputArray.setOutArrayI(new int[length]);

        int index = 0;
        for (int[] array: inArraysI) {
            for (int j : array) {
                outputArray.getOutArrayI()[index] = j;
                index++;
            }
        }

        MergeSort ms = new MergeSortImpl();
        ms.mergeSort(outputArray.getOutArrayI(), sortingDirection);

        return outputArray;
    }

    public static OutputArray convertS(InputArrays inputArrays, String sortingDirection) {
        List<String[]> inArraysS = inputArrays.getInArraysS();
        int length = 0;

        for (String[] array: inArraysS) {
            length += array.length;
        }

        OutputArray outputArray = new OutputArray();
        outputArray.setOutArrayS(new String[length]);

        int index = 0;
        for (String[] array: inArraysS) {
            for (String j : array) {
                outputArray.getOutArrayS()[index] = j;
                index++;
            }
        }

        MergeSort ms = new MergeSortImpl();
        ms.mergeSort(outputArray.getOutArrayS(), sortingDirection);

        return outputArray;
    }
}
