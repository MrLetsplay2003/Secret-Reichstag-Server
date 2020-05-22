package me.mrletsplay.srweb.packet.handler;

import java.util.Arrays;
import java.util.List;

import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.PacketData;

public abstract class PacketHandler implements IPacketHandler {
	
	private List<Class<? extends PacketData>> handlingTypes;
	private boolean requirePlayer;
	
	@SafeVarargs
	public PacketHandler(boolean requirePlayer, Class<? extends PacketData>... handlingTypes) {
		this.requirePlayer = requirePlayer;
		this.handlingTypes = Arrays.asList(handlingTypes);
	}
	
	@SafeVarargs
	public PacketHandler(Class<? extends PacketData>... handlingTypes) {
		this(true, handlingTypes);
	}
	
	@Override
	public boolean shouldHandle(Packet packet) {
		return handlingTypes.stream().anyMatch(c -> c.isInstance(packet.getData()));
	}
	
	@Override
	public boolean requirePlayer() {
		return requirePlayer;
	}

}
