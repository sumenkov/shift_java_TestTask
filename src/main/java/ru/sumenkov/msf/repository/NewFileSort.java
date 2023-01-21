package ru.sumenkov.msf.repository;

import ru.sumenkov.msf.SortDataType;
import ru.sumenkov.msf.service.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class NewFileSort implements FileSort{
    @Override
    public void runSort(List<File> inFiles, SortDataType sortDateType, Comparator comparator, File outputFile) throws IOException {

        SortCheck sortCheck = new SortCheckImpl();
        for (File file : inFiles) {
            if (sortCheck.isSorted(file, sortDateType, comparator)) {
                try {
                    Files.copy(Paths.get(file.getPath()), Paths.get("tmp/" + file.getName().split("\\.")[0] + ".s"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (numberOfLines(file) > Runtime.getRuntime().freeMemory() / 100) {
                splitFile(file, sortDateType);

                MergeSort mergeSort = new MergeSortImpl();
                for (File tmpFile : Objects.requireNonNull(new File("tmp/").listFiles())) {
                    if (tmpFile.getName().contains(".ns")) {
                        mergeSort.mergeSort(tmpFile, comparator);
                    }
                }
            }
        }
        mergeFile(sortDateType, comparator, outputFile);
    }

    void splitFile(File file, SortDataType sortDateType) {
        long newLinesFile = Runtime.getRuntime().freeMemory() / 100;
        BufferedReader reader;
        BufferedWriter writer;

        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            Files.newInputStream(
                                    Paths.get(file.getName())),
                            FileRead.ENCODING));
            String newFileName = "tmp/" + file.getName().split("\\.")[0] + 0 + ".ns";
            writer = new BufferedWriter(
                    new OutputStreamWriter(
                            Files.newOutputStream(
                                    Paths.get(newFileName)),
                            FileRead.ENCODING));
            long count = 0;
            int i = 1;
            for (String line; (line = reader.readLine()) != null;) {
                if (checkLine(line, sortDateType)) {
                    if (count++ == newLinesFile) {
                        writer.close();
                        newFileName = "tmp/" + file.getName().split("\\.")[0] + i + ".ns";
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
            }
            writer.close();
            reader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean checkLine(String line, SortDataType sortDateType) {
        if (sortDateType == SortDataType.INTEGER) {
            return Utility.isNumeric(line);
        } else {
            return !line.contains(" ") && !line.equals("");
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

    void mergeFile(SortDataType sortDateType, Comparator comparator, File outputFile) {
        List<String> filesName = new ArrayList<>();
        for (File tmpFile : Objects.requireNonNull(new File("tmp/").listFiles())) {
            filesName.add("tmp/" + tmpFile.getName());
        }
        if (filesName.size() == 0) {
            System.out.println("Нет данных для сортировки.");
            Utility.deleteDirectory(new File("tmp/"));
        } else {
            while (filesName.size() > 1) {
                int size = filesName.size() % 2 == 0 ? filesName.size() : filesName.size() - 1;
                for (int i = 0; i < size; i++) {
                    File fileLeft = new File(filesName.get(i));
                    File fileRight = new File(filesName.get(i + 1));
                    mergeAllInOne(fileLeft, fileRight, sortDateType, comparator);
                    i++;
                }
                filesName = new ArrayList<>();
                for (File tmpFile : Objects.requireNonNull(new File("tmp/").listFiles())) {
                    filesName.add("tmp/" + tmpFile.getName());
                }
            }

            if (!new File(filesName.get(0)).renameTo(outputFile)) {
                System.out.printf("Не удалось переименовать файл %s\n", filesName.get(0));
            }
            System.out.printf("Файл с результатами сохранен как %s\n", outputFile.getName());
        }
    }

    private void mergeAllInOne(File fileLeft, File fileRight, SortDataType sortDateType, Comparator comparator) {
        File twoInOne = new File("tmp/" + fileLeft.getName() + ".m");
        BufferedReader left = null, right = null;
        BufferedWriter bw = null;
        try {
            left = new BufferedReader(new InputStreamReader(Files.newInputStream(fileLeft.toPath()), FileRead.ENCODING));
            right = new BufferedReader(new InputStreamReader(Files.newInputStream(fileRight.toPath()), FileRead.ENCODING));
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
    }

    Comparable readXY(BufferedWriter bw, Comparable value, BufferedReader br, SortDataType sortDateType) throws IOException {
        bw.write(value.toString());
        bw.newLine();
        String tmp = br.readLine();
        return tmp != null ? (sortDateType == SortDataType.INTEGER ? Integer.parseInt(tmp) : tmp) : null;
    }
}
