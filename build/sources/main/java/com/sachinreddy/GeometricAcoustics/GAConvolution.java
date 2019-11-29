package com.sachinreddy.GeometricAcoustics;

import java.util.ArrayList;

public class GAConvolution {
	
	static float[] frequencyFilter = {1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
	static float[] rayLengthFilter = {1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
//	static float[] frequencyFilter = {0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f};
//	static float[] rayLengthFilter = {0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
	
	public static void Convolution(ArrayList<HistogramTriple> histogramValues) {
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
		
		int sectionLength = GeometricAcousticsCore.Config.environmentCalculationRays/4;
		float[] ret = new float[4];
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < sectionLength; i++) {
				frequencyData[i + j*sectionLength] = frequencyData[i + j*sectionLength] * frequencyFilter[i];
				rayLengthData[i + j*sectionLength] = rayLengthData[i + j*sectionLength] * rayLengthFilter[i];
			}
		}
		System.out.println("[FREQUENCY]: " + java.util.Arrays.toString(frequencyData));
		System.out.println("[RAY LENGTH]: " + java.util.Arrays.toString(rayLengthData));
	}
}
