package ru.sumenkov.msf.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Utility {

    public static void helper() {
        System.out.println("Проверьте параметры запуска программы:\n" +
                "usage: MergeSortFiles [OPTIONS] output.file input.files ...\n" +
                "[-a] [-d] [-i | -s]\n" +
                " -a   опционально: Сортировка по возрастанию\n" +
                " -d   опционально: Сортировка по убыванию\n" +
                " -i   обязательно: Сортировка целых чисел\n" +
                " -s   обязательно: Сортировка строк\n" +
                "output.file  обязательно: Имя файла для сохранения результата.\n" +
                "input.files  обязательно: Один, или более входных файлов.\n");
        System.exit(0);
    }
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static boolean allNullI(Integer[] ints) {
        boolean b = false;

        for (Integer i: ints) {
            if (i != null) {
                b = true;
                break;
            }
        }
        return b;
    }

    public static boolean allNullS(String[] strings) {
        boolean b = false;

        for (String i: strings) {
            if (i != null) {
                b = true;
                break;
            }
        }
        return b;
    }

    public static void closeFiles(List<BufferedReader> inputFiles) throws IOException {
        for (BufferedReader br: inputFiles) {
            br.close();
        }
    }

    public static void deleteDirectory(File file)
    {
        if (file.isDirectory())
        {
            File[] contents = file.listFiles();
            assert contents != null;
            for (File f: contents) {
                deleteDirectory(f);
            }
        }

        if (file.exists() || file.isDirectory()) {
            if (!file.delete())
                System.out.printf("Не удалось удалить файл %s\n", file.getName());
        }
    }
}
