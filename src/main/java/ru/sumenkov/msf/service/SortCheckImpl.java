package ru.sumenkov.msf.service;

import ru.sumenkov.msf.SortDataType;

import java.io.*;
import java.util.Comparator;

public class SortCheckImpl implements SortCheck {

    @Override
    public boolean isSorted(File file, SortDataType sortDateType, Comparator comparator) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            try {
                Comparable x = sortDateType == SortDataType.INTEGER ? Integer.parseInt(br.readLine()) : br.readLine();
                Comparable y = sortDateType == SortDataType.INTEGER ? Integer.parseInt(br.readLine()) : br.readLine();

                while (true) {
                    if (x != null && y != null) {
                    if (comparator.compare(x, y) > 0) {
                            return false;
                        }
                    } else {
                        return false;
                    }

                    x = y;
                    String tmp = br.readLine();

                    if (tmp != null && sortDateType == SortDataType.INTEGER) {
                        y = Integer.parseInt(tmp);
                    } else if (tmp != null) {
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
