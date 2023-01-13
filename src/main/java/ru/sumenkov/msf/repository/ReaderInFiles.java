package ru.sumenkov.msf.repository;

import ru.sumenkov.msf.model.InputArrays;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class ReaderInFiles {
    public static InputArrays read(String fileName) {

        File file = new File(fileName);

        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new InputArrays();
    }
}
