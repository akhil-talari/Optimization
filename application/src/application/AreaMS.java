package application;

import javafx.beans.property.SimpleStringProperty;

public class AreaMS {
		 
	private final SimpleStringProperty area;
	private final SimpleStringProperty milestone;


	public AreaMS(String area, String milestone) {
		this.area = new SimpleStringProperty(area);
		this.milestone = new SimpleStringProperty(milestone);       
	}
	public void setArea(String areas) {
		area.set(areas);	
	}
	public String getArea() {
		return area.get();	
	}

	public void setMilestone(String mstone) {
		milestone.set(mstone);	
	}
	public String getMilestone() {
		return milestone.get();	
	}
}

