package services;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Stack;

import model.Quad;
import model.SymbolTableEntry;
import services.LexicalAnalyzer;

public class SyntaxAnalyzer {
	
	private LexicalAnalyzer lex;
	private String NextToken;
	private int Subscript; 
	private Stack<String> SAS = new Stack<String>();
	private ArrayList<Quad> QuadTable = new ArrayList<Quad>();
	private int varCount = 0; // used for genTemp();
	
	public SyntaxAnalyzer(BufferedReader reader)
	{
		lex = new LexicalAnalyzer(reader);
		NextToken = lex.getNextToken();
	}
	
	public LexicalAnalyzer getLex()
	{
		return lex;
	}
	

	public void genQuad(String action, String op1, String op2, String result)
	{
		Quad newQuad = new Quad(action, op1, op2, result);
		QuadTable.add(newQuad);
		
	}
	
	public String getNextQuad()
	{
		return Integer.toString(QuadTable.size() -1);
	}
	
	public String genTemp(String tokenType)
	{
		String varName = "t" + varCount++;
		SymbolTableEntry newEntry = new SymbolTableEntry(varName, tokenType);
		lex.symbolTable.add(newEntry);
		return varName;
	}
	
	public void printQuadTable()
	{
		System.out.println("|=======================================================================================================================================================|");
		System.out.println("|---------------------------------------------------------------------------QUAD TABLE------------------------------------------------------------------|");
		System.out.println("|=======================================================================================================================================================|");
		System.out.println("|\t\tsubscript\t|\t\taction\t\t|\t\top1\t\t|\t\top2\t\t|\tresult\t\t|");
		System.out.println("|-------------------------------------------------------------------------------------------------------------------------------------------------------|");
		for(int i = 0; i < this.QuadTable.size(); i++)
		{
			System.out.println("|\t\t"+i+"\t\t|\t\t"+QuadTable.get(i).getAction()+"\t\t|\t\t"+QuadTable.get(i).getOp1()+"\t\t|\t\t"+QuadTable.get(i).getOp2()+"\t\t|\t" + QuadTable.get(i).getResult() + "\t\t|");
		}
		System.out.println("|-------------------------------------------------------------------------------------------------------------------------------------------------------|");
	}
 	public boolean P()
	{
		if(NextToken.equals("program"))
		{
			NextToken = lex.getNextToken();
			
			if(this.D())
			{
				if(NextToken.equals("begin"))
				{
					NextToken = lex.getNextToken();
					
					if(this.S())
					{
						if(NextToken.equals("end."))
						{
							NextToken = lex.getNextToken();
							return true; 
						}
						else
						{
							System.err.println("Expected: {'end.'} Got: " + NextToken + " @ line: " + lex.getLine());
							return false;
						}
					}
					else
					{
						return false;
					}
				
				}
				else
				{
					System.err.println("Expected: {'begin'} Got: " + NextToken + " @ line: " + lex.getLine());
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			System.err.println("Expected: {'program'} Got: " + NextToken + " @ line: " + lex.getLine());
			return false; 
		}
	}
	
	private boolean D()
	{	
		if(NextToken.equals("begin"))
		{
			return true;
		}
		else if(!NextToken.equals("begin") && (NextToken.equals(",") || lex.isIdentifier(NextToken) || NextToken.equals(":") || NextToken.equals(")")))
		{
			//Semantic Action
			//---------------------------
			SAS.push("#");
			//---------------------------
			if(this.IL())
			{
				if(NextToken.equals(":"))
				{
					NextToken = lex.getNextToken();
					if(this.D1())
					{
						return true;
					}
					else
					{
						return false;
					}
				}
				else
				{
					System.err.println("Expected {':'} Got: " + NextToken + " @ line: " + lex.getLine());
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		
		else
		{
			System.err.println("Expected: {'begin'} Got: " + NextToken + " @ line: " + lex.getLine());
			return false;
		}
		
	}

	private boolean D1()
	{
		//Check Selection Set of AR() first
		if(NextToken.equals("array"))
		{
			if(this.AR())
			{
				if(this.D())
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else if(NextToken.equals("integer"))
		{
			//Semantic Action
			//---------------------------------
			while(SAS.peek() != "#")
			{
				String x = SAS.pop();
				Subscript = lex.getSymbolSubscript(x);
				lex.symbolTable.get(Subscript).setTokenType("integer");
				
			}
			SAS.pop();
			//---------------------------------
			NextToken = lex.getNextToken();
			if(this.D())
			{
				return true;
			}
			else
			{
				return false;
			}
		}	
		else
		{	
			System.err.println("Expected{'integer'} Got: " + NextToken + " @ line: " + lex.getLine());
			return false;
		}
		
		
	}

	private boolean AR()
	{
		if(NextToken.equals("array"))
		{
			//Semantic Action
			//---------------------------------
			while(SAS.peek() != "#")
			{
				String x = SAS.pop();
				Subscript = lex.getSymbolSubscript(x);
				lex.symbolTable.get(Subscript).setTokenType("array");
				
			}
			//---------------------------------
			NextToken = lex.getNextToken();
			if(this.AR1())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			System.err.println("Expected: {'array'} Got: " + NextToken + " @ line: " + lex.getLine());
			return false;
		}
	}

	private boolean AR1()
	{
		if(NextToken.equals("("))
		{
			NextToken = lex.getNextToken();
			
			if(this.AR())
			{
				if(NextToken.equals(")"))
				{
					NextToken = lex.getNextToken();
					return true;
				}
				else
				{
					System.err.println("Expected: {')'} Got: " + NextToken + " @ line: " + lex.getLine());
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else if(lex.isIdentifier(NextToken) || NextToken.equals(",") || NextToken.equals("begin") || NextToken.equals(":") || NextToken.equals(")"))
		{
			return true;
		}
		else
		{
			System.err.println("Expected: { '(' 'id' 'begin' ',' ':' ')' } Got: " + NextToken + " @ line: " + lex.getLine());
			return false;
		}
	}

	private boolean IL()
	{
		if(NextToken.equals(","))
		{
			NextToken = lex.getNextToken();
			if(this.ID())
			{
				if(this.IL())
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else if(NextToken.equals(":") || NextToken.equals(")"))
		{
			return true;
		}
		else if(lex.isIdentifier(NextToken))
		{
			if(this.ID())
			{
				if(this.IL())
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			System.err.println("Expected: {':' ')' ','} Got: " + NextToken + " @ line: " + lex.getLine());
			return false;
		}
	}

	private boolean ID()
	{
		if(lex.isIdentifier(NextToken))
		{
			//Semantic Action
			//----------------------
			SAS.push(NextToken);
			//----------------------
			
			NextToken = lex.getNextToken();
			if(this.ID1())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			System.err.println("InvalidIdentifierException: Expected: {'id'} Got: " + NextToken + " @ line: " + lex.getLine());
			return false;
		}
	}

	private boolean ID1()
	{
		if(NextToken.equals("("))
		{
			NextToken = lex.getNextToken();
			if(this.SB())
			{
				if(NextToken.equals(")"))
				{
					NextToken = lex.getNextToken();
					return true;
				}
				else
				{
					System.err.println("Expected: {')'} Got: " + NextToken + " @ line: " + lex.getLine());
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else if(lex.isIdentifier(NextToken) || NextToken.equals(",") || NextToken.equals(":") || NextToken.equals(";") || NextToken.equals(")"))
		{
			return true;
		}
		else
		{
			System.err.println("Expected: {'(' 'id' ',' ':' ';' ')'} Got: " + NextToken + " @ line: " + lex.getLine());
			return false;
		}
	}

	private boolean SB()
	{ 
		if(lex.isConstant(NextToken) || lex.isIdentifier(NextToken))
		{
			//Semantic Action
			//-----------------------
			SAS.push(NextToken);
			//-----------------------
			
			NextToken = lex.getNextToken();
			if(this.SB())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if(NextToken.equals(","))
		{
			NextToken = lex.getNextToken();
			if(this.SB1())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if(NextToken.equals(")"))
		{
			return true;
		}
		else
		{
			System.err.println("Expected: {'cons' 'id' ',' ')'} Got: " + NextToken + " @ line: " + lex.getLine());
			return false;
		}
		
	}

	private boolean SB1()
	{
		if(lex.isConstant(NextToken) || lex.isIdentifier(NextToken))
		{
			//Semantic Action
			//-----------------------
			SAS.push(NextToken);
			//-----------------------
			NextToken = lex.getNextToken();
			if(this.SB())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			System.err.println("Expected: {'cons' 'id'} Got: " + NextToken + " @ line: " + lex.getLine());
			return false;
		}
	}
	
	private boolean S()
	{
		if(NextToken.equals("do"))
		{
			//Semantic Action
			//-----------------------------
			genQuad("jmp","---","---","---");
			SAS.push(getNextQuad());
			//-----------------------------
			NextToken = lex.getNextToken();
			if(this.S())
			{
				if(this.S1())
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else if(NextToken.equals("("))
		{
			NextToken = lex.getNextToken();
			if(this.IL())
			{
				if(NextToken.equals(")"))
				{
					NextToken = lex.getNextToken();
					if(this.S2())
					{
						return true;
					}
					else
					{
						return false;
					}
				}
				else
				{
					System.err.println("Expected: {')'} Got: " + NextToken + " @ line: " + lex.getLine());
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else if(NextToken.equals("assign"))
		{
			//Semantic Action
			//-----------------------
			String op = ":=";
			//-----------------------
			NextToken = lex.getNextToken();
			if(this.E())
			{
				//Semantic Action
				//-----------------------
				String x = SAS.pop();
				//-----------------------
				if(NextToken.equals("to"))
				{
					NextToken = lex.getNextToken();
					if(this.ID())
					{
						//Semantic Action
						//-----------------------
						String y = SAS.pop();
						genQuad(op,x,"---",y);
						//-----------------------
						if(NextToken.equals(";"))
						{
							NextToken = lex.getNextToken();
							if(this.S())
							{
								return true;
							}
							else
							{
								return false;
							}
						}
						else
						{
							System.err.println("Expected: {';'} Got: " + NextToken + " @ line: " + lex.getLine());
							return false;
						}
					}
					else
					{
						return false;
					}
				}
				else
				{
					System.err.println("Expected: {'to'} Got: " + NextToken + " @ line: " + lex.getLine());
					return false;
				}
			}
			else 
			{
				return false;
			}
		}
		else if(NextToken.equals("unless") || NextToken.equals("when") || NextToken.equals("end.") || NextToken.equals(";"))
		{
			return true;
		}
		else
		{
			System.err.println("Expected: {'do' '(' 'assign' 'unless' 'when' 'end.' ';'} Got: " + NextToken + " @ line: " + lex.getLine());
			return false;
		}

	}
	
	private boolean S1()
	{
		if(NextToken.equals("unless"))
		{
			
			NextToken = lex.getNextToken();
			if(this.C())
			{
				//Semantic Action
				//--------------------------
				String x = SAS.pop(); // temp generated by C Boolean for condition
				String y = SAS.pop(); // NextQuad variable to jump back to
				int loopStart = Integer.parseInt(y) +1;
				genQuad("jfalse",x,"---", Integer.toString(loopStart)); 
				
				// update quad table 
				int quadUpdate = Integer.parseInt(getNextQuad()) - 1;
				QuadTable.get(Integer.parseInt(y)).setResult(Integer.toString(quadUpdate));
				//--------------------------
				if(NextToken.equals(";"))
				{
					NextToken = lex.getNextToken();
					if(this.S())
					{
						return true;
					}
					else
					{
						return false;
					}
				}
				else
				{
					System.err.println("Expected: {';'} Got: " + NextToken + " @ line: " + lex.getLine());
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else if(NextToken.equals("when"))
		{
			NextToken = lex.getNextToken();
			if(this.C())
			{
				//Semantic Action
				//--------------------------
				String x = SAS.pop(); // temp generated by C Boolean for condition
				String y = SAS.pop(); // NextQuad variable to jump back to
				int ifAction = Integer.parseInt(y) +1;
				
				//------------------------
				Quad newQuad = new Quad("jmp", "---","---","---");
				int addIndex = QuadTable.size() - 1;
				QuadTable.add(addIndex, newQuad);
				SAS.push(Integer.toString(addIndex)); // pushing next quad to know where to update quad table.
				//------------------------
				genQuad("jtrue",x,"---", Integer.toString(ifAction)); 
				
				// update quad table 
				int quadUpdate = Integer.parseInt(getNextQuad()) - 1;
				QuadTable.get(Integer.parseInt(y)).setResult(Integer.toString(quadUpdate));
				//--------------------------
				if(this.S3())
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			System.err.println("Expected: {'unless' 'when'} Got: " + NextToken + " @ line: " + lex.getLine());
			return false;
		}
	}

	private boolean S2()
	{
		if(NextToken.equals("in") || NextToken.equals("out"))
		{
			//Semantic Action
			//-----------------------
			String op = NextToken;
			String x = SAS.pop();
			genQuad(op, "---", "---", x);
			//-----------------------
			NextToken = lex.getNextToken();
			if(NextToken.equals(";"))
			{
				NextToken = lex.getNextToken();
				if(this.S())
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				System.err.println("Expected: {';'} Got: " + NextToken + " @ line: " + lex.getLine());
				return false;
			}
		}
		else
		{
			System.err.println("Expected: {'in' 'out'} Got: " + NextToken + " @ line: " + lex.getLine());
			return false;
		}
	}

	private boolean S3()
	{
		if(NextToken.equals(";"))
		{
			//SEMANTIC ACTION
			//-------------------------------------
			String x = SAS.pop();
			int quadUpdate = Integer.parseInt(getNextQuad()) - 1;
			QuadTable.get(Integer.parseInt(x)).setResult(Integer.toString(quadUpdate));
			//-------------------------------------
			NextToken = lex.getNextToken();
			if(this.S())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if(NextToken.equals("else"))
		{
			
			NextToken = lex.getNextToken();
			if(this.S())
			{
				if(NextToken.equals(";"))
				{
					//SEMANTIC ACTION
					//--------------------------------
					String x = SAS.pop();
					int quadUpdate = Integer.parseInt(getNextQuad()) + 1;
					QuadTable.get(Integer.parseInt(x)).setResult(Integer.toString(quadUpdate));
					//--------------------------------
					NextToken = lex.getNextToken();
					if(this.S())
					{
						return true;
					}
					else
					{
						return false;
					}
				}
				else
				{
					System.err.println("Expected: {';'} Got: " + NextToken + " @ line: " + lex.getLine());
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			System.err.println("Expected: {';' 'else'} Got: " + NextToken + " @ line: " + lex.getLine());
			return false;
		}
	}

	private boolean E()
	{
		Boolean flag = false;
		String op = null;
		Boolean result;
		
		if(lex.isIdentifier(NextToken) || lex.isConstant(NextToken))
		{
			//Semantic Action
			//----------------------
			SAS.push(NextToken);
			//----------------------
			
			NextToken = lex.getNextToken();
			return true;
		}
		else if(NextToken.equals("+") || NextToken.equals("-") || NextToken.equals("*") || NextToken.equals("/"))
		{
			//Semantic Action
			//-----------------------
			flag = true;
			op = NextToken;
			//-----------------------
			
			NextToken = lex.getNextToken();
			if(this.E())
			{
				if(this.E())
				{
					result = true;
				}
				else
				{
					result = false;
				}
			}
			else
			{
				result = false;
			}
		}
		else
		{
			System.err.println("Expected: {'id' 'cons' '+' '-' '*' '/'} Got: " + NextToken + " @ line: " + lex.getLine());
			result = false;
		}
		
		if(flag && result)
		{
			String x = SAS.pop();
			String y = SAS.pop();
			int loc = lex.getSymbolSubscript(y);
			String tempType = lex.symbolTable.get(loc).getTokenType();
			String z = genTemp(tempType);
			genQuad(op,x,y,z);
			SAS.push(z);
			
		}
		return result;
	}

	private boolean C()
	{
		if(NextToken.equals("<") || NextToken.equals(">") || NextToken.equals("="))
		{
			//Semantic Action
			//------------------------------
			String op = NextToken;
			//------------------------------
			NextToken = lex.getNextToken();
			if(this.E())
			{
				if(this.E())
				{
					//Semantic Action
					//---------------------------
					String x = SAS.pop();
					String y = SAS.pop();
					String peek = SAS.peek();
					String z = genTemp("boolean");
					genQuad(op,y,x,z);
					if(peek.equals("not"))
					{
						SAS.pop();
						SAS.push("not " + z);
					}
					else
						SAS.push(z);
					//---------------------------
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else if(NextToken.equals("and") || NextToken.equals("or"))
		{
			//---------------------------
			String op = NextToken;
			//---------------------------
			NextToken = lex.getNextToken();
			if(this.C())
			{
				if(this.C())
				{
					//--------------------------
					String x = SAS.pop();
					String y = SAS.pop();
					String peek = SAS.peek();
					String z = genTemp("boolean");
					genQuad(op,x,y,z);
					if(peek.equals("not"))
					{
						SAS.pop();
						SAS.push("not " + z);
					}
					else
						SAS.push(z);
					//--------------------------
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else if(NextToken.equals("not"))
		{
			//Symantic Action
			//--------------------------------------
			SAS.push("not");
			//--------------------------------------
			NextToken = lex.getNextToken();
			if(this.C())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			System.err.println("Expected: {'<' '>' '=' 'and' 'or' 'not'} Got: " + NextToken + " @ line: " + lex.getLine());
			return false;
		}
	}





}
