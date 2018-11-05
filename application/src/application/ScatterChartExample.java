package application;

import javafx.application.Application;

import static application.Install.sequences_group;
import static application.Main.connection;
import static application.Main.module_label;
import static application.k_m.groups;
import static application.k_m.groups_get_string;
import static javafx.application.Application.launch;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

import java.text.DecimalFormat;
import java.util.*;

public class ScatterChartExample {

    public static void start() {

        Stage stage = new Stage();
        DecimalFormat df2 = new DecimalFormat(".##");
        k_m me = new k_m();

        me.memmain();

        //Defining the axes
        NumberAxis xAxis = new NumberAxis(0, 10, 1);
        xAxis.setLabel("K-Cluster");

        NumberAxis yAxis = new NumberAxis(1,  sequences_group.size(), 1);
        yAxis.setLabel("Similarity score");


        //Creating the Scatter chart
        ScatterChart<Number, Number> scatterChart =
                new ScatterChart(xAxis, yAxis);

        if (scatterChart.getData() == null)
            scatterChart.setData(FXCollections.<XYChart.Series<Number, Number>>observableArrayList());
        ScatterChart.Series<Number, Number> series = new ScatterChart.Series<Number, Number>();

        ScatterChart.Series<Number, Number> series2 = new ScatterChart.Series<Number, Number>();
        scatterChart.getData().add(series);

        for (int i = 1; i <= me.clusters_size; i++) {
            Collection<Double> temp;
            temp = me.groups_plot.get(i);

            Iterator<Double> iterator = temp.iterator();

            // while loop
            while (iterator.hasNext()) {
                //System.out.println("value= " + iterator.next());
                Double d = iterator.next();
                ScatterChart.Data<Number, Number> data = new ScatterChart.Data<Number, Number>((i), d);

                series.getData().add(data);
                series.setName("data points ");
                data.setExtraValue(i);

                data.getNode().setOnMouseClicked(e ->
                        {
                            System.out.println("X ");
                            System.out.println((int) data.getXValue());
                            System.out.println("Y ");
                            System.out.println((double) data.getYValue());
                            System.out.println(groups_get_string.get((double) data.getYValue()));

                            System.out.println("---------------------");


                            //System.out.println(favList);

                            Collection<Install_Chromosome> chromos =  groups_get_string.get((double) data.getYValue());
                            Iterator<Install_Chromosome> it = chromos.iterator();
                            Hookup.current = it.next();
                            GA.Hookup_gen = 1;
                            Hookup.initial();
                            });
                        }

            }



        for (int i = 0; i < me.mean_list.size(); i++) {
            ScatterChart.Data<Number, Number> data2 = new ScatterChart.Data<Number, Number>((i + 1), me.mean_list.get(i));

            series2.getData().add(data2);
            series2.setName("Mean");
        }

        scatterChart.getData().add(series2);
        //Setting the data to scatter chart

        //Creating a Group object
        StackPane root = new StackPane();
        root.getChildren().add(scatterChart);

        scatterChart.getStylesheets().addAll(ScatterChartExample.class.getResource("applicationkmeans.css").toExternalForm());
        //Creating a scene object
        Scene scene = new Scene(root, 500, 600);

        //Setting title to the Stage
        stage.setTitle("K-means clustering");

        //Adding scene to the stage
        stage.setScene(scene);


        //Displaying the contents of the stage
        stage.show();


    }
}