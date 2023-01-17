package ru.sumenkov.msf.repository;

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

    @Override
    public void runSort(List<String> inFiles, String sortDateType, String sortingDirection, String outFile) {

        try {
            if (!new File("tmp/").mkdir())
                System.out.println("Не удалось создать директорию для временных файлов.");

            long freeMemory = Runtime.getRuntime().freeMemory() / 3;
            for (String inFile : inFiles) {
                try {
                    fileSort(new File(inFile), sortDateType, sortingDirection, freeMemory);
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
            fewFiles(sortDateType, sortingDirection, outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void fileSecondSort(String inFile, String sortDateType, String sortingDirection, String outFile) {
        try {
            if (!new File("tmp/").mkdir())
                System.out.println("Не удалось создать директорию для временных файлов.");

            long freeMemory = Runtime.getRuntime().freeMemory() / 15;

            fileSort(new File(inFile), sortDateType, sortingDirection, freeMemory);
            fewFiles(sortDateType, sortingDirection, outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void fileSort(File file, String sortDateType, String sortingDirection, long freeMemory) throws IOException {

        SortCheck sortCheck = new SortCheckImpl();

        if (sortCheck.isSorted(file, sortDateType, sortingDirection)) {
            Path oldFile = file.toPath();
            Path newFile = Paths.get("tmp/" + file.getName() + ".sort");
            Files.copy(oldFile, newFile, StandardCopyOption.REPLACE_EXISTING);
        } else {
            if (file.length() >= freeMemory) {
                splitBigFile(file, sortDateType, sortingDirection, freeMemory);
            } else {
                smallFile(file.getName(), sortDateType, sortingDirection);
            }
        }
    }

    void smallFile(String file, String sortDateType, String sortingDirection) throws IOException {

        MergeSort mergeSort = new MergeSortImpl();
        FileRead fileRead = new FileReadImpl();
        FileWriter fw;

        if (file.split("/")[0].equals("tmp"))
            fw = new FileWriter(file + ".stmp");
        else
            fw = new FileWriter("tmp/" + file + ".stmp");

        if (sortDateType.equals("i")) {
            int[] ints = fileRead.readI(file);
            if (ints.length != 0) {
                mergeSort.mergeSort(ints, sortingDirection);
                for (int num : ints) {
                    fw.append(String.valueOf(num)).append("\n");
                }
            }
            fw.close();
        }
        else if (sortDateType.equals("s")) {
            String[] strings = fileRead.readS(file);
            mergeSort.mergeSort(strings, sortingDirection);

            for (String str: strings) {
                fw.append(str).append("\n");
            }
            fw.close();
        } else {
            System.out.println("Не определен формат данных для сортировки");
        }
    }

    void splitBigFile(File file, String sortDateType, String sortingDirection, long freeMemory) {

        int partCounter = 1;
        long fileLength = file.length();
        long maxPartCounter = fileLength % freeMemory == 0 ?
                fileLength / freeMemory : fileLength / freeMemory + 1;
        long allRows = 0;

        try (FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader)) {
            while (br.readLine() != null) {
                allRows++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long maxRows = allRows / maxPartCounter;

        try (FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader)) {
            String row;
            int rownum = 1;
            FileWriter fw = new FileWriter("tmp/" + file.getName() + partCounter + ".tmp");

            while ((row = br.readLine()) != null) {
                rownum ++;
                fw.append(row).append("\n");
                if ((rownum / maxRows) > (partCounter - 1)) {
                    fw.close();

                    File newFile = new File("tmp/" + file.getName() + partCounter + ".tmp");
                    smallFile("tmp/" + newFile.getName(), sortDateType, sortingDirection);

                    if (!file.delete())
                        System.out.printf("Не удалось удалить файл %s\n", file.getName());

                    partCounter++;
                    fw = new FileWriter("tmp/" + file.getName() + partCounter + ".tmp");
                }
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fewFiles(String sortDateType, String sortingDirection, String outFile) throws IOException {

        List<String> filesNames = new ArrayList<>();

        for (File tmpFile: Objects.requireNonNull(new File("tmp").listFiles())) {
            if (tmpFile.length() != 0)
                filesNames.add("tmp/" + tmpFile.getName());
        }

        if (filesNames.size() == 0) {
            System.out.println("Нет данных для сортировки.");
            Utility.deleteDirectory(new File("tmp"));
            System.exit(0);
        } else if (filesNames.size() == 1 && filesNames.get(0).contains(".sort")) {
            Path oldFile = new File(filesNames.get(0)).toPath();
            Path newFile = Paths.get(outFile);
            Files.copy(oldFile, newFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.printf("Файл с результатами сохранен как %s\n", outFile);
        } else {

            List<BufferedReader> inputFiles = new ArrayList<>();
            FileWriter fw = new FileWriter(outFile);

            for (String fileName : filesNames) {
                inputFiles.add(new BufferedReader(new FileReader(fileName)));
            }
            if (sortDateType.equals("i")) {
                Integer[] ints = new Integer[inputFiles.size()];
                for (int i = 0; i < inputFiles.size(); i++) {
                    String tmp = inputFiles.get(i).readLine();
                    if (tmp != null)
                        ints[i] = Integer.parseInt(tmp);
                }
                if (sortingDirection.equals("a")) {
                    while (Utility.allNullI(ints)) {
                        if (ints.length > 1) {
                            int indexOfMin = 0;
                            for (int i = 0; i < ints.length; i++) {
                                if (ints[i] != null && ints[indexOfMin] != null) {
                                    if (ints[i] < ints[indexOfMin]) {
                                        indexOfMin = i;
                                    }
                                } else {
                                    for (int j = 0; j < ints.length; j++) {
                                        if (ints[j] != null)
                                            indexOfMin = j;
                                    }
                                }
                            }
                            writeLineI(fw, ints, inputFiles, indexOfMin);
                        } else {
                            writeLineI(fw, ints, inputFiles, 0);
                        }
                        fw.flush();
                    }
                    fw.close();
                } else if (sortingDirection.equals("d")) {
                    while (Utility.allNullI(ints)) {
                        if (ints.length > 1) {
                            int indexOfMin = 0;
                            for (int i = 0; i < ints.length; i++) {
                                if (ints[i] != null && ints[indexOfMin] != null) {
                                    if (ints[i] > ints[indexOfMin]) {
                                        indexOfMin = i;
                                    }
                                } else {
                                    for (int j = 0; j < ints.length; j++) {
                                        if (ints[j] != null)
                                            indexOfMin = j;
                                    }
                                }
                            }
                            writeLineI(fw, ints, inputFiles, indexOfMin);
                        } else {
                            writeLineI(fw, ints, inputFiles, 0);
                        }
                        fw.flush();
                    }
                    fw.close();
                }
            } else if (sortDateType.equals("s")) {
                String[] strings = new String[inputFiles.size()];
                for (int i = 0; i < inputFiles.size(); i++) {
                    while (true) {
                        String line = inputFiles.get(i).readLine();
                        if (!line.contains(" ") && !line.equals("")) {
                            strings[i] = line;
                            break;
                        }
                    }
                }
                if (sortingDirection.equals("a")) {
                    while (Utility.allNullS(strings)) {
                        if (strings.length > 1) {
                            int indexOfMin = 0;
                            for (int i = 0; i < strings.length; i++) {
                                if (strings[i] != null && strings[indexOfMin] != null) {
                                    if (strings[i].compareTo(strings[indexOfMin]) <= 0) {
                                        indexOfMin = i;
                                    }
                                } else {
                                    for (int j = 0; j < strings.length; j++) {
                                        if (strings[j] != null)
                                            indexOfMin = j;
                                    }
                                }
                            }
                            fw.append(strings[indexOfMin]).append("\n");
                            strings[indexOfMin] = inputFiles.get(indexOfMin).readLine();
                        } else {
                            fw.append(strings[0]).append("\n");
                            strings[0] = inputFiles.get(0).readLine();
                        }
                        fw.flush();
                    }
                    fw.close();
                } else if (sortingDirection.equals("d")) {
                    while (Utility.allNullS(strings)) {
                        if (strings.length > 1) {
                            int indexOfMin = 0;
                            for (int i = 0; i < strings.length; i++) {
                                if (strings[i] != null && strings[indexOfMin] != null) {
                                    if (strings[i].compareTo(strings[indexOfMin]) >= 0) {
                                        indexOfMin = i;
                                    }
                                } else {
                                    for (int j = 0; j < strings.length; j++) {
                                        if (strings[j] != null)
                                            indexOfMin = j;
                                    }
                                }
                            }
                            fw.append(strings[indexOfMin]).append("\n");
                            strings[indexOfMin] = inputFiles.get(indexOfMin).readLine();
                        } else {
                            fw.append(strings[0]).append("\n");
                            strings[0] = inputFiles.get(0).readLine();
                        }
                        fw.flush();
                    }
                    fw.close();
                }
            }
            Utility.closeFiles(inputFiles);
            Utility.deleteDirectory(new File("tmp"));
            fileSecondSort(outFile, sortDateType, sortingDirection, outFile);
        }
        Utility.deleteDirectory(new File("tmp"));
    }

    void writeLineI(FileWriter fw, Integer[] ints, List<BufferedReader> inputFiles, Integer index) throws IOException {

        fw.append(String.valueOf(ints[index])).append("\n");

        String tmp = inputFiles.get(index).readLine();

        if (tmp == null) {
            ints[index] = null;
        } else {
            ints[index] = Integer.parseInt(tmp);
        }
    }
}
