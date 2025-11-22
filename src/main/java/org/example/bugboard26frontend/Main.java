package org.example.bugboard26frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // 1. Carichiamo il file FXML
        // NOTA: Abbiamo aggiunto "GUI/" perché il file ora è in quella sottocartella nelle risorse
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GUI/dashboard-view.fxml"));

        // 2. Creiamo la scena
        // 400x500 sono dimensioni standard per un box di login verticale
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);

        // 3. Impostiamo il titolo e mostriamo la finestra
        stage.setTitle("BugBoard 26 - Login");
        stage.setScene(scene);
        stage.setResizable(false); // Impedisce di allargare la finestra (brutto nei login)
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
