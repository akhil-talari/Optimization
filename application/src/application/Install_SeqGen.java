package application;

import java.util.ArrayList;
import java.util.Random;

public class Install_SeqGen {
	
	
	
	static ArrayList<Integer> middle_first_last(int begin, int end){

        Random random = new Random();
        int result = random.nextInt(end+1-begin) + begin;

        if(result == end){
            return last_to_First(begin, end);
        }
        else if(result == begin){
            return first_to_Last(begin , end);
        }
        else return middle(begin, end, result);
    }
	
	static ArrayList<Integer> last_to_First(int begin, int end){
		
		ArrayList<Integer> list = new ArrayList<Integer>();

		for(int i = end; i >= begin ; i--){
			list.add(i);
		}
		return list;
	}
	static ArrayList<Integer> first_to_Last(int begin, int end){
		
        ArrayList<Integer> list = new ArrayList<Integer>();

        for(int i = begin; i <= end  ; i++){
            list.add(i);
        }

        return list;
    }
	static ArrayList<Integer> middle(int begin, int end, int firstnum) {

		int numOfMachines = end-begin+1;

		ArrayList<Integer> solution = new ArrayList<Integer>();
		ArrayList<Integer> schedule = new ArrayList<Integer>();
		solution.add(firstnum);

		for(int i = 1; i< numOfMachines; i++){
			solution.add((int) Math.round( Math.random() ));
		}

		int startPosition = solution.get(0) ;
		schedule.add(startPosition);

		for (int i = 1; i < numOfMachines; i ++) {

			// 0 means take a module from left
			if (solution.get(i) == 0) {
				boolean foundOnLeft = false;
				for (int j = startPosition; j >=begin; j --) {
					if (!schedule.contains(j)) {
						schedule.add(j);
						foundOnLeft = true;
						break;
					}
				}
				if (foundOnLeft == false) {
					for (int j = startPosition; j <= end; j ++) {
						if (!schedule.contains(j)) {
							schedule.add(j);
							break;
						}
					}
				}
			}
			// 1 means take a module from right
			if (solution.get(i) == 1) {
				boolean foundOnRight = false;

				for (int j = startPosition; j <=end; j ++) {
					if (!schedule.contains(j)) {
						schedule.add(j);
						foundOnRight = true;
						break;
					}
				}

				if (foundOnRight == false) {
					for (int j = startPosition; j >=begin; j --) {
						if (!schedule.contains(j)) {
							schedule.add(j);
							break;
						}
					}
				}
			}
		}

		return schedule;

	}

}
