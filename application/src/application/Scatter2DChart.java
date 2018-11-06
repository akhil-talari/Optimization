package application;

import java.util.ArrayList;

import com.orsoncharts.data.xyz.XYZItemKey;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import static application.Main.*;


public class Scatter2DChart {
	
	 NumberAxis xAxis;
	 NumberAxis yAxis;
	 ScatterChart<Number,Number> sc;
	 static int slider_max = 5; //NOV5
	 
	Scatter2DChart(){
		
		xAxis = new NumberAxis(45,81, 1);
	    yAxis = new NumberAxis(50, 700,50);        
	    sc = new ScatterChart<Number,Number>(xAxis,yAxis);
	  //  sc.setTitle(name+"");
	}
 
    public void start() {
    	
    	Stage stage = new Stage();
        stage.setTitle("Scatter Chart Hookup");
               
        xAxis.setLabel(" Project Month");                
        yAxis.setLabel("Resource Variation (People)");
              
        Scene scene  = new Scene(sc, 500, 400);
        stage.setScene(scene);
        stage.show();
    }
 
    public void run(int name, ArrayList<int[]> objsfor2D) {
    	
    	Thread updateThread = new Thread(() -> {
    	     
            try {
          
              Thread.sleep(1000);
              Platform.runLater(() -> run1(name, objsfor2D));
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
        	
        });
        
        updateThread.setDaemon(true);
        updateThread.start();
    }

	private void run1(int name,ArrayList<int[]> objsfor2D) {

		if (sc.getData() == null) 
            sc.setData(FXCollections.<XYChart.Series<Number,Number>>observableArrayList());
        ScatterChart.Series<Number, Number> series = new ScatterChart.Series<Number, Number>();
        series.setName("Solution: "+name);
     	sc.getData().add(series);
        for (int i=0; i<objsfor2D.size(); i++) {
        	
        	int temp[] = new int[2];
	    	temp = objsfor2D.get(i);
	    	
        	ScatterChart.Data<Number, Number> data = new ScatterChart.Data<Number, Number>(temp[0], temp[1]);
        	
        	series.getData().add(data);
        	data.setExtraValue(i);
        	data.getNode().setOnMouseClicked(e -> 
        		{
                    //ArrayList<Integer> newTour =  Hookup.current.sequence;
                    ArrayList<Integer> newTour =  Install.Hchromofobjs.get((int)data.getXValue()+" "+(int)data.getYValue()).current.sequence;

                    int area =  Install.getTotalStorage(newTour);
                    System.out.println(Install.modinstallmonth);
                    System.out.println("====================================");
                    System.out.println("===================================");

                    ArrayList<Integer> newTour2  = Install.Hchromofobjs.get((int)data.getXValue()+" "+(int)data.getYValue()).sequence;
                    Hookup.current = Install.Hchromofobjs.get((int)data.getXValue()+" "+(int)data.getYValue()).current;
                    ArrayList<Unit> units2 = Hookup.Hookup_getSchedule(newTour2);

//                    ArrayList<Integer> newTour2  = Hookup.Hchromofobjs.get((int)data.getXValue()+" "+(int)data.getYValue());
//                    ArrayList<Unit> units2 = Hookup.Hookup_getSchedule(newTour2);
//
                    System.out.println(Hookup.connStartmonth);
                    System.out.println("====================================");
                    System.out.println(Hookup.connduration);
                    System.out.println("===================================");
                    //System.out.println((int) data.getXValue());

                    //NOV5
                    slider_max = (int) data.getXValue();
                    opacityLevel.setMax(slider_max);
                    Simulate.setContent(splitPane_sim);
                    //NOV5

                    Platform.runLater(new Runnable() {

                        int num1 = Install.Hchromofobjs.get((int)data.getXValue()+" "+(int)data.getYValue()).currentNo;

                        //int num1 = (GA.max_Install_gen-1)*GA.Install_PopSize+1;
                        int num = (name-1)*GA.Hookup_PopSize+(int)data.getExtraValue();
                   	    @Override
                   	    public void run() {
                            new simulation_info(module_label);
                            //simulation_info_sort.sort();
                            new simulation_conn_info(connection);
                   	    	Chart_PopUp cp = new Chart_PopUp();
                   	    	cp.run_ChartPopUp(num1,num,(int)data.getXValue()+1,Hookup.current.objectives[0]+1,Hookup.current.objectives[2]);
                        
                   	    }
                   	});
        		}
            );
        	
        }
        
       
	}
 }