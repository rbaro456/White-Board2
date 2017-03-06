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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import whiteboardfx.WhiteBoardFX;

/**
 *
 * @author Robert
 */
public class AddItem {
    
    private static TextField nameInput;
   
    public static void display() {
       
        Stage window = new Stage();  // creates stage for pop up window
       
        window.setTitle("Add Item");  
        window.initModality(Modality.APPLICATION_MODAL);  // sets the stage as priority
        window.setMinWidth(190);                          // No other stage can be touched until window is closed   
        window.setMinHeight(240);
       
        Label name = new Label(); // creates label to dispaly name input
        name.setText("Name: ");
       
        nameInput = new TextField();  // creates text field to input name
        nameInput.setOnKeyPressed(new EventHandler<KeyEvent>() {  // if enter key is pressed; name is inputted
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() ==  KeyCode.ENTER) {
                    addItem(nameInput.getText(),WhiteBoardFX.getCurrList());  // adds item to list
                }
            }
        });
       
        HBox nameField = new HBox(8,name,nameInput); // creates HBox that contains name label and name input
        nameField.setPadding(new Insets(15,25,15,25));
       
       
       
        Button add = new Button("Add");  // button to add item to list
        add.setOnAction(e -> addItem(nameInput.getText(),WhiteBoardFX.getCurrList()));  // when pressed it adds item to list
       
       
        Button cancel = new Button("Cancel");  // button to close window
        cancel.setOnAction(e -> window.close());  // closes window when pressed
       
       
        HBox optionBar = new HBox(35,add,cancel);  // creates HBox that contains add and cancel buttons
        optionBar.setAlignment(Pos.CENTER);
        optionBar.setPadding(new Insets(15,25,15,25));
       
        BorderPane b = new BorderPane();
        b.setTop(nameField);  // sets the name input field at top of window
        b.setBottom(optionBar);  // sets the add and cancel buttons at bottom of window
       
        Scene scene = new Scene(b);  // creates the new pop window scene
        window.setScene(scene);  // sets the scene
       
        window.showAndWait();  // prevents user from using any other stage
                              // until this one is closed  
  
    }
    
  
    
    public static void addItem(String name, int currList) {
        
        nameInput.clear();  // clears the text inputted by user
        name = name.trim();
        
        if(!name.isEmpty()) {  // if the user inputted text
            
            SqliteDB db = new SqliteDB();  // creates new data base object
            db.insertItem(currList+1, name);  // inserts item name into data base
                                              // currList+1 because database starts with 1; not 0
                                              
            Text nameText = new Text(name);  // creates text object from user input
            nameText.setFont(new Font("Arial", 50));
            nameText.setFill(Color.BLACK);

            BorderPane listItem = new BorderPane();  // creates border pane that contains list item
            listItem.setLeft(nameText);  // sets the name to left of border pane
            listItem.setStyle("-fx-padding: 5;" + "-fx-border-style: solid inside;"
            + "-fx-border-width: 0 4 4 4;" + "-fx-border-color: black;");
            
            //inserts listItem into appropriate list 
            WhiteBoardFX.getItemList().get(currList).getChildren().add(listItem);  
        }
    }
    
}
