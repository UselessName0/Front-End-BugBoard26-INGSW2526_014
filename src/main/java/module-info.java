module org.example.bugboard26frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.management;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens org.example.bugboard26frontend to javafx.fxml;
    exports org.example.bugboard26frontend;
    exports org.example.bugboard26frontend.entita;
    opens org.example.bugboard26frontend.entita to javafx.fxml;
    exports org.example.bugboard26frontend.entita.entita;
    opens org.example.bugboard26frontend.entita.entita to javafx.fxml;
}