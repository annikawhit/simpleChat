// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  ChatIF serverUI;
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
    try 
    {
      listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }

  
  //Instance methods ************************************************
  
  
  public void handleMessageFromServerUI(String message)
  {	
    if(message.startsWith("#")) {
		handleCommand(message);
	}
	else {
		message = "SERVER MSG> " + message;
		this.sendToAllClients(message);
		System.out.println(message);
	}
  }
  
  
  public void handleCommand(String command) {
	  
	  if (command.equals("#quit")) {
		  System.out.println("The server has successfully shut down.");
		  System.exit(0);
		  
	  }
	  else if(command.equals("#stop")) {
		  stopListening();
	  }
	  else if((command.split(" ")[0]).equals("#setport")) {
		  setPort(Integer.parseInt(command.split(" ")[1]));
		  System.out.println("Port has successfully been set to " + String.valueOf((command.split(" ")[1])));
		  
	  }
	  else if(command.equals("#close")) {
		 try {
			 close();
		 }
		 catch (IOException e) {
			 System.out.println("Error: Cannot close the server.");
		 }
	  }
	  else if(command.equals("#start")) {
		  if (!isListening()) {
			  try{
				  listen();
			  }
			  catch (IOException e1){
				  System.out.println("Error: Cannot start the server.");
			  }
		  }
		  else {
			  System.out.println("Error: Cannot start the server. Server is already listening.");
		  }
	  }
	  else if(command.equals("#getport")) {
		  System.out.println(String.valueOf(getPort()));
	  }
	  else {
		  System.out.println("Error: The command that was inputted is invalid.");
	  }
	  
  }
  
  
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client)
  {
	if ((((String.valueOf(msg)).split(" "))[0]).equals("#login")) {
		
		if (client.getInfo("login") == null) {
			client.setInfo("login", ((String.valueOf(msg)).split(" "))[1]);
			System.out.println("Message received: #login " + client.getInfo("login") + " from null.");
			System.out.println(client.getInfo("login") + " has logged on.");
			try {
				client.sendToClient(client.getInfo("login") + " has logged on.");
			}
			catch (IOException e) {
				System.out.println("Error - Unable to send message to client.");
			}
		}
	}
	else {
		System.out.println("Message received: " + msg + " from " + client.getInfo("login"));
	    this.sendToAllClients(client.getInfo("login") + "> " + msg);
	}
    
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
  
  
  
  
  /**
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  @Override
  synchronized protected void clientConnected(ConnectionToClient client) 
  {
	  System.out.println("A new client has connected to the server.");
  }

  /**
   * Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) 
  {
	  System.out.println(client.getInfo("login") + " has disconnected.");
  }
  

  
}
//End of EchoServer class
