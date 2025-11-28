package org.example.bugboard26frontend.GUI;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.bugboard26frontend.APIServices.ApiClient;
import org.example.bugboard26frontend.APIServices.AuthService;
import org.example.bugboard26frontend.Entita.Utente;
import org.example.bugboard26frontend.Main;

import java.io.IOException;
import java.security.AuthProvider;

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
                return authService.Login(email, password);
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
    private void apriDashboard(Utente utenteLoggato) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GUI/dashboard-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) emailField.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("BugBoard 26 - Dashboard di " + utenteLoggato.getNome());
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.centerOnScreen();
        stage.setResizable(true);
        stage.setMinWidth(1024);
        stage.setMinHeight(768);
    }

    // Metodo che mostra errore nel caso di login non andato a buon fine
    private void mostraErrore(String messaggio) {
        errorLabel.setText(messaggio);
        errorLabel.setStyle("-fx-text-fill: #ef4444;");
        errorLabel.setVisible(true);
    }
}