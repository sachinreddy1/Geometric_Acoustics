package com.sachinreddy.GeometricAcoustics;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;


public class GAConfigGUI extends GuiConfig
{
	public GAConfigGUI(GuiScreen parent)
	{
		super(parent, getConfigElements(), GeometricAcousticsCore.modid, false, false, "Geometric Acoustics Configuration");
	}
	
	/** Compiles a list of config elements */
    private static List<IConfigElement> getConfigElements() 
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
      
        //Add categories to config GUI
        list.add(categoryElement(GeometricAcousticsCore.configFile.CATEGORY_GENERAL, "General", "ga.configgui.ctgy.general"));
        list.add(categoryElement(GeometricAcousticsCore.Config.categoryPerformance, "Performance", "ga.configgui.ctgy.performance"));
        list.add(categoryElement(GeometricAcousticsCore.Config.categoryMaterialProperties, "Material Properties", "ga.configgui.ctgy.materialProperties"));
      
        return list;
    }
  
    /** Creates a button linking to another screen where all options of the category are available */
    private static IConfigElement categoryElement(String category, String name, String tooltip_key) 
    {
        return new DummyConfigElement.DummyCategoryElement(name, tooltip_key,
                new ConfigElement(GeometricAcousticsCore.configFile.getCategory(category)).getChildElements());
    }
}
