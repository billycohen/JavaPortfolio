import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a snake.
 * cougar age, move, eat rabbits and mouse, hibernate and die.
 * 
 * @author Ayberk Demirkol & Billy Cohen
 * @version 22.02.2018
 */
public class Snake extends Animals
{
   // Characteristics shared by all snakes (class variables).
    
   // The age at which a snake can start to breed.
   private static final int BREEDING_AGE = 30;
    private static final int MAX_AGE = 100;
    
    private static final double BREEDING_PROBABILITY = 0.2;
    private static final int MAX_LITTER_SIZE = 8;
    
    // Max amount of steps it can take without eating any food.
    private static final int MAX_FOOD_VALUE = 100;

   
   private static final int RABBIT_FOOD_VALUE = 15;
   private static final int CROW_FOOD_VALUE = 20;
    
   private static final int MUSHROOM_FOOD_VALUE = 5;

   // A shared random number generator to control breeding.
   private static final Random rand = Randomizer.getRandom();
    
   // Individual characteristics (instance fields).
   // Gender of snake. Set to Male in default.
   private boolean FEMALE;
   // The snake's age.
   private int age;
   // The snake's food level, which is increased by eating rabbits and mouses.
   private int foodLevel;
   // The snake's speed out of 5. 5 being slowest and 1 being fastest.
   private int speed = 3;
   /**
     * Create a snake. A snake can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
   public Snake(boolean randomAge, Field field, Location location){
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
     * This is what the snake does most of the time: it hunts for
     * mouses. In the process, it might breed, die of hunger,
     * or die of old age.If it's hibernating it will only
     * get hungry and older.
     * @param newSnakes A list to return newly born snakes.
     */
   public void act(List<Organism> newSnakes)
   {
       incrementAge();
       incrementHunger();
       if((isAlive()) && !isHibernating()) {
           giveBirth(newSnakes);            
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
    * Look for mouses adjacent to the current location
    * Only the first live mouse is eaten.
    * @return Where food was found, or null if it wasn't.
    */
   private Location findFood()
   {
       Field field = getField();
       List<Location> adjacent = field.adjacentLocations(getLocation());
       Iterator<Location> it = adjacent.iterator();
       while(it.hasNext()) {
            Location where = it.next();
            Object organism = field.getObjectAt(where);
            
            if(organism instanceof Rabbit) {
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
            }
        }
       return null;
   }
    
   /**
     * Make this snake more hungry. This could result in the snake's death.
     * snake goes frenzy and moves faster if the food level is below 10.
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
     * Check whether or not this snake is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newSnakes A list to return newly born snakes.
     */
   private void giveBirth(List<Organism> newSnakes)
   {
       // New foxes are born into adjacent locations.
       // Get a list of adjacent free locations.
       Field field = getField();
       List<Location> free = field.getFreeAdjacentLocations(getLocation());
       int births = breed();
       for(int b = 0; b < births && free.size() > 0; b++) {
           Location loc = free.remove(0);
           Snake young = new Snake(false, field, loc);
           newSnakes.add(young);
       }
   }

   /**
     * A jackal can breed if it has reached the breeding age
     * and it has a opposite gender jackal in an adjacent location.
     * If the adjacent jackal has HIV it will spread to this jackal.
     */
   private boolean canBreed()
   {
       if( age >= BREEDING_AGE){
           Field field = getField();
           List<Location> adjacent = field.adjacentLocations(getLocation());
           Iterator<Location> it = adjacent.iterator();
           while(it.hasNext()) {
               Location where = it.next();
               Object organism = field.getObjectAt(where);
               if (organism instanceof Snake) {
                Snake snake = (Snake) organism;
               if ((FEMALE != snake.getGender()) && (snake.getAge() >= BREEDING_AGE)) {
                    getHIVBy(snake);
                    return true;
               }
               }
           }
        }
        return false;
    }
    
   /**
     * Increase the age. This could result in the snake's death.
     * If the snake has a disease, it will age faster.
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
    
   /** Makes this snake sleep during selected hours.
    * @returns true if its sleeping
    */
   private boolean isHibernating(){
      if ((getField().getHours() >= 9) && getField().getHours() <= 20){
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
    * @returns the speed of this snake.
    */
   public int getSpeed(){
       return speed;
   }
    
   /** 
    * Sets the gender of this snake randomly.
    * A snake has a 50% chance of being female.
    */
   private void setGender(){
       double gender = rand.nextDouble();
       if (gender > 0.5) {
            FEMALE = true;
       } 
       else{
           FEMALE = false;
       }
    }
    
   /** 
    * Sets the speed of this snake to 2.
    */
   private void getFrenzy(){
       speed = 2;
   }
    
   /** 
    * @returns the age of this snake.
    */
   protected int getAge(){
       return age;
   }
    
   /** 
    * @returns the gender of this snake. True if its a female.
    */
   protected boolean getGender(){
       return FEMALE;
   }
   
}
