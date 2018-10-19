import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A simple model of a mouse.
 * Mouses age, move, eat plants, hibernate and die.
 * 
 * @author Ayberk Demirkol & Billy Cohen
 * @version 22.02.2018
 */
public class Mouse extends Animals
{
    private static final int BREEDING_AGE = 10;
    private static final int MAX_AGE = 25;
    
    private static final double BREEDING_PROBABILITY = 0.4;
    private static final int MAX_LITTER_SIZE = 4;
    
    // Max amount of food value mouse can have.
    private static final int MAX_FOOD_VALUE = 100;
   
    
    private static final int GRASS_FOOD_VALUE = 5;
    private static final int MUSHROOM_FOOD_VALUE = 5;
    private static final int BERRIES_FOOD_VALUE = 5;
    
    
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).   
    // Gender of Mouse. Set to Male in default.
    private boolean FEMALE;
    // The Mouse's age.
    private int age;
    // The Mouse's foodLevel which increases when it eats a plant.
    private int foodLevel;
    // The Mouse's speed out of 5. 5 being slowest and 1 being fastest.
    private int speed = 4;
    /**
     * Create a Mouse. A Mouse can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Mouse(boolean randomAge, Field field, Location location){
        super(field, location);
        setGender();
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(MAX_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = MAX_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the Mouse does most of the time: it hunts for
     * plants. In the process, it might breed, die of hunger,
     * or die of old age. If it's hibernating it will only
     * get hungry and older.
     * @param newMouses A list to return newly born foxes.
     */
    public void act(List<Organism> newMouses){
        incrementAge();
        incrementHunger();
        if((isAlive()) && !isHibernating()) {
            giveBirth(newMouses);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    /**
     * Look for plants adjacent to the current location
     * if the weather is not snowy and the mouse is not poisoned.
     * Only the first live plant is eaten. If the plant is
     * a berry, it will get poisoned, and will not be able to
     * eat for next steps.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood(){
        if(!getWeather().equals("Snowy") || !isPoisoned()){
        if(foodLevel < MAX_FOOD_VALUE -2){
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object organism = field.getObjectAt(where);
            
            if(organism instanceof Grass) {
                Grass grass = (Grass) organism;
                if(grass.isAlive()) { 
                    getMalariaBy(grass);
                    grass.setDead();
                    foodLevel += GRASS_FOOD_VALUE;
                    if(foodLevel > MAX_FOOD_VALUE)
                    {
                        foodLevel = MAX_FOOD_VALUE;
                    }
                    return where;
                }
                
            } else if(organism instanceof Mushroom) {
                Mushroom mushroom = (Mushroom) organism;
                if(mushroom.isAlive()) {
                    getMalariaBy(mushroom);
                    mushroom.setDead();
                    foodLevel += MUSHROOM_FOOD_VALUE;
                    if(foodLevel > MAX_FOOD_VALUE)
                    {
                        foodLevel = MAX_FOOD_VALUE;
                    }
                    return where;
                }
              
            } else if(organism instanceof Berries) {
                Berries berries = (Berries) organism;
                if(berries.isAlive()) {
                    getMalariaBy(berries);
                    berries.setDead();
                    foodLevel += BERRIES_FOOD_VALUE;
                    if(foodLevel > MAX_FOOD_VALUE)
                    {
                        foodLevel = MAX_FOOD_VALUE;
                    }
                    return where;
                }         
            }
        }
        }
        
        }
        return null;
    }
    
    /**
     * Make this mouse more hungry. This could result in the mouse's death.
     * Mouse goes frenzy and moves faster if the food level is below 10.
     */
    private void incrementHunger(){
        foodLevel--;
        if(foodLevel <= 10){
            getFrenzy();
        }
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this mouse is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newMouses A list to return newly born mouses.
     */
    private void giveBirth(List<Organism> newMouses){
        // New mouses are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++){
            Location loc = free.remove(0);
            Mouse young = new Mouse(false, field, loc);
            newMouses.add(young);
        }
    }
    
    /**
     * A mouse can breed if it has reached the breeding age
     * and it has a opposite gender mouse in an adjacent location.
     * If the adjacent mouse has HIV it will spread to this mouse.
     */
    protected boolean canBreed(){
        if( age >= BREEDING_AGE){
            Field field = getField();
            List<Location> adjacent = field.adjacentLocations(getLocation());
            Iterator<Location> it = adjacent.iterator();
            while(it.hasNext()) {
                Location where = it.next();
                Object organism = field.getObjectAt(where);
                if (organism instanceof Mouse) {
                Mouse mouse = (Mouse) organism;
                if ((FEMALE != mouse.getGender()) && (mouse.getAge() >= BREEDING_AGE)){
                    getHIVBy(mouse);
                    return true;
                }
                }
            }
        }
        return false;
    }
    
     /**
     * Increase the age. This could result in the mouse's death.
     * If the mouse has disease, it will age faster.
     */
    private void incrementAge(){
        if(getDisease() != null)
        {
            age++;
        }
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /** Makes the mouse sleep during selected hours.
     * @returns true if its sleeping
     */
    private boolean isHibernating(){
        if ((getField().getHours() >= 19) && getField().getHours() <= 04) {
            return true;
        }
        return false;
    }
 
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed(){
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY){
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }
    
    /** 
     * @returns the speed of this mouse.
     */
    public int getSpeed(){
        return speed;
    }
    
     /** 
     * Sets the gender of this mouse randomly.
     * A fox has a 50% chance of being female.
     */
    private void setGender(){
        double gender = rand.nextDouble();
        if (gender > 0.5) {
            FEMALE = true;
        } else {
            FEMALE = false;
        }
    }
    
    /** 
     * Sets the speed of this mouse to 1.
     */
    private void getFrenzy(){
        speed = 1;
    }
    
     /** 
     * @returns the age of this mouse.
     */
    protected int getAge(){
        return age;
    }
    
    /** 
     * @returns the gender of this fox. True if its a female.
     */
    protected boolean getGender(){
        return FEMALE;
    }
    
}
