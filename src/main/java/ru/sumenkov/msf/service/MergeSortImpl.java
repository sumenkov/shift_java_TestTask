package ru.sumenkov.msf.service;

public class MergeSortImpl implements MergeSort{

    @Override
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

    @Override
    public void mergeSort(String[] src) {
        int num = src.length;
        if (num < 2) return;

        int mid = num / 2;
        String[] left = new String[mid];
        String[] right = new String[num - mid];

        System.arraycopy(src, 0, left, 0, mid);
        System.arraycopy(src, mid, right, 0, num - mid);

        mergeSort(left);
        mergeSort(right);

        merge(src, left, right);
    }

    public void merge(int[] src, int[] left, int[] right) {

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

    public void merge(String[] src, String[] left, String[] right) {

        int k = 0, i = 0, j = 0;
        int leftLength = left.length;
        int rightLength = right.length;

        while (i < leftLength && j < rightLength) {
            if (left[i].compareTo(right[j]) <= 0)
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
