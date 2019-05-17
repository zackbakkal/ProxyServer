//package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;

/**
* title: ConnectionHandler.java
* implements: Runnable
* to compile: javac -cp commons-net-3.6.jar; ConnectionHandler.java
* description: This class handles connections that made a connection with the proxy server.
* 				It handles HTTP and FTP client requests. Depending on the request made,
* 				a client stub is object is created and handed the requested resource.
*
* @date: July 03, 2018
* @author Zakaria Bakkal
* @version 3.0
*/
public class ConnectionHandler implements Runnable {
	
	// client request types
	private final int FTP = 1;
	private final int HTTP = 2;
	private final int HTTPS = 3;

	// client connection socket
	private Socket connection;
	// the remote server that the client wishes to reach
	private String remoteServer;
	// used to read client request
	private BufferedReader clientInput;
	// used to respond to client
	private PrintWriter clientOutput;
	// holds the server response, header + data
	private String serverResponse;
	// holds the resource the client wishes to get
	private String resource;
	// the protocol the client request is using
	private int protocol;
	// holds the client request
	private String request;
	// holds the user name of the client for an FTP server
	private String user;
	// holds the password of the client for an FTP server
	private String pass;
	// the URL of the remote server
	private URL url;
	// used when an HTTP request is made
	private HTTPClientStub httpClientStub;
	// used when an FTP request is made
	private FTPClientStub ftpClientStub;

	/**
	 * Instantiates a new ConnectionHandler, by initializing the instance variable connection.
	 *
	 * @param connection the client connection
	 */
	public ConnectionHandler(Socket connection) {
		this.connection = connection;
	}
	
	/* 
	 * Reads the client request, connects to the remote server, reads the
	 * server response and sends it to the client, then closes the client connection.
	 * 
	 * calls: 
	 * 		setupClientStreams()
	 *		readRequest()
	 *		breakDownURL()
	 *		connectToServer()
	 *		readResponse()
	 *		sendResponseToClient()
	 *		closeClientConnection()
	 */
	@Override
	public void run() {
		
		try {
			setupClientStreams();
			readRequest();
			breakDownURL();
			connectToServer();
			readResponse();
			sendResponseToClient();
			closeClientConnection();
		} catch(IOException e) {
			System.out.println("Error");
		}
	}
	
	/**
	 * Setup the input and output streams used to communicate with the client.
	 * Called by: run()
	 */
	private void setupClientStreams() {
		try {
			clientOutput = new PrintWriter(connection.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println("ConnectionHandler: Unable to setup output stream");
		}
		
		try {
			clientInput = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));
		} catch (IOException e) {
			System.out.println("ConnectionHandler: Unable to setup input stream");
		}
	}
	
	/**
	 * Read the client request.
	 * Called by: run()
	 */
	private void readRequest() {
		// stores each character read from the client,
		// representing the client request
		StringBuilder line = new StringBuilder();
		
		try {
			int c;
			while(true) {
				c = clientInput.read();
				// check for end of request
				if(c == -1 || c == '\r') break;
				line.append((char) c);
			}
			// store the client request as lower case
			request = line.toString().toLowerCase();
		} catch (IOException e) {
			System.out.println("CoonectionHandler: Unable to read request");
		}
				
		
	}
	
	/**
	 * Break down URL sent by the client and retrieves the URL, protocol used,
	 * remote host, resource, and user name and password.
	 * 
	 * Calls:
	 *		retrieveURL()
	 *		retrieveProtocol()
	 *		retrieveRemoteHost()
	 *		retrieveResource()
	 *		retrieveUserAndPass()
	 *Called by: run()
	 */
	private void breakDownURL() {
		System.out.println("request: " + request);
		retrieveURL();
		System.out.println("url: " + url);
		retrieveProtocol();
		System.out.println("protocol: " + protocol);
		retrieveRemoteHost();
		System.out.println("remotehost: " + remoteServer);
		retrieveResource();
		System.out.println("resource: " + resource);
		if(protocol == FTP) {
			retrieveUserAndPass();
			System.out.println("user/pass: " + user + "/" + pass);
		}
	}
	
	/**
	 * Retrieve URL from the client request.
	 * Called by: breakDownURL()
	 */
	private void retrieveURL() {
		url = null;
		
		// check if the request is of the format: protocol://remotehost/resource	
		if(!request.contains(" ")){	
			try {
				url = new URL(request);
			} catch(IOException e) {
				System.out.println("ConnectionHandler: Invalid URL");
				serverResponse = "Invalid URL\r\n";
			}
		}
		
		// check if the client request is of the format:
		// remotehost/resource USER PASS
		if(request.contains(" ")) {
			// split the line
			String[] pieces = request.split(" ");
			// construct the retrieved url
			try {
				url = new URL(pieces[0]);
			} catch(IOException e) {
				System.out.println("ConnectionHandler: Invalid URL");
				serverResponse = "Invalid URL\r\n";
			}
		}
	}
	
	/**
	 * Retrieve protocol used by the client.
	 * Called by: breakDownURL()
	 */
	private void retrieveProtocol() {
		
		if(url != null) {
			// an FTP request
			if(url.getProtocol().equals("ftp")) {
				protocol = FTP;
			}
			
			// an HTTP, or HTTPs request.
			// if the protocol is absent it is considered as HTTP
			if(url.getProtocol().equals("http") 
					|| url.getProtocol().equals("https")) {
				protocol = HTTP;
			}
		}
	}
	
	/**
	 * Retrieve remote host the client wished to connect to.
	 * Called by: breakDownURL()
	 */
	private void retrieveRemoteHost() {
		if(url != null) {
			remoteServer = url.getHost();
		}
	}
	
	/**
	 * Retrieve resource the client wished to get.
	 * Called by: breakDownURL()
	 */
	private void retrieveResource() {
		if(url != null) {
			resource = url.getPath();
			// check if the client didn't include
			// a specific resource, then we use 
			// the default resource
			if(resource.equals("")) {
				resource = "/";
			}
		}
	}
	
	/**
	 * Retrieve user and pass used to connect to the FTP server
	 * Called by: breakDownURL()
	 */
	private void retrieveUserAndPass() {
		if(url != null) {
			// check if the protocol is indeed FTP
			// and is of the format: protocol://remotehost/resource USER PASS
			if(request.contains(" ")) {
				String[] pieces = request.split(" ");
				user = pieces[1];
				pass = pieces[2];
			} else {	// no user name nor password provided
				user = null;
				pass = null;
			}
		}
	}
	
	/**
	 * Connect to the remote server using an http or an ftp client stub
	 * depending the request type.
	 * Called by: start()
	 */
	private void connectToServer() {
		// use the HTTP client stub
		if(protocol == HTTP) {
			httpClientStub= new HTTPClientStub(remoteServer, resource);
			httpClientStub.start();
		}
		
		// use the FTP client stub
		if(protocol == FTP) {
			ftpClientStub= new FTPClientStub(remoteServer, resource, user, pass);
			ftpClientStub.start();
		}
		
	}
	
	/**
	 * Read response from the remote server.
	 * Called by: start()
	 */
	private void readResponse() {
		// check which protocol was used and read the 
		// appropriate response
		if(httpClientStub != null) {
			serverResponse = httpClientStub.getResponse();
		}
		if(ftpClientStub != null) {
			serverResponse = ftpClientStub.getResponse();
		}
		
		// no connection was ever made, thus exit
		if(httpClientStub == null && ftpClientStub == null) {
			System.out.println("ConnectionHandler: No connection established");
		}
		
	}
	
	/**
	 * Send response to the client.
	 * Called by: start()
	 */
	private void sendResponseToClient() {
		// the /r used to inform the client of end of message
		clientOutput.println(serverResponse + "done");
		clientOutput.flush();
		clientOutput.close();
	}
	
	/**
	 * Close client connection.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void closeClientConnection() throws IOException {
		connection.close();
	}
}
