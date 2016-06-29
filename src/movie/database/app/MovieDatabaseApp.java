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
        
        stage.setScene(ul.scene);
        stage.show();
        
        boolean isUser;
        
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn.getConnection();
        Statement stmt = conn.createStatement();
        
        String validationQuery = "SELECT * FROM USERS WHERE USERNAME = '" + "BenS" + "' AND PASSWORD = " + "'" + "thisismypassword1" + "'";
        ResultSet validationResults = stmt.executeQuery(validationQuery);
        
        if (validationResults.first()) {
            isUser = true;
        } else {
            isUser = false;
        };

        stmt.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}

class DatabaseConnection {
 
    String userName = "java";
    String password = "";
    String dbms = "mysql";
    String serverName = "localhost";
    String portNumber = "3306";
    String dbName;
    
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


