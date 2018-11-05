//Aim:To implement Kmeans clustering algorithm.
//Program
package application;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

import static application.Install.sequences_group;

import info.debatty.java.stringsimilarity.*;

class k_m
{
    static JaroWinkler jw = new JaroWinkler();
    static HashMap<Install_Chromosome, Double> sequences = new HashMap<>();
    static Multimap<Integer, Install_Chromosome> groups = ArrayListMultimap.create();
    static Multimap<Integer, Double> groups_plot = ArrayListMultimap.create();


    static Multimap<Double, Install_Chromosome> groups_get_string = ArrayListMultimap.create();

    static HashMap<Integer, String> itemmComp = new HashMap<>();

    static Set<String> checker;
    static double [] newArray;
    static Map<Integer, ArrayList<Integer>> listoflist = new LinkedHashMap<>();
    static ArrayList<Double> mean_list = new ArrayList<Double>();
    static int count1,count2,count3,count4,count5,count6,count7,count8,count9,count10;
    static double k[][];
    static double medianscore;
    static double tempk[][];
    static double m[];
    static double diff_mm[];
    static double diff[];
    static ArrayList<ms> msArrayList;

    static int iterations = 0;
    static int val = -1;
    static double sum_mean[];
    static int elements  = sequences_group.size(), clusters_size = 5;
    static String first ="";
    static String second ="";

    static int cal_diff(double a) // This method will determine the cluster in which an element go at a particular step.
    {
        val++;
        if (val > clusters_size-1 ){ val = 0;}
        return val;
    }

    static int cal_diff_2(double a) // This method will determine the cluster in which an element go at a particular step.
    {
        for(int i = 0; i< clusters_size; ++i) {
            diff_mm[i] = 0; // initializing means to 0
            diff[i] = 0;
        }
        int temp1=0;
        String item1 = "";
        Iterator<Map.Entry<Install_Chromosome, Double>> itr = sequences.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry<Install_Chromosome, Double> entry11 = itr.next();
            if (entry11.getValue().equals((a) )) {
                for(int s : entry11.getKey().sequence){
                    item1 += s + ","; //remove , last
                }
            }
        }

        for(int i = 0; i< clusters_size; ++i)
        {
//            System.out.println("item1"+ item1);
//            System.out.println("comp"+ i + " "+ itemmComp.get(i));
                double dd = jw.similarity(item1.replace("\\s+","").replace("[","").replace("]",""), itemmComp.get(i).replace("\\s+","").replace("[","").replace("]",""));
                diff[i]=dd;
            //System.out.println(diff[i]);
        }
        int val=0;
        double temp=diff[0];
        // System.out.println("diff 1"+diff[1]);
        for(int i = 0; i< clusters_size; ++i)
        {
            if(diff[i]<temp)
            {
                // System.out.println("--------------------------"+diff[i]);
                temp=diff[i];
                val=i;
            }
        }//end of for loop
        return val;
    }

    static void cal_median() // This method will determine intermediate mean values
    {
//        for(int i = 0; i< clusters_size; ++i)
//            m[i]=0; // initializing means to 0
//        for(int i = 0; i< clusters_size; ++i)
//        {
//            for(int j = 0; k[i][j]!=-1 && j< elements -1; ++j) //actually -1
//            {
//                medianscore [i][j] = 0;
//            }
//        }
        for(int i = 0; i< clusters_size; ++i)
        {
            msArrayList = new ArrayList<>();

            ArrayList<Double> store = new ArrayList<>();

            for(int j = 0; k[i][j]!=-1 && j< elements -1; ++j) //actually -1
            {
                String first = "";
                for (int jnext = j + 1; k[i][jnext] != -1 && jnext < elements - 1; ++jnext) //actually -1
                {

                    String second = "";
                    Iterator<Map.Entry<Install_Chromosome, Double>> itr1 = sequences.entrySet().iterator();
                    while (itr1.hasNext()) {
                        Map.Entry<Install_Chromosome, Double> entry11 = itr1.next();
                        if (entry11.getValue().equals((k[i][j]) )) {
                            for(int s : entry11.getKey().sequence){
                                first += s + ","; //remove , last
                            }
                        }
                        if (entry11.getValue().equals((k[i][jnext]) )) {
                            for(int s : entry11.getKey().sequence){
                                second += s + ","; //remove , last
                            }
                        }
                        //System.out.println(entry11.getKey()+" "+m[i]);
                    }
                    double d = jw.similarity(first.replace("\\s+", "").replace("[", "").replace("]", ""), second.replace("\\s+", "").replace("[", "").replace("]", ""));
                    medianscore += d;

                }
                ms elements = new ms(medianscore, first);
                msArrayList.add(elements);
            }

            Collections.sort(msArrayList, new Comparator<ms>() {
                @Override
                public int compare(ms o1, ms o2) {
                    if(o1.score < o2.score ){
                        return -1;
                    }
                    else if(o1.score > o2.score ){
                        return 1;
                    }
                    else return 0;

            }}
            );


            if(msArrayList.size() % 2 == 0){
                int p = msArrayList.size() / 2;
                itemmComp.put(i, msArrayList.get(p).instchromosome);

            }
            else{

                int p = (msArrayList.size() / 2) + 1;
                itemmComp.put(i, msArrayList.get(p).instchromosome);
            }
            //System.out.println("mean"+(i+1)+" "+m[i]);
        }
    }

    static void print_mean(){
        for(int i = 0; i< clusters_size; i++){
            mean_list.add(i, m[i]);
            System.out.println("K"+(i+1) + " Mean = "+m[i]);}
    }

    static int check1() // This checks if previous k ie. tempk and current k are same.Used as terminating case.
    {
        for(int i = 0; i< clusters_size; ++i)
            for(int j = 0; j< elements; ++j)
                if(tempk[i][j]!=k[i][j])
                {
                    return 0;
                }
        return 1;
    }

    public static void memmain()
    {
        double [] d = new double[elements];
        generateRandomArray(elements, d);

        System.out.println(d[1]);
        System.out.println("the number of elements "+elements);

        //d = new double[]{(double) newArray};
        System.out.println("the number of clusters: "+clusters_size);
        k=new double[clusters_size][elements];
        tempk=new double[clusters_size][elements];
        m=new double[clusters_size];
        diff_mm=new double[clusters_size];
        diff=new double[clusters_size];
        sum_mean = new double[elements];

//        for(int i = 0; i< clusters_size; ++i) {
//            m[i] = d[i];
//        }

        int temp=0;
        int flag=0;

        int cnt1 =0;

        do
        {
            for(int i = 0; i< clusters_size; ++i)
                for(int j = 0; j< elements; ++j)
                {
                    k[i][j]=-1;
                }

            for(int i = 0; i< elements; ++i) // for loop will cal cal_diff(int) for every element.
            {
                if(cnt1 == 0){
                    temp=cal_diff(d[i]);}
                else temp = cal_diff_2(d[i]);
                if(temp==0)
                    k[temp][count1++]=d[i];
                else
                if(temp==1)
                    k[temp][count2++]=d[i];
                else
                if(temp==2)
                    k[temp][count3++]=d[i];
                else
                if(temp==3)
                    k[temp][count4++]=d[i];
                else
                if(temp==4)
                    k[temp][count5++]=d[i];
                else
                if(temp==5)
                    k[temp][count6++]=d[i];
                else
                if(temp==6)
                    k[temp][count7++]=d[i];
                else
                if(temp==7)
                    k[temp][count8++]=d[i];
                else
                if(temp==8)
                    k[temp][count9++]=d[i];
                else
                if(temp==9)
                    k[temp][count10++]=d[i];
            }
            cnt1++;
            cal_median(); // call to method which will calculate mean at this step.

            flag=check1(); // check if terminating condition is satisfied.
            if(flag!=1)
                /*Take backup of k in tempk so that you can check for equivalence in next step*/
                for(int i = 0; i< clusters_size; ++i)
                    for(int j = 0; j< elements; ++j)
                        tempk[i][j]=k[i][j];
            for(int i = 0; i< clusters_size; ++i)
                count1=0;count2=0;count3=0;count4=0;count5=0;count6=0;count7=0;count8=0;count9=0;count10=0;

                iterations++;
        }
        while(iterations <=1);
//
//        }
//        while(flag==0);

        System.out.println("\n");
        for(int i = 0; i< clusters_size; ++i)
        {
            System.out.print("K"+(i+1)+"{ ");
            for(int j = 0; k[i][j]!=-1 && j< elements -1; ++j){
                System.out.print((k[i][j])+" ");}
            System.out.println("}");
        }

        //print_mean();

        System.out.println("\nThe Final Clusters By Kmeans are as follows: ");
        for(int i = 0; i< clusters_size; ++i)
        {
            System.out.print("K"+(i+1)+"{ ");
            for(int j = 0; k[i][j]!=-1 && j< elements -1; ++j){

                Iterator<Map.Entry<Install_Chromosome, Double>> itr = sequences.entrySet().iterator();
                while(itr.hasNext()){
                    Map.Entry<Install_Chromosome, Double> entry11 = itr.next();
                    if (entry11.getValue().equals((k[i][j])) ) {

                        groups_plot.put(i+1, entry11.getValue());
                        groups.put(i+1, entry11.getKey());
                        groups_get_string.put(entry11.getValue(), entry11.getKey());
                        System.out.print(entry11.getKey().sequence+" ");
                        itr.remove();
                    }
                }
            }
            System.out.println("}");
        }
        //System.out.println(groups_plot);

    }

    public static void generateRandomArray(int n, double[] d) {
//        List<Set<Integer>> nestedlist = new ArrayList<>();
//        Random r = new Random();
//        for (int l = 1; l <= n; l++) {
//            Set<Integer> uniqueNumbers = new LinkedHashSet<>();
//            while (uniqueNumbers.size() < boundary) {
//                uniqueNumbers.add(r.nextInt(boundary)+1);
//            }
//            nestedlist.add(uniqueNumbers);
//            //counter++;
//        }
//

        int ii = 0;
        for(Install_Chromosome seq: sequences_group){
            sequences.put(seq, (double) ii);
            ii++;
        }
        System.out.println("size----------"+sequences.size());

//        Map.Entry<String, Double> entry = sequences.entrySet().iterator().next();
//        String key= entry.getKey().replace("\\s+","").replace("[","").replace("]","");
//        System.out.println(key);


//        for (String seq : sequences.keySet()){
//
//            SequenceMatcher s = new SequenceMatcher("123", "123");
//
//            s.set_seqs(key, seq.replace("\\s+","").replace("[","").replace("]","").replace(",",""));
//            //s.set_seqs("1 2 3 4 5 6 7 8 9 10", "10 9 8 7 1 2 3 4 5 6");
//            sequences.put(seq, s.ratio());
//        }

//        for (String name: sequences.keySet()){
//
//            String key1 =name;
//            String value = sequences.get(name).toString();
//            //System.out.println(key1 + " " + value);
//        }

        for (Map.Entry<Install_Chromosome,Double> entry11 : sequences.entrySet()) {
            if (entry11.getValue().equals(1.0)) {
                System.out.println("999999999999999999999999");
                System.out.println(entry11.getKey().sequence);
            }
        }


        List<Double> values = new ArrayList<>();
        for (Map.Entry<Install_Chromosome,Double> entry1 : sequences.entrySet()) {
            Double val = entry1.getValue();
            values.add(val);
        }

        elements = values.size();
        for(int i = 0; i < values.size(); i++){
            d[i] = values.get(i);
            // System.out.println(d[i]);
        }

    }
}

class ms {
    double score;
    String instchromosome;

    public ms(double score, String instchromosome){
        this.score = score;
        this.instchromosome = instchromosome;
    }

}