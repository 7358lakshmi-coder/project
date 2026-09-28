import java.sql.Connection;
 import java.sql.DriverManager;
  public class dbConnection {
     public static Connection getConnection() {
         Connection con = null;
          try {
             String url = "jdbc:mysql://localhost:3306/library"; 
             String username = "root"; 
             
            String password = "Lakshmi@18"; 
            con = DriverManager.getConnection(url, username, password);
             System.out.println("Database Connected Successfully!");
             } catch (Exception e) { 
                System.out.println("Database Connection Failed!"); 
                e.printStackTrace();
             } return con;
 } }