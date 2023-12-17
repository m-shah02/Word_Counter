package test;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.util.Scanner;

public class WordCounterClientSSL{
    public static void main(String[] args) {
    	// Currently doesn't work
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, new SecureRandom());

            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket("localhost", 9999);

            System.out.println("Connected to server.");

            BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            PrintWriter out = new PrintWriter(sslSocket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);
            String userInput;
            do {
                System.out.print("Enter a message (type 'exit' to quit): ");
                userInput = scanner.nextLine();
                out.println(userInput);

                String response = in.readLine();
                System.out.println("Server response: " + response);
            } while (!userInput.equalsIgnoreCase("exit"));

            scanner.close();
            in.close();
            out.close();
            sslSocket.close();
            System.out.println("Disconnected from server.");
        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }
}


