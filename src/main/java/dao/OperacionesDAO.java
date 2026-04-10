package dao;

import utilities.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OperacionesDAO {

    public static class DatosResolucion {
        public String mes;
        public int resueltosBots;
        public int resueltosHumanos;

        public DatosResolucion(String mes, int resueltosBots, int resueltosHumanos) {
            this.mes = mes;
            this.resueltosBots = resueltosBots;
            this.resueltosHumanos = resueltosHumanos;
        }
    }

    public List<DatosResolucion> obtenerResolucionPorMes(String complejidadFiltro) {
        List<DatosResolucion> listaDatos = new ArrayList<>();

        String sql = "SELECT * FROM fn_obtener_resolucion_aiops(?)";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Le pasamos el filtro (ej. "Todas", "Alta", "Baja") directamente a la función
            pstmt.setString(1, complejidadFiltro);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String mes = "Mes " + rs.getInt("mes_num");
                int bots = rs.getInt("bots");
                int humanos = rs.getInt("humanos");

                listaDatos.add(new DatosResolucion(mes, bots, humanos));
            }

        } catch (Exception e) {
            System.err.println("Error ejecutando la función en OperacionesDAO:");
            e.printStackTrace();
        }

        return listaDatos;
    }
}