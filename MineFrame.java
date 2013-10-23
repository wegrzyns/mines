package mines;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButtonMenuItem;


public class MineFrame
{
  private JFrame frame;
  private JPanel gamePanel;
  
  private JLabel statusbar;
  private Board mineBoard;
  
  private int difficulty = 1;//Use an integer to define difficulty levels
  private Timer timer;//Declare a Timer object
  private final int DELAY = 20;//Delay on the timer
  public static boolean playingGame;//Static boolean to be accessed across all classes
  
  private int height = 440, width = 377;//Default width and height for the frame
  
  private JMenu fileMenu, editMenu, viewMenu, helpMenu, highscore;
  private JMenuBar menuBar = new JMenuBar();
  private JMenuItem saveItem, loadItem, exitItem, newGameItem, pauseItem, resolveItem,
    helpItem, aboutItem, undoItem, redoItem;
  private JRadioButtonMenuItem beginnerItem, intermediateItem, expertItem, customItem;
  
  public MineFrame()
  {
    frame = new JFrame();//Create the frame for the GUI
    
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Have the applcation exit when closed
    frame.setPreferredSize(new Dimension(width, height));//Set the preferred frame size
    frame.setLocationRelativeTo(null);//Centre the frame
    frame.setTitle("Minesweeper");//Title of the frame
    
    statusbar = new JLabel("");//Set the passed in status bar
    
    gamePanel = new JPanel(new BorderLayout());//New panel that contains the board
    
    frame.setJMenuBar(buildMenuBar());//Build the menu bar and set it as the JMenuBar
    
    frame.add(gamePanel);//Add gamePanel to the frame
    startNewGame();
    
    frame.pack();//Resize the frame to occupy the smallest amount of space
    frame.setLocationRelativeTo(null); //Centres the frame
    frame.setResizable(true);//Have the frame re-sizable (useful for troubleshooting)
    frame.setVisible(true);//Show all components on the window
  }
  
  //Function to start/restart the game
  public void startNewGame()
  {
    gamePanel.removeAll();
    gamePanel.add(statusbar, BorderLayout.SOUTH);
    
    playingGame = true;//Set to true so the user may make actions
    timer = new Timer(DELAY, new TimerListener());//Initialise a timer object
    timer.start();//Start the timer
    
    gamePanel.add(new Board(statusbar, difficulty));
    frame.setPreferredSize(new Dimension(width, height));
    
                           
    frame.validate();
    frame.repaint();
    frame.pack();
  }
  
  public JMenuBar buildMenuBar()
  {
      fileMenu = new JMenu("File");
      fileMenu.setMnemonic('F');
      saveItem = new JMenuItem("Save");
      saveItem.setMnemonic('S');
      //saveItem.setMnemonic(KeyEvent.VK_S);
      saveItem.addActionListener(new SaveListener());
      loadItem = new JMenuItem("Load");
      loadItem.setMnemonic('L');
      loadItem.addActionListener(new LoadListener());
      exitItem = new JMenuItem("Exit");
      exitItem.setMnemonic('X');
      exitItem.addActionListener(new ExitListener());

      //Add items to the fileMenu
      fileMenu.add(saveItem);
      fileMenu.add(loadItem);
      fileMenu.add(exitItem);

      //Create items to add to Edit
      editMenu = new JMenu("Edit");
      editMenu.setMnemonic('d');
      undoItem = new JMenuItem("Undo");
      undoItem.setMnemonic('Z');
      undoItem.addActionListener(new undoListener());
      redoItem = new JMenuItem("Redo");
      redoItem.setMnemonic('Y');
      redoItem.addActionListener(new redoListener());

      //Add items to the editMenu
      editMenu.add(undoItem);
      editMenu.add(redoItem);

      //Create menu items to add to View
      viewMenu = new JMenu("Game");
      viewMenu.setMnemonic('G');
      pauseItem = new JMenuItem("Pause");
      pauseItem.setMnemonic('P');
      newGameItem = new JMenuItem("New Game");
      newGameItem.setMnemonic('N');
      newGameItem.addActionListener(new newGameListener());
    
    //Difficulty radio buttons
    beginnerItem = new JRadioButtonMenuItem("Beginner");
    beginnerItem.setMnemonic('B');
    beginnerItem.addActionListener(new DifficultyListener());
      
    intermediateItem = new JRadioButtonMenuItem("Intermediate", true);
    intermediateItem.setMnemonic('I');
    intermediateItem.addActionListener(new DifficultyListener());
      
    expertItem = new JRadioButtonMenuItem("Expert");
    expertItem.setMnemonic('E');
    expertItem.addActionListener(new DifficultyListener());
      
    customItem = new JRadioButtonMenuItem("Custom...");
    customItem.setMnemonic('C');
    customItem.addActionListener(new CustomGameListener());
      
    //Create a button group and add the difficulty items to it
    ButtonGroup difficultyGroup = new ButtonGroup();
    difficultyGroup.add(beginnerItem);
    difficultyGroup.add(intermediateItem);
    difficultyGroup.add(expertItem);
    difficultyGroup.add(customItem);
      
    //Add all items to viewMenu
    viewMenu.add(newGameItem);
    viewMenu.add(pauseItem);
    viewMenu.add(beginnerItem);
    viewMenu.add(intermediateItem);
    viewMenu.add(expertItem);
    viewMenu.add(customItem);
      
      
    //Create menu items to add to Help
    helpMenu = new JMenu("Help");
    helpMenu.setMnemonic('H');
    resolveItem = new JMenuItem("Solve");
    resolveItem.setMnemonic('c');
    helpItem = new JMenuItem("Help");
    helpItem.setMnemonic('?');
    aboutItem = new JMenuItem("About");
    aboutItem.setMnemonic('A');
      
    //Add all items to helpMenu
    helpMenu.add(resolveItem);
    helpMenu.add(helpItem);
    helpMenu.add(aboutItem);
    
    highscore = new JMenu("Highscore");
    highscore.setMnemonic('H');
    highscore.addMenuListener(new HighscoreListener());
      
    //Add File, View and Help to the JMenuBar
    menuBar.add(fileMenu);
    menuBar.add(editMenu);
    menuBar.add(viewMenu);
    menuBar.add(highscore);
    menuBar.add(helpMenu);
      
      return menuBar;
  }
  private class DifficultyListener implements ActionListener
    {  
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if(beginnerItem.isSelected())
        {
          difficulty = 0;
          width = 196;
          height = 258;
          startNewGame();
        }
        
        else if(intermediateItem.isSelected())
        {
          difficulty = 1;
          height = 440;
          width = 377;
          startNewGame();
        }
        
        else if(expertItem.isSelected())
        {
          difficulty = 2;
          height = 529;
          width = 466;
          startNewGame();
        }
      }
    }
  
  private class GameListener implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      if(e.getSource() == newGameItem)
      {
        startNewGame();
      }
    }
  }
  
  private class TimerListener implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      if(!pauseItem.isSelected())
      {
          playingGame = false;
          timer.stop();
      }
      if(pauseItem.isSelected())
      {
        playingGame = true;
        timer.start();
      }
    }
  } 
}