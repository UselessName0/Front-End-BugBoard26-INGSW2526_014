package org.example.bugboard26frontend.GUI;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.bugboard26frontend.apiservices.IssueService;
import org.example.bugboard26frontend.apiservices.UtenteService;
import org.example.bugboard26frontend.entita.Issue;
import org.example.bugboard26frontend.entita.Utente;

import java.util.List;

public class AssegnaIssueController extends BaseController {

    @FXML private Circle avatarCircle;
    @FXML private TableView<Utente> tabellaUtenti;
    @FXML private TableColumn<Utente, String> colNome;
    @FXML private TableColumn<Utente, String> colCognome;
    @FXML private TableColumn<Utente, String> colRuolo;
    @FXML private TableColumn<Utente, Void> colAzione;

    private Issue issueDaAssegnare;
    private final ContextMenu menuUtente = new ContextMenu();
    
    private final UtenteService utenteService = new UtenteService();
    private final IssueService issueService = new IssueService();

    public void initialize() {
        configuraTabella();
        caricaUtentiDisponibili();
    }
    
    private void caricaUtentiDisponibili() {
        tabellaUtenti.setPlaceholder(new Label("Caricamento utenti in corso..."));
        
        Task<List<Utente>> taskCaricamento = new Task<>() {
            @Override
            protected List<Utente> call() throws Exception {
                return utenteService.getAllUtenti();
            }
        };
        
        taskCaricamento.setOnSucceeded(event -> {
            List<Utente> utenti = taskCaricamento.getValue();
            if (utenti == null || utenti.isEmpty()) {
                tabellaUtenti.setPlaceholder(new Label("Nessun utente trovato nel sistema."));
            } else {
                tabellaUtenti.setItems(FXCollections.observableArrayList(utenti));
            }
        });
        
        taskCaricamento.setOnFailed(event -> {
            Throwable e = taskCaricamento.getException();
            e.printStackTrace();
            mostraAlert(Alert.AlertType.ERROR, "Errore API", "Impossibile caricare la lista utenti.");
        });

        new Thread(taskCaricamento).start();
    }

    private void configuraTabella() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));

        Callback<TableColumn<Utente, Void>, TableCell<Utente, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Utente, Void> call(final TableColumn<Utente, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Assegna");

                    {
                        btn.getStyleClass().add("btn-create");
                        btn.setStyle("-fx-font-size: 11px; -fx-padding: 5 10;");
                        btn.setOnAction((ActionEvent event) -> {
                            Utente utenteSelezionato = getTableView().getItems().get(getIndex());
                            gestisciAssegnazione(utenteSelezionato);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
        colAzione.setCellFactory(cellFactory);
    }

    private void gestisciAssegnazione(Utente utente) {
        if (issueDaAssegnare == null) {
            mostraAlert(Alert.AlertType.ERROR, "Errore", "Nessuna issue selezionata.");
            return;
        }
        
        Task<Boolean> taskAssegnazione = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                issueService.settaAssegnatario(issueDaAssegnare.getId(), utente.getId());
                return true;
            }
        };
        
        taskAssegnazione.setOnSucceeded(event -> {
            mostraAlert(Alert.AlertType.INFORMATION, "Successo",
                    "Issue assegnata a " + utente.getNome() + " " + utente.getCognome());
            Stage stage = (Stage) tabellaUtenti.getScene().getWindow();
            apriDashboard(stage);
        });
        
        taskAssegnazione.setOnFailed(event -> {
            Throwable e = taskAssegnazione.getException();
            e.printStackTrace();
            mostraAlert(Alert.AlertType.ERROR, "Errore Assegnazione",
                    "Impossibile assegnare l'issue: " + e.getMessage());
        });

        new Thread(taskAssegnazione).start();
    }

    // --- MENU UTENTE & ALERT ---
    @FXML
    void apriMenuUtente(MouseEvent event) {
        if (menuUtente.isShowing()) menuUtente.hide();
        else menuUtente.show(avatarCircle, Side.BOTTOM, -100, 10);
        event.consume();
    }

    private void mostraAlert(Alert.AlertType tipo, String titolo, String messaggio) {
        Alert alert = new Alert(tipo);
        alert.setTitle("BugBoard 26 - " + titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    public void setIssueDaAssegnare(Issue issue) {
        this.issueDaAssegnare = issue;
        System.out.println("AssegnaIssueController: Ho ricevuto l'issue ID " + issue.getId());
    }
}