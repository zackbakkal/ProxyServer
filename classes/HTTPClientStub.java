//package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
* title: HTTPClientStub.java
* implements: ClientInterface
* to compile: javac -cp commons-net-3.6.jar; HTTPClientStub.java
* description: This class helps connect to an HTTP server and
* 				retrieve a resource.
*
* @date: July 05, 2018
* @author Zakaria Bakkal
* @version 3.0
*/
public class HTTPClientStub implements ClientInterface{

	// The local proxy server address
	private String localserver;
	// The connection is made on this port 
	private int port;
	// The remote host address
	private String remoteHost;
	// The connection to the server socket
	private Socket connection;
	// The resource path
	private String resource;
	// Used to write to the server
	private PrintWriter out;
	// Used to read from the server
	private BufferedReader in;
	// Holds the http request
	private String httpRequest;
	// Holds the server response
	private StringBuilder response;
	// http response code
	private int code;
	
	/**
	 * Instantiates a new HTTP client stub, by initializing the remoteHost,
	 * and the resource instance variables 
	 *
	 * @param remoteHost the remote host
	 * @param resource the resource
	 */
	public HTTPClientStub(String remoteHost, String resource) {
		this.remoteHost = remoteHost;
		this.resource = resource;
		try {
			InetAddress localhost = InetAddress.getLocalHost();
			localserver = (localhost.getHostAddress()).trim();
		} catch(UnknownHostException e) {
			System.out.println("HTTPClientStub: Unkown host");
		}
	}
	
	/* Connects to the remote server, sends the client request,
	 * read the server response and closes the connection.
	 * Calls:
	 * 		connect()
	 *		sendRequest()
	 *		readResponse()
	 *		closeConnection()
	 */
	public void start() {
		connect();
		if(connection != null) {
			sendRequest();
			readResponse();
			closeConnection();
		}
	}
	
	/**
	 * Connect to the server
	 * Called by: start()
	 */
	private void connect() {
		// check if the server is local
		// either running on 127.0.0.1 or the local IP address
		// and assign the appropriate port number
		if(remoteHost.equals("localhost") 
				|| remoteHost.equalsIgnoreCase(localserver)) {
			port = 800;
		} else {
			port = 80;
		}
		// establish the connection
		try {
			InetAddress address = InetAddress.getByName(remoteHost);
			connection = new Socket(address, port);
			// setup input and output streams to the server
			setupStreams();
		} catch (IOException e) {
			System.out.println("HTTPClientStub: Could't connect to server");
			response = new StringBuilder();
			response.append("Couldn't connect to server\r\n");
		}
		
		
	}
	
	/**
	 * Setup input and output streams to the remote server
	 * Called by: start()
	 */
	private void setupStreams() {
		try {
			out = new PrintWriter(connection.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println("HTTPClientStub: Unable to setup output stream");
		}
		
		try {
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (IOException e) {
			System.out.println("HTTPClientStub: Unable to setup input stream");
		}
	}
	
	/**
	 * Send the client request to the server
	 * Called by: start()
	 */
	private void sendRequest() {
		httpRequest = "GET " + resource + " HTTP/1.1\r\n\r\n";
		System.out.println(httpRequest);
		out.println(httpRequest);
		out.flush();
	}
	
	/**
	 * Read the server response.
	 * Calls: readHeader()
	 * Called by: start()
	 */
	private void readResponse() {
		String line;
		response = new StringBuilder();
		// read the header from the server
		readHeader();
		// depending on the response we read the body
		if(code == 200) {
			try {
				while((line = in.readLine()) != null) {
					// check for end of response
					if(line.equals("0")) break;
					response.append(line);
					response.append("\n");
				}
				
				in.close();
				
			} catch (IOException e) {
				System.out.println("HTTPClientStub: Unable to read response");
			}
		}
		
		// the page requested not found
		if(code == 404) {
			response.append("<html>\r\n")
            		.append("<head><title>File Not Found</title>\r\n")
            		.append("<head>\r\n")
            		.append("<body>")
            		.append("<h1>HTTP Error 404: File Not Found</h1>\r\n")
            		.append("</body></html>\r\n");
		}
	}
	
	/*
	 * Reads the http response header from the remote server.
	 * */
	private void readHeader() {
		String line;
		
		try {
			// used to check the server response code
			String firstLine = in.readLine();
			response.append(firstLine);
			response.append("\n");
			// now we read the rest of the header
			while((line = in.readLine()) != null) {
				// check for end of response
				if(line.equals("")) break;
				response.append(line);
				response.append("\n");
			}
			
			response.append("\n");
			
			// retrieve the response code now
			String[] pieces = firstLine.split(" ");
			try {
				code = Integer.parseInt(pieces[1]);
			} catch(NumberFormatException e) {
				System.out.println("HTTPClientStub: Invalid reponse code");
			}
			
			// if the code is not a success we close the input stream
			if(code == 404) {
				in.close();
			}
			
		} catch (IOException e) {
			System.out.println("HTTPClientStub: Unable to read response");
		}
	}
	
	/**
	 * Close connection to the remote server.
	 * Called by: start()
	 */
	private void closeConnection() {
		if(connection != null) {
			try {
				connection.close();
			} catch(IOException e) {
				System.out.println("HTTPClientStub: Error");
			}
		}
	}
	
	/* 
	 * Return the server response.
	 * 
	 * @return String
	 */
	public String getResponse() {
		return response.toString();
	}

	
}