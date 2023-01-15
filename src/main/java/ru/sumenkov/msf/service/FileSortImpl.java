package ru.sumenkov.msf.service;

import ru.sumenkov.msf.repository.ReaderInFiles;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileSortImpl implements FileSort {

    public void fileSort(File file, String sortDateType, String sortingDirection) throws IOException {

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
            System.out.println("Неизвестно направление сортировки");
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


            FileWriter fw = new FileWriter("tmp/" + partCounter + ".tmp");

            while ((row = br.readLine()) != null) {
                rownum ++;
                fw.append(row).append("\n");

                if ((rownum / maxRows) > (partCounter - 1)) {
                    fw.close();

                    File newFile = new File("tmp/" + partCounter + ".tmp");

                    smallFile("tmp/" + newFile.getName(), sortDateType, sortingDirection);

                    newFile.delete()

                    partCounter++;
                    fw = new FileWriter("tmp/" + partCounter + ".tmp");
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

        System.out.println(filesNames);

        List<BufferedReader> inputFiles = new ArrayList<>();
        FileWriter fw = new FileWriter("tmp/result.rtmp");

        for (String fileName: filesNames) {
            inputFiles.add(new BufferedReader(new FileReader(fileName)));
        }

        if (sortDateType.equals("i")) {
            Integer[] ints = new Integer[inputFiles.size()];
            for (int i = 0; i < inputFiles.size(); i++) {
                while (true) {
                    String tmp = inputFiles.get(i).readLine();
                    if (isNumeric(tmp)) {
                        ints[i] = Integer.parseInt(tmp);
                        break;
                    }
                }
            }

            if (sortingDirection.equals("a")){

                while (allNull(ints)) {

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

                        fw.append(String.valueOf(ints[indexOfMin])).append("\n");
                        fw.flush();

                        while (true) {

                            String tmp = inputFiles.get(indexOfMin).readLine();

                            if (isNumeric(tmp)) {
                                ints[indexOfMin] = Integer.parseInt(tmp);
                                break;
                            }

                            if (tmp == null) {
                                ints[indexOfMin] = null;
                                break;
                            }
                        }
                    } else {
                        fw.append(String.valueOf(ints[0])).append("\n");
                        fw.flush();

                        while (true) {

                            String tmp = inputFiles.get(0).readLine();

                            if (isNumeric(tmp)) {
                                ints[0] = Integer.parseInt(tmp);
                                break;
                            }

                            if (tmp == null) {
                                ints[0] = null;
                                break;
                            }
                        }
                    }
                }
                fw.close();
            }
        }
    }

    static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    static boolean allNull(Integer[] ints) {
        boolean b = false;

        for (Integer i: ints) {
            if (i != null) {
                b = true;
                break;
            }
        }
        return b;
    }
}
