package ru.sumenkov.msf.service;

import java.io.File;
import java.io.IOException;

public interface FileSort {
    void fileSort(File file, String sortDateType, String sortingDirection) throws IOException;
    void fewFiles(String sortDateType, String sortingDirection, String outFile) throws IOException;
}
