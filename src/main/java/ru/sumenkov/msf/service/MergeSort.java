package ru.sumenkov.msf.service;

public interface MergeSort {
    void mergeSort(int[] src);
    void mergeSort(String[] src);
    void merge(int[] src, int[] left, int[] right);
    void merge(String[] src, String[] left, String[] right);
}
