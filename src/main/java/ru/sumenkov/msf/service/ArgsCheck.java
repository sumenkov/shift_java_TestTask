package ru.sumenkov.msf.service;

import ru.sumenkov.msf.SortDataType;
import ru.sumenkov.msf.SortDirection;

/**
 *
 */
public interface ArgsCheck {
    /**
     * Проверка параметров запуска и инициализация переменных для геттеров:
     * {@link #getSortDataType()}, {@link #getSortDirection()}, {@link #getIndexInputFile()}
     */
    boolean check();

    /**
     * Для инициализации значения вызови хотя бы один раз {@link #check()}
     */
    SortDataType getSortDataType();

    /**
     * Для инициализации значения вызови хотя бы один раз {@link #check()}
     */
    SortDirection getSortDirection();

    /**
     * Для инициализации значения вызови хотя бы один раз {@link #check()}
     */
    int getIndexInputFile();
}
