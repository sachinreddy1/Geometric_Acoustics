package com.sachinreddy.GeometricAcoustics;
import java.io.*; 
import java.util.*; 

public class Compare {
	
   static HashMap <Pair, Integer> hm = new HashMap<Pair, Integer>(); 
	
   public static void compare(Pair arr[]) 
   { 
		// Comparator to sort the pair according to second element 
		Arrays.sort(arr, new Comparator<Pair>() { 
		    @Override public int compare(Pair p1, Pair p2) 
		    { 
		        return p1.data - p2.data; 
		    } 
		});
   } 
   
   // ------------------------------------------------- //
   
   static void countFreqValues(Pair arr[], int n) 
   { 
        for (int i=0; i<n; i++) 
            if (hm.containsKey(arr[i])) 
                hm.put(a[i], hm.get(a[i]) + 1); 
            else hm.put(a[i] , 1); 
    } 
   
   static int query(int x) 
   { 
       if (hm.containsKey(x)) 
           return hm.get(x); 
       return 0; 
   } 
} 
