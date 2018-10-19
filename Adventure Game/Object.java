import java.util.*;
/**
 * This class handles everything to do with objects
 * Objects are placed into rooms, and they may contain items
 * The player can search through the items using the search command.
 * The player can then take items from the object using the get command.
 * The player can also give items to objects, which may cause a special effect,
 * this is done using the give command.
 */
public class Object
{
    public String objectName;
    public ArrayList<Item> objectItems;
    /**
     * Constructor for objects of class Object
     */
    public Object(String objectName)
    {
        this.objectName = objectName;
        this.objectItems = new ArrayList<Item>();
    }
    
    public String objectName() {
        return objectName;
    }
    
    public void placeItem(Item insert)
    {
        objectItems.add(insert);
    }
    
    public void removeItem(Item insert)
    {
        objectItems.remove(insert);
    }
    
    public void searchObject(Object search) 
    {
        String returnString = "You found: ";
        for(int i=0; i < search.objectItems.size(); i++) 
        {
            returnString += search.objectItems.get(i).getName() + " ";
        }
        if (returnString == "You found: ")  
        {
            returnString += "nothing";
            System.out.println(returnString);
            return;
        } 
        System.out.println(returnString);
    }
    
    public Item getItem(String item)
    {
        for(int i=0; i < objectItems.size(); i++)
        {
            if (objectItems.get(i).getName().equals(item) == true ) 
            {
                return objectItems.get(i);
            }
        }
        return null;
    }
}
