package ru.sumenkov.msf.repository;

import ru.sumenkov.msf.SortDataType;

import java.io.File;
import java.util.Comparator;
import java.util.List;

/**
 * Сортировка данных в файлах, чтение/запись
 */
public interface FileSort {
    /**
     * Запуск процесса чтения файлов
     * @param inFiles
     * @param sortDateType
     * @param comparator
     * @param outputFile
     */
    void runSort(List<File> inFiles, SortDataType sortDateType, Comparator comparator, File outputFile);
}
