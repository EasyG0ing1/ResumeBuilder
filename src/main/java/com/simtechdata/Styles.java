package com.simtechdata;

import javafx.scene.text.Font;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Styles {
    public static Font getFira(double size) {
        String osName = System.getProperty("os.name").toLowerCase();
        String path;
        if(osName.contains("mac")) {
            path = Styles.class.getResource("/Fonts/FiraCode_Retina.ttf").toExternalForm();
        }
        else {
            path = Styles.class.getResource("/Fonts/FiraCode_Regular.ttf").toExternalForm();
        }
        return Font.loadFont(path, size);
    }

    public static Font getArial(double size) {
        String path = Styles.class.getResource("/Fonts/Arial.ttf").toExternalForm();
        return Font.loadFont(path, size);
    }

    public static Font getArialBold(double size) {
        String path = Styles.class.getResource("/Fonts/Arial_Rounded_Bold.ttf").toExternalForm();
        return Font.loadFont(path, size);
    }
}
