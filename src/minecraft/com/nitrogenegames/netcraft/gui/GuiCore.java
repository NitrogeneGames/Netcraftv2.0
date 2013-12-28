package com.nitrogenegames.netcraft.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.nitrogenegames.netcraft.Netcraft;
import com.nitrogenegames.netcraft.machine.ContainerCore;
import com.nitrogenegames.netcraft.machine.TileEntityCore;

public class GuiCore extends GuiContainer {
	
		TileEntityCore tel;
		ArrayList<TabButton> tabs;
		int x,y;
		
        public GuiCore (InventoryPlayer inventoryPlayer, TileEntityCore tileEntity) {
                //the container is instanciated and passed to the superclass for handling
                super(new ContainerCore(inventoryPlayer, tileEntity));
                tel = tileEntity;
                
                this.xSize = 252;
                this.ySize = 166;
        }

        @Override
        protected void drawGuiContainerForegroundLayer(int param1, int param2) {
                //draw text and stuff here
                //the parameters for drawString are: string, x, y, color
        		//tel = (TileEntityCore) tel.worldObj.getBlockTileEntity(tel.xCoord, tel.yCoord, tel.zCoord);
        	ContainerCore parentContainer = (ContainerCore) super.inventorySlots;
        	//System.out.println(tel.energy);
        	//System.out.println(parentContainer.tileEntity.energy);

                fontRenderer.drawString("Net Core", x+66, y+6, 4210752);
                //202, 252, middle is 227
                int ewidth = fontRenderer.getStringWidth("EU:");
                fontRenderer.drawString("EU:", x+227-(ewidth/2), y+120, 4210752);
                int nwidth = fontRenderer.getStringWidth(tel.energy + "");
                fontRenderer.drawString(tel.energy + "", x+227-(nwidth/2), y+135, 4210752);
                ItemStack par1ItemStack = tel.getStackInSlot(0);
                if(par1ItemStack != null) {
        		if( par1ItemStack.stackTagCompound == null )
                    par1ItemStack.setTagCompound( new NBTTagCompound( ) );

                NBTTagCompound tagCompound = par1ItemStack.getTagCompound();	
                NBTTagList tagList = tagCompound.getTagList("Marked");
                for (int i = 0; i < tagList.tagCount(); i++) {
                    NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
                    fontRenderer.drawString(tag.getString("MarkedThing"), x+90, (i * 10) + 30 + y, 4210752);
                }
                }
                //draws "Inventory" or your regional equivalent
                fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), x+ 8 + (12), y + ySize - 96 + 2, 4210752);

        }

        @Override
        protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
        {
        	final ResourceLocation texture = new ResourceLocation(Netcraft.modid.toLowerCase(), "/textures/gui/coregui.png");
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.renderEngine.bindTexture(texture);
            x = (this.width - this.xSize + 50) / 2;
            y = (this.height - this.ySize + 15) / 2;
            //this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
            //par1 = actual x, par2 = actual y, par3 = (u) x on texture file, par4 = (v) y on texture file, par5 = width, par6 = height
            drawTexturedModalRect(this.x, this.y, 0, 0, this.xSize, this.ySize);
            
            int k = this.tel.getEnergyScaled(78);
            /*
            218 is width from gui border left
            20 is length from gui border top, with 78 being the height of the energy bar (subtracting k to make it grow from the bottom)
            (0,166) is the location of the texturefile energy bar, top left
            16 is width of texture, k is height (displays from bottom - up)
            */
            drawTexturedModalRect(x + 218, y + 20 + 78 - k, 0, 166 + 78 - k, 16, k);
        }
        
        public void actionPerformed(GuiButton button)
        {
        	for(int i = 0; i < tabs.size(); i++){
        		tabs.get(i).setPressed(false);
        	}
        	tabs.get(button.id - 20).togglePressed();
        }
        
        public static void drawTexturedQuadFit(double x, double y, double width, double height, double zLevel){
    		Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(x + 0, y + height, zLevel, 0,1);
            tessellator.addVertexWithUV(x + width, y + height, zLevel, 1, 1);
            tessellator.addVertexWithUV(x + width, y + 0, zLevel, 1,0);
            tessellator.addVertexWithUV(x + 0, y + 0, zLevel, 0, 0);
            tessellator.draw();
    	}
        
        public void initGui(){
        	x = (this.width - this.xSize + 50) / 2;
            y = (this.height - this.ySize + 15) / 2;
        	initTabs();
        }
        
        private void initTabs(){
        	tabs = new ArrayList<TabButton>();
        	createTab(tabs.size(), "Modules");
        	createTab(tabs.size(), "Nodes");
        	createTab(tabs.size(), "Power");
        }
        
        public void createTab(int placement, String text){
        	TabButton temp = new TabButton(placement + 20, x - 52, y + placement*16, 50, 15, text, "/textures/gui/tabButtonBlueBig.png");
        	this.buttonList.add(temp);
        	tabs.add(temp);
        }

}