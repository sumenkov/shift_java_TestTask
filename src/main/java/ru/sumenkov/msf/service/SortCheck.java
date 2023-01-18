package ru.sumenkov.msf.service;

import ru.sumenkov.msf.SortDataType;
import ru.sumenkov.msf.SortDirection;

import java.io.File;

public interface SortCheck {
    boolean isSorted(File file, SortDataType sortDateType, SortDirection sortDirection);
}
