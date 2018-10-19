import java.util.ArrayList;
import java.util.Arrays;
/**
 * The Borough class is responsible for storing statistics on each of the
 * boroughs which hold listings on the map.
 */
public class Borough
{
    private static String name;
    private static ArrayList<AirbnbListing> listings;
    

    /**
     * Constructor for objects of class Bourough
     */
    public Borough(String name)
    {
        this.name = name;
    }

    /**
     * Responsible for adding listings to the borough.
     */
    private void populateBorough()
    {
        
    }
    
    /**
     * Returns the average price of the listings in the borough.
     */
    private int getAveragePrice()
    {
        int total = 0;
        for (int i=0;i<listings.size();i++)
        {
            total += listings.get(i).getPrice();
            
        }
        return (total/listings.size());
    }
    
    private int getAverageReview()
    {
        int total = 0;
        for (int i=0;i<listings.size();i++)
        {
            total += listings.get(i).getPrice();
            
        }
        return (total/listings.size());
    }
}
