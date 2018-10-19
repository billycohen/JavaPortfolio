
/**
 * Abstract class Diseases - write a description of the class here
 *
 * @author (your name here)
 * @version (version number or date here)
 */
public abstract class Diseases
{
    // instance variables - replace the example below with your own
    private boolean has;

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public boolean hasDisease(Organism organism)
    {
        return has;
    }
    
    public abstract double getSpreadRate();
    
    public abstract double getDeathRate();
    
}
