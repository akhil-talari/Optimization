package application;

import static application.GA.mapmodnames;
import static application.Install.*;
import static application.Install.modinstallmonth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Equipment_Chart{

    public Node run(int name, int Install_duration){
       
        

    	VBox v = new VBox();
        v.getChildren().add(createBarChart(name, Install_duration));
        v.getChildren().add(createLineChart(Install_duration));
        
        return v;
        
    }

    private NumberAxis createYaxis() {
        final NumberAxis axis = new NumberAxis(0, GA.Install_SPMT, 100);
        //axis.setPrefWidth(35);
        axis.setMinorTickCount(10);

       axis.setLabel("SPMT (axel)");

        return axis;
    }

    private CategoryAxis createXaxis() {
        final CategoryAxis axis = new CategoryAxis();
        axis.setLabel("Project Month");
        axis.setTickLabelGap(10);

        return axis;
    }

    private BarChart<String, Number> createBarChart(int name, int Install_duration) {

    //    final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis(0,3,1);
        final BarChart<String, Number> bc =
                new BarChart<String, Number>(createXaxis(), yAxis);
        setDefaultChartProperties(bc);
        bc.setBarGap(0.5);
        bc.setCategoryGap(3);
        yAxis.setSide(Side.LEFT);
        yAxis.setLabel("Quantity (ea)");
        XYChart.Series series2 = new XYChart.Series();
        XYChart.Series series3 = new XYChart.Series();
        series2.setName("600 ton crane");
        series3.setName("1350 ton crane");


        for(int i = 29 ; i <= Install_duration ; i++) {
            try {
                if (CranesPerMonth.get(i).contains("NA")) {
                }
                if (CranesPerMonth.get(i).contains("600.0")) {
                    int occurrences = Collections.frequency(CranesPerMonth.get(i), "600.0");
                    series2.getData().add(new XYChart.Data(i + "", 1));
                }else{
                	series2.getData().add(new XYChart.Data(i + "", 0));
                }
                if (CranesPerMonth.get(i).contains("1350.0")) {
                    int occurrences = Collections.frequency(CranesPerMonth.get(i), "1350.0");
                    series3.getData().add(new XYChart.Data(i + "", 1));
                }else{
                	series3.getData().add(new XYChart.Data(i + "", 0));
                }
            }
            catch (NullPointerException ex){
            	series2.getData().add(new XYChart.Data(i + "", 0));
            	series3.getData().add(new XYChart.Data(i + "", 0));
            	
            }
        }
        bc.setTitle("Equipment Histogram: Setting Scenario: "+name);
        bc.setScaleShape(true);

        bc.getData().addAll(series2, series3);
   
       // bc.relocate(100, 400);
        return bc;
    }

    private LineChart<String, Number> createLineChart(int Install_duration) {
        final LineChart<String, Number> chart = new LineChart<>(createXaxis(), createYaxis());
        setDefaultChartProperties(chart);

        chart.setCreateSymbols(false);

      
        XYChart.Series series1 = new XYChart.Series();
        for(int i=29;i<=Install_duration;i++){
			series1.getData().add(new XYChart.Data(i+"", SPMTUsedpermonth.get(i)));	
		}
                        		
        chart.getData().addAll(series1);
        series1.setName("SPMT Axels");
      
        
        return chart;
    }

    private void setDefaultChartProperties(final XYChart<String, Number> chart) {
        chart.setLegendVisible(true);
        chart.setAnimated(false);
    }
  

}
