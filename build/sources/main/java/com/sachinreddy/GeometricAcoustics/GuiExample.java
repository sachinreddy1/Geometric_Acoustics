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
	private final ResourceLocation bar = new ResourceLocation(GeometricAcousticsCore.modid, "textures/gui/horizontalaxis.png");
	private final int tex_width = 245, tex_height = 6;
	
	String guiText = "Geometric Acoustics Analytics:";
	
	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.renderEngine.bindTexture(bar);
            
//            float oneUnit = (float)bar_width / mc.thePlayer.getMaxHealth();
//            int currentWidth = (int)(oneUnit * mc.thePlayer.getHealth());
//            drawTexturedModalRect(1, 0, 1, tex_height, currentWidth, tex_height);
            
            ScaledResolution scaled = new ScaledResolution(mc);
			int width = scaled.getScaledWidth();
			int height = scaled.getScaledHeight();
            
            drawTexturedModalRect((width - tex_width)/2, height * 0.8f, 0, 0, tex_width, tex_height);
            //
			drawCenteredString(mc.fontRendererObj, guiText, width/2, height/15, Integer.parseInt("FFFFFF", 16));
        }
    }
	
}
