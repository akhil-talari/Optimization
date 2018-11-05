package application;

import static application.Install.StagingAreaPerMonth;

//import com.sun.javafx.charts.Legend;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class Stagging_Area {
	
	 public Node createStaggingChart(int name, int Install_duration) {

	      
	        final CategoryAxis xAxis = new CategoryAxis();
	        final NumberAxis yAxis = new NumberAxis();
	        final BarChart<String, Number> bc = new BarChart<String, Number>(xAxis, yAxis);
	        bc.setCategoryGap(0);
	        bc.setBarGap(1);
	        xAxis.setLabel("Project Month");
	        yAxis.setLabel("Area (square meter)");
	        XYChart.Series series2 = new XYChart.Series();


	        for(int i = 29 ; i <= Install_duration ; i++) {

	            XYChart.Data data = new XYChart.Data(i + "", StagingAreaPerMonth.get(i));
	            series2.getData().add(data);
	            data.nodeProperty().addListener(new ChangeListener<Node>() {
	                @Override
	                public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
	                    newNode.setStyle("-fx-bar-fill: red;");


	                }
	            });

	        }
	        bc.getData().addAll(series2);
	        bc.setTitle("Stagging Area Chart: Setting Scenario: "+name);
	        bc.setLegendVisible(false);

	        bc.getStylesheets().addAll(getClass().getResource("application.css").toExternalForm());

	        return bc;
	    }
}
