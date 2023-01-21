package ru.sumenkov.msf.repository;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileReadString implements FileRead<String> {
    @Override
    public File read(File file) {
//        List<String> list = new LinkedList<>();
        File checkFile = new File("tmp/" + file.getName() + ".с");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            try (FileWriter fw = new FileWriter(checkFile)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.contains(" ") && !line.equals("")) {
                        fw.append(line);
//                    list.add(line);
                    } else {
                        System.out.printf("Плохая строка. Файл %s, строка %s\n", file.getName(), line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.printf("Ошибка чтения файла %s\n", file.getName());
        }
        return checkFile;
    }
}
