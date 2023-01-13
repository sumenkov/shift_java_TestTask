package ru.sumenkov.msf.service;

public interface MergeSort {
    void mergeSort(int[] array, String sortingDirection);
    void mergeSort(String[] array, String sortingDirection);
    void mergeA(int[] array, int[] left, int[] right);
    void mergeA(String[] array, String[] left, String[] right);
}
