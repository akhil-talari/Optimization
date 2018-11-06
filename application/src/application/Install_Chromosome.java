package application;

import java.util.ArrayList;
import java.util.HashMap;

public class Install_Chromosome {
	
	public ArrayList<Integer> sequence;
	public int objectives[];
	public  int Number_of_violated_constraints;
    public  int rank;

	public HashMap<Integer,Integer > modinstallmonth;
	public HashMap<Integer, Integer> SPMTUsedpermonth;
	public HashMap<Integer, Integer> StagingAreaPerMonth;
	public HashMap<Integer,ArrayList<String>> CranesPerMonth;
	
	public Install_Chromosome(ArrayList<Integer> solutionX){
		
		sequence = new ArrayList<Integer>(solutionX);
		objectives = new int[3];
		Number_of_violated_constraints =0;   	
    	rank=0;
	}

	public void update_Install_Chromosome(HashMap<Integer, Integer> modinstallmonth, HashMap<Integer, Integer> SPMTUsedpermonth, HashMap<Integer, ArrayList<String>> cranesPerMonth, HashMap<Integer, Integer> stagingAreaPerMonth){
		this.modinstallmonth = modinstallmonth;
		this.CranesPerMonth = cranesPerMonth;
		this.SPMTUsedpermonth = SPMTUsedpermonth;
		this.StagingAreaPerMonth = stagingAreaPerMonth;

	}

}
