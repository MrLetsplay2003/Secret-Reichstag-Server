package me.mrletsplay.srweb.packet.handler.impl;

import org.java_websocket.WebSocket;

import me.mrletsplay.srweb.SRWeb;
import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.PacketData;
import me.mrletsplay.srweb.packet.handler.SingleTypePacketHandler;
import me.mrletsplay.srweb.packet.impl.PacketDisconnect;

public class DisconnectHandler extends SingleTypePacketHandler<PacketDisconnect> {

	public DisconnectHandler() {
		super(PacketDisconnect.class);
	}
	
	@Override
	public PacketData handleSingle(WebSocket webSocket, Player player, Packet packet, PacketDisconnect data) {
		SRWeb.removePlayer(player);
		player.getWebSocket().close();
		return null;
	}

}
