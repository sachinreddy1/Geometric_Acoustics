package com.sachinreddy.GeometricAcoustics;

public class Pair 
{
	public int soundType = 0;	// SoundType color
	public int data = 0;	// Ray Distance
	
	public static Pair create(int soundType, int data)
	{
		Pair i = new Pair();
		i.soundType = soundType;
		i.data = data;		
		return i;
	}
	
	Pair()
	{
		
	}
}
