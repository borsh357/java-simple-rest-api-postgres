package learnjava.restapi;

//STEP 1. Import required packages
import java.sql.*;
import java.util.HashMap;

public class PostgreSQLJDBC {

    //  Database credentials
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/postgres";
    static final String USER = "postgres";
    static final String PASS = "123QWEasd";

    public static void main(String[] argv) {}

    public static Connection connect() {

            Connection dbConnection = null;

            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }

            try {
                dbConnection = DriverManager.getConnection(DB_URL, USER, PASS);
                return dbConnection;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return dbConnection;
    }
}
