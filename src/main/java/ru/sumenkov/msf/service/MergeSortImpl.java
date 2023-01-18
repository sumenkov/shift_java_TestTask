package ru.sumenkov.msf.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSortImpl implements MergeSort {

    @Override
    public <T extends Comparable<T>> void mergeSort(List<T> array, Comparator comparator) {

        int num = array.size();
        if (num < 2) {
            return;
        }

        int mid = num / 2;
        List<T> left = new ArrayList<>(array.subList(0, mid));
        List<T> right = new ArrayList<>(array.subList(mid, num));

        mergeSort(left, comparator);
        mergeSort(right, comparator);

        merge(array, left, right, comparator);
    }

    private <T extends Comparable<T>> void merge(List<T> array, List<T> left, List<T> right, Comparator<T> comparator) {
        int k = 0, i = 0, j = 0;
        int leftLength = left.size();
        int rightLength = right.size();

        while (i < leftLength && j < rightLength) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                    array.set(k++, left.get(i++));
            } else {
                array.set(k++, right.get(j++));
            }
        }

        while (i < leftLength) {
            array.set(k++, left.get(i++));
        }

        while (j < rightLength) {
            array.set(k++, right.get(j++));
        }
    }
}
