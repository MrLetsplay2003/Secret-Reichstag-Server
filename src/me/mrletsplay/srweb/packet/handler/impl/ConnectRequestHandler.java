package me.mrletsplay.srweb.packet.handler.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;

import me.mrletsplay.srweb.SRWeb;
import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.Room;
import me.mrletsplay.srweb.game.state.GameMoveState;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.PacketData;
import me.mrletsplay.srweb.packet.handler.SingleTypePacketHandler;
import me.mrletsplay.srweb.packet.impl.PacketClientConnect;
import me.mrletsplay.srweb.packet.impl.PacketServerJoinError;
import me.mrletsplay.srweb.packet.impl.PacketServerRoomInfo;
import me.mrletsplay.srweb.session.PlayerSession;
import me.mrletsplay.srweb.session.SRWebSessionStore;

public class ConnectRequestHandler extends SingleTypePacketHandler<PacketClientConnect> {
	
	private static final Pattern NAME_PATTERN = Pattern.compile("(?:[a-zA-Z0-9äöü ]){1,20}");

	public ConnectRequestHandler() {
		super(PacketClientConnect.class);
	}
	
	@Override
	public PacketData handleSingle(WebSocket webSocket, Player player, Packet packet, PacketClientConnect data) {
		if(data.getSessionID() != null) {
			PlayerSession sess = SRWebSessionStore.getSession(data.getSessionID());
			
			if(sess == null) {
				return new PacketServerJoinError("Cannot rejoin, session expired");
			}
			
			Player pl = sess.getPlayer();
			
			if(pl.isOnline()) {
				return new PacketServerJoinError("Session already in use");
			}
			
			if(pl.getRoom() == null) {
				return new PacketServerJoinError("Cannot rejoin, player left normally");
			}
			
			pl.setWebSocket(webSocket);
			
			if(SRWeb.getRoom(pl.getRoom().getID()) == null) {
				return new PacketServerJoinError("Cannot rejoin, room closed");
			}
			
			SRWeb.addPlayer(pl);
			PacketServerRoomInfo rI = new PacketServerRoomInfo(data.getSessionID(), pl, pl.getRoom());
			
			if(pl.getRoom().isGameRunning() && pl.getRoom().getGameState().getMoveState().equals(GameMoveState.VOTE)
					&& pl.getRoom().getGameState().getVoteState().getVote(pl) != null) {
				rI.setVoteDone(true);
			}
			
			
			pl.getRoom().rejoinPlayer(pl);
			return rI;
		}
		
		String pName = data.getPlayerName().trim();
		
		if(pName.isEmpty()) {
			return new PacketServerJoinError("Name cannot be empty");
		}
		
		if(pName.length() > 20) {
			return new PacketServerJoinError("Name cannot be longer than 20 characters");
		}
		
		Matcher m = NAME_PATTERN.matcher(pName);
		if(!m.matches()) {
			return new PacketServerJoinError("Name contains invalid characters");
		}
		
		Player pl = new Player(webSocket, data.getPlayerName());
		
		Room r;
		
		if(data.isCreateRoom()) {
			if(data.getRoomName() == null || data.getRoomName().isEmpty()) {
				webSocket.close(CloseFrame.POLICY_VALIDATION);
				return null;
			}
			
			if(!data.getRoomSettings().isValid()) {
				return new PacketServerJoinError("Invalid room settings");
			}
			
			r = SRWeb.createRoom(data.getRoomName(), data.getRoomSettings());
		}else {
			if(data.getRoomID() == null || data.getRoomID().isEmpty()) {
				webSocket.close(CloseFrame.POLICY_VALIDATION);
			}
			
			r = SRWeb.getRoom(data.getRoomID());
			if(r == null) {
				return new PacketServerJoinError("Invalid room id");
			}
			
			if(r.getPlayers().stream().anyMatch(o -> o.getName().toLowerCase().equals(pName.toLowerCase()))) {
				return new PacketServerJoinError("Name already taken");
			}
			
			if(r.isFull()) {
				return new PacketServerJoinError("Room is full");
			}
			
			if(r.isGameRunning()) {
				return new PacketServerJoinError("Game is in progress");
			}
		}

		SRWeb.addPlayer(pl);
		String sessID = SRWebSessionStore.createSession(pl);
		r.addPlayer(pl);
		return new PacketServerRoomInfo(sessID, pl, r);
//		}
	}
	
}
