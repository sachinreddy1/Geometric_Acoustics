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
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import java.util.Arrays;

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
	public static Pair[] histogramData = new Pair[GeometricAcousticsCore.Config.environmentCalculationRays];
	// ------------------ //
	public static boolean isDisplaying = false;
	
	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT && isDisplaying) {
            mc = Minecraft.getMinecraft();
            
            ScaledResolution scaled = new ScaledResolution(mc);
			width = scaled.getScaledWidth();
			height = scaled.getScaledHeight();
			
			// Draw axis
			renderAxis();
			// Updating histogram
			renderHistogram();
			// Updating sound information
			renderSoundData();
			// Draw axis labels
			renderAxisLabels();
			// Draw sound information
			renderSoundInfoLabels();
        }
    }
	
	// ------------------------------------------------- //
	
	public void renderHistogram() {
		ResourceLocation histogramBlock = new ResourceLocation(GeometricAcousticsCore.modid, "textures/gui/histogram.png");
		int histOffestX = axisWidth / GeometricAcousticsCore.Config.environmentCalculationRays;
		
		// Sort the Pair array
		Compare obj = new Compare(); 
        obj.compare(histogramData); 
		
		for (int i = 0; i < GeometricAcousticsCore.Config.environmentCalculationRays; i++) {			
			int histOffestY = height - verticalPadding - histogramData[i].rayDistance;
 			float r = (float)((histogramData[i].soundType>>16)&0xFF)/255f;
 			float b = (float)((histogramData[i].soundType)&0xFF)/255f;
 			float g = (float)((histogramData[i].soundType>>8)&0xFF)/255f;
 			
			GL11.glPushMatrix();
 			{
 				GL11.glColor3f(r, g, b);
 				mc.renderEngine.bindTexture(histogramBlock);
				drawTexturedModalRect(horizontalPadding + 4 + histOffestX * i, histOffestY + 2, 0, 0, histOffestX, histogramData[i].rayDistance);
 			}
 			GL11.glPopMatrix();
		}
	}
	
	public void renderAxis() {
		ResourceLocation horizontalBar = new ResourceLocation(GeometricAcousticsCore.modid, "textures/gui/horizontalaxis.png");
		ResourceLocation verticalBar = new ResourceLocation(GeometricAcousticsCore.modid, "textures/gui/verticalaxis.png");
		int horizontalHeight = 6;
		int verticalWidth = 6;
		axisWidth = 245;
		axisHeight = 245;
	
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
	
	public void renderAxisLabels() {
		String xAxisLabel = "Blocks Hit";
		String yAxisLabel = "Ray Distance";
		String guiText = "Geometric Acoustics Analytics:";
		titlePosition = height/15;
		
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
	
	public void renderSoundInfoLabels() {
		String lastSound_Label = "Last Sound Source: ";
		String soundId_Label = "ID: ";
		String coordinates_Label = "Coordinates: ";
		String category_Label = "Category: ";
		String name_Label = "Name: ";
		
    	rightTablePosition = width - 175;
		rightTableOffset = 10;
		drawString(mc.fontRendererObj, lastSound_Label, rightTablePosition, titlePosition + 30, headerColor);
		drawString(mc.fontRendererObj, soundId_Label, rightTablePosition + rightTableOffset, titlePosition + 45, headerColor);
		drawString(mc.fontRendererObj, coordinates_Label, rightTablePosition + rightTableOffset, titlePosition + 60, headerColor);
		drawString(mc.fontRendererObj, category_Label, rightTablePosition + rightTableOffset, titlePosition + 87, headerColor);
		drawString(mc.fontRendererObj, name_Label, rightTablePosition + rightTableOffset, titlePosition + 102, headerColor);
	}
	
	public void renderSoundData() {
    	drawString(mc.fontRendererObj, id_data, rightTablePosition + rightTableOffset + 17, titlePosition + 45, color);
		drawString(mc.fontRendererObj, coordinates_data, rightTablePosition + rightTableOffset + 10, titlePosition + 72, color);
		drawString(mc.fontRendererObj, soundCategory_data, rightTablePosition + rightTableOffset + 55, titlePosition + 87, color);
		drawString(mc.fontRendererObj, name_data, rightTablePosition + rightTableOffset + 10, titlePosition + 114, color);
	}
	
	// ------------------------------------------------- //
	
	public static void updateOverlay(float posX, float posY, float posZ, int sourceID, SoundCategory sc, String name) {
		id_data = Integer.toString(sourceID);
		coordinates_data = "(" + (int)posX + ", " + (int)posY + ", " + (int)posZ + ")";
		soundCategory_data = sc.toString();
		name_data = name.substring(name.lastIndexOf(".") + 1);
	}
	
	// ------------------------------------------------- //
	
	@SubscribeEvent
	public void onKeyPress(InputEvent.KeyInputEvent event) {
	    if(Keyboard.isKeyDown(Keyboard.KEY_F6)) {
	    	if (isDisplaying)
	    		isDisplaying = false;
	    	else
	    		isDisplaying = true;
	    }
	}
	
}
