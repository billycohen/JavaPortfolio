import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The graphical user interface for the application.
 *
 * @author Ziad Al-Dara, Alexander Bass, William Cohen, Mihnea-Andrei Radulescu, Bhanu Guntupalli
 * @version 17.03.2018
 */
public class GUI extends JFrame implements ActionListener
{
    // The application frame.
    private JFrame frame;
    // Array of prices for the user to choose a range.
    private String[] prices = { "100k", "200k", "300k", "400k", "500k", "600k", "1 million", "5 million", "10 million", "25 million"};
    // The navigation buttons for moving between the panels.
    private JButton leftButton, rightButton;
    // The card layout panel in which the welcome, map and stats panels are placed.
    private JPanel mainPanel;
    // Keeps track of the panel number. 1 = Welcome, 2 = Map, 3 = Statistics.
    private int panelNumber = 1;
    
    public GUI()
    {
        frame = new JFrame("London Property Marketplace");
        frame.setPreferredSize(new Dimension(900, 600));
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        // Create panels to be put in the content pane, set layouts and borders
        JPanel topBar = new JPanel();
        topBar.setLayout(new BorderLayout());
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new CardLayout());
        
        JPanel navigationBar = new JPanel();
        navigationBar.setLayout(new BorderLayout());
        navigationBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        
        // Add panels to main content pane
        contentPane.add(topBar, BorderLayout.NORTH);
        contentPane.add(mainPanel, BorderLayout.CENTER);
        contentPane.add(navigationBar, BorderLayout.SOUTH);
        
        // Everything for the top bar
        JPanel selection = new JPanel();
        topBar.add(selection, BorderLayout.EAST);
        selection.setLayout(new BoxLayout(selection, BoxLayout.X_AXIS));
        
        JComboBox from = new JComboBox(prices);
        JComboBox to = new JComboBox(prices);
        
        selection.add(new JLabel("From:"));
        selection.add(from);
        selection.add(new JLabel("To:"));
        selection.add(to);
        
        // Everything for the main panel
        JPanel welcomePanel = new JPanel();
        JPanel mapPanel = new JPanel();
        JPanel statsPanel = new JPanel();
        mainPanel.add(welcomePanel, "Welcome");
        mainPanel.add(mapPanel, "Map");
        mainPanel.add(statsPanel, "Statistics");
        
        // -- ZIAD - ADD EVERYTHING IN THE WELCOME PANEL TO welcomePanel
        welcomePanel.add(new JLabel("Welcome to the London Property Marketplace! To continue, please select a valid price range."));
        welcomePanel.add(new JLabel(new ImageIcon("Assignment_4/imageLibrary/welcome/image_welcomePanel.png")));
        // -- PLACE EVERYTHING IN THE MAP SCREEN HERE
        mapPanel.add(new JLabel("This shows the map"));
        // -- WILLIAM - ADD EVERYTHING IN THE STATISTICS PANEL TO statsPanel
        statsPanel.add(new JLabel("Here are the statistics"));
        
        // Everything for the navigation bar
        leftButton = new JButton("<");
        leftButton.setEnabled(false); // left button is disabled initially
        rightButton = new JButton(">");
        
        leftButton.addActionListener(this);
        rightButton.addActionListener(this);
        
        navigationBar.add(leftButton, BorderLayout.WEST);
        navigationBar.add(rightButton, BorderLayout.EAST);
        
        frame.pack();
        frame.setVisible(true);
        
        //on click ">" create new Map(from, to)
        // Map calls AirbnbDataLoader
    }
    
    public void actionPerformed(ActionEvent e)
    {   
        if (e.getSource() == leftButton){
            enableButtons();
            CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
            cardLayout.previous(mainPanel);
            --panelNumber;
            if(panelNumber == 1)
                leftButton.setEnabled(false);
        }
        
        if (e.getSource() == rightButton){
            enableButtons();
            CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
            cardLayout.next(mainPanel);
            ++panelNumber;
            if(panelNumber == 3)
                rightButton.setEnabled(false);
        }
    }
    
    private void enableButtons()
    {
        leftButton.setEnabled(true);
        rightButton.setEnabled(true);
    }
}
