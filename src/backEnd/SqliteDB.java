/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backEnd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import whiteboardfx.WhiteBoardFX;
import static whiteboardfx.WhiteBoardFX.createTile;

/**
 *
 * @author Robert
 */
public class SqliteDB {
    
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C:/Users/Robert/whiteboard.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public void loadTileData() {
        
        String sql = "SELECT name, image FROM category";
        
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            WhiteBoardFX wb = new WhiteBoardFX();
            // loop through the result set
            while (rs.next()) {
                
                Button btn = wb.createTileBtn(rs.getString("image"));
                createTile(btn, WhiteBoardFX.getTilePane());
                wb.createListMenu(rs.getString("name"));
                //WhiteBoardFX.getListScene().add(wb.createListMenu(rs.getString("name")));
             /*
                System.out.println(rs.getString("name") +  "\t" + 
                                   rs.getString("image") + "\t" +
                                   rs.getDouble("capacity"));
             */
                
            }
         
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
    }
    
     public void loadListData() {
        
        String sql = "SELECT id, item FROM entry";
        
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            WhiteBoardFX wb = new WhiteBoardFX();
            // loop through the result set
            while (rs.next()) {
                
                Text nameText = new Text(rs.getString("item"));
                nameText.setFont(new Font("Arial", 50));
                nameText.setFill(Color.BLACK);
                
                BorderPane h = new BorderPane();
                h.setLeft(nameText);
                h.setStyle("-fx-padding: 5;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 0 4 4 4;" + "-fx-border-color: black;");
                WhiteBoardFX.getItemList().get(rs.getInt("id")-1).getChildren().add(h);
             /*
                System.out.println(rs.getString("name") +  "\t" + 
                                   rs.getString("image") + "\t" +
                                   rs.getDouble("capacity"));
             */
                
            }
         
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
    }
 
    /**
     * Delete a warehouse specified by the id
     *
     * @param id
     */
    public void insertItem(int currList, String item) {
        String sql = "INSERT INTO entry(id,item) VALUES(?,?)";
 
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, currList);
            pstmt.setString(2, item);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void insertTile(String name, String image) {
        String sql = "INSERT INTO category(name,image) VALUES(?,?)";
 
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, image);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            
            System.out.println(e.getMessage());
        }
    }
    
    public void deleteItem(int currList, String text) {
        String sql = "DELETE FROM entry WHERE item = ? AND id = ? ";
 
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            // set the corresponding param
            pstmt.setString(1, text);
            pstmt.setInt(2, currList);
            // execute the delete statement
            pstmt.executeUpdate();
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
 
    /**
     * @param args the command line arguments
     */
   
}
