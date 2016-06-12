//database queries
//1. check to see whether new movie exists already
//2. insert new movie record into database

package movie.database.app;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleGroup;

public class NewMovie {
    
    GridPane grid;
    Scene scene;
    
    Button menuButton;
    Button create;
    RadioButton newMovieRadio;
    RadioButton existingMovieRadio;
    
    TextField movieTitle;
    ListView movieTitleCombo;
    
    boolean inEditMode;
    
    
    public NewMovie() {
        grid = new GridPane();
        
        //initializing elements
        String titleString = "New Movie Copy";
        String newMovieRadioString = "New Movie";
        String existingMovieRadioString = "Existing Movie";
        String buttonText = "Create";
        
        create = new Button(buttonText);
        
        //Text nodes
        HBox hbox = new HBox();
        Text title = new Text(titleString);
        
        
        //defining radio buttons
        ToggleGroup tg = new ToggleGroup();
        newMovieRadio = new RadioButton(newMovieRadioString);
        newMovieRadio.setToggleGroup(tg);
        existingMovieRadio = new RadioButton(existingMovieRadioString);
        existingMovieRadio.setToggleGroup(tg);
        newMovieRadio.setSelected(true);
        
        //defining other fields
        movieTitle = new TextField();
        movieTitleCombo = new ListView(FXCollections.observableArrayList("First", "Second", "Third",
                "Fourth","Fifth","Sixth", "Seventh", "Eighth", "Ninth", "Tenth"));
        
        //style
        title.setFont(Font.font(18));
        
        
        
        //adding elements to Grid
        grid.add(title,0,0,2,1);
        grid.add(newMovieRadio, 0, 1);
        grid.add(existingMovieRadio, 1, 1);
        grid.add(movieTitle,0,2,2,1);
        
        //adding hbox to Grid
        hbox.getChildren().add(create);
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
    
    public void nextScreen(Menu menuInput, NewMovieConfirmation newMovieConfirmationInput) {
        //menu button returns user to menu screen
        //menu button returns user to screen
        menuButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                    MovieDatabaseApp.stage.setScene(menuInput.scene);
            }
        });
        
        newMovieRadio.setOnAction(new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent event) {
                    grid.getChildren().remove(movieTitleCombo);
                    grid.add(movieTitle, 0, 2, 2,1);
            }
        });
        
        existingMovieRadio.setOnAction(new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent event) {
                    grid.getChildren().remove(movieTitle);
                    grid.add(movieTitleCombo, 0, 2, 2,1);
            }
        });
        
        //create
        create.setOnAction(new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent event) {
                
                //pulling records from database
                System.out.println("Creating New Record and Pulling Data from Database for Confirmation page");
                
                //resetting fields
                movieTitle.setText("");
                movieTitleCombo.scrollTo(0);
                
                //setting new movie radio button to active
                newMovieRadio.setSelected(true);
                
                //changing scene to new movie confirmation
                MovieDatabaseApp.stage.setScene(newMovieConfirmationInput.scene);
            }
        });
        
    }
}