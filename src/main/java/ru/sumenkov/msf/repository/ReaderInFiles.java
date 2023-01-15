package ru.sumenkov.msf.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ReaderInFiles {

    public static int[] readI(String fileName) {

        File file = new File(fileName);
        List<Integer> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String line;
            while ((line = br.readLine()) != null) {
                if (isNumeric(line) && !line.contains(" ")) {
                    list.add(Integer.valueOf(line));
                } else {
                    System.out.printf("Неверный формат данных в строке. Файл %s, данные в строке %s\n", file.getName(), line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int[] ints = new int[list.size()];

        for (int i = 0; i < ints.length; i++) {
            ints[i] = list.get(i);
        }

        return ints;
    }

    public static String[] readS(String fileName) {

        File file = new File(fileName);
        List<String> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.contains(" ") && !line.equals("")) {
                    list.add(line);
                } else {
                    System.out.printf("Неверный формат данных в строке. Файл %s, данные в строке %s\n", file.getName(), line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] array = new String[list.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
