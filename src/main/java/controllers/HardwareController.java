package controllers;

import dao.HardwareDao;
import dao.HardwareDao.DatosHardware;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HardwareController implements Initializable {

    @FXML
    private ComboBox<String> filtroCategoria;

    @FXML
    private BarChart<Number, String> graficoHardware;
    private HardwareDao hardwareDAO = new HardwareDao();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filtroCategoria.getItems().addAll("Todas", "Soporte Técnico", "Redes y Conectividad", "Seguridad Perimetral");
        filtroCategoria.getSelectionModel().selectFirst();
        filtroCategoria.setOnAction(event -> {
            String seleccion = filtroCategoria.getValue();
            cargarDatosGrafico(seleccion);
        });
        // Carga de datos inicial
        cargarDatosGrafico("Todas");
    }

    private void cargarDatosGrafico(String categoria) {
        graficoHardware.getData().clear(); // Limpiar gráfico previo
        List<DatosHardware> datosBD = hardwareDAO.obtenerTopHardware(categoria);
        XYChart.Series<Number, String> serieHardware = new XYChart.Series<>();
        serieHardware.setName("Fallos Registrados");
        for (DatosHardware dato : datosBD) {
            serieHardware.getData().add(new XYChart.Data<>(dato.totalIncidencias, dato.nombreHardware));
        }
        graficoHardware.getData().add(serieHardware);
    }
}