/*
 * File: Adventure.java
 * --------------------
 * This program plays the Adventure game from Assignment #4.
 */

import java.io.*;
import java.util.*;

/* Class: Adventure */
/**
 * This class is the main program class for the Adventure game.
 */

public class Adventure extends AdventureStub {

	private SortedMap<Integer, AdvRoom> rooms = new TreeMap<Integer, AdvRoom>();
	private List<AdvObject> inventory = new ArrayList<AdvObject>();
	private Map<String, String> synonyms = new HashMap<String, String>();
	private boolean quit;

	private AdvRoom currentRoom;

	// Use this scanner for any console input
	private static Scanner scan = new Scanner(System.in);

	/**
	 * This method is used only to test the program
	 */
	public static void setScanner(Scanner theScanner) {
		scan = theScanner;
		// Delete the following line when done
		AdventureStub.setScanner(theScanner);
	}

	/**
	 * Runs the adventure program
	 */
	public static void main(String[] args) {

		System.out.print("What will be your adventure today? ");
		String name = scan.nextLine();

		Adventure game = new Adventure();

		// read the room file
		String roomFile = name + "Rooms.txt";
		try {
			Scanner s = new Scanner(new File(roomFile));
			while (s.hasNextInt()) {
				AdvRoom room = AdvRoom.readFromFile(s);
				game.rooms.put(room.getRoomNumber(), room);
			}
		} catch (IOException e) {
			System.out.println("The rooms file '" + roomFile + "' couldn't be read.");
			return;
		}

		// read the object file and place the objects in their corresponding
		// rooms.
		String objectFile = name + "Objects.txt";
		try {
			Scanner s = new Scanner(new File(objectFile));
			while (s.hasNextLine()) {
				AdvObject obj = AdvObject.readFromFile(s);
				if (obj != null) {
					game.rooms.get(obj.getInitialLocation()).addObject(obj);
				}
			}
		} catch (IOException e) {
			System.out.println("The objects file '" + objectFile + "' couldn't be read.");
			return;
		}

		// Read the synonym file
		String synonymFile = name + "Synonyms.txt";
		try {
			Scanner s = new Scanner(new File(synonymFile));
			String line;
			while (s.hasNextLine() && (line = s.nextLine().trim()).length() > 0) {
				String[] parts = line.split("=");
				game.synonyms.put(parts[0], parts[1]);
			}
		} catch (IOException e) {
			System.out.println("The synonyms file '" + objectFile + "' couldn't be read.");
			return;
		}

		// Run the adventure
		game.run();
	}

	// Run the game
	public void run() {
		currentRoom = rooms.get(rooms.firstKey());

		// loop
		while (true) {
			// ask for a command
			System.out.print("> ");
			String command = scan.nextLine().trim().toUpperCase();

			// process the command
			// split on one or more spaces: \s+
			String[] parts = command.split("\\s+");

			// Replace any word with its synonym
			// Loop through the map of synonyms
			// if parts[i] is a key, replace it by the value

			if (parts.length > 0) {
				AdvCommand cmd = null;
				AdvObject obj = null;
				if (parts.length > 1) {
					// get the object from parts[1]
				}
				switch (parts[0]) {
					case "TAKE":
						// take command
						cmd = AdvCommand.TAKE;
						break;
					case "DROP":
						// drop command
						cmd = AdvCommand.DROP;
						break;
					case "HELP":
						cmd = AdvCommand.HELP;
						break;
					// other commands
					// LOOK, I (inventory), QUIT
					default: // any motion command
						cmd = new AdvMotionCommand(parts[0]);
						break;
				}
				// execute the command
				cmd.execute(this, obj);
			}
		}
	}

	/* Method: executeMotionCommand(direction) */
	/**
	 * Executes a motion command. This method is called from the AdvMotionCommand
	 * class to move to a new room.
	 * 
	 * @param direction The string indicating the direction of motion
	 */
	public void executeMotionCommand(String direction) {
		super.executeMotionCommand(direction); // Replace with your code
	}

	/* Method: executeQuitCommand() */
	/**
	 * Implements the QUIT command. This command should ask the user to confirm the
	 * quit request and, if so, should exit from the play method. If not, the
	 * program should continue as usual.
	 */
	public void executeQuitCommand() {
		System.out.println("Are you sure? (Y/N): ");
		String input = scan.nextLine().trim().toLowerCase();
		if (input.equals("y")) {
			quit = true;
		} else if (input.equals("n")) {
			quit = false;
		}

	}

	/* Method: executeHelpCommand() */
	/**
	 * Implements the HELP command. Your code must include some help text for the
	 * user.
	 */
	public void executeHelpCommand() {
		super.executeHelpCommand(); // Replace with your code
	}

	/* Method: executeLookCommand() */
	/**
	 * Implements the LOOK command. This method should give the full description of
	 * the room and its contents.
	 */
	public void executeLookCommand() {
		String[] array = currentRoom.getDescription();
		for (int i = 0; i < array.length; i++) {
			System.out.println(array[i]);
		}
	}

	/* Method: executeInventoryCommand() */
	/**
	 * Implements the INVENTORY command. This method should display a list of what
	 * the user is carrying.
	 */
	public void executeInventoryCommand() {
		super.executeInventoryCommand(); // Replace with your code
	}

	/* Method: executeTakeCommand(obj) */
	/**
	 * Implements the TAKE command. This method should check that the object is in
	 * the room and deliver a suitable message if not.
	 * 
	 * @param obj The AdvObject you want to take
	 */
	public void executeTakeCommand(AdvObject obj) {
		super.executeTakeCommand(obj); // Replace with your code
	}

	/* Method: executeDropCommand(obj) */
	/**
	 * Implements the DROP command. This method should check that the user is
	 * carrying the object and deliver a suitable message if not.
	 * 
	 * @param obj The AdvObject you want to drop
	 */
	public void executeDropCommand(AdvObject obj) {
		super.executeDropCommand(obj); // Replace with your code
	}

	/* Private instance variables */
	// Add your own instance variables here
}
