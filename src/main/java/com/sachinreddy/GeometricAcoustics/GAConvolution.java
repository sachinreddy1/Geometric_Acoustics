package com.sachinreddy.GeometricAcoustics;

import java.util.ArrayList;

public class GAConvolution {
	
	static float[] frequencyFilter = {0.0033f, 0.0033f, 0.0033f, 0.0033f, 0.0033f, 0.0033f, 0.0033f, 0.0033f};
	static float[] rayLengthFilter = {0.0033f, 0.0033f, 0.0033f, 0.0033f, 0.0033f, 0.0033f, 0.0033f, 0.0033f};
	//
	static int numSections = 4;
	
	public static float[] Convolution(ArrayList<HistogramTriple> histogramValues) {
		float[] frequencyData = new float[GeometricAcousticsCore.Config.environmentCalculationRays];
		float[] rayLengthData = new float[GeometricAcousticsCore.Config.environmentCalculationRays];
		
		// Initialize to zero
		for (int i = 0; i < GeometricAcousticsCore.Config.environmentCalculationRays; i++) {
			frequencyData[i] = 0;
			rayLengthData[i] = 0;
		}
		
		// Creating data arrays
		for (int i = 0; i < histogramValues.size(); i++) {
			frequencyData[i] = histogramValues.get(i).frequency;
			rayLengthData[i] = histogramValues.get(i).rayDistance;
		}
		
		// Calculation
		int sectionLength = GeometricAcousticsCore.Config.environmentCalculationRays/numSections;
		float[] ret = new float[numSections];
		for (int j = 0; j < numSections; j++) {
			float frequencyDataSum = 0.0f;
			float rayLengthDataSum = 0.0f;
			for (int i = 0; i < sectionLength; i++) {
				frequencyDataSum += frequencyData[i + j*sectionLength] * frequencyFilter[i];
				rayLengthDataSum += rayLengthData[i + j*sectionLength] * rayLengthFilter[i];
			}
			ret[j] = frequencyDataSum + rayLengthDataSum;
		}
		
		// Return
		return ret;
	}
}
