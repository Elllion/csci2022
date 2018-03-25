package csci2020u;

import javafx.scene.*;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.input.*;
import javafx.scene.image.*;
import javafx.collections.*;
import javafx.event.*;

import java.io.*;
import java.net.*;



class ClientHandler extends Thread{
	Socket clientSocket;
	
	ClientHandler(Socket socket){
		clientSocket = socket;
	}
	
	public void run(){
		try{
		InputStream cin = clientSocket.getInputStream();
		InputStreamReader creader = new InputStreamReader(cin);
		BufferedReader cbin = new BufferedReader(creader);
		String cline = null;
		
		while((cline = cbin.readLine()) == null){
			
		}
		
		clientSocket.shutdownInput();
		
		Lab10.messages.append(cline);
		Lab10.messages.append(System.getProperty("line.separator"));	
		Lab10.txtArea.setText(Lab10.messages.toString());
		
		cbin.close();
		clientSocket.close();
		}catch(IOException e){
			e.printStackTrace();
		}			
	}
}

class NetworkHandler extends Thread{
	public void run(){
		ServerSocket serverSocket;
    Socket clientSocket;

   
        try {
            serverSocket = new ServerSocket(8888);

            System.out.println("Ready");

            //Constantly accept client connections
            while(true){
				System.out.println("Waiting for Connection...");
                clientSocket = serverSocket.accept();
                if(clientSocket != null){
                    //Open a thread if someone actually connects
                    ClientHandler conn = new ClientHandler(clientSocket);
                    conn.run();
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
	}
}
public class Lab10 extends Application {
    
	private BorderPane layout;

	public static StringBuilder messages = new StringBuilder();
	public static TextArea txtArea = new TextArea();
	
    @Override
    public void start(Stage primaryStage) throws Exception{
      
		GridPane editArea = new GridPane();
		editArea.setPadding(new Insets(10, 10, 10, 10));
		editArea.setVgap(10);
		editArea.setHgap(10);
	
		
	
		editArea.add(txtArea,0,0);
        
		Button exitButton = new Button("Exit");
		editArea.add(exitButton, 0, 1);
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
				public void handle(ActionEvent event) {
				System.exit(0);
			}
		});
    layout = new BorderPane();
	layout.setCenter(editArea);

	primaryStage.setTitle("Lab 10 - Server");
    Scene scene = new Scene(layout, 300, 200);
    primaryStage.setScene(scene);
    primaryStage.show();

    }

   

    public static void main(String[] args){
		NetworkHandler net = new NetworkHandler();
		net.start();
        launch(args);
	
    
	
    }
}