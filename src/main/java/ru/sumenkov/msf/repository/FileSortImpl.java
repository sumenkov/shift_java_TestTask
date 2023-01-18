package ru.sumenkov.msf.repository;

import ru.sumenkov.msf.SortDataType;
import ru.sumenkov.msf.SortDirection;
import ru.sumenkov.msf.service.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileSortImpl implements FileSort {

    public static final File TMP = new File("tmp/");

    @Override
    public void runSort(List<File> inFiles, SortDataType sortDateType, SortDirection sortDirection, File outputFile) {

            long freeMemory = Runtime.getRuntime().freeMemory() / 3;
            for (File inFile : inFiles) {
                fileSort(inFile, sortDateType, sortDirection, freeMemory);
            }
            fewFiles(sortDateType, sortDirection, outputFile);
    }

    void fileSecondSort(File inFile, SortDataType sortDateType, SortDirection sortingDirection, File outputFile) {

            long freeMemory = Runtime.getRuntime().freeMemory() / 15;
            fileSort(inFile, sortDateType, sortingDirection, freeMemory);
            fewFiles(sortDateType, sortingDirection, outputFile);
    }

    void fileSort(File file, SortDataType sortDateType, SortDirection sortingDirection, long freeMemory) {

        SortCheck sortCheck = new SortCheckImpl();

        if (sortCheck.isSorted(file, sortDateType, sortingDirection)) {
            Path oldFile = file.toPath();
            Path newFile = Paths.get("tmp/" + file.getName() + ".sort");
            try {
                Files.copy(oldFile, newFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.printf("Не удалось скопировать файл %s или записать новый файл %s\n", oldFile, newFile);
            }
        } else if (file.length() >= freeMemory) {
            splitBigFile(file, sortDateType, sortingDirection, freeMemory);
        } else {
            smallFile(file, sortDateType, sortingDirection);
        }
    }

    <T extends Comparable<T>> void smallFile(File file, SortDataType sortDateType, SortDirection sortingDirection) {

        MergeSort mergeSort = new MergeSortImpl();
        FileRead fileRead = sortDateType == SortDataType.INTEGER ? new FileReadInteger() : new FileReadString();

        try (FileWriter fw = fileWriter(file)) {
            List<T> array = fileRead.read(file);

            mergeSort.mergeSort(array, sortingDirection);
            for (Object num : array) {
                fw.append(String.valueOf(num)).append("\n");
            }

        } catch (IOException e) {
            System.out.printf("Ошибка во время сортировки файла %s\n", file.getName());
        }
    }

    FileWriter fileWriter(File file) throws IOException {
        FileWriter fw;
        if (file.getName().split("/")[0].equals("tmp")) {
            fw = new FileWriter(file + ".stmp");
        } else {
            fw = new FileWriter("tmp/" + file + ".stmp");
        }
        return fw;
    }

    void splitBigFile(File file, SortDataType sortDateType, SortDirection sortingDirection, long freeMemory) {

        int partCounter = 1;
        long fileLength = file.length();
        long maxPartCounter = fileLength % freeMemory == 0 ?
                fileLength / freeMemory : fileLength / freeMemory + 1;
        long allRows = numberOfLines(file);
        long maxRows = allRows / maxPartCounter;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            FileWriter fw = new FileWriter("tmp/" + file.getName() + partCounter + ".tmp");

            String row;
            int rownum = 1;
            while ((row = br.readLine()) != null) {
                rownum++;
                fw.append(row).append("\n");
                if ((rownum / maxRows) > (partCounter - 1)) {
                    fw.close();

                    File newFile = new File("tmp/" + file.getName() + partCounter + ".tmp");
                    smallFile(newFile, sortDateType, sortingDirection);

                    if (!file.delete()) {
                        System.out.printf("Не удалось удалить файл %s\n", file.getName());
                    }

                    partCounter++;
                    fw = new FileWriter("tmp/" + file.getName() + partCounter + ".tmp");
                }
            }
            fw.close();
        } catch (IOException e) {
            System.out.printf("Ошибка чтения файла %s\n", file.getName());
        }
    }

    private long numberOfLines(File file) {
        long rows = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.readLine() != null) {
                rows++;
            }
        } catch (IOException e) {
            System.out.printf("Ошибка чтения файла %s\n", file.getName());
        }
        return rows;
    }

    public void fewFiles(SortDataType sortDateType, SortDirection sortingDirection, File outputFile) {

        List<String> filesNames = new ArrayList<>();

        for (File tmpFile : Objects.requireNonNull(Objects.requireNonNull(TMP).listFiles())) {
            if (tmpFile.length() != 0) {
                filesNames.add("tmp/" + tmpFile.getName());
            }
        }

        if (filesNames.size() == 0) {
            System.out.println("Нет данных для сортировки.");
            Utility.deleteDirectory(TMP);
            return;
        } else if (filesNames.size() == 1) {
            if (filesNames.get(0).contains(".sort")) {
                Path oldFile = new File(filesNames.get(0)).toPath();
                Path newFile = Paths.get(outputFile.getName());
                try {
                    Files.copy(oldFile, newFile, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    System.out.printf("Не удалось скопировать файл %s или записать новый файл %s\n", oldFile, newFile);
                }
                System.out.printf("Файл с результатами сохранен как %s\n", outputFile.getName());
            } else {
                splitOneInTwo(new File(filesNames.get(0)), sortDateType, sortingDirection, outputFile);
            }
        } else if (filesNames.size() == 2 || filesNames.size() == 3) {
            File fileLeft = new File(filesNames.get(0));
            File fileRight = new File(filesNames.get(1));

            mergeTwoInOne(fileLeft, fileRight, sortDateType, sortingDirection, outputFile);
            fileSecondSort(outputFile, sortDateType, sortingDirection, outputFile);

        } else {
            int numOfFile = filesNames.size();
            if (!(numOfFile % 2 == 0)) {
                numOfFile -= 1;
            }

            File fileLeft, fileRight;

            for (int i = 0; i < numOfFile / 2; i++) {
                fileLeft = new File("tmp/" + filesNames.get(i) + ".left");
                fileRight = new File("tmp/" + filesNames.get(i++) + ".right");

                mergeTwoInOne(fileLeft, fileRight, sortDateType, sortingDirection, outputFile);
            }

//            List<BufferedReader> inputFiles = new ArrayList<>();
//            for (int i = 0; i < numOfFile; i++) {
//                try {
//                    inputFiles.add(new BufferedReader(new FileReader(filesNames.get(i))));
//                } catch (FileNotFoundException e) {
//                    System.out.printf("Ошибка чтения файла %s\n", filesNames.get(i));
//                }
//            }






//        } else if (filesNames.size() >= 3) {
//            List<BufferedReader> inputFiles = new ArrayList<>();
//            try (FileWriter fw = new FileWriter(outputFile)) {
//                for (String fileName : filesNames) {
//                    inputFiles.add(new BufferedReader(new FileReader(fileName)));
//                }
//
//                if (sortDateType == SortDataType.INTEGER) {
//                    Integer[] ints = new Integer[inputFiles.size()];
//                    for (int i = 0; i < inputFiles.size(); i++) {
//                        String tmp = inputFiles.get(i).readLine();
//                        if (tmp != null)
//                            ints[i] = Integer.parseInt(tmp);
//                    }
//                    if (sortingDirection == SortDirection.ASC) {
//                        while (Utility.allNull(ints)) {
//                            if (ints.length > 1) {
//                                int indexOfMin = 0;
//                                for (int i = 0; i < ints.length; i++) {
//                                    if (ints[i] != null && ints[indexOfMin] != null) {
//                                        if (ints[i] < ints[indexOfMin]) {
//                                            indexOfMin = i;
//                                        }
//                                    } else {
//                                        for (int j = 0; j < ints.length; j++) {
//                                            if (ints[j] != null) {
//                                                indexOfMin = j;
//                                            }
//                                        }
//                                    }
//                                }
//                                writeLineI(fw, ints, inputFiles, indexOfMin);
//                            } else {
//                                writeLineI(fw, ints, inputFiles, 0);
//                            }
//                            fw.flush();
//                        }
//                    } else if (sortingDirection == SortDirection.DESC) {
//                        while (Utility.allNull(ints)) {
//                            if (ints.length > 1) {
//                                int indexOfMin = 0;
//                                for (int i = 0; i < ints.length; i++) {
//                                    if (ints[i] != null && ints[indexOfMin] != null) {
//                                        if (ints[i] > ints[indexOfMin]) {
//                                            indexOfMin = i;
//                                        }
//                                    } else {
//                                        for (int j = 0; j < ints.length; j++) {
//                                            if (ints[j] != null) {
//                                                indexOfMin = j;
//                                            }
//                                        }
//                                    }
//                                }
//                                writeLineI(fw, ints, inputFiles, indexOfMin);
//                            } else {
//                                writeLineI(fw, ints, inputFiles, 0);
//                            }
//                            fw.flush();
//                        }
//                    }
//                } else if (sortDateType == SortDataType.STRING) {
//                    String[] strings = new String[inputFiles.size()];
//                    for (int i = 0; i < inputFiles.size(); i++) {
//                        while (true) {
//                            String line = inputFiles.get(i).readLine();
//                            if (!line.contains(" ") && !line.equals("")) {
//                                strings[i] = line;
//                                break;
//                            }
//                        }
//                    }
//                    if (sortingDirection == SortDirection.ASC) {
//                        while (Utility.allNull(strings)) {
//                            if (strings.length > 1) {
//                                int indexOfMin = 0;
//                                for (int i = 0; i < strings.length; i++) {
//                                    if (strings[i] != null && strings[indexOfMin] != null) {
//                                        if (strings[i].compareTo(strings[indexOfMin]) <= 0) {
//                                            indexOfMin = i;
//                                        }
//                                    } else {
//                                        for (int j = 0; j < strings.length; j++) {
//                                            if (strings[j] != null) {
//                                                indexOfMin = j;
//                                            }
//                                        }
//                                    }
//                                }
//                                fw.append(strings[indexOfMin]).append("\n");
//                                strings[indexOfMin] = inputFiles.get(indexOfMin).readLine();
//                            } else {
//                                fw.append(strings[0]).append("\n");
//                                strings[0] = inputFiles.get(0).readLine();
//                            }
//                            fw.flush();
//                        }
//                    } else if (sortingDirection == SortDirection.DESC) {
//                        while (Utility.allNull(strings)) {
//                            if (strings.length > 1) {
//                                int indexOfMin = 0;
//                                for (int i = 0; i < strings.length; i++) {
//                                    if (strings[i] != null && strings[indexOfMin] != null) {
//                                        if (strings[i].compareTo(strings[indexOfMin]) >= 0) {
//                                            indexOfMin = i;
//                                        }
//                                    } else {
//                                        for (int j = 0; j < strings.length; j++) {
//                                            if (strings[j] != null) {
//                                                indexOfMin = j;
//                                            }
//                                        }
//                                    }
//                                }
//                                fw.append(strings[indexOfMin]).append("\n");
//                                strings[indexOfMin] = inputFiles.get(indexOfMin).readLine();
//                            } else {
//                                fw.append(strings[0]).append("\n");
//                                strings[0] = inputFiles.get(0).readLine();
//                            }
//                            fw.flush();
//                        }
//                    }
//                }
//            } catch (IOException e) {
//                System.out.println(e.getMessage());
//            }
//            Utility.closeFiles(inputFiles);
//            Utility.deleteFiles(TMP);
            fileSecondSort(outputFile, sortDateType, sortingDirection, outputFile);
            fewFiles(sortDateType, sortingDirection, outputFile);
        }
        Utility.deleteDirectory(TMP);
    }

    void splitOneInTwo(File file, SortDataType sortDateType, SortDirection sortingDirection, File outputFile) {
        File fileLeft = new File("tmp/" + file.getName() + ".left");
        File fileRight = new File("tmp/" + file.getName() + ".right");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            try (FileWriter left = new FileWriter(fileLeft);
                 FileWriter right = new FileWriter(fileRight)) {

                while (true) {
                    String tmp = br.readLine();
                    if (tmp != null) {
                        left.append(tmp);
                        tmp = br.readLine();
                        if (tmp != null) {
                            right.append(tmp);
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.printf("Ошибка чтения файла %s\n", file.getName());
        }
        if (!file.delete()) {
            System.out.printf("Не удалось удалить файл %s\n", file.getName());
        }

        mergeTwoInOne(fileLeft, fileRight, sortDateType, sortingDirection, outputFile);
    }

    void mergeTwoInOne(File fileLeft, File fileRight, SortDataType sortDateType, SortDirection sortingDirection, File outputFile) {
        try (BufferedReader left = new BufferedReader(new FileReader(fileLeft));
             BufferedReader right = new BufferedReader(new FileReader(fileRight))) {
            try (FileWriter fw = new FileWriter(outputFile)) {
                List<BufferedReader> list = new ArrayList<>();
                list.add(left);
                list.add(right);

                Comparable x = sortDateType == SortDataType.INTEGER ?
                        Integer.parseInt(list.get(0).readLine()) : list.get(0).readLine();
                Comparable y = sortDateType == SortDataType.STRING ?
                        Integer.parseInt(list.get(1).readLine()) : list.get(1).readLine();

                while (true) {
                    if (sortingDirection == SortDirection.ASC) {
                        if (x != null && y != null) {
                            if (x.compareTo(y) <= 0) {
                                xWriteAndRead(fw, x, sortDateType, list);
                            } else {
                                yWriteAndRead(fw, y, sortDateType, list);
                            }
                        } else {
                            if (!xOryNull(fw, x, y, sortDateType, list)) {
                                break;
                            }
                        }
                    } else if (sortingDirection == SortDirection.DESC) {
                        if (x != null && y != null) {
                            if (x.compareTo(y) >= 0) {
                                xWriteAndRead(fw, x, sortDateType, list);
                            } else {
                                yWriteAndRead(fw, y, sortDateType, list);
                            }
                        } else {
                            if (!xOryNull(fw, x, y, sortDateType, list)) {
                                break;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.printf("Ошибка чтения файла %s или %s\n", fileLeft.getName(), fileRight.getName());
        }
        if (!fileLeft.delete()) {
            System.out.printf("Не удалось удалить файл %s\n", fileLeft.getName());
        }
        if (!fileRight.delete()) {
            System.out.printf("Не удалось удалить файл %s\n", fileRight.getName());
        }
    }

    boolean xWriteAndRead(FileWriter fw, Comparable x, SortDataType sortDateType, List<BufferedReader> list)
            throws IOException {
        fw.append(x.toString()).append("\n");
        x = sortDateType == SortDataType.INTEGER ?
                Integer.parseInt(list.get(0).readLine()) : list.get(0).readLine();
        return x != null;
    }

    boolean yWriteAndRead(FileWriter fw, Comparable y, SortDataType sortDateType, List<BufferedReader> list)
            throws IOException {
        fw.append(y.toString()).append("\n");
        y = sortDateType == SortDataType.INTEGER ?
                Integer.parseInt(list.get(0).readLine()) : list.get(0).readLine();
        return y != null;
    }

    boolean xOryNull(FileWriter fw, Comparable x, Comparable y, SortDataType sortDateType, List<BufferedReader> list)
            throws IOException {
        if (x == null && y == null) {
            return false;
        } else if (x == null) {
            while (true) {
                if (!yWriteAndRead(fw, y, sortDateType, list)) {
                    break;
                }
            }
            return false;
        } else {
            while (true) {
                if (!xWriteAndRead(fw, x, sortDateType, list)) {
                    break;
                }
            }
            return false;
        }
    }
//
//    void writeLineI(FileWriter fw, Integer[] ints, List<BufferedReader> inputFiles, Integer index) throws IOException {
//
//        fw.append(String.valueOf(ints[index])).append("\n");
//
//        String tmp = inputFiles.get(index).readLine();
//
//        if (tmp == null) {
//            ints[index] = null;
//        } else {
//            ints[index] = Integer.parseInt(tmp);
//        }
//    }
}
