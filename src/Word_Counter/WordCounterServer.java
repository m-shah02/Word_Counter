package Word_Counter;

import java.io.*;
import java.net.*;

public class WordCounterServer {
    public static void main(String[] args) {
        try {
            try (ServerSocket serverSocket = new ServerSocket(9999)) {
            	// Shows up before a client connects
				System.out.println("Server started. Waiting for client...");

				while (true) {
				    Socket clientSocket = serverSocket.accept();
				    
				    // Get Address and port for the print statement below
				    String clientAddress = clientSocket.getInetAddress().getHostAddress();
	                int clientPort = clientSocket.getPort();
				    System.out.println("Client (" + clientAddress + ":" + clientPort + ") connected");

				    // Generate threads to allow multiple connections
				    ClientHandler clientHandler = new ClientHandler(clientSocket);
				    Thread thread = new Thread(clientHandler);
				    thread.start();
				}
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
            	// Get client's IP address and port
                String clientAddress = clientSocket.getInetAddress().getHostAddress();
                int clientPort = clientSocket.getPort();
                
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received message from client: " + inputLine);
                    
                    // Ends program for client when client inputs "exit"
                    if (inputLine.equalsIgnoreCase("exit")) {
                        break;
                    }
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

        private int countWords(String message) {
        	// Gets message trims ending whitespaces and then counts all the remaining white spaces for word count
            String[] words = message.trim().split("\\s+");
            return words.length;
        }
    }
}
