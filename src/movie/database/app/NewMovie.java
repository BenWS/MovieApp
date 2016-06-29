package movie.database.app;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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
    
    HashMap<String,Integer> listMapping;
    TextField movieTitle;
    ListView movieTitleCombo;
       
    boolean inEditMode;
    
    
    public NewMovie() throws SQLException {
        grid = new GridPane();
        
        String titleString = "New Movie Copy";
        String newMovieRadioString = "New Movie";
        String existingMovieRadioString = "Existing Movie";
        String buttonText = "Create";
        
        create = new Button(buttonText);
        
        
        HBox hbox = new HBox();
        Text title = new Text(titleString);
        
        
        
        ToggleGroup tg = new ToggleGroup();
        newMovieRadio = new RadioButton(newMovieRadioString);
        newMovieRadio.setToggleGroup(tg);
        existingMovieRadio = new RadioButton(existingMovieRadioString);
        existingMovieRadio.setToggleGroup(tg);
        newMovieRadio.setSelected(true);
        
        //running query to obtain the values for the array list
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn.getConnection();
        Statement stmt = conn.createStatement();
        
        String getData = "SELECT movieID,title FROM movies";
        
        ResultSet rs = stmt.executeQuery(getData);
        listMapping = new HashMap<String, Integer>();
        
        while (rs.next()) {
            listMapping.put(rs.getString("title"), rs.getInt("movieID"));
        }
        
        //defining other fields
        movieTitle = new TextField();
        movieTitleCombo = new ListView(FXCollections.observableArrayList(listMapping.keySet()));
        
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
    
    public void nextScreen(Menu menuInput, NewMovieConfirmation nmcInput) {
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
                
                try {
                    updateRecords();
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                
                try {
                    displayData(nmcInput);
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                
                movieTitle.setText("");
                movieTitleCombo.scrollTo(0);

                newMovieRadio.setSelected(true);
                
                MovieDatabaseApp.stage.setScene(nmcInput.scene);
            }
        });
        
    }
    
    public void updateRecords() throws SQLException {

        if (newMovieRadio.isSelected()) {
            
            int movieID = 0;
            
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn.getConnection();
            Statement stmt = conn.createStatement();
            
            
            String movieInsert = "INSERT INTO movies (createDate, title) VALUES (now(),'" + this.movieTitle.getText() + "')";
            stmt.execute(movieInsert);
            

            String getMovieID = "SELECT movieID FROM movies WHERE title = \'" + this.movieTitle.getText() + "\'";
            ResultSet rs = stmt.executeQuery(getMovieID);

            while(rs.next()) {
                movieID = rs.getInt("movieID");
            }
            
            String movieCopyInsert = "INSERT INTO movieCopy (createDate, available, movieID) VALUES (now(), 1," + movieID + ")";
            
            stmt.execute(movieCopyInsert);
            stmt.close();
 
        } else if(existingMovieRadio.isSelected()) {
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn.getConnection();
            Statement stmt = conn.createStatement();
            
            String query = "INSERT INTO movieCopy (createDate, available, movieID) VALUES (now(), 1, " + listMapping.get(movieTitleCombo.getFocusModel().getFocusedItem()) + ")";

            stmt.execute(query);
            stmt.close();
        }
    }
    
    public void displayData(NewMovieConfirmation nmcInput) throws SQLException{
        
        //if new movie
        if (newMovieRadio.isSelected()) {
            
            //establish SQLConnection
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn.getConnection();
            Statement stmt = conn.createStatement();
            
            //get fields by movie title
            String newMovieQuery = "select title, director, releaseDate from movies where title = \'" + movieTitle.getText() + "\'";
            
            //excecute query
            ResultSet rs = stmt.executeQuery(newMovieQuery);
            //update field on NewMovieConfirmation
            while (rs.next()) {
                nmcInput.titleValue.setText(rs.getString("title"));
                nmcInput.directorValue.setText(rs.getString("director"));
                nmcInput.releaseDateValue.setText(rs.getString("releaseDate"));
            }
        } else if (existingMovieRadio.isSelected()) {
            
            //establish SQLConnection
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn.getConnection();
            Statement stmt = conn.createStatement();
            
            //get fields by movie title
            String newMovieQuery = "select title, director, releaseDate from movies where movieID = " + listMapping.get(movieTitleCombo.getFocusModel().getFocusedItem());
            
            //excecute query
            ResultSet rs = stmt.executeQuery(newMovieQuery);
            //update field on NewMovieConfirmation
            while (rs.next()) {
                nmcInput.titleValue.setText(rs.getString("title"));
                nmcInput.directorValue.setText(rs.getString("director"));
                nmcInput.releaseDateValue.setText(rs.getString("releaseDate"));
            }     
        }
    }
}