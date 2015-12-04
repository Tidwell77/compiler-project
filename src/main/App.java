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
		Boolean success;
		try 
		{
			in = new BufferedReader(new InputStreamReader(new FileInputStream("./program.txt"), "UTF-8"), bufferSize);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		SyntaxAnalyzer compiler = new SyntaxAnalyzer(in);
		
		if(compiler.P())
			success = true;
		else
			success = false;
		
		if(success)
		{
			System.out.println("\n\n");
			System.out.println("|===============================================================================================================================|");
			System.out.println("|----------------------------------------------------Compiled Successfully------------------------------------------------------|");
			System.out.println("|===============================================================================================================================|");
			System.out.println("\n\n\n");
			
			compiler.getLex().printSymbolTable();
			System.out.println("\n\n\n");
			compiler.printQuadTable();
		}
		else
		{
			System.out.println("\n");
			System.out.println("|===============================================================================================================================|");
			System.out.println("|-----------------------------------------------------Compilation Failed--------------------------------------------------------|");
			System.out.println("|===============================================================================================================================|");
		}
		
	}

}

