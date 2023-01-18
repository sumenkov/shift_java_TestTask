package ru.sumenkov.msf.service;

import ru.sumenkov.msf.SortDataType;
import ru.sumenkov.msf.SortDirection;

import java.io.File;

/**
 * Проверка файла на состояние сортировки данных.
 */
public interface SortCheck {
    /**
     * Возвращает true, если файл отсортирован, и наоборот.
     * @param file - файл
     * @param sortDateType - тип данных
     * @param sortDirection - направление сортировки
     * @return - Истина / Лож
     */
    boolean isSorted(File file, SortDataType sortDateType, SortDirection sortDirection);
}
