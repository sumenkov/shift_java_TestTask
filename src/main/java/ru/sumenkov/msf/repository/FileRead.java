package ru.sumenkov.msf.repository;

import java.io.File;
import java.util.List;

public interface FileRead {
    List<? extends Comparable> read(File file);
}
