package csci2020u;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.*;
import java.net.*;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.json.*;
import com.fasterxml.jackson.core.TreeCodec;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;


public class Lab09 extends Application {

private static Color[] pieColours = {
 Color.AQUA, Color.GOLD, Color.DARKORANGE,
 Color.DARKSALMON, Color.LAWNGREEN, Color.PLUM
};

private Map<String,Integer> events = new TreeMap<>();
public List<Float> stockDataA = new ArrayList<>();
public List<Float> stockDataB = new ArrayList<>();

    @FXML
    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();
        Scene scene = new Scene(root, 625, 400, Color.WHITE);

        canvas = new Canvas();

        // broken?
        canvas.setHeight(400);
        canvas.setWidth(625);

        root.getChildren().add(canvas);
        primaryStage.setTitle("Lab 09");
        primaryStage.setScene(scene);
        primaryStage.show();

		downloadStockPrices();
        draw(root);

    }

    private void draw(Group group) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	
	
	gc.setStroke(Color.BLACK);
	gc.strokeLine(50,350,575,350);
	gc.strokeLine(50,50,50,350);
        /*
        ArcType.ROUND - pie shaped
        ArcType.CHORD - endpoint connected to startpoint with line
         */
		float maxHeight = 0;

	for(int i =0;i < stockDataA.size(); i++){
		if(stockDataA.get(i) > maxHeight)
			maxHeight = stockDataA.get(i);
	}	
	
	for(int i =0;i < stockDataB.size(); i++){
		if(stockDataB.get(i) > maxHeight)
			maxHeight = stockDataB.get(i);
	}	
		gc.setStroke(Color.RED);
	for(int i = 0; i < stockDataA.size() - 1; i++){
		gc.strokeLine(((i * 1.0 / stockDataA.size()) * 525) + 50, (stockDataA.get(i) / maxHeight) * 300,(((i + 1.0) / stockDataA.size()) * 525) + 50, (stockDataA.get(i + 1) / maxHeight) * 300);	
		}
    
		
		gc.setStroke(Color.BLUE);
	for(int i = 0; i < stockDataB.size() - 1; i++){
		gc.strokeLine(((i * 1.0 / stockDataB.size()) * 525) + 50, (stockDataB.get(i) / maxHeight) * 300,(((i + 1.0) / stockDataB.size()) * 525) + 50, (stockDataB.get(i + 1) / maxHeight) * 300);	
		}
    }

	private void downloadStockPrices(){
		
		StringBuffer sBuff =  new StringBuffer();
		StringBuffer sBuffB =  new StringBuffer();
		try{
		URL siteA = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY_ADJUSTED&symbol=GOOG&apikey=B8FVI92689NMRNS8");
		HttpURLConnection conn = (HttpURLConnection)siteA.openConnection();
		
		//conn.setInstanceFollowRedirects(false);
		InputStreamReader iStream = new InputStreamReader(conn.getInputStream());
		BufferedReader bfStream = new BufferedReader(iStream);
		
		String line = "";
		
		
		while((line = bfStream.readLine()) != null){
			sBuff.append(line);			
		}		
		iStream.close();
		
		ObjectMapper mapper = new ObjectMapper();
		JSONObject data = new JSONObject(sBuff.toString()).getJSONObject("Monthly Adjusted Time Series");
		for(String key: data.keySet()){
			stockDataA.add(data.getJSONObject(key).getFloat("4. close"));
			
		}
		
		
		
		
		
		URL siteB = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY_ADJUSTED&symbol=AAPL&apikey=B8FVI92689NMRNS8");
		HttpURLConnection connB = (HttpURLConnection)siteB.openConnection();
		
		iStream = new InputStreamReader(connB.getInputStream());
		bfStream = new BufferedReader(iStream);
		
		while((line = bfStream.readLine()) != null){
			sBuffB.append(line);			
		}		
		iStream.close();
		
		mapper = new ObjectMapper();
		data = new JSONObject(sBuffB.toString()).getJSONObject("Monthly Adjusted Time Series");
		for(String key: data.keySet()){
			stockDataB.add(data.getJSONObject(key).getFloat("4. close"));
			
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

    public static void main(String[] args) {
        launch(args);
    }
}