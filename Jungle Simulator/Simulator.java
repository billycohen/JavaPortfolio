import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 210;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 140;
    // The probability that a fox will be created in any given grid position.
    
    // The probability that a rabbit will be created in any given grid position.
    private static final double MOUSE_CREATION_PROBABILITY = 0.10;
    private static final double CROW_CREATION_PROBABILITY = 0.05;
    private static final double RABBIT_CREATION_PROBABILITY = 0.04;
    private static final double FOX_CREATION_PROBABILITY = 0.04;
    private static final double SNAKE_CREATION_PROBABILITY = 0.03;
    private static final double JACKAL_CREATION_PROBABILITY = 0.02;
    private static final double COUGAR_CREATION_PROBABILITY = 0.01;
    
    private static final double MUSHROOM_CREATION_PROBABILITY = 0.03;
    private static final double BERRIES_CREATION_PROBABILITY = 0.03;
    private static final double SEEDS_CREATION_PROBABILITY = 0.03;
    private static final double GRASS_CREATION_PROBABILITY = 0.025;
    
    // List of animals in the field.
    private List<Organism> organisms;
    private Diseases diseases;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        organisms = new ArrayList<>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Rabbit.class, Color.ORANGE);
        view.setColor(Fox.class, Color.BLUE);
        view.setColor(Mouse.class, Color.RED);
        view.setColor(Grass.class, Color.GREEN);
        view.setColor(Cougar.class, Color.BLACK);
        view.setColor(Jackal.class, Color.YELLOW);
        view.setColor(Snake.class, Color.CYAN);
        view.setColor(Crow.class, Color.MAGENTA);
        view.setColor(Mushroom.class, Color.GREEN);
        view.setColor(Berries.class, Color.GREEN);
        view.setColor(Seeds.class, Color.GREEN);
        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            //delay(20);   // uncomment this to run more slowly
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;
        field.changeTime();
        if(field.getHours() == 0)
        {
           field.changeWeather();
        }
        
        // Provide space for newborn animals.
        List<Organism> newOrganisms = new ArrayList<>();        
        for(Iterator<Organism> it = organisms.iterator(); it.hasNext(); ) {
            Organism organism = it.next();
            
            if(step % organism.getSpeed() == 0)
            {
                organism.act(newOrganisms);
            }
            //To remove the animals who are dead
            if(!organism.isAlive()) {
                it.remove();
            }
        }   
        // Add the newly born foxes and rabbits to the main lists.
        organisms.addAll(newOrganisms);
        view.showStatus(step, field);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        organisms.clear();
        populate();
        
        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Fox fox = new Fox(true, field, location);
                    organisms.add(fox);
                }
                else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Rabbit rabbit = new Rabbit(true, field, location);
                    organisms.add(rabbit);
                }
                else if(rand.nextDouble() <= MOUSE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Mouse mouse = new Mouse(true, field, location);
                    organisms.add(mouse);
                }
                else if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Grass grass = new Grass(true, field, location);
                    organisms.add(grass);
                }
                else if(rand.nextDouble() <= SEEDS_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Seeds seeds = new Seeds(true, field, location);
                    organisms.add(seeds);
                }
                else if(rand.nextDouble() <= MUSHROOM_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Mushroom mushroom = new Mushroom(true, field, location);
                    organisms.add(mushroom);
                } 
                else if(rand.nextDouble() <= BERRIES_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Berries berries = new Berries(true, field, location);
                    organisms.add(berries);
                }            
                else if(rand.nextDouble() <= COUGAR_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Cougar cougar = new Cougar(true, field, location);
                    organisms.add(cougar);
                }
                else if(rand.nextDouble() <= JACKAL_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Jackal jackal = new Jackal(true, field, location);
                    organisms.add(jackal);
                }
                else if(rand.nextDouble() <= SNAKE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Snake snake = new Snake(true, field, location);
                    organisms.add(snake);
                }
                else if(rand.nextDouble() <= CROW_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Crow crow = new Crow(true, field, location);
                    organisms.add(crow);
                }
                // else leave the location empty.
            }
        }
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
}
