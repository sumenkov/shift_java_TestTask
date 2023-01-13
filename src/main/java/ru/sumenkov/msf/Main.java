package ru.sumenkov.msf;

import org.apache.commons.cli.*;
import ru.sumenkov.msf.service.ArrayCheck;
import ru.sumenkov.msf.service.ArrayCheckImpl;
import ru.sumenkov.msf.service.MergeSortImpl;
import ru.sumenkov.msf.service.MergeSort;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("Аргументы запуска: " + Arrays.toString(args) + "\n");

        CommandLineParser commandLineParser = new DefaultParser();
        Options options = new LaunchOptions().launchOptions();

        if (args.length == 0) helper(options);

        CommandLine commandLine;

        try {
            commandLine = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        int[] number = {
                9, 7, 4, 5, 4, 2, 1
        };
        int[] src2 = {
                5, 6, 8, 9, 11, 45, 52
        };
        String[] strings = {
                "e"
        };

        MergeSort ms = new MergeSortImpl();
        ArrayCheck ach = new ArrayCheckImpl();

//        ms.merge(result, src, src2); // Слияние предварительно отсартированных массивов
//        ms.mergeSort(strings); // Слияние массивов с нарушеной сортировкой
//        ach.isSortedA(number) // проверка, отсортирован ли массив, по возрастанию

        if (commandLine.hasOption("i")) {
            System.out.println(" ");

        } else if (commandLine.hasOption("s")) {
            System.out.println(" ");

        } else {
            helper(options);
        }
    }

    static void helper(Options options){
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MergeSortFiles", options, true);
        System.exit(0);
    }
}
