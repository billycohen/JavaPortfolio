import java.util.*;
/**
 *  This class is the main class of the "Prison Escape" application, which
 *  is based on the original "zuul-better" application which was created by
 *  Michael Kölling and David J. Barnes.
 * 
 *  This application is a game in which player(s) must escape from a prison
 *  by navigating through rooms, using items, completing quests, and
 *  engaging in combat with various enemies.
 *  
 *  This purpose of this class is to set up the game environment, which
 *  includes rooms, items, objects and NPCs that the player interacts with.
 *  
 *  This class also processes various commands which the player types into
 *  the console and links all of the other classes together.
 * 
 *  @Author: William Cohen
 *  @Version: 1.0
 */

public class Game
{
    private Parser parser;
    private Room jail, hallway, lab, teleporter, guardroom, barracks,
            stairwell, greathall, courtyard, gate;
    private Item knife, staff, hat, healthpot, jailkey, helmet, chestplate,
            platelegs, sword, backpack, dustykey, gatekey;
    private NPC wizard, prisoner, guard1, guard2, guard3, guard4, guard5,
            guard6, warden, dungeonmaster;
    private Object  crate, pedestal, wardrobe, weaponrack, jaildoor, hole,
            bucket, chest, armourrack, bedroll, tree, bush, fountain, lockedgate;
    public Player playerOne, playerTwo, currentPlayer;
    private boolean multiplayer; // checks whether or not multiple players are present
    private ArrayList<Room> rooms; // list used for teleporter room.
    
    /**
     * Create the game.
     * Initialize the map by creating rooms and exit points.
     * Create items and place them in rooms and on NPC's
     * Create NPC's and place them in specific rooms.
     */
    public Game(String player1, String player2) 
    {
        rooms = new ArrayList<Room>();
        multiplayer = false; // by default, game is configured for single player.
        createRoom(); 
        createNPC();
        createItem();
        createObject();
        createPlayer(player1, player2);
        initialise();
        currentPlayer = playerOne; // playerOne always starts the game
        parser = new Parser();
    }
    
    /**
     * Creates the players which are controlled by the users as a Player object
     * Assigns various fields to each player such as hitpoints, damage and
     * starting room.
     */
    private void createPlayer(String player1, String player2)
    {
        playerOne = new Player(player1, 30, 5, 20, jail);
        jail.placePlayer(playerOne);
        playerTwo = new Player(player2, 30, 5, 20, jail);
        jail.placePlayer(playerTwo);
    }
    
    /**
     * Sends player back to starting location and resets their stats.
     */
    private void playerReset()
    {
        currentPlayer.currentRoom.removePlayer(currentPlayer);
        jail.placePlayer(currentPlayer);
        currentPlayer.resetStats();
    }
    
    /**
     *  Create rooms which make up the game.
     *  All rooms are created with a description.
     *  Some rooms are added to an ArrayList rooms which is used when moving
     *  the player to a random room.
     */
    private void createRoom() 
    {
        // Create rooms
        jail = new Room("In a jail cell");
        hallway = new Room("In a dungeon hallway");
        lab = new Room("In a laboratory");
        guardroom = new Room("in a guard room");
        barracks = new Room("In the barracks");
        teleporter = new Room("You enter a teleporter...");
        stairwell = new Room("You enter a stairwell");
        greathall = new Room("You enter the great hall");
        courtyard = new Room("You enter the courtyard");
        gate = new Room("You exit through the gate");
        
        // Add rooms to list
        rooms.add(jail);
        rooms.add(hallway);
        rooms.add(lab);
        rooms.add(guardroom);
        
        // Give rooms exit points
        
        hallway.setExit("east", guardroom);
        hallway.setExit("west", lab);
        hallway.setExit("south", jail);
        
        stairwell.setExit("north", greathall);
        stairwell.setExit("south", hallway);
        
        greathall.setExit("east", courtyard);
        greathall.setExit("south", stairwell);
        
        courtyard.setExit("west", greathall);
        
        lab.setExit("east", hallway);
        lab.setExit("west", teleporter);
        
        guardroom.setExit("west", hallway);
        guardroom.setExit("north", barracks);
        
        barracks.setExit("south", guardroom);
        
    }
    
    /**
     *  This method is used to dynamically add exit points to rooms.
     */
    private void createExit(Room room, String direction, Room destination)
    {
        room.setExit(direction,destination);
    }
    
    /**
     *  Create items which are placed around the game.
     *  Items can be placed in rooms on in NPC inventories.
     *  All items are created with a description and weight.
     *  Items are also assigned a type, which determines what they are used for.
     */
    private void createItem() 
    {
        // Create items
        // rock = new Item("name",weight,"type");
        knife = new Item("knife", 1, "weapon");
        staff = new Item("staff", 2, "misc");
        hat = new Item("hat", 1, "misc");
        healthpot = new Item("healthpot", 1, "misc");
        jailkey = new Item("jailkey",0,"misc");
        chestplate = new Item("chestplate", 3, "chest");
        platelegs = new Item("platelegs", 3, "legs");
        helmet = new Item("helmet", 3, "helm");
        backpack = new Item("backpack", 0, "special");
        sword = new Item("sword", 4, "weapon");
        dustykey = new Item("dustykey", 0, "misc");
        gatekey = new Item("gatekey", 0, "misc");
    }
    
    /**
     *  Create NPC's which are placed around the game.
     *  All NPC's are created with a name and hitpoints.
     *  NPC's also carry items.
     *  NPC's can also be configured to be hostile or passive. true indicates hostile.
     *  NPC's are also placed in rooms at the start of the game.
     */
    private void createNPC() 
    {
        // Create NPCs
        wizard = new NPC("wizard", 50, false, 30, lab);
        prisoner = new NPC("prisoner", 10, false, 0, hallway);
        guard1 = new NPC("guard", 20, true, 5, guardroom);
        guard2 = new NPC("guard", 20, true, 5, guardroom);
        guard3 = new NPC("guard", 20, true, 5, greathall);
        guard4 = new NPC("guard", 20, true, 5, greathall);
        guard5 = new NPC("guard", 20, true, 5, greathall);
        guard6 = new NPC("guard", 20, true, 5, greathall);
        warden = new NPC("warden", 13, false, 5, hallway);
        dungeonmaster = new NPC("dungeonmaster", 40, false, 10, courtyard);
        
                                
    }
    
    /**
     *  Create objects which are placed in rooms.
     *  objects can contain items and be searched through by the player.
     */
    private void createObject() 
    {
        // Create Objects
        crate = new Object("crate");
        pedestal = new Object("pedestal");
        wardrobe = new Object("wardrobe");
        weaponrack = new Object("weaponrack");
        jaildoor = new Object("jaildoor");
        chest = new Object("chest");
        hole = new Object("hole");
        bucket = new Object("bucket");
        bedroll = new Object("bedroll");
        armourrack = new Object("armourrack");        
        bush = new Object("bush");
        tree = new Object("tree");
        fountain = new Object("fountain");
        lockedgate = new Object("lockedgate");
    }
    
    /**
     *  places items around the game
     *  places NPCs around the game
     *  places objects around the game
     *  gives NPCs and objects items
     */
    private void initialise()
    {
        // Place items in rooms.
        jail.placeItem(knife);
        
        // Place NPCs in rooms.
        lab.placeNPC(wizard);
        
        hallway.placeNPC(prisoner);
        
        guardroom.placeNPC(guard1);
        guardroom.placeNPC(guard2);
        
        hallway.placeNPC(warden);
        
        greathall.placeNPC(guard3);
        greathall.placeNPC(guard4);
        
        courtyard.placeNPC(guard5);
        courtyard.placeNPC(guard6);
        courtyard.placeNPC(dungeonmaster);
        
        // Give NPCs items
        wizard.placeItem(hat);
        
        prisoner.placeItem(backpack);
        
        warden.placeItem(dustykey);
        warden.placeItem(healthpot);
        
        guard1.placeItem(healthpot);
        guard2.placeItem(healthpot);
        guard3.placeItem(healthpot);
        guard4.placeItem(healthpot);
        guard5.placeItem(healthpot);
        guard6.placeItem(healthpot);
        
        dungeonmaster.placeItem(gatekey);
        
        // Place Objects in rooms.
        hallway.placeObject(crate);
        hallway.placeObject(pedestal);
        
        jail.placeObject(jaildoor);
        jail.placeObject(hole);
        jail.placeObject(bucket);
        jail.placeObject(bedroll);
        
        guardroom.placeObject(chest);
       
        barracks.placeObject(weaponrack);
        barracks.placeObject(armourrack);
        
        stairwell.placeObject(bucket);
        stairwell.placeObject(crate);
        
        courtyard.placeObject(tree);
        courtyard.placeObject(bush);
        courtyard.placeObject(fountain);
        courtyard.placeObject(lockedgate);
        
        
        // Give objects items
        hole.placeItem(jailkey);
        
        chest.placeItem(staff);
        
        crate.placeItem(healthpot);
        tree.placeItem(healthpot);
        
        armourrack.placeItem(chestplate);
        armourrack.placeItem(helmet);
        armourrack.placeItem(platelegs);
        
        weaponrack.placeItem(sword);
    }
    
    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();
        
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
            if (currentPlayer.currentRoom == gate) 
            {
                System.out.println("Congratulations, you have won!");
                finished = true;
                npcMovement();
            }
        }
        System.out.println("Thank you for playing.  Good bye.");
    }
    
    private String playerTag() 
    {
        return (currentPlayer.getUsername() + " - HP: " + 
                currentPlayer.getHP() + " / " + currentPlayer.getMaxHP()) + ". Carry Weight: "
                + currentPlayer.getCurrentWeight() + " / " + 
                currentPlayer.getMaxCarryW() + ". \n";
    }
    
    /**
     * Controls movement of the warden npc
     */
    private void npcMovement()
    {
        warden.npcMove(warden);
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println("Current player: " + currentPlayer.getUsername());
        System.out.println(currentPlayer.currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("inventory")) {
            currentPlayer.printInventory();
        }
        else if (commandWord.equals("go")) {
            goRoom(command, currentPlayer);
        }
        else if (commandWord.equals("back")) {
            goBack(command, currentPlayer);
        }
        else if (commandWord.equals("get")) {
            getItem(command);
        }
        else if (commandWord.equals("equip")) {
            equipItem(command);
        }
        else if (commandWord.equals("remove")) {
            removeItem(command);
        }
        else if (commandWord.equals("attack")) {
            attack(command);
        }
        else if (commandWord.equals("use")) {
            useItem(command);
        }
        else if (commandWord.equals("drop")) {
            dropItem(command);
        }
        else if (commandWord.equals("talk")) {
            talk(command);
        }
        else if (commandWord.equals("give")) {
            give(command);
        } else if (commandWord.equals("search")) {
            search(command);
        }
        else if (commandWord.equals("switch")) {
            if (multiplayer = true) 
            {
                currentPlayer = switchPlayer();
                System.out.println("Current Player: " + currentPlayer.getUsername());
                System.out.println(currentPlayer.currentRoom.getLongDescription());
            } else {
                System.out.println("You can only switch players if you are playing multiplayer");
            }
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }
    
    /**
     * This method switches the current player.
     */
    private Player switchPlayer()
    {
        if (currentPlayer == playerOne) 
        {
            return playerTwo;
        } else 
        {
            return playerOne;
        }
    }
    
    /**
     * This method handles player interaction with NPC's
     */
    private void talk(Command command) 
    {
        if(!command.hasSecondWord())
        {
            System.out.println(playerTag() + "Talk to who?");
            return;
        }
        String dialogue;
        
        String search = command.getSecondWord();
        
        NPC subject = currentPlayer.currentRoom.getNPC(search);
        
        
        if (subject == null) 
        {
            System.out.println(playerTag() + search + " is not in this room");
        } else 
        {
            subject.npcDialogue(subject);
        }
    }
    
    /**
     * Method which handles the attack command, allowing players to engage
     * in combat with NPCs that are located in their current room.
     */
    private void attack(Command command)
    {
        if(!command.hasSecondWord())
        {
            System.out.println(playerTag() + "Attack what?");
            return;
        }
        
        String search = command.getSecondWord();
        
        NPC subject = currentPlayer.currentRoom.getNPC(search);
        
        boolean death = false;
        
        if (subject == null) 
        {
            System.out.println(playerTag() + search + " is not in this room...");
        } else 
        {
            System.out.println("You attack " + search + "...");
            death = currentPlayer.attackNPC(subject);
            if (death == true) 
            {
                
            }
        }
    }
    
    /**
     * Allows players to use items such as health potions to recover stats
     * such as hitpoints.
     */
    private void useItem(Command command)
    {
        if(!command.hasSecondWord()) 
        {
            System.out.println(playerTag() + "Use what?");
            return;
        }
        
        String search = command.getSecondWord();
        
        Item item = currentPlayer.inventoryGet(search);
        
        if (item == null)
        {
            System.out.println(playerTag() + " " + search + " is not in your inventory...");
            return;
        }
        
        currentPlayer.useItem(item);
        return;
    }
    
    /**
     * This method allows the player to give items to NPCs, objects
     * and other players. This is used in quests and allows trading between
     * players.
     */
    private void give(Command command) 
    {
        if(!command.hasSecondWord()) 
        {
            System.out.println(playerTag() + "Give what?");
            return;
        }
        
        String searchInventory = command.getSecondWord();
        
        Item giveItem = currentPlayer.inventoryGet(searchInventory);
        
        if(giveItem == null) 
        {
            System.out.println(playerTag() + searchInventory + " is not in inventory");
            return;
        }
        
        if(!command.hasThirdWord()) 
        {
            System.out.println(playerTag() + "Give " + searchInventory + " to who?");
            return;
        }
        
        String search = command.getThirdWord();
        NPC subject = currentPlayer.currentRoom.getNPC(search);
        Object object = currentPlayer.currentRoom.getObject(search);
        Player player = currentPlayer.currentRoom.getPlayer(search);
        
        if(subject == null && object == null && player == null) 
        {
            System.out.println(playerTag() + search + " is not in this room");
            return;
        } else if (subject != null) 
        {
            String questCompleted;
            questCompleted = subject.giveNPC(subject, giveItem);
            if (questCompleted != null) 
            {
                currentPlayer.inventory.remove(giveItem);
                currentPlayer.inventory.add((subject.getReward(questCompleted)));
            } else
            {
                System.out.println(playerTag() + " They have no use for that item");
            }
        } else if(object !=null) 
        {
            System.out.println(playerTag() + " You place " + searchInventory + " in the "
            + search);
            object.placeItem(giveItem);
            currentPlayer.inventory.remove(giveItem);
            if ((giveItem == jailkey) && (object == jaildoor))
            {
                createExit(jail, "north", hallway);
                System.out.println(playerTag() + " You unlocked a door, you can go north");
                currentPlayer.currentRoom.getLongDescription();
                return;
            } else if ((giveItem == hat) && (object == pedestal)) {
                createExit(hallway, "north", stairwell);
                System.out.println(playerTag() + " You unlocked a door, you can go north");
                currentPlayer.currentRoom.getLongDescription();
                return;
            } else if ((giveItem == gatekey) && (object == lockedgate)) {
                createExit(courtyard, "north", gate);
                System.out.println(playerTag() + " You unlocked a door, you can go north");
                currentPlayer.currentRoom.getLongDescription();
                return;
            }
            
        } else if(player != null)
        {
            player.placeItem(giveItem);
            currentPlayer.inventory.remove(giveItem);
            System.out.println(playerTag() + "You gave " + searchInventory 
            + " to " + player.getUsername());
        }
        
        
    }
    
    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command, Player currentPlayer) 
    {
        if(!command.hasSecondWord()) 
        {
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        Room nextRoom = currentPlayer.currentRoom.getExit(direction);

        if (nextRoom == null) 
        {
            System.out.println("There is no door!");
            return;
        } else if (nextRoom == teleporter) 
        {
            goRandom();
            return;
        }
        nextRoom.placePlayer(currentPlayer);
        currentPlayer.previousRoom = currentPlayer.currentRoom;
        currentPlayer.currentRoom.removePlayer(currentPlayer);
        System.out.println(nextRoom.getLongDescription());
        currentPlayer.currentRoom = nextRoom;
    }
    
    /**
     * This method handles the back command which the player can use to 
     * quickly go back to the room they were previously in.
     */
    private void goBack(Command command, Player currentPlayer)
    {
        Room nextRoom = currentPlayer.previousRoom;
        if (nextRoom == null) 
        {
            System.out.println("You cannot move backwards");
            return;
        } else if (nextRoom == teleporter) 
        {
            System.out.println("You cannot move back into the teleporter");
            return;
        }
        nextRoom.placePlayer(currentPlayer);
        currentPlayer.previousRoom = currentPlayer.currentRoom;
        currentPlayer.currentRoom.removePlayer(currentPlayer);
        System.out.println(nextRoom.getLongDescription());
        currentPlayer.currentRoom = nextRoom;
    }
    
    /**
     * This method is used to randomly select a room and send the player to
     * that room. This is utilized by the teleporter room.
     */
    private void goRandom()
    {
        System.out.println("You have been teleported to a random room!");
        Random rand = new Random();
        int n = rand.nextInt(4) + 1;
        Room nextRoom = rooms.get(n);
        nextRoom.placePlayer(currentPlayer);
        currentPlayer.previousRoom = currentPlayer.currentRoom;
        currentPlayer.currentRoom.removePlayer(currentPlayer);
        System.out.println(nextRoom.getLongDescription());
        currentPlayer.currentRoom = nextRoom;
    }
    
    /**
     * This method handles the search command which the player can use to
     * search through objects that are currently in their room and display
     * the items that are in that object.
     */
    private void search(Command command) 
    {
        if (!command.hasSecondWord()) 
        {
            System.out.println(playerTag() + "Search what?");
            return;
        }
        String search = command.getSecondWord();
        
        Object result = currentPlayer.currentRoom.getObject(search);
        
        String found;
        
        if(result == null) 
        {
            System.out.println(playerTag() + search + " is not in this room");
            return;
        } else 
        {
            result.searchObject(result);
            return;
        }
    }
    
    /**
     * This method handles the get command which can be used by the player to
     * get items from the room or items within objects in the room they are
     * currently in.
     */
    private void getItem(Command command) 
    {
        if(!command.hasSecondWord()) 
        {
            System.out.println(playerTag() + "Get what?");
            return;
        }
        String item = command.getSecondWord();
        
        Item pickUp = currentPlayer.currentRoom.getItem(item);
        if (!command.hasThirdWord()) 
        {
            if (pickUp == null) 
            {
                System.out.println(playerTag() + "That item is not in this room...");
                return;
            } else 
            {
                if (currentPlayer.getCurrentWeight() + pickUp.getWeight() 
                    <= currentPlayer.getMaxCarryW()) 
                {
                    currentPlayer.inventory.add(pickUp);
                    currentPlayer.currentRoom.removeItem(item);
                    System.out.println(playerTag() + "You added the " + item + " to your inventory...");
                    return;
                } else 
                {
                    System.out.println("You are carrying too much stuff!");
                    return;
                }
                
            }
        }
        String search = command.getThirdWord();
        Object object = currentPlayer.currentRoom.getObject(search);
        
        
        if (object == null)
        {
            System.out.println(playerTag() + "That object is not in this room...");
            return;
        } else 
        {
            Item searchObject = object.getItem(item);
            if (searchObject == null)
            {
                System.out.println(playerTag() + item + " could not be found in " 
                + object.objectName() + "...");
                return;
            } else {
                currentPlayer.inventory.add(searchObject);
                object.removeItem(searchObject);
                System.out.println(playerTag() + "You take the " + searchObject.getName()
                + " from the " + object.objectName() + "...");
                return;
            }
        }
            
     
    }
    
    /**
     * This method handles the equip command which can be used by players to
     * equip certain items in certain equipment slots to upgrade their stats.
     */
    private void equipItem(Command command)
    {
        if(!command.hasSecondWord()) 
        {
            System.out.println(playerTag() + "Equip what?");
            return;
        }
        
        String search = command.getSecondWord();
        
        Item equip = currentPlayer.inventoryGet(search);
        
        if (equip == null)
        {
            System.out.println(playerTag() + search + " is not in your inventory...");
            return;
        } else 
        {
            currentPlayer.equipItem(equip);
            return;
        }
    }
    
    /**
     * This method handles the remove command, which is used by the player
     * to remove items that they have equipped.
     */
    private void removeItem(Command command)
    {
        if(!command.hasSecondWord()) 
        {
            System.out.println(playerTag() + "Remove what?");
            return;
        }
        
        String search = command.getSecondWord();
        
        Item remove = currentPlayer.equipGet(search);
        
        if (remove == null)
        {
            System.out.println(playerTag() + search + " is not equipped.");
            return;
        } else 
        {
            currentPlayer.removeItem(remove);
            return;
        }
    }
    
    /**
     * This method handles the drop command which is used to remove items from
     * the players inventory, and puts the item on the floor of the room they
     * are currently in.
     */
    private void dropItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println(playerTag() + "Drop what?");
            return;
        }

        String item = command.getSecondWord();
        Item newItem = null;
        
        for(int i=0;i<currentPlayer.inventory.size();i++) 
        {
            if(currentPlayer.inventory.get(i).getName().equals(item)) 
            {
                newItem = currentPlayer.inventory.get(i);
            }
        }
        
        if (newItem == null) 
        {
            System.out.println(playerTag() + "That item is not in your inventory...");
        } else 
        {
            currentPlayer.inventory.remove(newItem);
            currentPlayer.currentRoom.placeItem(newItem);
            System.out.println(playerTag() + "You dropped " + item +"...");
        }
    }
    
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) 
        {
            System.out.println(playerTag() + "Quit what?");
            return false;
        }
        else 
        {
            return true;  // signal that we want to quit
        }
    }
}
