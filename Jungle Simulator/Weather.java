import java.util.ArrayList;
import java.util.Random;
/**
 * The weather class handles the weather objects which are periodically created as the steps of the simulation increment.
 * 
 * Weather has various effects on the field and the animals within the field. Some animals do better in harsh conditions where others struggle.
 */
public class Weather
{
    private static final Random rand = Randomizer.getRandom();
    
    private final static int MAX_WEATHER_DAY = 4;
    
    

    private int time;
    private String weather;
    private Field field;
    ArrayList<String> weathers = new ArrayList <String>();
    
    public Weather(int duration, String type)
    {
        time = duration;
        weather = type;
        weathers.add("Snowy");
        weathers.add("Rainy");
        weathers.add("Sunny");
        weathers.add("Cloudy");
        weathers.add("Windy");
        weathers.add("Thunderstorm");
    }
    
    protected void act()
    {
        time --;
    }
    
    public String getType()
    {
    return weather;
    }
    
    public int getTime()
    {
        return time;
    }
    
    public int getMaxTime()
    {
        return MAX_WEATHER_DAY;
    }
    
    public String getRandomWeather()
    {
        return weathers.get(rand.nextInt(weathers.size()));
    }
}
