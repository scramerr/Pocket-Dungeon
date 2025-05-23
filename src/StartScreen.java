import java.util.Scanner;

public class StartScreen {

    public static void showStartScreen() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("+--------------------------------------+");
        System.out.println("|         🕹️  POCKET DUNGEON  🕹️        |");
        System.out.println("+--------------------------------------+");
        System.out.println();
        System.out.println("       ⛓️ You awaken in a dark dungeon... ⛓️");
        System.out.println();
        System.out.println("            🧍 Choose your destiny:");
        System.out.println();
        System.out.println("             1️⃣  Start New Game");
        System.out.println("             2️⃣  Load Game");
        System.out.println("             3️⃣  Exit");
        System.out.println();
        System.out.print("Enter your choice (1-3): ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.println("\n⚔️ Starting new game...");
                // Call your new game method here
                break;
            case "2":
                System.out.println("\n📂 Loading saved game...");
                // Call your load game method here
                break;
            case "3":
                System.out.println("\n👋 Goodbye, adventurer!");
                System.exit(0);
                break;
            default:
                System.out.println("\n❌ Invalid input. Please enter 1, 2 or 3.");
                showStartScreen(); // Restart screen
        }
    }
}
