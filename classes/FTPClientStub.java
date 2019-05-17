//package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
* title: FTPClientStub.java
* implements: ClientInterface
* to compile: javac -cp commons-net-3.6.jar; FTPClientStub.java
* description: This class helps connect to an FTP server and
* 				retrieve a resource.
*
* @date: July 05, 2018
* @author Zakaria Bakkal
* @version 2.0
*/
public class FTPClientStub implements ClientInterface{

	// this code represents FTP connection failure
	private final int FAILURE = 530;
	// the default port of an FTP server
	private final int PORT = 21;
		
	// used for FTP anonymous connections
	private final String ANONYMOUS = "anonymous";
	// holds the remote FTP server address
	private String remoteServer;
	// holds the resource name
	private String resource;
	// used to download the resource file
	private File temp;
	// holds the user name
	private String user;
	// holds the user password
	private String pass;
	// represents the client connection
	private FTPClient ftpClient;
	// used to write the resource content to the temp file
	private OutputStream output;
	// holds the remote file content
	private StringBuilder response;
	// connection flag
	private boolean connected;
	
	/**
	 * Anonymous FTP connection.
	 * Instantiates a new FTP client stub, by initializing the remoteServer,
	 * and the resource file name to the value of the arguments passed.
	 * Creates the temp file and set USER and PASS instance variable to ANONYMOUS.
	 *
	 * @param remoteServer the remote FTP server address
	 * @param resource the resource file name
	 */
	public FTPClientStub(String remoteServer, String resource) {
		this.remoteServer = remoteServer;
		this.resource = resource;
		this.temp = new File("temp");
		this.user = ANONYMOUS;
		this.pass = ANONYMOUS;
	}
	
	/**
	 * Non-anonymous FTP connection.
	 * Instantiates a new FTP client stub, by initializing the remoteServer,
	 * the resource file name, USER and PASS instance variable to the value 
	 * of the arguments passed. And Creates the temp file. if the USER and PASS
	 * are null ANONYMOUS connection is used.
	 *
	 * @param remoteServer the remote FTP server address
	 * @param resource the resource file name
	 * @param user the user name
	 * @param pass the user password
	 */
	public FTPClientStub(String remoteServer, String resource, String user, String pass) {
		this.remoteServer = remoteServer;
		this.resource = resource;
		this.temp = new File("temp");
		if(user != null) {
			this.user = user;
			this.pass = pass;
		} else {
			this.user = ANONYMOUS;
			this.pass = ANONYMOUS;
		}
	}
	
	/* 
	 * Connects to the FTP server, sets up the streams, sends request
	 * and read the response, then disconnects.
	 * calls:
	 * 		connect()
	 *		setupStreams()
	 *		sendRequest()
	 *		readResponse()
	 *		disconnect()
	 */
	public void start() {
		connect();
		if(connected) {
			setupStreams();
			sendRequest();
			readResponse();
			disconnect();
			temp.delete();
		}
	}
	
	/**
	 * Connects to the FTP server
	 * Called by: start()
	 */
	private void connect() {
		ftpClient = new FTPClient();
		try {
			// start a connection with the FTP server on port 21
			ftpClient.connect(remoteServer, PORT);
			// provide user name and user password
			ftpClient.login(user, pass);
			// check if the connection has succeeded
			int replyCode = ftpClient.getReplyCode();
			if(replyCode == FAILURE) {
				System.out.println("FTPClientStub: Invalid Username/Password");
				response = new StringBuilder();
				response.append("Invalid Username/Password\r\n");
			} else {
				connected = true;
				// Set the current data connection mode to PASSIVE_LOCAL_DATA_CONNECTION_MODE
				// This method causes a PASV or EPSV command to be issued to the server before
				// the opening of every data connection, telling the server to open a data port 
				// to which the client will connect to conduct data transfers
				ftpClient.enterLocalPassiveMode();
				// set the transfer mode of data from server to client to binay
				ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			}
		} catch (IOException e) {
			System.out.println("FTPClientStub: Could't connect to server");
			response = new StringBuilder();
			response.append("Couldn't connect to server\r\n");
		}
	}
	
	/**
	 * Setup the output stream that writes to the temp file.
	 * Called by: start()
	 */
	private void setupStreams() {
		try {
			output = new BufferedOutputStream(new FileOutputStream(temp));
		} catch(FileNotFoundException e) {
			System.out.println("FTPClientStub: Could not find file");
		}
		
	}
	
	/**
	 * Send an FTP request to retrieve the resource file and store it
	 * in the resource instance variable.
	 * Called by: start()
	 */
	private void sendRequest() {
		try {
			ftpClient.retrieveFile(resource, output);
			output.close();
		} catch(IOException e) {
			System.out.println("FTPClientStub: Could not retrieve file");
		}
	}
	
	/**
	 * Read the FTP server response and store it in the response
	 * instance variable.
	 * Called by: start()
	 */
	private void readResponse() {
		try(BufferedReader reader = new BufferedReader(new FileReader(temp))) {
			response = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null) {
				response.append(line)
						.append("\n");
			}
		} catch(IOException e) {
			System.out.println("FTPClientStub: Could read file");
		}
	}
	
	/**
	 * Disconnect from the FTP server
	 * Called by: start()
	 */
	private void disconnect() {
		try {
			if(ftpClient.isConnected()) {
				ftpClient.logout();
				ftpClient.disconnect();
			}
		} catch(IOException e) {
			System.out.println("FTPClientStub: Error disconnecting from server");
		}
	}

	/* 
	 * returns the resource file content.
	 * 
	 * @return String
	 */
	public String getResponse() {
		return response.toString();
	}
	
}
