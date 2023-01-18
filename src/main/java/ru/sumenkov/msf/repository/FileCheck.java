package ru.sumenkov.msf.repository;

import java.io.File;
import java.util.List;

/**
 * Проверка файлов на доступность
 */
public interface FileCheck {
    /**
     * Проверка файла для записи результатов сортировки
     * @param outputFile
     * @return
     */
    boolean outputFile(File outputFile);

    /**
     * Проверка списка файлов для чтения данных
     * @param args
     * @param startIndex
     * @return
     */
    List<File> listFile(String[] args, int startIndex);
}
