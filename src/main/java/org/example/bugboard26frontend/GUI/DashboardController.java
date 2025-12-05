package org.example.bugboard26frontend.GUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.example.bugboard26frontend.apiservices.ApiClient;
import org.example.bugboard26frontend.apiservices.IssueService;
import org.example.bugboard26frontend.entita.Issue;
import org.example.bugboard26frontend.entita.Utente;
import org.example.bugboard26frontend.enums.Stato;
import org.example.bugboard26frontend.enums.Tipo;
import org.example.bugboard26frontend.enums.Priorita;

import java.util.List;

public class DashboardController extends BaseController{

    @FXML private Button ButtonCerca;
    @FXML private ComboBox<Tipo> tipoComboBox;
    @FXML private ComboBox<Stato> statoComboBox;
    @FXML private TextField searchField;
    @FXML private TableView<Issue> tabellaIssue;
    @FXML private TableColumn<Issue, String> colTitolo;
    @FXML private TableColumn<Issue, String> colAutore;
    @FXML private TableColumn<Issue, String> colPriorita;
    @FXML private TableColumn<Issue, String> colStato;
    @FXML private TableColumn<Issue, String> colTipo;
    @FXML private TableColumn<Issue, String> colData;
    @FXML private Circle avatarCircle;


    private ContextMenu menuUtente;
    private final IssueService issueService = new IssueService();

    // Metodo per l'inizializzazione della tabella centrale (da modificare ovviamente)
    @FXML
    public void initialize() throws Exception {
        statoComboBox.getItems().setAll(Stato.values());
        tipoComboBox.getItems().setAll(Tipo.values());
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colPriorita.setCellValueFactory(new PropertyValueFactory<>("priorita"));
        colStato.setCellValueFactory(new PropertyValueFactory<>("stato"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataCreazione"));
        // Se autore è null perchè magari gli è stata tolta l'assegnazione (rimane comunque aperta ?)
        colAutore.setCellValueFactory(cellData -> {
            if (cellData.getValue().getUtente() != null) {
                return new SimpleStringProperty(cellData.getValue().getUtente().getNome() + " " + cellData.getValue().getUtente().getCognome()); // O getNome()
            } else {
                return new SimpleStringProperty("---");
            }
        });
        colPriorita.setCellFactory(column -> new TableCell<Issue, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                    setGraphic(null);
                } else {
                    Label badge = new Label(item);
                    String baseStyle = "-fx-text-fill: #000000; -fx-font-weight: bold; -fx-padding: 3 10 3 10; -fx-background-radius: 5;";
                    switch (item.toUpperCase()) {
                        case "ALTA":
                            badge.setStyle(baseStyle + "-fx-background-color: #ef4444;");
                            break; // Rosso
                        case "MEDIA":
                            badge.setStyle(baseStyle + "-fx-background-color: #f59e0b; -fx-text-fill: #000000;");
                            break; // Arancione
                        case "BASSA":
                            badge.setStyle(baseStyle + "-fx-background-color: #10b981;");
                            break; // Verde
                        default:
                            badge.setStyle(baseStyle + "-fx-background-color: #64748b;");
                    }
                    HBox box = new HBox(badge);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                    setText(null);
                }
            }
        });
        tabellaIssue.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tabellaIssue.getSelectionModel().getSelectedItem() != null) {
                Issue issueSelezionata = tabellaIssue.getSelectionModel().getSelectedItem();
                Stage stageCorrente = (Stage) tabellaIssue.getScene().getWindow();
                apriDettaglioIssue(issueSelezionata, stageCorrente);
            }
        });

        // Menu utente
        menuUtente = new ContextMenu();
        MenuItem voceProfilo = new MenuItem("Ciao " + (utenteLoggato != null ? utenteLoggato.getNome() : "User"));
        voceProfilo.setDisable(true);
        voceProfilo.getStyleClass().add("menu-titolo");
        MenuItem voceLogout = new MenuItem("Logout");
        voceLogout.setOnAction((ActionEvent event) -> {
            Stage stageCorrente = (Stage) tabellaIssue.getScene().getWindow();
            effettuaLogout(stageCorrente);
        });
        menuUtente.getItems().addAll(voceProfilo, new SeparatorMenuItem(), voceLogout);
        menuUtente.getStyleClass().add("mio-menu-utente");
        menuUtente.setOnShowing(e -> {
            if (menuUtente.getSkin() != null) {
                javafx.scene.Node nodoMenu = menuUtente.getSkin().getNode();
                nodoMenu.getScene().getStylesheets().add(
                        getClass().getResource("StyleGeneric.css").toExternalForm()
                );
            }
        });

        ButtonCerca.setOnMouseClicked(e -> {
            effettuaRicerca();
        });

        ButtonCerca.fire();

        // Carichiamo i dati finti per ora
        caricaDatiIssue();
    }

    public void effettuaRicerca() {

        String testoRicerca = searchField.getText();
        Stato statoSelezionato = statoComboBox.getValue();
        Tipo tipoSelezionato = tipoComboBox.getValue();

        ButtonCerca.setDisable(true);
        tabellaIssue.setPlaceholder(new Label("Caricamento in corso..."));

        Task<List<Issue>> task = new Task<>() {
            @Override
            protected List<Issue> call() throws Exception {
                return issueService.getIssues(testoRicerca, statoSelezionato, tipoSelezionato, "data", "desc", 0, 16);
            }
        };

        task.setOnSucceeded(e -> {
            List<Issue> issues = task.getValue();

            tabellaIssue.setItems(FXCollections.observableArrayList(issues));

            ButtonCerca.setDisable(false);
        });

        task.setOnFailed(e -> {
            Throwable errore = task.getException();
            errore.printStackTrace();

            tabellaIssue.setPlaceholder(new Label("Errore durante il caricamento delle issue."));
            ButtonCerca.setDisable(false);
        });

        new Thread(task).start();

    };


    private void caricaDatiIssue() throws Exception {
        String testoRicerca = searchField.getText();
        Stato statoSelezionato = statoComboBox.getValue();
        Tipo tipoSelezionato = tipoComboBox.getValue();

        List<Issue> listaFinta = issueService.getIssues(testoRicerca, statoSelezionato, tipoSelezionato, "data", "desc", 0, 16);
        ObservableList<Issue> datiGrafici = FXCollections.observableArrayList(listaFinta);
        tabellaIssue.setItems(datiGrafici);
    }


    @FXML
    void apriMenuUtente(MouseEvent event) {
        if (menuUtente.isShowing()) {
            menuUtente.hide();
        } else {
            menuUtente.show(avatarCircle, Side.BOTTOM, -100, 10);
        }
        event.consume();
    }



}