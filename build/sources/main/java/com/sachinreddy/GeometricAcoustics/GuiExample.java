package com.sachinreddy.GeometricAcoustics;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import java.lang.Integer;

public class GuiExample extends Gui
{
	String guiText = "Hello world!";
	
	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			
			Minecraft mc = Minecraft.getMinecraft();
			
			ScaledResolution scaled = new ScaledResolution(mc);
			int width = scaled.getScaledWidth();
			int height = scaled.getScaledHeight();
			
			drawCenteredString(mc.fontRendererObj, guiText, width/2, height/2, Integer.parseInt("FFAA00", 16));
			
		}
	}
	
}
