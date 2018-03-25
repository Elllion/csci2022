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


public class Lab10 extends Application {
    
	private BorderPane layout;

    @Override
    public void start(Stage primaryStage) throws Exception{
      
		GridPane editArea = new GridPane();
		editArea.setPadding(new Insets(10, 10, 10, 10));
		editArea.setVgap(10);
		editArea.setHgap(10);
	
		Label lblId = new Label("Username:");
		editArea.add(lblId, 0, 0);
		TextField txtID = new TextField();
		editArea.add(txtID, 1, 0);
	
		Label lblAs = new Label("Message:");
		editArea.add(lblAs, 0, 1);
		TextField txtAs = new TextField();
		editArea.add(txtAs, 1, 1);
	
		
		Button addButton = new Button("Send");
		editArea.add(addButton, 0, 2);
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
				public void handle(ActionEvent event) {

				try{
					Socket socket = new Socket("127.0.0.1", 8888);
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
					out.print(txtID.getText()+ ": " + txtAs.getText());
					out.flush();
                    socket.shutdownOutput();//Shutdown once output sent
				}catch (IOException e){
				}					
				System.out.println(txtID.getText()+ ": " + txtAs.getText());
				}
		});
        
		Button exitButton = new Button("Exit");
		editArea.add(exitButton, 0, 3);
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
				public void handle(ActionEvent event) {
				System.exit(0);
			}
		});
    layout = new BorderPane();
	layout.setCenter(editArea);

	primaryStage.setTitle("Lab 10 - Client");
    Scene scene = new Scene(layout, 300, 150);
    primaryStage.setScene(scene);
    primaryStage.show();

    }

   

    public static void main(String[] args) {
        launch(args);
    }
}