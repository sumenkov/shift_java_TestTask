package ru.sumenkov.msf.service;

import ru.sumenkov.msf.SortDirection;

public interface MergeSort {
    //    void mergeSort(int[] array, String sortingDirection);
//    void mergeSort(String[] array, String sortingDirection);
    void mergeSort(Comparable[] array, SortDirection sortingDirection);
}
