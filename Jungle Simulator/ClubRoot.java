
/**
 * Write a description of class ClubRoot here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class ClubRoot extends Diseases
{
    // instance variables - replace the example below with your own
    private static final double DEATH_RATE = 0.03;
    private static final double SPREAD_RATE = 0.02;

    /**
     * Constructor for objects of class ClubRoot
     */
    public ClubRoot()
    {
        
    }
    
    public double getDeathRate()
    {
        return DEATH_RATE;
    }
    
    public double getSpreadRate()
    {
        return SPREAD_RATE;
    }
}
