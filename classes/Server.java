//package client;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.concurrent.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * title: Server.java
 * compile: javac Server.java
 * run: java Server serverAddress rootDirectory
 * description: Provides multiple files, it is a working HTTP server.
 * 				This server is able to display HTML web pages as well
 * 				as embedded multimedia content such as .pdf files or
 * 				images (.jpg or .png). This server can be accessed by
 * 				any web browser.
 *
 * @date: July 10, 2018
 * @author Zakaria Bakkal
 * @version 3.0
 */
class Server {

    private String serverAddress;	// the host server, must be modified to start the local server
    private String root;	// the root where the server resides, must be modified as needed
    private String homePage = "/index.html";	// the home page of the HTTP server, must be modified as needed
    private byte[] content; 	// content of the server response message
    private int contentLength;	// length of the content in the server response message
    private String contentType;	// content-type of the server response message
    private byte[] header;	// headers
    private File file;	// the file which is requested and to be sent
    private String fileName;	// used to manipulate the requested file to be sent
    private int port = 800;	// default port
    private String encoding = "UTF-8";	// default encoding
    private int responseCode;	// server response code
    private String method;	// HTTP request message method

    /**
     * Instantiates a new server, by initializing serverAdress and root
     * instance variables to the value of the arguments passed.
     *
     * @param serverAddress the server address
     * @param root the root directory where the files reside
     */
    public Server(String serverAddress, String root) {
    	this.serverAddress = serverAddress;
    	this.root = root;
    }

    /**
     * Creates a pool of threads that takes care of each connections request.
     * Then, it starts accepting connections. , then submits the connection
     * to the ConnectionHandler object.
     * 
     */
    public void start() {
		ExecutorService pool = Executors.newFixedThreadPool(100);
		// holds the InetAddress of the server
		InetAddress local = null;
    	try {
            local = InetAddress.getByName(serverAddress);
        } catch (UnknownHostException ex) {
            System.out.println("Server: Unknown Host \"" + serverAddress + "\"");
        }
		
    	// start accepting connections
        try (ServerSocket server = new ServerSocket(this.port, 100, local)) {
            System.out.println("Server: " + server.getInetAddress() 
                    + "\tPort: " + server.getLocalPort());
            System.out.println("Accepting Connections...");
            
            while(true) {
				try {
					Socket connection = server.accept();
		            System.out.println();
		            System.out.println("Accepted connection...");
		            System.out.println("Client: " + connection.getInetAddress()
		                      + "\tPort: " + connection.getPort());
		            pool.submit(new Handler(connection) {});
		        } catch (IOException ex) {
		            System.out.println("Exception accepting connection" + ex);
				} catch (RuntimeException ex) {
					System.out.println("Unexpected error" + ex);
				}
            }
		} catch (IOException ex) {
			System.out.println("Could not start server" + ex);
		}
    }

    /*
     * handles individual client connections made to the server.
     * 
     * */
    private class Handler implements Callable<Void> {
        private final Socket connection;

        /**
         * Instantiates a new handler, by initializing the instance variable connection.
         *
         * @param connection the client connection
         */
        public Handler(Socket connection) {
        	this.connection = connection;
        }

        /*
         * Retrieves requested files from the root directory and sends them back
         * to the client, also constructs messages such as request messages and
         * error messages and sends them to the client.
         * */
        @Override
        public Void call() throws IOException {
            try {
            	// set up the input and output streams to communicate with the client
                OutputStream out = new BufferedOutputStream(connection.getOutputStream());
                InputStream in = new BufferedInputStream(connection.getInputStream());
                
                // read the first line only; that's all we need
                StringBuilder request = new StringBuilder(80);
                int c;
                while(true) {
                    c = in.read();
                    if(c == '\r' || c == '\n' || c == -1) break;
                    request.append((char) c);
                }
                // if this is HTTP/1.0 or later send a MIME header
                if(request.toString().indexOf("HTTP/") != -1) {
                    // get the request message method
                    method = request.toString().substring(0, request.toString().indexOf(" "));
                    // if the method is "GET" we proceed
                    if(method.equals("GET")) {
                        // set server response code to 200
                        responseCode = 200;
                        // find where the requested file starts
                        int index = request.toString().toLowerCase().indexOf("/");
                        // holds the index of the last character in the first line of the request message
                        int endOfLine;
                        // if the requested file is not specified we return the home page file "index.html"
                        if(request.toString().charAt(index + 1) == ' ') {
                            file = new File(root + homePage);
                        } else {	// otherwise we extract the file from the request message
                            endOfLine = request.toString().toLowerCase().indexOf(' ', index);
                            // construct a file object from the root and the requested file name
                            file = new File(root + request.toString().substring(index, endOfLine));
                        }

                        // if the file requested exists
                        if(file.canRead()) {
                            // get the requested file name as a string
                            fileName = request.toString().substring(request.toString().indexOf("/"), request.toString().length());
                            // get all the file read to content
                            content = Files.readAllBytes(file.toPath());
                            // find out the file content type, in case the file is css
                            // we set content type the the corresponding type otherwise we let java figure it out
                            contentType = URLConnection.getFileNameMap().getContentTypeFor(file.toString());
                            if(contentType == null) {
                                if(file.toString().endsWith(".css")) {
                                    contentType = "text/css";
                                }
                                if(file.toString().endsWith(".js")) {
                                    contentType = "application/javascript";
                                }
                            }


                            // calculate the file content length
                            contentLength = content.length;
                            // find the encoding of the request message desired
                            index = request.toString().toLowerCase().indexOf("charset");
                            // if the encoding is provided in the request message we exctract it from it
                            // otherwise the encoding used is the default one
                            if(index != -1) {
                                endOfLine = request.toString().indexOf('\r', index);
                                encoding = request.substring(index + 8, endOfLine);
                            }

                            // construct the response header
                            String h = "HTTP/1.0 200 OK\r\n"
                                    + "Server: OneFile 2.0\r\n"
                                    + "Content-length: " + contentLength + "\r\n"
                                    + "Content-type: " + contentType + "; charset= " 
                                    + encoding + "\r\n\r\n";
                            // transform it to a byte[] header
                            header = h.getBytes(Charset.forName("US-ASCII"));
                            // in this case the file is not found and the response is a 404 file not found
                        } else {
                            fileName = request.toString()
                                    .substring(request.toString().indexOf("/")
                                            , request.toString().length());
                            // construct an html response to the client
                            String fileNotFound = new StringBuilder("<html>\r\n")
                                    .append("<head><title>File Not Found</title>\r\n")
                                    .append("<head>\r\n")
                                    .append("<body>")
                                    .append("<h1>HTTP Error 404: File Not Found</h1>\r\n")
                                    .append("</body></html>\r\n").toString();
                            content = fileNotFound.getBytes(Charset.forName("US-ASCII"));
                            contentLength = content.length;
                            String h = "HTTP/1.0 404 File Not Found\r\n"
                                    + "Server: " + "HTTPServer" + "\r\n"
                                    + "Content-length:" + contentLength + "\r\n"
                                    + "Content-type:"  + "text/html" + "\r\n"
                                    + "charset=utf-8" + "\r\n\r\n";
                            header = h.getBytes(Charset.forName("US-ASCII"));
                            responseCode = 404;
                        }

                        out.write(header);
                        out.write(content);
                        out.write(0);
                        out.flush();

                    } else {
                        // set server response code to 501
                        responseCode = 501;
                        String h = "HTTP/1.0 501 Not Implemented\r\n"
                                    + "Server: " + "HTTPServer" + "\r\n"
                                    + "Content-length:" + 0 + "\r\n"
                                    + "Content-type:"  + "text/html" + "\r\n"
                                    + "charset=utf-8" + "\r\n\r\n";
                        header = h.getBytes(Charset.forName("US-ASCII"));
                    }

                    // after the server starts and accepts a valid connection and the connection has a valid request message
                    // we start setting the info needed for the INFO logger

                    // store the current date and time
                    String date = new SimpleDateFormat("dd/MM/yyy:HH:mm:ss -z").format(new Date());
                    // get the remote host address
                    String remoteHost = connection.getRemoteSocketAddress().toString();
                    // extract the desired remote address format
                    remoteHost = remoteHost.substring(1, remoteHost.indexOf(":"));
                    // this StringBuilder holds the whole message
                    StringBuilder loggerInfo = new StringBuilder();
                    loggerInfo.append(remoteHost)
                    		  .append(" - - [")
                    		  .append(date)
                    		  .append("] ")
                    		  .append("\"")
                    		  .append(method)
                    		  .append(" ")
                    		  .append(fileName)
                    		  .append("\" ")
                    		  .append(responseCode)
                    		  .append(" ")
                    		  .append(contentLength)
                    		  .append('\n');
                }
                    
            } catch (IOException ex) {
                System.out.println("Error writing to client" + ex);
            } finally {
                connection.close();
            }	
            
            return null;
            
        }
    }

	public static void main(String[] args) {

            String serverAddress = "";
            String root = "";
            if(args.length == 2) {
                serverAddress = args[0];
                root = args[1];
            } else {
		System.out.println("Usage: java Server host root");
            }

            Server server = new Server(serverAddress, root);
            server.start();
        }
}