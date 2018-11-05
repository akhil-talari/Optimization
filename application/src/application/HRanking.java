package application;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class HRanking {
	
	
	private ArrayList<Hookup_Chromosome>[] ranking_  ;
	
	HRanking(ArrayList<Hookup_Chromosome> union){
		
	
		 int [] dominateMe = new int[union.size()];

		    // iDominate[k] contains the list of solutions dominated by k
		    List<Integer> [] iDominate = new List[union.size()];

		    // front[i] contains the list of individuals belonging to the front i
		    List<Integer> [] front = new List[union.size()+1];
		        
		    
		    int flagDominate;    

		    // Initialize the fronts 
		    for (int i = 0; i < front.length; i++)
		      front[i] = new LinkedList<Integer>();
		        
		   
		    for (int p = 0; p < union.size(); p++) {
		    
		      iDominate[p] = new LinkedList<Integer>();
		      dominateMe[p] = 0;
		    }
		    for (int p = 0; p < (union.size()-1); p++) {
		      // For all q individuals , calculate if p dominates q or vice versa
		      for (int q = p+1; q < union.size(); q++) {
		        
		         flagDominate = compare(union.get(p),union.get(q));
		        
		        if (flagDominate == -1)
		        {
		          iDominate[p].add(q);
		          dominateMe[q]++;
		        }
		        else if (flagDominate == 1)
		        {
		          iDominate[q].add(p);
		          dominateMe[p]++;
		        }
		      }
		      // If nobody dominates p, p belongs to the first front
		    }
		    for (int p = 0; p < union.size(); p++) {
		      if (dominateMe[p] == 0) {
		        front[0].add(p);
		        union.get(p).rank = 0;
		      }
		    }    
		    
		    //Obtain the rest of fronts
		    int i = 0;
		    Iterator<Integer> it1, it2 ; // Iterators
		    while (front[i].size()!= 0) {
		      i++;
		      it1 = front[i-1].iterator();
		      while (it1.hasNext()) {
		        it2 = iDominate[it1.next()].iterator();
		        while (it2.hasNext()) {
		          int index = it2.next();
		          dominateMe[index]--;
		          if (dominateMe[index]==0) {
		            front[i].add(index);
		            union.get(index).rank = i;
		          }
		        }
		      }
		    }
 
		    ranking_ = new ArrayList[i];
		    //0,1,2,....,i-1 are front, then i fronts
		    for (int j = 0; j < i; j++) {
		      ranking_[j] = new ArrayList<Hookup_Chromosome>(front[j].size());
		      it1 = front[j].iterator();
		      while (it1.hasNext()) {
		                ranking_[j].add(union.get(it1.next()));
		      }
		    }
	}

	public static int compare(Hookup_Chromosome chromosome1, Hookup_Chromosome chromosome2) {

		    int dominate1=0 ;	                 
		    int dominate2=0 ; 
		    
		    int flag; //stores the result of the comparison

		    double value1, value2;
		    for (int i = 0; i < 2; i++) {
		      value1 = chromosome1.objectives[i];
		      value2 = chromosome2.objectives[i];
		      if (value1 < value2) {
		        flag = -1;
		      } else if (value1 > value2) {
		        flag = 1;
		      } else {
		        flag = 0;
		      }
		      
		      if (flag == -1) {
		        dominate1 = 1;
		      }
		      
		      if (flag == 1) {
		        dominate2 = 1;           
		      }
		    }
		            
		    if (dominate1 == dominate2) {            
		      return 0; //No one dominate the other
		    }
		    if (dominate1 == 1) {
		      return -1; // solution1 dominate
		    }
		    return 1;    // solution2 dominate   
		  
	}

	public ArrayList<Hookup_Chromosome> getSubfront(int rank) {
		
		return ranking_[rank];
		  
	}

}
