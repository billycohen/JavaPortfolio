
/**
 * Write a description of class HIV here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class HIV extends Diseases
{
    private static final double DEATH_RATE = 0.11;
    private static final double SPREAD_RATE = 0.01;
    public HIV()
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
