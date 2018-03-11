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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.*;
import java.net.*;
import javax.swing.JTable;

import org.apache.commons.validator.*;

public class Lab08 extends Application {

  private BorderPane layout;
  private TableView<StudentRecord> table;
  private TextField txtID, txtMt, txtAs, txtEx;
  
  private File currentFilename = new File("default.csv");
 EmailValidator mailCheck = EmailValidator.getInstance();
@Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Lab 8 Demo");

	Menu fileMenu = new Menu("File");
	
	MenuItem newButton =  new MenuItem("New");
	newButton.setOnAction(new EventHandler<ActionEvent>(){
		@Override public void handle(ActionEvent e) {
			table.getItems().clear();
		}
	});
	
	MenuItem openButton = new MenuItem("Open");
	openButton.setOnAction(new EventHandler<ActionEvent>(){
		@Override public void handle(ActionEvent a) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			fileChooser.getExtensionFilters().addAll(
         new ExtensionFilter("CSV Files", "*.csv"));
			File selectedFile = fileChooser.showOpenDialog(primaryStage);
			if (selectedFile != null) {
			currentFilename = selectedFile;
			try{
			load();
			}
			catch(FileNotFoundException e){
				System.err.println("File not found");
			}
			}
		}
	});
	
	MenuItem saveButton = new MenuItem("Save");
	saveButton.setOnAction(new EventHandler<ActionEvent>(){
		@Override public void handle(ActionEvent a) {
			try{save();}
			catch (FileNotFoundException e){
				System.err.println("File Not Found");
			}
		}
	});
	
	MenuItem saveAsButton = new MenuItem("Save As");
	saveAsButton.setOnAction(new EventHandler<ActionEvent>(){
		@Override public void handle(ActionEvent a) {
			try{
				FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			fileChooser.getExtensionFilters().addAll(
         new ExtensionFilter("CSV Files", "*.csv"));
			File selectedFile = fileChooser.showOpenDialog(primaryStage);
			if (selectedFile != null) {
			currentFilename = selectedFile;
			}
				save();}
			catch (FileNotFoundException e){
				System.err.println("File Not Found");
			}
		}
	});
	
	MenuItem exitButton = new MenuItem("Exit");
	exitButton.setOnAction(new EventHandler<ActionEvent>(){
		@Override public void handle(ActionEvent a) {
			System.exit(0);
		}
	});
	fileMenu.getItems().add(newButton);
	fileMenu.getItems().add(openButton);
	fileMenu.getItems().add(saveButton);
	fileMenu.getItems().add(saveAsButton);
	fileMenu.getItems().add(exitButton);
	
	MenuBar menuBar = new MenuBar();
	menuBar.getMenus().add(fileMenu);
	
    GridPane editArea = new GridPane();
    editArea.setPadding(new Insets(10, 10, 10, 10));
    editArea.setVgap(10);
    editArea.setHgap(10);
	
	table =  new TableView<>();
	table.setItems(DataSource.getAllMarks());
	table.setEditable(true);
	
	TableColumn<StudentRecord,String> columnID;
	columnID = new TableColumn<>("ID");
	columnID.setMinWidth(100);
	columnID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
	
	TableColumn<StudentRecord,Float> columnAS;
	columnAS = new TableColumn<>("Assignments");
	columnAS.setMinWidth(100);
	columnAS.setCellValueFactory(new PropertyValueFactory<>("assignments"));
	
	TableColumn<StudentRecord,Float> columnMT;
	columnMT = new TableColumn<>("Midterm");
	columnMT.setMinWidth(100);
	columnMT.setCellValueFactory(new PropertyValueFactory<>("midterm"));
	
	TableColumn<StudentRecord,Float> columnEX;
	columnEX = new TableColumn<>("Exam");
	columnEX.setMinWidth(100);
	columnEX.setCellValueFactory(new PropertyValueFactory<>("exam"));
	
	TableColumn<StudentRecord,Float> columnTL;
	columnTL = new TableColumn<>("Overall");
	columnTL.setMinWidth(100);
	columnTL.setCellValueFactory(new PropertyValueFactory<>("total"));
	
	TableColumn<StudentRecord,Character> columnGL;
	columnGL = new TableColumn<>("Grade");
	columnGL.setMinWidth(100);
	columnGL.setCellValueFactory(new PropertyValueFactory<>("grade"));
	
	table.getColumns().add(columnID);
	table.getColumns().add(columnAS);
	table.getColumns().add(columnMT);
	table.getColumns().add(columnEX);
	table.getColumns().add(columnTL);
	table.getColumns().add(columnGL);
	
	//Username
    Label lblId = new Label("Student ID:");
    editArea.add(lblId, 0, 0);
    txtID = new TextField();
    editArea.add(txtID, 1, 0);
	
	Label lblAs = new Label("Assignments:");
    editArea.add(lblAs, 2, 0);
    txtAs = new TextField();
    editArea.add(txtAs, 3, 0);
	
	Label lblMt = new Label("Midterm:");
    editArea.add(lblMt, 2, 1);
    txtMt = new TextField();
    editArea.add(txtMt, 3, 1);
	
	Label lblEx = new Label("Exam");
    editArea.add(lblEx, 2, 2);
    txtEx = new TextField();
    editArea.add(txtEx, 3, 2);
	
    Button addButton = new Button("Register");
    editArea.add(addButton, 0, 2);
    addButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
		StudentRecord newR = new StudentRecord(txtID.getText(),
		Float.parseFloat(txtAs.getText()),
		Float.parseFloat(txtMt.getText()),
		Float.parseFloat(txtEx.getText()));
		
		table.getItems().add(newR);
        txtID.setText("");
		txtAs.setText("");
		txtMt.setText("");
		txtEx.setText("");
      }
    });
	
    // initialize the border pane
    layout = new BorderPane();
	layout.setTop(menuBar);
	layout.setCenter(table);
	layout.setBottom(editArea);

    Scene scene = new Scene(layout, 600, 300);
    primaryStage.setScene(scene);
    primaryStage.show();
load();	
  }
	public void save() throws FileNotFoundException{
		try{		
		currentFilename.createNewFile();
		}catch(IOException e){
			System.err.println("IOException");
		}
		PrintWriter pw = new PrintWriter(currentFilename);
		StringBuilder sb = new StringBuilder();
		for(int r = 0; r < table.getItems().size(); r++){
			for(int c = 0; c < 4; c++){
				sb.append(table.getColumns().get(c).getCellObservableValue(r).getValue().toString());
				sb.append(",");
			}
			sb.append("\n");
		}
		
		pw.write(sb.toString());
		pw.close();
	}
	
	public void load() throws FileNotFoundException{
		ObservableList<StudentRecord> marks =
		FXCollections.observableArrayList();
		String line = "";
		try(BufferedReader br = new BufferedReader(new FileReader(currentFilename))){
			while((line = br.readLine()) != null){
				String[] data = line.split(",");
				marks.add(new StudentRecord(data[0],Float.parseFloat(data[1]),Float.parseFloat(data[2]),Float.parseFloat(data[3])));
			}			
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		table.setItems(marks);
	}
  public static void main(String[] args) {
    launch(args);
  }
}