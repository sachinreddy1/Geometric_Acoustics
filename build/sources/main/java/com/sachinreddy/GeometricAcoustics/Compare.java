package com.sachinreddy.GeometricAcoustics;
import java.io.*; 
import java.util.*;


public class Compare {
	
   static HashMap <Integer, ArrayList<Integer>> hm; 
	
   public static void comparePair(HistogramPair arr[]) 
   { 
		// Comparator to sort the pair according to second element 
		Arrays.sort(arr, new Comparator<HistogramPair>() { 
		    @Override public int compare(HistogramPair p1, HistogramPair p2) 
		    { 
		        return p1.data - p2.data; 
		    } 
		});
   } 
   
   public static void compareTriple(ArrayList<HistogramTriple> arr) 
   { 
		// Comparator to sort the pair according to second element 
	   Collections.sort(arr, new Comparator<HistogramTriple>() { 
		    @Override public int compare(HistogramTriple p1, HistogramTriple p2) 
		    { 
		        return p1.rayDistance - p2.rayDistance; 
		    } 
		});
   } 
   
   // ------------------------------------------------- //
      
   public static ArrayList<HistogramTriple> countFreqValues(HistogramPair arr[], int n) 
   { 
	   	// For each rayDistance, get all assigned soundTypes
	   	hm = new HashMap<Integer, ArrayList<Integer>>();
        for (int i=0; i<n; i++) {
        	if (hm.get(arr[i].data) == null)
        		hm.put(arr[i].data, new ArrayList<Integer>());
        	hm.get(hm.get(arr[i].data).add(arr[i].soundType));
        }
        
        // Count number of soundTypes for each rayDistance, and assign most frequent soundType
        ArrayList<HistogramTriple> ret = new ArrayList<HistogramTriple>();
        Set<Integer> keys = hm.keySet();
        for(Integer key: keys)
        {
        	ArrayList<Integer> colors = hm.get(key);
        	int frequency = colors.size();
        	int soundType = mostFrequentColor(colors, frequency);
        	int rayDistance = key;
        	ret.add(HistogramTriple.create(soundType, rayDistance, frequency));
        }
        
        return ret;
   }
   
   static int mostFrequentColor(ArrayList<Integer> arr, int n) 
   { 
       // Sort the array 
	   Collections.sort(arr);
       // find the max frequency using linear traversal
       int max_count = 1, res = arr.get(0); 
       int curr_count = 1; 
       for (int i = 1; i < n; i++) 
       { 
           if (arr.get(i) == arr.get(i-1)) 
               curr_count++; 
           else 
           { 
               if (curr_count > max_count) 
               { 
                   max_count = curr_count; 
                   res = arr.get(i-1); 
               } 
               curr_count = 1; 
           } 
       } 
     
       // If last element is most frequent 
       if (curr_count > max_count) 
       { 
           max_count = curr_count; 
           res = arr.get(n-1); 
       } 
       return res; 
   } 
} 
