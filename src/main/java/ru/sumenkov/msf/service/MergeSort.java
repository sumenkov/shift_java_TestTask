package ru.sumenkov.msf.service;

import ru.sumenkov.msf.SortDirection;

import java.util.List;

public interface MergeSort {
    <T extends Comparable<T>> void mergeSort(List<T> array, SortDirection sortingDirection);
}
