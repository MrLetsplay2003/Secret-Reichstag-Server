package me.mrletsplay.srweb.game;

import me.mrletsplay.srweb.packet.JavaScriptEnum;

public enum GameMode implements JavaScriptEnum {
	
	SECRET_HITLER(5, 10, 7, 6),
	SECRET_REICHSTAG(7, 14, 10, 8);
	
	private final int
		minPlayers,
		maxPlayers,
		minInvisible,
		minPrevPresident;

	private GameMode(int minPlayers, int maxPlayers, int minInvisible, int minPrevPresident) {
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		this.minInvisible = minInvisible;
		this.minPrevPresident = minPrevPresident;
	}
	
	public int getMinPlayers() {
		return minPlayers;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public int getMinInvisible() {
		return minInvisible;
	}
	
	public int getMinPrevPresident() {
		return minPrevPresident;
	}
	
}
