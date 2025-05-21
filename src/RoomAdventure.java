import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class RoomAdventure { // Main class containing game logic

    // class variables
    private static Room currentRoom; // The room the player is currently in
    private static List<String> inventory = new ArrayList<>(); // Dynamic inventory
    private static String status; // Message to display after each action

    // constants
    final private static String DEFAULT_STATUS =
        "Sorry, I do not understand. Try [verb] [noun]. Valid verbs include 'go', 'look', 'take', 'use'.";

    public static void main(String[] args) { // Entry point of the game
        showTitleScreen();             // Display title and wait for 'start'
        setupGame();                   // Initialize game world
        Scanner s = new Scanner(System.in);

        while (true) { // Continuous game loop
            System.out.println(currentRoom);
            System.out.print("Inventory: ");
            if (inventory.isEmpty()) {
                System.out.print("(empty)");
            } else {
                for (String item : inventory) System.out.print(item + " ");
            }
            System.out.println();

            System.out.println("What would you like to do?");
            String input = s.nextLine().trim().toLowerCase();
            String[] words = input.split(" ");

            if (words.length == 1) {
                String verb = words[0];
                if (verb.equals("quit")) {
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
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // nothing
            }

            System.out.println(status);
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // nothing
            }

        }
    }

    // Displays a simple ASCII title screen and waits for the player to type 'start'
    private static void showTitleScreen() {
        System.out.println("====================================");
        System.out.println("===      ROOM ADVENTURE       ===");
        System.out.println("====================================");
        System.out.println("Type 'start' to begin your journey...");
        Scanner s = new Scanner(System.in);
        while (true) {
            String cmd = s.nextLine().trim().toLowerCase();
            if (cmd.equals("start")) {
                break;
            }
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

    // Attempts to clear console by printing blank lines
    private static void clearScreen() {
        for (int i = 0; i < 50; i++) System.out.println();
    }

    private static void handleGo(String noun) { // Handles moving between rooms
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
                status = "Moved to " + currentRoom.getName();
                break;
            }
        }
    }

    private static void handleLook(String noun) { // Handles inspecting items
        String[] items = currentRoom.getItems();
        String[] itemDescriptions = currentRoom.getItemDescriptions();
        status = "I don't see that item.";
        for (int i = 0; i < items.length; i++) {
            if (noun.equals(items[i])) {
                status = itemDescriptions[i];
                break;
            }
        }
    }

    private static void handleTake(String noun) { // Handles picking up items
        String[] grabbables = currentRoom.getGrabbables();
        status = "I can't grab that.";
        for (String item : grabbables) {
            if (noun.equals(item)) {
                if (noun.equals("spider")) {
                    showDeathScreen("You provoked the deadly spider!");
                }
                inventory.add(noun);
                status = noun + " added to inventory.";
                // Remove from room
                currentRoom.setGrabbables(removeElement(currentRoom.getGrabbables(), noun));
                currentRoom.setItems(removeElement(currentRoom.getItems(), noun));
                break;
            }
        }
    }

    private static void handleUse(String noun) { // Handles using items
        if (!inventory.contains(noun)) {
            status = "You don't have that item.";
            return;
        }
        switch (noun) {
            case "apple": // Example food
            case "sandwich":
                status = "You eat the " + noun + ". Delicious!";
                inventory.remove(noun);
                break;
            case "key":
                // Only usable in Room 4F with a door
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

    private static void handleQuit() {
        System.out.println("Thank you for playing! Goodbye.");
        System.exit(0); // Terminates the program successfully
    }


    // Utility to remove a single element from a String[]
    private static String[] removeElement(String[] array, String elem) {
        List<String> list = new ArrayList<>(Arrays.asList(array));
        list.remove(elem);
        return list.toArray(new String[0]);
    }

    private static void setupGame() { // Initializes game world
        // First floor rooms
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

        // Exits first floor
        r1F.setExitDirections(new String[]{"east", "south"});
        r1F.setExitDestinations(new Room[]{r2F, r3F});
        r2F.setExitDirections(new String[]{"west", "south", "stairs"});
        r2F.setExitDestinations(new Room[]{r1F, r4F, r1S});
        r3F.setExitDirections(new String[]{"north", "east", "window"});
        r3F.setExitDestinations(new Room[]{r1F, r4F, null});
        r4F.setExitDirections(new String[]{"north", "west"});
        r4F.setExitDestinations(new Room[]{r2F, r3F});

        // Exits second floor
        r1S.setExitDirections(new String[]{"downstairs", "east"});
        r1S.setExitDestinations(new Room[]{r2F, r2S});
        r2S.setExitDirections(new String[]{"west", "south", "window"});
        r2S.setExitDestinations(new Room[]{r1S, r4S, null});
        r3S.setExitDirections(new String[]{"west", "ladder"});
        r3S.setExitDestinations(new Room[]{r4S, attic});
        r4S.setExitDirections(new String[]{"north", "east"});
        r4S.setExitDestinations(new Room[]{r2S, r3S});
        attic.setExitDirections(new String[]{"down"});
        attic.setExitDestinations(new Room[]{r3S});

        // Items and descriptions
        r1F.setItems(new String[]{"painting", "apple"});
        r1F.setItemDescriptions(new String[]{"An old painting of a landscape.", "A fresh red apple."});
        r2F.setItems(new String[]{"desk", "key"});
        r2F.setItemDescriptions(new String[]{"There's a locked drawer in the desk.", "A small rusty key."});
        r3F.setItems(new String[]{"rug"});
        r3F.setItemDescriptions(new String[]{"A dusty rug with a floral pattern."});
        r4F.setItems(new String[]{"door"});
        r4F.setItemDescriptions(new String[]{"A sturdy door that appears locked."});
        r1S.setItems(new String[]{"bookshelf", "sandwich"});
        r1S.setItemDescriptions(new String[]{"Filled with old tomes.", "A half-eaten sandwich."});
        r2S.setItems(new String[]{"table"});
        r2S.setItemDescriptions(new String[]{"There is a note on the table."});
        r3S.setItems(new String[]{"chest", "coin"});
        r3S.setItemDescriptions(new String[]{"A wooden chest that looks fragile.", "A shiny gold coin."});
        r4S.setItems(new String[]{"window seat"});
        r4S.setItemDescriptions(new String[]{"A cozy nook by the window."});
        attic.setItems(new String[]{"floorboards", "spider"});
        attic.setItemDescriptions(new String[]{"They creak underfoot.", "A large, menacing spider."});

        // Grabbables
        r1F.setGrabbables(new String[]{"apple"});
        r2F.setGrabbables(new String[]{"key"});
        r3F.setGrabbables(new String[]{});
        r4F.setGrabbables(new String[]{});
        r1S.setGrabbables(new String[]{"sandwich"});
        r2S.setGrabbables(new String[]{});
        r3S.setGrabbables(new String[]{"coin"});
        r4S.setGrabbables(new String[]{});
        attic.setGrabbables(new String[]{"spider"});

        currentRoom = r1F; // Start in Room 1F
    }
}

class Room {
    final private String name;
    private String[] exitDirections;
    private Room[] exitDestinations;
    private String[] items;
    private String[] itemDescriptions;
    private String[] grabbables;

    public Room(String name) {
        this.name = name;
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

    public void setItemDescriptions(String[] itemDescriptions) {
        this.itemDescriptions = itemDescriptions;
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\nLocation: ").append(name);
        result.append("\nYou See: ");
        for (String item : items) result.append(item).append(", ");
        result.append("\nExits: ");
        for (String direction : exitDirections) result.append(direction).append(" ");
        return result.toString();
    }
}