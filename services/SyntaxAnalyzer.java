package services;

import java.io.InputStreamReader;

import services.LexicalAnalyzer;

public class SyntaxAnalyzer {
	
	private LexicalAnalyzer lex;
	private String NextToken;
	private int Subscript; 
	
	
	public SyntaxAnalyzer(InputStreamReader reader)
	{
		lex = new LexicalAnalyzer(reader);
		NextToken = lex.getNextToken();
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
							System.err.println("Expected: {'end.'} Got: " + NextToken);
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
					System.err.println("Expected: {'begin'} Got: " + NextToken);
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
			System.err.println("Expected: {'program'} Got: " + NextToken);
			return false; 
		}
	}
	
	private boolean D()
	{
		if(NextToken.equals("begin"))
		{
			return true;
		}
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
				System.err.println("Expected {':'} Got: " + NextToken);
				return false;
			}
		}
		else
		{
			return false;
		}
		
	}

	private boolean D1()
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
		else if(NextToken.equals("integer"))
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
			System.err.println("Expected{'integer'} Got: " + NextToken);
			return false;
		}
		
	}

	private boolean AR()
	{
		if(NextToken.equals("array"))
		{
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
			System.err.println("Expected: {'array'} Got: " + NextToken);
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
					return true;
				}
				else
				{
					System.err.println("Expected: {')'} Got: " + NextToken);
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
			System.err.println("Expected: { '(' 'id' 'begin' ',' ':' ')' } Got: " + NextToken);
			return false;
		}
	}

	private boolean IL()
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
		else if(NextToken.equals(","))
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
		else
		{
			System.err.println("Expected: {':' ')' ','} Got: " + NextToken);
			return false;
		}
	}

	private boolean ID()
	{
		if(lex.isIdentifier(NextToken))
		{
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
			System.err.println("InvalidIdentifierException: Expected: {'id'} Got: " + NextToken);
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
					return true;
				}
				else
				{
					System.err.println("Expected: {')'} Got: " + NextToken);
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
			System.err.println("Expected: {'(' 'id' ',' ':' ';' ')'} Got: " + NextToken);
			return false;
		}
	}

	private boolean SB()
	{
		if(lex.isConstant(NextToken) || lex.isIdentifier(NextToken))
		{
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
			System.err.println("Expected: {'cons' 'id' ',' ')'} Got: " + NextToken);
			return false;
		}
		
	}

	private boolean SB1()
	{
		if(lex.isConstant(NextToken) || lex.isIdentifier(NextToken))
		{
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
			System.err.println("Expected: {'cons' 'id'} Got: " + NextToken);
			return false;
		}
	}
	
	private boolean S()
	{
		if(NextToken.equals("do"))
		{
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
					System.err.println("Expected: {')'} Got: " + NextToken);
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
			NextToken = lex.getNextToken();
			if(this.E())
			{
				if(NextToken.equals("to"))
				{
					NextToken = lex.getNextToken();
					if(this.ID())
					{
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
							System.err.println("Expected: {';'} Got: " + NextToken);
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
					System.err.println("Expected: {'to'} Got: " + NextToken);
					return false;
				}
			}
			else 
			{
				return false;
			}
		}
		else if(NextToken.equals("unless") || NextToken.equals("when") || NextToken.equals("end") || NextToken.equals(";"))
		{
			return true;
		}
		else
		{
			System.err.println("Expected: {'do' '(' 'assign' 'unless' 'when' 'end' ';'} Got: " + NextToken);
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
					System.err.println("Expected: {';'} Got: " + NextToken);
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
			System.err.println("Expected: {'unless' 'when'} Got: " + NextToken);
			return false;
		}
	}

	private boolean S2()
	{
		if(NextToken.equals("in") || NextToken.equals("out"))
		{
			NextToken = lex.getNextToken();
			if(NextToken.equals(";"))
			{
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
				System.err.println("Expected: {';'} Got: " + NextToken);
				return false;
			}
		}
		else
		{
			System.err.println("Expected: {'in' 'out'} Got: " + NextToken);
			return false;
		}
	}

	private boolean S3()
	{
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
		else if(NextToken.equals("else"))
		{
			NextToken = lex.getNextToken();
			if(this.S())
			{
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
					System.err.println("Expected: {';'} Got: " + NextToken);
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
			System.err.println("Expected: {';' 'else'} Got: " + NextToken);
			return false;
		}
	}

	private boolean E()
	{
		if(lex.isIdentifier(NextToken) || lex.isConstant(NextToken))
		{
			return true;
		}
		else if(NextToken.equals("+") || NextToken.equals("-") || NextToken.equals("*") || NextToken.equals("/"))
		{
			if(this.E())
			{
				if(this.E())
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
			System.err.println("Expected: {'id' 'cons' '+' '-' '*' '/'} Got: " + NextToken);
			return false;
		}
	}

	private boolean C()
	{
		if(NextToken.equals("<") || NextToken.equals(">") || NextToken.equals("="))
		{
			NextToken = lex.getNextToken();
			if(this.E())
			{
				if(this.E())
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
		else if(NextToken.equals("and") || NextToken.equals("or"))
		{
			NextToken = lex.getNextToken();
			if(this.C())
			{
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
				return false;
			}
		}
		else if(NextToken.equals("not"))
		{
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
			System.err.println("Expected: {'<' '>' '=' 'and' 'or' 'not'} Got: " + NextToken);
			return false;
		}
	}





}