package ru.sumenkov.msf;

import ru.sumenkov.msf.repository.*;
import ru.sumenkov.msf.service.ArgsCheck;
import ru.sumenkov.msf.service.ArgsCheckImpl;

import java.io.File;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ArgsCheck argsCheck = new ArgsCheckImpl(args);
        if (argsCheck.check()) {
            SortDataType sortDataType = argsCheck.getSortDataType();
            SortDirection sortingDirection = argsCheck.getSortDirection();
            Comparator comparator = sortingDirection == SortDirection.DESC
                    ? Comparator.reverseOrder() : Comparator.naturalOrder();
            int indexInputFile = argsCheck.getIndexInputFile();
            File outputFileName = new File(args[indexInputFile - 1]);

            FileCheck fileCheck = new FileCheckImpl();

            if (fileCheck.outputFile(outputFileName)) {
                List<File> listFile = fileCheck.listFile(args, indexInputFile);
                if (listFile.size() != 0) {
                    FileSort fileSort = new FileSortImpl();
                    fileSort.runSort(listFile, sortDataType, comparator, outputFileName);
                }
            }
        }
    }
}
