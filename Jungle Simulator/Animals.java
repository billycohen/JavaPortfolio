import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Animals extends Organism
{
    // Whether the animal is alive or not.
    private boolean alive;
    private boolean disease;
    private boolean poison;
    // The animal's field.
    private Field field;
    private static final Random rand = Randomizer.getRandom();
    // The animal's position in the field.
    private Location location;
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animals(Field field, Location location)
    {
       super(field, location);
       getAnimalSick();
    }
    
    protected boolean isPoisoned()
    {
        return poison;
    }
    
    protected void getPoisoned()
    {
        poison = true;
    }
    
    protected void getMalariaBy(Organism organisms)
    {
        if(organisms.getDisease() != null){
           Diseases disease = organisms.getDisease();
           double spreadRate = rand.nextDouble();
           if(disease instanceof Malaria && spreadRate < disease.getSpreadRate())
           {
           getSick(disease);
           }
        }
    }
    
    protected void getHIVBy(Organism organisms)
    {
        if(organisms.getDisease() != null){
           Diseases disease = organisms.getDisease();
           double spreadRate = rand.nextDouble();
           if(disease instanceof HIV && spreadRate < disease.getSpreadRate())
           {
           getSick(disease);
           }
        }
    }
    
    public void getSick(Diseases disease)
    {
        super.getSick(disease);
    }
    
        private void getAnimalSick(){
        double possibilityMalaria = rand.nextDouble();
        double possibilityHIV = rand.nextDouble();
        if(possibilityMalaria < MALARIA_CREATION_PROBABILITY)
        {
            Malaria malaria = new Malaria();
            getSick(malaria);
        }
        else if(possibilityHIV < MALARIA_CREATION_PROBABILITY)
        {
            HIV hiv = new HIV();
            getSick(hiv);
        }
    }
    
    protected Diseases getDisease()
    {
        return super.getDisease();
    }
    
    abstract protected boolean getGender();
    
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    
    abstract public void act(List<Organism> newAnimals);
    
    //public void getPoisened();
}
