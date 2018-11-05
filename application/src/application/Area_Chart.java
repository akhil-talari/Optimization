package application;

import javafx.scene.Node;
import static application.Install.modinstallmonth;
import static application.GA.mapmodnames;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

//overall schedule Area rollup
public class  Area_Chart {

	public static ArrayList<areaStartend> list;
	public static Node run(int name, int HookupName, int duration){
		
		String name1 = "";
		
		if(HookupName==0){
			
			Set<String> area = Main.areas.keySet();
			list = new ArrayList<areaStartend>();
			
			for( String s: area){
				
				areaStartend a = new areaStartend(s);
				
				//get module install months that belongs to modules of a particular area
				ArrayList<Integer> modules = GA.ModsinArea.get(s);
				for(int i = 0;i<modules.size();i++){
					if(Install.modinstallmonth.get(modules.get(i))<a.start){
						a.start = Install.modinstallmonth.get(modules.get(i));
					}
				    if(Install.modinstallmonth.get(modules.get(i))+1>a.end){
						a.end = Install.modinstallmonth.get(modules.get(i))+1;
					}
				}
				System.out.println(s+" "+a.start+" "+a.end);
				list.add(a);
			}
			name1 = " Setting Schedule: "+ name;
			
		}
		else if(name==0){
			
			Set<String> area = Main.areas.keySet();
			list = new ArrayList<areaStartend>();
			
			for( String s: area){
				
				areaStartend a = new areaStartend(s);
				
				ArrayList<Integer> conns = GA.ConnsinArea.get(s);
				for(int i = 0;i<conns.size();i++){
					if(Hookup.connStartmonth.get(conns.get(i))<a.start){
						a.start = Hookup.connStartmonth.get(conns.get(i));
					}
					if(Hookup.connStartmonth.get(conns.get(i))+Hookup.connduration.get(conns.get(i))>a.end){
						a.end = Hookup.connStartmonth.get(conns.get(i))+Hookup.connduration.get(conns.get(i));
					}
				}

				list.add(a);
				
			}
			name1 = " Hookup Schedule: "+HookupName;
		}
		else{ 
			Set<String> area = Main.areas.keySet();
			list = new ArrayList<areaStartend>();

			//get install month of module and connection end month of connection that belong to a area.
			for( String s: area){

				areaStartend a = new areaStartend(s);

				//get module install months that belongs to modules of a particular area
				ArrayList<Integer> modules = GA.ModsinArea.get(s);
				for(int i = 0;i<modules.size();i++){
					if(Install.modinstallmonth.get(modules.get(i))<a.start){
						a.start = Install.modinstallmonth.get(modules.get(i));
					}
				}

				//get connection end month that belongs to a particular area
				ArrayList<Integer> conns = GA.ConnsinArea.get(s);
				for(int i = 0;i<conns.size();i++){
					if(Hookup.connStartmonth.get(conns.get(i))+Hookup.connduration.get(conns.get(i))>a.end){
						a.end = Hookup.connStartmonth.get(conns.get(i))+Hookup.connduration.get(conns.get(i));
					}
				}

				list.add(a);

			}
			name1 = " Overall Schedule:"+" Setting Scenario: "+ name+" Hookup Scenario: "+HookupName;
		}
		 return GanttChart.start_GanttChart(name1,duration);
		
	}
	
	
}
 class areaStartend{
	
	int start;
	int end;
	String Area = null;
	
	areaStartend(String area){
		
		Area = area;
		start=100;
		end =0;
	}
	
}
