package com.nitrogenegames.netcraft.machine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.nitrogenegames.netcraft.Netcraft;
import com.nitrogenegames.netcraft.gui.GuiCore;
import com.nitrogenegames.netcraft.misc.PacketNet;
import com.nitrogenegames.netcraft.net.INet;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import ic2.api.energy.prefab.BasicSink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.energy.*;
import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.*;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityCore extends TileEntity implements IEnergySink, ISidedInventory, INet {

	public int energygainpertick = 0;
	public int energy = 0;
	public int tabPage = 0;
	public boolean isUsingPower = false;
	//ISidedInventory
	private int id = 0;
	public int maxenergy = 1000000;
	private boolean init;
	//private BasicSink electricSlicer = new BasicSink(this, 32, 3);
	private ItemStack[] inv = new ItemStack[11];
	public boolean powered = false;
	public int coreEnergyNeeded = 0;
	
	public int moduleCurrentPage = 1;
	public int moduleMaxPages = 3;
	public String moduleSelected = "None";
	/*public TileEntityCore() {
		this.net = new NetEntity(worldObj, new int[]{this.xCoord, this.yCoord, this.zCoord});
		net.update();
	}*/
	@Override
    public int getSizeInventory() {
            return inv.length;
    }
	
	@Override
	public Packet getDescriptionPacket() {
	    NBTTagCompound tagCompound = new NBTTagCompound();
	    writeToNBT(tagCompound);
        if(!worldObj.isRemote) {
        sendTabPacket(Side.CLIENT);
        }
	    return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tagCompound);
	}
	@Override
	public void onDataPacket(INetworkManager networkManager, Packet132TileEntityData packet) {
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
    public ItemStack getStackInSlot(int slot) {
       //System.out.println(slot + " debug message");
            return inv[slot];

    }

	@Override
    public ItemStack decrStackSize(int slot, int amt) {
            ItemStack stack = getStackInSlot(slot);
           if (stack != null) {
                   if (stack.stackSize <= amt) {
                          setInventorySlotContents(slot, null);
                    } else {
                       stack = stack.splitStack(amt);
                         if (stack.stackSize == 0) {
                                   setInventorySlotContents(slot, null);
                           }
                   }
           }
           return stack;
    }

	@Override
    public ItemStack getStackInSlotOnClosing(int slot) {
            ItemStack stack = getStackInSlot(slot);
            if (stack != null) {
                    setInventorySlotContents(slot, null);
            }
            return stack;
    }

	@Override
    public void setInventorySlotContents(int slot, ItemStack stack) {

            inv[slot] = stack;

            if (stack != null && stack.stackSize > getInventoryStackLimit()) {
                    stack.stackSize = getInventoryStackLimit();
            }               
    }

	@Override
    public String getInvName() {
            return "ironarcher.tileentitynetcraft";
    }



	@Override
	public boolean isInvNameLocalized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
    public int getInventoryStackLimit() {
            return 64;
    }

	@Override
    public boolean isUseableByPlayer(EntityPlayer player) {
            return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
            player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

	@Override
    public void openChest() {}

    @Override
    public void closeChest() {}
    
    public void setPressed(int p) {
    	this.tabPage = p;
    	sendTabPacket(Side.SERVER);
    	//SEND PACKET TO SERVER AND BACK TO CLIENT :O
    }
    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            
            if(tagCompound.hasKey("energy")){
            	this.energy = tagCompound.getInteger("energy");
            }
            if(tagCompound.hasKey("pressed")){
            	this.tabPage = tagCompound.getInteger("pressed");

           }
            NBTTagList tagList = tagCompound.getTagList("Inventory");
            for (int i = 0; i < tagList.tagCount(); i++) {
                    NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
                    byte slot = tag.getByte("Slot");
                    if (slot >= 0 && slot < inv.length) {
                            inv[slot] = ItemStack.loadItemStackFromNBT(tag);
                    }
}
				powered = tagCompound.getBoolean("POWER");
    }
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
    }
    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
            

            
            NBTTagList itemList = new NBTTagList();
            for (int i = 0; i < inv.length; i++) {
                    ItemStack stack = inv[i];
                    if (stack != null) {
                            NBTTagCompound tag = new NBTTagCompound();
                            tag.setByte("Slot", (byte) i);
                            stack.writeToNBT(tag);
                            itemList.appendTag(tag);
                    }
            }
            tagCompound.setTag("Inventory", itemList);
            tagCompound.setBoolean("POWER", powered);
            tagCompound.setInteger("energy", this.energy);
            tagCompound.setInteger("pressed", this.tabPage);

    }
    
    
    @Override
    public void updateEntity(){
    	
    	if(!init && worldObj != null){
    		if(!worldObj.isRemote){
    			EnergyTileLoadEvent loadEvent = new EnergyTileLoadEvent(this);
    			MinecraftForge.EVENT_BUS.post(loadEvent);
    		}
    		init = true;
    	}
    	int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		if(!worldObj.isRemote) { //MAYBE A BIT LAGGY?
    	if(meta <= 3 && powered == true) {
    		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta + 4, 2);
    	} else if(meta >= 4 && powered == false) {
    		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta - 4, 2);
    	}
		}
		if(!(this.getStackInSlot(1) == null)) {
			if(this.getStackInSlot(1).getItem() instanceof IElectricItem) {
				IElectricItem i = (IElectricItem) this.getStackInSlot(1).getItem();
				if(i.canProvideEnergy(this.getStackInSlot(1))) {
					IElectricItemManager i2;
					try {
						i2 = (IElectricItemManager) ElectricItem.class.getField("manager").get(null);
						int c = i2.discharge(this.getStackInSlot(1), 32, 4, true, false);
						this.energy += c;
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					}

				}
			}
		}
		sendEnergyPacket(Side.CLIENT);
		
    	worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    @Override
    public void invalidate(){
    	
    	EnergyTileUnloadEvent unloadEvent = new EnergyTileUnloadEvent(this);
		MinecraftForge.EVENT_BUS.post(unloadEvent);
    }
    
	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return null;
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}

	@Override
	public int getMaxSafeInput() {
		return 32;
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter,
			ForgeDirection direction) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public double demandedEnergyUnits() {
		return (int) (this.maxenergy - this.energy);
	}

	@Override
	public double injectEnergyUnits(ForgeDirection directionFrom, double amount) {

		if(this.maxenergy == this.energy){
			this.energygainpertick = 0;
		} else{
		this.energygainpertick = (int) amount;
		}
		
		this.isUsingPower = true;
		if(this.energy >= this.maxenergy){
			sendEnergyPacket(Side.CLIENT);
			return amount;
		}
		
		double openEnergy = this.maxenergy - this.energy;
		
		if(openEnergy >= amount){
			this.energy += amount;
			sendEnergyPacket(Side.CLIENT);
			return 0;
		} else if (amount > openEnergy){
			this.energy = this.maxenergy;
			sendEnergyPacket(Side.CLIENT);
			return amount - (int) openEnergy;
		}
		sendEnergyPacket(Side.CLIENT);
		return 0;
	}
	public int getEnergy() {
		return this.energy;
	}
	public void sendEnergyPacket(Side s) {
		if(s == Side.CLIENT && !worldObj.isRemote) {
    	ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);
        try {
        	//System.out.println(this.energy + " ENERGYS SENDING");
                outputStream.writeInt(this.energy);
                outputStream.writeInt(this.xCoord);
                outputStream.writeInt(this.yCoord);
                outputStream.writeInt(this.zCoord);
                outputStream.writeInt(this.energygainpertick);
        } catch (Exception ex) {
                ex.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "corepack";
        packet.data = bos.toByteArray();
        packet.length = bos.size();
    	PacketDispatcher.sendPacketToAllPlayers(packet); //Maybe change to players in radius?
		} else if(s == Side.SERVER && worldObj.isRemote) {
		    	ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
		        DataOutputStream outputStream = new DataOutputStream(bos);
		        try {
		        	//System.out.println(this.energy + " ENERGYS SENDING");
		                outputStream.writeInt(this.energy);
		                outputStream.writeInt(this.xCoord);
		                outputStream.writeInt(this.yCoord);
		                outputStream.writeInt(this.zCoord);
		                outputStream.writeInt(this.energygainpertick);
		        } catch (Exception ex) {
		                ex.printStackTrace();
		        }

		        Packet250CustomPayload packet = new Packet250CustomPayload();
		        packet.channel = "corepack";
		        packet.data = bos.toByteArray();
		        packet.length = bos.size();
		    	PacketDispatcher.sendPacketToServer(packet); //Maybe change to players in radius?
				}
	}
	 private byte [] createByteArray( Object obj)
	    {
	        byte [] bArray = null;
	        try
	        {
	                ByteArrayOutputStream baos = new ByteArrayOutputStream();
	                ObjectOutputStream objOstream = new ObjectOutputStream(baos);
	                objOstream.writeObject(obj);
	                bArray = baos.toByteArray();
	        }
	        catch (Exception e)
	        {
	     

	        }

	                return bArray;

	    }
	public void sendNetPacket(Side s) {
		if(s == Side.CLIENT && !worldObj.isRemote) {
	    	ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
	        try {

        DataOutputStream outputStream = new DataOutputStream(bos);

        	//System.out.println(this.energy + " ENERGYS SENDING");
                //outputStream.write(this.objects.t);
                outputStream.writeInt(this.xCoord);
                outputStream.writeInt(this.yCoord);
                outputStream.writeInt(this.zCoord);
        } catch (Exception ex) {
                ex.printStackTrace();
        }

        PacketNet packet = new PacketNet();
        packet.channel = "netupdate";
        packet.data = bos.toByteArray();
        packet.length = bos.size();
        packet.objects = this.objects;
        packet.connectors = this.connectors;
        packet.nodes = this.nodes;
    	PacketDispatcher.sendPacketToAllPlayers(packet); //Maybe change to players in radius?
		} /*else if(s == Side.SERVER && worldObj.isRemote) { 
	    	ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
	        DataOutputStream outputStream = new DataOutputStream(bos);
	        try {
	        	//System.out.println(this.energy + " ENERGYS SENDING");
	                //outputStream.write(this.objects.t);
	        		outputStream.write(createByteArray(objects));
	        		outputStream.write(createByteArray(connectors));
	        		outputStream.write(createByteArray(nodes));
	                outputStream.writeInt(this.xCoord);
	                outputStream.writeInt(this.yCoord);
	                outputStream.writeInt(this.zCoord);
	        } catch (Exception ex) {
	                ex.printStackTrace();
	        }

	        Packet250CustomPayload packet = new Packet250CustomPayload();
	        packet.channel = "netupdate";
	        packet.data = bos.toByteArray();
	        packet.length = bos.size();
		    	PacketDispatcher.sendPacketToServer(packet); //Maybe change to players in radius?
				} */
	}
	public void sendTabPacket(Side s) {
		if(s == Side.CLIENT && !worldObj.isRemote) {
    	ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);
        try {
        	//System.out.println(this.energy + " ENERGYS SENDING");
                outputStream.writeInt(this.tabPage);
                outputStream.writeInt(this.xCoord);
                outputStream.writeInt(this.yCoord);
                outputStream.writeInt(this.zCoord);
        } catch (Exception ex) {
                ex.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "coretab";
        packet.data = bos.toByteArray();
        packet.length = bos.size();
    	PacketDispatcher.sendPacketToAllPlayers(packet); //Maybe change to players in radius?
		} else if(s == Side.SERVER && worldObj.isRemote) { 
		    	ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
		        DataOutputStream outputStream = new DataOutputStream(bos);
		        try {
		        	//System.out.println(this.energy + " ENERGYS SENDING");
		                outputStream.writeInt(this.tabPage);
		                outputStream.writeInt(this.xCoord);
		                outputStream.writeInt(this.yCoord);
		                outputStream.writeInt(this.zCoord);
		        } catch (Exception ex) {
		                ex.printStackTrace();
		        }

		        Packet250CustomPayload packet = new Packet250CustomPayload();
		        packet.channel = "coretab";
		        packet.data = bos.toByteArray();
		        packet.length = bos.size();
		    	PacketDispatcher.sendPacketToServer(packet); //Maybe change to players in radius?
				}
	}
	public int getEnergyScaled(int i) {
		return this.energy * i / maxenergy;
	}
	
	public int getTabPage() {
		
		return tabPage;
	}

	@Override
	public ArrayList getConnected() {
		return Netcraft.getConnectedObjects(worldObj, this.xCoord, this.yCoord, this.zCoord);
	}

	@Override
	public int[] getCore() {
		return new int[] {xCoord, yCoord, zCoord};
	}


	
	public int getEnergyGainPerTick(){
		return energygainpertick;
	}
	/*
	@Override
	public int getStored() {
		// TODO Auto-generated method stub
		return energy;
	}

	@Override
	public void setStored(int energy) {
		this.energy = energy;
		
	}

	@Override
	public int addEnergy(int amount) {
		// TODO Auto-generated method stub
		return (int) this.injectEnergyUnits(null, amount);
	}

	@Override
	public int getCapacity() {
		return maxenergy;
	}

	@Override
	public int getOutput() {
		return 0;
	}

	@Override
	public double getOutputEnergyUnitsPerTick() {
		return 0;
	}

	@Override
	public boolean isTeleporterCompatible(ForgeDirection side) {
		//  Auto-generated method stub
		return false;
	}
	*/
	
	//TODO NET STUFF
	public ArrayList objects = new ArrayList();
	public ArrayList nodes = new ArrayList();
	public ArrayList connectors = new ArrayList();
	public void update() {
		update(false);
	}
	public void update(boolean p) {
    	ArrayList c = Netcraft.getConnectedObjects(worldObj, xCoord, yCoord, zCoord);
    	objects = c;
    	ArrayList d = new ArrayList();
        for(int i = 0; i < c.size(); i++) {
        	if(worldObj.getBlockId(((int[]) c.get(i))[0], ((int[]) c.get(i))[1], ((int[]) c.get(i))[2]) == Netcraft.connectionnode.blockID) {
        		d.add(new int[]{((int[]) c.get(i))[0], ((int[]) c.get(i))[1], ((int[]) c.get(i))[2]});
        	}
        }
        connectors = d;
        if(!p) {
        if(!this.worldObj.isRemote) {
        sendNetPacket(Side.CLIENT);
        } else {
            sendNetPacket(Side.SERVER);
        }
        }
	}

}
