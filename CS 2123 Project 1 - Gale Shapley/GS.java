/*
 * CS2123 Project 1: The Variants of Gale-Shapley Algorithm
 */
import java.util.*;


public class GS {

	public static void main(String[] args) {
		String[] menlist = {"xavier", "yancey", "zeus"};
		String[] womenlist = {"amy", "bertha", "clare"};
		
		HashMap<String, String[]> thepref = new HashMap<>(); //for standard GS algorithm input: man, women in order
		String[][] names = {
				{"amy", "bertha", "clare"}, //xavier
				{"bertha", "amy", "clare"}, //yancey
				{"amy", "bertha", "clare"}, //zeus
				{"yancey", "xavier", "zeus"}, //amy
				{"xavier", "yancey", "zeus"}, //bertha
				{"xavier", "yancey", "zeus"} //clare
		};
		thepref.put("xavier", names[0]);
		thepref.put("yancey", names[1]);
		thepref.put("zeus", names[2]);
		thepref.put("amy", names[3]);
		thepref.put("bertha", names[4]);
		thepref.put("clare", names[5]);
		
		String[][] blocked = { //for blocked GS algorithm
				{"xavier", "clare"}, {"zeus", "clare"}, {"zeus", "amy"}
		};
		
		HashMap<String, String[][]> thepreftie = new HashMap<>(); //for GS algorithm dealing with ties
		String[][][] names2 = {
				{{"bertha"}, {"amy"}, {"clare"}},
				{{"amy", "bertha"}, {"clare"}},
				{{"amy"}, {"bertha", "clare"}},
				{{"zeus", "xavier", "yancey"}},
				{{"zeus"}, {"xavier"}, {"yancey"}},
				{{"xavier", "yancey"}, {"zeus"}}
		};
		thepreftie.put("xavier", names2[0]);
		thepreftie.put("yancey", names2[1]);
		thepreftie.put("zeus", names2[2]);
		thepreftie.put("amy", names2[3]);
		thepreftie.put("bertha", names2[4]);
		thepreftie.put("clare", names2[5]);
		
		HashMap<String, String> match = gs(menlist, womenlist, thepref);
		System.out.print("{ ");
		for(String w: match.keySet()) {
			System.out.printf("%s: %s, ", w, match.get(w));
		}
		System.out.println("}");
		
		
		HashMap<String, String> match_block = gs_block(menlist, womenlist, thepref, blocked);
		System.out.print("{ ");
		for(String w: match_block.keySet()) {
			System.out.printf("%s: %s, ", w, match_block.get(w));
		}
		System.out.println("}");
		
		HashMap<String, String> match_tie = gs_tie(menlist, womenlist, thepreftie);
		System.out.print("{ ");
		for(String w: match_tie.keySet()) {
			System.out.printf("%s: %s, ", w, match_tie.get(w));
		}
		System.out.println("}");
	}
	
	/*
	 * Original Gale-Shapley Algorithm
	 * Inputs: men (array of men's names)
	 *         women (array of women's names)
	 *         pref (dictionary of preferences mapping names to list of preferred names in sorted order
	 * Output: a Dictionary (HashMap) of stable matches
	 */
	public static HashMap<String, String> gs(String[] men, String[] women, HashMap<String, String[]> pref) {
		//Dictionary (HashMap) to store results
		//first String indicates the woman (key)
		//second String indicates the matched man (value)
		//Please study the use of Java HashMap class 
		HashMap<String, String> S = new HashMap<>(); 
		
		// build the rank dictionary: woman, man, rank
		HashMap<String, HashMap<String, Integer>> rank = new HashMap<>();
		for(String w: women) {
			HashMap<String, Integer> mrank = new HashMap<>();
			int i = 1;
			for(String m : pref.get(w)) {
				mrank.put(m, i);
				i++;
			}
			rank.put(w, mrank);
		}
		
		// create a pointer to the next woman to propose: man, next rank
		HashMap<String, Integer> prefptr = new HashMap<>();
		for(String m : men) {
			prefptr.put(m, 0); //all starting from 0
		}
		
		// create a freemen list as we did in class
		ArrayList<String> freemen = new ArrayList<String>();
		for(String m: men) {
			freemen.add(m);
		}

		
		//algorithm start here
		while(freemen.size()>0) {
			String m = freemen.remove(0); //always remove from the front end
			int currentPtr = prefptr.get(m);
			String w = pref.get(m)[currentPtr];
			prefptr.put(m, currentPtr+1);
			if(!S.containsKey(w)) S.put(w, m);
			else {
				String mprime = S.get(w);
				if(rank.get(w).get(m) < rank.get(w).get(mprime)) { //0 is the highest rank
					S.put(w, m);
					freemen.add(mprime); //put the previous man back to freemen list
				}
				else {
					freemen.add(m); //put the current man back to freemen list
				}
			}
		}
		
		return S;
	}
	
	/*
	 * Modified Gale-Shapley Algorithm to handle unacceptable matches
	 * Inputs: men (array of men's names)
	 *         women (array of women's names)
	 *         pref (dictionary of preferences mapping names to list of preferred names in sorted order
	 *         blocked (2D array of blocked pairs)
	 * Output: a Dictionary (HashMap) of stable matches        
	 */
	public static HashMap<String, String> gs_block(String[] men, String[] women, 
			HashMap<String, String[]> pref, String[][] blocked) {
		//Dictionary (HashMap) to store results
		//first String indicates the woman (key)
		//second String indicates the matched man (value)
		//Please study the use of Java HashMap class 
		HashMap<String, String> S = new HashMap<>(); 
		//my solution here 
		
		// build the rank dictionary: woman, man, rank 
		HashMap<String, HashMap<String, Integer>> rank = new HashMap<>();
		for(String w : women) {
			HashMap<String, Integer> mrank = new HashMap<>();
			int i = 1;
			for(String m: pref.get(w)) {
				mrank.put(m, i);
				i++;
			}
			rank.put(w, mrank);
		}
		
		// create a pointer to the next woman to propose
		HashMap<String, Integer> prefptr = new HashMap<>();
		for(String m: men) {
			prefptr.put(m, 0); //all starting from 0
		}
		
		// create a freemen list as we did in class
		ArrayList<String> freemen = new ArrayList<String>();
		for(String m: men) {
			freemen.add(m);
		}

		
		while(freemen.size()>0) {
			String m = freemen.remove(0); //always remove from the front end
			int currentPtr = prefptr.get(m);
			String w = pref.get(m)[currentPtr];
			prefptr.put(m, currentPtr+1);
			if(!S.containsKey(w)) {
				boolean unacceptable = false;
				for (String[] pair : blocked){  //check if the current pair in the the blocked String[][]
					if(pair[0].equals(m) && pair[1].equals(w)) {
						unacceptable = true;
						break;
					}
				}
				if (!unacceptable) S.put(w, m); //if the woman is unmatched and the pair isn't blocked
			}
			else {
				String mprime = S.get(w);
				if(rank.get(w).get(m) < rank.get(w).get(mprime)) { //0 is the highest rank. if the woman prefers mprime
					boolean unacceptable = false;
					for (String[] pair : blocked){ //check if the current pair in the the blocked String[][]
						if(pair[0].equals(m) && pair[1].equals(w)) {
						unacceptable = true;
						break;
					}
					}
					if (!unacceptable) {
						S.put(w, m); //if the woman is prefers mprime and the pair isn't blocked
						freemen.add(mprime); //put the previous man back to freemen list
					}
				}
				else {
					freemen.add(m); //put the current man back to freemen list
				}
			}
		}
		
		return S;
	}
	
	/*
	 * Modified Gale-Shapley Algorithm to handle ties
	 * Inputs: men (array of men's names)
	 *         women (array of women's names)
	 *         preftie (dictionary of preferences mapping names to list of preferred names in sorted order
	 * Output: a Dictionary (HashMap) of stable matches
	 */
	public static HashMap<String, String> gs_tie(String[] men, String[] women, 
			HashMap<String, String[][]> preftie) {
		
		//Dictionary (HashMap) to store results
		//first String indicates the woman (key)
		//second String indicates the matched man (value)
		//Please study the use of Java HashMap class 
		HashMap<String, String> S = new HashMap<>(); 
		//my solution here
		
		// build the rank dictionary: women's ranking of men. We will use this to decide if we should break an existing pair
		HashMap<String, HashMap<String, Integer>> womensrank = new HashMap<>(); //woman, (man, rank)
		for(String w : women) {
			HashMap<String, Integer> mrank = new HashMap<>(); //man, rank
			int i = 1;
			for(String[] mlist : preftie.get(w)) {
				for(String man : mlist){
					mrank.put(man, i); //if they are tied (in the same array), put them on the same rank
				}
				i++;
			}
			womensrank.put(w, mrank);
		}

		// build the rank dictionary: men's ranking of women (ignoring ties bc it never matters, all we need is to get the name of the next woman to propose to)
		HashMap<String, HashMap<Integer, String>> mensrank = new HashMap<>(); //man, (rank, woman)
		for(String m : men) {
			HashMap<Integer, String> wrank = new HashMap<>(); //rank, woman
			int i = 1;
			for(String[] wlist : preftie.get(m)) {
				for(String woman : wlist){
					wrank.put(i, woman);
					i++;
				}
			}
			mensrank.put(m, wrank);
		}

		// create a pointer to the next woman each man should propose to
		HashMap<String, Integer> prefptr = new HashMap<>();
		for(String m : men) {
			prefptr.put(m, 1); //all starting from 1
		}
		
		// create a freemen list as we did in class
		ArrayList<String> freemen = new ArrayList<String>();
		for(String m: men) {
			freemen.add(m);
		}

		
		//algorithm start here
		while(freemen.size()>0) {
			String m = freemen.remove(0); //always remove from the front end
			
			//use men's ranking to get their next preferred partner
			int currentPtr = prefptr.get(m);
			String w = mensrank.get(m).get(currentPtr); 
			prefptr.put(m, currentPtr+1);
			
			if(!S.containsKey(w)) S.put(w, m);
			else {
				String mprime = S.get(w);
				if(womensrank.get(w).get(m) < womensrank.get(w).get(mprime)) { //0 is the highest rank, use women's ranking to decide whether or not to break a pair
					S.put(w, m);
					freemen.add(mprime); //put the previous man back to freemen list
				}
				else {
					freemen.add(m); //put the current man back to freemen list
				}
			}
		}
		return S;
	}
}
