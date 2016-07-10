package movie.database.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class MovieDatabaseApp extends Application {
    
    public static Stage stage = new Stage();
    
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

class NodeFormatter {
    public void formatNodes(
            Text titleInput, 
            Text[] textNodeInput, 
            TextField[] textFieldInput,
            RadioButton[] radioButtonInput,
            Button[] buttonInput) {
        
        titleInput.setFont(Font.font(18));
        titleInput.setFill(Color.ORANGERED);
        
        for (int i =0;i < textNodeInput.length;i++) {
            textNodeInput[i].setFont(Font.font(14));
            textNodeInput[i].setFill(Color.BLACK);
        }
        
        for (int i =0;i < textFieldInput.length;i++) {
            textFieldInput[i].setFont(Font.font(14));
        }
        
        for (int i =0;i < buttonInput.length;i++) {
            buttonInput[i].setFont(Font.font(14));
        }
        
    }
}