package me.mrletsplay.srweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.packet.PacketData;

public class PacketClientAuthRequest extends PacketData {
	
	@JSONValue
	private String discordCode;

	
	
}
