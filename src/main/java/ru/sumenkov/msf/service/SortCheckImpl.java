package ru.sumenkov.msf.service;

import ru.sumenkov.msf.SortDataType;
import ru.sumenkov.msf.SortDirection;

import java.io.*;

public class SortCheckImpl implements SortCheck {

    @Override
    public boolean isSorted(File file, SortDataType sortDateType, SortDirection sortDirection) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            try {
                Comparable x = null;
                Comparable y = null;
                if (sortDateType == SortDataType.INTEGER) {
                    x = Integer.parseInt(br.readLine());
                    y = Integer.parseInt(br.readLine());
                } else if (sortDateType == SortDataType.STRING) {
                    x = br.readLine();
                    y = br.readLine();
                }

                while (true) {
                    if (sortDirection == SortDirection.ASC) {
                        if (x != null && y != null) {
                            if (x.compareTo(y) > 0) {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    } else if (sortDirection == SortDirection.DESC) {
                        if (x != null && y != null) {
                            if (x.compareTo(y) < 0) {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }

                    x = y;
                    String tmp = br.readLine();

                    if (tmp != null && sortDateType == SortDataType.INTEGER) {
                        y = Integer.parseInt(tmp);
                    } else if (tmp != null && sortDateType == SortDataType.STRING) {
                        y = tmp;
                    } else {
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                return false;
            }
        } catch (IOException e) {
            System.out.printf("Ошибка чтения файла %s\n", file.getName());
            return false;
        }
        return true;
    }
}
