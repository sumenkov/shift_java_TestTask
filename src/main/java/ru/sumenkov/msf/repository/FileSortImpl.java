package ru.sumenkov.msf.repository;

import ru.sumenkov.msf.SortDataType;
import ru.sumenkov.msf.service.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileSortImpl implements FileSort{
    @Override
    public void runSort(List<File> inFiles, SortDataType sortDateType, Comparator comparator, File outputFile) {

        SortCheck sortCheck = new SortCheckImpl();
        for (File file : inFiles) {
            if (sortCheck.isSorted(file, comparator)) {
                copyFile(file, sortDateType, ".s");
            } else if (numberOfLines(file) > Runtime.getRuntime().freeMemory() / 100) {
                splitFile(file, sortDateType);
            } else {
                copyFile(file, sortDateType, ".ns");
            }
        }
        MergeSort mergeSort = new MergeSortImpl();
        for (File tmpFile : Objects.requireNonNull(TMP.listFiles())) {
            if (tmpFile.getName().contains(".ns")) {
                try {
                    mergeSort.mergeSort(tmpFile, comparator);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        mergeFile(sortDateType, comparator, outputFile);
    }

    void copyFile(File file, SortDataType sortDateType, String ending) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            Files.newInputStream(
                                    Paths.get(file.getPath())),
                            FileSort.ENCODING));
            String newFileName = "tmp/" + file.getName().split("\\.")[0] + ending;
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            Files.newOutputStream(
                                    Paths.get(newFileName)),
                            FileSort.ENCODING));
            for (String line; (line = reader.readLine()) != null; ) {
                if (checkLine(line, sortDateType)) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            writer.close();
            reader.close();
            if (file.getPath().contains("tmp/")) {
                if (!file.delete()) {
                    System.out.printf("???? ?????????????? ?????????????? ???????? %s\n", file.getName());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
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
                            FileSort.ENCODING));
            String newFileName = "tmp/" + file.getName().split("\\.")[0] + 0 + ".ns";
            writer = new BufferedWriter(
                    new OutputStreamWriter(
                            Files.newOutputStream(
                                    Paths.get(newFileName)),
                            FileSort.ENCODING));
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
                                        FileSort.ENCODING));
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
            System.out.printf("???????????? ???????????? ?????????? %s\n", file.getName());
        }
        return rows;
    }

    void mergeFile(SortDataType sortDateType, Comparator comparator, File outputFile) {
        List<File> filesName = new ArrayList<>(Arrays.asList(Objects.requireNonNull(TMP.listFiles())));
        if (filesName.size() == 0) {
            System.out.println("?????? ???????????? ?????? ????????????????????.");
            Utility.deleteDirectory(TMP);
        } else {
            while (filesName.size() > 1) {
                int size = filesName.size() % 2 == 0 ? filesName.size() : filesName.size() - 1;
                List<Thread> myThreads = new ArrayList<>();

                for (int i = 0; i < size; i++) {
                    File fileLeft = filesName.get(i);
                    File fileRight = filesName.get(i + 1);

                    Thread myThread = new Thread(() -> mergeAllInOne(fileLeft, fileRight, sortDateType, comparator));
                    myThread.start();
                    myThreads.add(myThread);
                    i++;
                }

                for (Thread thread: myThreads) {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
                filesName = new ArrayList<>(Arrays.asList(Objects.requireNonNull(TMP.listFiles())));
            }

            File file = filesName.get(0);
            SortCheck sortCheck = new SortCheckImpl();
            if (!sortCheck.isSorted(file, comparator)) {
                runSort(filesName, sortDateType, comparator, outputFile);
            } else {
                if (!file.renameTo(outputFile)) {
                    System.out.printf("???? ?????????????? ?????????????????????????? ???????? %s\n", filesName.get(0));
                }
                System.out.printf("???????? ?? ???????????????????????? ???????????????? ?????? %s\n", outputFile.getName());
                Utility.deleteDirectory(TMP);
            }
        }
    }

    void mergeAllInOne(File fileLeft, File fileRight, SortDataType sortDateType, Comparator comparator)  {
        File twoInOne = new File("tmp/" + fileLeft.getName() + ".m");
        BufferedReader left = null, right = null;
        BufferedWriter bw = null;
        try {
            left = new BufferedReader(new InputStreamReader(Files.newInputStream(fileLeft.toPath()), FileSort.ENCODING));
            right = new BufferedReader(new InputStreamReader(Files.newInputStream(fileRight.toPath()), FileSort.ENCODING));
            try {
                bw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(twoInOne.toPath()), FileSort.ENCODING));

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
            System.out.printf("???????????? ???????????? ?????????? %s ?????? %s\n", fileLeft.getName(), fileRight.getName());
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
            System.out.printf("???? ?????????????? ?????????????? ???????? %s\n", fileLeft.getName());
        }
        if (!fileRight.delete()) {
            System.out.printf("???? ?????????????? ?????????????? ???????? %s\n", fileRight.getName());
        }
    }

    Comparable readXY(BufferedWriter bw, Comparable value, BufferedReader br, SortDataType sortDateType) throws IOException {
        bw.write(value.toString());
        bw.newLine();
        String tmp = br.readLine();
        return tmp != null ? (sortDateType == SortDataType.INTEGER ? Integer.parseInt(tmp) : tmp) : null;
    }
}
