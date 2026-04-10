package dao;

import utilities.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FinanzasDAO {
    public static class DatosFinanzas {
        public String tipoCliente;
        public String pais;
        public double ingresosGenerados;

        public DatosFinanzas(String tipoCliente, String pais, double ingresosGenerados) {
            this.tipoCliente = tipoCliente;
            this.pais = pais;
            this.ingresosGenerados = ingresosGenerados;
        }
    }

    public List<DatosFinanzas> obtenerIngresosPorFechas(java.sql.Date fechaInicio, java.sql.Date fechaFin) {
        List<DatosFinanzas> listaDatos = new ArrayList<>();
        String sql = "SELECT * FROM fn_ingresos_tipo_pais(?, ?)";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (fechaInicio != null) {
                pstmt.setDate(1, fechaInicio);
            } else {
                pstmt.setNull(1, java.sql.Types.DATE);
            }

            if (fechaFin != null) {
                pstmt.setDate(2, fechaFin);
            } else {
                pstmt.setNull(2, java.sql.Types.DATE);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String tipo = rs.getString("tipo_cliente");
                String pais = rs.getString("pais");
                double ingresos = rs.getDouble("ingresos");

                listaDatos.add(new DatosFinanzas(tipo, pais, ingresos));
            }
        } catch (Exception e) {
            System.err.println("Error en FinanzasDAO:");
            e.printStackTrace();
        }
        return listaDatos;
    }
}