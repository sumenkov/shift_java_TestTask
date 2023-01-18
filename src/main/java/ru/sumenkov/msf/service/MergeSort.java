package ru.sumenkov.msf.service;

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
}
