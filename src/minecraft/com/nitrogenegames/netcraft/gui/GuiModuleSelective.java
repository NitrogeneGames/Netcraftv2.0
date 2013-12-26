package com.nitrogenegames.netcraft.gui;

import org.lwjgl.opengl.GL11;

import com.nitrogenegames.netcraft.Netcraft;
import com.nitrogenegames.netcraft.item.ItemModules;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.World;

public class GuiModuleSelective extends GuiScreen
{
/** This GUI's parent GUI. */

private ItemStack stack;
private int xSize = 176;
private int ySize = 166;
//public NBTTagList tagList;
public int pagenum = 1;
public int maxpages = 0;
private int selected;
private boolean lastone = false;
public GuiModuleSelective(ItemStack i)
{
         this.stack = ItemModules.p.getHeldItem();
     	if( stack.stackTagCompound == null )
            stack.setTagCompound( new NBTTagCompound( ) );
         NBTTagCompound tagCompound = stack.getTagCompound();	
         NBTTagList tagList = tagCompound.getTagList("Marked");
         if(!(tagList.tagCount() == 0)) {
         selected = tagCompound.getInteger("Selected");
         } else {
        	 selected = 0;
         }
         
         
}

public void setmaxpages()
{
    NBTTagCompound tagCompound = stack.getTagCompound();	
    NBTTagList tagList = tagCompound.getTagList("Marked");
    if (tagList.tagCount() % 2 == 1) {
    	//ODD
    	maxpages = ((tagList.tagCount()-1)/2) + 1;
    	lastone = true;
    } else {
    	//EVEN
    	maxpages = (tagList.tagCount())/2;
    	lastone = false;
    }
    	
	
}
public int getSelected(int s)
{
	if(s == selected) {
		return 0x006400;
	} else {
	return 4210752;
	}
}
@Override
public void drawScreen(int param1, int param2, float par3) {
	if( stack.stackTagCompound == null )
        stack.setTagCompound( new NBTTagCompound( ) );
    setmaxpages();
	buttons();
    NBTTagCompound tagCompound = stack.getTagCompound();	
    NBTTagList tagList = tagCompound.getTagList("Marked");


	drawDefaultBackground();
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    //this.mc.renderEngine.bindTexture("/irongui/guiModule.png");
    int x = (this.width - this.xSize) / 2;
    int y = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    //draw text and stuff here
    //the parameters for drawString are: string, x, y, color


    if (tagList.tagCount() == 0) {

        fontRenderer.drawString("No Blocks Marked", x + 43, y + 46, 4210752);
        fontRenderer.drawString("To mark a block, sneak", x+ 31, y + 70, 4210752);
        fontRenderer.drawString("and right click on it.", x+ 37, y + 80, 4210752);
    } else {
        if ((lastone == true) && pagenum == maxpages) {
            fontRenderer.drawString("Page " + pagenum + "/" + maxpages, x + 69, y + 154, 4210752);
            NBTTagCompound tag1 = (NBTTagCompound) tagList.tagAt((pagenum - 1) * 2);
            int[] int1 = Netcraft.decompileNBT(tag1.getString("MarkedThing"));
        fontRenderer.drawString(int1[0] + ", " + int1[1] + ", " + int1[2], x + 30, y + 40, getSelected((pagenum -1) * 2));
        
        } else {
            fontRenderer.drawString("Page " + pagenum + "/" + maxpages, x + 69, y + 154, 4210752);
            NBTTagCompound tag1 = (NBTTagCompound) tagList.tagAt((pagenum - 1) * 2);
            NBTTagCompound tag2 = (NBTTagCompound) tagList.tagAt(((pagenum - 1) * 2) + 1);
            int[] int1 = Netcraft.decompileNBT(tag1.getString("MarkedThing"));
            int[] int2 = Netcraft.decompileNBT(tag2.getString("MarkedThing"));
        fontRenderer.drawString(int1[0] + ", " + int1[1] + ", " + int1[2],  x + 30, y + 40, getSelected((pagenum -1) * 2));
        fontRenderer.drawString(int2[0] + ", " + int2[1] + ", " + int2[2], x + 30, y + 80, getSelected(((pagenum -1) * 2) + 1));
        }
        

    //for (int i = 0; i < tagList.tagCount(); i++) {
    //    NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
        //marked.add(tag.getString("MarkedThing"));
    
    //}
    }


    
    //draws "Inventory" or your regional equivalent
    //drawDefaultBackground();


    super.drawScreen(param1, param2, par3);
}
@SideOnly(Side.CLIENT)
public void buttons()
{
	this.buttonList.clear();
	
	int posX = (this.width - xSize) / 2;
	int posY = (this.height - ySize) / 2;
	if( stack.stackTagCompound == null )
        stack.setTagCompound( new NBTTagCompound( ) );
    NBTTagCompound tagCompound = stack.getTagCompound();	
    NBTTagList tagList = tagCompound.getTagList("Marked");
    if (tagList.tagCount() == 0) {
    	
    } else if ((tagList.tagCount() == 1)){
	this.buttonList.add(new GuiButton(0, posX+ 150, posY + 40, 20, 20, "X"));
    this.buttonList.add(new GuiButton(4, posX+ 120, posY + 40, 20, 20, "@")); 
	
    } else if ((pagenum == maxpages) && (lastone == true)) {
    this.buttonList.add(new GuiButton(0, posX+ 150, posY + 40, 20, 20, "X"));
    this.buttonList.add(new GuiButton(4, posX+ 120, posY + 40, 20, 20, "@")); 
    this.buttonList.add(new GuiButton(2, posX+ 70, posY + 130, 20, 20, "<")); 
    this.buttonList.add(new GuiButton(3, posX+ 92, posY + 130, 20, 20, ">")); 
    } else {
    this.buttonList.add(new GuiButton(0, posX+ 150, posY + 40, 20, 20, "X"));
    this.buttonList.add(new GuiButton(4, posX+ 120, posY + 40, 20, 20, "@")); 
    this.buttonList.add(new GuiButton(1, posX+ 150, posY + 80, 20, 20, "X")); 
    this.buttonList.add(new GuiButton(5, posX+ 120, posY + 80, 20, 20, "@")); 
    if (maxpages > 1) {
    this.buttonList.add(new GuiButton(2, posX+ 70, posY + 130, 20, 20, "<")); 
    this.buttonList.add(new GuiButton(3, posX+ 92, posY + 130, 20, 20, ">")); 
    }
    }
    

}
@Override
public void actionPerformed(GuiButton button)
{
    NBTTagCompound tagCompound = stack.getTagCompound();	
    NBTTagList tagList = tagCompound.getTagList("Marked");
	if(button.id == 0) {
		if(selected == ((pagenum -1) * 2)) {
		selected = 0;
		tagCompound.setInteger("Selected", 0);
		}
		if(lastone == true && !(pagenum == 1)) {
			pagenum -=1;
			tagList.removeTag(pagenum * 2);
			tagCompound.setTag("Marked", tagList);
		} else {
			tagList.removeTag((pagenum -1) * 2);
			tagCompound.setTag("Marked", tagList);
			
		}
	} else if (button.id == 1) {
		if(selected == (((pagenum -1) * 2)+1)) {
		selected = 0;
		tagCompound.setInteger("Selected", 0);
		}
		if(lastone == true && !(pagenum == 1)) {
			pagenum -=1;
			tagList.removeTag((pagenum * 2)+1);
			tagCompound.setTag("Marked", tagList);
		} else {
			tagList.removeTag(((pagenum -1) * 2)+1);
			tagCompound.setTag("Marked", tagList);
		}

	} else if (button.id == 2) {
		if(pagenum > 1) {
			pagenum -= 1;
		}
	} else if (button.id == 3) {
		if(pagenum < maxpages) {
			pagenum += 1;
		}
	} else if (button.id == 4) {
		selected = ((pagenum -1) * 2);
		tagCompound.setInteger("Selected", ((pagenum -1) * 2));
		
	} else if (button.id == 5) {
		selected = (((pagenum -1) * 2) + 1);
		tagCompound.setInteger("Selected", ((pagenum -1) * 2)+ 1);
	}

}
}