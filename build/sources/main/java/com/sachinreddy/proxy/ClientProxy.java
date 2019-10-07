package com.sachinreddy.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.item.Item;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
//		super.preInit(event);
	}
	
	public void registerModel(Item item) {
    }
	
	@Override
	public void init(FMLInitializationEvent event)
	{
//		super.init(event);
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
//		super.postInit(event);
	}
}
