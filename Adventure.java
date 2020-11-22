/*
 * File: Adventure.java
 * --------------------
 * This program plays the Adventure game from Assignment #4.
 */

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/* Class: Adventure */
/**
 * This class is the main program class for the Adventure game.
 */

public class Adventure extends AdventureStub {

	// Use this scanner for any console input
	private static Scanner scan = new Scanner(System.in);
	
	public static boolean testingmode = false;
	public ArrayList <AdvObject> inventory;
	public Map<Integer, AdvRoom> rooms;
	public Map<String, AdvCommand> commands;
	public Map<String, AdvObject> allobject;
	public AdvRoom currentroom;
	
	public  SortedMap<Integer,AdvRoom> room = new TreeMap<Integer,AdvRoom>();

	/**
	 * This method is used only to test the program
	 */
	public static void setScanner(Scanner theScanner) {
		scan = theScanner;
		// Delete the following line when done
		//AdventureStub.setScanner(theScanner);
	}

	/**
	 * Runs the adventure program
	 */
	public static void main(String[] args) {
		//AdventureStub.main(args); // Replace with your code
		Adventure adventure = new Adventure();
		System.out.print("What will be your adventure today? ");
		String gameName = scan.nextLine();
		
		// Reads the room file
		String roomFile = gameName.substring(0, 1).toUpperCase() + gameName.substring(1) + "Rooms.txt";
		
		try {
			Scanner scanner = new Scanner(new File(roomFile));
			
			while (scanner.hasNextInt()) {
				AdvRoom a = AdvRoom.readFromFile(scanner);
				adventure.rooms.put(a.getRoomNumber(), a);
			}
		} catch (IOException e) {
			System.out.println("The room file" + roomFile + "could not be read.");
			return;
		}	
		
		// Reads the objects file
		String objectFile = gameName.substring(0, 1).toUpperCase() + gameName.substring(1) + "Objects.txt";
		
		try {
			Scanner scanner = new Scanner(new File(objectFile));
			
			while (scanner.hasNextLine()) {
				AdvObject a = AdvObject.readFromFile(scanner);
				if (a != null) {
					adventure.rooms.get(a.getInitialLocation()).addObject(a);;
				}
			}
		} catch (IOException e) {
			System.out.println("The object file" + objectFile + "could not be read.");
			return;
		}	
		
		// Reads the synonyms file
		String synonymFile = gameName.substring(0, 1).toUpperCase() + gameName.substring(1) + "Synonyms.txt";
		
		try {
			Scanner scanner = new Scanner(new File(synonymFile));
			
			String line;
			while (scanner.hasNextLine() && (line = scanner.nextLine().trim()).length() > 0) {
				String[] parts = line.split("=");
				adventure.synonyms.put(parts[0], parts[1]);
			}
		} catch (IOException e) {
			System.out.println("The synonyms file" + synonymFile + "could not be read.");
			return;
		}	
		adventure.run();
	}


	/* Method: executeMotionCommand(direction) */
	/**
	 * Executes a motion command. This method is called from the
	 * AdvMotionCommand class to move to a new room.
	 * 
	 * @param direction
	 *            The string indicating the direction of motion
	 */
	public void executeMotionCommand(String direction) {
		 // Replace with your code
			if(direction ==null || direction.isEmpty())throw new AssertionError(); // what the fuck has happened?
		AdvMotionTableEntry result;
		
		AdvMotionTableEntry[] compass =this.currentroom.getMotionTable();
		 
		Boolean alldoesnotmatch = true;
		for(AdvMotionTableEntry s : compass) {
			if(s.getDirection().equals(direction)) {
				if(s.getKeyName()!=null&&this.inventory.containsKey(s.getKeyName())) {
						int destiny = s.getDestinationRoom();
						this.transportToRoom(this.rooms.get(s.getDestinationRoom()));
						alldoesnotmatch= false;
						break;
				}
				if(s.getKeyName()==null) {
					this.transportToRoom(this.rooms.get(s.getDestinationRoom()));
					alldoesnotmatch= false;
					break;
				}
			}
		}
		if(alldoesnotmatch)System.out.println("You cannot go there. ");	
	}
	
	private void transportToRoom(AdvRoom advRoom) {
		// TODO Auto-generated method stub 
	}

	/* Method: executeQuitCommand() */
	/**
	 * Implements the QUIT command. This command should ask the user to confirm
	 * the quit request and, if so, should exit from the play method. If not,
	 * the program should continue as usual.
	 */
	public void executeQuitCommand() {
		 // Replace with your code
		char temp ;
		if(Adventure.scan.hasNextLine()&& !((temp = Adventure.scan.nextLine().toUpperCase().charAt(0))=='Y'))
		{
			
			return;
		}
		//System.exit(0);
		try {
			System.out.println("Try to exist the game. ");
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/* Method: executeHelpCommand() */
	/**
	 * Implements the HELP command. Your code must include some help text for
	 * the user.
	 */
	public void executeHelpCommand() {
		 // Replace with your code
			for(AdvMotionTableEntry s :this.currentroom.getMotionTable()) {
			String key = s.getKeyName()!=null?": "+s.getKeyName():"";
			System.out.println(s.getDirection()+key);
		}
		if(this.currentroom.getObjectCount()>0) {
		
			for(int i = 0 ; i<this.currentroom.getObjectCount();i++)
			{
				System.out.println(this.currentroom.getObject(i).getDescription());
			}
		}
	}

	/* Method: executeLookCommand() */
	/**
	 * Implements the LOOK command. This method should give the full description
	 * of the room and its contents.
	 */
	public void executeLookCommand() {
		// Replace with your code
			System.out.println(">>>"+this.currentroom.getName());
		for(String s : this.currentroom.getDescription()) {
			System.out.println("   "+s);
		}
		this.currentroom.setVisited(true);
		this.handleForcedMotion();
	}
             private void handleForcedMotion() {
		// TODO Auto-generated method stub
	}

	/* Method: executeInventoryCommand() */
	/**
	 * Implements the INVENTORY command. This method should display a list of
	 * what the user is carrying.
	 */
	public void executeInventoryCommand() {
		if (inventory.isEmpty()) {
			System.out.println("You are empty-handed.");
		}
		 // Replace with your code
		for(AdvObject s : inventory) {
			System.out.println(s.getName()+": "+s.getDescription());
		}
	}

	/* Method: executeTakeCommand(obj) */
	/**
	 * Implements the TAKE command. This method should check that the object is
	 * in the room and deliver a suitable message if not.
	 * 
	 * @param obj
	 *            The AdvObject you want to take
	 */
	public void executeTakeCommand(AdvObject obj) {
		// Replace with your code
		if(obj==null||!this.currentroom.containsObject(obj)) {
			System.out.println("I cannot find the object... "); }
		else
		{
			this.currentroom.removeObject(obj);
			this.inventory.add(obj);
			System.out.println("Taken.");
		}
	}

	/* Method: executeDropCommand(obj) */
	/**
	 * Implements the DROP command. This method should check that the user is
	 * carrying the object and deliver a suitable message if not.
	 * 
	 * @param obj
	 *            The AdvObject you want to drop
	 */
	public void executeDropCommand(AdvObject obj) {
		 // Replace with your code
			if(obj==null || !this.inventory.containsValue(obj))
		{
			System.out.println
			(obj==null?"I cannot analyze the object you typed":"You don't have this object...");
		}
		else
		{
			this.currentroom.addObject(this.inventory.remove(obj.getName()));
			System.out.println("Dropped.");
		}
	}
		public void executeForcedCommand() {
			this.handleForcedMotion();
		}
		// TODO Auto-generated method stub
		
	}

	/* Private instance variables */
	// Add your own instance variables here

