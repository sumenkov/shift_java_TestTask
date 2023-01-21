package ru.sumenkov.msf.repository;

import java.io.File;
import java.util.List;

/**
 * Чтение файла, с проверкой на плохие строки
 * @param <T>
 */
public interface FileRead<T extends Comparable<T>> {
    public static final String ENCODING = "UTF-8";
    /**
     * Запуск чтения файла
     * @param file
     * @return
     */
//    List<T> read(File file);
    File read(File file);
}
