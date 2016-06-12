//database queries
//1. allow user to update movie record after clicking 'save'
//2. select from movieCopy table to see whether available is set to true or false - render 'check in/out' button accordingly
//3. 'check out' changes 'available' value from true to false in movieCopy table - same for check in
//4. select from movie table to pull in data after user searches on SearchByID screen

package movie.database.app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SearchResults {
    
    GridPane grid;
    Scene scene;
    
    Button menuButton;
    Button edit;
    Button save;
    Button checkOut;
    Button checkIn;
    Button backToSearch;
    
    TextField directorValueEdit;
    TextField releaseDateValueEdit;
    Text directorValue;
    Text releaseDateValue;
    boolean inEditMode = false;
    
    
    public SearchResults() {
        
        grid = new GridPane();
        
        Text title = new Text("Really, Really, Long Movie Title.");

        //Text Nodes
        Text director = new Text("Director:");
        Text releaseDate = new Text("Release Date:");
        Text noOfCopies = new Text("# of Copies:");
        Text movieCopyID = new Text("Movie Copy ID:");

        //Corresponding values for the above Text Nodes
        directorValue = new Text();
        releaseDateValue = new Text();
        Text noOfCopiesValue = new Text();
        Text movieCopyIDValue = new Text();
        
        //for when user desires to edit the above text nodes
        directorValueEdit = new TextField();
        releaseDateValueEdit = new TextField();


        checkOut = new Button("Check Out");
        checkIn = new Button("Check In");
        backToSearch = new Button("Back");
        edit = new Button("Edit");
        save = new Button("Save");
        

        
        //Field nodes
        TextField username = new TextField();
        PasswordField password = new PasswordField();
        PasswordField reenterPassword = new PasswordField();

        //adding elements to Grid
        grid.add(title,0,0,2,1);
        grid.add(director, 0, 1);
        grid.add(directorValue, 1, 1);
        grid.add(releaseDate, 0, 2);
        grid.add(releaseDateValue, 1, 2);
        grid.add(noOfCopies,0,3);
        grid.add(noOfCopiesValue,1,3);
        grid.add(movieCopyID,0,4);
        grid.add(movieCopyIDValue,1,4);
        grid.add(checkOut,0,5);
        grid.add(backToSearch,1,5);
        grid.add(edit,0,6);
        
        //style
        title.setFont(Font.font(14));

        //setting additional positional properties
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        
        BorderPane menuButtonContainer = new BorderPane();
        menuButtonContainer.setPadding(new Insets(10,0,0,10));
        
        menuButton = new Button("Menu");
        menuButtonContainer.setCenter(this.grid);
        menuButtonContainer.setTop(menuButton);
        
        scene = new Scene(menuButtonContainer,340,270);
    }
    
    public void nextScreen(Menu menuInput, SearchByID searchInput) {
        
        //menu button returns user to screen
        menuButton.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            
            @Override
            public void handle(javafx.event.ActionEvent event) {
                if (!inEditMode) {
                    MovieDatabaseApp.stage.setScene(menuInput.scene);
                }
            }
        });
        
        //edit button
        edit.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            
            @Override
            public void handle(javafx.event.ActionEvent event) {
                //set the string value of the text fields to text values
                directorValueEdit.setText(directorValue.getText());
                releaseDateValueEdit.setText(releaseDateValue.getText());
                
                //convert second column from text nodes to text field
                grid.getChildren().removeAll(directorValue, releaseDateValue);
                grid.add(directorValueEdit, 1, 1);
                grid.add(releaseDateValueEdit, 1, 2);
                
                //change edit button to save button
                grid.getChildren().remove(edit);
                grid.add(save, 0, 6);
                
                //setting inEditMode to true - for preventing other click methods from executing
                inEditMode = true;
                
            }
        });
        
        //save button
        save.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            
            @Override
            public void handle(javafx.event.ActionEvent event) {
                
                //persisting data to database
                System.out.println("Persisting: " + directorValueEdit.getText());
                System.out.println("Persisting: " + releaseDateValueEdit.getText());
                
                //set the string value of the text to text field values
                directorValue.setText(directorValueEdit.getText());
                releaseDateValue.setText(releaseDateValueEdit.getText());
                
                //convert second column from text field nodes to text nodes
                grid.getChildren().removeAll(directorValueEdit, releaseDateValueEdit);
                grid.add(directorValue, 1, 1);
                grid.add(releaseDateValue, 1, 2);
                
                //change edit button to save button
                grid.getChildren().remove(save);
                grid.add(edit, 0, 6);
                
                //setting inEditMode to true - for preventing other click methods from executing
                inEditMode = false;
                
            }
        });
        
        //back to search button
        backToSearch.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            
            @Override
            public void handle(javafx.event.ActionEvent event) {
                
                if (!inEditMode) {
                    //send user back to search page
                    MovieDatabaseApp.stage.setScene(searchInput.scene);
                }
                
            }
        });
        
        //check out button
        checkOut.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            
            @Override
            public void handle(javafx.event.ActionEvent event) {
                
                if (!inEditMode) {     
                    //send user back to search page
                    MovieDatabaseApp.stage.setScene(searchInput.scene);
                    
                    //updates database to make movie unavailable
                    System.out.println("Database updated");
                }
                
            }
        });
        
        //check in button
        checkIn.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            
            @Override
            public void handle(javafx.event.ActionEvent event) {
                
                if (!inEditMode) {     
                    //send user back to search page
                    MovieDatabaseApp.stage.setScene(searchInput.scene);
                    
                    //updates database to make movie unavailable
                    System.out.println("Database updated");
                }
                
            }
        });
    }
}
