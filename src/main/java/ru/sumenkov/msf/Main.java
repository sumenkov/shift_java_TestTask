package ru.sumenkov.msf;

import org.apache.commons.cli.*;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("Аргументы запуска: " + Arrays.toString(args) + "\n");

        CommandLineParser commandLineParser = new DefaultParser();
        Options options = new LaunchOptions().launchOptions();

        if (args.length < 3) LaunchOptions.helper(options);

        CommandLine commandLine;

        try {
            commandLine = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

//        MergeSort ms = new MergeSortImpl();
//        ms.merge(result, src, src2); // Слияние предварительно отсартированных массивов
//        ms.mergeSort(strings); // Слияние массивов с нарушеной сортировкой

//        ArrayCheck ach = new ArrayCheckImpl();
//        ach.isSortedA(number) // проверка, отсортирован ли массив, по возрастанию

        if (commandLine.hasOption("i")) {
            System.out.println(" ");

        } else if (commandLine.hasOption("s")) {
            System.out.println(" ");

        } else {
            LaunchOptions.helper(options);
        }
    }
}
