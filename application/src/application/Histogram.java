package application;

import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import static application.Install.SPMTUsedpermonth;

public class Histogram {
	
	public Node run_MpHist(int name, int Hookupname, int duration){
		
		VBox v =new VBox();
		v.getChildren().add(Hookupchar_bargraph(name, Hookupname,  duration));
		v.getChildren().add(Hookupchart_line(name, Hookupname,  duration));
		return v;
	}
	
    public Node Hookupchar_bargraph(int name, int Hookupname, int duration) {
    	
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> barChart = new BarChart<>(xAxis,yAxis);
        barChart.setCategoryGap(0);
        barChart.setBarGap(1);
        barChart.setTitle("Manpower Histogram: Hookup Scenario: "+Hookupname);
        xAxis.setLabel("Project Month");
        yAxis.setLabel("Crew");

        XYChart.Series series1 = new XYChart.Series();
        for(int i=29;i<duration;i++){
            XYChart.Data data = new XYChart.Data(i+"",Hookup.crewUsedpermonth.get(i));
            series1.getData().add(data);
            data.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
                    newNode.setStyle("-fx-bar-fill: red;");
                }
            });
        }
        barChart.getData().addAll(series1);
        barChart.setLegendVisible(false);
        barChart.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        return barChart;
    }

    public Node Hookupchart_line(int name, int Hookupname, int duration) {
    	
        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Project Month");
        final NumberAxis yAxis = new NumberAxis(0,500,50);
        yAxis.setLabel("Crew");

        final LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        setDefaultChartProperties(chart);

        chart.setCreateSymbols(false);

        XYChart.Series series2 = new XYChart.Series();
        int cumulative = 0;
        for(int i=29;i<duration;i++){
        	
            cumulative += Hookup.crewUsedpermonth.get(i);
            series2.getData().add(new XYChart.Data(i+"", cumulative));
        }

        chart.getData().addAll(series2);
        series2.setName("Cumulative crew");
        //  chart.setPadding(new Insets(5, 54.5, 5, 0));
        return chart;
    }
    private void setDefaultChartProperties(final XYChart<String, Number> chart) {
    	
        chart.setLegendVisible(true);
        chart.setAnimated(false);
    }
}
