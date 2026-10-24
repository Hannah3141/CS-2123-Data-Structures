/*
 * CS2123 Project 2: DFS-based Topological Sorting 
 */

import java.util.*;
import java.io.*;

public class TopoSort {
	public static ArrayList<Integer> processed = new ArrayList<Integer>();
	public static void main(String[] args) throws IOException {
		// The small built-in DAG G1
		// Use this graph to verify the correctness of your DFS-based topological sorting
		int cs143 = 0, cs321 = 1, cs322 = 2, cs142 = 3, cs370 = 4, cs341 = 5, cs326 = 6, cs378 = 7, cs401 = 8, cs421 = 9;
		int[][] g1 = { 
			{ cs321, cs341, cs370, cs378 }, //0=143
			{ cs322, cs326 }, //1=321
			{ cs401, cs421 }, //2=322
			{ cs143 }, //3=142
			{}, //4=370
			{ cs401 }, //5=341
			{ cs401, cs421 }, //6=326
			{ cs401 }, //7=378
			{}, //8=401
			{} //9=421
		};

		//make a hashmap where the key is the source node and the arraylist contains all it's destination nodes
		HashMap<Integer, ArrayList<Integer>> G0 = new HashMap<>();
		for (int i = 0; i < g1.length; i++) {
			ArrayList<Integer> neighbors = new ArrayList<Integer>();
			for (int j = 0; j < g1[i].length; j++) {
				neighbors.add(g1[i][j]);
			}
			G0.put(i, neighbors);
		}

		String names[] = { "cs143", "cs321", "cs322", "cs142", "cs370", "cs341", "cs326", "cs378", "cs401", "cs421" };

		// Section 1: for project step 2, verify the correctness of your topo_sort() functions
	    //     no need to change this section, just verify the correctness of the printout
		int[] topo_order = topo_sort(G0);
		for (int i = 0; i < names.length; i++) {
			System.out.printf("%s: %d, ", names[i], topo_order[i]);
		}
		System.out.println();
		topo_order = ind_topo(G0);
		for (int i = 0; i < names.length; i++) {
			System.out.printf("%s: %d, ", names[i], topo_order[i]);
		}
		System.out.println();

		// Section 2: for project step3, performance comparison
	    //   load each graph from DAG1.txt-DAG5.txt
	    //   store each graph in memory using the format of G1
	    //     Hint: you may need Java's File and Scanner classes to read in graph data 
		FileWriter writer = new FileWriter("output.txt");
	
		File DAG1 = new File("DAG1.txt");
		Scanner file1 = new Scanner(DAG1);
		HashMap<Integer, ArrayList<Integer>> G1 = new HashMap<>();
		for(int i = 0; i < 5000; i++){ 
			G1.put(i, new ArrayList<Integer>()); //make an empty array list for each node
		}
		while(file1.hasNextLine()){
			String[] data = file1.nextLine().split(" ");
			int dest = Integer.parseInt(data[0]);
			int source = Integer.parseInt(data[1]);
			ArrayList<Integer> dests = G1.get(source); 
			dests.add(dest); //add the new destination to the list of destinations
			G1.put(source, dests); //add the whole list to the value with the source as the key
		}
		
		File DAG2 = new File("DAG2.txt");
		Scanner file2 = new Scanner(DAG2);
		HashMap<Integer, ArrayList<Integer>> G2 = new HashMap<>();
		for(int i = 0; i < 10000; i++){
			G2.put(i, new ArrayList<Integer>());
		}
		while(file2.hasNextLine()){
			String[] data = file2.nextLine().split(" ");
			int dest = Integer.parseInt(data[0]);
			int source = Integer.parseInt(data[1]);
			ArrayList<Integer> dests = new ArrayList<>();
			dests.add(dest);
			G2.put(source, dests);
		}

		File DAG3 = new File("DAG3.txt");
		Scanner file3 = new Scanner(DAG3);
		HashMap<Integer, ArrayList<Integer>> G3 = new HashMap<>();
		for(int i = 0; i < 50000; i++){
			G3.put(i, new ArrayList<Integer>());
		}
		while(file3.hasNextLine()){
			String[] data = file3.nextLine().split(" ");
			int dest = Integer.parseInt(data[0]);
			int source = Integer.parseInt(data[1]);
			ArrayList<Integer> dests = new ArrayList<>();
			dests.add(dest);
			G3.put(source, dests);
		}

		File DAG4 = new File("DAG4.txt");
		Scanner file4 = new Scanner(DAG4);
		HashMap<Integer, ArrayList<Integer>> G4 = new HashMap<>();
		for(int i = 0; i < 100000; i++){
			G4.put(i, new ArrayList<Integer>());
		}
		while(file4.hasNextLine()){
			String[] data = file4.nextLine().split(" ");
			int dest = Integer.parseInt(data[0]);
			int source = Integer.parseInt(data[1]);
			ArrayList<Integer> dests = new ArrayList<>();
			dests.add(dest);
			G4.put(source, dests);
		}

		File DAG5 = new File("DAG5.txt");
		Scanner file5 = new Scanner(DAG5);
		HashMap<Integer, ArrayList<Integer>> G5 = new HashMap<>();
		for(int i = 0; i < 200000; i++){
			G5.put(i, new ArrayList<Integer>());
		}
		while(file5.hasNextLine()){
			String[] data = file5.nextLine().split(" ");
			int dest = Integer.parseInt(data[0]);
			int source = Integer.parseInt(data[1]);
			ArrayList<Integer> dests = new ArrayList<>();
			dests.add(dest);
			G5.put(source, dests);
		}
		
		//   call your topo_sort and the provided ind_topo by passing each graph as the input to obtain the execution time (in milliseconds) 
		// Complete the code for performance comparison here  
		long start = System.currentTimeMillis();
		topo_order = topo_sort(G1);
		long recursive = System.currentTimeMillis();
		topo_order = ind_topo(G1);
		long end = System.currentTimeMillis();
		try {
			writer.append("Graph 1 recursive: " + Long.toString(recursive - start) + "\n");
			writer.append("Graph 1 inductive: " + Long.toString(end - recursive) + "\n");
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		
		start = System.currentTimeMillis();
		topo_order = topo_sort(G2);
		recursive = System.currentTimeMillis();
		topo_order = ind_topo(G2);
		end = System.currentTimeMillis();
		try {
			writer.append("Graph 2 recursive: " + Long.toString(recursive - start) + "\n");
			writer.append("Graph 2 inductive: " + Long.toString(end - recursive) + "\n");
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

		start = System.currentTimeMillis();
		topo_order = topo_sort(G3);
		recursive = System.currentTimeMillis();
		topo_order = ind_topo(G3);
		end = System.currentTimeMillis();
		try {
			writer.append("Graph 3 recursive: " + Long.toString(recursive - start) + "\n");
			writer.append("Graph 3 inductive: " + Long.toString(end - recursive) + "\n");
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

		start = System.currentTimeMillis();
		topo_order = topo_sort(G4);
		recursive = System.currentTimeMillis();
		topo_order = ind_topo(G4);
		end = System.currentTimeMillis();
		try {
			writer.append("Graph 4 recursive: " + Long.toString(recursive - start) + "\n");
			writer.append("Graph 4 inductive: " + Long.toString(end - recursive) + "\n");
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		
		start = System.currentTimeMillis();
		topo_order = topo_sort(G5);
		recursive = System.currentTimeMillis();
		topo_order = ind_topo(G5);
		end = System.currentTimeMillis();
		try {
			writer.append("Graph 5 recursive: " + Long.toString(recursive - start) + "\n");
			writer.append("Graph 5 inductive: " + Long.toString(end - recursive) + "\n");
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

		file1.close();
		file2.close();
		file3.close();
		file4.close();
		file5.close();
		writer.close();
	}

	// For project step1    
	// Input
	//    G as the input graph
	//    s as the current vertex to be explored 
	public static void dfs_topo(HashMap<Integer, ArrayList<Integer>> G, Integer s,
			HashMap<Integer, Integer> E, ArrayList<Integer> D, ArrayList<Integer> P, 
			int[] L, int currLabel) {//you may modify the input argument if necessary
		//add your function body here
		E.put(s, s); 
		D.add(s); 
		for(Integer nb : G.get(s)){
			if(E.containsKey(nb)) continue; 
			dfs_topo(G, nb, E, D, P, L, currLabel); //if the node's neighbor isn't explored, go to it
		}
		P.add(s); //add the node to processed once it's returned from all its neighbors
		L[s] = currLabel; //start with biggest label for deepest node
		currLabel--;
		return;
		/*ArrayList<Integer> adj = G.get(s);
		if(adj != null){
			for(int e = 0; e < adj.size(); e++){
				if (!processed.contains(adj.get(e))){
					dfs_topo(G, adj.get(e));
				}
			}
		}
		processed.add(s);  */
	}

	// For project step1
	// Input
	//    G as the input graph
	// Output
	//    L as the Java array to record the ordering of vertices. Example L[0] = 5 means node 0 is assigned topo sort number 5
	public static int[] topo_sort(HashMap<Integer, ArrayList<Integer>> G) {//you CANNOT change the input argument list
		int[] L = new int[G.size()];
		//my code here
		//explored
		HashMap<Integer, Integer> E = new HashMap<>();
		//discovered
		ArrayList<Integer> D = new ArrayList<>();
		//processed
		ArrayList<Integer> P = new ArrayList<>();
		int currLabel = G.size(); //1 indexed, not 0 indexed, so start with size not size-1
		for(int i = 0; i < L.length; i++){ //make sure all the nodes get processed, even if they aren't reachable from the first node by DFS
			if(!processed.contains(i)) { 
 				dfs_topo(G, i, E, D, P, L, currLabel);
			}
		}
		
		return L;
		/*
		for(int i = 0; i < L.length; i++){
			L[processed.get(i)] = L.length - i;
		}*/
	}

	// I. This is the provided function for induction-based topological 
	// sorting. You need to call this function and compare with your 
	// DFS-based topological sorting for performance     
	// II. DO NOT change any part of this function
	// III. This function destroys the input graph G because it 
	// removes one node in every iteration. During performance comparison, 
	// if you call this function before topo_sort(), you need to make a deep 
	// copy of your graph G and pass that copy to topo_sort() 
	public static int[] ind_topo(HashMap<Integer, ArrayList<Integer>> G) {

		int[] count = new int[G.size()]; //in-degree of each node
		for (int i = 0; i < G.size(); i++) {
			for (Integer v : G.get(i)) {
				count[v]++;
			}
		}

		int[] L = new int[G.size()]; //an array to record the ordering of all nodes
		int k = 1; // topo order's value starts from 1 in induction-based method
		while (!G.isEmpty()) {
			Integer u = 0;
			for(Integer u1: G.keySet()) {
				if(count[u1]==0) {
					u=u1;
					break; //break after finding a source node
				}
			}
			L[u] = k; //assign a topo order value to this node
			k++;
			for (Integer v : G.get(u)) {
				count[v]--;
			}
			G.remove(u); //remove this source node and its outgoing edges from G
		}
		return L;
	}
}




