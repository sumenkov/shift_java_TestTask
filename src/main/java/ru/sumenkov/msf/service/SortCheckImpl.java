package ru.sumenkov.msf.service;

import ru.sumenkov.msf.repository.FileSort;

import java.io.*;
import java.nio.file.Files;
import java.util.Comparator;

public class SortCheckImpl implements SortCheck {

    @Override
    public boolean isSorted(File file, Comparator comparator) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), FileSort.ENCODING));

            Comparable x = br.readLine(), y = br.readLine();;

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

                if (tmp != null) {
                    y = tmp;
                } else {
                    break;
                }
            }

        } catch (IOException ex) {
            System.out.printf("Ошибка чтения файла %s\n", file.getName());
            return false;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return true;
    }
}
