package ru.sumenkov.msf.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;

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

    public static boolean allNull(Object[] array) {
        boolean b = false;

        for (Object i : array) {
            if (i != null) {
                b = true;
                break;
            }
        }
        return b;
    }

    public static void closeFiles(List<BufferedReader> inputFiles) {
        for (BufferedReader br : inputFiles) {
            try {
                br.close();
            } catch (IOException e) {
                System.out.println("Не смог закрыть поток данных из массива потоков.");
            }
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

    public static void deleteFiles(File file) {
        if (file.isDirectory()) {
            File[] contents = file.listFiles();
            if (contents != null) {
                for (File f : contents) {
                    if (f.isFile()) {
                        if (!f.delete()) {
                            System.out.printf("Не удалось удалить файл %s\n", file.getName());
                        }
                    } else if (f.isDirectory()) {
                        deleteDirectory(f);
                    }
                }
            } else {
                System.out.printf("Не удалось найти файл %s\n", file.getName());
            }
        }
    }
}
