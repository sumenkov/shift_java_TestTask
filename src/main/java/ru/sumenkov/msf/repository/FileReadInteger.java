package ru.sumenkov.msf.repository;

import ru.sumenkov.msf.service.Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReadInteger implements FileRead<Integer> {
    @Override
    public List<Integer> read(File file) {
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

        return list;
    }
}
