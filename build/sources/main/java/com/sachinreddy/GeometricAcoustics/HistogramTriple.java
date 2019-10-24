package com.sachinreddy.GeometricAcoustics;

public class HistogramTriple 
{
	public int soundType;
	public int rayDistance;
	public int frequency;
	
	public static HistogramTriple create(int soundType, int rayDistance, int frequency)
	{
		HistogramTriple i = new HistogramTriple();
		i.soundType = soundType;
		i.rayDistance = rayDistance;
		i.frequency = frequency;
		return i;
	}
	
	HistogramTriple()
	{
		
	}
}
