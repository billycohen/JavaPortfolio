import java.util.*;
/**
 * This class handles all of the items in the game.
 * Items are assigned a description, weight and type.
 * The weight is designed to restrict the amount of items the player can carry.
 * The type of item determines what is is used for.
 * Items with the type "weapon" can be equipped by the player in a weapon slot
 * which increases the amount of damage they do to monsters.
 * Items with the type "head", "chest" etc can be equipped as armour to
 * decrease the amount of damage players take during combat.
 * There are also other "special" items which are used to grant the player
 * certain effects such as extra carrying capacity.
 */
public class Item
{
    public String itemDesc;
    public int itemWeight;
    public String itemType;
    /**
     * Creates an item using a description and weight which are provided by the game class.
     */
    public Item(String itemDesc, int itemWeight, String itemType)
    {
        this.itemDesc = itemDesc;
        this.itemWeight = itemWeight;
        this.itemType = itemType;
    }
    
    /**
     * This method returns the name of an item
     */
    public String getName()  
    {
        return itemDesc;
    }
    
    /**
     * This method returns the item type of an item
     */
    public String getType() 
    {
        return itemType;
    }
    
    /**
     * This method returns the weight of an item
     */
    public int getWeight()
    {
        return itemWeight;
    }
}
