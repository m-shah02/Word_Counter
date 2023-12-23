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
import java.net.*;
import java.util.Scanner;

public class WordCounterClient {
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
            Socket socket = new Socket("localhost", 9999);
            System.out.println("Connected to server.");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

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
            socket.close();
            System.out.println("Disconnected from server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
