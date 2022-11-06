
import java.io.*;
import java.util.Scanner;

import client.*;
import common.*;

public class ServerConsole implements ChatIF
{
	EchoServer server;
	Scanner fromServerConsole;
	
	final public static int DEFAULT_PORT = 5555;
	
	
	public ServerConsole(int port) 
	{
		server= new EchoServer(port);
	        
	    fromServerConsole = new Scanner(System.in);  
	   
	}
	
	
	@Override
	public void display(String message) {
		System.out.println(message);
		
	}
	
	
	
	public void accept() 
	{
	  try
	  {

	    String message;

	    while (true) 
	    {
	      message = fromServerConsole.nextLine();
	      server.handleMessageFromServerUI(message);
	    }
	  } 
	  catch (Exception ex) 
	  {
	    System.out.println("Unexpected error while reading from console!");
	  }
	}
	
	
	public static void main(String[] args) 
	  {
		int port = 0;


		try
	    {
	      port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	      port = DEFAULT_PORT; //Set port to 5555
	    }
	    ServerConsole chat= new ServerConsole(port);
	    chat.accept();  //Wait for console data
	  }
}
