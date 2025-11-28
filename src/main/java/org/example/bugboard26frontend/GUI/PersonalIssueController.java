package org.example.bugboard26frontend.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class PersonalIssueController extends BaseController {

    @FXML private Circle avatarCircle;

    private final ContextMenu menuUtente = new ContextMenu();

    @FXML
    public void initialize() {
        String nomeUtente = (utenteLoggato != null) ? utenteLoggato.getNome() : "User";
        MenuItem voceProfilo = new MenuItem("Ciao " + nomeUtente);
        voceProfilo.setDisable(true);
        voceProfilo.getStyleClass().add("menu-titolo");
        MenuItem voceLogout = new MenuItem("Logout");

        voceLogout.setOnAction((ActionEvent event) -> {
            if (avatarCircle.getScene() != null) {
                Stage stage = (Stage) avatarCircle.getScene().getWindow();
                // Chiamiamo il metodo del padre che accetta lo Stage
                effettuaLogout(stage);
            }
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