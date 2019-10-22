package com.sachinreddy.GeometricAcoustics;
import java.io.*; 
import java.util.*; 

public class Compare {
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
} 
