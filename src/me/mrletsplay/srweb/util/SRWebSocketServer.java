package me.mrletsplay.srweb.util;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.converter.JSONConverter;
import me.mrletsplay.srweb.SRWeb;
import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.packet.ClassSerializer;
import me.mrletsplay.srweb.packet.JavaScriptConvertible;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.handler.IPacketHandler;
import me.mrletsplay.srweb.packet.impl.PacketServerKeepAlive;

public class SRWebSocketServer extends WebSocketServer {
	
	private List<IPacketHandler> handlers;
	
	public SRWebSocketServer(InetSocketAddress address) {
		super(address);
		this.handlers = new ArrayList<>();
		setReuseAddr(true);
		setTcpNoDelay(true);
		
		if(SRWebConfig.isEnableSSL()) {
			SSLContext ctx = SSLHelper.getContext();
			setWebSocketFactory(new DefaultSSLWebSocketServerFactory(ctx));
			System.out.println("Successfully enabled SSL");
		}
		
		new Thread(() -> {
			while(true) {
				for(Player p : SRWeb.getPlayers()) {
					try {
						p.send(new Packet(new PacketServerKeepAlive()));
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
				
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					return;
				}
			}
		}, "Server-KeepAlive").start();
	}
	
	public void addHandler(IPacketHandler handler) {
		handlers.add(handler);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println("Connection from " + conn.getRemoteSocketAddress());
		JSONObject obj = new JSONObject();
		obj.put("init", true);
		
		JSONArray array = new JSONArray();
		for(Class<? extends JavaScriptConvertible> cls : SRWeb.SERIALIZABLE_CLASSES) {
			array.add(ClassSerializer.serializeClass(cls));
		}
		obj.put("classes", array);
		
		conn.send(obj.toFancyString());
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("Disconnect from " + conn.getRemoteSocketAddress() + " (code: " + code + ")");
		Player p = SRWeb.getPlayer(conn);
		if(p != null) {
			p.setWebSocket(null);
			SRWeb.removePlayer(p);
		}
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		if(message.length() > 16384) {
			conn.close(CloseFrame.TOOBIG);
			return;
		}
		
		System.out.println(">> " + message);
		
		Packet p = JSONConverter.decodeObject(new JSONObject(message), Packet.class);
		Player pl = SRWeb.getPlayer(conn);
		
		/*if(pl == null) {
			if(p.getData() instanceof PacketClientConnect) {
				
			}else if(p.getData() instanceof PacketClientAuthRequest) {
				
			}else {
				conn.close(CloseFrame.POLICY_VALIDATION, "Not a connect packet");
				return;
			}
		}*/
		
		for(IPacketHandler handler : handlers) {
			if(handler.shouldHandle(p)) {
				try {
					pl.send(new Packet(p.getID(), handler.handle(conn, pl, p, p.getData())));
				}catch(Exception e) {
					e.printStackTrace();
					conn.close(CloseFrame.POLICY_VALIDATION, "Exception in handler");
				}
				return;
			}
		}
		
		conn.close(CloseFrame.POLICY_VALIDATION, "No handler available");
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
	}

	@Override
	public void onStart() {
		System.out.println("Server is listening on port " + getPort());
	}
	
}
