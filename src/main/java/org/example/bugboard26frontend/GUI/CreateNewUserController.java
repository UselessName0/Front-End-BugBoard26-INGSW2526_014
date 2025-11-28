package org.example.bugboard26frontend.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CreateNewUserController extends BaseController {

    @FXML private TextField nomeField;
    @FXML private TextField cognomeField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    protected void onCreaClick(ActionEvent event) {
        String nome = nomeField.getText();
        String cognome = cognomeField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // 1. VALIDAZIONE LOCALE
        if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty()) {
            // Creiamo l'alert di errore direttamente qui
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore Dati");
            alert.setHeaderText(null);
            alert.setContentText("Per favore, compila tutti i campi prima di proseguire.");
            alert.showAndWait();
            return;
        }

        // 2. LOGICA DI SALVATAGGIO (Qui chiamerai il tuo backend)
        System.out.println("Salvataggio utente: " + email);

        // 3. POPUP DI SUCCESSO
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Operazione Completata");
        alert.setHeaderText(null);
        alert.setContentText("L'utente " + nome + " " + cognome + " è stato creato con successo!");

        // showAndWait() blocca il codice finché l'utente non preme OK
        alert.showAndWait();

        // 4. CAMBIO PAGINA (Usiamo il metodo ereditato per tornare alla dashboard)
        apriDashboard(event);
    }

    @FXML
    protected void tornaIndietro(ActionEvent event) {
        apriDashboard(event);
    }
}