package model;

public class SymbolTableEntry {
	
	String token;
	String tokenType;
	int memLocation;
	
	
	public SymbolTableEntry(String token, String tokenType)
	{
		this.token = token;
		this.tokenType = tokenType;
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
	public int getMemLocation() {
		return memLocation;
	}
	public void setMemLocation(int memLocation) {
		this.memLocation = memLocation;
	}
	
	

}

