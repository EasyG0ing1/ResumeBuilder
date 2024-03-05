package com.simtechdata;

import javafx.application.Application;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.simtechdata.Resources.*;

public class Main extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Path qPath = Resources.get(SOURCE_QUESTIONS);
        if(!qPath.toFile().exists()) {
            SourceQuestions sq = new SourceQuestions(getQuestions(), getHints());
            sq.save();
        }
        new GUI();
    }

    private static String getQuestions() {
        return """
                1;;List your roles (job titles) that you held for which these questions will be relevant?
                2;;What were the main goals or objectives in each of your roles?
                3;;How did you go beyond your job description to contribute to the company?
                4;;Can you quantify your impact in terms of efficiency, revenue, or cost savings?
                5;;What projects did you lead or contribute significantly to?
                6;;Did you implement any new technologies or processes that improved outcomes?
                7;;How did you ensure the security or reliability of your network/systems?
                8;;What were some of the challenges you faced, and how did you overcome them?
                9;;How did you support business continuity or growth through your IT strategies?
                10;;Did you receive any recognition or awards during your tenure?
                11;;How did you contribute to team development or training?
                12;;Were there any specific instances where your actions directly resulted in improved client satisfaction or retention?
                13;;What learning or professional development activities did you undertake to improve your skills or stay updated in your field?
                """;
    }

    private static String getHints() {
        return """
                1;;Put each role on a new line
                2;;This can help you think about the broader context of your work. Format the answer like role: answer (two line feeds)
                3;;Consider moments when you took initiative or went the extra mile. Format the answer like role: answer (two line feeds)
                4;;Numbers speak volumes, such as “Increased network efficiency by 20% through optimization of X.”. Format the answer like role: answer (two line feeds)
                5;;This can showcase leadership and teamwork skills. Format the answer like role: answer (two line feeds)
                6;;Consider any specific projects or technology that you utilized to meet a need or accomplish a goal. Format the answer like role: answer (two line feeds)
                7;;Given your role in IT, specifics on security enhancements or uptime improvements can be significant. Format the answer like role: answer (two line feeds)
                8;;This can demonstrate problem-solving skills. Format the answer like role: answer (two line feeds)
                9;;Think about your role in the bigger picture of the organization's success. Format the answer like role: answer (two line feeds)
                10;;Acknowledgments from peers or superiors can be a testament to your performance.
                11;;If you helped colleagues grow or introduced best practices, it’s worth mentioning.
                12;;Provide any feedback or metrics on client satisfaction which can be compelling, clients can be those you consulted or supported. Format the answer like role: answer (two line feeds)
                13;;This shows commitment to your growth and adaptability.
                """;
    }
}
