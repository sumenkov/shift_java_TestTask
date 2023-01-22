package ru.sumenkov.msf.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * Сортировка слиянием
 */
public interface MergeSort {
    /**
     * Сортировка массива слиянием
     * @param array Integer or String
     * @param comparator
     * @param <T>
     */
    <T extends Comparable<T>> void mergeSort(List<T> array, Comparator comparator);
    <T extends Comparable<T>> void mergeSort(File file, Comparator comparator) throws IOException;
}
