package ru.sumenkov.msf.repository;

import ru.sumenkov.msf.SortDataType;
import ru.sumenkov.msf.SortDirection;

import java.io.File;
import java.util.List;

/**
 * Сортировка данных в файлах, чтение/запись
 */
public interface FileSort {
    /**
     * Запуск процесса чтения файлов
     * @param inFiles
     * @param sortDateType
     * @param sortDirection
     * @param outputFile
     */
    void runSort(List<File> inFiles, SortDataType sortDateType, SortDirection sortDirection, File outputFile);
}
