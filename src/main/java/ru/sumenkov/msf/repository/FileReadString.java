package ru.sumenkov.msf.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReadString implements FileRead<String> {
    @Override
    public List<String> read(File file) {
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
            System.out.printf("Ошибка чтения файла %s\n", file.getName());
        }
        return list;
    }
}
