package me.mrletsplay.ssweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.game.RoomSettings;
import me.mrletsplay.ssweb.packet.JavaScriptSetter;
import me.mrletsplay.ssweb.packet.PacketData;

public class PacketClientConnect extends PacketData {
	
	@JSONValue
	@JavaScriptSetter("setPlayerName")
	private String playerName;
	
	@JSONValue
	@JavaScriptSetter("setCreateRoom")
	private boolean createRoom;

	@JSONValue
	@JavaScriptSetter("setRoomID")
	private String roomID;

	@JSONValue
	@JavaScriptSetter("setRoomName")
	private String roomName;
	
	@JSONValue
	@JavaScriptSetter("setRoomSettings")
	private RoomSettings roomSettings;
	
	@JSONConstructor
	private PacketClientConnect() {}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public boolean isCreateRoom() {
		return createRoom;
	}
	
	public String getRoomID() {
		return roomID;
	}
	
	public String getRoomName() {
		return roomName;
	}
	
	public RoomSettings getRoomSettings() {
		return roomSettings;
	}

}