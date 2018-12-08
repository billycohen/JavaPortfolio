import java.util.List;
import java.util.ArrayList;

/**
 * The area class is used to store multiple locations, which is useful when
 * events take place, as these take place on specific points of the map.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Area
{
    private static Location BL;
    private static int size;
    private static ArrayList<Location> locations;
    private static Field field;
    /**
     * Constructor for objects of class Area
     * Each area has a location BL (bottom left) along with a size which determines the boundries of
     * the area.
     * 
     * When an area is created, the addLocations() method is executed, which adds all of the locations
     * within the boundries to the locations ArrayList.
     */
    public Area(Location BL, int size, Field field)
    {
        this.BL = BL;
        this.size = size;
        this.field = field;
        locations = new ArrayList<Location>();
        setSize();
        addLocations();
    }
    
    public void setSize()
    {
        if (BL.getRow() + size > field.getWidth()) {
           size = field.getWidth() - BL.getRow();
        }
        if(BL.getCol() + size > field.getDepth()) {
            size = field.getDepth() - BL.getCol();
        }
    }
    
    /**
     * Constructor for objects of class Area
     */
    public void addLocations() 
    {
        int row = BL.getRow();
        int col = BL.getCol();
        Location location;
        while (row <= (BL.getRow() + size)) {
            while (col <= (BL.getCol() + size)) {
                location = new Location(row,col);
                locations.add(location);
                col++;
            }
            col = BL.getCol();
            row++;
        }
    }
    
    public ArrayList<Location> getLocations()
    {
        return locations;
    }
    
    public int getSize()
    {
        return locations.size();
    }
}
