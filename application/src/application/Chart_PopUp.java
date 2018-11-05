package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Chart_PopUp {

	@SuppressWarnings("unchecked")
	public void run_ChartPopUp(int name, int HookupName, int duration, int Install_duration, int storage){
		
		Node n;
		
		Stage primaryStage = new Stage();
		
		
		Pane leftPane = new Pane();	
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToWidth(true);
    	scrollPane.setFitToHeight(true);
    	
		scrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		TreeItem<String> rootItem = new TreeItem<String>("Plots");
		rootItem.setExpanded(false);
		
		TreeItem<String> subrootItem = new TreeItem<String>("Duration  (Completion Month: "+(duration-1)+")");
		TreeItem<String> subrootItem1 = new TreeItem<String>("Overall Schedule");
		subrootItem1.setExpanded(false);
		TreeItem<String> item1 = new TreeItem<String>("Setting Schedule by Area");
		TreeItem<String> item11 = new TreeItem<String>("Setting Schedule by Module");
		TreeItem<String> item2 = new TreeItem<String>("Hookup Schedule by Area");
		TreeItem<String> item22 = new TreeItem<String>("Hookup Schedule by Connection");
		item1.getChildren().addAll(item11);
		item2.getChildren().addAll(item22);
		subrootItem1.getChildren().addAll(item1,item2);
		subrootItem.getChildren().addAll(subrootItem1);
		
		TreeItem<String> subrootItemC = new TreeItem<String>("Cost");
		
		TreeItem<String> subrootItemRV = new TreeItem<String>("Resource Variation");
		TreeItem<String> subrootItem2 = new TreeItem<String>("Equipment Histogram");
		TreeItem<String> subrootItem3 = new TreeItem<String>("Manpower Histogram");
		subrootItemRV.getChildren().addAll(subrootItem2,subrootItem3);
		
		TreeItem<String> subrootItemS = new TreeItem<String>("Storage Area (Total Storage: "+storage+"(m2))");
		TreeItem<String> subrootItem4 = new TreeItem<String>("Stagging Area Histogram");
		subrootItemS.getChildren().addAll(subrootItem4);
		
		rootItem.getChildren().addAll(subrootItem,subrootItemC,subrootItemRV,subrootItemS);
		TreeView<String> tree = new TreeView<String>(rootItem);
		tree.setPadding(new Insets(10,10,10,10));
		leftPane.getChildren().add(tree);
		
		tree.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {

	        @Override
	        public void changed(ObservableValue observable, Object oldValue,
	                Object newValue) {
	        	
            	scrollPane.setFitToHeight(true);
	            TreeItem<String> selectedItem = (TreeItem<String>) newValue;
	            if(selectedItem.getValue().equals("Overall Schedule")){
	            	
	            	
	            	scrollPane.setContent( Area_Chart.run(name,HookupName,duration));
	            }
	            else if(selectedItem.getValue().equals("Setting Schedule by Area")){

	            	scrollPane.setContent( Area_Chart.run(name,0,Install_duration));
	            	
	            }
	            else if(selectedItem.getValue().equals("Hookup Schedule by Area")){

	            	scrollPane.setContent( Area_Chart.run(0,HookupName,duration));

	            }
	            else if(selectedItem.getValue().equals("Setting Schedule by Module")){
	            	
	            	scrollPane.setFitToHeight(false);
	            	scrollPane.setContent( Individual_GanttChart.start_GanttChart("Setting Schedule: "+name,"Modules",Install_duration));
	            }
	            else if(selectedItem.getValue().equals("Hookup Schedule by Connection")){
	            	
	            	scrollPane.setFitToHeight(false);
	            	scrollPane.setContent( Individual_GanttChart.start_GanttChart("Hookup Schedule: "+HookupName,"Connections",duration));

	            }else if(selectedItem.getValue().equals("Equipment Histogram")){
	            
	            	Equipment_Chart ec = new Equipment_Chart();
					scrollPane.setContent( ec.run(name,Install_duration));
	            	
	            }else if(selectedItem.getValue().equals("Manpower Histogram")){
	            	
	            	Histogram h = new Histogram();
					scrollPane.setContent(h.run_MpHist(name, HookupName,duration));
	            	
	            }else if(selectedItem.getValue().equals("Stagging Area Histogram")){
	            	
					Stagging_Area sa = new Stagging_Area();
					scrollPane.setContent(sa.createStaggingChart(name,Install_duration));
	            	
	            }
	             
	        }

	      });
		
	

		// scrollpane for rightside of splitpane
		
		SplitPane splitPane = new SplitPane();
		splitPane.getItems().addAll(leftPane, scrollPane);
		splitPane.setDividerPositions(0.40);
		leftPane.maxWidthProperty().bind(
				splitPane.widthProperty().multiply(0.30));
		
		
		BorderPane root = new BorderPane(splitPane);
		Scene scene = new Scene(root, 800, 600);		    
		scene.getStylesheets().add(
				getClass().getResource("application.css").toExternalForm());
		splitPane.prefHeightProperty().bind(scene.heightProperty());
		tree.prefHeightProperty().bind(scene.heightProperty());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
