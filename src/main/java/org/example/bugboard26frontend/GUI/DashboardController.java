package org.example.bugboard26frontend.GUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.bugboard26frontend.Entita.ApiService;
import org.example.bugboard26frontend.Entita.Issue;
import org.example.bugboard26frontend.Entita.Utente;
import org.example.bugboard26frontend.Main;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    @FXML private TableView<Issue> tabellaIssue;
    @FXML private TableColumn<Issue, String> colTitolo;
    @FXML private TableColumn<Issue, String> colAutore;
    @FXML private TableColumn<Issue, String> colPriorita;
    @FXML private TableColumn<Issue, String> colStato;
    @FXML private TableColumn<Issue, String> colTipo;
    @FXML private TableColumn<Issue, String> colData;

    private final ApiService apiService = new ApiService();

    // Metodo per l'inizializzazione della tabella centrale (da modificare ovviamente)
    @FXML
    public void initialize() {
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colPriorita.setCellValueFactory(new PropertyValueFactory<>("priorita"));
        colStato.setCellValueFactory(new PropertyValueFactory<>("stato"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataCreazione"));
        // Se autore è null perchè magari gli è stata tolta l'assegnazione (rimane comunque aperta ?)
        colAutore.setCellValueFactory(cellData -> {
            if (cellData.getValue().getAssegnatario() != null) {
                return new SimpleStringProperty(cellData.getValue().getAssegnatario().getEmail()); // O getNome()
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
                        case "HIGH": badge.setStyle(baseStyle + "-fx-background-color: #ef4444;"); break; // Rosso
                        case "MEDIUM": badge.setStyle(baseStyle + "-fx-background-color: #f59e0b; -fx-text-fill: #000000;"); break;
                        case "LOW": badge.setStyle(baseStyle + "-fx-background-color: #10b981;"); break; // Verde
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
            // Se fai doppio click E hai selezionato qualcosa
            if (event.getClickCount() == 2 && tabellaIssue.getSelectionModel().getSelectedItem() != null) {
                Issue issueSelezionata = tabellaIssue.getSelectionModel().getSelectedItem();
                // Apri la pagina di dettaglio
                apriDettaglioIssue(issueSelezionata);
            }
        });

        // Carichiamo i dati finti per ora
        caricaDatiFinti();
    }

    // Metodo (da modificare) per caricare dati finti sulla tabella
    private void caricaDatiFinti() {
        List<Issue> listaFinta = new ArrayList<>();
        Utente u1 = new Utente(); u1.setEmail("mario.rossi@dev.it");
        Utente u2 = new Utente(); u2.setEmail("luigi.verdi@design.it");
        Utente u3 = new Utente(); u3.setEmail("admin@bugboard.com");
        listaFinta.add(creaIssue(1L, "Login non funzionante", "BUG", "HIGH", "TO DO", "2025-10-05", null));
        listaFinta.add(creaIssue(2L, "Aggiornare colori sidebar", "FEATURE", "LOW", "IN_PROGRESS", "2025-10-06", null));
        listaFinta.add(creaIssue(3L, "Errore esportazione PDF", "BUG", "MEDIUM", "TO DO", "2025-10-08", null));
        listaFinta.add(creaIssue(4L, "Manca documentazione API", "DOCUMENTATION", "HIGH", "DONE", "2025-10-01", null));
        listaFinta.add(creaIssue(5L, "Crash con immagini > 5MB", "BUG", "HIGH", "TO DO", "2025-10-09", null));
        Utente uTest = new Utente(); uTest.setEmail("admin@test.com");
        listaFinta.add(creaIssue(6L, "Esempio con autore", "QUESTION", "LOW", "DONE", "2025-09-25", uTest));
        ObservableList<Issue> datiGrafici = FXCollections.observableArrayList(listaFinta);
        tabellaIssue.setItems(datiGrafici);
    }

    // Metodo (da mofidicare) per la creazione di una issue
    private Issue creaIssue(Long id, String titolo, String tipo, String priorita, String stato, String data, Utente autore) {
        Issue i = new Issue();
        i.setId(id);
        i.setTitolo(titolo);
        i.setTipo(tipo);
        i.setPriorita(priorita);
        i.setStato(stato);
        i.setDataCreazione(data);
        i.setAssegnatario(autore);
        return i;
    }

    // Metodo per l'apertura dello stage di creazione di una nuova issue
    @FXML
    public void apriCreaIssue() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GUI/nuovaissue-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) tabellaIssue.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("BugBoard 26 - Crea Nuova Issue");
            stage.setWidth(1280);
            stage.setHeight(720);
            stage.centerOnScreen();
            stage.setResizable(true);
            stage.setMinWidth(1024);
            stage.setMinHeight(768);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore nell'apertura della schermata Crea Issue: " + e.getMessage());
        }
    }

    // --- METODO PER APRIRE LA NUOVA PAGINA ---
    public void apriDettaglioIssue(Issue issueSelezionata) {
        try {
            // Carica il file FXML che abbiamo appena rifatto uguale al mockup
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/bugboard26frontend/GUI/issue-view.fxml"));
            Parent root = fxmlLoader.load();

            // Passa i dati al controller della pagina di dettaglio
            IssueController controller = fxmlLoader.getController();
            controller.setDatiIssue(issueSelezionata);

            // Cambia la scena
            Scene scene = new Scene(root);
            Stage stage = (Stage) tabellaIssue.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Dettaglio Issue #" + issueSelezionata.getId());
            stage.setWidth(1280);
            stage.setHeight(720);
            stage.centerOnScreen();
            stage.setResizable(true);
            stage.setMinWidth(1024);
            stage.setMinHeight(768);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore apertura dettaglio: " + e.getMessage());
        }
    }
}