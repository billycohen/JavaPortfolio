import java.util.*;
/**
 * This class handles all of the NPCs in the game
 * NPCs are non-player-characters and can be interacted with by the player
 * Certain NPCs can be friendly towards the player, offering them clues
 * or quests where they reward items.
 * NPCs can also be hostile towards the player, where they will engage
 * the player in combat to try to kill them.
 * NPCs can carry items and also move around between the rooms periodically.
 */
public class NPC
{
    public String npcName; 
    public int npcHP; // gives npc a HP value, used during combat.
    public int attackDmg; // 
    public boolean hostile;
    public ArrayList<Item> npcBag;
    public boolean wizardQuest = false; // Boolean to check whether quest is completed
    public boolean prisonerQuest = false; //
    public ArrayList<String> direction;
    private Room currentRoom;
    
    /**
     * Constructor for objects of class NPC.
     * Assigns attributes to a particular NPC.
     */
    public NPC(String npcName, int npcHP, boolean hostile, int attackDmg, Room currentRoom)
    {
        this.npcName = npcName;
        this.npcHP = npcHP;
        this.hostile = hostile;
        this.attackDmg = attackDmg;
        this.npcBag = new ArrayList<Item>();
        this.currentRoom = currentRoom;
    }
    
    /**
     * This method returns the name of an NPC
     */
    public String getName() 
    {
        return npcName;
    }
    
    /**
     * This method returns the damage of an NPC
     */
    public int getDmg()
    {
        return attackDmg;
    }
    
    /**
     * This method returns the hitpoints of an NPC
     */
    public int getHP()
    {
        return npcHP;
    }
    
    /**
     * This method updates the hp of an NPC
     */
    public int updateHP(int dmg)
    {
        npcHP -= dmg;
        return npcHP;
    }
    
    /**
     * This method returns the currentRoom of an NPC
     */
    public Room getRoom()
    {
        return currentRoom;
    }
    
    /**
     * This method handles to movement of an NPC from one room to another.
     * It generates a random number to select a direction in which the NPC will move
     */
    public void npcMove(NPC npc) {
        direction = new ArrayList<String>();
        direction.add("north");
        direction.add("east");
        direction.add("south");
        direction.add("west");
        
        Random rand = new Random();
        int  n = rand.nextInt(4) + 0;
        
        String moveDirection = direction.get(n);
        
        Room nextRoom = currentRoom.getExit(moveDirection);
        
        if (nextRoom == null) 
        {
            return;
        } 
        nextRoom.placeNPC(npc);
        currentRoom.removeNPC(npc);
        currentRoom = nextRoom;
    }
    
    /**
     * This method handles placing items in NPC inventories.
     */
    public void placeItem(Item insert) {
        npcBag.add(insert);
    }

    /**
     * This method handles to NPC dialogue when the player
     * uses the "talk" command.
     */
    public void npcDialogue(NPC npc) {
        String dialogue;
        if (npc.npcName.equals("wizard") == true) {
            // wizard dialogue
            if (wizardQuest == false) {
                dialogue = "Wizard: Greetings adventurer, are you interested in a quest? \n";
                dialogue += "        I appear to have lost my staff, can you find it for me? \n";
                dialogue += "        If you bring it to me, I will reward you greatly!";
                System.out.println(dialogue);
                return;
            } else {
                dialogue = "Wizard: Greetings adventurer. Thanks for your help earlier!";
                System.out.println(dialogue);
                return;
            }
        } else if (npc.npcName.equals("prisoner") == true) {
            if (prisonerQuest == false) 
            {
            System.out.println("Prisoner: Psst, get me out of here and I'll make it");
            System.out.println("          worth your time.");
            }
        } else if (npc.npcName.equals("orc") == true) {
            System.out.println("This NPC has nothing to say");
        } else if (npc.npcName.equals("a") == true) {
            System.out.println("This NPC has nothing to say");
        } else {
            System.out.println("This NPC has nothing to say");
        }
    }
    
    /**
     * This method handles giving items to NPC's
     */
    public String giveNPC(NPC npc, Item item) {
        //
        if((npc.npcName.equals("wizard") == true) && (item.itemDesc.equals("staff") == true)) {
            System.out.println("Wizard: Thank you adventurer, please take this as a reward!");
            System.out.println("You have revieved a magical Wizard's hat.");
            System.out.println("Place this on a pedestal.");
            wizardQuest = true;
            return "hat";
        } else if (((npc.npcName.equals("prisoner") == true) && (item.itemDesc.equals("dustykey")))) {
            System.out.println("Prisoner: Here, take this.");
            System.out.println("You have recieved a backpack. Equip it to increase");
            System.out.println("your carrying capacity.");
            prisonerQuest = true;
            return "backpack";
            
        }
        else {
            return null;
        }
    }
    
    /**
     * This method retrieves items from the NPC's inventory,
     */
    public void removeBag(Item item) {
        for(int i=0; i < npcBag.size(); i++){
            if (npcBag.get(i).equals(item) ) {
                npcBag.remove(i);
            }
        }
    }
    
    /**
     * This method searches through the NPCs inventory for an item based on
     * the name of the item.
     */
    public Item getReward(String itemName) {
        for(int i=0; i < npcBag.size(); i++){
            if (npcBag.get(i).itemDesc.equals(itemName) ) {
                return npcBag.get(i);
            }
        }
        return null;
    }
    
    /**
     * This method handles the death of NPCs.
     */
    public void npcDeath()
    {
        for(int i=0; i < npcBag.size(); i++){
            Item drop = npcBag.get(i);
            currentRoom.placeItem(drop);
            System.out.println(getName() + " has dropped " + drop.getName());
        }
    }
    
}
