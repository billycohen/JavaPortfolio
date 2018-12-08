import java.util.*;
/**
 * This class handles the players which users control when playing the game.
 * Each player has their own attributes: a username, hitpoints and inventory.
 * Players can move around from room to room and interact with various 
 * entities in the room, whether it be NPCs, items or objects
 * Players can also equip certain items which boost their stats, allowing them
 * to take on larger challenges and further progress through the game.
 */
public class Player
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            {
    public Room currentRoom;
    public ArrayList<Item> inventory;
    private Item weapon, helm, chest, legs, special;
    public Room previousRoom;
    private int hp;
    private int maxhp;
    private int dmg;
    public int carryW;
    private String username;
    private ArrayList<String> direction;

    /**
     * Create an object of type Player.
     * Attributes username, hp, damage and current room are provided by the
     * game class.
     * Assign each player their own inventory, carry capacity and various
     * equipment slots.
     */
    public Player(String username, int hp, int dmg, int carryW, Room currentRoom)
    {
        this.currentRoom = currentRoom;
        this.previousRoom = previousRoom;
        this.inventory = new ArrayList<Item>();
        this.hp = hp;
        this.dmg = dmg;
        this.carryW = carryW;
        this.username = username;
        this.weapon = weapon;
        this.helm = helm;
        this.chest = chest;
        this.legs = legs;
        this.special = special;
        this.maxhp = 30;
    }
    
    /**
     * Method used to return the username of a player
     */
    public String getUsername()
    {
        return username;
    }
    
    /**
     * Method used to return the damage of a player
     */
    public int getDmg()
    {
        return dmg;
    }
    
    
    /**
     * Method used to return the hitpoints of a player
     */
    public int getHP()
    {
        return hp;
    }
    
    /**
     * Method used to return the maximum hitpoints of a player
     */
    public int getMaxHP()
    {
        return maxhp;
    }
    
    /**
     * Method used to return the carrying capacity of a player
     */
    public int getMaxCarryW()
    {
        return carryW;
    }
    
    /**
     * This method current weight of all the items the player is carrying.
     */
    public int getCurrentWeight()
    {
        int currentWeight = 0;
        for(int i=0; i < inventory.size(); i++){
                    currentWeight += inventory.get(i).getWeight();
                }
        return currentWeight;
    }
    
    /**
     * Method used to adjust the HP of a player
     */
    public int updateHP(int dmg)
    {
        hp -= dmg;
        System.out.println("Your HP is now " + hp);
        return hp;
    }
    
    /**
     * Method used to print all of the items in the players inventory
     */
    public void printInventory() 
    {
        String displayInventory;
        if (inventory.size() == 0) 
        {
            displayInventory = "Inventory is empty";
        } else {
                displayInventory = "Inventory: \n";
                for(int i=0; i < inventory.size(); i++){
                    displayInventory += "Item: " + inventory.get(i).itemDesc
                    + ". Weight: " + inventory.get(i).itemWeight + ".\n";
                }
        }
        System.out.println(displayInventory);
    }
    
    /**
     * Method used to search through the inventory of a player using the 
     * name of an item.
     */
    public Item inventoryGet(String search) 
    {
        for(int i=0; i < inventory.size(); i++){
            if (inventory.get(i).getName().equals(search) ) {
                return inventory.get(i);
            }
        }
        return null;
    }
    
    /**
     * Method used to search through currently equipped items using the
     * name of the item.
     */
    public Item equipGet(String search)
    {
        if (weapon != null) {
            if (weapon.getName().equals(search) == true)
            {
                return weapon;
            }
        } 
        if (helm != null) {
            if (helm.getName().equals(search) == true)
            {
                return helm;
            }
        }
        if (chest != null) {
            if (chest.getName().equals(search) == true)
            {
                return chest;
            }
 
        }
        if (legs != null) {
            if (legs.getName().equals(search) == true)
            {
                return legs;
            }
        }
        if (special != null) {
            if (special.getName().equals(search) == true)
            {   
                return special;
            }
        }
        return null;
    }
    
    /**
     * Method used to place an item in the player's inventory
     */
    public void placeItem(Item insert)
    {
        inventory.add(insert);
    }
    
    /**
     * Method used to move an item from the player's inventory to an 
     * appropriate equipment slot.
     */
    public void equipItem(Item insert)
    {
        if ((insert.itemType != "weapon") && (insert.itemType != "helm") &&
            (insert.itemType != "chest") && (insert.itemType != "legs") &&
            (insert.itemType != "special"))
        {
            System.out.println("You cannot equip that item");
        }
        else if(insert.itemType == "weapon")
        {
            if (weapon == null) 
            {
                weapon = insert;
                inventory.remove(insert);
                statUp(insert);
            } else 
            {
                System.out.println("You muse remove your current item first");
                return;
            }
        }
        else if(insert.itemType == "helm")
        {
            if (helm == null) 
            {
            helm = insert;
            inventory.remove(insert);
            statUp(insert);
            } else 
            {
                System.out.println("You muse remove your current item first");
                return;
            }
        }
        else if(insert.itemType == "chest")
        {
            if (chest == null) 
            {
            chest = insert;
            inventory.remove(insert);
            statUp(insert);
            } else 
            {
                System.out.println("You muse remove your current item first");
                return;
            }
        }
        else if(insert.itemType == "legs")
        {
            if (legs == null) 
            {
            legs = insert;
            inventory.remove(insert);
            statUp(insert);
            } else 
            {
                System.out.println("You muse remove your current item first");
                return;
            }
        }
        else if(insert.itemType == "special")
        {
            if (helm == null) 
            {
            special = insert;
            inventory.remove(insert);
            statUp(insert);
            } else 
            {
                System.out.println("You muse remove your current item first");
                return;
            }
        }
    }
    
    /**
     * Method used to remove an item from the players equipment slot.
     */
    public void removeItem(Item insert)
    {
        if(insert.itemType == "weapon")
        {
            weapon = null;
            inventory.add(insert);
            statDown(insert);
            return;
        }
        else if(insert.itemType == "helm")
        {
            helm = null;
            inventory.add(insert);
            statDown(insert);
            return;
        }
        else if(insert.itemType == "chest")
        {
            chest = null;
            inventory.add(insert);
            statDown(insert);
            return;
        }
        else if(insert.itemType == "legs")
        {
            legs = null;
            inventory.add(insert);
            statDown(insert);
            return;
        }
        else if(insert.itemType == "special")
        {
            special = null;
            inventory.add(insert);
            statDown(insert);
            return;
        }
    }
    
    /**
     * Method used to update the statistics of a player based on which
     * item they have equipped.
     */
    public void statUp(Item item) 
    {
        if (item.itemType == "weapon") 
        {
            if (item.getName() == "knife")
            {
                System.out.println("You equipped " + item.getName()
                + ". Your damage has been raised by 3.");
                dmg += 3;
                return;
            }
            if (item.getName() == "sword")
            {
                System.out.println("You equipped " + item.getName()
                + ". Your damage has been raised by 6.");
                dmg += 6;
                return;
            }
            return;
        }
        if (item.itemType == "helm") 
        {
            if (item.getName() == "helmet")
            {
                System.out.println("You equipped " + item.getName()
                + ". Your maximum health has been raised by 7.");
                maxhp += 7;
                return;
            }
            return;
        }
        if (item.itemType == "chest") 
        {
            if (item.getName() == "chesstplate")
            {
                System.out.println("You equipped " + item.getName()
                + ". Your maximum health has been raised by 15.");
                maxhp += 15;
                return;
            }
            return;
        }
        if (item.itemType == "legs") 
        {
            if (item.getName() == "platelegs")
            {
                System.out.println("You equipped " + item.getName()
                + ". Your maximum health has been raised by 10.");
                maxhp += 10;
                return;
            }
            return;
        }
        if (item.itemType == "special") 
        {
            if (item.getName() == "backpack")
            {
                System.out.println("You equipped " + item.getName()
                + ". Your carrying capacity has been raised by 20.");
                carryW += 20;
                return;
            }
            return;
        }
    }
    
    /**
     * Method used to update the statistics of a player based on which item
     * they have removed
     */
    public void statDown(Item item) 
    {
        if (item.itemType == "weapon") 
        {
            if (item.getName() == "knife")
            {
                System.out.println("You removed " + item.getName()
                + ". Your damage has been lowered by 3.");
                dmg -= 3;
                return;
            }
            if (item.getName() == "sword")
            {
                System.out.println("You removed " + item.getName()
                + ". Your damage has been lowered by 6.");
                dmg -= 6;
                return;
            }
            return;
        }
        if (item.itemType == "helm") 
        {
            if (item.getName() == "helmet")
            {
                System.out.println("You removed " + item.getName()
                + ". Your maximum hitpoints has been lowered by 7.");
                maxhp -= 7;
                return;
            }
            return;
        }
        if (item.itemType == "chest") 
        {
            if (item.getName() == "chest")
            {
                System.out.println("You removed " + item.getName()
                + ". Your maximum health has been lowered by 15.");
                maxhp -= 15;
                return;
            }
            return;
        }
        if (item.itemType == "legs") 
        {
            if (item.getName() == "platelegs")
            {
                System.out.println("You removed " + item.getName()
                + ". Your maximum health has been lowered by 10.");
                maxhp -= 10;
                return;
            }
            return;
        }
        if (item.itemType == "special") 
        {
            if (item.getName() == "backpack")
            {
                System.out.println("You removed " + item.getName()
                + ". Your carrying capacity has been lowered by 20.");
                carryW -= 20;
                return;
            }
            return;
        }
    }
    
    /**
     * Method used to handle combat between the player and an NPC
     */
    public boolean attackNPC(NPC subject) 
    {
        int playerdmg = getDmg();
        int npchp = subject.updateHP(playerdmg);
        if (npchp <= 0)
        {
            currentRoom.removeNPC(subject);
            System.out.println("You have killed " + subject.getName());
            subject.npcDeath();
            return false;
        } 
        System.out.println("You deal " + dmg + " damage to " 
                            + subject.getName() + "...");
                            
        int npcdmg = subject.getDmg();
        int playerhp = updateHP(npcdmg);
        if (playerhp <= 0)
        {
            System.out.println(subject.getName() + " deals a lethal attack to "
            + getUsername() + ". Killing them! \n They have respawned in jail.");
            return true;
        }
        return false;
    }
    
    /**
     * Method used to return the death of a player when their HP reaches 0.
     * Dead players drop all items on the floor of the room they are currently
     * in at the time of death.
     * Dead players respawn in the jail and their stats are reset.
     */
    public void playerDeath()
    {
        Item item;
        for (int i=0;i<inventory.size();i++) {
            item = inventory.get(i);
            inventory.remove(item);
            currentRoom.placeItem(item);
        }
        //playerReset();
        hp = 20;
    }
    
    /**
     * Method used to handle using items.
     */
    public void useItem(Item item)
    {
        if (item.getName() == "health_potion")
        {
            updateHP(-5);
            System.out.println("You use the " + item.getName());
        }
    }
    
    /**
     * This method resets the stats of a player.
     */
    public void resetStats()
    {
        hp = 30;
        dmg = 5;
    }
}
