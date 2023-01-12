package ru.sumenkov.shiftJavaTestTask.service;

public class MergeSort {

    public void mergeSort(int[] src) {

        int num = src.length;
        if (num < 2) return;

        int mid = num / 2;
        int[] left = new int[mid];
        int[] right = new int[num - mid];

        System.arraycopy(src, 0, left, 0, mid);
        System.arraycopy(src, mid, right, 0, num - mid);

        mergeSort(left);
        mergeSort(right);

        merge(src, left, right);
    }

    protected void merge(int[] src, int[] left, int[] right) {

        int k = 0, i = 0, j = 0;
        int leftLength = left.length;
        int rightLength = right.length;

        while (i < leftLength && j < rightLength) {
            if (left[i] <= right[j])
                src[k++] = left[i++];
            else
                src[k++] = right[j++];
        }

        while (i < leftLength) {
            src[k++] = left[i++];
        }

        while (j < rightLength) {
            src[k++] = right[j++];
        }
    }
}
