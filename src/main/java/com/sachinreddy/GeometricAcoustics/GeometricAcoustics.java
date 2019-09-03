package com.sachinreddy.GeometricAcoustics;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCcontext;
import org.lwjgl.openal.ALCdevice;
import org.lwjgl.openal.EFX10;

import paulscode.sound.SoundSystemConfig;

public class GeometricAcoustics 
{		
	private static final String logPrefix = "[GEOMETRIC ACOUSTICS]";
		
	private static SoundCategory lastSoundCategory;
	private static String lastSoundName;
		
	// ------------------------------------------------- //
	
	public static void setLastSoundCategory(SoundCategory sc)
	{
		lastSoundCategory = sc;
	}
	
	public static void setLastSoundName(String name)
	{
		lastSoundName = name;
	}
	
	// ------------------------------------------------- //
	
	public static void onPlaySound(float posX, float posY, float posZ, int sourceID)
	{
		log("On play sound... Source ID: " + sourceID + " " + posX + ", " + posY + ", " + posZ + "    Sound category: " + lastSoundCategory.toString() + "    Sound name: " + lastSoundName);			
	}
	
	// ------------------------------------------------- //
	
	protected static void log(String message)
	{
		System.out.println(logPrefix + ": " + message);
	}
	
}
