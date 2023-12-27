package Word_Counter;

import java.io.*;
import java.net.SocketException;
import java.util.Scanner;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Client class of the Word Counter application.
 * 
 * This class represents the client-side functionality of the Word Counter application.
 * It establishes a connection to the server and allows the user to input text, 
 * receiving word count responses from the server. The client uses SSL/TLS for secure 
 * communication with the server.
 * 
 * Upon establishing a connection, the client sends messages to the server for word 
 * counting based on user input. The program continues to receive word count responses 
 * until the user decides to exit by typing 'exit'.
 * 
 * The SSL/TLS configuration for client-side communication is set up by specifying 
 * truststore information, enabling specific SSL protocols, and performing an SSL handshake.
 * 
 * @author Mansoor Shah
 */
public class WordCounterClient {
	
	//Port Number
	static int portNumber = 17777;
	// Directory and password for truststore
	static final String TRUSTSTOREPATH = "src\\keys\\client.truststore";
	static final String TRUSTSTOREPASS = "tpJ585GQ";
	
	/**
	 * Initiates a client-side connection using SSL to communicate with a server.
	 * Establishes a connection to the server using SSLSocket and communicates
	 * by sending messages and receiving word counts for input messages until
	 * the user exits.
	 *
	 * Uses SSL/TLS to connect securely to the server. The client-side SSL
	 * configuration is set up by specifying truststore information, enabling
	 * specific SSL protocols, and performing an SSL handshake.
	 *
	 * @param args The command-line arguments passed to the program.
	 */
    public static void main(String[] args) {
        try {
            // For SSL debugging purposes:
            //System.setProperty("javax.net.debug", "ssl");

            // Tells the client where the truststore and certificate is and sets it to be used for the client side ssl
            System.setProperty("javax.net.ssl.trustStore", TRUSTSTOREPATH);
            System.setProperty("javax.net.ssl.trustStorePassword", TRUSTSTOREPASS);

            // Implementing Client-side SSL
            SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket s = (SSLSocket) ssf.createSocket("localhost", portNumber);
            
            s.setEnabledProtocols(new String[] {"TLSv1.3", "TLSv1.2"});
            s.startHandshake(); // the SSL handshake
            
            // Sends message to say there's been a successful connection to the server
            System.out.println("Connected to server from: " + s.getLocalAddress() + ":" + s.getLocalPort());

            // Used to create buffered reader that reads data from server
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            // Used to create a PrintWriter that writes data to server
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

            // For the inputted message to scan
            Scanner scanner = new Scanner(System.in);
            String userInput;
            do {
            	// Prompts user to input message for Client to count
                System.out.print("Enter a message (type 'exit' to quit): ");
                userInput = scanner.nextLine();
                out.println(userInput);

                // Shows the client the word count 
                String response = in.readLine();
                System.out.println("Server response: " + response);
            } while (!userInput.equalsIgnoreCase("exit"));

            // Closes program here...
            scanner.close();
            in.close();
            out.close();
            s.close();
        } 
        catch(SocketException s){}
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
        	System.out.println("Disconnected from server.");
        }
    }
}
