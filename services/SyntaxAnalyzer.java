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
			
			if(D())
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
							System.err.println("Expected: {end.} Got: " + NextToken);
							return false;
						}
					}
					else
					{
						System.err.println("Expected: {do, (, assign, unless, when, end, ;} Got: " + NextToken);
						return false;
					}
				
				}
				else
				{
					System.err.println("Expected: {begin} Got: " + NextToken);
					return false;
				}
			}
			else
			{
				System.err.println("Expected: {id, begin} Got: " + NextToken);
				return false;
			}
		}
	}
	

}
