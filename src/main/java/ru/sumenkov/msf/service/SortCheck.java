package ru.sumenkov.msf.service;

import ru.sumenkov.msf.SortDataType;
import ru.sumenkov.msf.SortDirection;

import java.io.File;
import java.io.IOException;

public interface SortCheck {
    boolean isSorted(File file, SortDataType sortDateType, SortDirection sortingDirection) throws IOException;
}
