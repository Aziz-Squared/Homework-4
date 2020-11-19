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
	public Map<String, AdvObject> inventory;
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
		
		while (scan.hasNextInt()) {
			AdvRoom a = AdvRoom.readFromFile(scan);
			adventure.room.put(a.getRoomNumber(), a);
		}
	}

	/**
	 * Runs the adventure program
	 */
	public static void main(String[] args) {
		//AdventureStub.main(args); // Replace with your code
		Adventure adventure = null;
					
		System.out.print("What will be your adventure today? ");
		String game = scan.next();
		if (game.toLowerCase().equals("crowther")) {
			game = "CrowtherRooms.txt";
		} else if (game.toLowerCase().equals("tiny")) {
			game = "TinyRooms.txt";
		} else if (game.toLowerCase().equals("small")) {
			game = "SmallRooms.txt";
		}
		
		try (Scanner scanner = new Scanner(new File(game))) {
			adventure.setScanner(scanner);
			AdvRoom currentroom = AdvRoom.readFromFile(scanner);
			System.out.println(currentroom);
			// first room  
			AdvRoom currentRoom = adventure.rooms.get(adventure.rooms.firstKey());
			while (true) {
				System.out.print(currentRoom.getDescription());
     			System.out.print("< ");
//				String direction = scan.nextLine();
//				
//				AdvMotionTableEntry next = currentRoom.getMotionTable()[2];
//				System.out.println(next);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Game could not be found. Sorry!");
		}	
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
		 // Replace with your code
		for(Entry<String ,AdvObject> s : this.inventory.entrySet()) {
			AdvObject temp= s.getValue();
			System.out.println(temp.getName()+": "+temp.getDescription());
		}
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
			this.inventory.put(obj.getName(), obj);
			System.out.println(obj.getName()+" is added to inventory. ");
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
			System.out.println("The object is dropped to the current location...");
		}
	}
		public void executeForcedCommand() {
			this.handleForcedMotion();
		}
		// TODO Auto-generated method stub
		
	}

	/* Private instance variables */
	// Add your own instance variables here

