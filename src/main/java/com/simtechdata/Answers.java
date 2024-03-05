package com.simtechdata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Answers {

    public Answers(LinkedList<Question> questionList) {
        fillMap(questionList);
    }

    private final Map<Integer, Question> questionMap = new HashMap<>();
    private static boolean dataSaved = true;
    private static Timer timer;
    private static final Path file = Resources.get(Resources.ANSWERS);

    private void fillMap(LinkedList<Question> qList) {
        for (Question question : qList) {
            questionMap.put(question.getId(), question);
        }
    }

    public void updateAnswer(int id, String answer) {
        String lastAnswer = questionMap.get(id).getAnswer();
        if (!lastAnswer.equals(answer)) {
            questionMap.get(id).setAnswer(answer);
            dataSaved = false;
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            timer = new Timer();
            timer.schedule(saveTask(), 10000);
        }
    }

    private TimerTask saveTask() {
        return new TimerTask() {
            @Override
            public void run() {
                save();
                timer = null;
            }
        };
    }

    private String getJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    private void save() {
        if (!dataSaved) {
            String json = getJson();
            try {
                Files.writeString(file, json);
                dataSaved = true;
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean exists() {
        return file.toFile().exists();
    }

    public static Path getFilePath() {
        return file;
    }

    public LinkedList<Question> getQuestions() {
        LinkedList<Question> list = new LinkedList<>();
        int count = questionMap.size();
        for (int x = 1; x <= count; x++) {
            Question question = questionMap.get(x);
            list.addLast(question);
        }
        return list;
    }
    public boolean stopped() {
        return dataSaved;
    }
    public void stop() {
        new Thread(() -> {
            save();
            if (timer != null) {
                timer.cancel();
            }
        }).start();
    }
}
