package movie.database.app;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class NewMovieConfirmation {
    
    NodeFormatter nf = new NodeFormatter();
    
    GridPane grid;
    Scene scene;
    
    Text directorValue;
    Text releaseDateValue;
    Text title;
    Text movieCopyIDValue;
    Text noOfCopiesValue;
    TextField directorValueEdit;
    TextField releaseDateValueEdit;
    
    boolean inEditMode = false;
    
    Button menuButton;
    Button edit;
    Button save;
    Button backToSearch;
    
    public NewMovieConfirmation() {
        
        grid = new GridPane();
        
        title = new Text("Really, Really, Long Movie Title.");

        Text director = new Text("Director:");
        Text releaseDate = new Text("Release Date:");
        Text noOfCopies = new Text("# of Copies:");
        Text movieCopyID = new Text("Movie Copy ID:");

        directorValue = new Text();
        releaseDateValue = new Text();
        noOfCopiesValue = new Text();
        movieCopyIDValue = new Text();
        
        directorValueEdit = new TextField();
        releaseDateValueEdit = new TextField();
        
        backToSearch = new Button("Back");
        edit = new Button("Edit");
        save = new Button("Save");

        grid.add(title,0,0,2,1);
        grid.add(director, 0, 1);
        grid.add(directorValue, 1, 1);
        grid.add(releaseDate, 0, 2);
        grid.add(releaseDateValue, 1, 2);
        grid.add(noOfCopies,0,3);
        grid.add(noOfCopiesValue,1,3);
        grid.add(movieCopyID,0,4);
        grid.add(movieCopyIDValue,1,4);
        grid.add(edit,0,5);
        grid.add(backToSearch,1,5);
        
        BorderPane menuButtonContainer = new BorderPane();
        menuButtonContainer.setPadding(new Insets(10,0,0,10));
        menuButton = new Button("Menu");
        menuButtonContainer.setCenter(this.grid);
        menuButtonContainer.setTop(menuButton);
        
        Text[] textNodes = {
            director,releaseDate, noOfCopies, movieCopyID,
            directorValue, releaseDateValue,noOfCopiesValue, movieCopyIDValue
        };
        TextField[] textFieldNodes = {directorValueEdit, releaseDateValueEdit};
        Button[] buttons = {backToSearch, edit, save, menuButton};
        RadioButton[] radioButtons = {};
        
        nf.formatNodes(
                title, 
                textNodes, 
                textFieldNodes, 
                radioButtons, 
                buttons);

        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        
        scene = new Scene(menuButtonContainer,500,350);
    }
    
    public void nextScreen(Menu menuInput, NewMovie newMovie) {
        
        //menu button
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
                
                directorValueEdit.setText(directorValue.getText());
                releaseDateValueEdit.setText(releaseDateValue.getText());
                
                grid.getChildren().removeAll(directorValue, releaseDateValue);
                grid.add(directorValueEdit, 1, 1);
                grid.add(releaseDateValueEdit, 1, 2);
                
                grid.getChildren().remove(edit);
                grid.add(save, 0, 5);
                
                inEditMode = true;
                
            }
        });
        
        save.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            
            @Override
            public void handle(javafx.event.ActionEvent event) {
                
                try {
                    editRecords();
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                
                directorValue.setText(directorValueEdit.getText());
                releaseDateValue.setText(releaseDateValueEdit.getText());
                
                grid.getChildren().removeAll(directorValueEdit, releaseDateValueEdit);
                grid.add(directorValue, 1, 1);
                grid.add(releaseDateValue, 1, 2);
                
                grid.getChildren().remove(save);
                grid.add(edit, 0, 5);
                
                inEditMode = false;
                
            }
        });
        
        //back returns to new movie creation screen
        backToSearch.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            
            @Override
            public void handle(javafx.event.ActionEvent event) {
                
                if (!inEditMode) {
                    MovieDatabaseApp.stage.setScene(newMovie.scene);
                }
            }
        });
    }
    
    public void editRecords() throws SQLException {
        
        int movieID = 0;
        
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn.getConnection();
        Statement stmt = conn.createStatement();
        

        String movieIDQuery = "SELECT MOVIEID FROM MOVIECOPY WHERE MOVIECOPYID = " + movieCopyIDValue.getText();
        ResultSet movieIDResult = stmt.executeQuery(movieIDQuery);
        while(movieIDResult.next()) {
            movieID = movieIDResult.getInt("movieID");
        }
        
        String updateRecord = "UPDATE MOVIES SET DIRECTOR = '" + directorValueEdit.getText() + "' , RELEASEDATE = '" + releaseDateValueEdit.getText() + "' WHERE MOVIEID = " + movieID;
        
        stmt.execute(updateRecord);
    }
}