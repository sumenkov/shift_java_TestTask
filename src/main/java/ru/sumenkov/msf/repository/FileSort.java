package ru.sumenkov.msf.repository;

import ru.sumenkov.msf.SortDataType;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * Сортировка данных в файлах, чтение/запись
 */
public interface FileSort {
    /**
     * Кодировка чтения файлов
     */
    String ENCODING = "UTF-8";
    /**
     * Директория для хранения временных файлов
     */
    File TMP = new File("tmp/");
    /**
     * Запуск процесса чтения файлов
     */
    void runSort(List<File> inFiles, SortDataType sortDateType, Comparator comparator, File outputFile) throws IOException;
}
