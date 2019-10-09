package com.sachinreddy.GeometricAcoustics;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class GuiExample extends Gui
{
	private final ResourceLocation horizontalBar = new ResourceLocation(GeometricAcousticsCore.modid, "textures/gui/horizontalaxis.png");
	private final ResourceLocation verticalBar = new ResourceLocation(GeometricAcousticsCore.modid, "textures/gui/verticalaxis.png");
	private int horizontalWidth = 245, horizontalHeight = 6;
	private int verticalWidth = 6, verticalHeight = 245;
	private final int verticalPadding = 60, horizontalPadding = 30;
	
	int color = Integer.parseInt("FFFFFF", 16);
	//
	String guiText = "Geometric Acoustics Analytics:";
	String xAxisLabel = "Blocks";
	String yAxisLabel = "Energy";
	//
	String lastSound_Label = "Last Sound Source: ";
	String soundId_Label = "ID: ";
	String coordinates_Label = "Coordinates: ";
	String category_Label = "Category: ";
	String name_Label = "Name: ";
	String data_Label = "Data: ";
	//
	int rightTablePosition;
	int rightTableOffset;
	
	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution scaled = new ScaledResolution(mc);
			int width = scaled.getScaledWidth();
			int height = scaled.getScaledHeight();
			int titlePosition = height/15;
			
			// Draw debug title
			drawCenteredString(mc.fontRendererObj, guiText, width/2, titlePosition, color);
            
			// Draw graph axis
			if (verticalHeight > height)
				verticalHeight = height - (2 * verticalPadding);
			if (horizontalWidth > width)
				horizontalWidth = width - (2 * horizontalPadding);
			
			mc.renderEngine.bindTexture(verticalBar);
			drawTexturedModalRect(horizontalPadding, height - verticalPadding - verticalHeight + 2, 0, 0, verticalWidth, verticalHeight);
			mc.renderEngine.bindTexture(horizontalBar);
            drawTexturedModalRect(horizontalPadding + 2, height - verticalPadding, 0, 0, horizontalWidth, horizontalHeight);
            
            // Draw axis labels
 			drawCenteredString(mc.fontRendererObj, xAxisLabel, horizontalPadding + 30, height - verticalPadding + 10, color);
 			GL11.glPushMatrix();
 			GL11.glTranslatef(horizontalPadding - 12, height - verticalPadding - 30, 0);
 			GL11.glRotatef(-90f, 0, 0, 1);
 			drawCenteredString(mc.fontRendererObj, yAxisLabel, 0, 0, color);
 			GL11.glPopMatrix();
 			
 			// Draw sound information
 			rightTablePosition = width - 175;
 			rightTableOffset = 10;
 			drawString(mc.fontRendererObj, lastSound_Label, rightTablePosition, titlePosition + 30, color);
 			drawString(mc.fontRendererObj, soundId_Label, rightTablePosition + rightTableOffset, titlePosition + 50, color);
 			drawString(mc.fontRendererObj, coordinates_Label, rightTablePosition + rightTableOffset, titlePosition + 70, color);
 			drawString(mc.fontRendererObj, category_Label, rightTablePosition + rightTableOffset, titlePosition + 90, color);
 			drawString(mc.fontRendererObj, name_Label, rightTablePosition + rightTableOffset, titlePosition + 110, color);
 			drawString(mc.fontRendererObj, data_Label, rightTablePosition + rightTableOffset, titlePosition + 130, color);
        }
    }
	
	public void updateOverlay() {
		
	}
	
}
