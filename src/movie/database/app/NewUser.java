package movie.database.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class NewUser {
    
    NodeFormatter nf = new NodeFormatter();
    
    GridPane grid;
    Scene scene;
    
    Button menuButton;
    Button submit;
    
    TextField reenterPassword;
    TextField password;
    TextField username;
    
    Text warning;
    
    public NewUser() {
        grid = new GridPane();
        
        String titleString = "Please Provide the Following";
        String usernameString = "Username";
        String passwordString = "Password";
        String reenterPasswordString = "Re-enter Password";
        String buttonText = "Submit";
        
        submit = new Button(buttonText);
        
        Text title = new Text(titleString);
        Text usernameLabel = new Text(usernameString);
        Text passwordLabel = new Text(passwordString);
        Text reenterPasswordLabel = new Text(reenterPasswordString);
        
        HBox hbox = new HBox();
        
        username = new TextField();
        password = new PasswordField();
        reenterPassword = new PasswordField();
        
        warning = new Text("Password Must Match");
        warning.setVisible(false);
        
        grid.add(title,0,0,2,1);
        grid.add(usernameLabel, 0, 1);
        grid.add(username, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(password, 1, 2);
        grid.add(reenterPasswordLabel,0,3);
        grid.add(reenterPassword,1,3);
        grid.add(warning, 0, 4);
        
        hbox.getChildren().add(submit);
        grid.add(hbox, 1, 4);
        hbox.setAlignment(Pos.BOTTOM_RIGHT);
        
        BorderPane menuButtonContainer = new BorderPane();
        menuButtonContainer.setPadding(new Insets(10,0,0,10));
        menuButton = new Button("Menu");
        menuButtonContainer.setCenter(this.grid);
        menuButtonContainer.setTop(menuButton);
        
        Text[] textNodes = {usernameLabel, passwordLabel, reenterPasswordLabel};
        TextField[] textFieldNodes = {username, password,reenterPassword};
        Button[] buttons = {submit, menuButton};
        RadioButton[] radioButtons = {};
        
        nf.formatNodes(
                title, 
                textNodes, 
                textFieldNodes, 
                radioButtons, 
                buttons);
        
        warning.setFill(Color.RED);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        
        scene = new Scene(menuButtonContainer,460,320);
    }
    
    public void nextScreen(Menu menuInput) {

        menuButton.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            
            @Override
            public void handle(javafx.event.ActionEvent event) {
                MovieDatabaseApp.stage.setScene(menuInput.scene);
                warning.setVisible(false);
            }
        });
        
        submit.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            
            @Override
            public void handle(javafx.event.ActionEvent event) {
                
                if (password.getText().equals(reenterPassword.getText())) {
                    
                    warning.setVisible(false);
                    
                    MovieDatabaseApp.stage.setScene(menuInput.scene);
                    
                    try {
                        updateRecord(username.getText(), password.getText());
                    } catch (SQLException ex) {
                        System.out.println(ex);
                    }
                    
                } else {warning.setVisible(true);}
                
                username.setText("");
                password.setText("");
                reenterPassword.setText("");
            }
        });
    }
    public void updateRecord(String username, String password) throws SQLException {
        
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn.getConnection();
        
        Statement stmt = null;
        String query = "INSERT INTO USERS (username,password, createDate) "
                + "VALUES('" + username + "','" + password + "',now()) ";
        
        try {
            stmt = conn.createStatement();
            stmt.execute(query);
        } catch (SQLException e ) {
            System.out.println(e);
        } finally {
            if (stmt != null) {stmt.close();}
        }
    }
}