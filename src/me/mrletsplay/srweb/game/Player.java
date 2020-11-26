package me.mrletsplay.srweb.game;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.java_websocket.WebSocket;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.game.state.GamePolicyCard;
import me.mrletsplay.srweb.game.state.board.action.GameActionData;
import me.mrletsplay.srweb.packet.JavaScriptConvertible;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.JavaScriptSetter;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.util.SRWebSocketServer;

public class Player implements JavaScriptConvertible {
	
	private SRWebSocketServer server;

	private WebSocket webSocket;
	
	private Room room;
	
	@JSONValue
	@JavaScriptGetter("getID")
	private String id;
	
	@JSONValue
	@JavaScriptGetter("getName")
	private String name;
	
	private List<GamePolicyCard> hand;
	
	private GameActionData actionData;
	
	public Player(SRWebSocketServer server, WebSocket webSocket, String name) {
		this.server = server;
		this.webSocket = webSocket;
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.hand = Collections.emptyList();
	}
	
	public SRWebSocketServer getServer() {
		return server;
	}
	
	public WebSocket getWebSocket() {
		return webSocket;
	}
	
	public String getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setRoom(Room room) {
		this.room = room;
	}
	
	public Room getRoom() {
		return room;
	}
	
	public void setHand(List<GamePolicyCard> hand) {
		this.hand = hand;
	}
	
	public List<GamePolicyCard> getHand() {
		return hand;
	}
	
	public void setWebSocket(WebSocket webSocket) {
		this.webSocket = webSocket;
	}
	
	@JSONValue("online")
	@JavaScriptGetter("isOnline")
	@JavaScriptSetter("setOnline")
	public boolean isOnline() {
		return webSocket != null && webSocket.isOpen();
	}
	
	public void setActionData(GameActionData actionData) {
		this.actionData = actionData;
	}
	
	public GameActionData getActionData() {
		return actionData;
	}
	
	public void send(Packet p) {
		if(!isOnline()) return;
		webSocket.send(p.toJSON().toString());
	}
	
}
