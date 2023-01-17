package ru.sumenkov.msf;

import ru.sumenkov.msf.repository.FileSort;
import ru.sumenkov.msf.repository.FileSortImpl;
import ru.sumenkov.msf.service.ArgsCheck;
import ru.sumenkov.msf.service.ArgsCheckImpl;
import ru.sumenkov.msf.service.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ArgsCheck argsCheck = new ArgsCheckImpl(args);
        argsCheck.check();

        String sortDateType = argsCheck.sortDateType();
        String sortingDirection = argsCheck.sortingDirection();
        int startIndex = argsCheck.startIndex();
        String outFile = args[startIndex - 1];

        File of = new File(outFile);
        if (of.exists() && !of.isDirectory()) {
            Scanner reader = new Scanner(System.in);
            System.out.printf("Файл %s уже существует, заменить его на новый? (Y/n)\n", of.getName());
            String str = reader.next();
            if (str.equals("n") || str.equals("N") || str.equals("т") || str.equals("Т")) {
                System.out.println("Выход из программы.\nУкажите другое имя файла для сохранения результатов.");
                System.exit(0);
            }
            if (!of.delete()) {
                System.out.printf("Не удалось удалить файл %s\n", of.getName());
                System.exit(0);
            }
            reader.close();
        }

        List<String> inFiles = new ArrayList<>(Arrays.asList(args).subList(startIndex, args.length));
        for (int i = 0; i < inFiles.size(); i++) {
            File f = new File(inFiles.get(i));
            if (!f.exists() || f.isDirectory()) {
                System.out.printf("Файл %s не существует, пропускаем.\n", f.getName());
                inFiles.remove(f.getName());
                i--;
            } else if (f.length() == 0) {
                System.out.printf("Файл %s пустой, пропускаем.\n", f.getName());
                inFiles.remove(f.getName());
                i--;
            }
        }
        if (inFiles.size() == 0) {
            System.out.println("Нет файлов для чтения.");
            Utility.helper();
        }

        FileSort fileSort = new FileSortImpl();
        fileSort.runSort(inFiles, sortDateType, sortingDirection, outFile);

    }
}
