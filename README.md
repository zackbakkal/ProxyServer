# ProxyServer

Project Classes:
ProxyServer: This is the proxy server that handles the client requests. Accepts connection on port 8000.
ConnectionHandler: This class helps the proxy server determine the client request type (HTTP or FTP) and instantiate the right object to handle the client request.
HTTPClientStub: This class handles HTTP client requests. Connects to HTTP servers on port 80, but if the local machine is the host then it connects on port 800.
FTPClientStub: This class handles FTP client requests. Connects to FTP servers on port 21.
Client: This class represents the client who initiates the requests.
ClientInterface: This interface is used to hide the Clients Implementation. All client classes will only have a start method available. Connects to the proxy on port 8000.
Server: This class is the basic web server that deals with requests from a client. Accepts connection on port 800.





Compiling Programs:
javac -cp commons-net-3.6.jar; ProxyServer.java 
javac -cp commons-net-3.6.jar; ConnectionHandler.java 
javac -cp commons-net-3.6.jar; HTTPClientStub.java 
javac -cp commons-net-3.6.jar; FTPClientStub.java 
javac Client.java 
javac ClientInterface.java 
javac Server.java
Running the programs:
First run the Server:
java Server ipAddress root
Then, run the Proxy:
javac -cp commons-net-3.6.jar;. ProxyServer
Then, run the Client:
Note: url must start with an http or ftp, otherwise you will get an error “Invalid url”
for HTTP:
java Client http://hostname/resource
java Client http://hostname/
java Client http://hostname
Note:
If the resource is not provided it is considered “/” or “index.html”
for FTP:
		java Client ftp://hostname/resource username password
		java Client ftp://hostname/resource anonymous
		java Client ftp://hostname/resource
		Note:
If the username and password are not provided they are considered “anonymous”
		If only the username is provided it must be anonymous
