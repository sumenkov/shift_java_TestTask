package ru.sumenkov.msf.repository;

import java.util.List;

public interface FileCheck {
    void outputFile(String outputFile);
    List<String> listFile(String[] args, int startIndex);
}
