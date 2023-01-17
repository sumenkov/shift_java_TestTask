package ru.sumenkov.msf.repository;

import java.io.File;
import java.util.List;

public interface FileCheck {
    boolean outputFile(File outputFile);

    List<File> listFile(String[] args, int startIndex);
}
