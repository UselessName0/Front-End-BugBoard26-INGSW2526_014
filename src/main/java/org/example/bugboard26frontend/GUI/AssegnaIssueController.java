package org.example.bugboard26frontend.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.bugboard26frontend.Entita.Issue;
import org.example.bugboard26frontend.Entita.Utente;

public class AssegnaIssueController extends BaseController {

    @FXML private Circle avatarCircle;
    @FXML private TableView<Utente> tabellaUtenti;
    @FXML private TableColumn<Utente, String> colNome;
    @FXML private TableColumn<Utente, String> colCognome;
    @FXML private TableColumn<Utente, String> colRuolo;
    @FXML private TableColumn<Utente, Void> colAzione;

    private Issue issueDaAssegnare;
    private final ContextMenu menuUtente = new ContextMenu();

    public void initialize() {
        setupMenuUtente();
        configuraTabella();
    }

    private void configuraTabella() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colRuolo.setCellValueFactory(new PropertyValueFactory<>("ruolo"));

        // LOGICA DEL BOTTONE "ASSEGNA"
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

        // TODO: Chiamata al Backend per salvare
        System.out.println("Assegno issue " + issueDaAssegnare.getId() + " a " + utente.getCognome());

        mostraAlert(Alert.AlertType.INFORMATION, "Assegnata!",
                "Issue assegnata a " + utente.getNome() + " " + utente.getCognome());

        // Torniamo alla dashboard
        Stage stage = (Stage) tabellaUtenti.getScene().getWindow();
        apriDashboard(stage);
    }

    public void setIssueDaAssegnare(Issue issue) {
        this.issueDaAssegnare = issue;
    }

    // --- MENU UTENTE ---
    private void setupMenuUtente() {
        String nome = (utenteLoggato != null) ? utenteLoggato.getNome() : "User";
        MenuItem voceProfilo = new MenuItem("Ciao " + nome);
        voceProfilo.setDisable(true);
        voceProfilo.getStyleClass().add("menu-titolo");

        MenuItem voceLogout = new MenuItem("Logout");
        voceLogout.setOnAction((ActionEvent event) -> {
            if (avatarCircle.getScene() != null) {
                Stage stage = (Stage) avatarCircle.getScene().getWindow();
                effettuaLogout(stage);
            }
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

    // -- GESTIONE ALLERT --
    private void mostraAlert(Alert.AlertType tipo, String titolo, String messaggio) {
        Alert alert = new Alert(tipo);
        alert.setTitle("BugBoard 26 - " + titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}