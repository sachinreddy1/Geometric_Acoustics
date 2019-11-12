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

import akka.japi.Pair;
import net.minecraft.util.SoundCategory;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import java.util.Arrays;
import java.util.ArrayList; 

public class GAGuiOverlay extends Gui
{
	public static Minecraft mc;		
	//
	private final int verticalPadding = 60, horizontalPadding = 30;
	static int color = Integer.parseInt("FFFFFF", 16);
	static int headerColor = Integer.parseInt("FFC04D", 16);
	//
	private int width;
	private int height;
	static int axisHeight;
	private int axisWidth;
	//
	static int rightTablePosition;
	static int rightTableOffset;
	static int titlePosition;	
	//
	static String id_data = "";
	static String coordinates_data = "";
	static String soundCategory_data = "";
	static String name_data = "";
	//
	public static HistogramPair[] histogramData = new HistogramPair[GeometricAcousticsCore.Config.environmentCalculationRays];
	//
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
		
		// Sort the Pair array
		Compare obj = new Compare(); 
        ArrayList<HistogramTriple> histogramValues = obj.countFreqValues(histogramData, histogramData.length);
        obj.compareTriple(histogramValues);
        int histOffestX = axisWidth / histogramValues.size();
		
        for (int i = 0; i < histogramValues.size(); i++) {
			// Set the height threshold
        	if (histogramValues.get(i).frequency * 5 > axisHeight) 
        		histogramValues.get(i).frequency = axisHeight;
        	
        	int histOffestY = height - verticalPadding - histogramValues.get(i).frequency * 5;
 			float r = (float)((histogramValues.get(i).soundType>>16)&0xFF)/255f;
 			float b = (float)((histogramValues.get(i).soundType)&0xFF)/255f;
 			float g = (float)((histogramValues.get(i).soundType>>8)&0xFF)/255f;
 			
 			// Draw x-values
 			GL11.glPushMatrix();
 			{
 				float size = 0.5f;
 				GL11.glScalef(size,size,size);
 				float mSize = (float)Math.pow(size,-1);
 				String rayDistanceText = Integer.toString(histogramValues.get(i).rayDistance);
 				String frequencyText = Integer.toString(histogramValues.get(i).frequency);
	 			int x = horizontalPadding + histOffestX/2 + histOffestX * i + 2;
	 			int rayDistanceTextY = height - verticalPadding + 8;
	 			int frequencyTextY = height - verticalPadding - histogramValues.get(i).frequency * 5 - 5;
	 			
	 			int textColor = Integer.parseInt("FFC04D", 16); 
	 			if (i%2==0)
	 				textColor = Integer.parseInt("FFFFFF", 16);
	 			
	 			drawString(mc.fontRendererObj, rayDistanceText, Math.round(x/size), Math.round(rayDistanceTextY/size), textColor);
 				drawString(mc.fontRendererObj, frequencyText, Math.round(x/size), Math.round(frequencyTextY/size), textColor);
	 			GL11.glScalef(mSize,mSize,mSize);
 			}
 			GL11.glPopMatrix();
 			
 			// Draw histogram bar
 			GL11.glPushMatrix();
 			{
 				GL11.glColor3f(r, g, b);
 				mc.renderEngine.bindTexture(histogramBlock);
 				drawTexturedModalRect(horizontalPadding + 4 + histOffestX * i, histOffestY + 2, 0, 0, histOffestX, histogramValues.get(i).frequency * 5);
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
		String xAxisLabel = "Ray Length";
		String yAxisLabel = "Frequency";
		String guiText = "Geometric Acoustics Analytics:";
		titlePosition = height/15;
		
    	drawCenteredString(mc.fontRendererObj, guiText, width/2, titlePosition, color);
    	drawString(mc.fontRendererObj, xAxisLabel, horizontalPadding + 20, height - verticalPadding + 16, color);
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
		if (sc.toString() != "PLAYERS")
			return;
		
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
