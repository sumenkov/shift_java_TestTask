package ru.sumenkov.msf.service;

import java.io.File;

public class Utility {

    public static void printHelpInto() {
        System.out.println("Проверьте параметры запуска программы:\n" +
                "usage: MergeSortFiles [OPTIONS] output.file input.files ...\n" +
                "[-a] [-d] [-i | -s]\n" +
                " -a   опционально: Сортировка по возрастанию\n" +
                " -d   опционально: Сортировка по убыванию\n" +
                " -i   обязательно: Сортировка целых чисел\n" +
                " -s   обязательно: Сортировка строк\n" +
                "output.file  обязательно: Имя файла для сохранения результата.\n" +
                "input.files  обязательно: Один, или более входных файлов.\n");
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void deleteDirectory(File file) {
        if (file.isDirectory()) {
            File[] contents = file.listFiles();
            if (contents != null) {
                for (File f : contents) {
                    deleteDirectory(f);
                }
            } else {
                System.out.printf("Не удалось найти файл %s\n", file.getName());
            }
        }

        if (file.exists() || file.isDirectory()) {
            if (!file.delete()) {
                System.out.printf("Не удалось удалить файл %s\n", file.getName());
            }
        }
    }
}
