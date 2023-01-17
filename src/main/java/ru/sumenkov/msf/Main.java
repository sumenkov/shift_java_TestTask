package ru.sumenkov.msf;

import ru.sumenkov.msf.service.FileSort;
import ru.sumenkov.msf.service.FileSortImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        for (String arg: args) {
            List<String> list = new ArrayList<>(Arrays.asList("-i", "-s", "-a", "-d"));
            if (arg.contains("-")) {
                if (!list.contains(arg)) {
                    System.out.println("Неизвестный параметр запуска.");
                    helper();
                }
            }
        }

        int lengthArgs = args.length;
        if (lengthArgs < 3) helper();
        else if (Arrays.toString(args).contains("-i") && Arrays.toString(args).contains("-s")) helper();
        else if (Arrays.toString(args).contains("-a") && Arrays.toString(args).contains("-d")) helper();
        else if ((Arrays.toString(args).contains("-a") || Arrays.toString(args).contains("-d")) && lengthArgs < 4) {
            System.out.println("Не указан файл для записи или чтения.");
            helper();
        }

        String sortDateType = null;
        if (Arrays.toString(args).contains("-i")) sortDateType = "i";
        else if (Arrays.toString(args).contains("-s")) sortDateType = "s";
        else helper();

        String sortingDirection = Arrays.toString(args).contains("-d") ? "d" : "a";
        int startIndex = Arrays.toString(args).contains("-a") || Arrays.toString(args).contains("-d") ? 3 : 2;

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
            helper();
        }

        FileSort fileSort = new FileSortImpl();
        fileSort.filesSort(inFiles, sortDateType, sortingDirection, outFile);

    }

    static void helper() {
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
}
