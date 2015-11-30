package model;

public class Quad {

	private String action;
	private String op1;
	private String op2;
	private String result;
	
	public Quad(String action, String op1, String op2, String result)
	{
		this.action = action;
		this.op1 = op1;
		this.op2 = op2;
		this.result = result;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getOp1() {
		return op1;
	}

	public void setOp1(String op1) {
		this.op1 = op1;
	}

	public String getOp2() {
		return op2;
	}

	public void setOp2(String op2) {
		this.op2 = op2;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}