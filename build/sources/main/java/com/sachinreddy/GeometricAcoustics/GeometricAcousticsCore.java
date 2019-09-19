package com.sachinreddy.GeometricAcoustics;

import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = GeometricAcousticsCore.modid, version = GeometricAcousticsCore.version)
public class GeometricAcousticsCore implements IClassTransformer
{
	
	@Mod.Instance("ga")
	public static GeometricAcousticsCore instance;
	//
	public static final String modid = "ga";
	public static final String version = "1.0";
		
	public static class Config
	{
		//general
		public static float rolloffFactor = 1.0f;
		public static float globalReverbGain = 1.0f;
		public static float globalReverbBrightness = 1.0f;
		public static double soundDistanceAllowance = 4.0f;
		public static float globalBlockAbsorption = 1.0f;
		public static float globalBlockReflectance = 1.0f;
		public static float airAbsorption = 1.0f;
		public static float underwaterFilter = 0.8f;
		
		//performance
		public static boolean skipRainOcclusionTracing = true;
		public static int environmentEvaluationRays = 32;
		public static boolean simplerSharedAirspaceSimulation = false;
		
		//block properties
		public static float stoneReflectivity = 1.0f;
		public static float woodReflectivity = 0.4f;
		public static float groundReflectivity = 0.3f;
		public static float plantReflectivity = 0.5f;
		public static float metalReflectivity = 1.0f;
		public static float glassReflectivity = 0.5f;
		public static float clothReflectivity = 0.05f;
		public static float sandReflectivity = 0.2f;
		public static float snowReflectivity = 0.2f;
		
		//misc
		public static boolean debugLogging = false;
		public static boolean occlusionLogging = false;
		public static boolean environmentLogging = false;
		public static boolean performanceLogging = false;
		
		public static final String categoryGeneral = "general";
		public static final String categoryPerformance = "performance";
		public static final String categoryMaterialProperties = "material properties";
		public static final String categoryMisc = "misc";
	}
	
	// ------------------------------------------------- //
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		FMLCommonHandler.instance().bus().register(instance);
	}
	
	// ------------------------------------------------- //
	
	private void log(String message)
	{		
		System.out.println(message);
	}
	
	// ------------------------------------------------- //
	
	//arg0: class name, arg1: new name of the class, arg2: chunk of bytecode that's about to be loaded into JVM
	@Override 
	public byte[] transform(String arg0, String arg1, byte[] arg2)
	{		
		//SetupReverb() in SoundManager.SoundSystemStarterThread
		{
			InsnList toInject = new InsnList();
			toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/sachinreddy/GeometricAcoustics/GeometricAcoustics", "initialize", "()V"));
			
			arg2 = patchMethodInClass(arg0, arg2, 
					new String[]{"net.minecraft.client.audio.SoundManager$SoundSystemStarterThread", "ccn$a"}, 						//Target Class name
					new String[]{"<init>", "<init>"}, 																				//Target method name
					new String[]{"(Lnet/minecraft/client/audio/SoundManager;)V", "(Lccn;)V"},	//Target method signature
					Opcodes.INVOKESPECIAL,						//Target opcode
					AbstractInsnNode.METHOD_INSN, 				//Target node type
					new String[]{"<init>", "<init>"},			//Target node method invocation name
					null,
					new InsnList[]{toInject, toInject}, 		//Instructions to inject
					false, 										//Insert before the target node?
					0,
					0,
					false,
					0
					);
		}
		
		// ------------------------------------------------- //
		
		//setLastSoundCategory() and setLastSoundName() in SoundManager.playSound()
		{
			InsnList toInject = new InsnList();
			toInject.add(new VarInsnNode(Opcodes.ALOAD, 7));
			toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/sachinreddy/GeometricAcoustics/GeometricAcoustics", "setLastSoundCategory", "(Lnet/minecraft/util/SoundCategory;)V"));
			
			InsnList toInjectObf = new InsnList();
			toInjectObf.add(new VarInsnNode(Opcodes.ALOAD, 7));
			toInjectObf.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/sachinreddy/GeometricAcoustics/GeometricAcoustics", "setLastSoundCategory", "(Lno;)V"));
			
			arg2 = patchMethodInClass(arg0, arg2, 
					new String[]{"net.minecraft.client.audio.SoundManager", "ccn"}, 	//Target Class name
					new String[]{"playSound", "c"}, 								//Target method name
					new String[]{"(Lnet/minecraft/client/audio/ISound;)V", "(Lcbz;)V"},	//Target method signature
					Opcodes.INVOKEVIRTUAL,						//Target opcode
					AbstractInsnNode.METHOD_INSN, 				//Target node type
					new String[]{"setVolume", "setVolume"},		//Target node method invocation name
					null,
					new InsnList[]{toInject, toInjectObf}, 		//Instructions to inject
					false, 										//Insert before the target node?
					0,
					0,
					false,
					0
					);
		}
		
		//setLastSoundName() in SoundManager.playSound()
		{
			InsnList toInject = new InsnList();
			toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
			toInject.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/client/audio/ISound", "getSoundLocation", "()Lnet/minecraft/util/ResourceLocation;"));
			toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/ResourceLocation", "toString", "()Ljava/lang/String;"));
			toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/sachinreddy/GeometricAcoustics/GeometricAcoustics", "setLastSoundName", "(Ljava/lang/String;)V"));
			
			InsnList toInjectObf = new InsnList();
			toInjectObf.add(new VarInsnNode(Opcodes.ALOAD, 1));
			toInjectObf.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "cbz", "a", "()Lkq;"));
			toInjectObf.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "kq", "toString", "()Ljava/lang/String;"));
			toInjectObf.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/sachinreddy/GeometricAcoustics/GeometricAcoustics", "setLastSoundName", "(Ljava/lang/String;)V"));
			
			arg2 = patchMethodInClass(arg0, arg2, 
					new String[]{"net.minecraft.client.audio.SoundManager", "ccn"}, 	//Target Class name
					new String[]{"playSound", "c"}, 								//Target method name
					new String[]{"(Lnet/minecraft/client/audio/ISound;)V", "(Lcbz;)V"},	//Target method signature
					Opcodes.INVOKEVIRTUAL,						//Target opcode
					AbstractInsnNode.METHOD_INSN, 				//Target node type
					new String[]{"setVolume", "setVolume"},		//Target node method invocation name
					null,
					new InsnList[]{toInject, toInjectObf}, 		//Instructions to inject
					false, 										//Insert before the target node?
					0,
					0,
					false,
					0
					);
		}
		
		// ------------------------------------------------- // *
		
		//Global volume multiplier
		{
			InsnList toInject = new InsnList();
			toInject.add(new FieldInsnNode(Opcodes.GETSTATIC, "com/sachinreddy/GeometricAcoustics/GeometricAcoustics", "globalVolumeMultiplier", "F"));
			toInject.add(new InsnNode(Opcodes.FMUL));
			
			arg2 = patchMethodInClass(arg0, arg2, 
					new String[]{"net.minecraft.client.audio.SoundManager", "ccn"}, 	//Target Class name
					new String[]{"playSound", "c"}, 								//Target method name
					new String[]{"(Lnet/minecraft/client/audio/ISound;)V", "(Lcbz;)V"},	//Target method signature
					Opcodes.INVOKESPECIAL,						//Target opcode
					AbstractInsnNode.METHOD_INSN, 				//Target node type
					new String[]{"getClampedVolume", "e"},										//Target node method invocation name
					new String[]{"(Lnet/minecraft/client/audio/ISound;)F", "(Lcbz;)F"}, 	//Target node method invocation  signature
					new InsnList[]{toInject}, 									//Instructions to inject
					false, 										//Insert before the target node?
					0,			//Nodes to delete before the target node (done before injection)
					0,			//Nodes to delete after the target node (done before injection)
					false,
					0
					);
		}
		
		
		// ------------------------------------------------- //
		
		//onPlaySound() in paulscode.libraries.SourceLWJGLOpenAL.play()
		{
			InsnList toInject = new InsnList();
			toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
			toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "paulscode/sound/libraries/SourceLWJGLOpenAL", "position", "Lpaulscode/sound/Vector3D;"));
			toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "paulscode/sound/Vector3D", "x", "F"));
			
			toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
			toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "paulscode/sound/libraries/SourceLWJGLOpenAL", "position", "Lpaulscode/sound/Vector3D;"));
			toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "paulscode/sound/Vector3D", "y", "F"));
			
			toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
			toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "paulscode/sound/libraries/SourceLWJGLOpenAL", "position", "Lpaulscode/sound/Vector3D;"));
			toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "paulscode/sound/Vector3D", "z", "F"));
			
			toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
			toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "paulscode/sound/libraries/SourceLWJGLOpenAL", "channelOpenAL", "Lpaulscode/sound/libraries/ChannelLWJGLOpenAL;"));
			toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "paulscode/sound/libraries/ChannelLWJGLOpenAL", "ALSource", "Ljava/nio/IntBuffer;"));
			
			toInject.add(new InsnNode(Opcodes.ICONST_0));
			toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/nio/IntBuffer", "get", "(I)I", false));
			toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/sachinreddy/GeometricAcoustics/GeometricAcoustics", "onPlaySound", "(FFFI)V", false));
			
			arg2 = patchMethodInClass(arg0, arg2, 
					new String[]{"paulscode.sound.libraries.SourceLWJGLOpenAL"}, 	//Target Class name
					new String[]{"play"}, 								//Target method name
					new String[]{"(Lpaulscode/sound/Channel;)V"},	//Target method signature
					Opcodes.INVOKEVIRTUAL,						//Target opcode
					AbstractInsnNode.METHOD_INSN, 				//Target node type
					new String[]{"play"},										//Target node method invocation name
					null,
					new InsnList[]{toInject}, 									//Instructions to inject
					false, 										//Insert before the target node?
					0,			
					0,			
					false,
					0
					);
		}
		
		// ------------------------------------------------- //
		
		return arg2;
	}
	
	private byte[] patchMethodInClass(String currentClassName, byte[] bytes, String[] targetClassNames, String[] targetMethodNames, String[] targetMethodSignatures, 
			int targetNodeOpcode, int targetNodeType, String[] targetInvocationMethodNames, String[] targetInvocationMethodSignatures, InsnList[] instructionsToInjects, 
			boolean insertBefore, int nodesToDeleteBefore, int nodesToDeleteAfter, boolean deleteTargetNode, int targetNodeOffset)
	{
		String targetClassName = targetClassNames[0];
		String targetMethodName = targetMethodNames[0];
		String targetInvocationMethodName = targetInvocationMethodNames[0];
		
		String targetMethodSignature = targetMethodSignatures[0];
		InsnList instructionsToInject = instructionsToInjects[0];
		boolean obfuscated = false;
		
		if (targetClassNames.length == 2)
		{
			if (currentClassName.equals(targetClassNames[1]))
			{
				targetClassName = targetClassNames.length == 2 ? targetClassNames[1] : targetClassNames[0];
				targetMethodName = targetMethodNames.length == 2 ? targetMethodNames[1] : targetClassNames[0];
				targetInvocationMethodName = targetInvocationMethodNames.length == 2 ? targetInvocationMethodNames[1] : targetInvocationMethodNames[0];
				targetMethodSignature = targetMethodSignatures.length == 2 ? targetMethodSignatures[1] : targetMethodSignatures[0];
				instructionsToInject = instructionsToInjects.length == 2 ? instructionsToInjects[1] : instructionsToInjects[0];
				obfuscated = true;
			}
		}
		
		String targetInvocationMethodSignature = null;
		if (targetInvocationMethodSignatures != null)
		{
			targetInvocationMethodSignature = targetInvocationMethodSignatures[0];
			if (obfuscated)
			{
				targetInvocationMethodSignature = targetInvocationMethodSignatures.length == 2 ? targetInvocationMethodSignatures[1] : targetInvocationMethodSignatures[0];
			}
		}
		
		
		//If this isn't the target class, leave!
		if (!currentClassName.equals(targetClassName))
			return bytes;
		
		log("#################################################################   Patching Class: " + targetClassName);
		
		//Setup ASM class manipulation stuff
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		
		
		//Now we loop over all of the methods declared inside the class until we get to the target method name
		@SuppressWarnings("unchecked")
		Iterator<MethodNode> methods = classNode.methods.iterator();
		while(methods.hasNext())
		{
			MethodNode m = methods.next();
			log("********* Method Name: " + m.name + " Desc: " + m.desc);
			int targetIndex = -1;
			
			//Check if this is the method name and the signature matches
			if (m.name.equals(targetMethodName) && m.desc.equals(targetMethodSignature))
			{
				log("*************************************** Inside target method: " + targetMethodName);
				
				AbstractInsnNode currentNode = null;
				AbstractInsnNode targetNode = null;
				
				@SuppressWarnings("unchecked")
				Iterator<AbstractInsnNode> iter = m.instructions.iterator();
				
				int index = -1;
				
				//Loop over the instruction set
				while (iter.hasNext())
				{
					index++;
					currentNode = iter.next();
					
					if (currentNode.getOpcode() == targetNodeOpcode)
					{
						if (targetNodeType == AbstractInsnNode.METHOD_INSN) //If we're looking for a method opcode
						{
							if (currentNode.getType() == AbstractInsnNode.METHOD_INSN)
							{
								MethodInsnNode method = (MethodInsnNode)currentNode;
								//log("Method found: " + method.name);
								if (method.name.equals(targetInvocationMethodName))
								{
									if (method.desc.equals(targetInvocationMethodSignature) || targetInvocationMethodSignature == null)
									{
										log("Found target method invocation for injection: " + targetInvocationMethodName);
										targetNode = currentNode;
										targetIndex = index;
									}
									
								}
							}
						}
						else
						{
							if (currentNode.getType() == targetNodeType)
							{
								log("Found target node for injection: " + targetNodeOpcode);
								targetNode = currentNode;
								targetIndex = index;
							}
						}
						
					}
				}
				
				//Offset the target node by the supplied offset value
				if (targetNodeOffset > 0)
				{
					for (int i = 0; i < targetNodeOffset; i++)
					{
						targetNode = targetNode.getNext();
					}
				}
				else if (targetNodeOffset < 0)
				{
					for (int i = 0; i < -targetNodeOffset; i++)
					{
						targetNode = targetNode.getPrevious();
					}
				}
				
				if (targetNode != null)	//If we've found the target, inject the instructions!
				{
					for (int i = 0; i < nodesToDeleteBefore; i++)
					{
						AbstractInsnNode previousNode = targetNode.getPrevious();
						log("Removing Node " + previousNode.getOpcode());
						m.instructions.remove(previousNode);
					}
					
					for (int i = 0; i < nodesToDeleteAfter; i++)
					{
						AbstractInsnNode nextNode = targetNode.getNext();
						log("Removing Node " + nextNode.getOpcode());
						m.instructions.remove(nextNode);
					}
					
					
					
					if (insertBefore)
						m.instructions.insertBefore(targetNode, instructionsToInject);
					else
						m.instructions.insert(targetNode, instructionsToInject);
					
					if (deleteTargetNode)
					{
						m.instructions.remove(targetNode);
					}
					
					log("Patching complete!----------------------------------------------------------------------------------------");
				}
				break;
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}
}
