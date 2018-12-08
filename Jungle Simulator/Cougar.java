import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a cougar.
 * cougar age, move, eat rabbits and foxes, hibernate and die.
 * 
 * @author Ayberk Demirkol & Billy Cohen
 * @version 22.02.2018
 */
public class Cougar extends Animals
{
    // Characteristics shared by all cougars (class variables).
    
    private static final int BREEDING_AGE = 30;
    private static final int MAX_AGE = 100;
    
    private static final double BREEDING_PROBABILITY = 0.15;
    private static final int MAX_LITTER_SIZE = 2;
    
    private static final int MAX_FOOD_VALUE = 100;
  
    // Max amount of steps it can take without eating any food.
    private static final int FOX_FOOD_VALUE = 20;  
    private static final int SNAKE_FOOD_VALUE = 30;
    private static final int JACKAL_FOOD_VALUE = 40;
    
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // Gender of cougar. Set to Male in default.
    private boolean FEMALE;
    // The cougar's age.
    private int age;
    // The cougar's food level, which is increased by eating rabbits and foxes.
    private int foodLevel;
    // The cougar's speed out of 5. 5 being slowest and 1 being fastest.
    private int speed = 1;
    /**
     * Create a cougar. A cougar can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Cougar(boolean randomAge, Field field, Location location){
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
     * This is what the cougar does most of the time: it hunts for
     * rabbit or foxes. In the process, it might breed, die of hunger,
     * or die of old age.If it's hibernating it will only
     * get hungry and older.
     * @param newCougars A list to return newly born cougars.
     */
    public void act(List<Organism> newCougars){
        incrementAge();
        incrementHunger();
        if((isAlive()) && !isHibernating()) {
            giveBirth(newCougars);            
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
     * Look for foxes and rabbits adjacent to the current location
     * Only the first live rabbit or fox is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood(){
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object organism = field.getObjectAt(where);
            
            if(organism instanceof Jackal) {
                Jackal jackal = (Jackal) organism;
                if(jackal.isAlive()) { 
                    getMalariaBy(jackal);
                    jackal.setDead();
                    foodLevel += JACKAL_FOOD_VALUE;
                    if(foodLevel > MAX_FOOD_VALUE)
                    {
                        foodLevel = MAX_FOOD_VALUE;
                    }
                    return where;
                }
                
            } else if(organism instanceof Snake) {
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
              
            } else if(organism instanceof Fox) {
                Fox fox = (Fox) organism;
                if(fox.isAlive()) {
                    getMalariaBy(fox);
                    fox.setDead();
                    foodLevel += FOX_FOOD_VALUE;
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
     * Make this cougars more hungry. This could result in the cougar's death.
     * cougar goes frenzy and moves faster if the food level is below 10.
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
     * Check whether or not this cougar is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newCougars A list to return newly born cougars.
     */
    private void giveBirth(List<Organism> newCougars){
        // New cougars are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Cougar young = new Cougar(false, field, loc);
            newCougars.add(young);
        }
    }

    /**
     * A cougar can breed if it has reached the breeding age
     * and it has a opposite gender cougar in an adjacent location.
     * If the adjacent cougar has HIV it will spread to this cougar.
     */
    private boolean canBreed(){
        if( age >= BREEDING_AGE){
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object organism = field.getObjectAt(where);
            if (organism instanceof Cougar) 
            {
                Cougar cougar = (Cougar) organism;
                if ((FEMALE != cougar.getGender()) && (cougar.getAge() >= BREEDING_AGE)) {
                    getHIVBy(cougar);
                    return true;
                }
            }
        }
        }
        return false;
    }
    
    /**
     * Increase the age. This could result in the cougar's death.
     * If the cougar has a disease, it will age faster.
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
    
    /** Makes this cougar sleep during selected hours.
     * @returns true if its sleeping
     */
    private boolean isHibernating(){
        if ((getField().getHours() >= 06) && getField().getHours() <= 21){
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
     * @returns the speed of this cougar.
     */
    public int getSpeed(){
        return speed;
    }
    
    /** 
     * Sets the gender of this cougar randomly.
     * A cougar has a 50% chance of being female.
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
     * Sets the speed of this cougar to 4.
     */
    private void getFrenzy(){
        speed = 4;
    }
    
    /** 
     * @returns the age of this cougar.
     */
    protected int getAge(){
        return age;
    }
    
    /** 
     * @returns the gender of this cougar. True if its a female.
     */
    protected boolean getGender(){
        return FEMALE;
    }
}
