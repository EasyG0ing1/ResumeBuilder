package com.simtechdata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SourceQuestions {

    public SourceQuestions(String questions, String hints) {
        this.questions = questions;
        this.hints = hints;
    }

    private final String questions;
    private final String hints;

    public void save() {
        Path path = Resources.get(Resources.SOURCE_QUESTIONS);
        if(!path.toFile().exists()){
            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(this);
                Files.writeString(path, json);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getQuestions() {
        return questions;
    }

    public String getHints() {
        return hints;
    }
}
