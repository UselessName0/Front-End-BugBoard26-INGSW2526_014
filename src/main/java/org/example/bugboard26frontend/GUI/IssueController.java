package org.example.bugboard26frontend.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
    @FXML private VBox commentBox;
    @FXML private TextField titoloField;
    @FXML private Label labelTitolo;
    @FXML private Label labelDescrizione;
    @FXML private Label badgePriorita;
    @FXML private Label badgeTipo;


    private CommentoService commentoService = new CommentoService();
    private Issue issueCorrente;


    public void setDatiIssue(Issue issue) throws Exception {
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
        try{
            cambiaScena(event, "assegnaissue-view.fxml", "Assegna Issue");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore caricamento vista: " + e.getMessage());
        }
    }

    private void caricaCommenti() throws Exception {
        List<Commento> commenti = commentoService.getCommentiByIssueId(issueCorrente.getId());

        commentsContainer.getChildren().clear();

        for(Commento commento : commenti) {
            // 1. Card Principale
            VBox cardCommento = new VBox();
            cardCommento.setSpacing(5);
            cardCommento.getStyleClass().add("comment-box");

            // 2. Autore
            Label autoreLabel = new Label(commento.getUtente().getNome() + " " + commento.getUtente().getCognome() + ":");
            autoreLabel.setStyle("-fx-text-fill: #3b82f6; -fx-font-weight: bold;");

            // 3. Contenuto
            Label contenutoLabel = new Label(commento.getContenuto());
            contenutoLabel.setStyle("-fx-text-fill: #cbd5e1;");
            contenutoLabel.setWrapText(true);

            // --- 4. AREA MI PIACE (Ricostruzione dell'HBox dell'FXML) ---
            HBox likeBox = new HBox();
            likeBox.setAlignment(Pos.CENTER_LEFT);
            likeBox.setSpacing(8);
            likeBox.setPadding(new Insets(5, 0, 0, 0)); // Padding top="5"

            // Bottone Cuore
            Button btnLike = new Button("❤ Mi piace");
            // Copio esattamente lo stile che avevi nell'FXML
            btnLike.setStyle("-fx-background-color: transparent; -fx-text-fill: #64748b; -fx-padding: 0; -fx-cursor: hand; -fx-font-size: 11px;");

            // Azione (Opzionale: per ora stampa solo in console)
            btnLike.setOnAction(e -> {
                System.out.println("Hai messo like al commento di " + commento.getUtente().getCognome());
                // Qui in futuro chiamerai il service per salvare il like
            });

            // Numero Like (Contatore)
            // Se nel DB hai un campo "numeroLike", usa: String.valueOf(commento.getNumeroLike())
            // Per ora metto "0" fisso o un numero random per bellezza
            Label lblNumeroLike = new Label("0");
            lblNumeroLike.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11px;");

            // Aggiungiamo bottone e numero alla riga orizzontale
            likeBox.getChildren().addAll(btnLike, lblNumeroLike);
            // -------------------------------------------------------------

            // 5. Aggiungiamo tutto alla card verticale
            cardCommento.getChildren().addAll(autoreLabel, contenutoLabel, likeBox);

            // 6. Aggiungiamo la card alla lista principale
            commentsContainer.getChildren().add(cardCommento);
        }

    }



}