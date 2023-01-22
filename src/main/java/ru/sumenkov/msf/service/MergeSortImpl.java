package ru.sumenkov.msf.service;

import ru.sumenkov.msf.repository.FileSort;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    @Override
    public <T extends Comparable<T>> void mergeSort(File file, Comparator comparator) throws IOException {
        BufferedReader lines = new BufferedReader(
                new InputStreamReader(
                        Files.newInputStream(
                                Paths.get(file.getPath())),
                        FileSort.ENCODING));
        List<T> array = new ArrayList<>();
        while (true) {
            String line = lines.readLine();
            if (line != null) {
                array.add((T) line);
            } else {
                break;
            }
        }

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

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        Files.newOutputStream(
                                Paths.get("tmp/" + file.getName().split("\\.")[0] + ".s")),
                        FileSort.ENCODING));
        for (Comparable<T> value: array) {
            writer.write(value.toString());
            writer.newLine();
        }
        writer.close();

        if (!file.delete()) {
            System.out.printf("Не удалось удалить файл %s\n", file.getName());
        }
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
