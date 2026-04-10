package controllers;

import dao.OperacionesDAO;
import dao.OperacionesDAO.DatosResolucion;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OperacionesController implements Initializable {

    @FXML
    private ComboBox<String> filtroComplejidad;

    @FXML
    private BarChart<String, Number> graficoResolucion;

    private OperacionesDAO operacionesDAO = new OperacionesDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Llenar el ComboBox
        filtroComplejidad.getItems().addAll("Todas", "Baja", "Media", "Alta");
        filtroComplejidad.getSelectionModel().selectFirst();

        // 2. Acción cuando el usuario cambia el filtro
        filtroComplejidad.setOnAction(event -> {
            String seleccion = filtroComplejidad.getValue();
            cargarDatosGrafico(seleccion);
        });

        // 3. Carga inicial
        cargarDatosGrafico("Todas");
    }

    private void cargarDatosGrafico(String complejidad) {
        // Limpiamos el gráfico por si tenía datos anteriores
        graficoResolucion.getData().clear();

        // Llamamos a la BD usando el DAO
        List<DatosResolucion> datosBD = operacionesDAO.obtenerResolucionPorMes(complejidad);

        // Creamos la Serie (Categoría) para los Autómatas
        XYChart.Series<String, Number> serieBots = new XYChart.Series<>();
        serieBots.setName("Autómatas (AIOps)");

        // Creamos la Serie para los Humanos
        XYChart.Series<String, Number> serieHumanos = new XYChart.Series<>();
        serieHumanos.setName("Empleados Humanos");

        // Recorremos los datos que llegaron de la BD y los inyectamos en el gráfico
        for (DatosResolucion dato : datosBD) {
            serieBots.getData().add(new XYChart.Data<>(dato.mes, dato.resueltosBots));
            serieHumanos.getData().add(new XYChart.Data<>(dato.mes, dato.resueltosHumanos));
        }

        // Agregamos ambas series al BarChart para que se dibujen
        graficoResolucion.getData().addAll(serieBots, serieHumanos);
    }
}