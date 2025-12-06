package org.example.bugboard26frontend.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.bugboard26frontend.apiservices.UtenteService;
import org.example.bugboard26frontend.entita.Utente;

public class CreateNewUserController extends BaseController {

    @FXML private TextField nomeField;
    @FXML private TextField cognomeField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private final UtenteService utenteService = new UtenteService();

    @FXML
    protected void onCreaClick(ActionEvent event) {
        String nome = nomeField.getText();
        String cognome = cognomeField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty()) {
            mostraAlert(Alert.AlertType.ERROR, "Dati Mancanti", "Per favore, compila tutti i campi prima di proseguire.");
            return;
        }

        String passwordRegex = "^(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])(?=\\S+$).{8,}$";

        if (!password.matches(passwordRegex)) {
            mostraAlert(Alert.AlertType.WARNING, "Password non valida",
                    "La password non rispetta i requisiti di sicurezza.\n" +
                            "Assicurati che abbia:\n\n" +
                            "• Almeno 8 caratteri\n" +
                            "• Almeno un numero\n" +
                            "• Almeno un carattere speciale (es. ! @ # $)\n" +
                            "• Nessuno spazio vuoto");
            return; // Interrompiamo qui se la password non va bene
        }

        try {
            Utente nuovoUtente = new Utente();
            nuovoUtente.setNome(nome);
            nuovoUtente.setCognome(cognome);
            nuovoUtente.setEmail(email);
            nuovoUtente.setPassword(password);
            utenteService.creaUtente(nuovoUtente);

            mostraAlert(Alert.AlertType.INFORMATION, "Operazione Completata",
                    "L'utente " + nome + " " + cognome + " è stato creato con successo!");

            apriDashboard(event);

        } catch (Exception e) {
            e.printStackTrace();

            String msg = e.getMessage();
            if (msg.contains("ConstraintViolation")) {
                msg = "La password o l'email non rispettano i criteri del server.";
            }
            mostraAlert(Alert.AlertType.ERROR, "Errore Creazione", "Impossibile salvare l'utente:\n" + msg);
        }
    }

    // Metodo di utilità per creare Alert puliti senza ripetere codice
    private void mostraAlert(Alert.AlertType tipo, String titolo, String contenuto) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }

    @FXML
    protected void tornaIndietro(ActionEvent event) {
        apriDashboard(event);
    }
}