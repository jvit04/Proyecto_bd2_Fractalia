package controllers;

import dao.VentasDao;
import dao.VentasDao.DatosVentas;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class VentasController implements Initializable {

    @FXML
    private LineChart<String, Number> graficoVentas;

    private VentasDao ventasDAO = new VentasDao();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatosGrafico();
    }

    private void cargarDatosGrafico() {
        graficoVentas.getData().clear(); // Limpiar gráfico previo

        List<DatosVentas> datosBD = ventasDAO.obtenerResumenVentas();
        XYChart.Series<String, Number> serieVentas = new XYChart.Series<>();
        serieVentas.setName("Tendencia de Ingresos");

        for (DatosVentas dato : datosBD) {
            // Unimos mes y año para la etiqueta del eje X (Ej: "1/2026")
            String etiquetaFecha = dato.mes + "/" + dato.anio;
            serieVentas.getData().add(new XYChart.Data<>(etiquetaFecha, dato.totalVentas));
        }

        graficoVentas.getData().add(serieVentas);
    }
}