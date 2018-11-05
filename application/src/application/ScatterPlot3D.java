package application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.font.LineMetrics;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import com.orsoncharts.Chart3DFactory;
import com.orsoncharts.Chart3DPanel;
import com.orsoncharts.Chart3D;
import com.orsoncharts.Colors;
import com.orsoncharts.axis.LabelOrientation;
import com.orsoncharts.axis.NumberAxis3D;
import com.orsoncharts.axis.TickData;
import com.orsoncharts.data.xyz.XYZDataset;
import com.orsoncharts.data.xyz.XYZSeries;
import com.orsoncharts.data.xyz.XYZSeriesCollection;
import com.orsoncharts.graphics3d.Dimension3D;
import com.orsoncharts.graphics3d.RenderingInfo;
import com.orsoncharts.graphics3d.Utils2D;
import com.orsoncharts.graphics3d.ViewPoint3D;
import com.orsoncharts.graphics3d.swing.DisplayPanel3D;
import com.orsoncharts.marker.RangeMarker;
import com.orsoncharts.plot.XYZPlot;
import com.orsoncharts.renderer.xyz.ScatterXYZRenderer;
import com.orsoncharts.renderer.xyz.XYZRenderer;
import com.orsoncharts.style.ChartStyler;
import com.orsoncharts.style.ChartStyles;
import com.orsoncharts.util.Anchor2D;

@SuppressWarnings("serial")
public class ScatterPlot3D extends JFrame {

	 static XYZSeriesCollection dataset;
	
    public  ScatterPlot3D(String title) {
        super(title);
        //addWindowListener(new ExitOnClose());
        dataset = new XYZSeriesCollection();
        XYZSeries s = new XYZSeries("gen: 0");
        for(int i=0; i< Install.objsfor3D.size();i++){
        	int temp[] = new int[3];
        	temp = Install.objsfor3D.get(i);
        	s.add(temp[0],temp[1],temp[2]);
        }
       
        dataset.add(s);
        
       setContentPane(createDemoPanel());
    }	
    public static  JPanel createDemoPanel() {
    	
        DemoPanel content = new DemoPanel(new BorderLayout());
        content.setPreferredSize(new Dimension(500,500));
        Chart3D chart = createChart();
        
        Chart3DPanel1 chartPanel = new Chart3DPanel1(chart);
        content.setChartPanel(chartPanel);
       // chartPanel.zoomToFit(OrsonChartsDemo.DEFAULT_CONTENT_SIZE);
        content.add(new DisplayPanel3D(chartPanel));
        return content;
    }
    private static Chart3D createChart() {
    	
        Chart3D chart = Chart3DFactory1.createScatterChart("", 
                null, dataset, "Project Month", "Resource Variation(SPMT)", "Total Storage (square meter)");
       
        
        XYZPlot plot = (XYZPlot) chart.getPlot();
        plot.setDimensions(new Dimension3D(10.0, 6.0, 10.0));
        //plot.setGridlinePaintX(Color.BLACK);
        
        ScatterXYZRenderer renderer = (ScatterXYZRenderer) plot.getRenderer();
        renderer.setSize(0.25);
       
        renderer.setColors(Colors.createIntenseColors());
        chart.setViewPoint(ViewPoint3D.createAboveLeftViewPoint(40));
        return chart;    
    }
    public void create(){
    	getContentPane().add(createDemoPanel());
    }
	public  void createSeries(String name) {
		
		
		
		  XYZSeries s = new XYZSeries(name);
		 for(int i=0; i< Install.objsfor3D.size();i++){
	    	int temp[] = new int[3];
	    	temp = Install.objsfor3D.get(i);
	    	s.add(temp[0],temp[1],temp[2]);
		 }
        
        dataset.add(s);
        setContentPane(createDemoPanel());
        //Thread.sleep(2000);
        
        
    }
	

}
class Chart3DFactory1 {
	
	public static Chart3D createScatterChart(String title, String subtitle,  
            XYZDataset dataset, String xAxisLabel, String yAxisLabel,  
            String zAxisLabel) { 
        NumberAxis3D1 xAxis = new NumberAxis3D1(xAxisLabel); 
        NumberAxis3D1 yAxis = new NumberAxis3D1(yAxisLabel); 
        yAxis.setTickLabelOrientation(LabelOrientation.PERPENDICULAR); 
        NumberAxis3D1 zAxis = new NumberAxis3D1(zAxisLabel); 
        XYZRenderer renderer = new ScatterXYZRenderer(); 
        XYZPlot plot = new XYZPlot(dataset, renderer, xAxis, yAxis, zAxis); 
        return new Chart3D(title, subtitle, plot); 
    } 
	
}
class NumberAxis3D1 extends NumberAxis3D{

	private Object tickLabelFormatter;
	public NumberAxis3D1(String label) {
		super(label);
		// TODO Auto-generated constructor stub
	}
	public void draw(Graphics2D g2, Point2D pt0, Point2D pt1,  
            Point2D opposingPt, List<TickData> tickData, RenderingInfo info, 
            boolean hinting) { 
         
         
         super.draw( g2, pt0,  pt1,  
            opposingPt,  tickData,  info, 
             hinting);
        // draw a line for the axis 
         
        g2.setPaint(Color.BLACK); 
        Line2D axisLine = new Line2D.Float(pt0, pt1);   
        g2.draw(axisLine); 
         
       
       
    } 
	
	
}