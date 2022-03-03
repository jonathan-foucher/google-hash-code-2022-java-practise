package com.main.services;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class FileService {
    public static List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String currentLine = null;
            while ((currentLine = reader.readLine()) != null) {
                lines.add(currentLine);
            }
            reader.close();
            return lines;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public static void writeFile(String filePath, List<String> content) {
        try {
            Writer writer = null;
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8));

            Writer finalWriter = writer;
            for (String line : content) {
                finalWriter.write(line + "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
