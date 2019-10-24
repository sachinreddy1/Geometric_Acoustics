package com.sachinreddy.GeometricAcoustics;

public class HistogramPair 
{
	public int soundType = 0;	// SoundType color
	public int data = 0;	// Ray Distance
	
	public static HistogramPair create(int soundType, int data)
	{
		HistogramPair i = new HistogramPair();
		i.soundType = soundType;
		i.data = data;		
		return i;
	}
	
	HistogramPair()
	{
		
	}
}
