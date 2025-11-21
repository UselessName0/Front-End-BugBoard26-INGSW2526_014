package org.example.bugboard26frontend.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.bugboard26frontend.Entita.ApiService;
import org.example.bugboard26frontend.Entita.Utente;

public class LoginController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final ApiService apiService = new ApiService();

    @FXML
    protected void onLoginClick() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Resetta messaggi precedenti
        errorLabel.setVisible(false);

        if(email.isEmpty() || password.isEmpty()){
            mostraErrore("Inserisci email e password!");
            return;
        }

        try {
            // Chiamata al Backend
            Utente utente = apiService.login(email, password);

            // Se arrivi qui, è andato tutto bene
            errorLabel.setText("BENVENUTO " + utente.getEmail() + "!");
            errorLabel.setStyle("-fx-text-fill: #10b981;"); // Verde
            errorLabel.setVisible(true);

            // TODO: Qui in futuro aprirai la Dashboard
            System.out.println("Token sessione: " + utente.getSessioneToken());

        } catch (Exception e) {
            // Errore dal server (es. Password sbagliata)
            mostraErrore("Errore: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostraErrore(String messaggio) {
        errorLabel.setText(messaggio);
        errorLabel.setStyle("-fx-text-fill: #ef4444;"); // Rosso
        errorLabel.setVisible(true);
    }
}
