//database queries


package movie.database.app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class SearchByID {
    
    Scene scene;
    GridPane grid;
    
    Button menuButton;
    Button submitButton;
    
    TextField input_1;
    
    
    public SearchByID() {
        
        //initializing grid
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        //text strings for field labels
        String inputTitleString = "Please Enter Movie ID";
        String buttonText = "Search";

        //text field nodes
        input_1 = new TextField();

        //button node
        submitButton = new Button(buttonText);

        //text nodes
        Text inputTitle = new Text(inputTitleString);

        //adding nodes to inputGrid
        grid.add(inputTitle, 0, 0, 2, 1);
        grid.add(input_1, 0, 1);
        grid.add(submitButton, 1, 1);

        //setting additional positional properties
        grid.setHgap(10);
        grid.setVgap(10);

        //styling
        inputTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));
        inputTitle.setFill(Color.ORANGE);
        
        BorderPane menuButtonContainer = new BorderPane();
        menuButtonContainer.setPadding(new Insets(10,0,0,10));
        
        menuButton = new Button("Menu");
        menuButtonContainer.setCenter(this.grid);
        menuButtonContainer.setTop(menuButton);
        
        scene = new Scene(menuButtonContainer,460,220);
    
    }
    
    public void nextScreen (Menu menuInput, SearchResults searchResultsInput) {
        
        //menu button returns user to screen
        menuButton.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            
            @Override
            public void handle(javafx.event.ActionEvent event) {
                MovieDatabaseApp.stage.setScene(menuInput.scene);
            }
        });

        //submit button
        
        submitButton.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            
            @Override
            public void handle(javafx.event.ActionEvent event) {
                
                //populate the search results page w/ data from the database
                System.out.println("Pulling Results from Database");
                
                //setting 'check in' or 'check out' as the current button on search results
                System.out.println("Checking whether copy is in stock or out of stock");
                
                //send user to search results screen
                MovieDatabaseApp.stage.setScene(searchResultsInput.scene);
                
                //clear text field from this page
                input_1.setText("");
                
            }
        });
    }
}
