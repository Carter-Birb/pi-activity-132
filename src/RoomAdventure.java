import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class RoomAdventure { // Main class containing game logic

    // The current room the player is in
    private static Room currentRoom;
    // Player's inventory
    private static List<String> inventory = new ArrayList<>();
    // Status message to show after each action
    private static String status;

    // Default message for unrecognized commands
    final private static String DEFAULT_STATUS =
        "Sorry, I do not understand. Try [verb] [noun]. Valid verbs include 'go', 'look', 'take', 'use'.";

    public static void main(String[] args) throws InterruptedException { // Entry point of the game
        showTitleScreen(); // Display game title screen and wait for user to type "start"
        setupGame();       // Initialize rooms, items, and connections
        Scanner s = new Scanner(System.in);

        while (true) {
            printSeparator();
            System.out.print(currentRoom); // Show current room info
            displayInventory();            // Show player inventory
            printSeparator();
            System.out.println("\nWhat would you like to do?");
            String input = s.nextLine().trim().toLowerCase();
            String[] words = input.split(" ");

            // Process user input based on number of words
            if (words.length == 1) {
                if (words[0].equals("quit")) {
                    handleQuit();
                } else {
                    status = DEFAULT_STATUS;
                }
            } else if (words.length == 2) {
                String verb = words[0];
                String noun = words[1];
                switch (verb) {
                    case "go":
                        handleGo(noun);
                        break;
                    case "look":
                        handleLook(noun);
                        break;
                    case "take":
                        handleTake(noun);
                        break;
                    case "use":
                        handleUse(noun);
                        break;
                    default:
                        status = DEFAULT_STATUS;
                }
            } else {
                status = DEFAULT_STATUS;
            }

            System.out.println();
            System.out.println(status); // Display outcome message
            delay(1000);               // Wait for 1 second
        }
    }

    // Print player's current inventory
    private static void displayInventory() {
        System.out.print("Inventory: ");
        if (inventory.isEmpty()) {
            System.out.print("(empty)");
        } else {
            System.out.print(String.join(", ", inventory));
        }
    }

    // Shows simple ASCII title screen and wait for "start"
    private static void showTitleScreen() {
        System.out.println("====================================");
        System.out.println("===        ROOM ADVENTURE        ===");
        System.out.println("====================================");
        System.out.println("Type 'start' to begin your journey...");
        Scanner s = new Scanner(System.in);
        while (true) {
            String cmd = s.nextLine().trim().toLowerCase();
            if (cmd.equals("start")) break;
            System.out.println("Please type 'start' to play.");
        }
        clearScreen();
    }

    // Shows a multi-line death screen with cause, waits for input, then exits
    private static void showDeathScreen(String cause) {
        System.out.println("\n☠☠☠ YOU DIED ☠☠☠");
        System.out.println("Cause of death: " + cause);
        System.out.println("Better luck next time...");
        System.out.println("Press ENTER to exit.");
        new Scanner(System.in).nextLine();
        System.exit(0);
    }

    // Clears the console by printing 50 new lines
    private static void clearScreen() {
        for (int i = 0; i < 50; i++) System.out.println();
    }

    // Pause the program for the given number of milliseconds
    private static void delay(int millis) throws InterruptedException {
        Thread.sleep(millis);
    }

    // Handle the "go" command to move between rooms
    private static void handleGo(String noun) {
        String[] exitDirections = currentRoom.getExitDirections();
        Room[] exitDestinations = currentRoom.getExitDestinations();
        status = "I don't see that exit.";
        for (int i = 0; i < exitDirections.length; i++) {
            if (noun.equals(exitDirections[i])) {
                // Check for deadly windows
                if (exitDirections[i].equals("window")) {
                    showDeathScreen("You jumped through a deadly window!");
                }
                currentRoom = exitDestinations[i];
                status = "You move to " + currentRoom.getName() + ".";
                return;
            }
        }
    }

    // Handle the "look" command to examine items
    private static void handleLook(String noun) {
        String[] items = currentRoom.getItems();
        String[] descriptions = currentRoom.getItemDescriptions();
        status = "I don't see that item.";
        for (int i = 0; i < items.length; i++) {
            if (noun.equals(items[i])) {
                status = descriptions[i];
                return;
            }
        }
    }

    // Handle the "take" command to collect items into inventory
    private static void handleTake(String noun) {
        String[] grabbables = currentRoom.getGrabbables();
        status = "I can't grab that.";
        for (String item : grabbables) {
            if (noun.equals(item)) {
                if (noun.equals("spider")) {
                    showDeathScreen("You provoked the deadly spider!");
                }
                inventory.add(noun);
                status = noun + " added to inventory.";
                currentRoom.setGrabbables(removeElement(currentRoom.getGrabbables(), noun)); // Removes Grabbable from the Room
                currentRoom.setItems(removeElement(currentRoom.getItems(), noun)); // Removed Item from the Room
                return;
            }
        }
    }

    // Handle the "use" command to interact with inventory items
    private static void handleUse(String noun) {
        if (!inventory.contains(noun)) {
            status = "You don't have that item.";
            return;
        }
        switch (noun) {
            case "apple":
            case "sandwich":
                status = "You eat the " + noun + ". Delicious!";
                inventory.remove(noun);
                break;
            case "key":
                if (currentRoom.getName().equals("Room 4F")) {
                    status = "You use the key to unlock the door. You can now exit!";
                    inventory.remove(noun);
                } else {
                    status = "You can't use that here.";
                }
                break;
            default:
                status = "You can't use that.";
        }
    }

    // Handle "quit" command
    private static void handleQuit() {
        System.out.println("Thanks for playing!");
        System.exit(0);
    }

    // Helper method to remove an element from an array
    private static String[] removeElement(String[] array, String elem) {
        List<String> list = new ArrayList<>(Arrays.asList(array));
        list.remove(elem);
        return list.toArray(new String[0]);
    }

    // Print a separator line between sections
    private static void printSeparator() {
        System.out.print("\n" + "=".repeat(36));
    }

    // Initialize rooms, their contents, and their connections
    private static void setupGame() {
        Room r1F = new Room("Room 1F");
        Room r2F = new Room("Room 2F");
        Room r3F = new Room("Room 3F");
        Room r4F = new Room("Room 4F");
        // Second floor rooms
        Room r1S = new Room("Room 1S");
        Room r2S = new Room("Room 2S");
        Room r3S = new Room("Room 3S");
        Room r4S = new Room("Room 4S");
        // Attic
        Room attic = new Room("Attic");

        // Set exits for each room and link destinations
        // First floor
        r1F.setExitDirections(new String[]{"east", "south"});
        r1F.setExitDestinations(new Room[]{r2F, r3F});
        r2F.setExitDirections(new String[]{"west", "south", "stairs"});
        r2F.setExitDestinations(new Room[]{r1F, r4F, r1S});
        r3F.setExitDirections(new String[]{"north", "east", "window"});
        r3F.setExitDestinations(new Room[]{r1F, r4F, null});
        r4F.setExitDirections(new String[]{"north", "west"});
        r4F.setExitDestinations(new Room[]{r2F, r3F});
        // Second floor
        r1S.setExitDirections(new String[]{"downstairs", "east"});
        r1S.setExitDestinations(new Room[]{r2F, r2S});
        r2S.setExitDirections(new String[]{"west", "south", "window"});
        r2S.setExitDestinations(new Room[]{r1S, r4S, null});
        r3S.setExitDirections(new String[]{"west", "ladder"});
        r3S.setExitDestinations(new Room[]{r4S, attic});
        r4S.setExitDirections(new String[]{"north", "east"});
        r4S.setExitDestinations(new Room[]{r2S, r3S});
        // Attic
        attic.setExitDirections(new String[]{"down"});
        attic.setExitDestinations(new Room[]{r3S});

        // Set items and descriptions for each room
        // First floor
        r1F.setItems(new String[]{"painting", "apple"});
        r1F.setItemDescriptions(new String[]{"An old painting of a landscape.", "A fresh red apple."});
        r2F.setItems(new String[]{"desk", "key"});
        r2F.setItemDescriptions(new String[]{"A desk with a locked drawer.", "A small rusty key."});
        r3F.setItems(new String[]{"rug"});
        r3F.setItemDescriptions(new String[]{"A dusty rug with a floral pattern."});
        r4F.setItems(new String[]{"door"});
        r4F.setItemDescriptions(new String[]{"A sturdy door that appears locked."});
        // Second floor
        r1S.setItems(new String[]{"bookshelf", "sandwich"});
        r1S.setItemDescriptions(new String[]{"Filled with old tomes.", "A half-eaten sandwich."});
        r2S.setItems(new String[]{"table"});
        r2S.setItemDescriptions(new String[]{"There is a note on the table."});
        r3S.setItems(new String[]{"chest", "coin"});
        r3S.setItemDescriptions(new String[]{"A fragile chest.", "A shiny gold coin."});
        r4S.setItems(new String[]{"window seat"});
        r4S.setItemDescriptions(new String[]{"A cozy nook by the window."});
        // Attic
        attic.setItems(new String[]{"floorboards", "spider"});
        attic.setItemDescriptions(new String[]{"They creak underfoot.", "A large, menacing spider."});

        // Set grabbable items in each room
        // First floor
        r1F.setGrabbables(new String[]{"apple"});
        r2F.setGrabbables(new String[]{"key"});
        r3F.setGrabbables(new String[]{});
        r4F.setGrabbables(new String[]{});
        // Second floor
        r1S.setGrabbables(new String[]{"sandwich"});
        r2S.setGrabbables(new String[]{});
        r3S.setGrabbables(new String[]{"coin"});
        r4S.setGrabbables(new String[]{});
        // Attic
        attic.setGrabbables(new String[]{"spider"});

        // Set starting room
        currentRoom = r1F;
    }
}

class Room {
    private String name;
    private String[] exitDirections;
    private Room[] exitDestinations;
    private String[] items;
    private String[] itemDescriptions;
    private String[] grabbables;

    public Room(String name) {
        this.name = name;
        this.exitDirections = new String[]{};
        this.exitDestinations = new Room[]{};
        this.items = new String[]{};
        this.itemDescriptions = new String[]{};
        this.grabbables = new String[]{};
    }

    public String getName() {
        return name;
    }

    public void setExitDirections(String[] exitDirections) {
        this.exitDirections = exitDirections;
    }

    public String[] getExitDirections() {
        return exitDirections;
    }

    public void setExitDestinations(Room[] exitDestinations) {
        this.exitDestinations = exitDestinations;
    }

    public Room[] getExitDestinations() {
        return exitDestinations;
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    public String[] getItems() {
        return items;
    }

    public void setItemDescriptions(String[] descriptions) {
        this.itemDescriptions = descriptions;
    }

    public String[] getItemDescriptions() {
        return itemDescriptions;
    }

    public void setGrabbables(String[] grabbables) {
        this.grabbables = grabbables;
    }

    public String[] getGrabbables() {
        return grabbables;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nYou are in " + name + ".\n");
        sb.append("You see: ");
        if (items.length > 0) {
            sb.append(String.join(", ", items));
        } else {
            sb.append("(Nothing)");
        }
        sb.append("\n");
        sb.append("Exits: ");
        if (exitDirections.length > 0) {
            sb.append(String.join(", ", exitDirections));
        } else {
            sb.append("(None)");
        }
        sb.append("\n");
        return sb.toString();
    }
}