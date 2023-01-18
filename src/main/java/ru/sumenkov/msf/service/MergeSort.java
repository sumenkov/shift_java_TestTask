package ru.sumenkov.msf.service;

import ru.sumenkov.msf.SortDirection;

import java.util.List;

/**
 * Сортировка слиянием
 */
public interface MergeSort {
    /**
     * Сортировка массива слиянием
     * @param array Integer or String
     * @param sortingDirection
     * @param <T>
     */
    <T extends Comparable<T>> void mergeSort(List<T> array, SortDirection sortingDirection);
}
