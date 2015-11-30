package model;

public class Temp {
	
	private int varCount = 0;
	private String name;
	private String tokenType;
	
	public int getVarCount() {
		return varCount;
	}

	public void setVarCount(int varCount) {
		this.varCount = varCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public Temp(String tokenType)
	{
		varCount++;
		this.name = "t" + varCount;
		this.tokenType = tokenType;
	}
	
	public Temp genTemp(String tokenType)
	{
		return new Temp(tokenType);
	}
	

}
