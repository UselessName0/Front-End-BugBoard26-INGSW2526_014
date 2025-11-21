module org.example.bugboard26frontend {
    // Librerie Grafiche Standard
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    // Librerie Grafiche Aggiuntive (Template)
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.management;

    // Librerie per la connessione Backend (HTTP e JSON)
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    // --- PERMESSI (EXPORTS e OPENS) ---

    // 1. Pacchetto Principale (Main)
    exports org.example.bugboard26frontend;
    opens org.example.bugboard26frontend to javafx.fxml;

    // 2. Pacchetto GUI (Dove ci sono i Controller come LoginController)
    // FONDAMENTALE: Senza questo, FXML non riesce a caricare il controller!
    exports org.example.bugboard26frontend.GUI;
    opens org.example.bugboard26frontend.GUI to javafx.fxml;

    // 3. Pacchetto Entita (Dove ci sono Utente, ApiService...)
    // FONDAMENTALE: 'opens' serve anche a Jackson per trasformare il JSON in Oggetti
    exports org.example.bugboard26frontend.Entita;
    opens org.example.bugboard26frontend.Entita to javafx.fxml, com.fasterxml.jackson.databind;
}