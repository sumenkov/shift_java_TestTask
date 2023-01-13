package ru.sumenkov.msf.repository;

import ru.sumenkov.msf.model.OutputArray;

import java.io.FileWriter;
import java.io.IOException;

public class WriterOutFile {

    public static void write(OutputArray outputArray, String fileName) {

        try(FileWriter writer = new FileWriter(fileName, true))
        {
            for (int str: outputArray.getOutArrayI()) {
                writer.append((char) str);
            }

            writer.flush();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
