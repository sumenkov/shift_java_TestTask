package ru.sumenkov.msf.service;

import ru.sumenkov.msf.SortDirection;

import java.util.ArrayList;
import java.util.List;

public class MergeSortImpl implements MergeSort {

    @Override
    public <T extends Comparable<T>> void mergeSort(List<T> array, SortDirection sortingDirection) {

        int num = array.size();
        if (num < 2) {
            return;
        }

        int mid = num / 2;
        List<T> left = new ArrayList<>(array.subList(0, mid));
        List<T> right = new ArrayList<>(array.subList(num - mid, num));

//        System.arraycopy(array, 0, left, 0, mid);
//        System.arraycopy(array, mid, right, 0, num - mid);

        mergeSort(left, sortingDirection);
        mergeSort(right, sortingDirection);

        merge(array, left, right, sortingDirection);
    }

    private <T extends Comparable<T>> void merge(List<T> array, List<T> left, List<T> right, SortDirection sortingDirection) {
        int k = 0, i = 0, j = 0;
        int leftLength = left.size();
        int rightLength = right.size();

        while (i < leftLength && j < rightLength) {
            if (sortingDirection == SortDirection.ASC) {
                if (left.get(i).compareTo(right.get(j)) <= 0) {
                    array.set(k++, left.get(i++));
//                    array[k++] = left[i++];
                } else {
                    array.set(k++, right.get(j++));
//                    array[k++] = right[j++];
                }
            } else if (sortingDirection == SortDirection.DESC) {
                if (left.get(i).compareTo(right.get(j)) >= 0) {
                    array.set(k++, left.get(i++));
//                    array[k++] = left[i++];
                } else {
                    array.set(k++, right.get(j++));
//                    array[k++] = right[j++];
                }
            }
        }

        while (i < leftLength) {
            array.set(k++, left.get(i++));
//            array[k++] = left[i++];
        }

        while (j < rightLength) {
            array.set(k++, right.get(j++));
//            array[k++] = right[j++];
        }
    }
}
