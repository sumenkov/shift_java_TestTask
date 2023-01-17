package ru.sumenkov.msf.service;

import ru.sumenkov.msf.SortDirection;

public class MergeSortImpl implements MergeSort {

    @Override
    public void mergeSort(Comparable[] array, SortDirection sortingDirection) {

        int num = array.length;
        if (num < 2) return;

        int mid = num / 2;
        Comparable[] left = new Comparable[mid];
        Comparable[] right = new Comparable[num - mid];

        System.arraycopy(array, 0, left, 0, mid);
        System.arraycopy(array, mid, right, 0, num - mid);

        mergeSort(left, sortingDirection);
        mergeSort(right, sortingDirection);

        merge(array, left, right, sortingDirection);
    }

    private void merge(Comparable[] array, Comparable[] left, Comparable[] right, SortDirection sortingDirection) {
        int k = 0, i = 0, j = 0;
        int leftLength = left.length;
        int rightLength = right.length;

        while (i < leftLength && j < rightLength) {
            if (sortingDirection == SortDirection.ASC) {
                if (left[i].compareTo(right[j]) <= 0) {
                    array[k++] = left[i++];
                } else {
                    array[k++] = right[j++];
                }
            } else if (sortingDirection == SortDirection.DESC) {
                if (left[i].compareTo(right[j]) >= 0) {
                    array[k++] = left[i++];
                } else {
                    array[k++] = right[j++];
                }
            }
        }

        while (i < leftLength) {
            array[k++] = left[i++];
        }

        while (j < rightLength) {
            array[k++] = right[j++];
        }
    }
}
