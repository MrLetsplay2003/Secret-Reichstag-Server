package me.mrletsplay.srweb.auth;

import me.mrletsplay.mrcore.config.FileCustomConfig;
import me.mrletsplay.mrcore.http.HttpUtils;

public class DiscordAuth implements AuthMethod {

	private static final String
		AUTH_ENDPOINT = "https://discordapp.com/api/oauth2/authorize",
		TOKEN_ENDPOINT = "https://discordapp.com/api/oauth2/token";
	
	private String
		clientID,
		clientSecret;
	
	public DiscordAuth() {
		FileCustomConfig cfg = getConfig();
		boolean init = cfg.isEmpty();
		cfg.addDefault("client-id", "[INSERT CLIENT ID HERE]");
		cfg.addDefault("client-secret", "[INSERT CLIENT SECRET HERE]");
		cfg.saveToFile();
		
		if(init) {
			System.out.println("Please configure the auth method \"" + getIdentifier() + "\" and restart");
			return;
		}
		
		clientID = cfg.getString("client-id");
		clientSecret = cfg.getString("client-secret");
	}
	
	public String getAuthURL(String redirectURL) {
		return AUTH_ENDPOINT
			+ "?client_id=" + HttpUtils.urlEncode(clientID)
			+ "&redirect_uri=" + HttpUtils.urlEncode(redirectURL) // TODO: protocol
			+ "&response_type=code"
			+ "&scope=identify email";
	}

	@Override
	public String getIdentifier() {
		return "discord";
	}

	@Override
	public boolean isAvailable() {
		return clientID != null && clientSecret != null;
	}

}
