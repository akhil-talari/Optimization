package application;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;

import javafx.scene.shape.Rectangle;

public class DragResizer {
    
    /**
     * The margin around the control that a user can click in to start resizing
     * the region.
     */
    private static final int RESIZE_MARGIN = 8;

    private final Rectangle region;

    private double y,x;
    
    private boolean initMinHeight,initMinWidth;
    
    private boolean dragging;
    
    private DragResizer(Rectangle rect) {
        region = rect;
    }

    public static void makeResizable(Rectangle rect) {
        final DragResizer resizer = new DragResizer(rect);
        
        
        
        rect.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	if(Main.mynode==null){
            		resizer.mousePressed(event);
            	}
            }});
        rect.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	if(Main.mynode==null){
            		resizer.mouseDragged(event);
            	}
            }});
        rect.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	if(Main.mynode==null){
            		resizer.mouseOver(event);
            	}
            }});
        rect.setOnMouseReleased(new EventHandler<MouseEvent>() {
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
    }

    protected void mouseOver(MouseEvent event) {
        if(isInDraggableZoneY(event) || dragging) {
        	
            region.setCursor(Cursor.S_RESIZE);
        }
        else if(isInDraggableZoneX(event) || dragging){
        	
        	region.setCursor(Cursor.E_RESIZE);
        }
        else {
            region.setCursor(Cursor.DEFAULT);
        }
        
    }

    protected boolean isInDraggableZoneY(MouseEvent event) {
    	
        return event.getY() > (region.getHeight()+20 - RESIZE_MARGIN);
    }
    protected boolean isInDraggableZoneX(MouseEvent event) {
    	
        return event.getX() > (region.getWidth()+20 - RESIZE_MARGIN);
    }


    protected void mouseDragged(MouseEvent event) {
        if(!dragging) {
            return;
        }
        
        double mousey = event.getY();
        double mousex = event.getX();
        
        double newHeight = region.getHeight() + (mousey - y);
        double newWidth = region.getWidth() + (mousex - x);

        region.setHeight(newHeight);
        region.setWidth(newWidth);
        
        y = mousey;
        x = mousex;
    }

    protected void mousePressed(MouseEvent event) {
        
        // ignore clicks outside of the draggable margin
        if(!(isInDraggableZoneY(event)||isInDraggableZoneX(event))) {
            return;
        }
        
        dragging = true;
        
        // make sure that the minimum height is set to the current height once,
        // setting a min height that is smaller than the current height will
        // have no effect
        if (!initMinHeight) {
            region.setHeight(region.getHeight());
            initMinHeight = true;
        }
        if (!initMinWidth) {
            region.setWidth(region.getWidth());
            initMinWidth = true;
        }
        
        y = event.getY();
        x = event.getX();
    }
}
