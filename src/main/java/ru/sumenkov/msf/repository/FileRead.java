package ru.sumenkov.msf.repository;

import java.io.File;
import java.util.List;

public interface FileRead<T extends Comparable<T>> {
    List<T> read(File file);
}
