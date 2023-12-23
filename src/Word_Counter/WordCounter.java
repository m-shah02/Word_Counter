/**
 * Functional implementation of Word Counter without Client/Server TCP/UDP model attached...
 */
package Word_Counter;

import java.util.Scanner;

public class WordCounter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Enter a sentence or 'exit' to quit: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                running = false;
            } else {
                int wordCount = countWords(input);
                System.out.println("The number of words entered is: " + wordCount);
            }
        }

        System.out.println("Exiting the program...");
        scanner.close();
    }

    public static int countWords(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }

        String[] words = input.split("\\s+");
        return words.length;
    }
}
