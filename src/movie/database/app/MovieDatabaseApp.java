package movie.database.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javafx.application.Application;
import javafx.stage.Stage;


public class MovieDatabaseApp extends Application {
    
    public static Stage stage = new Stage();
    
    //event handlers for menu page
    @Override
    public void start(Stage primaryStage) throws SQLException {
        
        Menu menu = new Menu();
        NewMovie nm = new NewMovie();
        NewMovieConfirmation nmc = new NewMovieConfirmation();
        NewUser nu = new NewUser();
        SearchByID sbid = new SearchByID();
        SearchResults sr = new SearchResults();
        UserLogin ul = new UserLogin();
        
        menu.nextScreen(sbid, nm, nu);
        nm.nextScreen(menu,nmc);
        nmc.nextScreen(menu, nm);
        nu.nextScreen(menu);
        sbid.nextScreen(menu, sr);
        sr.nextScreen(menu, sbid);
        ul.nextScreen(menu);
        
        
        stage.setScene(menu.scene);
        stage.show();
        
        String myUserName = "java";
        String myPassword = "";
        String myDBMS = "mysql";
        String myServerName = "localhost";
        String myPortNumber = "3306";
        String dbName = "myDatabase";
        
        DatabaseConnection dbConn = new DatabaseConnection(myUserName, myPassword, myDBMS,myServerName,myPortNumber,dbName);
        
        Connection conn = dbConn.getConnection();
        
        Statement stmt = null;
        String query = "select * from movies order by year limit 10 ";
        String result = null;
        
        try {
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String title = rs.getString(1);
            int year = rs.getInt(2);
            System.out.println(title + "\t\t\t\t\t" + year);
        }
    } catch (SQLException e ) {
        System.out.println(e);
    } finally {
        if (stmt != null) { stmt.close(); }
    }
    
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}

class DatabaseConnection {
 
    String userName;
    String password;
    String dbms;
    String serverName;
    String portNumber;
    String dbName;
    
    public DatabaseConnection(
            String userNameInput,
            String passwordInput,
            String dbmsInput, 
            String serverNameInput, 
            String portNumberInput, 
            String dbNameInput) {
        
        dbms = dbmsInput;
        serverName = serverNameInput;
        portNumber = portNumberInput;
        dbName = dbNameInput;
        userName = userNameInput;
        password = passwordInput;
        
    }
    
    public Connection getConnection() throws SQLException {
        
        Connection conn = null;
        Properties connectionProps = new Properties();

        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);

        if (this.dbms.equals("mysql")) {
            conn = DriverManager.getConnection(
                       "jdbc:" + this.dbms + "://" +
                       this.serverName +
                       ":" + this.portNumber + "/" + "movieApp",
                       connectionProps);
        } else if (this.dbms.equals("derby")) {
            conn = DriverManager.getConnection(
                       "jdbc:" + this.dbms + ":" +
                       this.dbName +
                       ";create=true",
                       connectionProps);
        }
        
        System.out.println("Connected to database");
        return conn;
    }
}


