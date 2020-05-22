package me.mrletsplay.srweb.auth;

import me.mrletsplay.mrcore.config.FileCustomConfig;
import me.mrletsplay.srweb.util.SRWebConfig;

public interface AuthMethod {
	
	public boolean isAvailable();
	
	public String getIdentifier();
	
	public String getAuthURL(String redirectURL);
	
	public default FileCustomConfig getConfig() {
		return SRWebConfig.getAuthConfig(getIdentifier());
	}
	
}
