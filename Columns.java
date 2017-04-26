import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Columns extends JPanel {
    
    protected static final int numRows = 17;
    protected static final int numCols = 11;
    protected static final int numColors = 5;
    public static final int CELLSIZE = 30;
    protected static final int height = CELLSIZE*numRows + (1/2 * CELLSIZE);
    private boolean gameOver = false;
    private boolean isPaused = true;
    //protected BlockManager column;
    BlockManager blocks = new BlockManager(numRows,numCols,numColors);
    private static final String START = "New Game";
    private static final String PAUSE = "Pause";
    private static final String RESUME = "Resume";
    private static final String END = "Game Over";
    public char EMPTY = '.';

    public static int score = 0;
    
    public class ButtonPanel extends JPanel implements ActionListener {
        JButton pausePlay = new JButton(START);
        
        public ButtonPanel() {
            setBackground(Color.BLACK);
            add(pausePlay);
            pausePlay.addActionListener(this);
        }
        
        public void actionPerformed(ActionEvent e) {
            isPaused = !isPaused;
            if(!gameOver) {
                if(isPaused) pausePlay.setText(RESUME);
                else {
                    pausePlay.setText(PAUSE);
                }
            }
            else {
                if(isPaused) pausePlay.setText(START);
                else {
                    pausePlay.setText(END);
                }
            }       
        }
    }
    
    public class ScorePanel extends JPanel {
        JLabel textualTextduction = new JLabel();
        public ScorePanel() {
            this.add(textualTextduction);
            updateScore(score);
        }
        
        public void updateScore(int curScore) {
            textualTextduction.setText("Current Score: " + score);
        }
    }
    
    public class EastPanel extends JPanel {
        public EastPanel() {
            setPreferredSize(new Dimension(200, height));
            setBackground(Color.WHITE);
            JPanel buttonPanel = new ButtonPanel();
            add(buttonPanel, BorderLayout.SOUTH);
            JPanel scorePanel = new ScorePanel();
            this.add(scorePanel, BorderLayout.SOUTH);
        }
    }
    
    public class BlockPanel extends JPanel {
        int xPosition = 0;
        int yPosition = 0;
        Timer timer;
        char[][] board = blocks.getBoard();

        public BlockPanel() {
            setPreferredSize(new Dimension(numCols*CELLSIZE,height));
            setBackground(Color.LIGHT_GRAY);
        }
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for(int i = 0; i < numRows; i++) {
                for(int j = 0; j < numCols; j++) {
                    g.setColor(blocks.getColor(board[i][j]));
                    if(i == 4 && j == 2) g.setColor(Color.BLUE);
                    g.fillRoundRect(xPosition, yPosition, CELLSIZE, CELLSIZE, CELLSIZE/4, CELLSIZE/4);
                    xPosition += CELLSIZE;  
                }
                yPosition+= CELLSIZE;
                xPosition = 0;
            }
        }   
    }
    
    public class MainFrame extends JFrame { 
        JFrame fralPacino = new JFrame("COLUMNS BIATCH");
        public MainFrame() {
            fralPacino.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fralPacino.setBackground(Color.BLUE);
            fralPacino.setLayout(new BorderLayout());
                   
            //column = new BlockManager(numRows,numCols,5);
            JPanel blockPanel = new BlockPanel();
            fralPacino.add(blockPanel, BorderLayout.WEST);
            JPanel eastPanel = new EastPanel();
            fralPacino.add(eastPanel, BorderLayout.EAST);
            
            
            fralPacino.getContentPane();
            fralPacino.pack();
            fralPacino.setVisible(true);
        }
    }

    protected boolean isGameOver() {
        char[][] board = blocks.getBoard();
        for(int i = 0; i < numRows; i++) {
            if(board[i][0] != EMPTY)  gameOver = true;
        }
        gameOver = false;
        return gameOver;
    }
    
    private Columns() {
        @SuppressWarnings("unused")
        MainFrame frame = new MainFrame();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                @SuppressWarnings("unused")
                Columns yerDone = new Columns();
            }
        });
    }
}
