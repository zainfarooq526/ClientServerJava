
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


public class GuiServer extends Application{

	
	TextField s1,s2,s3,s4, c1, t1;
	Button serverChoice,clientChoice,b1, b2;
	HashMap<String, Scene> sceneMap;
	GridPane grid;
	HBox hbox;
	HBox buttonBox;
	VBox clientBox;
	Scene startScene;
	BorderPane startPane;
	Server serverConnection;
	Client clientConnection;


	TextField tf1;


	ListView<String> listItems, listItems2, listItems3;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("The Networked Client/Server GUI Example");
		
		this.serverChoice = new Button("Server");
		this.serverChoice.setStyle("-fx-pref-width: 300px");
		this.serverChoice.setStyle("-fx-pref-height: 300px");
		
		this.serverChoice.setOnAction(e->{ primaryStage.setScene(sceneMap.get("server"));
											primaryStage.setTitle("This is the Server");
				serverConnection = new Server(data -> {
					Platform.runLater(()->{
						listItems.getItems().add(data.toString());
					});

				});
											
		});
		
		
		this.clientChoice = new Button("Client");
		this.clientChoice.setStyle("-fx-pref-width: 300px");
		this.clientChoice.setStyle("-fx-pref-height: 300px");
		
		this.clientChoice.setOnAction(e-> {primaryStage.setScene(sceneMap.get("client"));
											primaryStage.setTitle("This is a client");
											clientConnection = new Client(data->{
							Platform.runLater(()->{
								listItems2.getItems().add(clientConnection.pack.Message);
								listItems3.getItems().clear();
								ArrayList<String> temp = clientConnection.pack.P;
								for (int i = 0; i < temp.size(); i++) {
									listItems3.getItems().add(temp.get(i));
								}
							});});
											clientConnection.start();
		});
		
		this.buttonBox = new HBox(400, serverChoice, clientChoice);
		startPane = new BorderPane();
		startPane.setPadding(new Insets(70));
		startPane.setCenter(buttonBox);
		
		startScene = new Scene(startPane, 800,800);
		
		listItems = new ListView<String>();
		listItems2 = new ListView<String>();
		listItems3 = new ListView<String>();

		c1 = new TextField();
		b1 = new Button("Send");
		tf1 = new TextField();
		b2 = new Button("Click after adding client numbers you want to text to box below (blank to send to all)");

		b1.setOnAction(e->{clientConnection.send(c1.getText()); c1.clear();});
		b2.setOnAction(e->{String[] temp = tf1.getText().split(",");
			for(String i : temp) {
				clientConnection.pack.addClient.add(Integer.parseInt(i));
			};
			tf1.clear();
		});


		sceneMap = new HashMap<String, Scene>();
		
		sceneMap.put("server",  createServerGui());
		sceneMap.put("client",  createClientGui());
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
		
		 
		
		primaryStage.setScene(startScene);
		primaryStage.show();
		
	}
	
	public Scene createServerGui() {
		
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: coral");
		
		pane.setCenter(listItems);
	
		return new Scene(pane, 1000, 700);

		
	}
	
	public Scene createClientGui() {
		
		clientBox = new VBox(10, c1,b1,b2,tf1,listItems2, listItems3);
		clientBox.setStyle("-fx-background-color: blue");
		return new Scene(clientBox, 1000, 700);
		
	}


}
