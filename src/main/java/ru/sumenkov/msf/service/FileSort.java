package ru.sumenkov.msf.service;

import java.util.List;

public interface FileSort {
    void filesSort(List<String> inFiles, String sortDateType, String sortingDirection, String outFile);
}
