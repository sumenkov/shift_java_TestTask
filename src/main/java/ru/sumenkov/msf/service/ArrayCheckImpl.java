package ru.sumenkov.msf.service;

public class ArrayCheckImpl implements ArrayCheck {

    @Override
    public boolean isSortedA(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1])
                return false;
        }
        return true;
    }

    @Override
    public boolean isSortedD(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] < array[i + 1])
                return false;
        }
        return true;
    }

    @Override
    public boolean isSortedA(String[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i].compareTo(array[i + 1]) > 0)
                return false;
        }
        return true;
    }

    @Override
    public boolean isSortedD(String[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i].compareTo(array[i + 1]) < 0)
                return false;
        }
        return true;
    }
}
