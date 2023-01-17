package ru.sumenkov.msf.repository;

import ru.sumenkov.msf.service.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FileCheckImpl implements FileCheck {
    @Override
    public void outputFile(String outputFile) {

        File of = new File(outputFile);
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
    }

    @Override
    public List<String> listFile(String[] args, int startIndex) {

        List<String> listFile = new ArrayList<>(Arrays.asList(args).subList(startIndex, args.length));

        for (int i = 0; i < listFile.size(); i++) {
            File f = new File(listFile.get(i));
            if (!f.exists() || f.isDirectory()) {
                System.out.printf("Файл %s не существует, пропускаем.\n", f.getName());
                listFile.remove(f.getName());
                i--;
            } else if (f.length() == 0) {
                System.out.printf("Файл %s пустой, пропускаем.\n", f.getName());
                listFile.remove(f.getName());
                i--;
            }
        }

        if (listFile.size() == 0) {
            System.out.println("Нет файлов для чтения.");
            Utility.helper();
        }
        return listFile;
    }
}
