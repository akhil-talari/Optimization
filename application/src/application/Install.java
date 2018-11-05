package application;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static application.Main.Install_SPMT;

public class Install {

	static Set<Install_Chromosome> sequences_group = new HashSet<Install_Chromosome>();
	static HashMap<Integer,Integer > modinstallmonth;
    static HashMap<Integer, Integer> SPMTUsedpermonth;
    static HashMap<Integer, Integer> StagingAreaPerMonth;
    static HashMap<Integer,ArrayList<String>> CranesPerMonth;
    static HashMap<String, ArrayList<Integer>> chromofobjs;
    static ArrayList<int[]> objsfor3D;
    static int flag=0;
    
	
	Install(int flag1){
		this.flag = flag1;
		initial();
	}

	private void initial() {
		
		ArrayList<Install_Chromosome> current_population = new ArrayList<Install_Chromosome>();
		
		chromofobjs = new HashMap<String, ArrayList<Integer>>();
		current_population = createChromosomes(GA.Install_PopSize);
		
		ScatterPlot3D app = new ScatterPlot3D("ScatterPlot3D.java");
		app.pack();
		app.setVisible(true);
		
		while(GA.Install_gen<=GA.max_Install_gen){
			
			ArrayList<Install_Chromosome> offspring_population = new ArrayList<Install_Chromosome>();
			offspring_population=GALoop(current_population);
			
			ArrayList<Install_Chromosome> union = new ArrayList<Install_Chromosome>();
            union.addAll(current_population);
            union.addAll(offspring_population);
           
            ArrayList<Install_Chromosome> new_population = new ArrayList<Install_Chromosome>();
            new_population = newPopAfterRanking(GA.Install_PopSize,union);

			for(int i = 0; i< new_population.size(); i++){
				sequences_group.add(new_population.get(i));
			}

            app.createSeries("gen: "+GA.Install_gen);
            current_population = new_population;
            
            GA.Install_gen = GA.Install_gen+1;
		}

		ScatterChartExample sce = new ScatterChartExample();
		sce.start();

		Ranking ranking = new Ranking(current_population);
		//Hookup.current = ranking.getSubfront(0).get(0);
		
		//Unit[] units = getFullSchedule(Hookup.current.sequence);
		//System.out.println(modinstallmonth);
		
		//Hookup.initial();
	}

	private ArrayList<Install_Chromosome> newPopAfterRanking(int populationSize, ArrayList<Install_Chromosome> union) {
		
		objsfor3D = new ArrayList<int[]>();
		ArrayList<Install_Chromosome> new_population = new ArrayList<Install_Chromosome>();

		Ranking ranking = new Ranking(union);

		int remain = populationSize;
		int index = 0;
		ArrayList<Install_Chromosome> front = null;

		// Obtain the next front
		front = ranking.getSubfront(index);

		while ((remain > 0) && (remain >= front.size())) {

			//Add the individuals of this front
			for (int k = 0; k < front.size(); k++) {
				new_population.add(front.get(k));
				int[] point = new int[3];
				point[0] = front.get(k).objectives[0];
				point[1] = front.get(k).objectives[1];
				point[2] = front.get(k).objectives[2];
				objsfor3D.add(point);
				chromofobjs.put(point[0]+" "+point[1]+" "+point[2],front.get(k).sequence);
			} 

			remain = remain - front.size();

			index++;
			if (remain > 0) {
				front = ranking.getSubfront(index);
			}       
		} 

		if (remain > 0) {                       

			for (int k = 0; k < remain; k++) {
				new_population.add(front.get(k));
				int[] point = new int[3];
				point[0] = front.get(k).objectives[0];
				point[1] = front.get(k).objectives[1];
				point[2] = front.get(k).objectives[2];
				objsfor3D.add(point);
				chromofobjs.put(point[0]+" "+point[1]+" "+point[2],front.get(k).sequence);
			} 

			remain = 0;
		}
		return new_population;  
	}

	private ArrayList<Install_Chromosome> GALoop(ArrayList<Install_Chromosome> current_population) {
		
		Random r=new Random(System.currentTimeMillis());
		
		ArrayList<Install_Chromosome> offspring_population = new ArrayList<Install_Chromosome>();
		
		for(int i=0;i<current_population.size();i++){
			
			offspring_population.add(null);
		}
		for(int i=0;i<offspring_population.size();i++){
			
			offspring_population.set(i,evolvePopulation(current_population,r));		
		}
		return offspring_population;
	}

	private Install_Chromosome evolvePopulation(ArrayList<Install_Chromosome> current_population, Random r) {
		
		int tournamentSize = 2;
		double crossoverRate = GA.Install_CR;
		double mutationRate = GA.Install_MR;
		
		ArrayList<Integer> tour1 = new ArrayList<Integer>();
		ArrayList<Integer> tour2 = new ArrayList<Integer>();
		
		tour1 = tournamentSelection(current_population,tournamentSize,r);
		tour2 = tournamentSelection(current_population,tournamentSize,r);
		
		ArrayList<Integer> offspring = new ArrayList<Integer>();
	
		offspring = crossOver(crossoverRate,tour1,tour2,r);
		offspring = mutate(offspring,mutationRate,r);
		
		Install_Chromosome new_offspring = new Install_Chromosome(offspring);
		evaluate_objs(new_offspring);
		
		return new_offspring;
	}

	private ArrayList<Integer> mutate(ArrayList<Integer> offspring,double mutationRate, Random r) {
		
		float f=r.nextFloat();
		if(f<mutationRate)
		{
			for(int z=0;z<GA.modsinzone.size();z++){
				
				String direction = Main.ZoneDirection.get(z+1);
				if(direction.equals("Random")){
				
					if(GA.modsinzone.get(z).size()!=0 && GA.modsinzone.get(z).size()!=1){
						int swapPos1 = GA.modsinzone.get(z).get(0)-1;
						int swapPos2=GA.modsinzone.get(z).get(0);
						int swapCity1=offspring.get(swapPos1);
						int swapCity2=offspring.get(swapPos2);
						offspring.set(swapPos1, swapCity2);
						offspring.set(swapPos2, swapCity1);
					}
				}
			}
		}
		return offspring;
	}

	private ArrayList<Integer> crossOver(double crossoverRate,ArrayList<Integer> tour1,ArrayList<Integer> tour2, Random r) {
		
		ArrayList<Integer> offspring=new ArrayList<Integer>();

		for(int i=0;i<tour1.size();i++){
			offspring.add(null);
		}

		float f=r.nextFloat();
		if(f<crossoverRate) {
			
			int startPos = -1;
			int endPos = GA.modsinzone.get(0).size(); 
			
			int z=1;
			while(!Main.ZoneDirection.get(z).equals("Random")){
				
				z++;
			}
			if(GA.modsinzone.get(z).size()!=0){
				
				startPos = GA.modsinzone.get(z).get(0)-2;
				endPos = GA.modsinzone.get(z).size()-1+GA.modsinzone.get(z).get(0); 
			}

			for (int i = 0; i < offspring.size(); i++) {
				if(i > startPos && i < endPos) {
					offspring.set(i, tour1.get(i));
				}else{
					offspring.set(i,tour2.get(i));
				}
			}
			
		}
		else {
			offspring.clear();
			offspring.addAll(tour1);
		}
		return offspring;
	}

	private ArrayList<Integer> tournamentSelection(ArrayList<Install_Chromosome> current_population, int tournamentSize, Random r) {
		
		Install_Chromosome solution1, solution2;
	    solution1 = current_population.get(r.nextInt(current_population.size()));
	    solution2 = current_population.get(r.nextInt(current_population.size()));

	    if (current_population.size() >= 2)
	    	while (solution1 == solution2)
	        solution2 = current_population.get(r.nextInt(current_population.size()));
	    
	    int flag = Ranking.compare(solution1,solution2);
	    if (flag == -1)
	      return solution1.sequence;
	    else if (flag == 1)
	      return solution2.sequence;
	    else
	      if (r.nextDouble()<0.5)
	        return solution1.sequence;
	      else
	        return solution2.sequence;
	}

	private ArrayList<Install_Chromosome> createChromosomes(int populationSize) {
		
		objsfor3D = new ArrayList<int[]>();
		ArrayList<Install_Chromosome> population = new ArrayList<Install_Chromosome>();

		for(int i=0;i<populationSize;i++)
		{
			population.add(null);
			
			ArrayList<Integer> solutionX = new ArrayList<Integer>();  

			for(int z=0;z<GA.modsinzone.size();z++){

				ArrayList<Integer> list = new ArrayList<Integer>();

				if(GA.modsinzone.get(z).size()!=0 ){
					
					String direction = Main.ZoneDirection.get(z+1);
					if(direction.equals("Left")){
						list = Install_SeqGen.first_to_Last(GA.modsinzone.get(z).get(0), GA.modsinzone.get(z).get(GA.modsinzone.get(z).size()-1));		
					}else if(direction.equals("Right")){
						list = Install_SeqGen.last_to_First(GA.modsinzone.get(z).get(0), GA.modsinzone.get(z).get(GA.modsinzone.get(z).size()-1));
					}else{
						list = Install_SeqGen.middle_first_last(GA.modsinzone.get(z).get(0), GA.modsinzone.get(z).get(GA.modsinzone.get(z).size()-1));
					}
					solutionX.addAll(list);
				}	
			}

			Install_Chromosome newchrom = new Install_Chromosome(solutionX);
			evaluate_objs(newchrom);
			population.set(i,newchrom);
			int[] point = new int[3];
			point[0] = newchrom.objectives[0];
			point[1] = newchrom.objectives[1];
			point[2] = newchrom.objectives[2];
			objsfor3D.add(point);
			chromofobjs.put(point[0]+" "+point[1]+" "+point[2],newchrom.sequence);
		}
		return population;	
	}

	public void evaluate_objs(Install_Chromosome thisChrom) {
		
		int obj1 = 0,obj2 = 0, obj3 = 0;
		
		obj1 = getTotalMonth(thisChrom.sequence);
		obj2 = getResourceVariation(thisChrom.sequence);
		obj3 = getTotalStorage(thisChrom.sequence);
		
		thisChrom.objectives[0] = obj1;
		thisChrom.objectives[1] = obj2;
		thisChrom.objectives[2] = obj3;
	}

	public static int getTotalStorage(ArrayList<Integer> solution) {
		
		StagingAreaPerMonth = new HashMap<Integer, Integer>();
		
		Unit[] units  = getFullSchedule(solution);

		int totalStorage = 0;

		for (int module = 1; module <= Main.module.size(); module++) {
			
			int arrivalMonth = 29;
			if(flag==0){
				 arrivalMonth = getModuleArrivalMonth(module);
			}
			int installMonth = arrivalMonth;
			
			

			
			for (int i = arrivalMonth-29; i < units.length; i++) {
				
				if(!(units[i].machines.contains(module)) ){
					
					units[i].storage += getArea(module); 
					
				}else{  

					installMonth = i+29;
					break;
				}
			}
			totalStorage += (installMonth - arrivalMonth) * getArea(module);
			
			
		}
		for(int i=0; i<units.length;i++){
			
			StagingAreaPerMonth.put(i+29,0);
			if(units[i].storage>0){
				StagingAreaPerMonth.put(i+29,units[i].storage);
			}
		}
		
		
		return totalStorage;
	}

	private int getResourceVariation(ArrayList<Integer> solution) {
		
		Unit[] units  = getFullSchedule(solution);

		int totalCrewVariation = 0;
		int previousTotalCrew = 0;

		for (int i = 0; i < 55; i++) {
			
			Unit unit = units[i];
			int totalCrews = unit.crewUsed;
			totalCrewVariation += Math.abs(totalCrews - previousTotalCrew)*Math.abs(totalCrews - previousTotalCrew);
			previousTotalCrew = totalCrews;
		}
		return totalCrewVariation;
	}

	private int getTotalMonth(ArrayList<Integer> solution) {
		
		Unit[] units  = getFullSchedule(solution);

		int totalMonths = 0;

		for (int i = 0; i < 55; i++) {
			Unit unit = units[i];

			if (unit.crewUsed > 0) {
				totalMonths = i+29;
			}
		}
		return totalMonths;
	}
	public static Unit[] getFullSchedule(ArrayList<Integer> solution) {
		
			modinstallmonth = new HashMap<Integer, Integer>();
			SPMTUsedpermonth = new HashMap<Integer, Integer>();
			CranesPerMonth = new HashMap<Integer,ArrayList<String>>();

	        Set<Integer> alreadyInstalled = new HashSet<Integer>();

	        Unit[] units = new Unit[55];
	        for (int i = 0; i < 55; i ++) {
	            units[i] = new Unit();
	            SPMTUsedpermonth.put(i+29, 0);
	        }

	      //  Queue<Integer> queue = new LinkedList<>();

	        // Looping based on month
	        for (int i = 0; i < 55; i ++) {
	        	
	        	ArrayList<Integer> availableModules;
	        	if(flag==0){
	        		 availableModules = getModulesForMonth(i+29);
	        	}else{
	        		 availableModules = getAllModules();
	        	}
	            //shuffleArray(availableModules, solution.getVariableValue(54));
	            // Iterating through the available modules and checking if the pre-requisites are installed already
	            for (int index = 0; index < availableModules.size(); index++) {
	                // Get next available machine to install
	                int module = availableModules.get(index);
	                boolean isFine = true;
	                if (alreadyInstalled.contains(module)) {
	                    isFine = false;
	                }
	             // check if available machine in zone 1
	                for(int z=0;z<GA.modsinzone.size();z++){
	                	
	                	int from = GA.modsinzone.get(z).get(0)-1;
	                	int to = GA.modsinzone.get(z).get(0)+ GA.modsinzone.get(z).size()-1;
	                	ArrayList<Integer> zone = null;
	                	try{
	                	 zone = new ArrayList<Integer>(solution.subList(from,to));
	                	 
	                	}catch(Exception e){
	                		zone = new ArrayList<Integer>(solution.subList(from,to-1));
	                		
	                		System.out.println(solution.size());
	                		System.out.println(GA.modsinzone.get(z));
	                		System.out.println(zone);
	                		System.exit(0);
	                	}
	                	
	                	if(GA.modsinzone.get(z).contains(module)){
	                		 for (Integer mac: zone) {
	                			 if (mac == module ) break;
	                             if (!alreadyInstalled.contains(mac)) isFine = false;
	                		 }
	                		break;
	                	}
	                }
	           
	                if (isFine) {
	                	
	                	
	                    if (units[i].crewUsed + getSPMT(module) <= getSPMTForMonth(i+29)&& units[i].NoOfInstallations+1<=15) {
	                        units[i].machines.add(module);
	                        String cranecapacity = getCraneCapacity(module);
	                        units[i].cranes.add(cranecapacity);
	                        
	                        modinstallmonth.put(module,i+29);
	            
	                        units[i].crewUsed += getSPMT(module);
	                        units[i].NoOfInstallations += 1;
	                       
	                        //line chart use spmt per month
							SPMTUsedpermonth.put(i+29,units[i].crewUsed);
							//bar chart for cranes
							CranesPerMonth.put(i+29,units[i].cranes);
	                        alreadyInstalled.add(module);
	                    } 
	                }

	            }
	            
	        }
	        return units;
	    }

	private static ArrayList<Integer> getAllModules() {
		// TODO Auto-generated method stub
		ArrayList<Integer> mods = new ArrayList<Integer>();

		for(int m=1;m<=Main.module.size();m++){	

				mods.add(m);
		}
		return mods;
	}

	private static String getCraneCapacity(int module) {


		return GA.mapmodnames.get(module).crane;
	}

	private static ArrayList<Integer> getModulesForMonth(int month) {

		ArrayList<Integer> mods = new ArrayList<Integer>();

		for(int m=1;m<=Main.module.size();m++){

			if((int)(Double.parseDouble(GA.mapmodnames.get(m).ROS))<=month){

				mods.add(m);

			}
		}
		return mods;
	}

	public static int getSPMT(int module) {

		if(GA.mapmodnames.get(module).axe.equals("NA")){

			return 0;
		}
		else{

			return (int)(Double.parseDouble(GA.mapmodnames.get(module).axe));
		}	
	}

	private static int getSPMTForMonth(int i) {

		return GA.Install_SPMT;
	}
	private static double getArea(int module) {

		MyObject module1 = GA.mapmodnames.get(module);

		double Area = Double.parseDouble(module1.width) * Double.parseDouble(module1.length);

		return Area;
	}
	private static int getModuleArrivalMonth(int module) {

		return (int)(Double.parseDouble(GA.mapmodnames.get(module).ROS));
	}


}
