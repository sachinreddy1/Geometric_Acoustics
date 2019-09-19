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
	//
	private static int auxFXSlot0;
	private static int auxFXSlot1;
	private static int auxFXSlot2;
	private static int auxFXSlot3;
	//
	private static int reverb0;
	private static int reverb1;
	private static int reverb2;
	private static int reverb3;
	//
	private static int directFilter0;
	//
	private static int sendFilter0;
	private static int sendFilter1;
	private static int sendFilter2;
	private static int sendFilter3;
	//
	private static SoundCategory lastSoundCategory;
	private static String lastSoundName;
	//
	private static Minecraft minecraft;
	//
	public static int attenuationModel = SoundSystemConfig.ATTENUATION_ROLLOFF;
	public static float globalRolloffFactor = GeometricAcousticsCore.Config.rolloffFactor;
	public static float globalVolumeMultiplier = 4.0f;
	public static float globalReverbMultiplier = 0.7f * GeometricAcousticsCore.Config.globalReverbGain;
	public static double soundDistanceAllowance = GeometricAcousticsCore.Config.soundDistanceAllowance;
		
	// ------------------------------------------------- //
	
	public static void initialize()
	{
		log("Initializing Geometric Acoustics...");
		setupReverb();
		minecraft = Minecraft.getMinecraft();
	}
	
	public static void applyConfigChanges()
	{
		globalRolloffFactor = GeometricAcousticsCore.Config.rolloffFactor;
		globalReverbMultiplier = 0.7f * GeometricAcousticsCore.Config.globalReverbGain;
		soundDistanceAllowance = GeometricAcousticsCore.Config.soundDistanceAllowance;
		
		if (auxFXSlot0 != 0)
		{
			setReverbParameters(ReverbParameters.getReverb0(), auxFXSlot0, reverb0);
			setReverbParameters(ReverbParameters.getReverb1(), auxFXSlot1, reverb1);
			setReverbParameters(ReverbParameters.getReverb2(), auxFXSlot2, reverb2);
			setReverbParameters(ReverbParameters.getReverb3(), auxFXSlot3, reverb3);
		}
	}
	
	private static void setupReverb()
	{
		ALCcontext currentContext = ALC10.alcGetCurrentContext();
		ALCdevice currentDevice = ALC10.alcGetContextsDevice(currentContext);
		
		if (ALC10.alcIsExtensionPresent(currentDevice, "ALC_EXT_EFX"))
			log("EFX Extension recognized.");
		else
			return;
		
		// Create auxiliary effect slots
		auxFXSlot0 = EFX10.alGenAuxiliaryEffectSlots();
		EFX10.alAuxiliaryEffectSloti(auxFXSlot0, EFX10.AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL10.AL_TRUE);
		auxFXSlot1 = EFX10.alGenAuxiliaryEffectSlots();
		EFX10.alAuxiliaryEffectSloti(auxFXSlot1, EFX10.AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL10.AL_TRUE);
		auxFXSlot2 = EFX10.alGenAuxiliaryEffectSlots();
		EFX10.alAuxiliaryEffectSloti(auxFXSlot2, EFX10.AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL10.AL_TRUE);
		auxFXSlot3 = EFX10.alGenAuxiliaryEffectSlots();
		EFX10.alAuxiliaryEffectSloti(auxFXSlot3, EFX10.AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL10.AL_TRUE);	
		
		// Create effect objects
		reverb0 = EFX10.alGenEffects();
		EFX10.alEffecti(reverb0, EFX10.AL_EFFECT_TYPE, EFX10.AL_EFFECT_EAXREVERB);
		reverb1 = EFX10.alGenEffects();
		EFX10.alEffecti(reverb1, EFX10.AL_EFFECT_TYPE, EFX10.AL_EFFECT_EAXREVERB);
		reverb2 = EFX10.alGenEffects();
		EFX10.alEffecti(reverb2, EFX10.AL_EFFECT_TYPE, EFX10.AL_EFFECT_EAXREVERB);
		reverb3 = EFX10.alGenEffects();
		EFX10.alEffecti(reverb3, EFX10.AL_EFFECT_TYPE, EFX10.AL_EFFECT_EAXREVERB);
		
		//Create filters
		directFilter0 = EFX10.alGenFilters();
		EFX10.alFilteri(directFilter0, EFX10.AL_FILTER_TYPE, EFX10.AL_FILTER_LOWPASS);
		sendFilter0 = EFX10.alGenFilters();
		EFX10.alFilteri(sendFilter0, EFX10.AL_FILTER_TYPE, EFX10.AL_FILTER_LOWPASS);
		sendFilter1 = EFX10.alGenFilters();
		EFX10.alFilteri(sendFilter1, EFX10.AL_FILTER_TYPE, EFX10.AL_FILTER_LOWPASS);
		sendFilter2 = EFX10.alGenFilters();
		EFX10.alFilteri(sendFilter2, EFX10.AL_FILTER_TYPE, EFX10.AL_FILTER_LOWPASS);
		sendFilter3 = EFX10.alGenFilters();
		EFX10.alFilteri(sendFilter3, EFX10.AL_FILTER_TYPE, EFX10.AL_FILTER_LOWPASS);
		
		applyConfigChanges();
		
		log("Reverb parameters setup.");
	}
	
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
		//log("[SOUND PLAYED]: Source ID: " + sourceID + " | (" + posX + ", " + posY + ", " + posZ + ") | Sound category: " + lastSoundCategory.toString() + " | Sound name: " + lastSoundName);
		calculateEnvironment(posX, posY, posZ, sourceID);
	}
	
	// ------------------------------------------------- //
	
	private static float getBlockReflectivity(Int3 blockPos)
	{
		Block block = minecraft.theWorld.getBlockState(new BlockPos(blockPos.x, blockPos.y, blockPos.z)).getBlock();
		SoundType soundType = block.getSoundType();
		
		float reflectivity = 0.5f;
		
		if (soundType == SoundType.STONE)
			reflectivity = GeometricAcousticsCore.Config.stoneReflectivity;
		else if (soundType == SoundType.WOOD)
			reflectivity = GeometricAcousticsCore.Config.woodReflectivity;
		else if (soundType == SoundType.GROUND)
			reflectivity = GeometricAcousticsCore.Config.groundReflectivity;
		else if (soundType == SoundType.PLANT)
			reflectivity = GeometricAcousticsCore.Config.plantReflectivity;
		else if (soundType == SoundType.METAL)
			reflectivity = GeometricAcousticsCore.Config.metalReflectivity;
		else if (soundType == SoundType.GLASS)
			reflectivity = GeometricAcousticsCore.Config.glassReflectivity;
		else if (soundType == SoundType.CLOTH)
			reflectivity = GeometricAcousticsCore.Config.clothReflectivity;
		else if (soundType == SoundType.SAND)	
			reflectivity = GeometricAcousticsCore.Config.sandReflectivity;
		else if (soundType == SoundType.SNOW)
			reflectivity = GeometricAcousticsCore.Config.snowReflectivity;
		else if (soundType == SoundType.LADDER)
			reflectivity = GeometricAcousticsCore.Config.woodReflectivity;
		else if (soundType == SoundType.ANVIL)
			reflectivity = GeometricAcousticsCore.Config.metalReflectivity;
		
		reflectivity *= GeometricAcousticsCore.Config.globalBlockReflectance;
		
		return reflectivity;
	}
	
	private static Vec3d getNormalFromFacing(EnumFacing sideHit)
	{
		Vec3i inormal = sideHit.getDirectionVec();
		Vec3d normal = new Vec3d(inormal.getX(), inormal.getY(), inormal.getZ());
		return normal;
	}
	
	private static Vec3d reflect(Vec3d dir, Vec3d normal)
	{
		double dot = dir.dotProduct(normal);
		double x = dir.xCoord - 2.0 * dot * normal.xCoord;
		double y = dir.yCoord - 2.0 * dot * normal.yCoord;
		double z = dir.zCoord - 2.0 * dot * normal.zCoord;
		return new Vec3d(x, y, z);
	}
	
	private static Vec3d offsetSoundByName(Vec3d soundPos, Vec3d playerPos, String name, String soundCategory)
	{
		double offsetX = 0.0;
		double offsetY = 0.0;
		double offsetZ = 0.0;
		
		double offsetTowardsPlayer = 0.0;
		
		Vec3d toPlayerVector = playerPos.subtract(soundPos).normalize();
		
		//names
		if (name.matches(".*step.*"))
		{
			offsetY = 0.1;
		}
		
		//categories
		if (soundCategory.matches("block") || soundCategory.matches("record"))
		{
			offsetTowardsPlayer = 0.89;
		}
		
		if (soundPos.yCoord % 1.0 < 0.001 && soundPos.yCoord > 0.01)
		{
			offsetY = 0.1;
		}
		
		offsetX += toPlayerVector.xCoord * offsetTowardsPlayer;
		offsetY += toPlayerVector.yCoord * offsetTowardsPlayer;
		offsetZ += toPlayerVector.zCoord * offsetTowardsPlayer;
		soundPos = soundPos.addVector(offsetX, offsetY, offsetZ);		
		return soundPos;
	}
	
	// ------------------------------------------------- //
	
	private static void calculateEnvironment(float posX, float posY, float posZ, int sourceID)
	{
		if (posX < 0.01f && posY < 0.01f && posZ < 0.01f)
		{			
			setEnvironment(sourceID, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
			return;
		}
		
		if (minecraft.thePlayer == null || minecraft.theWorld == null)
			return;
		
		if (GeometricAcousticsCore.Config.skipRainOcclusionTracing && lastSoundName.matches(".*rain.*"))
		{
			setEnvironment(sourceID, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
			return;
		}
		
		float directCutoff = 1.0f;
		float absorptionCoeff = GeometricAcousticsCore.Config.globalBlockAbsorption * 3.0f;
		
		//Direct sound occlusion
		Vec3d soundPos = new Vec3d(posX, posY, posZ);
		Vec3d playerPos = minecraft.thePlayer.getPositionVector();
		playerPos = new Vec3d(playerPos.xCoord, playerPos.yCoord + minecraft.thePlayer.getEyeHeight(), playerPos.zCoord);
		soundPos = offsetSoundByName(soundPos, playerPos, lastSoundName, lastSoundCategory.getName());		
		Vec3d toPlayerVector = playerPos.subtract(soundPos).normalize();
		
		Vec3d rayOrigin = new Vec3d(soundPos.xCoord, soundPos.yCoord, soundPos.zCoord);
		if (lastSoundName.matches(".*block.*"))
		{
			rayOrigin = rayOrigin.add(toPlayerVector.scale(0.867));
		}
		
		// ---------------------- //
			
		float occlusionAccumulation = 0.0f;
		for(int i = 0; i < 10; i++)
		{
			RayTraceResult rayHit = minecraft.theWorld.rayTraceBlocks(rayOrigin, playerPos, true);
			
			//If we hit a block
			if (rayHit != null)
			{	
				Block blockHit = minecraft.theWorld.getBlockState(rayHit.getBlockPos()).getBlock();
				float blockOcclusion = 1.0f;
				if (!blockHit.isOpaqueCube(blockHit.getDefaultState()))
					blockOcclusion *= 0.15f;
								
				//Accumulate density
				occlusionAccumulation += blockOcclusion;
				rayOrigin = new Vec3d(rayHit.hitVec.xCoord + toPlayerVector.xCoord * 0.1, rayHit.hitVec.yCoord + toPlayerVector.yCoord * 0.1, rayHit.hitVec.zCoord + toPlayerVector.zCoord * 0.1);
			}
			else
				break;
		}
		
		directCutoff = (float)Math.exp(-occlusionAccumulation * absorptionCoeff);
		float directGain = (float)Math.pow(directCutoff, 0.1);
		
		// ---------------------- //
				
		//Calculate reverb parameters for this sound
		float sendGain0 = 0.0f;
		float sendGain1 = 0.0f;
		float sendGain2 = 0.0f;
		float sendGain3 = 0.0f;
		
		float sendCutoff0 = 1.0f;
		float sendCutoff1 = 1.0f;
		float sendCutoff2 = 1.0f;
		float sendCutoff3 = 1.0f;
		
		if (minecraft.thePlayer.isInsideOfMaterial(Material.WATER))
		{
			directCutoff *= 1.0f - GeometricAcousticsCore.Config.underwaterFilter;
		}
		
		if (lastSoundName.matches(".*rain.*"))
		{
			setEnvironment(sourceID, sendGain0, sendGain1, sendGain2, sendGain3, sendCutoff0, sendCutoff1, sendCutoff2, sendCutoff3, directCutoff, directGain);
			return;
		}
		
		//Shoot rays around sound
		final float phi = 1.618033988f;
		final float gAngle = phi * (float)Math.PI * 2.0f;
		final float maxDistance = 256.0f;
		
		final int numRays = GeometricAcousticsCore.Config.environmentEvaluationRays;
		final int rayBounces = 4;
		float reflectionEnergySum = 0.0f;
		float rayLengthSum = 0.0f;
		int numRaysHit = 0;
		
		int secondaryRayHits = 0;
		float secondaryRayLengthSum = 0.0f;
		float secondaryReflectionEnergySum = 0.0f;
		
		int[] rayHits = new int[rayBounces];
		
		float totalReflectivityRatio = 0.0f;
		float[] bounceReflectivityRatio = new float[rayBounces];
		
		float sharedAirspace = 0.0f;
		
		float rcpTotalRays = 1.0f / (numRays * rayBounces);
		float rcpPrimaryRays = 1.0f / (numRays);
		
		final double reflectionEnergyCurve = 1.0;
		
		for (int i = 0; i < numRays; i++)
		{
			float fi = (float)i;
			float fiN = (float)fi / (float)numRays;
			float longitude = gAngle * fi * 1.0f;
			float latitude = (float)Math.asin(fiN * 2.0f - 1.0f);
			
			Vec3d rayDir = new Vec3d(0.0, 0.0, 0.0);
			{
				double x = Math.cos(latitude) * Math.cos(longitude);
				double y = Math.cos(latitude) * Math.sin(longitude);
				double z = Math.sin(latitude);
				rayDir = new Vec3d(x, y, z);
			}
			
			Vec3d rayStart = new Vec3d(soundPos.xCoord, soundPos.yCoord, soundPos.zCoord);
			Vec3d rayEnd = new Vec3d(rayStart.xCoord + rayDir.xCoord * maxDistance, rayStart.yCoord + rayDir.yCoord * maxDistance, rayStart.zCoord + rayDir.zCoord * maxDistance);
			RayTraceResult rayHit = minecraft.theWorld.rayTraceBlocks(rayStart, rayEnd, true);
			
			if (rayHit != null)
			{
				double rayLength = soundPos.distanceTo(rayHit.hitVec);
				rayLengthSum += (float)rayLength;
				reflectionEnergySum += 1.0f / numRays;
				numRaysHit++;
				
				//Additional bounces
				Int3 lastHitBlock = Int3.create(rayHit.getBlockPos().getX(), rayHit.getBlockPos().getY(), rayHit.getBlockPos().getZ());
				Vec3d lastHitPos = rayHit.hitVec;
				Vec3d lastHitNormal = getNormalFromFacing(rayHit.sideHit);
				Vec3d lastRayDir = rayDir;
				
				float totalRayDistance = (float)rayLength;
				
				//Secondary ray bounces
				for (int j = 0; j < rayBounces; j++)
				{
					float fj = (float)j / rayBounces;
					Vec3d newRayDir = reflect(lastRayDir, lastHitNormal);
					Vec3d newRayStart = new Vec3d(lastHitPos.xCoord + lastHitNormal.xCoord * 0.01, lastHitPos.yCoord + lastHitNormal.yCoord * 0.01, lastHitPos.zCoord + lastHitNormal.zCoord * 0.01);
					Vec3d newRayEnd = new Vec3d(newRayStart.xCoord + newRayDir.xCoord * maxDistance, newRayStart.yCoord + newRayDir.yCoord * maxDistance, newRayStart.zCoord + newRayDir.zCoord * maxDistance);					
					RayTraceResult newRayHit = minecraft.theWorld.rayTraceBlocks(newRayStart, newRayEnd, true);
					
					float soundDirToPlayerDot = (float)newRayDir.dotProduct(toPlayerVector);
					float energyTowardsPlayer = 0.25f;
					float blockReflectivity = getBlockReflectivity(lastHitBlock);
					energyTowardsPlayer *= blockReflectivity * 0.75f + 0.25f;
					float bounceToPlayerDistance = (float)lastHitPos.distanceTo(playerPos);
					
					totalReflectivityRatio += blockReflectivity;
					
					if (newRayHit != null)
					{
						//if ray got obstructed before it reached the player, it wasn't heard
						if (lastHitPos.distanceTo(newRayHit.hitVec) < bounceToPlayerDistance)
						{
						}
						
						double newRayLength = lastHitPos.distanceTo(newRayHit.hitVec);
						secondaryRayLengthSum += newRayLength;
						secondaryReflectionEnergySum = 0.0f;
						secondaryRayHits++;
						rayHits[j]++;
						bounceReflectivityRatio[j] += (float)Math.pow(blockReflectivity, reflectionEnergyCurve);
						
						totalRayDistance += newRayLength;
						
						lastHitPos = newRayHit.hitVec;
						lastHitNormal = getNormalFromFacing(newRayHit.sideHit);
						lastRayDir = newRayDir;
						lastHitBlock = Int3.create(newRayHit.getBlockPos().getX(), newRayHit.getBlockPos().getY(), newRayHit.getBlockPos().getZ());
						
						if (GeometricAcousticsCore.Config.simplerSharedAirspaceSimulation && j == rayBounces-1 || !GeometricAcousticsCore.Config.simplerSharedAirspaceSimulation)
						{
							Vec3d finalHitToPlayer = playerPos.subtract(lastHitPos).normalize();
							Vec3d finalRayStart = new Vec3d(lastHitPos.xCoord + lastHitNormal.xCoord * 0.01, lastHitPos.yCoord + lastHitNormal.yCoord * 0.01, lastHitPos.zCoord + lastHitNormal.zCoord * 0.01);
							RayTraceResult finalRayHit = minecraft.theWorld.rayTraceBlocks(finalRayStart, playerPos, true);
							
							if (finalRayHit == null)
								sharedAirspace += 1.0f;
						}
					}
					else
					{
						totalRayDistance += lastHitPos.distanceTo(playerPos);
					}
					
					float reflectionDelay = (float)Math.pow(Math.max(totalRayDistance, 0.0), 1.0) * 0.12f * blockReflectivity;
					
					float cross0 = 1.0f - MathHelper.clamp_float(Math.abs(reflectionDelay - 0.0f), 0.0f, 1.0f);
					float cross1 = 1.0f - MathHelper.clamp_float(Math.abs(reflectionDelay - 1.0f), 0.0f, 1.0f);
					float cross2 = 1.0f - MathHelper.clamp_float(Math.abs(reflectionDelay - 2.0f), 0.0f, 1.0f);
					float cross3 = MathHelper.clamp_float(reflectionDelay - 2.0f, 0.0f, 1.0f);
					
					sendGain0 += cross0 * energyTowardsPlayer * 6.4f * rcpTotalRays;
					sendGain1 += cross1 * energyTowardsPlayer * 12.8f * rcpTotalRays;
					sendGain2 += cross2 * energyTowardsPlayer * 12.8f * rcpTotalRays;
					sendGain3 += cross3 * energyTowardsPlayer * 12.8f * rcpTotalRays;
					
					if (newRayHit == null)
					{
						break;
					}
				}
			}
		}
		
		totalReflectivityRatio /= numRays * rayBounces;
				
		float rayHitRatio = (float)numRaysHit/(float)numRays;
		float avgRayLength = rayLengthSum / (float)numRaysHit;
		float avgSecondaryLength = secondaryRayHits != 0 ? secondaryRayLengthSum / secondaryRayHits : 0.0f;
		float secondaryHitRatio = (float)secondaryRayHits/(float)(numRays * rayBounces);
		
		float hitRatioBounce1 = (float)rayHits[0] / (float)numRays;
		float hitRatioBounce2 = (float)rayHits[1] / (float)numRays;
		float hitRatioBounce3 = (float)rayHits[2] / (float)numRays;
		float hitRatioBounce4 = (float)rayHits[3] / (float)numRays;
		
		bounceReflectivityRatio[0] = (float)Math.pow(bounceReflectivityRatio[0] / (float)numRays, 1.0 / reflectionEnergyCurve);
		bounceReflectivityRatio[1] = (float)Math.pow(bounceReflectivityRatio[1] / (float)numRays, 1.0 / reflectionEnergyCurve);
		bounceReflectivityRatio[2] = (float)Math.pow(bounceReflectivityRatio[2] / (float)numRays, 1.0 / reflectionEnergyCurve);
		bounceReflectivityRatio[3] = (float)Math.pow(bounceReflectivityRatio[3] / (float)numRays, 1.0 / reflectionEnergyCurve);
		
		sharedAirspace *= 64.0f;
		if (GeometricAcousticsCore.Config.simplerSharedAirspaceSimulation)
			sharedAirspace *= rcpPrimaryRays;
		else
			sharedAirspace *= rcpTotalRays;
		
		float sharedAirspaceWeight0 = MathHelper.clamp_float(sharedAirspace / 20.0f, 0.0f, 1.0f);
		float sharedAirspaceWeight1 = MathHelper.clamp_float(sharedAirspace / 15.0f, 0.0f, 1.0f);
		float sharedAirspaceWeight2 = MathHelper.clamp_float(sharedAirspace / 10.0f, 0.0f, 1.0f);
		float sharedAirspaceWeight3 = MathHelper.clamp_float(sharedAirspace / 10.0f, 0.0f, 1.0f);
		
		sendCutoff0 = (float)Math.exp(-occlusionAccumulation * absorptionCoeff * 1.0f) * (1.0f - sharedAirspaceWeight0) + sharedAirspaceWeight0;
		sendCutoff1 = (float)Math.exp(-occlusionAccumulation * absorptionCoeff * 1.0f) * (1.0f - sharedAirspaceWeight1) + sharedAirspaceWeight1;
		sendCutoff2 = (float)Math.exp(-occlusionAccumulation * absorptionCoeff * 1.5f) * (1.0f - sharedAirspaceWeight2) + sharedAirspaceWeight2;
		sendCutoff3 = (float)Math.exp(-occlusionAccumulation * absorptionCoeff * 1.5f) * (1.0f - sharedAirspaceWeight3) + sharedAirspaceWeight3;
		
		//attempt to preserve directionality when airspace is shared by allowing some of the dry signal through but filtered
		float averageSharedAirspace = (sharedAirspaceWeight0 + sharedAirspaceWeight1 + sharedAirspaceWeight2 + sharedAirspaceWeight3) * 0.25f;
		directCutoff = (float)Math.max((float)Math.pow(averageSharedAirspace, 0.5) * 0.2f, directCutoff);
		directGain = (float)Math.pow(directCutoff, 0.1);
		
		sendGain1 *= (float)Math.pow(bounceReflectivityRatio[1], 1.0); 
		sendGain2 *= (float)Math.pow(bounceReflectivityRatio[2], 3.0);
		sendGain3 *= (float)Math.pow(bounceReflectivityRatio[3], 4.0);
		
		sendGain0 = MathHelper.clamp_float(sendGain0 * 1.00f - 0.00f, 0.0f, 1.0f);
		sendGain1 = MathHelper.clamp_float(sendGain1 * 1.00f - 0.00f, 0.0f, 1.0f);
		sendGain2 = MathHelper.clamp_float(sendGain2 * 1.05f - 0.05f, 0.0f, 1.0f);
		sendGain3 = MathHelper.clamp_float(sendGain3 * 1.05f - 0.05f, 0.0f, 1.0f);
		
		sendGain0 *= (float)Math.pow(sendCutoff0, 0.1);
		sendGain1 *= (float)Math.pow(sendCutoff1, 0.1);
		sendGain2 *= (float)Math.pow(sendCutoff2, 0.1);
		sendGain3 *= (float)Math.pow(sendCutoff3, 0.1);
		
		if (minecraft.thePlayer.isInWater())
		{
			sendCutoff0 *= 0.4f;
			sendCutoff1 *= 0.4f;
			sendCutoff2 *= 0.4f;
			sendCutoff3 *= 0.4f;
		}
		
		setEnvironment(sourceID, sendGain0, sendGain1, sendGain2, sendGain3, sendCutoff0, sendCutoff1, sendCutoff2, sendCutoff3, directCutoff, directGain);
	}
	
	// ------------------------------------------------- //
	
	private static void setEnvironment(int sourceID, float sendGain0, float sendGain1, float sendGain2, float sendGain3, float sendCutoff0, float sendCutoff1,
			float sendCutoff2, float sendCutoff3, float directCutoff, float directGain)
	{
		EFX10.alFilterf(sendFilter0, EFX10.AL_LOWPASS_GAIN, sendGain0);
		EFX10.alFilterf(sendFilter0, EFX10.AL_LOWPASS_GAINHF, sendCutoff0);
		AL11.alSource3i(sourceID, EFX10.AL_AUXILIARY_SEND_FILTER, auxFXSlot0, 0, sendFilter0);	
		
		EFX10.alFilterf(sendFilter1, EFX10.AL_LOWPASS_GAIN, sendGain1);
		EFX10.alFilterf(sendFilter1, EFX10.AL_LOWPASS_GAINHF, sendCutoff1);
		AL11.alSource3i(sourceID, EFX10.AL_AUXILIARY_SEND_FILTER, auxFXSlot1, 1, sendFilter1);	
		
		EFX10.alFilterf(sendFilter2, EFX10.AL_LOWPASS_GAIN, sendGain2);
		EFX10.alFilterf(sendFilter2, EFX10.AL_LOWPASS_GAINHF, sendCutoff2);
		AL11.alSource3i(sourceID, EFX10.AL_AUXILIARY_SEND_FILTER, auxFXSlot2, 2, sendFilter2);	
		
		EFX10.alFilterf(sendFilter3, EFX10.AL_LOWPASS_GAIN, sendGain3);
		EFX10.alFilterf(sendFilter3, EFX10.AL_LOWPASS_GAINHF, sendCutoff3);
		AL11.alSource3i(sourceID, EFX10.AL_AUXILIARY_SEND_FILTER, auxFXSlot3, 3, sendFilter3);	
		
		EFX10.alFilterf(directFilter0, EFX10.AL_LOWPASS_GAIN, directGain);
		EFX10.alFilterf(directFilter0, EFX10.AL_LOWPASS_GAINHF, directCutoff);
		AL10.alSourcei(sourceID, EFX10.AL_DIRECT_FILTER, directFilter0);
		
		AL10.alSourcef(sourceID, EFX10.AL_AIR_ABSORPTION_FACTOR, GeometricAcousticsCore.Config.airAbsorption);
	}
	
	protected static void setReverbParameters(ReverbParameters r, int auxFXSlot, int reverbSlot)
	{
		//Set default parameters
		EFX10.alEffectf(reverbSlot, EFX10.AL_EAXREVERB_DENSITY, r.density);		
		EFX10.alEffectf(reverbSlot, EFX10.AL_EAXREVERB_DIFFUSION, r.diffusion);
		EFX10.alEffectf(reverbSlot, EFX10.AL_EAXREVERB_GAIN, r.gain);
		EFX10.alEffectf(reverbSlot, EFX10.AL_EAXREVERB_GAINHF, r.gainHF);
		EFX10.alEffectf(reverbSlot, EFX10.AL_EAXREVERB_DECAY_TIME, r.decayTime);
		EFX10.alEffectf(reverbSlot, EFX10.AL_EAXREVERB_DECAY_HFRATIO, r.decayHFRatio);
		EFX10.alEffectf(reverbSlot, EFX10.AL_EAXREVERB_REFLECTIONS_GAIN, r.reflectionsGain);
		EFX10.alEffectf(reverbSlot, EFX10.AL_EAXREVERB_LATE_REVERB_GAIN, r.lateReverbGain);
		EFX10.alEffectf(reverbSlot, EFX10.AL_EAXREVERB_LATE_REVERB_DELAY, r.lateReverbDelay);
		EFX10.alEffectf(reverbSlot, EFX10.AL_EAXREVERB_AIR_ABSORPTION_GAINHF, r.airAbsorptionGainHF);
		EFX10.alEffectf(reverbSlot, EFX10.AL_EAXREVERB_ROOM_ROLLOFF_FACTOR, r.roomRolloffFactor);
		
		//Attach updated effect object
		EFX10.alAuxiliaryEffectSloti(auxFXSlot, EFX10.AL_EFFECTSLOT_EFFECT, reverbSlot);
	}
	
	// ------------------------------------------------- //
	
	protected static void log(String message)
	{
		System.out.println(logPrefix + ": " + message);
	}
	
}
