package com.sachinreddy.GeometricAcoustics;
import java.io.*; 
import java.util.*;

import org.apache.commons.lang3.tuple.Pair; 

public class Compare {
	
   static HashMap <HistogramPair, Integer> hm; 
	
   public static void compare(HistogramPair arr[]) 
   { 
		// Comparator to sort the pair according to second element 
		Arrays.sort(arr, new Comparator<HistogramPair>() { 
		    @Override public int compare(HistogramPair p1, HistogramPair p2) 
		    { 
		        return p1.data - p2.data; 
		    } 
		});
   } 
   
   // ------------------------------------------------- //
   
   // Sort the list
   // Count frequencies of all pairs in the list
   // index - Triple(color, rayLength, freq)
   
   public static void countFreqValues(HistogramPair arr[], int n) 
   { 
	   	hm = new HashMap<HistogramPair, Integer>();
        for (int i=0; i<n; i++) 
            if (hm.containsKey(arr[i])) 
                hm.put(arr[i], hm.get(arr[i]) + 1); 
            else hm.put(arr[i] , 1); 
   }
   
   public static int query(HistogramPair x) 
   { 
       if (hm.containsKey(x)) 
           return hm.get(x); 
       return 0; 
   }
   
   public static Int3[] getQuery() 
   { 
	   Set<HistogramPair> keys = hm.keySet();
	   Int3[] ret = new Int3[hm.size()];
	   int i = 0;
	   for(HistogramPair key: keys)
	   {
		   ret[i] = Int3.create(key.soundType, key.data, hm.get(key));
		   i++;
	   }
	   return ret;
   }
} 
