//package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* title: ProxyServer.java
* to compile: javac -cp commons-net-3.6.jar; ProxyServer.java
* to run: java -cp commons-net-3.6.jar;. ProxyServer
* description: A proxy server is a special-purpose HTTP server; it is the core application for this project.
* 				The proxy server has two socket connections. It “listens” for communication from clients
* 				through one and launches requests to specific Web servers through the other.
*
* @date: July 01, 2018
* @author Zakaria Bakkal
* @version 2.0
*/
public class ProxyServer {
	
	// the proxy's default port
	private final int PORT = 8000;
	
	/**
	 * starts the proxy server and accepts connections from clients.
	 * Each client connection is handled by a separate Runnable object.
	 * The Runnable object is of class ConnectionHandler.
	 */
	private void start() {
		// a pool that handles multiple connections
		ExecutorService pool = Executors.newFixedThreadPool(100);
		
		// create an InetAddress object for the proxy server
		InetAddress local = null;
		try {
			local = InetAddress.getByName("localhost");
		} catch(UnknownHostException e) {
			System.out.println("Proxy: Unkown host");
		}
		
		// start accepting connections from clients
		try(ServerSocket proxyServer = new ServerSocket(PORT, 100, local)) {
			System.out.println("Server: " + proxyServer.getInetAddress() 
            						+ "\tPort: " + proxyServer.getLocalPort());
			System.out.println("Accepting Connections...");
			
			while(true) {
				try {
					Socket connection = proxyServer.accept();
					System.out.println();
					System.out.println("Client: " + connection.getInetAddress() 
									+ " Port: " + connection.getPort()
									+ "\tis connected");
					System.out.println();
					pool.submit(new ConnectionHandler(connection));
				} catch(IOException e) {
					System.out.println("Proxy: Unable to accept connection");
				} catch(RuntimeException e) {
					System.out.println("Unexpected error: " + e);
				}
			}
		} catch(IOException e) {
			System.out.println("Proxy: Could not start server: " + e);
		}
		
	}
	
	public static void main(String[] args) {
		
		ProxyServer proxy = new ProxyServer();
		proxy.start();
	}
}
