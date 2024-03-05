module ResumeBuilder {
    requires javafx.graphics;
    requires javafx.controls;
    requires com.google.gson;

    exports com.simtechdata;

    opens com.simtechdata to com.google.gson;
}
