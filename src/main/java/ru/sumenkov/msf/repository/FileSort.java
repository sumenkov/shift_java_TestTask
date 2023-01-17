package ru.sumenkov.msf.repository;

import ru.sumenkov.msf.SortDataType;
import ru.sumenkov.msf.SortDirection;

import java.io.File;
import java.util.List;

public interface FileSort {
    void runSort(List<File> inFiles, SortDataType sortDateType, SortDirection sortingDirection, File outputFile);
}
