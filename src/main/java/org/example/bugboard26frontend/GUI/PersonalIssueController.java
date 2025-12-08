package org.example.bugboard26frontend.GUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.example.bugboard26frontend.apiservices.IssueService;
import org.example.bugboard26frontend.entita.Issue;
import org.example.bugboard26frontend.enums.Stato;
import org.example.bugboard26frontend.enums.Tipo;
import java.util.List;

public class PersonalIssueController extends BaseController {

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
    @FXML private Button btnCreaUtente;

    private ContextMenu menuUtente;
    private final IssueService issueService = new IssueService();

    @FXML
    public void initialize() {
        boolean isAdmin = checkifAdmin();
        if(!isAdmin){
            btnCreaUtente.setVisible(false);
            btnCreaUtente.setManaged(false);
        }
        statoComboBox.getItems().setAll(Stato.values());
        tipoComboBox.getItems().setAll(Tipo.values());
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colPriorita.setCellValueFactory(new PropertyValueFactory<>("priorita"));
        colStato.setCellValueFactory(new PropertyValueFactory<>("stato"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataCreazione"));
        colAutore.setCellValueFactory(cellData -> {
            if (cellData.getValue().getUtente() != null) {
                return new SimpleStringProperty(cellData.getValue().getUtente().getNome() + " " + cellData.getValue().getUtente().getCognome());
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
                        case "ALTA": badge.setStyle(baseStyle + "-fx-background-color: #ef4444;"); break;
                        case "MEDIA": badge.setStyle(baseStyle + "-fx-background-color: #f59e0b; -fx-text-fill: #000000;"); break;
                        case "BASSA": badge.setStyle(baseStyle + "-fx-background-color: #10b981;"); break;
                        default: badge.setStyle(baseStyle + "-fx-background-color: #64748b;");
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
        String nomeUtente = (utenteLoggato != null) ? utenteLoggato.getNome() : "Ospite";
        MenuItem voceProfilo = new MenuItem("Ciao " + nomeUtente);
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
                menuUtente.getSkin().getNode().getScene().getStylesheets().add(
                        getClass().getResource("StyleGeneric.css").toExternalForm()
                );
            }
        });
        ButtonCerca.setOnAction(e -> {
            effettuaRicerca();
        });
        if (utenteLoggato != null) {
            System.out.println("Utente trovato (" + utenteLoggato.getNome() + "), avvio ricerca...");
            ButtonCerca.fire(); // Ora questo funzionerà e lancerà la ricerca!
        } else {
            System.err.println("Errore: Nessun utente loggato in BaseController.");
            tabellaIssue.setPlaceholder(new Label("Effettua il login per vedere le issue."));
        }
    }

    public void effettuaRicerca() {
        String testoRicerca = searchField.getText();
        Stato statoSelezionato = statoComboBox.getValue();
        Tipo tipoSelezionato = tipoComboBox.getValue();

        Long idUtente = (utenteLoggato != null) ? utenteLoggato.getId() : null;

        if (idUtente == null) {
            tabellaIssue.setPlaceholder(new Label("Errore: Utente non loggato."));
            return;
        }

        ButtonCerca.setDisable(true);
        tabellaIssue.setPlaceholder(new Label("Caricamento issue personali..."));

        Task<List<Issue>> task = new Task<>() {
            @Override
            protected List<Issue> call() throws Exception {
                return issueService.getIssuesByUtente(
                        idUtente,
                        testoRicerca,
                        statoSelezionato,
                        tipoSelezionato,
                        "data",
                        "desc",
                        0,
                        16
                );
            }
        };

        task.setOnSucceeded(e -> {
            List<Issue> issues = task.getValue();
            tabellaIssue.setItems(FXCollections.observableArrayList(issues));
            if (issues.isEmpty()) {
                tabellaIssue.setPlaceholder(new Label("Non hai issue assegnate con questi filtri."));
            }
            ButtonCerca.setDisable(false);
        });

        task.setOnFailed(e -> {
            Throwable errore = task.getException();
            errore.printStackTrace();
            tabellaIssue.setPlaceholder(new Label("Errore durante il caricamento delle issue."));
            ButtonCerca.setDisable(false);
        });

        new Thread(task).start();
    }

    @FXML
    private void tornaIndietro(ActionEvent event) {
        apriDashboard(event);
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