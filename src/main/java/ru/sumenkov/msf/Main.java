package ru.sumenkov.msf;

import org.apache.commons.cli.*;
import ru.sumenkov.msf.service.MergeSortImpl;
import ru.sumenkov.msf.service.MergeSort;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("Аргументы запуска: " + Arrays.toString(args));

        CommandLineParser commandLineParser = new DefaultParser();
        Options options = new LaunchOptions().launchOptions();

        if (args.length == 0) helper(options);

        CommandLine commandLine;

        try {
            commandLine = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        int[] src = {
                1, 3, 8, 15, 19, 23
        };
        int[] src2 = {
                5, 6, 8, 9, 11, 45, 52
        };
        String[] strings = {
                "a", "f", "sd", "r", "ty", "j", "u", "yu", "n", "r", "s"
        };

        MergeSort ms = new MergeSortImpl();

        if (commandLine.hasOption("i")) {
            System.out.println("Merge sort Numbers\nOld: " + Arrays.toString(src) + " " + Arrays.toString(src2));
            int[] result = new int[src.length + src2.length];

            ms.merge(result, src, src2); // Слияние предварительно отсартированных массивов

            System.out.println("New: " + Arrays.toString(result));

        } else if (commandLine.hasOption("s")) {
            System.out.println("Merge sort Strings\nOld: " + Arrays.toString(strings));

            ms.mergeSort(strings); // Слияние массивов с нарушеной сортировкой

            System.out.println("New: " + Arrays.toString(strings));

        } else {
            helper(options);
        }
    }

    private static void helper(Options options){
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MergeSortFiles", options, true);
        System.exit(0);
    }
}
