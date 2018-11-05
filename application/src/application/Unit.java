package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Unit {
	
    public int NoOfInstallations;
	public ArrayList<String> cranes;
	public int time;
    public List<Integer> machines;
    public HashMap<Integer, Integer> crewOfMachine = new HashMap<>();
    public int crewUsed;
	public int storage;

    public Unit() {
    	
        machines = new ArrayList<>();
        crewOfMachine = new HashMap<>();
        crewUsed = 0;
        storage=0;
        NoOfInstallations = 0;
        cranes = new ArrayList<String>();
    }
}
