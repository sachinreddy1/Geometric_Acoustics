package com.sachinreddy.GeometricAcoustics;
import java.io.*; 
import java.util.*; 

class Compare {
    static Pair[] compare(Pair arr[]) 
    { 
        // Comparator to sort the pair according to second element 
        Arrays.sort(arr, new Comparator<Pair>() { 
            @Override public int compare(Pair p1, Pair p2) 
            { 
                return p1.y - p2.y; 
            } 
        }); 
  
//        for (int i = 0; i < n; i++) { 
//            System.out.print(arr[i].x + " " + arr[i].y + " "); 
//        } 
//        System.out.println(); 
        return arr;
    } 
} 
