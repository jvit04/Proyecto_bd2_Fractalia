package dao;

import utilities.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VentasDao {


    public static class DatosVentas {
        public int anio;
        public int mes;
        public double totalVentas;
        public long totalContratos;

        public DatosVentas(int anio, int mes, double totalVentas, long totalContratos) {
            this.anio = anio;
            this.mes = mes;
            this.totalVentas = totalVentas;
            this.totalContratos = totalContratos;
        }
    }

    public List<DatosVentas> obtenerResumenVentas() {
        List<DatosVentas> listaDatos = new ArrayList<>();
        // Llamada a tu función
        String sql = "SELECT * FROM public.obtener_resumen_ventas()";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int anio = rs.getInt("anio");
                int mes = rs.getInt("mes");
                double total = rs.getDouble("total_ventas");
                long contratos = rs.getLong("total_contratos");

                listaDatos.add(new DatosVentas(anio, mes, total, contratos));
            }
        } catch (Exception e) {
            System.err.println("Error en VentasDao:");
            e.printStackTrace();
        }
        return listaDatos;
    }
}