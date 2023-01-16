package ru.sumenkov.msf.service;

import java.io.File;
import java.io.IOException;

public interface SortCheck {
    boolean isSorted(File file, String sortDateType, String sortingDirection) throws IOException;
}
