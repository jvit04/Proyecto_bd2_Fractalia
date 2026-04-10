package dao;

import utilities.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HardwareDao {
    public static class DatosHardware {
        public String nombreHardware;
        public int totalIncidencias;
        public DatosHardware(String nombreHardware, int totalIncidencias) {
            this.nombreHardware = nombreHardware;
            this.totalIncidencias = totalIncidencias;
        }
    }

    public List<DatosHardware> obtenerTopHardware(String categoriaFiltro) {
        List<DatosHardware> listaDatos = new ArrayList<>();
        String sql = "SELECT * FROM fn_top_hardware_incidencias(?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categoriaFiltro);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String hardware = rs.getString("hardware");
                int total = rs.getInt("total");
                listaDatos.add(new DatosHardware(hardware, total));
            }
        } catch (Exception e) {
            System.err.println("Error en HardwareDAO:");
            e.printStackTrace();
        }
        return listaDatos;
    }
}