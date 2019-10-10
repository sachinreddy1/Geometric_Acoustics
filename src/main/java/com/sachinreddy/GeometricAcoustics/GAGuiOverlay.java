package com.sachinreddy.GeometricAcoustics;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.SoundCategory;

public class GAGuiOverlay extends Gui
{
	public static Minecraft mc;		
	// ------------------ //
	private final int verticalPadding = 60, horizontalPadding = 30;
	static int color = Integer.parseInt("FFFFFF", 16);
	static int headerColor = Integer.parseInt("FFC04D", 16);
	// ------------------ //
	private int width;
	private int height;
	static int axisHeight;
	private int axisWidth;
	// ------------------ //
	static int rightTablePosition;
	static int rightTableOffset;
	static int titlePosition;	
	// ------------------ //
	static String id_data = "";
	static String coordinates_data = "";
	static String soundCategory_data = "";
	static String name_data = "";
	// ------------------ //
	static int[] raySoundTypes = new int[GeometricAcousticsCore.Config.environmentCalculationRays];
	static float[] histogramData = new float[GeometricAcousticsCore.Config.environmentCalculationRays];
	
	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            mc = Minecraft.getMinecraft();
            
            ScaledResolution scaled = new ScaledResolution(mc);
			width = scaled.getScaledWidth();
			height = scaled.getScaledHeight();
			
			// Draw axis
			renderAxis(event);
			// Updating histogram
			renderHistogram(event);
			// Updating sound information
			renderSoundData(event);
			// Draw axis labels
			renderAxisLabels(event);
			// Draw sound information
			renderSoundInfoLabels(event);
        }
    }
	
	// ------------------------------------------------- //
	
	public void renderHistogram(RenderGameOverlayEvent event) {
		ResourceLocation histogramBlock = new ResourceLocation(GeometricAcousticsCore.modid, "textures/gui/histogram.png");
		
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			int histOffestX = axisWidth / GeometricAcousticsCore.Config.environmentCalculationRays;
 			
 			for (int i = 0; i < GeometricAcousticsCore.Config.environmentCalculationRays; i++) {
	 			int histOffestY = height - verticalPadding - (int)histogramData[i];
	 			float r = (float)((raySoundTypes[i]>>16)&0xFF)/255f;
	 			float b = (float)((raySoundTypes[i])&0xFF)/255f;
	 			float g = (float)((raySoundTypes[i]>>8)&0xFF)/255f;
 				
 				GL11.glPushMatrix();
 	 			{
	 				GL11.glColor3f(r, g, b);
	 				mc.renderEngine.bindTexture(histogramBlock);
					drawTexturedModalRect(horizontalPadding + 4 + histOffestX * i, histOffestY + 2, 0, 0, histOffestX, (int)histogramData[i]);
 	 			}
 	 			GL11.glPopMatrix();
 			}
		}
	}
	
	public void renderAxis(RenderGameOverlayEvent event) {
		ResourceLocation horizontalBar = new ResourceLocation(GeometricAcousticsCore.modid, "textures/gui/horizontalaxis.png");
		ResourceLocation verticalBar = new ResourceLocation(GeometricAcousticsCore.modid, "textures/gui/verticalaxis.png");
		int horizontalHeight = 6;
		int verticalWidth = 6;
		axisWidth = 245;
		axisHeight = 245;
		
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			if (axisHeight > height)
				axisHeight = height - (2 * verticalPadding);
			if (axisWidth > width)
				axisWidth = width - (2 * horizontalPadding);
			
			GL11.glPushMatrix();
 			{
 				GL11.glColor3f(1, 1, 1);
				mc.renderEngine.bindTexture(verticalBar);
				drawTexturedModalRect(horizontalPadding, height - verticalPadding - axisHeight + 2, 0, 0, verticalWidth, axisHeight);
				mc.renderEngine.bindTexture(horizontalBar);
	            drawTexturedModalRect(horizontalPadding + 2, height - verticalPadding, 0, 0, axisWidth, horizontalHeight);
 			}
 			GL11.glPopMatrix();
        }
	}
	
	public void renderAxisLabels(RenderGameOverlayEvent event) {
		
		String xAxisLabel = "Blocks Hit";
		String yAxisLabel = "Ray Distance";
		String guiText = "Geometric Acoustics Analytics:";
		titlePosition = height/15;
		
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
        	drawCenteredString(mc.fontRendererObj, guiText, width/2, titlePosition, color);
        	drawString(mc.fontRendererObj, xAxisLabel, horizontalPadding + 20, height - verticalPadding + 11, color);
 			GL11.glPushMatrix();
 			{
	 			GL11.glTranslatef(horizontalPadding - 12, height - verticalPadding - 20, 0);
	 			GL11.glRotatef(-90f, 0, 0, 1);
	 			drawString(mc.fontRendererObj, yAxisLabel, 0, 0, color);
 			}
 			GL11.glPopMatrix();
        }
	}
	
	public void renderSoundInfoLabels(RenderGameOverlayEvent event) {
		String lastSound_Label = "Last Sound Source: ";
		String soundId_Label = "ID: ";
		String coordinates_Label = "Coordinates: ";
		String category_Label = "Category: ";
		String name_Label = "Name: ";
		String data_Label = "Data: ";
		
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
        	rightTablePosition = width - 175;
 			rightTableOffset = 10;
 			drawString(mc.fontRendererObj, lastSound_Label, rightTablePosition, titlePosition + 30, headerColor);
 			drawString(mc.fontRendererObj, soundId_Label, rightTablePosition + rightTableOffset, titlePosition + 45, headerColor);
 			drawString(mc.fontRendererObj, coordinates_Label, rightTablePosition + rightTableOffset, titlePosition + 60, headerColor);
 			drawString(mc.fontRendererObj, category_Label, rightTablePosition + rightTableOffset, titlePosition + 87, headerColor);
 			drawString(mc.fontRendererObj, name_Label, rightTablePosition + rightTableOffset, titlePosition + 102, headerColor);
 			drawString(mc.fontRendererObj, data_Label, rightTablePosition + rightTableOffset, titlePosition + 129, headerColor);
        }
	}
	
	public void renderSoundData(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
        	drawString(mc.fontRendererObj, id_data, rightTablePosition + rightTableOffset + 17, titlePosition + 45, color);
 			drawString(mc.fontRendererObj, coordinates_data, rightTablePosition + rightTableOffset + 10, titlePosition + 72, color);
 			drawString(mc.fontRendererObj, soundCategory_data, rightTablePosition + rightTableOffset + 55, titlePosition + 87, color);
 			drawString(mc.fontRendererObj, name_data, rightTablePosition + rightTableOffset + 10, titlePosition + 114, color);
 			//
 			GL11.glRecti(width/2 - 20, height/2 + 30, width/2 + 20, height/2 - 30);
        }
	}
	
	// ------------------------------------------------- //
	
	public static void updateOverlay(float posX, float posY, float posZ, int sourceID, SoundCategory sc, String name) {
		id_data = Integer.toString(sourceID);
		coordinates_data = "(" + (int)posX + ", " + (int)posY + ", " + (int)posZ + ")";
		soundCategory_data = sc.toString();
		name_data = name.substring(name.lastIndexOf(".") + 1);
	}
	
	public static void updateHistogram(Int3 lastHitBlock, float data, int index) {
		raySoundTypes[index] = getSoundResource(lastHitBlock);
		if (data > axisHeight)
			histogramData[index] = axisHeight;
		else
			histogramData[index] = data;
	}
	
	// ------------------------------------------------- //
	
	private static int getSoundResource(Int3 blockPos)
	{		
		Block block = mc.theWorld.getBlockState(new BlockPos(blockPos.x, blockPos.y, blockPos.z)).getBlock();
		SoundType soundType = block.getSoundType();
		
		if (soundType == SoundType.STONE)
			return Integer.parseInt("a9a9a9", 16);
		else if (soundType == SoundType.WOOD)
			return Integer.parseInt("deb887", 16);
		else if (soundType == SoundType.GROUND)
			return Integer.parseInt("a5682a", 16);
		else if (soundType == SoundType.PLANT)
			return Integer.parseInt("228b22", 16);
		else if (soundType == SoundType.METAL)
			return Integer.parseInt("4682b4", 16);
		else if (soundType == SoundType.GLASS)
			return Integer.parseInt("dcdcdc", 16);
		else if (soundType == SoundType.CLOTH)
			return Integer.parseInt("ffc04d", 16);
		else if (soundType == SoundType.SAND)	
			return Integer.parseInt("f4a460", 16);
		else if (soundType == SoundType.SNOW)
			return Integer.parseInt("ffffff", 16);
		else if (soundType == SoundType.LADDER)
			return Integer.parseInt("ce954b", 16);
		else if (soundType == SoundType.ANVIL)
			return Integer.parseInt("1a1a1a", 16);
				
		return Integer.parseInt("9370db", 16);
	}
}
