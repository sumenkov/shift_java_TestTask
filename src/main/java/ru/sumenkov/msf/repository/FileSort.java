package ru.sumenkov.msf.repository;

import java.util.List;

public interface FileSort {
    void runSort(List<String> inFiles, String sortDateType, String sortingDirection, String outFile);
}
