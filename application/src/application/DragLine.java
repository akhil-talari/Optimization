package application;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;


public class DragLine {
    
    /**
     * The margin around the control that a user can click in to start resizing
     * the region.
     */
 //   private static final int RESIZE_MARGIN = 8;

    private final Line region;
    private final MyConn connection;

    private double y,x;
    
   // private boolean initMinHeight,initMinWidth;
    
    private boolean dragging, dragged=false;
    
    private DragLine(Line line, MyConn conn) {
        region = line;
        connection = conn;
    }

    public static void makeResizable(Line line, MyConn conn) {
        final DragLine resizer = new DragLine(line,conn);
        
        
        
        line.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	if(Main.mynode==null){
            		resizer.mousePressed(event);
            	}
            }});
        line.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	if(Main.mynode==null){
            		resizer.mouseDragged(event);
            	}
            }});
        line.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	if(Main.mynode==null){
            		resizer.mouseOver(event);
            	}
            }});
        line.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	if(Main.mynode==null){
            		resizer.mouseReleased(event);
            	}
            }});
    }

    protected void mouseReleased(MouseEvent event) {
        dragging = false;
        region.setCursor(Cursor.DEFAULT);
        if(dragged ==true){
        Main.getmodforconn(region,connection);
        region.toFront();
        dragged=false;
        }
        
    }

    protected void mouseOver(MouseEvent event) {
    	
        if(isInDraggableZone(event) || dragging) {
        	
            region.setCursor(Cursor.MOVE);
        }
       
        else {
            region.setCursor(Cursor.DEFAULT);
        }
        
    }

    protected boolean isInDraggableZone(MouseEvent event) {
    	
    	if((event.getX()>=region.getEndX()-5 && event.getX()<=region.getEndX()+5)  || (event.getY()>=region.getEndY()-5 && event.getY()<=region.getEndY()+5)){
    		return true;
    	}
    	
        return false;
    }
    


    protected void mouseDragged(MouseEvent event) {
    	
    	dragged = true;
        if(!dragging) {
            return;
        }
        
        double mousey = event.getY();
        double mousex = event.getX();
   
        
        y = mousey;
        x = mousex;
        region.setEndX(x);
        region.setEndY(y);
        
        connection.endx = x;
        connection.endy = y;
        
    }

    protected void mousePressed(MouseEvent event) {
        
        // ignore clicks outside of the draggable margin
        if(!(isInDraggableZone(event))) {
            return;
        }
        
        dragging = true;
        
        // make sure that the minimum height is set to the current height once,
        // setting a min height that is smaller than the current height will
    
        y = event.getY();
        x = event.getX();
    }
}
