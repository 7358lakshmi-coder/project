import java.sql.Connection;
import java.sql.DriverManager;

public class dbConnection {

    static String url =
        "jdbc:mysql://localhost:3306/shopping";

    static String username = "root";

    static String password = "Lakshmi@18";

    public static Connection getConnection() {

        Connection con = null;

        try {

            con = DriverManager.getConnection(
                url,
                username,
                password
            );

            System.out.println("Database Connected");

        } catch (Exception e) {

            System.out.println(e);
        }

        return con;
    }
}