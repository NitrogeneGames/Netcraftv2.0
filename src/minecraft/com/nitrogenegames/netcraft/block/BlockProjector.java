package com.nitrogenegames.netcraft.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.nitrogenegames.netcraft.Netcraft;
import com.nitrogenegames.netcraft.machine.TileEntityProjector;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockProjector extends BlockContainer {
	Netcraft.EnumProjector type;
	public int range = 20;
	public boolean upgraded = false;
	public int rangeupgraded = 0;
	public int itemID;
    private final Random furnaceRand = new Random();
	public BlockProjector(int par1, Material par2Material, Netcraft.EnumProjector par3) {
		super(par1, par2Material);
		type = par3;

	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityProjector();
	}
    /*public int idDropped(int par1, Random par2Random, int par3)
    {

		if(type == Netcraft.EnumProjector.BEAM) {
			return Netcraft.itemProjectorBeam.itemID;
		} else if(type == Netcraft.EnumProjector.CIRCULAR) {
			return Netcraft.itemProjectorRadial.itemID;
		} else {
			return Netcraft.itemProjectorSatelite.itemID;
		}
    }*/
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {


        if (hasTileEntity(par6) && !(this instanceof BlockContainer))
        {
            par1World.removeBlockTileEntity(par2, par3, par4);
        }
    	Netcraft.cacheTileEntity(par1World.getBlockTileEntity(par2, par3, par4), par2, par3, par4);
        
    }
    public ArrayList<ItemStack> getBlockDropped(World w, int x, int y, int z, int meta, int fortune){
    	ArrayList<ItemStack> list = new ArrayList<ItemStack>();
    	ItemStack s;
		if(type == Netcraft.EnumProjector.BEAM) {
	    	s = new ItemStack(Netcraft.itemProjectorBeam, 1);
		} else if(type == Netcraft.EnumProjector.CIRCULAR) {
	    	s = new ItemStack(Netcraft.itemProjectorRadial, 1);
		} else {
	    	s = new ItemStack(Netcraft.itemProjectorSatelite, 1);
		}
    	TileEntityProjector t = (TileEntityProjector) Netcraft.getCached(x, y, z);
    	Netcraft.removeCached(x, y, z);
    	Netcraft.InstantiateStackNBT(s);
    	s.stackTagCompound.setInteger("range", t.range);
       list.add(s);

		return list;
    }
	
	

}
