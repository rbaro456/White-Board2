/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whiteboardfx;

import backEnd.SqliteDB;
import promptWindow.AddItem;
import promptWindow.CreateTile;

import java.awt.Graphics;

import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 *
 * @author Robert
 */
public class WhiteBoardFX extends Application implements EventHandler<ActionEvent>{
   
    private static Scene tileMenu;  // main tile menu
    private static TilePane tile;  // contains tiles for tile menu
    private static Button tileBtn; // tile button for tile menu
   
   // static BorderPane listMenu; // contains all nodes for list menu
    private static BorderPane header; // header for list menu
    private static HBox buttonPane;  // buttons for list menu
    private static BorderPane footer; // footer for list menu
    private static Button addItem, delItem, backBtn, remove; // buttons used for lists
    
    private static ArrayList<Scene> listScene = new ArrayList<>();  // contains each list menu scene
    private static ArrayList<BorderPane> listPane = new ArrayList<>();  // contains the main BorderPane of each list menu
    private static ArrayList<VBox> itemList = new ArrayList<>(); // contains each list in list menu
    
    private static Stage primary;  // main stage
    private static int currList;  // int indicating the current list being used in itemList 
    private static int numOfLists = 0; // counts the number of lists created
    
    @Override
    public void start(Stage primaryStage) {
  
         
        
        createTileMenu();
        
        SqliteDB db = new SqliteDB();
        db.loadTileData();
        db.loadListData();
        
        
        /*
        String[] imageFiles = {"toDo.png", "research.png", "house.png", 
                                "fix.png", "grocery.png", "invest.png", 
                                "projects.png", "reminder.png"};
        
        String[] listTitles = {"To Do", "Research", "House", 
                                "Fix", "Grocery", "Investments", 
                                "Projects", "Reminder"};
        
        for(int i = 0; i < 8; i++) {
            
            //createTile(imageFiles[i],tile);
            Button btn = createTileBtn(imageFiles[i]);
            createTile(btn, tile);
            listScene.add(createListMenu(listTitles[i]));
            
        }
        
        
        */
        primary = primaryStage;
        primary.setTitle("White Board");
        primary.setScene(tileMenu);
        primary.setFullScreen(true);
        primary.setOnCloseRequest(e -> e.consume());
       // primary.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); /**SET THIS WHEN FINISHED WITH**/
        primary.show();                                                       /**PROJECT**/
    }
    
    public void createTileMenu() {
   
        BorderPane bb = new BorderPane();
        
        tile = new TilePane(Orientation.HORIZONTAL);  // arranging tiles within pane
        tile.setTileAlignment(Pos.TOP_LEFT);
        tile.setPrefRows(4);
        tile.setPadding(new Insets(60, 60, 60, 60));
        tile.setHgap(60);
        tile.setVgap(40);
        Image m = new Image(getClass().getResourceAsStream("meow.png"));
        tile.setBackground(new Background(new BackgroundImage(m,BackgroundRepeat.REPEAT,  // WHAT DO 
                                                                  BackgroundRepeat.REPEAT, // THE PARAMETERS MEAN???
                                                                  BackgroundPosition.DEFAULT,
                                                                  BackgroundSize.DEFAULT)));
        
        //tile.setStyle("-fx-background: cornflowerblue;");
        bb.setCenter(tile);
        BorderPane h = new BorderPane();
        h.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
        + "-fx-border-width: 4;" + "-fx-border-color: black;"
        + "-fx-background-color: cornflowerblue;");
        Button create = new Button("Create");
        create.setOnAction(e -> CreateTile.display());
        create.setPrefSize(130,100);
        Button quit = new Button("Quit");
        quit.setOnAction(e -> primary.close());
        quit.setPrefSize(130,100);
        h.setLeft(create);
        h.setRight(quit);
        bb.setBottom(h);
        tileMenu = new Scene(bb);  // creates the tile menu
        
    }
    
    public Button createTileBtn(String imageName) {  // creates the tile to be placed in tileMenu
        
        tileBtn = new Button();  // arranges the tile
        tileBtn.setPrefSize(200,150);
        tileBtn.setStyle("-fx-base: white;");
        tileBtn.setOnAction(this);
        Image btnImage = new Image(getClass().getResourceAsStream(imageName));  // sets graphic for tile
        tileBtn.setGraphic(new ImageView(btnImage));
        
        return tileBtn;
    }
    
    public static void createTile(Button tileBtn, TilePane tile) { // adds the tile button to the tileMenu 
      
        tile.getChildren().add(tileBtn);  // adds tile to the tile menu
    }
    
    public void createListMenu(String name) {  // creates scene used to list items
        
        Text titleText = new Text(name);  // creates and formats title for menu
        InnerShadow is = new InnerShadow();  // creates shadow effect for titleText
        is.setOffsetX(4.0f);
        is.setOffsetY(4.0f);
        
        titleText.setEffect(is);  // sets the shadow effect
        titleText.setFont(Font.font(null, FontWeight.BOLD, 80));  // sets the font to Bold; Size 80
        titleText.setFill(Color.RED);  // sets the color
      
        backBtn = new Button();  // back button used to return to tile menu
        backBtn.setPrefSize(50,50);
        backBtn.setOnAction(e -> {  // sets the scene to the tile menu if pressed
            primary.setScene(tileMenu);
            primary.setFullScreen(true);
        });
        Image backImage = new Image(getClass().getResourceAsStream("back.png"));  // sets image and effects for back button
        backBtn.setGraphic(new ImageView(backImage));
        backBtn.setStyle("-fx-border-color: transparent;" + "-fx-background-color: transparent;");
        
        
        header = new BorderPane(); // creates BorderPane for the top of scene
        header.setLeft(titleText);
        header.setRight(backBtn);
        header.setPrefHeight(100);
        header.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
        + "-fx-border-width: 4;" + "-fx-border-color: black;"
        +  "-fx-background-color: #000000;");
        
        
        VBox listContainer = new VBox();  // VBox that contains all the border panes with list items
        listContainer.setStyle("-fx-background-color: #dc143c;" +  "-fx-border-color: black;" + "-fx-border-width: 4;");
        itemList.add(numOfLists,listContainer);  // Adds listContainer to the appropriate position in itemList array
        
        ScrollPane sp = new ScrollPane(itemList.get(numOfLists));  // creates scrollpane for listContainer 
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:transparent;");
        
        
        addItem = new Button("Add");  // creates button used to display pop up window to add items
        addItem.setOnAction(e -> AddItem.display());  // if pressed it displays pop up window to add items to listContainer
        addItem.setPrefSize(110, 80);
        
        delItem = new Button("Delete"); // creates button used to delete items
        delItem.setOnAction(e -> deleteItem(currList));  // if pressed the user has the ability to 
        delItem.setPrefSize(110, 80);                    // delete list items within listContainer
        
        buttonPane = new HBox(20,addItem,delItem);  // places HBox at the bottom of page; 
        buttonPane.setPrefHeight(100);              // that contains add and delete buttons  
            
        footer = new BorderPane();  // footer creates a borderpane to be placed at bottom of scene
        footer.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
        + "-fx-border-width: 4;" + "-fx-border-color: black;"
        + "-fx-background-color: #000000;");
        footer.setLeft(buttonPane);  // sets the add/delete buttont to the bottom left of scene
       
        BorderPane listMenu = new BorderPane();  // contains all the components of the scene
        listMenu.setTop(header);  // sets header to top of scene
        listMenu.setCenter(sp);  // sets the listContainer to the center of scene
        listMenu.setBottom(footer);  // sets the footer to bottom of scene
        listPane.add(numOfLists,listMenu);  // inserts listMenu to appropriate position in array 
                                            // that contains all the listMenus
       
        Scene newListScene = new Scene(listPane.get(numOfLists));  // creates the new list scene
        
        numOfLists++;  // increment number of lists by one
        
        listScene.add(newListScene);  // adds the new list menu scene to the listScene array
        
        //return newListScene;  // returns the scene and add it to the listScene array
    }
   
    public void deleteItem(int currList) {  // deletes item within current list
        
        BorderPane listMenu = listPane.get(currList);  // retrieves the list menu currently being used
        BorderPane header = (BorderPane) listMenu.getTop();  // gets the header compnent
        BorderPane footer = (BorderPane) listMenu.getBottom();  // gets the footer component
        
        HBox addDel = (HBox) footer.getLeft();  // gets the HBox that contains add and delete button
        ObservableList<Node> addDelBtns = addDel.getChildren();
        
        /**Disables all other button functionality when in delete item mode**/
        addDelBtns.get(0).setDisable(true);  // disables add button
        addDelBtns.get(1).setDisable(true); // disables delete button
        header.getRight().setDisable(true);  // disables back button
        
        ObservableList<Node> listItems = itemList.get(currList).getChildren();  // gets the current list of border panes 
                                                                                // containing the list items
        int i = 0;
        for (Iterator<Node> it = listItems.iterator(); it.hasNext();) { // loops through each list item contained within the border pane
            it.next();
                      
            CheckBox box = new CheckBox();  // creates check box to be added to list item
            box.setMinSize(60, 60);
            BorderPane item = (BorderPane)listItems.get(i);  // gets the borderpane containing list item
            item.setRight(box);  // adds check box to right side of each list item
            i++;
        }
        
        remove = new Button("Remove");  // creates remove button on bottom right of scene
        remove.setPrefSize(110, 80);
        remove.setOnAction(e -> removeItems());  // user uses check boxes to check the items they want to remove
        footer.setRight(remove);                 // when remove is pressed; the checked items are then removed
    }
    
    public void removeItems() {
        
        BorderPane listMenu = listPane.get(currList);  // gets the main border pane from current list menu scene
        BorderPane header = (BorderPane) listMenu.getTop();  // gets the header from current list menu scene
        BorderPane footer = (BorderPane) listMenu.getBottom(); // gets the footer from current list menu scene
        
        ObservableList<Node> itemListArray = itemList.get(currList).getChildren();  // returns array of border pane nodes containing list items
        Iterator<Node> ite = itemListArray.iterator();  // iterator to go through itemListArray
        
        while(ite.hasNext()) {  // Loops through the itemListArray; removes any list items with a checked box
            
            BorderPane item = (BorderPane) ite.next();  // gets the list item
            CheckBox box = (CheckBox) item.getRight();  // gets the check box within that list item
            
            if(box.isSelected()) {  // if check box is selected; remove item
                   
                Text itemName = (Text)item.getLeft();  // gets the name of list item               
                SqliteDB db = new SqliteDB();  // creates new data base object
                db.deleteItem(currList+1, itemName.getText());  // deletes item from db 
                                                                // currList+1 because db starts 1; not 0
                ite.remove();  // removes list item from itemListArray     
                
            }  
        }
        
        footer.setRight(null);  // gets rid of remove button
        
        ite = itemListArray.iterator();  // creates an iterator for itemListArray
        while(ite.hasNext()) {  // loops through itemListArray and removes the check boxes from each list item
            
            BorderPane item = (BorderPane) ite.next();
            item.setRight(null);
        }
        
        HBox addDel = (HBox) footer.getLeft();  // gets the HBox containing add/del buttons
        ObservableList<Node> addDelBtns = addDel.getChildren();  
        
        /**Enables all other button functionality when leaving delete item mode**/
        addDelBtns.get(0).setDisable(false);
        addDelBtns.get(1).setDisable(false);
        header.getRight().setDisable(false);
       
    }
    
    public void ifTileClicked(int numOfLists, ActionEvent e) {  // if tile is clicked
                                                                // switch to appropriate list scene
        ObservableList<Node> tileBtns = tile.getChildren();  // returns array of all the Tile Buttons
        
        for(int i = 0; i < numOfLists; i++) {  // loops through tile buttons to check if clicked
            
            if(e.getSource() == tileBtns.get(i)) {  // if tile buttons is clicked
            
                primary.setScene(listScene.get(i));  // set scene to appropriate list scene
                primary.setFullScreen(true);
                currList = i;  // updates current list being used
            } 
        }
        
    }
    
    public static int getCurrList() {  // returns the currList variable
        
        return currList;
    }
    
    public static TilePane getTilePane() {  // returns the TilePane menu
        
        return tile;
    }
    
    public static ArrayList<Scene> getListScene() {  // returns array of all the list scenes
        
        return listScene;
    }
    
    public static ArrayList<VBox> getItemList() {
        
        return itemList;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(ActionEvent e) {
        
        ifTileClicked(numOfLists, e);  // if tile is clicked; set scene to approiate list menu
    
    }  

}
