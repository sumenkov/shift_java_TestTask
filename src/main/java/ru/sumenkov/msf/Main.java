package ru.sumenkov.msf;

import ru.sumenkov.msf.service.FileSort;
import ru.sumenkov.msf.service.FileSortImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        if (args.length < 3) helper();

        int startIndex = Arrays.toString(args).contains("-a") || Arrays.toString(args).contains("-d") ? 3 : 2;
        List<String> inFiles = new ArrayList<>(Arrays.asList(args).subList(startIndex, args.length));
        String sortDateType = null;
        String sortingDirection = Arrays.toString(args).contains("-d") ? "d" : "a";

        if (Arrays.toString(args).contains("-i")) sortDateType = "i";
        else if (Arrays.toString(args).contains("-s")) sortDateType = "s";
        else helper();

        FileSort fileSort = new FileSortImpl();
        try {
            for (String inFile : inFiles) {
                fileSort.fileSort(new File(inFile), sortDateType, sortingDirection);
            }

            fileSort.fewFiles(sortDateType, sortingDirection, args[startIndex - 1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void helper() {
        System.out.println("usage: MergeSortFiles [OPTIONS] output.file input.files ...\n" +
                "[-a] [-d] [-i | -s]\n" +
                " -a   опционально: Сортировка по возрастанию\n" +
                " -d   опционально: Сортировка по убыванию\n" +
                " -i   обязательно: Сортировка целых чисел\n" +
                " -s   обязательно: Сортировка строк\n" +
                "output.file  обязательно: Имя файла для сохранения результата.\n" +
                "input.files  обязательно: Один, или более входных файлов.\n");
        System.exit(0);
    }
}
