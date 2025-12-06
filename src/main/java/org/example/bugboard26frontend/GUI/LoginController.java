package org.example.bugboard26frontend.GUI;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
                Utente utenteRicevuto = authService.getUtenteLoggato();

                // === MODIFICA QUI ===
                // Salviamo l'utente nella variabile STATICA del BaseController
                if (utenteRicevuto != null) {
                    BaseController.utenteLoggato = utenteRicevuto;
                    System.out.println("Login: Utente salvato in sessione -> " + utenteRicevuto.getNome());
                    apriDashboard();
                } else {
                    mostraErrore("Errore: Login riuscito ma dati utente vuoti.");
                }

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

    // Metodo semplificato: non serve più passare argomenti
    private void apriDashboard() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GUI/dashboard-view.fxml"));
        Parent root = fxmlLoader.load();

        // Non serve più settare l'utente nel controller, perché BaseController lo ha già statico

        Scene scene = new Scene(root);
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

    private void mostraErrore(String messaggio) {
        errorLabel.setText(messaggio);
        errorLabel.setStyle("-fx-text-fill: #ef4444;");
        errorLabel.setVisible(true);
    }
}