package com.nitrogenegames.netcraft.misc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketNet extends Packet250CustomPayload
{
	public ArrayList objects;
	public ArrayList connectors;
	public ArrayList nodes;
    public PacketNet(String par1Str, byte[] par2ArrayOfByte)
    {
    	super(par1Str, par2ArrayOfByte);
    }
    public PacketNet() {}
}
