package services;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import model.SymbolTableEntry;

public class LexicalAnalyzer{
	
	private int line = 1;
	BufferedReader reader;
	ArrayList<SymbolTableEntry> symbolTable = new ArrayList<SymbolTableEntry>();
	
	
	HashSet<String> operatorSet = new HashSet<String>();
	HashSet<String> keyWordSet = new HashSet<String>();
	HashSet<String> symbolSet = new HashSet<String>();
	
	// |--------------------------------------------------|
	// |Symbol Table									  |
	// |subscript | symbol | token type | memory location |
	// |    0	  |program |keyword		|				  |
	// |    1	  |begin   |keyword		|				  |
	// |    2	  |end.	   |keyword		|				  |
	// |    3	  |integer |keyword		|				  |
	// |    4	  |array   |keyword		|				  |
	// |    5	  |do	   |keyword		|				  |
	// |    6	  |assign  |keyword		|				  |
	// |    7	  |to	   |keyword		|				  |
	// |    8	  |unless  |keyword		|				  |
	// |    9	  |when	   |keyword		|				  |
	// |    10	  |in	   |keyword		|				  |
	// |    11	  |out	   |keyword		|				  |
	// |    12	  |else	   |keyword		|				  |
	// |    13	  |and	   |keyword		|				  |
	// |    14	  |or	   |keyword		|				  |
	// |    15	  |not	   |keyword		|				  |
	// |    16	  | .	   |symbol		|				  |
	// |    17	  | ,	   |symbol  	|				  |
	// |    18	  |	;	   |symbol		|				  |
	// |    19	  |	:	   |symbol		|				  |
	// |    20	  |	(	   |symbol		|				  |
	// |    21	  |	)	   |symbol		|				  |
	// |    22	  |	+	   |operator	|				  |
	// |    23	  |	-	   |operator	|				  |
	// |    24	  |	*	   |operator	|				  |
	// |    25	  |	/	   |operator	|				  |
	// |    26	  |	=	   |operator	|				  |
	// |    27	  |	<	   |operator	|				  |
	//|     28	  |	>	   |operator	|				  |
    // |--------------------------------------------------|
	
	public LexicalAnalyzer(BufferedReader reader)
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
		symbolSet.add(",");
		symbolSet.add(";");
		symbolSet.add(":");
		symbolSet.add("(");
		symbolSet.add(")");
		
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
		keyWordSet.add("and");
		keyWordSet.add("or");
		keyWordSet.add("not");
		
		symbolTable.add(new SymbolTableEntry("program", "keyword"));
		symbolTable.add(new SymbolTableEntry("begin", "keyword"));
		symbolTable.add(new SymbolTableEntry("end.", "keyword"));
		symbolTable.add(new SymbolTableEntry("integer", "keyword"));
		symbolTable.add(new SymbolTableEntry("array", "keyword"));
		symbolTable.add(new SymbolTableEntry("do", "keyword"));
		symbolTable.add(new SymbolTableEntry("assign", "keyword"));
		symbolTable.add(new SymbolTableEntry("to", "keyword"));
		symbolTable.add(new SymbolTableEntry("unless", "keyword"));
		symbolTable.add(new SymbolTableEntry("when", "keyword"));
		symbolTable.add(new SymbolTableEntry("in", "keyword"));
		symbolTable.add(new SymbolTableEntry("out", "keyword"));
		symbolTable.add(new SymbolTableEntry("else", "keyword"));
		symbolTable.add(new SymbolTableEntry("and", "keyword"));
		symbolTable.add(new SymbolTableEntry("or", "keyword"));
		symbolTable.add(new SymbolTableEntry("not", "keyword"));
		symbolTable.add(new SymbolTableEntry(".", "symbol"));
		symbolTable.add(new SymbolTableEntry(",", "symbol"));
		symbolTable.add(new SymbolTableEntry(";", "symbol"));
		symbolTable.add(new SymbolTableEntry(":", "symbol"));
		symbolTable.add(new SymbolTableEntry("(", "symbol"));
		symbolTable.add(new SymbolTableEntry(")", "symbol"));
		symbolTable.add(new SymbolTableEntry("+", "op"));
		symbolTable.add(new SymbolTableEntry("-", "op"));
		symbolTable.add(new SymbolTableEntry("*", "op"));
		symbolTable.add(new SymbolTableEntry("/", "op"));
		symbolTable.add(new SymbolTableEntry("=", "op"));
		symbolTable.add(new SymbolTableEntry("<", "op"));
		symbolTable.add(new SymbolTableEntry(">", "op"));
		
	}
	
	public String getNextToken()
	{
		StringBuilder sb = new StringBuilder();
		char nextSymbol;
		try 
		{
			nextSymbol = (char) reader.read();
			//skip over any leading whitespace
			while((Character.isWhitespace(nextSymbol) || Character.isSpaceChar(nextSymbol)) && reader.ready())
			{
				if(nextSymbol == '\n')
					this.line++;
				nextSymbol = (char) reader.read();
			}
			//Case 1: first encountered character is a letter
			if(Character.isJavaIdentifierStart(nextSymbol))
			{
				while(Character.isJavaIdentifierPart(nextSymbol) && reader.ready())
				{	
					sb.append(nextSymbol);
					reader.mark(0);
					nextSymbol = (char) reader.read();
				}
				reader.reset();
				String token = sb.toString();
				
				if(token.equals("end"))
				{
					reader.mark(0);
					nextSymbol = (char) reader.read();
					if(nextSymbol == '.')
					{
						sb.append(nextSymbol);
						token = sb.toString();
					}
					else
						reader.reset();
				}
				if(!isTokenInSymbolTable(token))
					symbolTable.add(new SymbolTableEntry(token, this.symbolType(token)));
				return token;
			}
			//Case 2: first character encountered is a digit or symbol
			else if(Character.isJavaIdentifierPart(nextSymbol))
			{
				do
				{
					sb.append(nextSymbol);
					reader.mark(0);
					nextSymbol = (char) reader.read();
				}while(Character.isJavaIdentifierPart(nextSymbol) && reader.ready());
				
				reader.reset();
				String token = sb.toString();
				if(!isTokenInSymbolTable(token))
					symbolTable.add(new SymbolTableEntry(token, this.symbolType(token)));
				return token;
			}
			//Case 3: first encountered character is a operator
			else if(nextSymbol == '+' || nextSymbol == '-' || nextSymbol == '*' || nextSymbol == '/' || nextSymbol == '<' || nextSymbol == '>' || nextSymbol == '=')
			{
				do
				{
					sb.append(nextSymbol);
					reader.mark(0);
					nextSymbol = (char) reader.read();
				}while((nextSymbol == '+' || nextSymbol == '-' || nextSymbol == '*' || nextSymbol == '/' || nextSymbol == '<' || nextSymbol == '>' || nextSymbol == '=') && reader.ready());
				reader.reset();
				String token = sb.toString();
				if(!isTokenInSymbolTable(token))
					symbolTable.add(new SymbolTableEntry(token, this.symbolType(token)));
				return token;
			}
			//Case 4: first encountered character is a paranthesis
			else if(nextSymbol == '(' || nextSymbol == ')' || nextSymbol == ',' || nextSymbol == ';' || nextSymbol == ':')
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
			return "op";
		if(this.isIdentifier(s))
			return "id";
		if(this.isKeyWord(s))
			return "keyword";
		if(this.isSymbol(s))
			return "symbol";
		if(this.isConstant(s))
			return "cons";
		
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
	public int getLine()
	{
		return this.line;
	}
	
	public void printSymbolTable()
	{
		System.out.println("|\t\tsubscript\t|\t\tsymbol\t\t|\t\ttoken type\t|\t\tmemory location\t\t|");
		for(int i = 0; i < this.symbolTable.size(); i++)
		{
			System.out.println("|\t\t"+i+"\t\t|\t\t"+symbolTable.get(i).getToken()+"\t\t|\t\t"+symbolTable.get(i).getTokenType()+"\t\t|\t\t"+symbolTable.get(i).getMemLocation()+"\t\t|");
		}
	}
}
