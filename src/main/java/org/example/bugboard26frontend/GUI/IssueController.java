package org.example.bugboard26frontend.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.bugboard26frontend.Entita.Issue;
import org.example.bugboard26frontend.Main; // Assicurati che questo import sia corretto per la tua classe Main/HelloApplication
import javafx.event.ActionEvent;
import java.io.IOException;

public class IssueController {

    @FXML private Label labelTitolo;
    @FXML private Label labelDescrizione;
    @FXML private Label badgePriorita;
    @FXML private Label badgeTipo;

    private Issue issueCorrente;

    public void setDatiIssue(Issue issue) {
        this.issueCorrente = issue;
        if (issue != null) {
            // 1. Popola Titolo
            if (labelTitolo != null) {
                labelTitolo.setText(issue.getTitolo());
            }

            // 2. Popola Descrizione
            if (labelDescrizione != null) {
                if (issue.getDescrizione() != null && !issue.getDescrizione().isEmpty()) {
                    labelDescrizione.setText(issue.getDescrizione());
                } else {
                    labelDescrizione.setText("Nessuna descrizione fornita per questa issue.");
                }
            }

            // 3. Popola Badge Priorità
            if (badgePriorita != null) {
                badgePriorita.setText(issue.getPriorita().toUpperCase());
                aggiornaStileBadge(badgePriorita, issue.getPriorita());
            }

            // 4. Popola Badge Tipo
            if (badgeTipo != null) {
                badgeTipo.setText(issue.getTipo().toUpperCase());
                aggiornaStileBadge(badgeTipo, issue.getTipo());
            }
        }
    }

    private void aggiornaStileBadge(Label badge, String testo) {
        // Rimuovi tutti gli stili potenziali precedenti per evitare conflitti
        badge.getStyleClass().removeAll("badge-red", "badge-purple", "badge-green", "badge-orange");
        String t = testo.toUpperCase();
        if (!badge.getStyleClass().contains("badge")) {
            badge.getStyleClass().add("badge");
        }
        if (t.equals("HIGH") || t.equals("CRITICAL")) {
            badge.getStyleClass().add("badge-red");
        } else if (t.equals("BUG") || t.equals("FEATURE")) {
            badge.getStyleClass().add("badge-purple");
        } else if (t.equals("LOW")) {
            badge.getStyleClass().add("badge-green");
        } else if (t.equals("MEDIUM")) {
            badge.getStyleClass().add("badge-orange");
        } else {
            badge.getStyleClass().add("badge-purple");
        }
    }

    @FXML
    private void tornaIndietro(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GUI/dashboard-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Recupera lo stage direttamente dal bottone che è stato cliccato
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("BugBoard 26 - Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore tornando indietro: " + e.getMessage());
        }
    }


}