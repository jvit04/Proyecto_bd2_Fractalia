package utilities;
//En esta clase se guardan las rutas de archivo, usuario y contraseña de la base y el url de la base de datos.
public class Paths {
    public static final String FractaliaApp = "/main.fxml";
    public static final String ImagenIconoApp = "/imagenes/logoFractalia.png";
    public static final String UrlBaseDatos= "jdbc:postgresql://localhost:5432/FractaliaOLAP";
    //Explicación de esta parte (José Viteri):
    //Al querer ejecutar la rama de mi compañero como el tiene sus propias credenciales en la base, esto genero error
    //ya que las credenciales no son las mismas, se buscó una solución para que podamos ejecutar
    //con distintas credencias el programa.
    public static final String USER = System.getenv("DB_USER") != null
            ? System.getenv("DB_USER")
            : "postgres";


    public static final String PASSWORD = System.getenv("DB_PASSWORD") != null
            ? System.getenv("DB_PASSWORD")
            : "123456";
//Usar este comando de git: git config --global core.autocrlf true
    //si sale este mensaje: warning: in the working copy of 'src/main/resources/DashboardOperaciones.fxml', LF will be replaced by CRLF the next time Git touches it
}
