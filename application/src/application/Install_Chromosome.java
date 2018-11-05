package application;

import java.util.ArrayList;

public class Install_Chromosome {
	
	public ArrayList<Integer> sequence;
	public int objectives[];
	public  int Number_of_violated_constraints;
    public  int rank;
	
	public Install_Chromosome(ArrayList<Integer> solutionX){
		
		sequence = new ArrayList<Integer>(solutionX);
		objectives = new int[3];
		Number_of_violated_constraints =0;   	
    	rank=0;
	}

}
