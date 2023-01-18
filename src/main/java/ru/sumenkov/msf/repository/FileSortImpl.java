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

    void fileSort(File file, SortDataType sortDateType, SortDirection sortDirection, long freeMemory) {

        SortCheck sortCheck = new SortCheckImpl();

        if (sortCheck.isSorted(file, sortDateType, sortDirection)) {
            if (!file.renameTo(new File("tmp/" + file.getName() + ".sort"))) {
                System.out.printf("Не удалось переименовать файл %s\n", file);
            }
        } else if (file.length() >= freeMemory) {
            splitBigFile(file, sortDateType, sortDirection, freeMemory);
        } else {
            smallFile(file, sortDateType, sortDirection);
        }
    }

    <T extends Comparable<T>> void smallFile(File file, SortDataType sortDateType, SortDirection sortingDirection) {

        MergeSort mergeSort = new MergeSortImpl();
        FileRead fileRead = sortDateType == SortDataType.INTEGER ? new FileReadInteger() : new FileReadString();

        try (FileWriter fw = fileWriter(file)) {
            List<T> array = fileRead.read(file);

            mergeSort.mergeSort(array, sortingDirection);

            for (Object obj : array) {
                fw.append(String.valueOf(obj)).append("\n");
            }

            fw.flush();
        } catch (IOException e) {
            System.out.printf("Ошибка во время сортировки файла %s\n", file.getName());
        }
    }

    FileWriter fileWriter(File file) throws IOException {
        FileWriter fw;
        if (file.getName().split("/")[0].equals("tmp")) {
            fw = new FileWriter(file.getName() + ".stmp");
        } else {
            fw = new FileWriter("tmp/" + file.getName() + ".stmp");
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

                    if (!newFile.delete()) {
                        System.out.printf("Не удалось удалить файл %s\n", newFile.getName());
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
                fileSecondSort(new File(filesNames.get(0)), sortDateType, sortingDirection, outputFile);
            }
        } else {
            File fileLeft = new File(filesNames.get(0));
            File fileRight = new File(filesNames.get(1));
            File twoInOne = mergeTwoInOne(fileLeft, fileRight, sortDateType, sortingDirection);

            fileSecondSort(twoInOne, sortDateType, sortingDirection, outputFile);

        }

        Utility.deleteDirectory(TMP);
    }

    File mergeTwoInOne(File fileLeft, File fileRight, SortDataType sortDateType, SortDirection sortingDirection) {
        File twoInOne = new File("tmp/" + fileLeft.getName() + ".tio");
        try (BufferedReader left = new BufferedReader(new FileReader(fileLeft));
             BufferedReader right = new BufferedReader(new FileReader(fileRight))) {
            try (FileWriter fw = new FileWriter(twoInOne)) {

                Comparable x = null;
                Comparable y = null;
                if (sortDateType == SortDataType.INTEGER) {
                    x = Integer.parseInt(left.readLine());
                    y = Integer.parseInt(right.readLine());
                } else if (sortDateType == SortDataType.STRING) {
                    x = left.readLine();
                    y = right.readLine();
                }

                while (true) {
                    if (sortingDirection == SortDirection.ASC) {
                        if (x != null && y != null) {
                            if (x.compareTo(y) <= 0) {
                                fw.append(x.toString()).append("\n");
                                String tmp = left.readLine();
                                if (tmp != null) {
                                    x = sortDateType == SortDataType.INTEGER ? Integer.parseInt(tmp) : tmp;
                                } else {
                                    x = null;
                                }
                            } else {
                                fw.append(y.toString()).append("\n");
                                String tmp = right.readLine();
                                if (tmp != null) {
                                    y = sortDateType == SortDataType.INTEGER ? Integer.parseInt(tmp) : tmp;
                                } else {
                                    y = null;
                                }
                            }
                        } else {
                            if (x == null && y == null) {
                                break;
                            } else if (x == null) {
                                while (true) {
                                    fw.append(y.toString()).append("\n");
                                    String tmp = right.readLine();
                                    if (tmp != null) {
                                        y = sortDateType == SortDataType.INTEGER ? Integer.parseInt(tmp) : tmp;
                                    } else {
                                        y = null;
                                        break;
                                    }
                                }
                            } else {
                                while (true) {
                                    fw.append(x.toString()).append("\n");
                                    String tmp = left.readLine();
                                    if (tmp != null) {
                                        x = sortDateType == SortDataType.INTEGER ? Integer.parseInt(tmp) : tmp;
                                    } else {
                                        x = null;
                                        break;
                                    }
                                }
                            }
                        }
                    } else if (sortingDirection == SortDirection.DESC) {
                        if (x != null && y != null) {
                            if (x.compareTo(y) >= 0) {
                                fw.append(x.toString()).append("\n");
                                String tmp = left.readLine();
                                if (tmp != null) {
                                    x = sortDateType == SortDataType.INTEGER ? Integer.parseInt(tmp) : tmp;
                                } else {
                                    x = null;
                                }
                            } else {
                                fw.append(y.toString()).append("\n");
                                String tmp = right.readLine();
                                if (tmp != null) {
                                    y = sortDateType == SortDataType.INTEGER ? Integer.parseInt(tmp) : tmp;
                                } else {
                                    y = null;
                                }
                            }
                        } else {
                            if (x == null && y == null) {
                                break;
                            } else if (x == null) {
                                while (true) {
                                    fw.append(y.toString()).append("\n");
                                    String tmp = right.readLine();
                                    if (tmp != null) {
                                        y = sortDateType == SortDataType.INTEGER ? Integer.parseInt(tmp) : tmp;
                                    } else {
                                        y = null;
                                        break;
                                    }
                                }
                            } else {
                                while (true) {
                                    fw.append(x.toString()).append("\n");
                                    String tmp = left.readLine();
                                    if (tmp != null) {
                                        x = sortDateType == SortDataType.INTEGER ? Integer.parseInt(tmp) : tmp;
                                    } else {
                                        x = null;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                fw.flush();
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
        return twoInOne;
    }
}
