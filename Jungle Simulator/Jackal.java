import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a jackal.
 * cougar age, move, eat rabbits and mouse, hibernate and die.
 * 
 * @author Ayberk Demirkol & Billy Cohen
 * @version 22.02.2018
 */
public class Jackal extends Animals
{
    // Characteristics shared by all jackals (class variables).
    
     private static final int BREEDING_AGE = 50;
    private static final int MAX_AGE = 100;
    
    private static final double BREEDING_PROBABILITY = 0.25;
    private static final int MAX_LITTER_SIZE = 6;
    
    // Max amount of steps it can take without eating any food.
    private static final int MAX_FOOD_VALUE = 100;

    // Food value of a single rabbit.
    private static final int RABBIT_FOOD_VALUE = 15;
    private static final int CROW_FOOD_VALUE = 20;
    private static final int SNAKE_FOOD_VALUE = 30;

    private static final int MUSHROOM_FOOD_VALUE = 5;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // Gender of jackal. Set to Male in default.
    private boolean FEMALE;
    // The jackal's age.
    private int age;
    // The jackal's food level, which is increased by eating rabbits and mouses.
    private int foodLevel;
    // The jackal's speed out of 5. 5 being slowest and 1 being fastest.
    private int speed = 3;
    /**
     * Create a jackal. A jackal can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Jackal(boolean randomAge, Field field, Location location){
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
     * This is what the jackal does most of the time: it hunts for
     * rabbit or mouses. In the process, it might breed, die of hunger,
     * or die of old age.If it's hibernating it will only
     * get hungry and older.
     * @param newJackals A list to return newly born jackals.
     */
    public void act(List<Organism> newJackals){
        incrementAge();
        incrementHunger();
        if((isAlive()) && !isHibernating()) {
            giveBirth(newJackals);            
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
     * Look for mouses and rabbits adjacent to the current location
     * Only the first live rabbit or mouse is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood(){
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object organism = field.getObjectAt(where);
            
            if(organism instanceof Snake) {
                Snake snake = (Snake) organism;
                if(snake.isAlive()) { 
                    getMalariaBy(snake);
                    snake.setDead();
                    foodLevel += SNAKE_FOOD_VALUE;
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
                
            } else if(organism instanceof Crow) {
                Crow crow = (Crow) organism;
                if(crow.isAlive()) {
                    getMalariaBy(crow);
                    crow.setDead();
                    foodLevel += CROW_FOOD_VALUE;
                    if(foodLevel > MAX_FOOD_VALUE)
                    {
                        foodLevel = MAX_FOOD_VALUE;
                    }
                    return where;
                }
              
            } else if(organism instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) organism;
                if(rabbit.isAlive()) {
                    getMalariaBy(rabbit);
                    rabbit.setDead();
                    foodLevel += RABBIT_FOOD_VALUE;
                    if(foodLevel > MAX_FOOD_VALUE)
                    {
                        foodLevel = MAX_FOOD_VALUE;
                    }
                    return where;
                }         
            }
        }
        return null;
    }
    
    /**
     * Make this jackal more hungry. This could result in the jackal's death.
     * jackal goes frenzy and moves faster if the food level is below 10.
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
     * Check whether or not this jackal is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newJackals A list to return newly born jackals.
     */
    private void giveBirth(List<Organism> newJackals){
        // New jackals are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Jackal young = new Jackal(false, field, loc);
            newJackals.add(young);
        }
    }

    /**
     * A jackal can breed if it has reached the breeding age
     * and it has a opposite gender jackal in an adjacent location.
     * If the adjacent jackal has HIV it will spread to this jackal.
     */
    private boolean canBreed(){
        if( age >= BREEDING_AGE){
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object organism = field.getObjectAt(where);
            if (organism instanceof Jackal) 
            {
                Jackal jackal = (Jackal) organism;
                if ((FEMALE != jackal.getGender()) && (jackal.getAge() >= BREEDING_AGE)) {
                    getHIVBy(jackal);
                    return true;
                }
            }
        }
        }
        return false;
    }
    
    /**
     * Increase the age. This could result in the jackal's death.
     * If the jackal has a disease, it will age faster.
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
    
    /** Makes this jackal sleep during selected hours.
     * @returns true if its sleeping
     */
    private boolean isHibernating(){
        if ((getField().getHours() >= 05) && getField().getHours() <= 23){
            return false;
        }
        return true;
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed(){
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }
    
    /** 
     * @returns the speed of this jackal.
     */
    public int getSpeed(){
        return speed;
    }
    
    /** 
     * Sets the gender of this jackal randomly.
     * A jackal has a 50% chance of being female.
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
     * Sets the speed of this jackal to 3.
     */
    private void getFrenzy(){
        speed = 3;
    }
    
    /** 
     * @returns the age of this jackal.
     */
    protected int getAge(){
        return age;
    }
    
    /** 
     * @returns the gender of this jackal. True if its a female.
     */
    protected boolean getGender(){
        return FEMALE;
    }
}
