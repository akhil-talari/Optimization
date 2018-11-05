package application;

import static application.GA.mapmodnames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class GA {
	
	int zone=0,count=1; 
	static ArrayList<ArrayList<Integer>> modsinzone;
	static HashMap<Integer,MyObject> mapmodnames;
	static int Install_PopSize;
	static int Install_gen = 1;
	static int max_Install_gen ;
	static double Install_CR;
	static double Install_MR ;
	static int Hookup_PopSize; 
	static int max_Hookup_gen;
	static double Hookup_CR;
	static double Hookup_MR; 
	static int Hookup_MCrew,Hookup_MCrewPM;
	static int connections;
	static int Hookup_gen;
	static int Install_SPMT;
	static int NoofWorkers, NoofHours,Manhours,NoofCranes,NoofCranes1,NoofCranes2,NoofCranes3,NoofCranes4,
	pcost,ecost,icost;
	static HashMap<String,ArrayList<Integer>> ModsinArea;
	static HashMap<String,ArrayList<Integer>> ConnsinArea;
	static HashMap<Integer,ArrayList<Integer>> connectionMap;
	static HashMap<Integer, Integer> mapmodnums; 
	static HashMap<String, Integer> AreaMilestones;
	
	
	GA(){
		
		zone=0;
		count=1; 
		modsinzone = new ArrayList<ArrayList<Integer>>();
		mapmodnames = new HashMap<Integer,MyObject>();
		Install_gen = 1;
		Hookup_MCrew = Integer.parseInt(Main.Hookup_MCrew.getText());
		Hookup_MCrewPM = Integer.parseInt(Main.Hookup_MCrewPM.getText());
		//Hookup_MCrew =3;
		Install_SPMT = Integer.parseInt(Main.Install_SPMT.getText());
		NoofCranes = Integer.parseInt(Main.NoofCranes.getText());
		NoofCranes1 = Integer.parseInt(Main.NoofCranes1.getText());
		NoofCranes2 = Integer.parseInt(Main.NoofCranes2.getText());
		NoofCranes3 = Integer.parseInt(Main.NoofCranes3.getText());
		NoofCranes4 = Integer.parseInt(Main.NoofCranes4.getText());
		NoofWorkers = Integer.parseInt(Main.NoofWorkers.getText());
		NoofHours = Integer.parseInt(Main.NoofHours.getText());
		Manhours = Integer.parseInt(Main.Manhours.getText());
		pcost = Integer.parseInt(Main.pcost.getText());
		ecost = Integer.parseInt(Main.ecost.getText());
		icost = Integer.parseInt(Main.icost.getText());
		
		if(Main.module.size()<110){

			Install_PopSize = 50;
			max_Install_gen = 7;
			Install_CR = 0.9;
			Install_MR = 0.1;
			Hookup_PopSize =50;
			max_Hookup_gen = 10;
			Hookup_CR=0.9;
			Hookup_MR = 0.1;
			
		}
		else {
			Install_PopSize = 70;
			max_Install_gen = 8;
			Install_CR = 0.9;
			Install_MR = 0.1;
			Hookup_PopSize =70;
			max_Hookup_gen = 12;
			Hookup_CR=0.9;
			Hookup_MR = 0.1;
			
		}
	    connections = Main.connection.size();

	    Hookup_gen=1;
	    connectionMap = new HashMap<Integer,ArrayList<Integer>>();
	    mapmodnums = new HashMap<Integer,Integer>();
	    ModsinArea = new HashMap<String,ArrayList<Integer>>();
	    ConnsinArea = new HashMap<String,ArrayList<Integer>>();
	    
	    Set<String> area = Main.areas.keySet();
	    for( String s: area){
	    	
	    	ModsinArea.put(s,new ArrayList<Integer>());
	    	ConnsinArea.put(s,new ArrayList<Integer>());
	    }
		
		while(count<=Main.module.size()){
			
			zone++;
			
			ArrayList<Integer> mods = new ArrayList<Integer>();
			
			for(int m=0;m<Main.module.size();m++){
				
				MyObject temp = Main.module_label.get(Integer.parseInt(Main.module.get(m).getId()));
				if(Integer.parseInt(temp.zone) == zone){
					
					mods.add(count);
					mapmodnames.put(count,temp);
					mapmodnums.put((int)(Double.parseDouble(temp.name)),count);
					ArrayList<Integer> modsinArea = ModsinArea.get(temp.area);
					modsinArea.add(count);
					ModsinArea.put(temp.area,modsinArea);
					count++;
							
				}
				
			}
			
			
			modsinzone.add(mods);
		}
		System.out.println(mapmodnums);
		
		for(int conn =1;conn<=Main.connection.size();conn++){
			
			ArrayList<Integer> modules = new ArrayList<Integer>();
			String splited[] = Main.connection.get(conn+"").conn_between.split("[,\\s]+");
			modules.add((int)(Double.parseDouble(splited[0])));
			modules.add((int)(Double.parseDouble(splited[1])));
			connectionMap.put(conn,modules);
			
			MyObject mod1 = mapmodnames.get(GA.mapmodnums.get(modules.get(0)));
			MyObject mod2 = mapmodnames.get(GA.mapmodnums.get(modules.get(1)));
			
			ArrayList<Integer> connsinArea = ConnsinArea.get(mod1.area);
			connsinArea.add(conn);
			ConnsinArea.put(mod1.area,connsinArea);
			ArrayList<Integer> connsinArea1 = ConnsinArea.get(mod2.area);
			connsinArea1.add(conn);
			ConnsinArea.put(mod2.area,connsinArea1);
		}
		System.out.println(connectionMap);
		
		AreaMilestones = new HashMap<String, Integer>();
		
		for (AreaMS o : Main.Area_MStones.getItems()) {
			String area1 = Main.AreaName.getCellData(o);
			int MS = Integer.parseInt(Main.AreaDL.getCellData(o));
			
			AreaMilestones.put(area1,MS);
		}
	
		System.out.println(AreaMilestones);
		
		for(int z=1;z<=GA.modsinzone.size();z++){
			if(!Main.ZoneDirection.containsKey(z)){
				
				Main.ZoneDirection.put(z,"Random");
			}
		}
	
		System.out.println(Main.ZoneDirection);
		
	}
}
