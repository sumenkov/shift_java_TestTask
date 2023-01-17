package ru.sumenkov.msf.repository;

import ru.sumenkov.msf.service.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FileCheckImpl implements FileCheck {
    @Override
    public boolean outputFile(File outputFile) {

        if (outputFile.exists() && !outputFile.isDirectory()) {
            try (Scanner reader = new Scanner(System.in)) {
                System.out.printf("Файл %s уже существует, заменить его на новый? (Y/n)\n", outputFile.getName());
                String str = reader.next();
                if (str.equals("n") || str.equals("N") || str.equals("т") || str.equals("Т")) {
                    System.out.println("Выход из программы.\nУкажите другое имя файла для сохранения результатов.");
                    return false;
                }

                if (!outputFile.delete()) {
                    System.out.printf("Не удалось удалить файл %s\n", outputFile.getName());
                    return false;
                }
            }
        }

        if (!FileSortImpl.TMP.isDirectory()) {
            if (!FileSortImpl.TMP.mkdir()) {
                System.out.println("Не удалось создать директорию для временных файлов.");
                return false;
            }
        }

        return true;
    }

    @Override
    public List<File> listFile(String[] args, int indexInputFile) {

        List<String> allListFile = new ArrayList<>(Arrays.asList(args).subList(indexInputFile, args.length));
        List<File> result = new ArrayList<>();

        for (String s : allListFile) {
            File file = new File(s);
            if (checkFile(file)) {
                result.add(file);
            }
        }

        if (result.size() == 0) {
            System.out.println("Нет файлов для чтения.");
            Utility.printHelpInto();
        }

        return result;
    }

    private boolean checkFile(File file) {
        if (!file.exists() || file.isDirectory()) {
            System.out.printf("Файл %s не существует, пропускаем.\n", file.getName());
            return false;
        } else if (file.length() == 0) {
            System.out.printf("Файл %s пустой, пропускаем.\n", file.getName());
            return false;
        }
        return true;
    }
}
