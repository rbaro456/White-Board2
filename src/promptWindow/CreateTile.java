/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package promptWindow;

import backEnd.SqliteDB;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import whiteboardfx.WhiteBoardFX;

/**
 *
 * @author Robert
 */
public class CreateTile {
    
    private static TextField nameInput, imageInput;
    

    public static void display() {
        
        Stage window = new Stage();  // creates stage for pop up window
       
        window.setTitle("Create Tile");
        window.initModality(Modality.APPLICATION_MODAL);  // sets the stage as priority
        window.setMinWidth(190);                          // No other stage can be touched until window is closed   
        window.setMinHeight(240);

        Label name = new Label();  // creates label to dispaly name input
        name.setText("Name: ");

        nameInput = new TextField();  // creates text field to input name
        /*
        nameInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() ==  KeyCode.ENTER) {
                    addTile(nameInput.getText());
                }
            }
        });
        */
        
        // creates HBox that contains name label and name text field
        HBox nameField = new HBox(name,nameInput);  
        nameField.setPadding(new Insets(15,25,2,25));

        Label image = new Label();  // creates to display image input
        image.setText("Image: ");
        
        imageInput = new TextField();  // creates text field to input image name
        
        // creates HBox that contains image label and image text field
        HBox imageField = new HBox(image,imageInput);  
        imageField.setPadding(new Insets(15,25,15,25));
        
        // creates VBox that contains the name and image fields
        VBox inputFields = new VBox(nameField,imageField);  

        Button add = new Button("Add");  // button to add item to list
        add.setOnAction(e -> addTile(nameInput.getText(),imageInput.getText()));  // when pressed it adds tile to tile pane


        Button cancel = new Button("Cancel");  // button to close window
        cancel.setOnAction(e -> window.close());  // closes window when pressed


        HBox optionBar = new HBox(35,add,cancel);  // creates HBox that contains add and cancel buttons
        optionBar.setAlignment(Pos.CENTER);
        optionBar.setPadding(new Insets(15,25,15,25));

        BorderPane b = new BorderPane();
        b.setTop(inputFields);  // sets the name input field at top of window
        b.setBottom(optionBar);  // sets the add and cancel buttons at bottom of window

        Scene scene = new Scene(b);  // creates the new pop window scene
        window.setScene(scene);  // sets the scene

        window.showAndWait();  // prevents user from using any other stage
                              // until this one is closed
       
    }
    
    public static void addTile(String name, String imageName) {
        
        nameInput.clear();  // clears the text inputted by user
        imageInput.clear();  // clears the text inputted by user
        
        WhiteBoardFX wb = new WhiteBoardFX();  // creates WhiteBoardFX object
       
        imageName = imageName.trim();  
        
        if(!imageName.isEmpty()) {  // if the user inputted text
            
            SqliteDB db = new SqliteDB();  // creates new data base object
            db.insertTile(name,imageName);  // inserts tile into db
            
            Button tileBtn = wb.createTileBtn(imageName);  // creates tile button
            WhiteBoardFX.createTile(tileBtn,WhiteBoardFX.getTilePane());  // creates the tile and adds to tile pane         
            wb.createListMenu(name);  // creates list menu for new tile
           // WhiteBoardFX.getListScene().add(btn.createListMenu(name));  // crea
            
        }    
    }
}
