package Word_Counter;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class WordCounterClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 9999);
            System.out.println("Connected to server.");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

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
            socket.close();
            System.out.println("Disconnected from server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
