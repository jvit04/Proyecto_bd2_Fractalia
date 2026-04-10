package application;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utilities.Paths;

import javafx.scene.image.Image;
import java.io.IOException;
import java.util.Objects;

public class App extends Application {

    public static void main(String[] args){

        launch(args);
    }
//metodo start de java FX que permite iniciar la aplicación
    @Override
    public void start(Stage stage) throws IOException {
        AnchorPane loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Paths.FractaliaApp)));
        Scene scene = new Scene(loader);
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.setResizable(false);
        Image icono = new Image(Objects.requireNonNull(getClass().getResourceAsStream(Paths.ImagenIconoApp)));//icono de la aplicacion
        stage.getIcons().add(icono);
        stage.setTitle("Fractalia");//titulo de la barra superior
        stage.show();

    }
}
