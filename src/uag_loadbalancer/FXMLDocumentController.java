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
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 *
 * @author vikranth
 */
public class FXMLDocumentController implements Initializable {
   ObservableList<uagDeatails> data; 
   boolean flag=false;
   ArrayList<uagList> uag;
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
        
        
        StringBuffer sb=new StringBuffer();
                  BufferedReader bufReader = null;  
  BufferedWriter bufWriter = null;  
  
  try {  
   bufReader = new BufferedReader(new FileReader("Apache24\\conf1.txt"));  
   bufWriter = new BufferedWriter(new FileWriter(  
     "Apache24\\conf\\httpd.conf"));  
   String data;  
   
   while ((data = bufReader.readLine()) != null) {    
      sb.append(data+"\n");
      
      
   }
   int i=1;
 for(uagList ug:uag)
 {
     sb.append(String.format("BalancerMember %s:%s route=node%d", ug.url,"443",i)+"\n");
     i++;
     
 }
  bufReader = new BufferedReader(new FileReader("Apache24\\conf2.txt"));
  
     while ((data = bufReader.readLine()) != null) {  
  //  System.out.println(data);  
      sb.append(data+"\n");
      
      
   }
    bufWriter.write(sb.toString());
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
   ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
exec.scheduleAtFixedRate(new Runnable() {
  @Override
  public void run() {
    // do stuff
    try{
    for(uagList ug:uag)
 {System.out.println(ug.url);
         TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };
 
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
 
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
 
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
 
        URL url = new URL(ug.url+"/"+ug.healthName);
        HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
        con.getResponseCode();
        
        if(String.valueOf(con.getResponseCode()).equals("200")&&ug.status.equals("down"))
        {
            //add
            System.out.println("add");
            ug.setStatus("up");
            flag=true;
            
        }
        else if(!String.valueOf(con.getResponseCode()).equals("200")&&ug.status.equals("up"))
        {
            //remove
            System.out.println("remove");
            ug.setStatus("down");
            flag=true;
        }
 }
    }catch(Exception e){ System.out.println("remove exception");}
 if(flag){
    StringBuffer sb=new StringBuffer();
                  BufferedReader bufReader = null;  
  BufferedWriter bufWriter = null;  
  
  try {  
   bufReader = new BufferedReader(new FileReader("Apache24\\conf1.txt"));  
   bufWriter = new BufferedWriter(new FileWriter(  
     "Apache24\\conf\\httpd.conf"));  
   String data;  
   
   while ((data = bufReader.readLine()) != null) {    
      sb.append(data+"\n");
      
      
   }
   int i=1;
 for(uagList ug:uag)
 {
     if(ug.status.equals("up")){
     sb.append(String.format("BalancerMember %s:%s route=node%d", ug.url,"443",i)+"\n");
     i++;
     }
     
 }
  bufReader = new BufferedReader(new FileReader("Apache24\\conf2.txt"));
  
     while ((data = bufReader.readLine()) != null) {  
  //  System.out.println(data);  
      sb.append(data+"\n");
      
      
   }
    bufWriter.write(sb.toString());
    bufReader.close();  
   bufWriter.close();
   
  } catch (Exception e) {  
   e.printStackTrace();  
  }   
 try {
    InetAddress IP=InetAddress.getLocalHost();
    
    Process p1 = runtime.exec("cmd /c start Apache24\\bin\\restart.bat");
    InputStream is = p1.getInputStream();
    int i = 0;
    while( (i = is.read() ) != -1) {
        System.out.print((char)i);
    }
} catch(IOException ioException) {
    System.out.println(ioException.getMessage() );
} 
 ObservableList<uagDeatails>  data2 =FXCollections.observableArrayList();
 for(uagList ug:uag)
        {
            data2.add(new uagDeatails(ug.url, ug.healthName, ug.status));
        }
 table.setItems(data2);
 flag=false;
 }
 
  }
}, 0, 5, TimeUnit.SECONDS);
   
        
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
        
        table.setEditable(true);
        
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
            uag=new ArrayList();
            for(int i=0;i<n;i++)
            {
                data.add(new uagDeatails("https://", "favicon.ico", "down"));
                uag.add(new uagList("https://","favicon.ico","down"));
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
                    uag.get(t.getTablePosition().getRow()).setUrl(t.getNewValue());
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
                    uag.get(t.getTablePosition().getRow()).setHealthName(t.getNewValue());
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

class uagList{
    String url,healthName,status;
    uagList(String url,String healthName,String status)
    {
        this.url=url;
        this.healthName=healthName;
        this.status=status;
    }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getHealthName() {
            return healthName;
        }

        public void setHealthName(String healthName) {
            this.healthName = healthName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    
}
    
}
