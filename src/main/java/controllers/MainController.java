package controllers;

import javafx.event.ActionEvent; // ¡IMPORTACIÓN CORREGIDA!
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {

    @FXML
    private StackPane areaContenido; // El lienzo en blanco del centro

    // Método genérico para cambiar la vista
    private void cargarVista(String archivoFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/" + archivoFxml));
            Node vista = loader.load();

            areaContenido.getChildren().clear();
            areaContenido.getChildren().add(vista);

        } catch (IOException e) {
            System.err.println("Error al cargar la vista: " + archivoFxml);
            e.printStackTrace();
        }
    }

    // Eventos de los botones del menú lateral
    @FXML
    void mostrarFinanzas(ActionEvent event) {
        cargarVista("DashboardFinanzas.fxml");
    }

    @FXML
    void mostrarOperaciones(ActionEvent event) {
        cargarVista("DashboardOperaciones.fxml");
    }

    @FXML
    void mostrarHardware(ActionEvent event) {
        cargarVista("DashboardHardware.fxml");
    }

    @FXML
    void mostrarTendencias(ActionEvent event) {
        cargarVista("DashboardVentas.fxml");
    }
}