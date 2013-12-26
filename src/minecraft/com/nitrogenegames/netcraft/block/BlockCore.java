package com.nitrogenegames.netcraft.block;

import ic2.api.energy.tile.IEnergyTile;

import java.util.List;
import java.util.Random;

import com.nitrogenegames.netcraft.Netcraft;
import com.nitrogenegames.netcraft.machine.TileEntityCore;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraftforge.common.ForgeDirection;

public class BlockCore extends BlockContainer {
	 public BlockCore (int id, String texture) {
        super(id, Material.rock);
        setCreativeTab(Netcraft.netcrafttab);
        setHardness(4f);
 		setResistance(10f);
 		setLightValue(0f);
 		setStepSound(Block.soundMetalFootstep);
 }
	 
	 public void activatemodules(World world, int x, int y, int z) {
         TileEntityCore tileEntity = (TileEntityCore) world.getBlockTileEntity(x, y, z);
         for (int i = 0; i < 1; i++) {
            ItemStack stack = tileEntity.getStackInSlot(i);
            if(!(stack == null)) {
        	if (!(stack.getItem() == null)) {
        	 if(Netcraft.isModule(stack)) {
        		 ItemModules module = (ItemModules) stack.getItem();
        		 module.activate(world, stack, x, y, z);
        	 }
         	}
            }
         }
	 }
	 /*public ItemStack[] pickupstack(World world, int x, int y, int z) {
		 ItemStack[] stacks = new ItemStack[1];
         TileEntityCore tileEntity = (TileEntityCore) world.getBlockTileEntity(x, y, z);
         for (int i = 0; i < 1; i++) {
        	 ItemStack stack = tileEntity.getStackInSlot(i);
        	 stacks[i] = stack;
         }
         return stacks;
	 } */
	 @Override
	 public void onBlockAdded(World par1World, int par2, int par3, int par4)
	 {
	          if (!par1World.isRemote)
	          {
	        	  TileEntityCore tileEntity = (TileEntityCore) par1World.getBlockTileEntity(par2, par3, par4);
	        	  	
	                  
	                  boolean powered = tileEntity.powered;
	                  if (powered && !par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
	                  {
	                          par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, 4);
	                  }
	                  else if (!powered && par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
	                  {
	                          tileEntity.powered = true;
	                          activatemodules(par1World, par2, par3, par4);
	                  }
	          }
	 }
	 @Override
	 public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
	 {
	          if (!par1World.isRemote)
	          {
	        	  TileEntityCore tileEntity = (TileEntityCore) par1World.getBlockTileEntity(par2, par3, par4);
	        	  	
                  
                  boolean powered = tileEntity.powered;
	                  if (powered && !par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
	                  {
	                          par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, 4);
	                  }
	                  else if (!powered && par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
	                  {
	                          tileEntity.powered = true;
	                          activatemodules(par1World, par2, par3, par4);
	                  }
	          }
	 }

	 @Override
	 public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
	 {
	          if (!par1World.isRemote)
	          {
	        	  TileEntityCore tileEntity = (TileEntityCore) par1World.getBlockTileEntity(par2, par3, par4);
	        	  	
                  
                  boolean powered = tileEntity.powered;
	                  if (powered && !par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
	                          tileEntity.powered = false;
	          }
	 }
	 @SideOnly(Side.CLIENT)
     private Icon[] icons;

     @SideOnly(Side.CLIENT)
     @Override
     public void registerIcons(IconRegister par1IconRegister)
     {
           icons = new Icon[3];
          
           for(int i = 0; i < icons.length; i++)
           {
                  icons[i] = par1IconRegister.registerIcon(Netcraft.modid + ":" + (this.getUnlocalizedName().substring(5)) + i);
           }
     }
	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}
}