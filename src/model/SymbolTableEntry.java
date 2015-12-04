package model;

public class SymbolTableEntry {
	
	String token;
	String tokenType;
	String memLocation;
	
	
	public SymbolTableEntry(String token, String tokenType)
	{
		this.token = token;
		this.tokenType = tokenType;
		this.memLocation = "---";
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public String getMemLocation() {
		return memLocation;
	}
	public void setMemLocation(String memLocation) {
		this.memLocation = memLocation;
	}
	
	

}



