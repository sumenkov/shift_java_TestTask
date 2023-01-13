package ru.sumenkov.msf.mapper;

import ru.sumenkov.msf.model.InputArrays;
import ru.sumenkov.msf.model.OutputArray;
import ru.sumenkov.msf.service.MergeSort;
import ru.sumenkov.msf.service.MergeSortImpl;

import java.util.List;

public class ConvertingInArraysToOutArray {

    public static OutputArray convert(InputArrays inputArrays) {

        List<int[]> inArraysI = inputArrays.getInArraysI();
        int length = 0;
        int[] tmp = new int[length];

        for (int[] array: inArraysI) {
            length += array.length;
        }

        OutputArray outputArray = new OutputArray();
        outputArray.setOutArrayI(new int[length]);

        MergeSort ms = new MergeSortImpl();

        for (int[] array : inArraysI) {
            ms.merge(outputArray.getOutArrayI(), tmp, array); // Слияние предварительно отсартированных массивов
        }

        return outputArray;
    }
}
