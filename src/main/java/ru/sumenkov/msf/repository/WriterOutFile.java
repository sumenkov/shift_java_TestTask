package ru.sumenkov.msf.repository;

import ru.sumenkov.msf.model.OutputArray;

import java.io.FileWriter;
import java.io.IOException;

public class WriterOutFile {

    public static void write(OutputArray outputArray, String fileName) {

        try(FileWriter writer = new FileWriter(fileName, false))
        {
            for (int num: outputArray.getOutArrayI()) {
                writer.append(String.valueOf(num)).append("\n");
            }

            writer.flush();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
