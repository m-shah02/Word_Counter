package Word_Counter;

import java.io.*;
import java.net.*;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

/**
 * The WordCounterServer class represents a server application for the Word Counter system.
 * 
 * This class initializes a server to handle client connections using SSL/TLS for secure
 * communication. It listens for incoming client connections and creates a separate thread
 * (ClientHandler) for each client to manage individual client-server communication. The server
 * continues to accept client connections indefinitely, handling multiple clients concurrently.
 * 
 * The server-side SSL configuration is implemented by specifying the keystore location
 * and password. It utilizes SSLServerSocketFactory to create a secure server socket for
 * communication with clients over SSL/TLS.
 * 
 * @author Mansoor Shah
 */
public class WordCounterServer {
	
	// Port Number
	static int portNumber = 17777;
	// Directory and password for keystores
	static final String KEYSTOREPATH = "src\\keys\\wc.jks";
	static final String KEYSTOREPASS = "tpJ585GQ";
	
	/**
	 * Main method representing the server's entry point to handle client connections.
	 * 
	 * The main method initializes and starts the server-side application for handling client
	 * connections. It sets up the server to listen for incoming client connections and creates
	 * a new thread (ClientHandler) for each client to manage individual client-server communication.
	 * The server runs indefinitely, continuously accepting new client connections and creating
	 * separate threads to handle them.
	 * 
	 * Server-side SSL is implemented by specifying the keystore location and password for the
	 * SSL configuration. It uses SSLServerSocketFactory to create a secure server socket for
	 * communication with clients over SSL/TLS.
	 * 
	 * Upon accepting a client connection, the server displays a message indicating the client's
	 * IP address and port and initiates a new thread (ClientHandler) to handle communication with
	 * the connected client. Each client is managed in a separate thread, enabling multiple clients
	 * to connect and communicate concurrently.
	 * 
	 * @param args The command-line arguments passed to the program.
	 */
    public static void main(String[] args) {
        try {
            	// Shows up before a client connects
				System.out.println("Server started. Waiting for client...");
				
				//System.setProperty("javax.net.debug", "all");		//for debugging

			    //tell the execution environment where the keystore (and the certificate) is
			    System.setProperty("javax.net.ssl.keyStore", KEYSTOREPATH);
			    System.setProperty("javax.net.ssl.keyStorePassword", KEYSTOREPASS);
			    
			    // Implementing Server-Side SSL
			    SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			    SSLServerSocket ss = (SSLServerSocket) ssf.createServerSocket(portNumber);
			    
			    ss.setEnabledProtocols(new String[]{"TLSv1.3", "TLSv1.2"});
			    
				while (true) {
				    Socket clientSocket = ss.accept();
				    
				    // Get Address and port for the print statement below
				    String clientAddress = clientSocket.getInetAddress().getHostAddress();
	                int clientPort = clientSocket.getPort();
				    System.out.println("Client (" + clientAddress + ":" + clientPort + ") connected");

				    // Generate threads to allow multiple connections
				    ClientHandler clientHandler = new ClientHandler(clientSocket);
				    Thread thread = new Thread(clientHandler);
				    thread.start();
				}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Inner class representing a ClientHandler to manage client-server communication.
     * 
     * The ClientHandler class implements the Runnable interface to handle client requests
     * within a server thread. It manages communication with a specific client by reading
     * incoming messages, processing them to determine word counts, and sending back the calculated
     * word count to the client. The class continues to handle communication until the client sends
     * an "exit" message, at which point it closes the communication streams and disconnects
     * the client from the server.
     *
     * ClientHandler retrieves the client's IP address and port for identification and logs
     * incoming messages along with the respective client details. Upon receiving an "exit" message
     * from the client, it terminates the communication session and closes the associated
     * input/output streams and the client socket.
     */
    static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        
        /**
         * Constructs a ClientHandler object to manage client-server communication.
         * 
         * Initializes a ClientHandler object with the provided Socket representing a
         * connection to a client. This constructor sets up the instance to handle incoming
         * communication from the associated client socket.
         *
         * @param socket The Socket object representing the client connection.
         */
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
        
        /**
         * Overrides the 'run' method of the Runnable interface to handle client-server communication.
         * 
         * This method represents the execution logic for handling client requests within a server thread.
         * It reads incoming messages from the client, processes them to determine word counts,
         * and sends the calculated word count back to the client. The method continues to receive 
         * and process messages until the client sends an "exit" message, at which point it closes
         * the communication streams and disconnects the client from the server.
         *
         * The method retrieves the client's IP address and port for identification and logs 
         * incoming messages with their respective client details. Upon receiving an "exit" message
         * from the client, it terminates the communication session and closes the associated
         * input/output streams and the client socket.
         */
        @Override
        public void run() {
            try {

            	// Get client's IP address and port
                String clientAddress = clientSocket.getInetAddress().getHostAddress();
                int clientPort = clientSocket.getPort();
                
                // Used to create buffered reader that reads data from client
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // Used to create a PrintWriter that writes data to client
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received message from client (" + clientAddress + ":" + clientPort + "): " + inputLine);
                    
                    // Ends program for client when client inputs "exit"
                    if (inputLine.equalsIgnoreCase("exit")) {
                        break;
                    }
                    // Output final result here
                    int wordCount = countWords(inputLine);
                    out.println("The total word count of this message is " + wordCount + (wordCount > 1 ? " words." : " word."));
                }
                
                // Program ends when "exit" is send
                in.close();
                out.close();
                clientSocket.close();
                System.out.println("Client (" + clientAddress + ":" + clientPort + ") disconnected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        /**
         * Counts the number of words in the provided message.
         * 
         * The method takes a string message as input, trims any leading or trailing 
         * whitespaces, and splits the message based on whitespace characters to count 
         * the words present in the message. The word count is determined by the number 
         * of non-empty segments obtained after splitting the message.
         *
         * @param message The message in which words are counted.
         * @return The count of words present in the message.
         */
        private int countWords(String message) {
        	// Gets message trims ending whitespaces and then counts all the remaining white spaces for word count
            String[] words = message.trim().split("\\s+");
            return words.length;
        }
    }
}
