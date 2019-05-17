//package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
* title: Client.java
* implements: ClientInterface
* to compile: javac Client.java
* to run: java Client
* description: The client basically needs access to the function that allows
* 			   the user to input the resource URL that identifies the resource
*              s/he wants to access. However, the client has to access the
*              resource by sending a request to the proxy server, and then the
*              client must receive the resource from the proxy server. No matter
*              what kind of data the client receives, the client prints it on the screen. 
*
* @date: July 05, 2018
* @author Zakaria Bakkal
* @version 4.0
*/
public class Client implements ClientInterface{
	
	// The proxy host server
	private final String HOST_SERVER = "localhost";
	// The port the proxy server accepts connection from
	private final int PORT = 8000;
	// The socket that connects to the proxy
	private Socket socket;
	// Used to read proxy server response 
	private BufferedReader input;
    // Used to write to the proxy server
    private PrintWriter output;
    // The url the client wishes to reach
    private String url;
    // The user name for an FTP server
    private String user;
    // The user password for an FTP server
    private String pass;
    // The proxy server response
    private StringBuilder response;
	
	/**
	 * Anonymous FTP server connection or an HTTP server connection
	 * Instantiates a new client by initializing the url to the value of 
	 * the url argument. And sets the user name and password to null.
	 *
	 * @param url the url
	 */
	public Client(String url){
		this.url = url;
		this.user = null;
		this.pass = null;
	}
	
	/**
	 * FTP connection with provided user name and password
	 * Instantiates a new client, by initializing the url to the value of 
	 * the url argument. And sets the user name and password to the values of
	 * user and pass arguemnts.
	 *
	 * @param url the url the client wished to reach
	 * @param user the user name of the client
	 * @param pass the password of the user name
	 */
	public Client(String url, String user, String pass){
		this.url = url;
		this.user = " " + user;
		this.pass = " " + pass;
	}
	
	/* Connects to the proxy server, sets up streams, send the client request,
	 * resds the proxy response and prints it on the screen.
	 * Calls:
	 * 		connect();
	 *		setupStreams();
	 *		sendRequest();
	 *		readResponse();
	 *		print();
	 */
	public void start() {
		connect();
		setupStreams();
		sendRequest();
		readResponse();
		print();
	}
	
	/**
	 * Connect to the proxy server
	 * Called by: start()
	 */
	public void connect() {
		InetAddress local = null;
		try {
			local = InetAddress.getByName(HOST_SERVER);
			socket = new Socket(local, PORT);
		} catch(IOException e) {
			System.out.println("Client: Couldn't connect to server " + local);
		}
	}
	
	/**
	 * Setup the input and output streams to communicate with the proxy server.
	 * Called by: start()
	 */
	public void setupStreams() {
		try {
			output = new PrintWriter(socket.getOutputStream(), true);
			input = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
	    } catch(IOException e) {
	    	System.out.println("Client: Couldn't setup streams");
	    }
		
	}

	/**
	 * Sends request to the proxy server 
	 * Called by: start()
	 */
	public void sendRequest() {
		// check if the client provided user name and password
		if(user != null) {
			// construct the request with url, user name and password
			String request = url + user + pass;
	        output.println(request);
	        output.flush();
		} else {	// the connection is either an anonymous FTP or HTTP connection
			String request = url;
	        output.println(request);
		}
	}
	
	/**
	 * Read the proxy server response.
	 * Called by: start()
	 */
	public void readResponse() {
		String line;
		response = new StringBuilder();
		try {
			while((line = input.readLine()) != null) {
				// check for end of response
				if(line.equals("done")) break;
				response.append(line);
				response.append("\n");
			}
			
			input.close();
		} catch(IOException e) {
        	System.out.println("Client: Couldn't read response");
        	System.exit(1);
        }
	}
	
	/**
	 * Prints the content of the proxy response received
	 * Called by: start()
	 */
	public void print() {
		System.out.println(response.toString());
	}


	public static void main(String[] args){
		
        String url = null;
        String user = null;
        String pass = null;
        
        // Format used:
        // http://remotehost/resource
        // ftp://remotehost/resource
        // remotehost/resource
        if(args.length == 1) {
        	url = args[0];
        	Client Client = new Client(url);
            Client.start();
        } else if(args.length == 2 && args[0].startsWith("ftp://")) {
        	url = args[0];
        	user = args[1];
        	pass = "anonymous";
        	Client Client = new Client(url, user, pass);
            Client.start();
        }else if(args.length == 3 && args[0].startsWith("ftp://")) { // Format used: ftp://remotehost/resource USER PASS
        	url = args[0];
        	user = args[1];
        	pass = args[2];
        	Client Client = new Client(url, user, pass);
            Client.start();
        } else {	// Format used: http://remotehost/resource
        	System.out.println("Usage:");
        	System.out.println("\tjava Client http[s]://]hostname[/resource]");
        	System.out.println("\tjava Client ftp://hostname/resource [USER PASS]");
        }
	}
}
