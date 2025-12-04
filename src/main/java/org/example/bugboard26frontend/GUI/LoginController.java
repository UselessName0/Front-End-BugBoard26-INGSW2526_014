package org.example.bugboard26frontend.GUI;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.bugboard26frontend.apiservices.AuthService;
import org.example.bugboard26frontend.entita.Utente;
import org.example.bugboard26frontend.Main;

import java.io.IOException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    protected void onLoginClick() {
        String email = emailField.getText();
        String password = passwordField.getText();
        errorLabel.setVisible(false);
        if(email.isEmpty() || password.isEmpty()){
            mostraErrore("Inserisci email e password!");
            return;
        }
        Task<Boolean> loginTask = new Task<>() {
            @Override
            protected Boolean call() throws  Exception {
                return authService.login(email, password);
            }
        };

        loginTask.setOnSucceeded(event -> {
            errorLabel.setText("");
            System.out.println("Login effettuato con successo!");

            try {
                Utente utenteLoggato = authService.getUtenteLoggato();
                apriDashboard(utenteLoggato);
            } catch (IOException e) {
                e.printStackTrace();
                mostraErrore("Errore aprendo la dashboard: " + e.getMessage());
            }
        });

        loginTask.setOnFailed(event -> {
            Throwable exception = loginTask.getException();
            String messaggioErrore = "Errore durante il login.";
            if (exception != null) {
                messaggioErrore += " " + exception.getMessage();
            }
            mostraErrore(messaggioErrore);
        });

        new Thread(loginTask).start();
    }

    // Metodo che permette l'apertura della dashboard
    private void apriDashboard (Utente utenteLoggato) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GUI/dashboard-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage dashboardStage = new Stage();
        dashboardStage.setScene(scene);
        dashboardStage.setTitle("BugBoard 26 - Dashboard");
        dashboardStage.setResizable(true);
        dashboardStage.setWidth(1280);
        dashboardStage.setHeight(720);
        dashboardStage.setMinWidth(1024);
        dashboardStage.setMinHeight(720);
        dashboardStage.centerOnScreen();
        dashboardStage.show();

        Stage loginStage = (Stage) emailField.getScene().getWindow();
        loginStage.close();
    }

    // Metodo che mostra errore nel caso di login non andato a buon fine
    private void mostraErrore(String messaggio) {
        errorLabel.setText(messaggio);
        errorLabel.setStyle("-fx-text-fill: #ef4444;");
        errorLabel.setVisible(true);
    }
}