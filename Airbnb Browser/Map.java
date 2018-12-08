import java.util.ArrayList;
import java.util.Arrays;
/**
 * The Map class represents a list of properties that are currently being displayed on the map, based on the users search parameters.
 *  This class is also responsible for calculating various statistics based on the listings stored within the map.
 *  
 * @author Ziad Al-Dara, Alexander Bass, William Cohen, Mihnea-Andrei Radulescu, Bhanu Guntupalli
 * @version 17.03.2018
 */
public class Map
{
    private ArrayList<AirbnbListing> listings;
    private int lowerLimit, upperLimit;
    private AirbnbDataLoader loader;
    private ArrayList<AirbnbListing> validListings;
    private ArrayList<AirbnbListing> filteredListings;
    private ArrayList<String> neighbourhoods;
    
    /**
     * Constructor for objects of class Map
     */
    public Map(int lowerLimit, int upperLimit)
    {
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        listings = new AirbnbDataLoader().load();
        neighbourhoods = new ArrayList<String>();
        validListings = new ArrayList<AirbnbListing>();
        filteredListings = new ArrayList<AirbnbListing>();
        populateProperties();
    }
    
    /**
     * Places valid propeties on the map based on the lower and upper boundry parameters provided by the user.
     * This method is also used to reset the map once the user has disabled any filters that they have enabled.
     */
    public void populateProperties()
    {
        validListings.clear(); // clears the validListings arraylist, used when disabling a filter.
        System.out.print("Loading valid Airbnb listings...");
        for (int i=0; i<listings.size(); i++)
        {
            if (((listings.get(i).getPrice() >= lowerLimit) && (listings.get(i).getPrice() <= upperLimit)))
                validListings.add(listings.get(i));
        }
        System.out.println("Success! Displaying listings priced between: " + lowerLimit + " and " + upperLimit + ". Number of loaded records: " + validListings.size());
        if (validListings.size() == 0)
            System.out.println("No properties found, please adjust your search criteria");
    }
    
    /**
     * Filters the current list of valid properties, removing those that do not match the requested type.
     * The type is selected with a series of check boxes.
     */
    public void filterListingsByRoomType(String roomType)
    {
        filteredListings.clear();
        System.out.print("Filtering Airbnb listings...");
        for (int i=0; i<validListings.size(); i++)
        {
            if ((validListings.get(i).getRoom_type().equals(roomType)))
                filteredListings.add(validListings.get(i));
        }
        validListings.clear();
        // for loop used to ensure arraylists have different memory locations.
        for (int i=0;i<filteredListings.size();i++)
        {
            validListings.add(filteredListings.get(i));
        }
        System.out.println("Success! Displaying listings with Room_Type: " + roomType + ". Number of loaded records: " + validListings.size());
        if (validListings.size() == 0)
            System.out.println("No properties found, please adjust your search criteria");
    }
    
    /**
     * Filters the current list of valid properties, removing those that do not match the requested neighbourhood.
     * The neighborhood is selected with a drop-down box within the GUI.
     */
    public void filterListingsByNeighbourhood(String neighbourhood)
    {
        filteredListings.clear();
        System.out.print("Filtering Airbnb listings...");
        for (int i=0; i<validListings.size(); i++)
        {
            if (validListings.get(i).getNeighbourhood().equals(neighbourhood))
                filteredListings.add(validListings.get(i));
        }
        validListings.clear();
        // for loop used to ensure arraylists have different memory locations.
        for (int i=0;i<filteredListings.size();i++)
        {
            validListings.add(filteredListings.get(i));
        }
        System.out.println("Success! Displaying listings in neighbourhood: " + neighbourhood + ". Number of loaded records: " + validListings.size());
        if (validListings.size() == 0)
            System.out.println("No properties found, please adjust your search criteria");
    }
    
    /**
     * Filters the current list of valid properties, removing those that do not match the requested stay duration.
     * The length of time that the user would like to stay is selected with a text box within the GUI
     */
    public void filterListingsByStay(int stay)
    {
        filteredListings.clear();
        System.out.print("Filtering Airbnb listings...");
        for (int i=0; i<validListings.size(); i++)
        {
            if (validListings.get(i).getMinimumNights() <= stay)
                filteredListings.add(validListings.get(i));
        }
        validListings.clear();
        // for loop used to ensure arraylists have different memory locations.
        for (int i=0;i<filteredListings.size();i++)
        {
            validListings.add(filteredListings.get(i));
        }
        System.out.println("Success! Displaying listings with Stay: " + stay + ". Number of loaded records: " + validListings.size());
        if (validListings.size() == 0)
            System.out.println("No properties found, please adjust your search criteria");
    }
    
    /**
     * Filters the current list of valid properties, removing those that do not match the requested Host ID.
     * The Host ID that the user would like to view properties of is selected with a text box within the GUI.
     */
    public void filterListingsByHost(int ID)
    {
        filteredListings.clear();
        System.out.print("Filtering Airbnb listings...");
        for (int i=0; i<validListings.size(); i++)
        {
            if (validListings.get(i).getMinimumNights() == ID)
                filteredListings.add(validListings.get(i));
        }
        validListings.clear();
        // for loop used to ensure arraylists have different memory locations.
        for (int i=0;i<filteredListings.size();i++)
        {
            validListings.add(filteredListings.get(i));
        }
        System.out.println("Success! Displaying listings with Host_ID: " + ID + ". Number of loaded records: " + validListings.size());
        if (validListings.size() == 0)
            System.out.println("No properties found, please adjust your search criteria");
    }
    
    /**
     * Returns the average price of all the listings that are currently shown on the map.
     */
    public int getAveragePrice()
    {
        int total = 0;
        for (int i=0;i<validListings.size();i++)
        {
            total += validListings.get(i).getPrice();
        }
        return (total/validListings.size());
    }
    
    /**
     * Returns the average price within a specific neighbourhood.
     * 
     * LOCAL STATISTICS CAN BE HANDLED IN A SEPERATE CLASS - BOROUGH? UP FOR DISCUSSION.
     */
    public int getAverageLocalPrice(String neighbourhood)
    {
        ArrayList<AirbnbListing> tempList = new ArrayList<AirbnbListing>(); // Used to temporarily store current listings.
        // for loop used to ensure arraylists have different memory locations.
        for (int i=0; i<validListings.size(); i++)
        {
            tempList.add(validListings.get(i));
        }
        filterListingsByNeighbourhood(neighbourhood);
        int avg = getAveragePrice();
        System.out.println(avg);
        validListings.clear();
        // for loop used to ensure arraylists have different memory locations. Sets validListing back to original state.
        for (int i =0; i<tempList.size(); i++)
        {
            validListings.add(tempList.get(i));
        }
        return avg;
    }
    
    /**
     * Returns the highest priced listing currently shown on the map.
     */
    public int getHighestPrice()
    {
        int maxPrice = 0;
        for (int i=0;i<validListings.size();i++)
        {
            if (validListings.get(i).getPrice() > maxPrice)
                maxPrice = validListings.get(i).getPrice();
        }
        return maxPrice;
    }
    
    /**
     * Returns the lowest priced listing currently shown on the map.
     */
    public int getLowestPrice()
    {
        int minPrice = 9999999;
        for (int i=0;i<validListings.size();i++)
        {
            if (validListings.get(i).getPrice() < minPrice)
                minPrice = validListings.get(i).getPrice();
        }
        return minPrice;
    }
    
    /**
     * Returns the total amount of reviews across all of the listings on the map.
     * 
     * MAY BE REDUNDANT, CAN ALWAYS REMOVE.
     */
    public int getTotalNumberOfReviews()
    {
        int total = 0;
        for (int i=0;i<validListings.size();i++)
        {
            total += validListings.get(i).getNumberOfReviews();
        }
        return total;
    }
    
    /**
     * Returns the total number of listings on the map.
     * 
     * MAY BE REDUNANT, CAN ALWAYS REMOVE.
     */
    public int getTotalNumberOfListings()
    {
        return validListings.size();
    }
    
    public ArrayList<AirbnbListing> getValidListings()
    {
        return validListings;
    }
}
