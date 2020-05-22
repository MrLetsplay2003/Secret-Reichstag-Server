package me.mrletsplay.srweb.packet.handler;

import org.java_websocket.WebSocket;

import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.PacketData;

public abstract class SingleTypePacketHandler<T extends PacketData> extends PacketHandler {

	private Class<T> handlingType;
	
	public SingleTypePacketHandler(Class<T> handlingType) {
		super(handlingType);
		this.handlingType = handlingType;
	}
	
	public SingleTypePacketHandler(boolean requirePlayer, Class<T> handlingType) {
		super(requirePlayer, handlingType);
		this.handlingType = handlingType;
	}
	
	@Override
	public PacketData handle(WebSocket webSocket, Player player, Packet packet, PacketData data) {
		return handleSingle(webSocket, player, packet, handlingType.cast(packet.getData()));
	}
	
	public abstract PacketData handleSingle(WebSocket webSocket, Player player, Packet packet, T data);

}
