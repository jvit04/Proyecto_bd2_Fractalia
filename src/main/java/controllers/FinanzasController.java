package controllers;
import java.time.LocalDate;
import javafx.scene.control.DateCell;
import javafx.util.Callback;
import dao.FinanzasDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Comparator;

public class FinanzasController implements Initializable {

    @FXML private DatePicker fechaInicio;
    @FXML private DatePicker fechaFin;
    @FXML private PieChart graficoIngresos;
    @FXML private GridPane leyendaCustom;

    private FinanzasDAO finanzasDAO = new FinanzasDAO();
    private final String[] COLORES_JAVAFX = {
            "#f3622d", "#fba71b", "#57b757", "#41a9c9", "#4258c9", "#9a42c8"
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configuramos los límites del calendario primero
        configurarCalendarios();
        cargarDatosGrafico();

        fechaInicio.valueProperty().addListener((obs, oldV, newV) -> cargarDatosGrafico());
        fechaFin.valueProperty().addListener((obs, oldV, newV) -> cargarDatosGrafico());
    }
    private void configurarCalendarios() {
     //los límites históricos del Data Warehouse
        LocalDate fechaMinima = LocalDate.of(2023, 1, 1);
        LocalDate fechaMaxima = LocalDate.of(2026, 12, 31);

        // fábrica de celdas que evaluará cada día en el calendario
        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                // Si el día a pintar está fuera de nuestro rango, lo deshabilitamos
                if (item.isBefore(fechaMinima) || item.isAfter(fechaMaxima)) {
                    setDisable(true);
                    // Le damos un aspecto de "deshabilitado" (gris claro)
                    setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #b0b0b0;");
                }
            }
        };

        // 3. Aplicamos esta regla a ambos DatePickers
        fechaInicio.setDayCellFactory(dayCellFactory);
        fechaFin.setDayCellFactory(dayCellFactory);
    }
    private void cargarDatosGrafico() {
        graficoIngresos.getData().clear();
        leyendaCustom.getChildren().clear();

        java.sql.Date sqlInicio = (fechaInicio.getValue() != null) ? java.sql.Date.valueOf(fechaInicio.getValue()) : null;
        java.sql.Date sqlFin = (fechaFin.getValue() != null) ? java.sql.Date.valueOf(fechaFin.getValue()) : null;
        List<FinanzasDAO.DatosFinanzas> datosBD = finanzasDAO.obtenerIngresosPorFechas(sqlInicio, sqlFin);

        if (datosBD == null || datosBD.isEmpty()) {
            graficoIngresos.setTitle("No hay ingresos registrados en estas fechas");
            return;
        } else {
            graficoIngresos.setTitle(""); // Borramos el título si sí hay datos
        }
        double totalIngresos = datosBD.stream().mapToDouble(d -> d.ingresosGenerados).sum();
        datosBD.sort(Comparator.comparingDouble((FinanzasDAO.DatosFinanzas d) -> d.ingresosGenerados).reversed());
        double sumaOtros = 0;
        int numItem = 0;

        for (int i = 0; i < datosBD.size(); i++) {
            FinanzasDAO.DatosFinanzas d = datosBD.get(i);
            if (i < 5) {
                double porcentaje = (d.ingresosGenerados / totalIngresos) * 100;
                graficoIngresos.getData().add(new PieChart.Data(String.format("%.1f%%", porcentaje), d.ingresosGenerados));
                String textoCompleto = String.format("%s (%s) - %s", d.tipoCliente, d.pais, formatearDinero(d.ingresosGenerados));
                agregarItemLeyenda(textoCompleto, numItem);
                numItem++;
            } else {
                sumaOtros += d.ingresosGenerados;
            }
        }

        if (sumaOtros > 0) {
            double porcentajeOtros = (sumaOtros / totalIngresos) * 100;
            graficoIngresos.getData().add(new PieChart.Data(String.format("%.1f%%", porcentajeOtros), sumaOtros));

            agregarItemLeyenda(String.format("Otros destinos - %s", formatearDinero(sumaOtros)), numItem);
        }

        Platform.runLater(() -> {
            int indexColor = 0;
            for (PieChart.Data data : graficoIngresos.getData()) {
                if (data.getNode() != null) {
                    String colorHex = COLORES_JAVAFX[indexColor % COLORES_JAVAFX.length];
                    data.getNode().setStyle("-fx-pie-color: " + colorHex + ";");
                }
                indexColor++;
            }
        });
    }

    private String formatearDinero(double monto) {
        if (monto >= 1_000_000) {
            return String.format("$%.2fM", monto / 1_000_000.0);
        } else if (monto >= 1_000) {
            return String.format("$%.2fK", monto / 1_000.0);
        } else {
            return String.format("$%,.2f", monto);
        }
    }

    private void agregarItemLeyenda(String texto, int index) {
        Circle colorCirculo = new Circle(5, Color.web(COLORES_JAVAFX[index % COLORES_JAVAFX.length]));
        colorCirculo.setTranslateY(2.5);
        Label lblTexto = new Label(texto);
        lblTexto.setStyle("-fx-font-size: 11px; -fx-text-fill: #333333;");
        lblTexto.setWrapText(true);
        lblTexto.setMaxWidth(185);

        HBox item = new HBox(6, colorCirculo, lblTexto);
        item.setAlignment(Pos.TOP_LEFT);

        leyendaCustom.add(item, index % 2, index / 2);
    }
}