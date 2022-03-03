package com.main;

import com.main.services.FileService;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public class Main {
    private static final File INPUT_FOLDER_PATH = new File("./resources/input-files");
    private static final String OUTPUT_FOLDER_PATH = "./resources/output-files/";
    private static final int FIRST_FILE_INDEX = 0;
    private static final int LAST_FILE_INDEX = 0;

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        List<File> inputFiles = new ArrayList<>(Arrays.asList(Objects.requireNonNull(INPUT_FOLDER_PATH.listFiles())));
        inputFiles.sort(Comparator.comparing(File::getName));

        inputFiles = inputFiles.subList(FIRST_FILE_INDEX, LAST_FILE_INDEX + 1);

        inputFiles.forEach(file -> {
            LOGGER.info("Processing " + file.getName());
            List<String> inputData = FileService.readFile(file.getPath());
            System.out.println(inputData);
            FileService.writeFile(OUTPUT_FOLDER_PATH + file.getName().replace(".txt", "_result.txt"), inputData);
            LOGGER.info(file.getName() + " done");
        });
    }

}
