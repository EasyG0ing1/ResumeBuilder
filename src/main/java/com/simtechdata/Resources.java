package com.simtechdata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum Resources {

    FOLDER,
    ANSWERS,
    SOURCE_QUESTIONS;

    private static final String root = System.getProperty("user.home");
    private static final String folder = ".ResumeBuilder";
    private static final String sourceQuestionsFilename = "SourceQuestions.json";
    private static final String answersFilename = "Answers.json";
    private static final Path folderPath = Paths.get(root, folder);
    private static final Path sourceQuestionPath = Paths.get(root, folder, sourceQuestionsFilename);
    private static final Path answersPath = Paths.get(root, folder, answersFilename);

    private static void makeFolder() {
        try {
            if(!folderPath.toFile().exists())
                Files.createDirectories(folderPath);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path get(Resources resource) {
        makeFolder();
        return switch(resource) {
            case FOLDER -> folderPath;
            case SOURCE_QUESTIONS -> sourceQuestionPath;
            case ANSWERS -> answersPath;
        };
    }

}
