package me.mrletsplay.srweb.packet.handler.impl;

import org.java_websocket.WebSocket;

import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.PacketData;
import me.mrletsplay.srweb.packet.handler.SingleTypePacketHandler;
import me.mrletsplay.srweb.packet.impl.PacketClientAuthRequest;

public class AuthRequestHandler extends SingleTypePacketHandler<PacketClientAuthRequest> {

	public AuthRequestHandler() {
		super(false, PacketClientAuthRequest.class);
	}

	@Override
	public PacketData handleSingle(WebSocket webSocket, Player player, Packet packet, PacketClientAuthRequest data) {
		return null;
	}

}
