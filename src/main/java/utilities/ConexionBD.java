package utilities;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//Metodo que facilita la conexión a la base de datos.
public class ConexionBD {
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(Paths.UrlBaseDatos, Paths.USER, Paths.PASSWORD);
    }
}