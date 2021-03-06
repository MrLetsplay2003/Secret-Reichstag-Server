package me.mrletsplay.srweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.packet.PacketData;

public class PacketDisconnect extends PacketData {
	
	@JSONValue
	private String reason;
	
	@JSONConstructor
	private PacketDisconnect() {}
	
	public PacketDisconnect(String reason) {
		this.reason = reason;
	}
	
	public String getReason() {
		return reason;
	}

}
