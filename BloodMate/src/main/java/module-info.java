module com.blooddonation {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.base;
    requires javafx.media;
    requires javafx.web;

    exports com.blooddonation;
    opens com.blooddonation to javafx.fxml;
} 