package ru.sumenkov.msf;

import org.apache.commons.cli.*;

import ru.sumenkov.msf.mapper.ConvertingInArraysToOutArray;
import ru.sumenkov.msf.model.InputArrays;
import ru.sumenkov.msf.repository.ReaderInFiles;
import ru.sumenkov.msf.repository.WriterOutFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        CommandLineParser commandLineParser = new DefaultParser();
        Options options = new LaunchOptions().launchOptions();

        if (args.length < 3) LaunchOptions.helper(options);

        CommandLine commandLine;
        try {
            commandLine = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        int startIndex = Arrays.toString(args).contains("-a") || Arrays.toString(args).contains("-d") ? 3 : 2;
        List<Object> inFiles = new ArrayList<>(Arrays.asList(args).subList(startIndex, args.length));

        String sortingDirection = commandLine.hasOption("d") ? "d" : "a";

        InputArrays inputArrays = new InputArrays();

        if (commandLine.hasOption("i")) {
            for (Object file: inFiles) {
                int[] tmp = ReaderInFiles.readI(String.valueOf(file));
                inputArrays.getInArraysI().add(tmp);
            }

            WriterOutFile.writeI(
                    ConvertingInArraysToOutArray.convertI(inputArrays, sortingDirection),
                    args[startIndex - 1]
            );

        } else if (commandLine.hasOption("s")) {
            for (Object file: inFiles) {
                String[] tmp = ReaderInFiles.readS(String.valueOf(file));
                inputArrays.getInArraysS().add(tmp);
            }

            WriterOutFile.writeS(
                    ConvertingInArraysToOutArray.convertS(inputArrays, sortingDirection),
                    args[startIndex - 1]
            );
        } else {
            LaunchOptions.helper(options);
        }
    }
}
