//database queries for class
//1. insert new user record - DONE
//2. check database to check whether user currently exists

package movie.database.app;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class NewUser {
    
    GridPane grid;
    Scene scene;
    
    Button menuButton;
    Button submit;
    
    TextField reenterPassword;
    TextField password;
    TextField username;
    
    public NewUser() {
        grid = new GridPane();
        
        //initializing elements
        String titleString = "Please Provide the Following";
        String usernameString = "Username";
        String passwordString = "Password";
        String reenterPasswordString = "Re-enter Password";
        String buttonText = "Submit";
        
        submit = new Button(buttonText);
        
        //Text nodes
        HBox hbox = new HBox();
        Text title = new Text(titleString);
        Text usernameLabel = new Text(usernameString);
        Text passwordLabel = new Text(passwordString);
        Text reenterPasswordLabel = new Text(reenterPasswordString);
        
        //Field nodes
        username = new TextField();
        password = new PasswordField();
        reenterPassword = new PasswordField();
        
        //style
        title.setFont(Font.font(18));
        
        
        
        //adding elements to Grid
        grid.add(title,0,0,2,1);
        grid.add(usernameLabel, 0, 1);
        grid.add(username, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(password, 1, 2);
        grid.add(reenterPasswordLabel,0,3);
        grid.add(reenterPassword,1,3);
        
        //adding hbox to Grid
        hbox.getChildren().add(submit);
        grid.add(hbox, 1, 4);
        hbox.setAlignment(Pos.BOTTOM_RIGHT);

        //setting additional positional properties
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        
        BorderPane menuButtonContainer = new BorderPane();
        menuButtonContainer.setPadding(new Insets(10,0,0,10));
        
        menuButton = new Button("Menu");
        menuButtonContainer.setCenter(this.grid);
        menuButtonContainer.setTop(menuButton);
        
        scene = new Scene(menuButtonContainer,460,320);
    }
    
    public void nextScreen(Menu menuInput) {
        //menu button takes user back to menu
        //menu button
        menuButton.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            
            @Override
            public void handle(javafx.event.ActionEvent event) {
                MovieDatabaseApp.stage.setScene(menuInput.scene);
            }
        });
        
        //submit button
        submit.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            
            @Override
            public void handle(javafx.event.ActionEvent event) {
                
                //verify passwords match
                if (password.getText().equals(reenterPassword.getText())) {
                    
                    //Returns user to menu
                    MovieDatabaseApp.stage.setScene(menuInput.scene);
                    
                    //create new user in database
                    System.out.println("Creating new user in database");
                    try {
                        updateRecord(username.getText(), password.getText());
                    } catch (SQLException ex) {
                        Logger.getLogger(NewUser.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }
    public void updateRecord(String username, String password) throws SQLException {
        
        String myUserName = "java";
        String myPassword = "";
        String myDBMS = "mysql";
        String myServerName = "localhost";
        String myPortNumber = "3306";
        String dbName = "myDatabase";
        boolean result;
        
        DatabaseConnection dbConn = new DatabaseConnection(myUserName, myPassword, myDBMS,myServerName,myPortNumber,dbName);
        Connection conn = dbConn.getConnection();
        
        Statement stmt = null;
        String query = "INSERT INTO USERS (username,password, createDate) "
                + "VALUES('" + username + "','" + password + "',now()) ";
        System.out.println(query);
        
        try {
            stmt = conn.createStatement();
            result  = stmt.execute(query);        } catch (SQLException e ) {
            System.out.println(e);
        } finally {
            if (stmt != null) { stmt.close(); }
        }
    }
}