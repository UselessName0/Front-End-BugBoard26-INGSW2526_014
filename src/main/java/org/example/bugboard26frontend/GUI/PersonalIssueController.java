package org.example.bugboard26frontend.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.example.bugboard26frontend.Main;
import java.io.IOException;

public class PersonalIssueController {

    @FXML private Circle avatarCircle;
    ContextMenu menuUtente = new ContextMenu();

    public void initialize() {
        // Menu utente
        MenuItem voceProfilo = new MenuItem("Ciao User");
        voceProfilo.setDisable(true);
        voceProfilo.getStyleClass().add("menu-titolo");
        MenuItem voceLogout = new MenuItem("Logout");
        voceLogout.setOnAction((ActionEvent event) -> {
            effettuaLogout();
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
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GUI/dashboard-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("BugBoard - Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore tornando indietro: " +e.getMessage());
        }
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

    private void effettuaLogout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/bugboard26frontend/GUI/login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 500);
            Stage stage = (Stage) avatarCircle.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();
            System.out.println("Logout effettuato con successo.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore durante il logout. Controlla il percorso del file FXML.");
        }
    }
}
