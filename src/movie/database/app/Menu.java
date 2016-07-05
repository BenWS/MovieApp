package movie.database.app;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Menu {
    

    Scene scene;
    GridPane grid;
    
    Button searchButton;
    Button newRecordButton;
    Button newUserButton;
        
    public Menu() {
        
        grid = new GridPane();
            
        String titleString = "Menu";
        String searchButtonText = "Search Movies";
        String newRecordButtonText = "New Record";
        String newUserButtonText = "New User";
        
        //button Nodes
        searchButton = new Button(searchButtonText);
        newRecordButton = new Button(newRecordButtonText);
        newUserButton = new Button(newUserButtonText);
        
        Text title = new Text(titleString);
        
        title.setFont(Font.font(18));
        
        grid.add(title,0,0,2,1);
        grid.add(searchButton, 0, 1);
        grid.add(newRecordButton, 1, 1);
        grid.add(newUserButton, 0, 2);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        
        scene = new Scene(this.grid,320,320);
        
    }
    
    public void nextScreen(SearchByID searchByIDInput, NewMovie newMovieInput, NewUser newUserInput) {
        
        
        this.searchButton.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            
            @Override
            public void handle(javafx.event.ActionEvent event) {
                MovieDatabaseApp.stage.setScene(searchByIDInput.scene);
            }
        });
        
        this.newRecordButton.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            
            @Override
            public void handle(javafx.event.ActionEvent event) {
                MovieDatabaseApp.stage.setScene(newMovieInput.scene);
            }
        });
        
        this.newUserButton.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            
            @Override
            public void handle(javafx.event.ActionEvent event) {
                MovieDatabaseApp.stage.setScene(newUserInput.scene);
            }
        });
        
    }
}