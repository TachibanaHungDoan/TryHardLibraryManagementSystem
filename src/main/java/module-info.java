module com.example.libms {
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;
    requires mysql.connector.java;
    requires javafx.media;
    requires com.jfoenix;
    requires com.google.gson;
    requires java.sql;
    requires mockito.all;
    requires jfxtras.test_support;

    opens com.example.libms to javafx.fxml;
    exports com.example.libms;
}