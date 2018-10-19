import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, eat plants, hibernate and die.
 * 
 * @author Ayberk Demirkol & Billy Cohen
 * @version 22.02.2018
 */
public class Rabbit extends Animals
{
    // Characteristics shared by all rabbits (class variables).

    private static final int BREEDING_AGE = 15;
    private static final int MAX_AGE = 100;
    
    private static final double BREEDING_PROBABILITY = 0.40;
    private static final int MAX_LITTER_SIZE = 3;
    
    // Max amount of steps it can take without eating any food.
    private static final int MAX_FOOD_VALUE = 100;
    private static final int MOUSE_FOOD_VALUE = 15;
    
    private static final int GRASS_FOOD_VALUE = 5;
    private static final int SEEDS_FOOD_VALUE = 5;
    
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // Individual characteristics (instance fields).
    
    // Gender of Rabbit. Set to Male in default.
    private boolean FEMALE;
    // The Rabbit's age.
    private int age;
    // The Rabbit's food level, which is increased by eating plants.
    private int foodLevel;
    // The Rabbit's speed out of 5. 5 being slowest and 1 being fastest.
    private int speed = 5;
    /**
     * Create a rabbit. A rabbit can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Rabbit(boolean randomAge, Field field, Location location){
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
     * This is what the rabbit does most of the time: it hunts for
     * plants. In the process, it might breed, die of hunger,
     * or die of old age.If it's hibernating it will only
     * get hungry and older. If the weather is snowy, it will age faster.
     * @param newRabbits A list to return newly born rabbits.
     */
    public void act(List<Organism> newRabbits){
        if(!getWeather().equals("Snowy")){
            age+=2;
        }
        incrementAge();
        incrementHunger();
        if((isAlive()) && !isHibernating()) {
            giveBirth(newRabbits);            
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
     * if the weather is not thunderstorm or the rabbit is not poisoned.
     * Only the first live plant is eaten.If the plant is
     * a berry, it will get poisoned, and will not be able to
     * eat for next steps.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood(){
        if(!getWeather().equals("Thunderstorm") || !isPoisoned()){
        if(foodLevel < MAX_FOOD_VALUE -2)
        {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object organism = field.getObjectAt(where);
            
            if(organism instanceof Mouse) {
                Mouse mouse = (Mouse) organism;
                if(mouse.isAlive()) {
                    getMalariaBy(mouse);
                    mouse.setDead();
                    foodLevel += MOUSE_FOOD_VALUE;
                    if(foodLevel > MAX_FOOD_VALUE)
                    {
                        foodLevel = MAX_FOOD_VALUE;
                    }
                    return where;
                }
                
            } else if(organism instanceof Grass) {
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
              
            } else if(organism instanceof Seeds) {
                Seeds seeds = (Seeds) organism;
                if(seeds.isAlive()) {
                    getMalariaBy(seeds);
                    seeds.setDead();
                    foodLevel += SEEDS_FOOD_VALUE;
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
     * Make this rabbit more hungry. This could result in the rabbit's death.
     * rabbit goes frenzy and moves faster if the food level is below 10.
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
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    private void giveBirth(List<Organism> newRabbits){
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Rabbit young = new Rabbit(false, field, loc);
            newRabbits.add(young);
        }
    }

    /**
     * A rabbit can breed if it has reached the breeding age
     * and it has a opposite gender rabbit in an adjacent location.
     * If the adjacent rabbit has HIV it will spread to this rabbit.
     */
    private boolean canBreed(){
        if( age >= BREEDING_AGE){
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object organism = field.getObjectAt(where);
            if (organism instanceof Rabbit) 
            {
                Rabbit rabbit = (Rabbit) organism;
                if ((FEMALE != rabbit.getGender()) && (rabbit.getAge() >= BREEDING_AGE)) {
                    getHIVBy(rabbit);
                    return true;
                }
            }
        }
        }
        return false;
    }
    
    /**
     * Increase the age. This could result in the rabbit's death.
     * If the rabbit has a disease, it will age faster.
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
    
    /** Makes this rabbit sleep during selected hours.
     * @returns true if its sleeping
     */
    private boolean isHibernating(){
        if ((getField().getHours() >= 06) && getField().getHours() <= 19){
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
     * @returns the speed of this rabbit.
     */
    public int getSpeed(){
        return speed;
    }
    
    /** 
     * Sets the gender of this rabbit randomly.
     * A rabbit has a 50% chance of being female.
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
     * Sets the speed of this rabbit to 1.
     */
    private void getFrenzy(){
        speed = 1;
    }
    
    /** 
     * @returns the age of this rabbit.
     */
    protected int getAge(){
        return age;
    }
    
    /** 
     * @returns the gender of this rabbit. True if its a female.
     */
    protected boolean getGender(){
        return FEMALE;
    }
}
