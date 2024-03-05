package com.simtechdata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.simtechdata.Resources.*;

public class GUI {


    public GUI() {
        if(Answers.exists()) {
            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = Files.readString(Answers.getFilePath());
                answers = gson.fromJson(json, Answers.class);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            answers = new Answers(getNewQuestions());
        }
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        width = bounds.getWidth() * .75;
        height = bounds.getHeight() * .75;

        createControls();
        show();
        timer.scheduleAtFixedRate(clockTask(), 0, 1000);
    }

    private final double width;
    private final double height;
    private ScrollPane scrollPane;
    private VBox content;
    private VBox questionContent;
    private Button button;
    private Label clock;
    private long startTime = System.currentTimeMillis() - 10000;
    private final Timer timer = new Timer();
    private final Answers answers;

    private void createControls() {
        clock = new Label("Countdown Save Timer");
        clock.setFont(Styles.getArial(14));
        clock.setStyle(back);
        Tooltip tt = new Tooltip("Closing the app normally commits unsaved data");
        Tooltip.install(clock, tt);
        scrollPane = new ScrollPane();
        scrollPane.setStyle(back);
        LinkedList<Question> qList = answers.getQuestions();
        button = new Button("Copy For ChatGPT");
        button.setPrefWidth(125);
        button.setOnAction(e-> getChatGPT());
        questionContent = new VBox();
        content = new VBox();
        content.setPadding(new Insets(10));
        content.setSpacing(25);
        content.setAlignment(Pos.CENTER);
        content.setStyle(back);
        questionContent.setPadding(new Insets(5));
        questionContent.setSpacing(10);
        questionContent.setStyle(back);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStylesheets().add(getClass().getResource("/Stylesheets/ScrollPane.css").toExternalForm());
        for (Question question : qList) {
            questionContent.getChildren().add(getQuestion(question));
        }
        scrollPane.setContent(questionContent);
        content.getChildren().addAll(clock, scrollPane, button);
    }

    private final String back = "-fx-background-color: rgb(175,175,175);";
    private VBox getQuestion(Question question) {
        Label label = new Label(question.getQuestion());
        label.setFont(Styles.getArialBold(20));
        label.setStyle(back);
        String hint = question.getHint();
        String[] parts = hint.split("\\. ");
        if(parts.length > 1) {
            hint = parts[0] + "\n" + parts[1];
        }
        Tooltip tooltip = new Tooltip(hint);
        Tooltip.install(label, tooltip);
        TextArea ta = new TextArea(question.getAnswer());
        int id = question.getId();
        ta.textProperty().addListener((source, oldValue, newValue) -> {
            startTime = System.currentTimeMillis();
            answers.updateAnswer(id, newValue);
        });
        ta.setPrefWidth(width - 50);
        ta.setPrefHeight(250);
        ta.setFont(Styles.getFira(14));
        ta.setWrapText(true);
        ta.setStyle("-fx-control-inner-background: rgb(200,200,200)");
        VBox vbox = new VBox(label, ta);
        vbox.setPadding(new Insets(5,5,25,5));
        String h = "Metropolitan";
        vbox.setSpacing(15);
        vbox.setStyle(back);
        return vbox;
    }

    private LinkedList<Question> getNewQuestions() {
        LinkedList<Question> list = new LinkedList<>();
        Path sourceQuestionsPath = Resources.get(SOURCE_QUESTIONS);
        if (sourceQuestionsPath.toFile().exists()) {
            try {
                String json = Files.readString(sourceQuestionsPath);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                SourceQuestions sq = gson.fromJson(json, SourceQuestions.class);
                String qContents = sq.getQuestions();
                String hContents = sq.getHints();
                String[] questions = qContents.split("\\n");
                String[] hints = hContents.split("\\n");
                for (int x = 0; x < questions.length; x++) {
                    String[] qParts = questions[x].split(";;");
                    String[] hParts = hints[x].split(";;");
                    String id = qParts[0];
                    String question = qParts[1];
                    String hint = hParts[1];
                    Question questionObj = new Question(id, question, hint);
                    list.addLast(questionObj);
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }

    private void getChatGPT() {
        LinkedList<Question> list = answers.getQuestions();
        StringBuilder sb = new StringBuilder();
        sb.append(getGPTPreface());
        for(Question q : list){
            String question = q.getQuestion();
            String answer = q.getAnswer();
            sb.append("Question: ").append(question).append("\n").append("Answer:").append(((answer == null) ? " No Answer\n" : "\n"));
            if(answer != null) {
                String[] ans = splitString(answer);
                for (String ansLine : ans) {
                    sb.append("\t").append(ansLine).append("\n");
                }
            }
            sb.append("\n");
        }
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(sb.toString());
        clipboard.setContent(content);
    }

    private String[] splitString(String input) {
        if(input == null)
            return new String[1];
        List<String> result = new ArrayList<>();
        int start = 0;
        int len = 120; //Maximum length of each line in the split
        String[] parts = input.split("\\n");
        for(String part : parts) {
            if(part.trim().startsWith("-")) {
                result.add("\t" + part);
                continue;
            }
            while (start < part.length()) {
                if (start + len > part.length()) {
                    // If the remaining string is shorter than 80 chars, add it as is.
                    result.add(part.substring(start));
                    break;
                }

                int end = start + len;
                // Ensure we don't split in the middle of a word and check if we're not at the end also if the next character is not a space
                if (end < part.length() && part.charAt(end) != ' ') {
                    // Move the end back to the last space within the limit
                    while (end > start && part.charAt(end - 1) != ' ') {
                        end--;
                    }
                }

                // If we didn't find a space, force split
                if (end == start) {
                    end = start + len; // fallback to splitting at 80 characters, even if it's in the middle of a word
                }

                result.add(part.substring(start, end));

                // Update start position for next iteration, careful not to skip characters
                start = end;
                while (start < part.length() && part.charAt(start) == ' ') {
                    start++; // skip spaces to start the next line without leading spaces
                }
            }
        }
        return result.toArray(new String[0]);
    }

    private void show() {
        Scene scene = new Scene(content, width, height);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setOnCloseRequest(e->stop());
        stage.show();
    }

    private TimerTask clockTask() {
        return new TimerTask() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                long seconds = (now - startTime) / 1000;
                long time = 10 - seconds;
                String msg = (time <= 0) ? "Data Saved" : "Not Saved (" + time + ")";
                Platform.runLater(() -> {
                    if(time <= 0)
                        clock.setStyle("-fx-text-fill: rgb(0,115,0);");
                    else
                        clock.setStyle("-fx-text-fill: rgb(200,70,0);");
                    clock.setText(msg);
                });
            }
        };
    }

    private String getGPTPreface() {
        return """
                The following questions and answers are intended to be used for you to generate a list of action statements that I will place in my resume, that will generate the most positive hits when my resume is submitted to an `Applicant Tracking System` engine so that my resume can appear as favorable as possible to a potential employer I am applying to.

                Please analyze these questions and answers, then generate the action statements that will be the most favorable to any `Applicant Tracking System` engine that I submit my resume to:

                """;
    }

    private void stop() {
        answers.stop();
        timer.cancel();
        int counter = 0;
        while(!answers.stopped() && counter < 30) {
            sleep(100); //give answers time so save and stop timer
            counter++;
        }
        if(counter >= 30)
            System.exit(0);
    }

    private void sleep(long milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        }
        catch (InterruptedException ignored) {
        }
    }
}
