package org.example.bugboard26frontend.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.bugboard26frontend.Entita.Issue;
import org.example.bugboard26frontend.Entita.Utente;
import org.example.bugboard26frontend.Main;

import java.io.IOException;

public abstract class BaseController {

    protected Utente utenteLoggato;

    public void setUtenteLoggato(Utente utente) {
        this.utenteLoggato = utente;
    }

    // --- METODO GENERICO PER CAMBIARE SCENA ---
    protected void cambiaScena(Stage stage, String fxmlFile, String titolo) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GUI/" + fxmlFile));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            // Passaggio dati (Utente)
            Object controller = fxmlLoader.getController();
            if (controller instanceof BaseController && this.utenteLoggato != null) {
                ((BaseController) controller).setUtenteLoggato(this.utenteLoggato);
            }

            // Impostazione scena
            stage.setScene(scene);
            stage.setTitle("BugBoard 26 - " + titolo);

            // Proprietà finestra
            stage.setResizable(true);
            stage.setMinWidth(1024);
            stage.setMinHeight(768);
            stage.setWidth(1280);
            stage.setHeight(720);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore caricamento vista: " + fxmlFile);
        }
    }

    protected void cambiaScena(ActionEvent event, String fxmlFile, String titolo) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        cambiaScena(stage, fxmlFile, titolo);
    }

    // --- NAVIGAZIONE SIDEBAR ----

    public void apriCreaIssue(ActionEvent event) {
        cambiaScena(event, "createissue-view.fxml", "Crea Nuova Issue");
    }

    public void apriLeMieIssue(ActionEvent event) {
        cambiaScena(event, "personalissue-view.fxml", "Le Mie Issue");
    }

    public void apriCreaNuovoUtente(ActionEvent event) {
        cambiaScena(event, "createnewuser-view.fxml", "Crea Nuovo Utente");
    }

    public void apriDashboard(ActionEvent event) {
        cambiaScena(event, "dashboard-view.fxml", "Dashboard");
    }

    public void apriDashboard(Stage stage) {
        cambiaScena(stage, "dashboard-view.fxml", "Dashboard");
    }

    // --- LOGOUT ---
    public void effettuaLogout(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        effettuaLogout(stage);
    }

    public void effettuaLogout(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GUI/login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setMinWidth(0);
            stage.setMinHeight(0);
            stage.setScene(scene);
            stage.setTitle("BugBoard 26 - Login");
            stage.setWidth(400);
            stage.setHeight(500);
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
            System.out.println("Logout effettuato.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void apriDettaglioIssue(Issue issueSelezionata, Stage currentStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GUI/issue-view.fxml"));
            Parent root = fxmlLoader.load();

            // Passa i dati
            IssueController controller = fxmlLoader.getController();
            controller.setUtenteLoggato(this.utenteLoggato);
            controller.setDatiIssue(issueSelezionata);

            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.setTitle("Dettaglio Issue #" + issueSelezionata.getId());

            // Reset proprietà standard
            currentStage.setResizable(true);
            currentStage.setWidth(1280);
            currentStage.setHeight(720);
            currentStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}