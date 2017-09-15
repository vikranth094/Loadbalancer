/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uag_loadbalancer;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;


/**
 *
 * @author vikranth
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label error;
     @FXML 
    private TextField uaginput;
     @FXML 
     private TableView table; 
     @FXML
    private Button cont;
    
    @FXML
    private void noUagClick(ActionEvent event) {
        try{
        Integer.parseInt(uaginput.getText());
        }
        catch(Exception e)
        {
            error.setText("Invalid Input");uaginput.requestFocus();
        }
        if(uaginput.getText().length()<1||Integer.parseInt(uaginput.getText())<=0){
        error.setText("Invalid Input");uaginput.requestFocus();
        }
        else
        {
            error.setVisible(false);
            uaginput.setVisible(false);
            cont.setVisible(false);
            
            
            table.setVisible(true);
            table.setEditable(true);
            ObservableList<uagDeatails> data =FXCollections.observableArrayList();
            int n=Integer.parseInt(uaginput.getText());
            for(int i=0;i<n;i++)
            {
                data.add(new uagDeatails("https://", "favicon.ico", "down"));
            }
        TableColumn uagUrl = new TableColumn("UAG IP ADDRESS");
        uagUrl.setMinWidth(100);
       
        uagUrl.setCellValueFactory(
                new PropertyValueFactory<>("url"));
        uagUrl.setCellFactory(TextFieldTableCell.<uagDeatails>forTableColumn());
     uagUrl.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<uagDeatails, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<uagDeatails, String> t) {
                    System.out.println("ON edit commit" + t);
                    ((uagDeatails) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())).seturl(t.getNewValue());
                }
            }
    );
     
        TableColumn healthfile = new TableColumn("HEALTH CHECK FILENAME");
        healthfile.setMinWidth(100);
        healthfile.setCellValueFactory(
                new PropertyValueFactory<>("healthFileName"));
         healthfile.setCellFactory(TextFieldTableCell.<uagDeatails>forTableColumn());
     healthfile.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<uagDeatails, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<uagDeatails, String> t) {
                    System.out.println("ON edit commit" + t);
                    ((uagDeatails) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())).sethealth(t.getNewValue());
                }
            }
    );
        
         TableColumn status = new TableColumn("STATUS");
        status.setMinWidth(100);
        status.setCellValueFactory(
                new PropertyValueFactory<>("status"));
            table.setItems(data);
            table.getColumns().addAll(uagUrl, healthfile,status);
        
        
       
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        table.setVisible(false);
        uaginput.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        
    }

public static class uagDeatails{
  private final SimpleStringProperty url;
        private final SimpleStringProperty healthFileName;  
        private final SimpleStringProperty status;  
         private uagDeatails(String url, String healthFileName,String status) {
            this.url = new SimpleStringProperty(url);
            this.healthFileName = new SimpleStringProperty(healthFileName);
            this.status=new SimpleStringProperty(status);
        }

        public String getUrl() {
            return url.get();
        }

        public String getHealthFileName() {
            return healthFileName.get();
        }

        public String getStatus() {
            return status.get();
        }
    public void seturl(String url) {
            this.url.set(url);
        }
    public void sethealth(String healthfile) {
            this.healthFileName.set(healthfile);
        }
}    
    
}
