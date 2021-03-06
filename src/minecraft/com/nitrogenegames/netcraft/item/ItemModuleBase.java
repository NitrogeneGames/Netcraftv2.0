package com.nitrogenegames.netcraft.item;

import com.nitrogenegames.netcraft.Netcraft;

import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.*;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;


public class ItemModuleBase extends Item {

	public ItemModuleBase(int par1) {
		super(par1); //Returns super constructor: par1 is ID
			setCreativeTab(Netcraft.netcrafttab); //Tells the game what creative mode tab it goes in
		}
		
	public void registerIcons(IconRegister par1IconRegister)
		{
		    this.itemIcon = par1IconRegister.registerIcon(Netcraft.modid + ":" + this.getUnlocalizedName());
		}
}