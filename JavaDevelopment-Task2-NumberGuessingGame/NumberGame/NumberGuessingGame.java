import java.util.Random;
import java.util.Scanner;

public class NumberGuessingGame {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        int round = 1;
        int totalScore = 0;
        String playAgain = "yes";

        System.out.println("=================================");
        System.out.println("      NUMBER GUESSING GAME");
        System.out.println("=================================");

        while (playAgain.equalsIgnoreCase("yes")) {

            System.out.println("\nSelect Difficulty:");
            System.out.println("1. Easy   (1-50, 10 attempts)");
            System.out.println("2. Medium (1-100, 7 attempts)");
            System.out.println("3. Hard   (1-200, 5 attempts)");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            int maxNumber;
            int maxAttempts;

            switch (choice) {
                case 1:
                    maxNumber = 50;
                    maxAttempts = 10;
                    break;

                case 2:
                    maxNumber = 100;
                    maxAttempts = 7;
                    break;

                case 3:
                    maxNumber = 200;
                    maxAttempts = 5;
                    break;

                default:
                    System.out.println("Invalid choice! Medium difficulty selected.");
                    maxNumber = 100;
                    maxAttempts = 7;
            }

            int randomNumber = random.nextInt(maxNumber) + 1;

            int attempts = 0;
            boolean guessedCorrectly = false;

            System.out.println("\nGuess a number between 1 and " + maxNumber);
            System.out.println("You have " + maxAttempts + " attempts.");

            while (attempts < maxAttempts) {

                System.out.print("\nEnter your guess: ");
                int guess = sc.nextInt();

                attempts++;

                if (guess > randomNumber) {
                    System.out.println("Too High!");
                }
                else if (guess < randomNumber) {
                    System.out.println("Too Low!");
                }
                else {
                    System.out.println("Correct!");
                    System.out.println("You guessed it in "
                            + attempts + " attempts!");

                    guessedCorrectly = true;

                    totalScore += (maxAttempts - attempts + 1) * 10;

                    break;
                }

                System.out.println("Attempts used: "
                        + attempts + "/" + maxAttempts);

                System.out.println("Attempts remaining: "
                        + (maxAttempts - attempts));
            }

            if (!guessedCorrectly) {
                System.out.println("\nYou Lost!");
                System.out.println("The correct number was: "
                        + randomNumber);
            }

            System.out.println("\n========== ROUND SUMMARY ==========");

            if (guessedCorrectly) {
                System.out.println("Round " + round
                        + " — guessed in " + attempts + " attempts");
            }
            else {
                System.out.println("Round " + round
                        + " — Failed after " + maxAttempts + " attempts");
            }

            System.out.println("Total Score: " + totalScore);

            System.out.print("\nDo you want to play again? (yes/no): ");
            playAgain = sc.next();

            round++;
        }

        System.out.println("\nGAME OVER");
        System.out.println("Final Score: " + totalScore);

        sc.close();
    }
}