package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ReadConnProps {


	private final Line region;

	private ReadConnProps(Line line) {
		region = line;
	}

	public static void run(Line line) {

		final ReadConnProps prop = new ReadConnProps(line);

		line.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getClickCount() == 2){
					prop.mousePressed(event);
				}
			}
		});

	}

	protected void mousePressed(MouseEvent event) {
		//System.out.println(Main.connection.get(region.getId()).conn_between+"-");
		pop_window( Main.connection.get(region.getId()));
	

	}

	public void pop_window( MyConn temp) {

		// System.out.println(temp);
		
		TextField conn_no = new TextField(temp.Name);
		//TextField conn_type = new TextField(temp.Conn_Type);
		TextField conn_between = new TextField(temp.conn_between);
		
		ComboBox<String> conn_type = new ComboBox<String>();
		if(region.getStrokeWidth()==3){
					
			conn_type.setMinWidth(150);
			conn_type.setValue("Single");
			temp.Conn_Type = "Single";
			
		}else{
			
			conn_type.setMinWidth(150);
			conn_type.setValue("Double");
			temp.Conn_Type = "Double";
		}
		//conn_type.setEditable(true);
		
		
		ScrollPane sp = new ScrollPane();
		sp.setFitToHeight(true);
		sp.setFitToWidth(true);
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);

		// insert values from excel to gridpane i.e, to the textfields
		GridPane prop = new GridPane();
		prop.setVgap(4);
		prop.setPadding(new Insets(5, 5, 5, 5));

		prop.add(new Label("Connection Name: "), 0, 0);
		prop.add(conn_no, 1, 0);
		prop.add(new Label("Connection Type: "), 0, 1);
		prop.add(conn_type, 1, 1);
		prop.add(new Label("Connecting Modules: "), 0, 2);
		prop.add(conn_between, 1, 2);
			

		sp.setContent(prop);
		Stage stage = new Stage();
		stage.setTitle("Connection Properties");
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

		// set Stage boundaries to the lower right corner of the visible bounds
		// of the main screen
		stage.setX(primaryScreenBounds.getMinX()
				+ primaryScreenBounds.getWidth() - 300);
		stage.setY(primaryScreenBounds.getMinY()
				+ primaryScreenBounds.getHeight() - 680);
		stage.setScene(new Scene(new BorderPane(sp), 300, 300));
		stage.show();
		// Hide this current window (if this is what you want)
		// ((Node)(event.getSource())).getScene().getWindow().hide();

		conn_no.textProperty().addListener((observable, oldValue, newValue) -> {

			temp.Name = newValue;

		});
		conn_type.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				temp.Conn_Type =  conn_type.getSelectionModel().getSelectedItem().toString();
				//System.out.println(conn_type.getSelectionModel().getSelectedItem().toString());
			}
		        
		});
		conn_between.textProperty().addListener((observable, oldValue, newValue) -> {

			temp.conn_between = newValue;
			
		});
		

	}
}
