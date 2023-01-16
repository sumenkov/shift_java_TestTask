package ru.sumenkov.msf.service;

import ru.sumenkov.msf.repository.ReaderInFiles;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileSortImpl implements FileSort {

    public void fileSort(File file, String sortDateType, String sortingDirection) throws IOException {

        new File("tmp/").mkdir();

        if (file.length() >= Runtime.getRuntime().freeMemory()) {
            splitBigFile(file, sortDateType, sortingDirection);
        } else {
            smallFile(file.getName(), sortDateType, sortingDirection);
        }
    }

    void smallFile(String file, String sortDateType, String sortingDirection) throws IOException {

        MergeSort mergeSort = new MergeSortImpl();

        FileWriter fw;
        if (file.split("/")[0].equals("tmp"))
            fw = new FileWriter(file + ".stmp");
        else
            fw = new FileWriter("tmp/" + file + ".stmp");

        if (sortDateType.equals("i")) {
            int[] ints = ReaderInFiles.readI(file);
            mergeSort.mergeSort(ints, sortingDirection);

            for (int num: ints) {
                fw.append(String.valueOf(num)).append("\n");
            }

            fw.close();
        }
        else if (sortDateType.equals("s")) {
            String[] strings = ReaderInFiles.readS(file);
            mergeSort.mergeSort(strings, sortingDirection);

            for (String str: strings) {
                fw.append(str).append("\n");
            }

            fw.close();
        } else {
            System.out.println("Не определен формат данных для сортировки");
        }
    }

    void splitBigFile(File file, String sortDateType, String sortingDirection) {

        int partCounter = 1;
        long sizeOfFiles = Runtime.getRuntime().freeMemory();
        long fileLength = file.length();

        long maxPartCounter = fileLength % sizeOfFiles == 0 ?
                fileLength / sizeOfFiles : fileLength / sizeOfFiles + 1;

        long allRows = 0;
        try(FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader)) {

            while (br.readLine() != null) {
                allRows++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long maxRows = allRows / maxPartCounter;

        try(FileReader reader = new FileReader(file);
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

                    newFile.delete();

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
            if (!tmpFile.getName().contains("rtmp"))
                filesNames.add("tmp/" + tmpFile.getName());
        }

        List<BufferedReader> inputFiles = new ArrayList<>();
        FileWriter fw = new FileWriter(outFile);

        for (String fileName: filesNames) {
            inputFiles.add(new BufferedReader(new FileReader(fileName)));
        }

        if (sortDateType.equals("i")) {

            Integer[] ints = new Integer[inputFiles.size()];

            for (int i = 0; i < inputFiles.size(); i++) {
                String tmp = inputFiles.get(i).readLine();
                ints[i] = Integer.parseInt(tmp);

            }

            if (sortingDirection.equals("a")){

                while (allNullI(ints)) {

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

                        writeLineI(fw, ints,inputFiles, indexOfMin);

                    } else {

                        writeLineI(fw, ints, inputFiles, 0);
                    }
                    fw.flush();
                }
                fw.close();

            } else if (sortingDirection.equals("d")) {

                while (allNullI(ints)) {

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

                        writeLineI(fw, ints,inputFiles, indexOfMin);

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

            if (sortingDirection.equals("a")){

                while (allNullS(strings)) {

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

                while (allNullS(strings)) {

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
        closeFiles(inputFiles);
        deleteDirectory(new File("tmp"));
    }

    boolean allNullI(Integer[] ints) {
        boolean b = false;

        for (Integer i: ints) {
            if (i != null) {
                b = true;
                break;
            }
        }
        return b;
    }

    boolean allNullS(String[] strings) {
        boolean b = false;

        for (String i: strings) {
            if (i != null) {
                b = true;
                break;
            }
        }
        return b;
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

    void closeFiles(List<BufferedReader> inputFiles) throws IOException {
        for (BufferedReader br: inputFiles) {
            br.close();
        }
    }

    public static void deleteDirectory(File file)
    {
        if (file.isDirectory())
        {
            File[] contents = file.listFiles();
            assert contents != null;
            for (File f: contents) {
                deleteDirectory(f);
            }
        }
        file.delete();
    }
}
