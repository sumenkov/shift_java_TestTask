package ru.sumenkov.msf.service;

public class MergeSortImpl implements MergeSort{

    @Override
    public void mergeSort(int[] array, String sortingDirection) {

        int num = array.length;
        if (num < 2) return;

        int mid = num / 2;
        int[] left = new int[mid];
        int[] right = new int[num - mid];

        System.arraycopy(array, 0, left, 0, mid);
        System.arraycopy(array, mid, right, 0, num - mid);

        mergeSort(left, sortingDirection);
        mergeSort(right, sortingDirection);

        if (sortingDirection.equals("a"))
            mergeA(array, left, right);
        else
            mergeD(array, left, right);
    }

    @Override
    public void mergeSort(String[] array, String sortingDirection) {
        int num = array.length;
        if (num < 2) return;

        int mid = num / 2;
        String[] left = new String[mid];
        String[] right = new String[num - mid];

        System.arraycopy(array, 0, left, 0, mid);
        System.arraycopy(array, mid, right, 0, num - mid);

        mergeSort(left, sortingDirection);
        mergeSort(right, sortingDirection);

        if (sortingDirection.equals("a"))
            mergeA(array, left, right);
        else
            mergeD(array, left, right);
    }

    void mergeA(int[] array, int[] left, int[] right) {

        int k = 0, i = 0, j = 0;
        int leftLength = left.length;
        int rightLength = right.length;

        while (i < leftLength && j < rightLength) {
            if (left[i] <= right[j])
                array[k++] = left[i++];
            else
                array[k++] = right[j++];
        }

        while (i < leftLength) {
            array[k++] = left[i++];
        }

        while (j < rightLength) {
            array[k++] = right[j++];
        }
    }

    void mergeA(String[] array, String[] left, String[] right) {

        int k = 0, i = 0, j = 0;
        int leftLength = left.length;
        int rightLength = right.length;

        while (i < leftLength && j < rightLength) {
            if (left[i].compareTo(right[j]) <= 0)
                array[k++] = left[i++];
            else
                array[k++] = right[j++];
        }

        while (i < leftLength) {
            array[k++] = left[i++];
        }

        while (j < rightLength) {
            array[k++] = right[j++];
        }
    }

    void mergeD(int[] array, int[] left, int[] right) {

        int k = 0, i = 0, j = 0;
        int leftLength = left.length;
        int rightLength = right.length;

        while (i < leftLength && j < rightLength) {
            if (left[i] >= right[j])
                array[k++] = left[i++];
            else
                array[k++] = right[j++];
        }

        while (i < leftLength) {
            array[k++] = left[i++];
        }

        while (j < rightLength) {
            array[k++] = right[j++];
        }
    }

    void mergeD(String[] array, String[] left, String[] right) {

        int k = 0, i = 0, j = 0;
        int leftLength = left.length;
        int rightLength = right.length;

        while (i < leftLength && j < rightLength) {
            if (left[i].compareTo(right[j]) >= 0)
                array[k++] = left[i++];
            else
                array[k++] = right[j++];
        }

        while (i < leftLength) {
            array[k++] = left[i++];
        }

        while (j < rightLength) {
            array[k++] = right[j++];
        }
    }
}
