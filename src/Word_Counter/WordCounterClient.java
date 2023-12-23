/**
 * Client class of the Word Counter application
 * 
 * This is the class where the once the client has established a connection to Server can provide its sample text
 * After which it will await a response from the server for how many words are in the sample text * 
 * 
 * @author Mansoor Shah
 */
package Word_Counter;

import java.io.*;
import java.util.Scanner;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class WordCounterClient {
	
	static int portNumber = 17777;
	static final String TRUSTSTOREPATH = "C:\\Users\\asus\\eclipse-workspace\\Word_Counter\\src\\keys\\client.truststore";
	static final String TRUSTSTOREPASS = "tpJ585GQ";
	
	/**
	 * The main method where the client connects to the server.
	 * Then client performs the required task of providing sample text 
	 * for server to count the words of.
	 * 
	 * @param args
	 * @exception IOException
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
            
            System.out.println("Connected to server.");

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

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
            System.out.println("Disconnected from server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
