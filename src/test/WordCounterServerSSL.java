package test;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;

public class WordCounterServerSSL{
    public static void main(String[] args) {
    	// Currently doesn't work
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, new SecureRandom());

            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(9999);

            System.out.println("Server started. Waiting for client...");

            while (true) {
                SSLSocket clientSocket = (SSLSocket) sslServerSocket.accept();
                System.out.println("Client connected.");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private final SSLSocket clientSocket;

        public ClientHandler(SSLSocket socket) {
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



