    import java.util.*;
    
    /**
     * Class Room - a room in an adventure game.
     *
     * This class is part of the "World of Zuul" application. 
     * "World of Zuul" is a very simple, text based adventure game.  
     *
     * A "Room" represents one location in the scenery of the game.  It is 
     * connected to other rooms via exits.  For each existing exit, the room 
     * stores a reference to the neighboring room.
     * 
     * @author  Michael Kölling and David J. Barnes
     * @version 2016.02.29
     */
    
    public class Room 
    {
        private String description;
        private int roomID;
        private HashMap<String, Room> exits;        // stores exits of this room.
        private ArrayList<Item> items;              // stores items of this room.
        private ArrayList<NPC> npcs;                // stores npcs of this room.
        private ArrayList<Object> objects;          // stores objects of this room.
        private ArrayList<Player> players;          // stores players of this room.
        public  ArrayList<Room> rooms;
        /**
         * Create a room using the description provided by the Game class.
         * Create an ArrayList of items which are located in the room.
         * Create a HashMap which stores the exits of the room.
         * Create an ArrayList of objects which are located in the room
         * Create an ArrayList of npcs which are located in the room
         */
        public Room(String description) 
        {
            this.description = description;
            this.roomID = roomID;
            this.items = new ArrayList<Item>();
            this.npcs = new ArrayList<NPC>();
            this.objects = new ArrayList<Object>();
            this.players = new ArrayList<Player>();
            exits = new HashMap<>();
            
        }
        
        public int getID()
        {
            return roomID;
        }
        
        /**
         * This method handles placing items into the room
         */
        public void placeItem(Item insert) 
        {
            items.add(insert);
        }
        
        /**
         * This method handles placing NPCs into the room
         */
        public void placeNPC(NPC insert) 
        {
            npcs.add(insert);
        }
        
        public void removeNPC(NPC insert)
        {
            npcs.remove(insert);
        }
        
        public void placePlayer(Player insert) 
        {
            players.add(insert);
        }
        
        public void removePlayer(Player insert)
        {
            players.remove(insert);
        }
        
        public void placeObject(Object insert) 
        {
            objects.add(insert);
        }
        
        /**
         * Define an exit from this room.
         * @param direction The direction of the exit.
         * @param neighbor  The room to which the exit leads.
         */
        public void setExit(String direction, Room neighbor) 
        {
            exits.put(direction, neighbor);
        }
    
        /**
         * @return The short description of the room
         * (the one that was defined in the constructor).
         */
        public String getShortDescription()
        {
            return description;
        }
    
        /**
         * Returns room information such as a description, exits,
         * items, and npcs.
         */
        public String getLongDescription()
        {
            return "You are " + description + ".\n" + getExitString()
            + getItemString() + getObjectString() +  getNPCString() 
            + getPlayerString();
        }
    
        /**
         * This method returns a String with all of the items of the room.
         */
        private String getItemString() 
        {
            if (items.isEmpty() == true) 
            {
                return "";
            }
            String returnString = "Items:";
            for(int i=0; i < items.size(); i++)
            {
                returnString += " " + items.get(i).itemDesc;
            }
            return ".\n" + returnString;
        }
        
        private String getObjectString() 
        {
            if (objects.isEmpty() == true) 
            {
                return "";
            }
            String returnString = "Objects:";
            for(int i=0; i < objects.size(); i++)
            {
                returnString += " " + objects.get(i).objectName;
            }
            return ".\n" + returnString;
        }
        
        /**
         * Return a string describing the room's exits, for example
         * "Exits: north west".
         * @return Details of the room's exits.
         */
        private String getExitString()
        {
            String returnString = "Exits:";
            Set<String> keys = exits.keySet();
            for(String exit : keys) 
            {
                returnString += " " + exit;
            }
            return returnString;
        }
        
        /**
         * This method returns a the names of all the NPCs in the room
         */
        private String getNPCString() 
        {
            if (npcs.isEmpty() == true) 
            {
                return "";
            }
            String returnString = "NPC's:";
            for(int i=0; i < npcs.size(); i++)
            {
                returnString += " " + npcs.get(i).npcName;
            }
            return ".\n" + returnString;
        }
    
        /**
         * This method returns a the names of all the NPCs in the room
         */
        private String getPlayerString() 
        {
            if (players.isEmpty() == true) 
            {
                return "";
            }
            String returnString = "Players:";
            for(int i=0; i < players.size(); i++)
            {
                returnString += " " + players.get(i).getUsername();
            }
            return ".\n" + returnString;
        }
        
        /**
         * Return the room that is reached if we go from this room in direction
         * "direction". If there is no room in that direction, return null.
         * @param direction The exit's direction.
         * @return The room in the given direction.
         */
        public Room getExit(String direction) 
        {
            return exits.get(direction);
        }
        
        /**
         * This method returns an item from the room based on its index
         */
        public Item getItem(int index) 
        {
            return items.get(index);
        }
        
        /**
         * This method returns an item from the room based on its name
         */
        public Item getItem(String itemName) 
        {
            for(int i=0; i < items.size(); i++)
            {
                if (items.get(i).getName().equals(itemName)) 
                {
                    return items.get(i);
                }
            }
            return null;
        }
        
        public Object getObject(String search) 
        {
            for(int i=0; i < objects.size(); i++)
            {
                if (objects.get(i).objectName().equals(search)) 
                {
                    return objects.get(i);
                }
            }
            return null;
        }
        
        /**
         * This method returns an NPC from the room based on its name
         */
        public NPC getNPC(String npcName) 
        {
            for(int i=0; i < npcs.size(); i++)
            {
                if (npcs.get(i).getName().equals(npcName)) 
                {
                    return npcs.get(i);
                }
            }
            return null;
        }
        
        /**
         * This method returns an NPC from the room based on its name
         */
        public Player getPlayer(String playerName) 
        {
            for(int i=0; i < players.size(); i++)
            {
                if (players.get(i).getUsername().equals(playerName)) 
                {
                    return players.get(i);
                }
            }
            return null;
        }
        
        /**
         * This method handles removing an item from the room
         */
        public void removeItem(String itemName) 
        {
            for(int i=0; i < items.size(); i++)
            {
                if (items.get(i).getName().equals(itemName)) 
                {
                    items.remove(i);
                }
            }
        }
    }
    
