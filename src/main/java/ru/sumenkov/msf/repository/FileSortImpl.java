package ru.sumenkov.msf.repository;

import ru.sumenkov.msf.SortDataType;
import ru.sumenkov.msf.service.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class FileSortImpl implements FileSort {

    public static final File TMP = new File("tmp/");

    @Override
    public void runSort(List<File> inFiles, SortDataType sortDateType, Comparator comparator, File outputFile) {

//            long freeMemory = Runtime.getRuntime().freeMemory();
            for (File inFile : inFiles) {
                fileSort(inFile, sortDateType, comparator);
            }
            fewFiles(sortDateType, comparator, outputFile);
    }

    void nextFileSort(File inFile, SortDataType sortDateType, Comparator comparator, File outputFile) {
//            long freeMemory = Runtime.getRuntime().freeMemory();
            fileSort(inFile, sortDateType, comparator);
            fewFiles(sortDateType, comparator, outputFile);
    }

    void fileSort(File file, SortDataType sortDateType, Comparator comparator) {

        SortCheck sortCheck = new SortCheckImpl();

        if (sortCheck.isSorted(file, sortDateType, comparator)) {
            if (!file.renameTo(new File("tmp/" + file.getName().split("\\.")[0] + ".sort"))) {
                System.out.printf("Не удалось переименовать файл %s\n", file);
            }
        } else {
            if (!file.renameTo(new File("tmp/" + file.getName().split("\\.")[0] + ".ns"))) {
                System.out.printf("Не удалось переименовать файл %s\n", file);
            }
        }
//        } else if (file.length() >= freeMemory) {
//            splitBigFile(file, sortDateType, comparator, freeMemory);
//        } else {
//            smallFile(file, sortDateType, comparator);
//        }
    }

//    <T extends Comparable<T>> void smallFile(File file, SortDataType sortDateType, Comparator comparator) {
//
//        MergeSort mergeSort = new MergeSortImpl();
////        FileRead fileRead = sortDateType == SortDataType.INTEGER ? new FileReadInteger() : new FileReadString();
//        FileRead fileRead = new FileReadString();
//
//        try (FileWriter fw = fileWriter(file)) {
////            List<T> array = fileRead.read(file);
//            с
//
////            mergeSort.mergeSort(array, comparator);
//
////            for (Object obj : array) {
////                fw.append(String.valueOf(obj)).append("\n");
////            }
//
//            fw.flush();
//        } catch (IOException e) {
//            System.out.printf("Ошибка во время сортировки файла %s\n", file.getName());
//        }
//    }

//    FileWriter fileWriter(File file) throws IOException {
//        FileWriter fw;
//        if (file.getName().split("/")[0].equals("tmp")) {
//            fw = new FileWriter(file.getName() + ".s");
//        } else {
//            fw = new FileWriter("tmp/" + file.getName() + ".s");
//        }
//        return fw;
//    }

//    void splitBigFile(File file, SortDataType sortDateType, Comparator comparator, long freeMemory) {
//
//        int partCounter = 1;
//        long fileLength = file.length();
//        long maxPartCounter = fileLength % freeMemory == 0 ?
//                fileLength / freeMemory : fileLength / freeMemory + 1;
//        long allRows = numberOfLines(file);
//        long maxRows = allRows / maxPartCounter;
//
//        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//            FileWriter fw = new FileWriter("tmp/" + file.getName() + partCounter);
//
//            String row;
//            int rownum = 1;
//            while ((row = br.readLine()) != null) {
//                rownum++;
//                fw.append(row).append("\n");
//                if ((rownum / maxRows) > (partCounter - 1)) {
//                    fw.close();
//
//                    File newFile = new File("tmp/" + file.getName() + partCounter);
//                    smallFile(newFile, sortDateType, comparator);
//
//                    if (!newFile.delete()) {
//                        System.out.printf("Не удалось удалить файл %s\n", newFile.getName());
//                    }
//
//                    partCounter++;
//                    fw = new FileWriter("tmp/" + file.getName() + partCounter);
//                }
//            }
//            fw.close();
//        } catch (IOException e) {
//            System.out.printf("Ошибка чтения файла %s\n", file.getName());
//        }
//    }

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

    public void fewFiles(SortDataType sortDateType, Comparator comparator, File outputFile) {

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
            String fileName = filesNames.get(0);
            if (fileName.contains(".sort")) {
                Path oldFile = new File(fileName).toPath();
                Path newFile = Paths.get(outputFile.getName());
                try {
                    Files.copy(oldFile, newFile, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    System.out.printf("Не удалось скопировать файл %s или записать новый файл %s\n", oldFile, newFile);
                }
                System.out.printf("Файл с результатами сохранен как %s\n", outputFile.getName());
            } else {
                // sorting and next
//                String newFile = "newFile";
                long maxLines = numberOfLines(new File(fileName));
//                System.out.println("maxLines: " + maxLines);
                long newLines = maxLines != 0 ? maxLines / 2 : 1;
                if (maxLines % newLines != 0) {
                    newLines++;
                }
//                System.out.println("newLines: " + newLines);
                BufferedReader reader = null;
                BufferedWriter writer = null;
                try {
                    reader = new BufferedReader(
                            new InputStreamReader(
                                    Files.newInputStream(
                                            Paths.get(fileName)),
                                    FileRead.ENCODING));

                    String newFileName = fileName.split("\\.")[0] + 0;
                    writer = new BufferedWriter(
                            new OutputStreamWriter(
                                    Files.newOutputStream(
                                            Paths.get(newFileName)),
                                    FileRead.ENCODING));
                    long count = 0;
                    int i = 1;
                    for (String line; (line = reader.readLine()) != null;) {
                        if (count++ == newLines) {
                            writer.close();
                            fileSort(new File(newFileName), sortDateType, comparator);

                            newFileName = fileName.split("\\.")[0] + i;
                            writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                            Files.newOutputStream(
                                                    Paths.get(newFileName)),
                                            FileRead.ENCODING));
                            count = 0;
                            i++;
                        }
                        writer.write(line);
                        writer.newLine();
                    }
                    fileSort(new File(newFileName), sortDateType, comparator);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } finally {
                    try {
                        if (writer != null) {
                            writer.close();
                        }
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }

                if (!new File(fileName).delete()) {
                    System.out.printf("Не удалось удалить файл %s\n", fileName);
                }

                fewFiles(sortDateType, comparator, outputFile);
//                fileSecondSort(new File(newFile), sortDateType, comparator, outputFile);
            }
        } else {
            File fileLeft = new File(filesNames.get(0));
            File fileRight = new File(filesNames.get(1));
            File twoInOne = mergeTwoInOne(fileLeft, fileRight, sortDateType, comparator);

            nextFileSort(twoInOne, sortDateType, comparator, outputFile);

        }

        Utility.deleteDirectory(TMP);
    }

    File mergeTwoInOne(File fileLeft, File fileRight, SortDataType sortDateType, Comparator comparator) {
        File twoInOne = new File("tmp/" + fileLeft.getName().split("\\.")[0] + ".m");
//        try (BufferedReader left = new BufferedReader(new FileReader(fileLeft));
//             BufferedReader right = new BufferedReader(new FileReader(fileRight))) {
        BufferedReader left = null, right = null;
        BufferedWriter bw = null;
        try {
            left = new BufferedReader(new InputStreamReader(Files.newInputStream(fileLeft.toPath()), FileRead.ENCODING));
            right = new BufferedReader(new InputStreamReader(Files.newInputStream(fileRight.toPath()), FileRead.ENCODING));
//            try (FileWriter fw = new FileWriter(twoInOne)) {
            try {
                bw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(twoInOne.toPath()), FileRead.ENCODING));

                Comparable x = sortDateType == SortDataType.INTEGER
                        ? Integer.parseInt(left.readLine()) : left.readLine();
                Comparable y = sortDateType == SortDataType.INTEGER
                        ? Integer.parseInt(right.readLine()) : right.readLine();

                while (true) {
                    if (x != null && y != null) {
                        if (comparator.compare(x, y) <= 0) {
                            x = readXY(bw, x, left, sortDateType);
                        } else {
                            y = readXY(bw, y, right, sortDateType);
                        }
                    } else {
                        if (x == null && y == null) {
                            break;
                        } else if (x == null) {
                            do {
                                y = readXY(bw, y, right, sortDateType);
                            } while (y != null);
                        } else {
                            do {
                                x = readXY(bw, x, left, sortDateType);
                            } while (x != null);
                        }
                    }
//                    fw.flush();
                    bw.flush();
                }
            } finally {
                if (bw != null) {
                    bw.close();
                }
            }
        } catch (IOException e) {
            System.out.printf("Ошибка чтения файла %s или %s\n", fileLeft.getName(), fileRight.getName());
        } finally {
            try {
                if (left != null) {
                    left.close();
                }
                if (right != null) {
                    right.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        if (!fileLeft.delete()) {
            System.out.printf("Не удалось удалить файл %s\n", fileLeft.getName());
        }
        if (!fileRight.delete()) {
            System.out.printf("Не удалось удалить файл %s\n", fileRight.getName());
        }
        return twoInOne;
    }

    Comparable readXY(BufferedWriter bw, Comparable value, BufferedReader br, SortDataType sortDateType) throws IOException {
//        bw.append(value.toString()).append("\n");
        bw.write(value.toString());
        bw.newLine();

        String tmp = br.readLine();
        return tmp != null ? (sortDateType == SortDataType.INTEGER ? Integer.parseInt(tmp) : tmp) : null;
    }
}
