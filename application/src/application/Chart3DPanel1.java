package application;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Formatter;

import javafx.application.Platform;
import javafx.stage.Stage;

import com.orsoncharts.Chart3D;
import com.orsoncharts.Chart3DPanel;
import com.orsoncharts.data.ItemKey;
import com.orsoncharts.data.xyz.XYZItemKey;
import com.orsoncharts.graphics3d.Object3D;
import com.orsoncharts.graphics3d.RenderedElement;
import com.orsoncharts.graphics3d.RenderingInfo;
import com.orsoncharts.interaction.Chart3DMouseEvent;
import com.orsoncharts.interaction.Chart3DMouseListener;
import com.orsoncharts.label.StandardXYZItemLabelGenerator;
import com.orsoncharts.util.ArgChecks;

public class Chart3DPanel1 extends Chart3DPanel {
	
	Chart3D chart;
	
	public Chart3DPanel1(Chart3D chart) {
		super(chart);
		// TODO Auto-generated constructor stub
	}
	
/*	  @Override
	    public void mouseClicked(MouseEvent e) {

	        RenderingInfo info = getRenderingInfo();
	        if (info != null) {
	        	 Object3D object = info.fetchObjectAt(e.getX(), e.getY());
	             if (object != null) {
	                 ItemKey key = (ItemKey) object.getProperty(Object3D.ITEM_KEY);
	                 int seriesIndex = ScatterPlot3D.dataset.getSeriesIndex(((XYZItemKey) key).getSeriesKey());
	                 int item = ((XYZItemKey) key).getItemIndex();
	                 
	                 if (key != null) {
	                    ArrayList<Integer> newTour =  Install.chromofobjs.get(Getxyzdata.getdatavalues((XYZItemKey)key));
	                    int area =  Install.getTotalStorage(newTour);
	                     Platform.runLater(new Runnable() {
	                    	    @Override
	                    	    public void run() {
	                    	    	//GanttChart.start_GanttChart(1,newTour);
	       	                     	//Histogram.start_Hist(1);
	                    	    	int num = seriesIndex*GA.Install_PopSize+item;
	                    	    	//Area_Chart.run(" - Scenario: "+num);
	       	                     	// call the equipment chart
	                    	    	new Equipment_Chart().run(" - Scenario: "+num);
	                    	    	//call staging area chart
	                    	    	//new Stagging_Area().createBarChart(" - Scenario: "+num);
	                    	    }
	                    	});
	                     
	                 }
	             } 
	        }
	       
	        super.mouseClicked(e);
	    }
*/
}
class  Getxyzdata {
	
	public static  String getdatavalues(XYZItemKey key) {
        ArgChecks.nullNotPermitted(ScatterPlot3D.dataset, "dataset");
        ArgChecks.nullNotPermitted(key.getSeriesKey(), "seriesKey");
        
        int seriesIndex = ScatterPlot3D.dataset.getSeriesIndex(key.getSeriesKey());
       
        int x = (int)ScatterPlot3D.dataset.getX(seriesIndex, key.getItemIndex());
        int y = (int)ScatterPlot3D.dataset.getY(seriesIndex,  key.getItemIndex());
        int z = (int)ScatterPlot3D.dataset.getZ(seriesIndex,  key.getItemIndex());
      //  System.out.println("gen: "+seriesIndex+" item: "+key.getItemIndex());
        String result = x+" "+y+" "+z;
        
        return result;
    }
}