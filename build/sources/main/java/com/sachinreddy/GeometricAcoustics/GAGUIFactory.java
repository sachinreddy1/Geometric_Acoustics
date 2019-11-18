package com.sachinreddy.GeometricAcoustics;

import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.IModGuiFactory.RuntimeOptionCategoryElement;
import net.minecraftforge.fml.client.IModGuiFactory.RuntimeOptionGuiHandler;

public class GAGUIFactory implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance) {
		// TODO Auto-generated method stub
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return GAConfigGUI.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(
			RuntimeOptionCategoryElement element) {
		// TODO Auto-generated method stub
		return null;
	}
}
