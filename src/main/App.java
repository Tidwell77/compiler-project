package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import services.LexicalAnalyzer;
import services.SyntaxAnalyzer;

public class App {

	public static void main(String[] args) throws UnsupportedEncodingException 
	{
		int bufferSize = 10240; // 10KB.
		BufferedReader in = null;
		//InputStreamReader in = null;
		try 
		{
			//in = new InputStreamReader(new FileInputStream("./program.txt"));
			in = new BufferedReader(new InputStreamReader(new FileInputStream("./program.txt"), "UTF-8"), bufferSize);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		SyntaxAnalyzer compiler = new SyntaxAnalyzer(in);
		
		if(compiler.P())
			System.out.println("Compiled Successfully");
		else
			System.out.println("You Got BUGS!!!!!");
		
		
//		LexicalAnalyzer lex = new LexicalAnalyzer(in);
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());	
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());	
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());	
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		System.out.println(lex.getNextToken());
//		

	}

}
