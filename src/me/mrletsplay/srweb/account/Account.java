package me.mrletsplay.srweb.account;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONConvertible;
import me.mrletsplay.mrcore.json.converter.JSONValue;

public class Account implements JSONConvertible {
	
	@JSONValue
	private String userID;
	
	@JSONValue
	private String username;

	@JSONConstructor
	private Account() {}
	
	public Account(String userID, String username) {
		this.userID = userID;
		this.username = username;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public String getUsername() {
		return username;
	}

}
