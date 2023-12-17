package Word_Counter;

import java.io.*;
import java.net.*;

public class WordCounterServer {
    public static void main(String[] args) {
        try {
            try (ServerSocket serverSocket = new ServerSocket(9999)) {
				System.out.println("Server started. Waiting for client...");

				while (true) {
				    Socket clientSocket = serverSocket.accept();
				    System.out.println("Client connected.");

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
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received message from client: " + inputLine);
                    if (inputLine.equalsIgnoreCase("exit")) {
                        break;
                    }
                    int wordCount = countWords(inputLine);
                    out.println("Word count: " + wordCount);
                }

                in.close();
                out.close();
                clientSocket.close();
                System.out.println("Client disconnected.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private int countWords(String message) {
            String[] words = message.trim().split("\\s+");
            return words.length;
        }
    }
}
