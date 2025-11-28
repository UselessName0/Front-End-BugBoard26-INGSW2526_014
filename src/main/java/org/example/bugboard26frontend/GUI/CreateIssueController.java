package org.example.bugboard26frontend.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.bugboard26frontend.APIServices.ApiClient;
import org.example.bugboard26frontend.Entita.Issue;
import java.io.File;

import static org.example.bugboard26frontend.APIServices.ApiClient.getApiClient;

public class CreateIssueController extends BaseController {
    @FXML private TextField titoloField;
    @FXML private ComboBox<String> tipoCombo;
    @FXML private ComboBox<String> prioritaCombo;
    @FXML private TextArea descrizioneArea;
    @FXML private Label labelNomeFile;
    private File fileSelezionato;
    private ApiClient apiClient = ApiClient.getApiClient();

    // Metodo che permette la selezione di tipi specifici di file (foto)
    @FXML
    protected void selezionaFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona Immagine");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Stage stage = (Stage) labelNomeFile.getScene().getWindow();
        fileSelezionato = fileChooser.showOpenDialog(stage);
        if (fileSelezionato != null) {
            labelNomeFile.setText(fileSelezionato.getName());
        }
    }

    // Metodo che permette la pubblicazione di una nuova issue creata
    @FXML
    protected void pubblicaIssue() {
        String titolo = titoloField.getText();
        String tipo = tipoCombo.getValue();
        String priorita = prioritaCombo.getValue();
        String descrizione = descrizioneArea.getText();
        if (titolo.isEmpty() || descrizione.isEmpty()) {
            System.out.println("Errore: Compila tutti i campi!");
            return;
        }
        try {
            Issue nuovaIssue = new Issue();
            nuovaIssue.setTitolo(titolo);
            nuovaIssue.setTipo(tipo);
            nuovaIssue.setPriorita(priorita);
            nuovaIssue.setDescrizione(descrizione);
            nuovaIssue.setStato("TO DO");
          //  apiClient.creaIssue(nuovaIssue);
            System.out.println("Issue pubblicata con successo!");
            Stage stage =  (Stage) labelNomeFile.getScene().getWindow();
            apriDashboard(stage);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }
}