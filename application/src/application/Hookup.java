package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Hookup {
	
	static Install_Chromosome current;
	static HashMap<Integer,Integer > connStartmonth;
    static HashMap<Integer,Integer > connduration;
    static HashMap<Integer, Integer> crewUsedpermonth;
    static HashMap<String, ArrayList<Integer>> Hchromofobjs;
    static ArrayList<int[]> objsfor2D;
    static ArrayList<Unit> units;
    
	public static void initial() {	
		
		ArrayList<Hookup_Chromosome> current_population = new ArrayList<Hookup_Chromosome>();
		
		current_population = createChromosomes(GA.Hookup_PopSize);
		
		Hchromofobjs = new HashMap<String, ArrayList<Integer>>();

		Scatter2DChart sc = new Scatter2DChart();
		sc.start();
		sc.run(0,objsfor2D);
		while(GA.Hookup_gen <= GA.max_Hookup_gen){

			ArrayList<Hookup_Chromosome> offspring_population = new ArrayList<Hookup_Chromosome>();
			offspring_population=GALoop(current_population);

			ArrayList<Hookup_Chromosome> union = new ArrayList<Hookup_Chromosome>();
			union.addAll(current_population);
			union.addAll(offspring_population);

			ArrayList<Hookup_Chromosome> new_population = new ArrayList<Hookup_Chromosome>();
			new_population = newPopAfterRanking(GA.Hookup_PopSize,union);
			sc.run(GA.Hookup_gen,objsfor2D);
			current_population = new_population;

			GA.Hookup_gen = GA.Hookup_gen+1;
		}
	/*	HRanking ranking = new HRanking(current_population);
		ranking.getSubfront(0);*/
	}

	private static ArrayList<Hookup_Chromosome> newPopAfterRanking(int hookup_PopSize, ArrayList<Hookup_Chromosome> union) {
		
		objsfor2D = new ArrayList<int[]>();
		ArrayList<Hookup_Chromosome> new_population = new ArrayList<Hookup_Chromosome>();

		HRanking ranking = new HRanking(union);

		int remain = hookup_PopSize;
		int index = 0;
		ArrayList<Hookup_Chromosome> front = null;

		// Obtain the next front
		front = ranking.getSubfront(index);

		while ((remain > 0) && (remain >= front.size())) {

			//Add the individuals of this front
			for (int k = 0; k < front.size(); k++) {
				new_population.add(front.get(k));
				int[] point = new int[2];
				point[0] = front.get(k).objectives[0];
				point[1] = front.get(k).objectives[1];
				objsfor2D.add(point);
				Hchromofobjs.put(point[0]+" "+point[1],front.get(k).sequence);
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
				int[] point = new int[2];
				point[0] = front.get(k).objectives[0];
				point[1] = front.get(k).objectives[1];
				objsfor2D.add(point);
				Hchromofobjs.put(point[0]+" "+point[1],front.get(k).sequence);
			} 

			remain = 0;
		}
		return new_population; 
	}

	private static ArrayList<Hookup_Chromosome> GALoop(ArrayList<Hookup_Chromosome> current_population) {
		
		Random r=new Random(System.currentTimeMillis());

		ArrayList<Hookup_Chromosome> offspring_population = new ArrayList<Hookup_Chromosome>();

		for(int i=0;i<current_population.size();i++){

			offspring_population.add(null);
		}
		for(int i=0;i<offspring_population.size();i++){

			Hookup_Chromosome ofSpring = evolvePopulation(current_population,r);
			if(ofSpring!=null){
				offspring_population.set(i,ofSpring);
			}else{
				i=i-1;
			}
			
		}
		return offspring_population;
	}

	private static Hookup_Chromosome evolvePopulation(ArrayList<Hookup_Chromosome> current_population, Random r) {
		
		int tournamentSize = 2;
		double crossoverRate = GA.Hookup_CR;
		double mutationRate = GA.Hookup_MR;
		
		ArrayList<Integer> tour1 = new ArrayList<Integer>();
		ArrayList<Integer> tour2 = new ArrayList<Integer>();
		
		tour1 = tournamentSelection(current_population,tournamentSize,r);
		tour2 = tournamentSelection(current_population,tournamentSize,r);
		
		ArrayList<Integer> offspring = new ArrayList<Integer>();
	
		offspring = crossOver(crossoverRate,tour1,tour2,r);
		offspring = mutate(offspring,mutationRate,r);
		
		Hookup_Chromosome new_offspring = new Hookup_Chromosome(offspring);
		
		boolean Chromisgood = evaluate_objs(new_offspring);
		
		if(Chromisgood){
			
			return new_offspring;
			
		}
		return null;
		
		
	}

	private static ArrayList<Integer> mutate(ArrayList<Integer> offspring,double mutationRate, Random r) {
		
		for (int swapPos1 = 0; swapPos1 < offspring.size(); swapPos1++) {
			float f = r.nextFloat();
			if (f < mutationRate) {

				int swapPos2 = r.nextInt(offspring.size());
				int swapCity1 = offspring.get(swapPos1);
				int swapCity2 = offspring.get(swapPos2);
				offspring.set(swapPos1, swapCity2);
				offspring.set(swapPos2, swapCity1);

			}
		}
		return offspring;
	}

	private static ArrayList<Integer> crossOver(double crossoverRate,ArrayList<Integer> tour1, ArrayList<Integer> tour2, Random r) {
		
		ArrayList<Integer> offspring=new ArrayList<Integer>();

		for(int i=0;i<tour1.size();i++){
			offspring.add(null);
		}

		float f = r.nextFloat();
		if (f < crossoverRate) {
			int crossPoint = (int) (Math.random() * tour1.size());//make a crossover point
			for (int i = 0; i < tour1.size(); ++i) {
				if (i < crossPoint)
					offspring.set(i, tour1.get(i));
				else
					offspring.set(i, tour2.get(i));
			}
		} else {
			offspring.clear();
			offspring.addAll(tour1);
		}

		return offspring;
	}

	private static ArrayList<Integer> tournamentSelection(ArrayList<Hookup_Chromosome> current_population,int tournamentSize, Random r) {
		
		Hookup_Chromosome solution1, solution2;
	    solution1 = current_population.get(r.nextInt(current_population.size()));
	    solution2 = current_population.get(r.nextInt(current_population.size()));

	    if (current_population.size() >= 2)
	    	while (solution1 == solution2)
	        solution2 = current_population.get(r.nextInt(current_population.size()));
	    
	    int flag = HRanking.compare(solution1,solution2);
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

	private static ArrayList<Hookup_Chromosome> createChromosomes(int hookup_PopSize) {
		
		objsfor2D = new ArrayList<int[]>();
		Random r = new Random();
		
		ArrayList<Hookup_Chromosome> population = new ArrayList<Hookup_Chromosome>();
        
		for (int j = 0; j < GA.Hookup_PopSize; j++) {
            population.add(null);
        }
		for (int i = 0; i < hookup_PopSize; i++) {
			
			ArrayList<Integer> list = new ArrayList<Integer>();

			for (int j = 0; j < GA.connections; j++) {

				list.add(r.nextInt(GA.Hookup_MCrew)+1);
			}
			Hookup_Chromosome newChrom = new Hookup_Chromosome(list);
			boolean Chromisgood = evaluate_objs(newChrom);
			
			if(Chromisgood){
				
				population.set(i, newChrom);
				int[] point = new int[2];
				point[0] = newChrom.objectives[0];
				point[1] = newChrom.objectives[1];
				objsfor2D.add(point);
				
			}else{
				
				i=i-1;
			}
			
		}
		return population;
	}

	private static boolean evaluate_objs(Hookup_Chromosome thisChrom) {
		
		int obj1 = 0,obj2 = 0;
		
		ArrayList<Unit> units1 = Hookup_getSchedule(thisChrom.sequence);
		
		obj1 = getTotalMonth();
		obj2 = getResourceVariation();
		
		//System.out.println(obj1+" "+obj2);
		thisChrom.objectives[0] = obj1;
		thisChrom.objectives[1] = obj2;
		
		Set<String> area = Main.areas.keySet();
		ArrayList<areaEndDate> list = new ArrayList<areaEndDate>();

		//get install month of module and connection end month of connection that belong to a area.
		for( String s: area){
		//	if(!s.equals("Utilities")){
				areaEndDate a = new areaEndDate(s);

				//get connection end month that belongs to a particular area
				ArrayList<Integer> conns = GA.ConnsinArea.get(s);
				for(int i = 0;i<conns.size();i++){
					if(Hookup.connStartmonth.get(conns.get(i))+Hookup.connduration.get(conns.get(i))>a.end){
						a.end = Hookup.connStartmonth.get(conns.get(i))+Hookup.connduration.get(conns.get(i))-1;
					}
				}
				list.add(a);

				if(a.end > GA.AreaMilestones.get(s)){
					return false;
				}
			
	//		}

		}
		
		//if any of the constraint fails
		
		return true;
	}

	private static int getResourceVariation() {
		
		//Unit[] units = Hookup_getSchedule(sequence);
		int totalCrewVariation = 0;
		int previousTotalCrew = 0;
		int totalMonths = 0;

		for (int i = 0; i < units.size(); i++) {
			Unit unit = units.get(i);

			int totalCrews = 0;
			for (Integer machine : unit.machines) {
				totalCrews += unit.crewOfMachine.get(machine);
			}

			totalCrewVariation += Math.abs(totalCrews - previousTotalCrew)*Math.abs(totalCrews - previousTotalCrew);
			previousTotalCrew = totalCrews;
		}

		totalCrewVariation += previousTotalCrew;
		return totalCrewVariation;
	}

	private static int getTotalMonth() {
		
		//Unit[] units = Hookup_getSchedule(sequence);

		int totalMonths = 0;

		for (int i = 0; i < units.size(); i++) {
			Unit unit = units.get(i);

			int totalCrews = 0;
			for (Integer machine : unit.machines) {
				totalCrews += unit.crewOfMachine.get(machine);
			}
			if (totalCrews > 0) {
				totalMonths = i+29 ;
			}
		}
		return totalMonths;

	}
	public static ArrayList<Unit> Hookup_getSchedule(ArrayList solution) {

		Unit[] u  = Install.getFullSchedule(current.sequence);
		
		connStartmonth = new HashMap<Integer, Integer>();
		crewUsedpermonth = new HashMap<Integer, Integer>();
		connduration = new HashMap<Integer,Integer>();

		//Unit[] units = new Unit[50];
		 units = new ArrayList<Unit>();

		boolean[] connectionUsed = new boolean[GA.connections];
		// Loop through all the months
		int i=0;
		while(true) {

			if(!crewUsedpermonth.containsKey(i+29)){
				crewUsedpermonth.put(i+29,0);
			}
			try{
				Unit unit = units.get(i);
			}
			catch(IndexOutOfBoundsException e){
				units.add(new Unit());
				Unit unit = units.get(i);
			}

			boolean allMachineAllocated = true;
			int totalCrew = getCrewLimitForTheMonth(i+29);


			// Sanity check
			for (int k = 0; k < GA.connections; k++) {

				if (connectionUsed[k] == false) {
					allMachineAllocated = false;
				}
			}
			if (allMachineAllocated) break;

			int count = 0;

			while (true) {

				count++;
				if (units.get(i).crewUsed >= totalCrew) break;
				// Get the next available connection
				//System.out.println("Month"+i);
				Integer connectionToAllocate = giveNextAvailableConnection(i+29, connectionUsed);
				
				

				int g = connectionToAllocate + 1;

				if (connectionToAllocate == -1) break;

				if (count > 50) {
					break;
				}

				if (!connectionUsed[connectionToAllocate]) {
					// Get number of crews for connection from chromosome
					int crew = getCrewForConnection(solution, connectionToAllocate);
					int duration = getDuration(connectionToAllocate + 1, crew);

					try {
						// ********** Important ***********
						// Check if future months exceed crew limit
						// IF this connection even in future exceeds total crew limit, then skip
						//*********************************
						//
						boolean noMonthsExceedsCrewLimit = true;
						for (int j = i; j < i + duration; j++) {
							
							try{
								Unit unit1 = units.get(j);
							}
							catch(IndexOutOfBoundsException e){
								
								units.add(new Unit());
							}

							if (units.get(j).crewUsed + crew > getCrewLimitForTheMonth(j+29)) {//check if for the following months is it okay to add crews for the current connection

								noMonthsExceedsCrewLimit = false;
								break;
							}
						}
						if (noMonthsExceedsCrewLimit == false) break;

						if (noMonthsExceedsCrewLimit) {

							connectionUsed[connectionToAllocate] = true; //now good to hookup this m/c
							int conn_duration =0;
							for (int j = i; j < i + duration; j++) {

								if (units.get(j).crewUsed + crew > totalCrew) {

									System.out.println("WTH!!!!!!");
								}
								units.get(j).machines.add(connectionToAllocate+1);
								units.get(j).crewOfMachine.put(connectionToAllocate+1, crew);
								units.get(j).crewUsed += crew;
								crewUsedpermonth.put(j+29, units.get(j).crewUsed);
								
								if(!connStartmonth.containsKey(connectionToAllocate+1)){
									connStartmonth.put(connectionToAllocate+1, j+29);
								}
								connduration.put(connectionToAllocate+1, ++conn_duration);
							}
						}
					} catch (Exception ex) {
					
						System.out.println("HERE: " + duration);
					}
				}

				allMachineAllocated = true;
				for (int k = 0; k < GA.connections; k++) {
					if (connectionUsed[k] == false) {
						allMachineAllocated = false; //just to check if every m/c is used or not
					}
				}

				if (allMachineAllocated) break;
			}
			i++;

		}
		
		return units;

	}

	public static int getCrewForConnection(ArrayList solution, Integer connectionToAllocate) {

		for (int i = 0; i < solution.size(); i++) {
			if (connectionToAllocate == i)
				return (int) solution.get(i);
		}
		return -1;
	}
	private static int getCrewLimitForTheMonth(int i) {
		return GA.Hookup_MCrewPM;
	}
	private static Integer giveNextAvailableConnection(int month, boolean[] connectionUsed) {

		List<Integer> availMacs = getMachinesforMonth(month);
		for (int k = 0; k < availMacs.size(); k++) {
			if (connectionUsed[availMacs.get(k) - 1] == false) return availMacs.get(k) - 1;
		}
		return -1;
	}
	private static List<Integer> getMachinesforMonth(int month) {

		ArrayList<Integer> connections = new ArrayList<Integer>();
		
		for (int j = 1; j <= GA.connections; j++) {

			if (getMonthForConnection(j) <= month) {

				connections.add(j);
			}
		}
		
		return connections;
	}
	private static int getMonthForConnection(int conn) {
		
		int month =0;
		ArrayList<Integer> conn_modules = new ArrayList<Integer>();
		conn_modules = GA.connectionMap.get(conn);
		int module1 = GA.mapmodnums.get(conn_modules.get(0));
		int module2 = GA.mapmodnums.get(conn_modules.get(1));
		//System.out.println(module1+" "+module2);

		//System.out.println(conn+"->"+Math.max(getmodInstallMonth(conn_modules.get(0)), getmodInstallMonth(conn_modules.get(1))));
		try{
			month =  Math.max(Install.modinstallmonth.get(module1),Install.modinstallmonth.get(module2));
		}
		catch(NullPointerException e){
			
			System.out.println(module1+" "+module2);
			System.out.println(Install.modinstallmonth);
			System.out.println(Install.modinstallmonth.get(module1));
			System.out.println(Install.modinstallmonth.get(module2));

			System.exit(0);
		}
		return month;
	}

	private static double MetricTon(int id) {
		
		ArrayList<Integer> conn_modules = new ArrayList<Integer>();
		conn_modules = GA.connectionMap.get(id);
		int module1 = GA.mapmodnums.get(conn_modules.get(0));
		int module2 = GA.mapmodnums.get(conn_modules.get(1));
		
		
		return Double.parseDouble(GA.mapmodnames.get(module1).weight_mt)+Double.parseDouble(GA.mapmodnames.get(module2).weight_mt);
	}

	// connectionId starts from 1
	public static int getDuration(int connectionId, int crew) {

		int months = (int) Math.ceil((MetricTon(connectionId)*GA.Manhours)/(GA.NoofWorkers * crew * GA.NoofHours*30.4));
		//System.out.println(months);
		return months;
	}



}
class areaEndDate{
	

	int end;
	String Area = null;
	
	areaEndDate(String area){
		
		Area = area;
		end =0;
	}
}
