package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class zoneDetails {
	
	private final SimpleStringProperty zone;
	 private final StringProperty dir = new SimpleStringProperty();


	public zoneDetails(String zone, String dir) {
		this.zone = new SimpleStringProperty(zone);
		setDir(dir);      
	}
	public void setZone(String zones) {
		zone.set(zones);	
	}
	public String getZone() {
		return zone.get();	
	}
	
	 public final StringProperty dirProperty() {
         return this.dir;
     }


     public final String getDir() {
         return this.dirProperty().get();
     }


     public final void setDir(final String dirs) {
         this.dirProperty().set(dirs);
     }

}
