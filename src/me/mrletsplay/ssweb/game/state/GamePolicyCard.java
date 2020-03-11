package me.mrletsplay.ssweb.game.state;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.packet.JavaScriptEnum;
import me.mrletsplay.ssweb.packet.JavaScriptGetter;

public enum GamePolicyCard implements JavaScriptEnum {
	
	LIBERAL(GameParty.LIBERAL),
	COMMUNIST(GameParty.COMMUNIST),
	FASCIST(GameParty.FASCIST),
	;
	
	@JSONValue
	@JavaScriptGetter("getParty")
	private GameParty party;
	
	private GamePolicyCard(GameParty party) {
		this.party = party;
	}

	public GameParty getParty() {
		return party;
	}
	
}