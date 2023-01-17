package ru.sumenkov.msf;

import ru.sumenkov.msf.repository.FileCheck;
import ru.sumenkov.msf.repository.FileCheckImpl;
import ru.sumenkov.msf.repository.FileSort;
import ru.sumenkov.msf.repository.FileSortImpl;
import ru.sumenkov.msf.service.ArgsCheck;
import ru.sumenkov.msf.service.ArgsCheckImpl;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        ArgsCheck argsCheck = new ArgsCheckImpl(args);
        argsCheck.check();

        String sortDateType = argsCheck.sortDateType();
        String sortingDirection = argsCheck.sortingDirection();
        int startIndex = argsCheck.startIndex();
        String outputFile = args[startIndex - 1];

        FileCheck fileCheck = new FileCheckImpl();
        fileCheck.outputFile(outputFile);

        List<String> listFile = fileCheck.listFile(args, startIndex);

        FileSort fileSort = new FileSortImpl();
        fileSort.runSort(listFile, sortDateType, sortingDirection, outputFile);
    }
}
