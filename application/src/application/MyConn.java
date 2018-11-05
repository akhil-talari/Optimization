package application;



import javafx.scene.shape.Line;

public class MyConn {
	
	public String Name;
	public String Conn_Type;
	public String conn_between;
	public double startx;
	public double starty;
	public double endx;
	public double endy;
	public int orientation=0;

	public MyConn(Line line) {
		
		this.Name = line.getId();
		this.startx = line.getStartX();
		this.starty = line.getStartY();
		this.endx = line.getEndX();
		this.endy = line.getEndY();
		if(this.startx != this.endx){
			this.orientation=1;
		}
		if(line.getStrokeWidth()==3){
			Conn_Type="Single";
		}else{
			Conn_Type="Double";
		}
	}

}
