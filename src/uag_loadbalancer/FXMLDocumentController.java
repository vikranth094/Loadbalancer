/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uag_loadbalancer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
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
   ObservableList<uagDeatails> data; 
    @FXML
    private Label error,status,myip;
     @FXML 
    private TextField uaginput;
     @FXML 
     private TableView table; 
     @FXML
    private Button cont;
      @FXML
    private Button start;
       @FXML
    private Button stop;
    @FXML
    private void startClick(ActionEvent event)
    {
        
                  BufferedReader bufReader = null;  
  BufferedWriter bufWriter = null;  
  
  try {  
   bufReader = new BufferedReader(new FileReader("Apache24\\conf1.txt"));  
   bufWriter = new BufferedWriter(new FileWriter(  
     "Apache24\\conf\\httpd.conf"));  
   String data;  
   while ((data = bufReader.readLine()) != null) {  
    System.out.println(data);  
    bufWriter.write(data);  
   }  
    
    bufReader.close();  
   
    bufWriter.close();
  } catch (Exception e) {  
   e.printStackTrace();  
  } 
  
 
        
        
        
        
        
        table.setEditable(false);
        Runtime runtime = Runtime.getRuntime();
try {
    InetAddress IP=InetAddress.getLocalHost();
    myip.setText("Load Balancer ip Address is "+IP.getHostAddress()+":8080");
    Process p1 = runtime.exec("cmd /c start Apache24\\bin\\start.bat");
    InputStream is = p1.getInputStream();
    int i = 0;
    while( (i = is.read() ) != -1) {
        System.out.print((char)i);
    }
} catch(IOException ioException) {
    System.out.println(ioException.getMessage() );
}
   start.setVisible(false);
   stop.setVisible(true);
   status.setText("Status: Active");
   
   
        
    }
    @FXML
    private void stopClick(ActionEvent event)
    {

    
  
  
        
        
        
        
        
        myip.setText("");
        try {
              Runtime runtime = Runtime.getRuntime();
    
    Process p1 = runtime.exec("cmd /c start Apache24\\bin\\stop.bat");
    InputStream is = p1.getInputStream();
    int i = 0;
    while( (i = is.read() ) != -1) {
        System.out.print((char)i);
    }
} catch(IOException ioException) {
    System.out.println(ioException.getMessage() );
}
        status.setText("Status: Inactive");
        stop.setVisible(false);
        start.setVisible(true);
        
    }
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
            start.setVisible(true);
            
            table.setVisible(true);
            table.setEditable(true);
             data =FXCollections.observableArrayList();
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
        start.setVisible(false);
        stop.setVisible(false);
        status.setText("Status: Inactive");
        myip.setText("");
        
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
