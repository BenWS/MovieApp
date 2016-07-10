package movie.database.app;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
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
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleGroup;

public class NewMovie {
    
    NodeFormatter nf = new NodeFormatter();
    
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
        
        Text title = new Text(titleString);
        create = new Button(buttonText);
        
        ToggleGroup tg = new ToggleGroup();
        newMovieRadio = new RadioButton(newMovieRadioString);
        existingMovieRadio = new RadioButton(existingMovieRadioString);
        existingMovieRadio.setToggleGroup(tg);
        newMovieRadio.setToggleGroup(tg);
        newMovieRadio.setSelected(true);
        
        HBox hbox = new HBox();
        
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn.getConnection();
        Statement stmt = conn.createStatement();
        String getData = "SELECT movieID,title FROM movies";
        ResultSet rs = stmt.executeQuery(getData);
        listMapping = new HashMap<String, Integer>();
        while (rs.next()) {
            listMapping.put(rs.getString("title"), rs.getInt("movieID"));
        }
        
        movieTitle = new TextField();
        movieTitleCombo = new ListView(FXCollections.observableArrayList(listMapping.keySet()));
        movieTitleCombo.setMaxHeight(200);

        grid.add(title,0,0,2,1);
        grid.add(newMovieRadio, 0, 1);
        grid.add(existingMovieRadio, 1, 1);
        grid.add(movieTitle,0,2,2,1);
        
        hbox.getChildren().add(create);
        grid.add(hbox, 1, 3);
        hbox.setAlignment(Pos.BOTTOM_RIGHT);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        
        BorderPane menuButtonContainer = new BorderPane();
        menuButtonContainer.setPadding(new Insets(10,0,0,10));
        menuButton = new Button("Menu");
        menuButtonContainer.setCenter(this.grid);
        menuButtonContainer.setTop(menuButton);
        
        Text[] textNodes = {};
        TextField[] textFieldNodes = {};
        Button[] buttons = {menuButton, create};
        RadioButton[] radioButtons = {existingMovieRadio, newMovieRadio};
        
        nf.formatNodes(
                title, 
                textNodes, 
                textFieldNodes, 
                radioButtons, 
                buttons);
        

        
        scene = new Scene(menuButtonContainer,460,375);
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
        
        create.setOnAction(new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent event) {
                
                int movieCopyID = 0;
                
                try {
                    movieCopyID = updateRecords();
                    displayData(nmcInput, movieCopyID);
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                
                movieTitle.setText("");
                movieTitleCombo.scrollTo(0);                   
                
                MovieDatabaseApp.stage.setScene(nmcInput.scene);
                
                if (existingMovieRadio.isSelected()) {
                    grid.getChildren().remove(movieTitleCombo);
                    grid.add(movieTitle, 0, 2, 2,1);
                    newMovieRadio.setSelected(true);
                }
            }
        });
        
    }
    
    public int updateRecords() throws SQLException {

        int movieCopyID = 0;
        
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
            stmt.execute(movieCopyInsert, RETURN_GENERATED_KEYS);
            ResultSet movieCopyIDResult = stmt.getGeneratedKeys();
            while(movieCopyIDResult.next()) {
                movieCopyID = movieCopyIDResult.getInt(1);
            }
            
            stmt.close();
 
        } else if(existingMovieRadio.isSelected()) {
            
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn.getConnection();
            Statement stmt = conn.createStatement();
            
            String query = "INSERT INTO movieCopy (createDate, available, movieID) VALUES (now(), 1, " + listMapping.get(movieTitleCombo.getFocusModel().getFocusedItem()) + ")";
            stmt.execute(query, RETURN_GENERATED_KEYS);
            ResultSet movieCopyIDResult = stmt.getGeneratedKeys();
            while(movieCopyIDResult.next()) {
                movieCopyID = movieCopyIDResult.getInt(1);
            }                       
            
            stmt.close();
        }
        return movieCopyID;
    }

    public void displayData(NewMovieConfirmation nmcInput, int movieCopyIDInput) throws SQLException{

        if (newMovieRadio.isSelected()) {
            
            int movieID = 0;
            int count = 0;
            
            nmcInput.movieCopyIDValue.setText(String.valueOf(movieCopyIDInput));
            
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn.getConnection();
            Statement stmt = conn.createStatement();

            String getMovieID = "SELECT movieID FROM movies WHERE title = \'" + this.movieTitle.getText() + "\'";
            ResultSet movieIDResults = stmt.executeQuery(getMovieID);
            while(movieIDResults.next()) {
                movieID = movieIDResults.getInt("movieID");
            }
            
            String movieCountQuery = "SELECT COUNT(*) AS COUNT FROM MOVIECOPY WHERE MOVIEID = " + movieID;
            ResultSet countResults = stmt.executeQuery(movieCountQuery);
            while(countResults.next()) {
                nmcInput.noOfCopiesValue.setText(String.valueOf(countResults.getInt("COUNT")));
            }
            
            String newMovieQuery = "select title, director, releaseDate from movies where title = '" + movieTitle.getText() + "'";
            ResultSet newMovieQueryResults = stmt.executeQuery(newMovieQuery);
            while (newMovieQueryResults.next()) {
                nmcInput.title.setText(newMovieQueryResults.getString("title"));
                nmcInput.directorValue.setText(newMovieQueryResults.getString("director"));
                nmcInput.releaseDateValue.setText(newMovieQueryResults.getString("releaseDate"));
            }
            
        } else if (existingMovieRadio.isSelected()) {
            
            int movieID = 0;
            
            nmcInput.movieCopyIDValue.setText(String.valueOf(movieCopyIDInput));
            
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn.getConnection();
            Statement stmt = conn.createStatement();
            
            String movieCountQuery = "SELECT COUNT(*) AS COUNT FROM MOVIECOPY WHERE MOVIEID = " + listMapping.get(movieTitleCombo.getFocusModel().getFocusedItem());
            ResultSet countResults = stmt.executeQuery(movieCountQuery);
            while(countResults.next()) {
                nmcInput.noOfCopiesValue.setText(String.valueOf(countResults.getInt("COUNT")));
            }

            String newMovieQuery = "select title, director, releaseDate from movies where movieID = " + listMapping.get(movieTitleCombo.getFocusModel().getFocusedItem());
            ResultSet rs = stmt.executeQuery(newMovieQuery);
            while (rs.next()) {
                nmcInput.title.setText(rs.getString("title"));
                nmcInput.directorValue.setText(rs.getString("director"));
                nmcInput.releaseDateValue.setText(rs.getString("releaseDate"));
            }     
        }
    }
}