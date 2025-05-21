import java.util.Scanner; // Import Scanner for reading useri nput

public class RoomAdventure { // Main class containing game logic

}

class Room { // Represents a game room
    private String name; // Room name
    private String[] exitDirections; // Directions you can go
    private Room[] exitDestinations; // Rooms reached by each direcetion
    private String[] items; // Items visible in the room
    private String[] itemDescriptions; // Descriptions for those items
    private String[] grabbables; // Items you can take

    public Room(String name) { // Constructor
        this.name = name; // Set the room's name
    }

    public void setExitDirections(String[] exitDirections) { // Setter for exits
        this.exitDirections = exitDirections;    
    }

    public String[] getExitDirections() { // Getter for exits
        return exitDirections;
    }

    public void setExitDestinations(Room[] exitDestinations) { // Setter for exit destinations
        this.exitDestinations = exitDestinations;
    }

    public Room[] getExitDestinations() { // Getter for exit destinations
        return exitDestinations;    
    }

    public void setItems(String[] items) { // Setter for items
        this.items = items;    
    }

    public String[] getItems() { // Getter for items 
        return items;
    }

    public void setItemDescriptions(String[] itemDescriptions) { // Setter for descriptions
        this.itemDescriptions = itemDescriptions;
    }

    public String[] getItemDescriptions() { // Getter for descriptions
        return itemDescriptions;
    }

    public void setGrabbables(String[] grabbables) { // Setter for grabbable items
        this.grabbables = grabbables;    
    }

    public String[] getGrabbables() { // Getter for grabbable items
        return grabbables;    
    }

    @Override // This (@Override) is extra. It CAN be used, but not required.`
    public String toString() { // Custom print for the room
        String result = "\nLocation: " + name; // Show room name
        result += "\nYou see: "; // List items
        for (String item : items) { // Loop items
            result += item + " "; // Append each item    
        }
        result += "\nExits: "; // List exits
        for (String direction : exitDirections) { // Loop exits
            result += direction + " "; // Append each direction    
        }
        return result + "\n"; // Return full description
    }

}