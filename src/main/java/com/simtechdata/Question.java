package com.simtechdata;

public class Question {

    public Question(String id, String question, String hint) {
        this.question = question;
        this.hint = hint;
        this.id = Integer.parseInt(id);
    }

    private final int id;
    private final String question;
    private final String hint;
    private String answer;

    public int getId() {
        return id;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer == null ? "" : answer;
    }

    public String getHint() {
        return hint;
    }
}
