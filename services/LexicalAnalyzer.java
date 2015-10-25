package services;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

import model.SymbolTableEntry;

public class LexicalAnalyzer{
	
	private String programContents;
	int currentPos = 0;
	InputStreamReader reader;
	ArrayList<SymbolTableEntry> symbolTable = new ArrayList<SymbolTableEntry>();
	
	
	HashSet<String> operatorSet = new HashSet<String>();
	HashSet<String> keyWordSet = new HashSet<String>();
	HashSet<String> symbolSet = new HashSet<String>();
	
	// |--------------------------------------------------|
	// |Symbol Table									  |
	// |subscript | symbol | token type | memory location |
	// |    	  |		   |			|				  |
    // |--------------------------------------------------|
	
	public LexicalAnalyzer(InputStreamReader reader)
	{
		this.reader = reader;
		
		operatorSet.add("*");
		operatorSet.add("-");
		operatorSet.add("+");
		operatorSet.add("/");
		operatorSet.add("=");
		operatorSet.add("<");
		operatorSet.add(">");
		
		
		symbolSet.add(".");
		symbolSet.add(";");
		symbolSet.add("(");
		symbolSet.add(")");
		
		//add keywords to set
		keyWordSet.add("program");
		keyWordSet.add("begin");
		keyWordSet.add("end.");
		keyWordSet.add("integer");
		keyWordSet.add("array");
		keyWordSet.add("do");
		keyWordSet.add("assign");
		keyWordSet.add("to");
		keyWordSet.add("unless");
		keyWordSet.add("when");
		keyWordSet.add("in");
		keyWordSet.add("out");
		keyWordSet.add("else");
		
		
	}
	
	public String getNextToken()
	{
		StringBuilder sb = new StringBuilder();
		char nextSymbol;
		try 
		{
			nextSymbol = (char) reader.read();
			//skip over any leading whitespace
			while(Character.isWhitespace(nextSymbol) && Character.isSpaceChar(nextSymbol) && reader.ready())
				nextSymbol = (char) reader.read();
			//Case 1: first encountered character is a letter
			if(Character.isJavaIdentifierStart(nextSymbol))
			{
				while(Character.isJavaIdentifierPart(nextSymbol) && reader.ready())
				{	
					sb.append(nextSymbol);
					nextSymbol = (char) reader.read();
				}
				
				String token = sb.toString();
				if(!isTokenInSymbolTable(token))
					symbolTable.add(new SymbolTableEntry(token, this.symbolType(token)));
				return token;
			}
			//Case 2: first character encountered is a digit or symbol
			if(Character.isJavaIdentifierPart(nextSymbol))
			{
				do
				{
					sb.append(nextSymbol);
					nextSymbol = (char) reader.read();
				}while(Character.isJavaIdentifierPart(nextSymbol) && reader.ready());
				
				
				String token = sb.toString();
				if(!isTokenInSymbolTable(token))
					symbolTable.add(new SymbolTableEntry(token, this.symbolType(token)));
				return token;
			}
			//Case 3: first encountered character is a operator
			if(operatorSet.contains(nextSymbol))
			{
				do
				{
					sb.append(nextSymbol);
					nextSymbol = (char) reader.read();
				}while(operatorSet.contains(nextSymbol) && reader.ready());
				
				String token = sb.toString();
				if(!isTokenInSymbolTable(token))
					symbolTable.add(new SymbolTableEntry(token, this.symbolType(token)));
				return token;
			}
			//Case 4: first encountered character is a paranthesis
			if(nextSymbol == '(' || nextSymbol == ')')
			{
				sb.append(nextSymbol);
				String token = sb.toString();
				if(!isTokenInSymbolTable(token))
					symbolTable.add(new SymbolTableEntry(token, this.symbolType(token)));
				return token;
			}
			
			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return "Invalid Token";
	}
	
	public boolean isTokenInSymbolTable(String s)
	{
		for(int i = 0; i < symbolTable.size(); i++)
		{
			if(symbolTable.get(i).getToken().equals(s))
			{
				return true;
			}
		}
		return false;
	}
	
	public String symbolType(String s)
	{
		if(this.isOperator(s))
			return "Operator";
		if(this.isIdentifier(s))
			return "Identifier";
		if(this.isKeyWord(s))
			return "Keyword";
		if(this.isSymbol(s))
			return "Symbol";
		if(this.isConstant(s))
			return "Constant";
		if(this.isIdentifier(s))
			return "Identifier";
		
		return "Not a valid Token";
	}
	
	public boolean isOperator(String s)
	{
		if(operatorSet.contains(s))
			return true;
		return false;
	}
	public boolean isKeyWord(String s)
	{
		if(keyWordSet.contains(s))
			return true;
		return false;
	}
	public boolean isSymbol(String s)
	{
		if(symbolSet.contains(s))
			return true;
		return false;
	}
	
	public boolean isConstant(String s)
	{
		try
		{
			Integer.parseInt(s);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}
	public boolean isIdentifier(String s)
	{
		if(isOperator(s) || isKeyWord(s) || isSymbol(s) || isConstant(s))
			return false;
		return true;
	}
}
