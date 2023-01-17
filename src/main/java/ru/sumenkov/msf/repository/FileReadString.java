package ru.sumenkov.msf.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReadString implements FileRead {
    @Override
    public String[] read(File file) {
        List<String> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
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
}
