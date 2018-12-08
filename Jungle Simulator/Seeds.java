import java.util.List;
import java.util.Random;
/**
 * Write a description of class Seeds here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Seeds extends Plants
{
    // instance variables - replace the example below with your own
    private static final int MAX_AGE = 20;
    // The likelihood of a rabbit breeding.
    private double BREEDING_PROBABILITY = 0.03;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    private static final int BREEDING_AGE = 15;
    private static final int MAX_LENGTH = 45;
    // Individual characteristics (instance fields).
    
    // The rabbit's age.
    private int age;
    private int length;
    /**
     * Constructor for objects of class Seeds
     */
    public Seeds(boolean randomAge, Field field, Location location)
    {
        // initialise instance variables
        super(field, location);
        age = 0;
        length = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            length = rand.nextInt(MAX_LENGTH);
        }
    }

    public void act(List<Organism> newSeeds)
    {
        if (isAlive() && !isHibernating()) {
            if(getWeather().equals("Rainy")){
               BREEDING_PROBABILITY = 0.15;
            }
            else if(getWeather().equals("Windy")){
                double deathPossibility = rand.nextDouble();
                if(deathPossibility >= 80)
                {
                    setDeadAge();
                }
            }
            else{
               BREEDING_PROBABILITY = 0.06;
            }
            incrementAge();
            if(isAlive()) {
                giveBirth(newSeeds);
            }  
        }
    }
    
    private boolean isHibernating()
    {
        if ((getField().getHours() >= 05) && getField().getHours() <= 22) {
            return false;
        }
        return true;
    }

    
    private void incrementAge()
    {
        if(getDisease() != null)
        {
            age++;
        }
        age++;
        length += 4;
        if(age > MAX_AGE) {
            setDeadAge();
        }
        if(length >= MAX_LENGTH) {
            length = MAX_LENGTH;
        }
    }
    
    protected void setDead()
    {
       length -= 5;
       if(length < 0)
       {
           super.setDead();
       }
    }
    
    protected void setDeadAge()
    {
        super.setDead();
    }
    
    
    private void giveBirth(List<Organism> newPlant)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
           Location loc = free.remove(0);
           Grass young = new Grass(false, field, loc);
           newPlant.add(young);
        }
    }
    
    public int getSpeed()
    {
        return super.getSpeed();
    }
    
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }
    
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}

