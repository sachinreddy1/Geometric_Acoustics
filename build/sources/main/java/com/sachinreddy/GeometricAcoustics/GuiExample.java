package com.sachinreddy.GeometricAcoustics;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//import com.sachinreddy.GeometricAcousticsMod;

public class GuiExample extends Gui
{
	private final ResourceLocation horizontalBar = new ResourceLocation(GeometricAcousticsCore.modid, "textures/gui/horizontalaxis.png");
	private final ResourceLocation verticalBar = new ResourceLocation(GeometricAcousticsCore.modid, "textures/gui/verticalaxis.png");
	private int horizontalWidth = 245, horizontalHeight = 6;
	private int verticalWidth = 6, verticalHeight = 245;
	private final int verticalPadding = 60, horizontalPadding = 30;
	
	String guiText = "Geometric Acoustics Analytics:";
	String xAxisLabel = "Blocks";
	String yAxisLabel = "Energy";
	
	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution scaled = new ScaledResolution(mc);
			int width = scaled.getScaledWidth();
			int height = scaled.getScaledHeight();
			
			// Draw debug title
			drawCenteredString(mc.fontRendererObj, guiText, width/2, height/15, Integer.parseInt("FFFFFF", 16));
            
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
 			drawCenteredString(mc.fontRendererObj, xAxisLabel, horizontalPadding + 30, height - verticalPadding + 10, Integer.parseInt("FFFFFF", 16));
 			drawCenteredString(mc.fontRendererObj, yAxisLabel, horizontalPadding, height - verticalPadding - 30, Integer.parseInt("FFFFFF", 16));
        }
    }
	
}
