package com.sachinreddy.GeometricAcoustics;
import java.io.*; 
import java.util.*; 

public class Compare {
	public static void compare(Pair arr[], int n) 
    { 
        // Comparator to sort the pair according to second element 
        Arrays.sort(arr, new Comparator<Pair>() { 
            @Override public int compare(Pair p1, Pair p2) 
            { 
                return p1.rayDistance - p2.rayDistance; 
            } 
        }); 
  
        for (int i = 0; i < n; i++) { 
            System.out.print(arr[i].rayDistance); 
        } 
        System.out.println(); 
    } 
} 
