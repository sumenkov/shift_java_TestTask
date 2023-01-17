package ru.sumenkov.msf.service;

import ru.sumenkov.msf.SortDataType;
import ru.sumenkov.msf.SortDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgsCheckImpl implements ArgsCheck {
    private final String[] args;
    private SortDataType sortDataType;
    private SortDirection sortDirection;
    private int indexInputFile;

    public ArgsCheckImpl(String[] args) {
        this.args = args;
    }

    @Override
    public void check() {
        List<String> list = new ArrayList<>(Arrays.asList("-i", "-s", "-a", "-d"));
        for (String arg : this.args) {
            if (arg.contains("-") && !list.contains(arg)) {
                System.out.println("Неизвестный параметр запуска.");
                Utility.printHelpInto();
            }
        }

        boolean sortTypeInt = Arrays.binarySearch(args, "-i") >= 0;
        boolean sortTypeStr = Arrays.binarySearch(args, "-s") >= 0;
        boolean sortDirectionAsc = Arrays.binarySearch(args, "-a") >= 0;
        boolean sortDirectionDesc = Arrays.binarySearch(args, "-d") >= 0;

        int lengthArgs = this.args.length;

        if (lengthArgs < 3) {
            Utility.printHelpInto();
        } else if (sortTypeInt && sortTypeStr) {
            Utility.printHelpInto();
        } else if (sortDirectionAsc && sortDirectionDesc) {
            Utility.printHelpInto();
        } else if ((sortDirectionAsc || sortDirectionDesc) && lengthArgs < 4) {
            System.out.println("Не указан файл для записи или чтения.");
            Utility.printHelpInto();
        }

        if (sortTypeInt) {
            this.sortDataType = SortDataType.INTEGER;
        } else if (sortTypeStr) {
            this.sortDataType = SortDataType.STRING;
        } else {
            Utility.printHelpInto();
        }

        this.sortDirection = sortDirectionDesc ? SortDirection.DESC : SortDirection.ASC;
        this.indexInputFile = sortDirectionAsc || sortDirectionDesc ? 3 : 2;
    }

    public SortDataType getSortDataType() {
        return sortDataType;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public int getIndexInputFile() {
        return indexInputFile;
    }
}
