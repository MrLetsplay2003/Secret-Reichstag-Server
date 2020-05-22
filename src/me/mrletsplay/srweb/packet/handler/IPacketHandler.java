package me.mrletsplay.srweb.packet.handler;

import org.java_websocket.WebSocket;

import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.PacketData;

public interface IPacketHandler {
	
	public boolean shouldHandle(Packet packet);
	
	public boolean requirePlayer();
	
	public PacketData handle(WebSocket webSocket, Player player, Packet packet, PacketData data);

}
