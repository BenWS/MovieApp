//queries to be written
//1. select to see whether username and password combo exist in database

package movie.database.app;

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
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class UserLogin {
    
    GridPane grid;
    Scene scene;
    
    Button menuButton;
    Button submit;
    
    TextField username;
    PasswordField password;
    
    public UserLogin() {
        
        grid = new GridPane();
        
        //initializing elements
        String titleString = "Welcome! Please Login";
        String usernameString = "Username";
        String passwordString = "Password";
        String buttonText = "Submit";
        
        submit = new Button(buttonText);
        
        //Text nodes
        HBox hbox = new HBox();
        Text title = new Text(titleString);
        Text usernameLabel = new Text(usernameString);
        Text passwordLabel = new Text(passwordString);
        
        //Field nodes
        username = new TextField();
        password = new PasswordField();
        
        //style
        title.setFont(Font.font(18));
        
        
        
        //adding elements to Grid
        grid.add(title,0,0,2,1);
        grid.add(usernameLabel, 0, 1);
        grid.add(username, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(password, 1, 2);
        
        //adding hbox to Grid
        hbox.getChildren().add(submit);
        grid.add(hbox, 1, 3);
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
        
        scene = new Scene(menuButtonContainer,460,270);
        

    }
    
    public void nextScreen(Menu menuInput) {
        
        //menu button returns user to menu screen
        menuButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                MovieDatabaseApp.stage.setScene(menuInput.scene);
            }
        });
        
        //submit button
        submit.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                //verify username password entered exists in database
                System.out.println("Verifying that User and Password Data exist in database");
                
                //if user exists in database
                //bring user to menu screen
                MovieDatabaseApp.stage.setScene(menuInput.scene);
                
                //clear text fields of data
                username.setText("");
                password.setText("");
            }
        });
        
        
    }
}