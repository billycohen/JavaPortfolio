
public class Time
{
    private int hours;
    private int minutes;
    private int hoursLimit;
    private int minutesLimit;
    public Time(int hour, int minute)
    {
        this.hours = hour;
        this.minutes = minute;
    }
    
    public int getHour()
    {
        return hours;
    }
    
    public int getMinute()
    {
        return minutes;
    }
    
    public void incrementHour()
    {
        hours ++;
        if(hours >= 24)
        {
        hours %= 24;
        }
    }
    
    public void incrementMinute()
    {
        minutes += 30;
        if(minutes >= 60)
        {
            minutes %= 60;
        }
    }
}
