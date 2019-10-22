package com.sachinreddy.GeometricAcoustics;

public class Pair 
{
	public int soundType = 0;	// SoundType color
	public int rayDistance = 0;	// Ray Distance
	
	public static Pair create(int soundType, int rayDistance)
	{
		Pair i = new Pair();
		i.soundType = soundType;
		i.rayDistance = rayDistance;		
		return i;
	}
	
	Pair()
	{
		
	}
}
