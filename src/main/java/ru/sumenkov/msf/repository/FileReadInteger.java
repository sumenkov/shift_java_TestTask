package ru.sumenkov.msf.repository;

import ru.sumenkov.msf.service.Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReadInteger implements FileRead {
    @Override
    public Integer[] read(File file) {
        List<Integer> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (Utility.isNumeric(line)) {
                    list.add(Integer.valueOf(line));
                } else {
                    System.out.printf("Неверный формат данных в строке. Файл %s, данные в строке %s\n", file.getName(), line);
                }
            }
        } catch (IOException e) {
            System.out.printf("Ошибка чтения файла %s\n", file.getName());
        }

        Integer[] array = new Integer[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }
        return array;
    }
}
