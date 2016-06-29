package movie.database.app;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    
    Text warning;
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
        warning = new Text("Record Does Not Exist");
        warning.setVisible(false);

        //adding nodes to inputGrid
        grid.add(inputTitle, 0, 0, 2, 1);
        grid.add(input_1, 0, 1);
        grid.add(warning, 1, 1);
        grid.add(submitButton, 1, 1);

        //setting additional positional properties
        grid.setHgap(10);
        grid.setVgap(10);

        //styling
        inputTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));
        inputTitle.setFill(Color.ORANGE);
        warning.setFill(Color.RED);
        
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
                
                warning.setVisible(false);
                
                //create db connection
                try {
                    DatabaseConnection dbConn = new DatabaseConnection();
                    Connection conn = dbConn.getConnection();
                    Statement stmt = conn.createStatement();

                    String movieDataQuery = "select movieID, available from movieApp.movieCopy where movieCopyID = " + input_1.getText();

                    ResultSet movieData = stmt.executeQuery(movieDataQuery);

                    if (movieData.first()) {
                        queryDB(searchResultsInput);
                        MovieDatabaseApp.stage.setScene(searchResultsInput.scene);
                    } else {
                        warning.setVisible(true);
                    }



                    //clear text field for next use of page
                    input_1.setText("");
                
                } catch (SQLException E) {System.out.println(E);}
                
            }
        });
    }
    
    //make updates to SearchResults Page after searching ID
    public void queryDB (SearchResults searchResults) throws SQLException {
        
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn.getConnection();
        Statement stmt = conn.createStatement();
        
        int movieID = 0;
        int movieCopyCount = 0;
        int available = 0;
        String director = "";
        String title = "";
        String releaseDate = "";

        
        int movieCopyID = Integer.parseInt(input_1.getText());

        String movieIDQuery = "select movieID, available from movieApp.movieCopy where movieCopyID = " + movieCopyID;
        String movieDataQuery = "select title, director, releaseDate from movieApp.movies ";
        String movieCopyCountQuery = "select count(*) AS Count from movieApp.movieCopy WHERE available = 1 AND movieID = " + movieID;
        
        ResultSet movieIDResults = stmt.executeQuery(movieIDQuery);
        
        while(movieIDResults.next()) {
            movieID = movieIDResults.getInt("movieID");
            available = movieIDResults.getInt("available");
        }
        
        ResultSet movieDataResults = stmt.executeQuery(movieDataQuery);
        
        while(movieDataResults.next()) {
            director = movieDataResults.getString("director");
            title = movieDataResults.getString("title");
            releaseDate = movieDataResults.getString("releaseDate");
        }
        
        ResultSet copyCountResults = stmt.executeQuery(movieCopyCountQuery);
        
        while(copyCountResults.next()) {
            movieCopyCount = copyCountResults.getInt("Count");
        }
        
        stmt.close();
        
        searchResults.titleValue.setText(title);
        searchResults.directorValue.setText(director);
        searchResults.releaseDateValue.setText(releaseDate);
        searchResults.noOfCopiesValue.setText(String.valueOf(movieCopyCount));
        searchResults.movieCopyIDValue.setText(String.valueOf(movieCopyID));
       
        if (available == 1) {
            searchResults.checkInOut.setText("Check Out");
        } else if (available == 0) {
            searchResults.checkInOut.setText("Check In");
        }
        
    }
}
