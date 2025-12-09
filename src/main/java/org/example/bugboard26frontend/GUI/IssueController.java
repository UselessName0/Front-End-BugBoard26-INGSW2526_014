package org.example.bugboard26frontend.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.bugboard26frontend.apiservices.ApiClient;
import org.example.bugboard26frontend.apiservices.CommentoService;
import org.example.bugboard26frontend.entita.Commento;
import org.example.bugboard26frontend.entita.Issue;
import org.example.bugboard26frontend.entita.Utente;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.List;


public class IssueController extends BaseController {

    @FXML private VBox commentsContainer;
    @FXML private TextField titoloField;
    @FXML private Label labelTitolo;
    @FXML private Label labelDescrizione;
    @FXML private Label badgePriorita;
    @FXML private Label badgeTipo;
    @FXML private TextArea textAreaCommento;
    @FXML private Button btnCreaUtente;
    @FXML private Button btnAssegnaIssue;
    @FXML private Button btnArchiviaIssue;


    private CommentoService commentoService = new CommentoService();
    private Issue issueCorrente;
    private Issue issueDaAssegnare;
    private final ApiClient apiClient = ApiClient.getApiClient();

    public void setDatiIssue(Issue issue) throws Exception {
        boolean isAdmin = checkifAdmin();
        if(!isAdmin){
            btnCreaUtente.setVisible(false);
            btnCreaUtente.setManaged(false);
            btnAssegnaIssue.setVisible(false);
            btnAssegnaIssue.setManaged(false);
            btnArchiviaIssue.setVisible(false);
            btnArchiviaIssue.setManaged(false);
        }
        this.issueCorrente = issue;
        if (issue != null) {
            // 1. Popola Titolo
            if (labelTitolo != null) {
                labelTitolo.setText(issue.getTitolo());
            }

            // 2. Popola Descrizione
            if (labelDescrizione != null) {
                if (issue.getDescrizione() != null && !issue.getDescrizione().isEmpty()) {
                    labelDescrizione.setText(issue.getDescrizione());
                } else {
                    labelDescrizione.setText("Nessuna descrizione fornita per questa issue.");
                }
            }

            // 3. Popola Badge Priorità
            if (badgePriorita != null) {
                badgePriorita.setText(issue.getPriorita().toUpperCase());
                aggiornaStileBadge(badgePriorita, issue.getPriorita());
            }

            // 4. Popola Badge Tipo
            if (badgeTipo != null) {
                badgeTipo.setText(issue.getTipo().toUpperCase());
                aggiornaStileBadge(badgeTipo, issue.getTipo());
            }

            caricaCommenti();
        }
    }

    private void aggiornaStileBadge(Label badge, String testo) {
        // Rimuovi tutti gli stili potenziali precedenti per evitare conflitti
        badge.getStyleClass().removeAll("badge-red", "badge-purple", "badge-green", "badge-orange");
        String t = testo.toUpperCase();
        if (!badge.getStyleClass().contains("badge")) {
            badge.getStyleClass().add("badge");
        }
        if (t.equals("ALTA") || t.equals("CRITICAL")) {
            badge.getStyleClass().add("badge-red");
        } else if (t.equals("BUG") || t.equals("FEATURE")) {
            badge.getStyleClass().add("badge-purple");
        } else if (t.equals("BASSA")) {
            badge.getStyleClass().add("badge-green");
        } else if (t.equals("MEDIA")) {
            badge.getStyleClass().add("badge-orange");
        } else {
            badge.getStyleClass().add("badge-purple");
        }
    }

    @FXML
    private void tornaIndietro (ActionEvent event) {
        try {
            Stage stage = (Stage) titoloField.getScene().getWindow();
            apriDashboard(stage);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore tornando indietro: " + e.getMessage());
        }
    }

    public void setUtenteLoggato(Utente utenteLoggato) {
    }

    @FXML
    private void apriAssegnazioneIssue(ActionEvent event) {
        try {
            // 1. Carichiamo il loader manualmente invece di usare cambiaScena()
            FXMLLoader loader = new FXMLLoader(getClass().getResource("assegnaissue-view.fxml"));
            Parent root = loader.load();

            // 2. Otteniamo il controller della nuova pagina
            AssegnaIssueController controller = loader.getController();

            // 3. PASSAGGIO FONDAMENTALE: Passiamo l'issue corrente al nuovo controller
            if (this.issueCorrente != null) {
                controller.setIssueDaAssegnare(this.issueCorrente);
                System.out.println("Sto passando l'issue ID: " + this.issueCorrente.getId());
            } else {
                System.out.println("Attenzione: issueCorrente è null!");
            }

            // 4. Mostriamo la scena (codice simile a cambiaScena)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("BugBoard 26 - Assegna Issue");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore caricamento vista assegnazione: " + e.getMessage());
        }
    }

    private void caricaCommenti() throws Exception {
        List<Commento> commenti = commentoService.getCommentiByIssueId(issueCorrente.getId());
        commentsContainer.getChildren().clear();

        for(Commento commento : commenti) {
            VBox cardCommento = new VBox();
            cardCommento.setSpacing(5);
            cardCommento.getStyleClass().add("comment-box");

            // --- 1. GESTIONE AUTORE ---
            String testoAutore;
            if (commento.getUtente() != null) {
                testoAutore = commento.getUtente().getNome() + " " + commento.getUtente().getCognome() + ":";
            } else {
                testoAutore = "Utente sconosciuto:";
            }
            Label autoreLabel = new Label(testoAutore);
            autoreLabel.setStyle("-fx-text-fill: #3b82f6; -fx-font-weight: bold;");

            // --- 2. GESTIONE CONTENUTO ---
            Label contenutoLabel = new Label(commento.getContenuto());
            contenutoLabel.setStyle("-fx-text-fill: #cbd5e1;");
            contenutoLabel.setWrapText(true);

            // --- 3. GESTIONE LIKE ---
            HBox likeBox = new HBox();
            likeBox.setAlignment(Pos.CENTER_LEFT);
            likeBox.setSpacing(8);
            likeBox.setPadding(new Insets(5, 0, 0, 0));

            // Creiamo il bottone UNA VOLTA sola
            Button btnLike = new Button("❤ Mi piace");

            // Creiamo la label col numero

            int numerolike = commento.getNumeroMiPiace(); // O commento.getNumeroMiPiace() controlla il nome esatto nel getter
            Label lblNumeroLike = new Label(String.valueOf(numerolike));
            lblNumeroLike.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11px;");

            // Logica per controllare se HO messo like
            boolean hoMessoLike = false;

            // Controllo di sicurezza: verifichiamo che l'utente sia loggato e la lista miPiace non sia null
            if (apiClient.getUtenteLoggato() != null && commento.getMiPiace() != null) {
                Long mioId = apiClient.getUtenteLoggato().getId();

                for(Utente u : commento.getMiPiace()) {
                    if(u.getId().equals(mioId)) {
                        hoMessoLike = true;
                        break;
                    }
                }
            }

            // Applichiamo lo stile (Rosso se like attivo, Grigio se no)
            if(hoMessoLike) {
                btnLike.setStyle("-fx-background-color: transparent; -fx-text-fill: #ef4444; -fx-padding: 0; -fx-cursor: hand; -fx-font-size: 11px; -fx-font-weight: bold;");
            } else {
                btnLike.setStyle("-fx-background-color: transparent; -fx-text-fill: #64748b; -fx-padding: 0; -fx-cursor: hand; -fx-font-size: 11px;");
            }

            // Assegniamo l'azione UNA VOLTA sola
            btnLike.setOnAction(e -> {
                System.out.println("Click like rilevato");
                try {
                    // Chiamata al service
                    commentoService.toggleLike(commento.getId());
                    // Ricarichiamo per aggiornare numero e colore
                    caricaCommenti();
                } catch(Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Errore nel mettere/togliere like: " + ex.getMessage());
                }
            });

            // Aggiungiamo bottone e numero al box orizzontale
            likeBox.getChildren().addAll(btnLike, lblNumeroLike);

            // Aggiungiamo tutto alla card
            cardCommento.getChildren().addAll(autoreLabel, contenutoLabel, likeBox);
            commentsContainer.getChildren().add(cardCommento);
        }
    }

    @FXML
    private void inserisciCommento(ActionEvent event) {
        String testo = textAreaCommento.getText();
        if (testo == null || testo.trim().isEmpty()) {
            System.out.println("Il commento non può essere vuoto!");
            return;
        }
        try {
            Commento nuovoCommento = new Commento();
            nuovoCommento.setContenuto(testo);
            nuovoCommento.setIssue(this.issueCorrente);
            nuovoCommento.setUtente(this.utenteLoggato);
            commentoService.creaCommento(nuovoCommento);
            textAreaCommento.clear();
            caricaCommenti();
            System.out.println("Commento inserito con successo!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore durante l'inserimento del commento: " + e.getMessage());
        }
    }
}