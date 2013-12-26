package com.nitrogenegames.netcraft.gui;

import org.lwjgl.opengl.GL11;

import com.nitrogenegames.netcraft.item.ItemModules;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class GuiModuleTesla extends GuiScreen {
	private ItemStack stack;
	private int xSize = 176;
	private int ySize = 166;
	private int range;
	private int mode;
	public int maxrange = 50;
	public GuiModuleTesla(ItemStack i) {
		this.stack = ItemModules.p.getHeldItem();
		if( stack.stackTagCompound == null )
	        stack.setTagCompound( new NBTTagCompound( ) );
	    NBTTagCompound tagCompound = stack.getTagCompound();	
	    range = tagCompound.getInteger("Range");
	    if(range == 0) {
	    	range = 20; //PUT DEFAULT RANGE HERE
	    }
	    mode = tagCompound.getInteger("Mode");
	    if(mode == 0) {
	    	mode = 3; //PUT DEFAULT MODE HERE
	    }
	    
	}
	public String decompileMode(int i) {
		if(i == 1) {
			return "Kill all hostile mobs";
		} else if(i == 2) {
			return "Kill all mobs";
		} else if (i == 3) {
			return "Kill all players and mobs";
		} else {
			return "Invalid Mode";
		}
	}
	@Override
	public void drawScreen(int param1, int param2, float par3) {
		
		if( stack.stackTagCompound == null )
	        stack.setTagCompound( new NBTTagCompound( ) );
		buttons();
	    NBTTagCompound tagCompound = stack.getTagCompound();	
	    NBTTagList tagList = tagCompound.getTagList("Marked");
		drawDefaultBackground();
	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    
	    //this.mc.renderEngine.bindTexture("/irongui/guiModule.png");
	    int x = (this.width - this.xSize) / 2;
	    int y = (this.height - this.ySize) / 2;
	    this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        fontRenderer.drawString("Range: " + range, x + 60, y + 20, 4210752);
        fontRenderer.drawString("Mode: " + decompileMode(mode), x + 10, y + 70, 4210752);

        super.drawScreen(param1, param2, par3);
	}
	@SideOnly(Side.CLIENT)
	public void buttons()
	{
		if( stack.stackTagCompound == null )
	        stack.setTagCompound( new NBTTagCompound( ) );
		this.buttonList.clear();
		
		int posX = (this.width - xSize) / 2;
		int posY = (this.height - ySize) / 2;
	    this.buttonList.add(new GuiButton(0, posX+ 60, posY + 40, 20, 20, "<"));
	    this.buttonList.add(new GuiButton(1, posX+ 82, posY + 40, 20, 20, ">"));
	    this.buttonList.add(new GuiButton(4, posX+ 38, posY + 40, 20, 20, "<<"));
	    this.buttonList.add(new GuiButton(3, posX+ 104, posY + 40, 20, 20, ">>"));
	    this.buttonList.add(new GuiButton(2, posX+ 40, posY + 92, 70, 20, "Toggle Mode"));


	}
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0) {
			if(range > 1) {
			    NBTTagCompound tagCompound = stack.getTagCompound();	
				range = range - 1;
			    tagCompound.setInteger("Range", range);
			}
		} else if(button.id == 1) {
			if(range < maxrange) {
			    NBTTagCompound tagCompound = stack.getTagCompound();	
				range = range + 1;
			    tagCompound.setInteger("Range", range);
			}
		} else if(button.id == 2) {
		    NBTTagCompound tagCompound = stack.getTagCompound();	
			if(mode == 3) {
			    mode = 1;
			    tagCompound.setInteger("Mode", mode);
			} else if (mode == 1 || mode == 2) {			
				mode +=1;
			    tagCompound.setInteger("Mode", mode);
			} 
		} else if(button.id == 3) {
			if(range + 10 < maxrange) {
			    NBTTagCompound tagCompound = stack.getTagCompound();	
				range = range + 10;
			    tagCompound.setInteger("Range", range);
			} else {
			    NBTTagCompound tagCompound = stack.getTagCompound();	
				range = maxrange;
			    tagCompound.setInteger("Range", range);
			}
		} else if(button.id == 4) {
			if(range - 10 > 1) {
			    NBTTagCompound tagCompound = stack.getTagCompound();	
				range = range - 10;
			    tagCompound.setInteger("Range", range);
			} else {
			    NBTTagCompound tagCompound = stack.getTagCompound();	
				range = 1;
			    tagCompound.setInteger("Range", range);
			}
		}
	} 
	

}
