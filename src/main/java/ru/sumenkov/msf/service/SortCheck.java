package ru.sumenkov.msf.service;

import java.io.File;
import java.util.Comparator;

/**
 * Проверка файла на состояние сортировки данных.
 */
public interface SortCheck {
    /**
     * Возвращает true, если файл отсортирован, и наоборот.
     * @param file - файл
     * @param comparator - направление сортировки
     * @return - Истина / Лож
     */
    boolean isSorted(File file, Comparator comparator);
}
