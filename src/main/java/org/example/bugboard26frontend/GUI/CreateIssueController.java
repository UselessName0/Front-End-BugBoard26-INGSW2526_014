package org.example.bugboard26frontend.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.bugboard26frontend.apiservices.ApiClient;
import org.example.bugboard26frontend.apiservices.IssueService;
import org.example.bugboard26frontend.entita.Issue;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.example.bugboard26frontend.enums.Priorita;
import org.example.bugboard26frontend.enums.Tipo;


public class CreateIssueController extends BaseController {
    @FXML private TextField titoloField;
    @FXML private ComboBox<String> tipoCombo;
    @FXML private ComboBox<String> prioritaCombo;
    @FXML private TextArea descrizioneArea;
    @FXML private Label labelNomeFile;
    private File fileSelezionato;
    private ApiClient apiClient = ApiClient.getApiClient();
    private IssueService issueService = new IssueService();


    public void initialize() {
       List<String> listaTipi = new ArrayList<>();
       for(Tipo tipo : Tipo.values()) {
           listaTipi.add(tipo.name());
       }
       tipoCombo.getItems().addAll(listaTipi);

       List<String> listaPriorita = new ArrayList<>();
         for(Priorita priorita : Priorita.values()) {
              listaPriorita.add(priorita.name());
         }
         prioritaCombo.getItems().addAll(listaPriorita);
    }
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
            nuovaIssue.setUtente(apiClient.getUtenteLoggato());
            issueService.creaIssue(nuovaIssue);
            System.out.println("Issue pubblicata con successo!");
            Stage stage =  (Stage) labelNomeFile.getScene().getWindow();
            apriDashboard(stage);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }
}