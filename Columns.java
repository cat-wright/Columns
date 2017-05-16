/**
 * Catherine Wright
 * Final Project CS251 Columns Game
 * April 30, 2017
 * 
 * Columns.java creates a GUI using BlockManager.java
 * to control the logic.  README.txt included in JAR.  
 */


import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class Columns extends JPanel {
    
    protected static final int numRows = 10;
    protected static final int numCols = 14;
    protected static final int numColors = 3;
    public static final int CELLSIZE = 30;
    public static final int drop = numCols/2;
    protected static final int height = CELLSIZE*(numRows-3) + (1/2 * CELLSIZE);
    private boolean gamePlay = false;
    private boolean gameOver = false;
    private boolean isPaused = true;
    private boolean bonusBlocks = false;
    Timer timer;
    BlockManager blocks = new BlockManager(numRows,numCols,numColors);
    private static final String START = "New Game";
    private static final String PAUSE = "Pause";
    private static final String RESUME = "Resume";
    private static final String END = "You Lost!";
    private char EMPTY = '.';
    protected JPanel blockPanel;
    public static int score = 0;
    
    /**
     * Inner class ButtonPanel controls the on-screen button 
     * used for pausing/resuming/game over and new game
     *
     */
    private class ButtonPanel extends JPanel implements ActionListener {
        JButton pausePlay = new JButton(START);
        
        public ButtonPanel() {
            setBackground(new Color(96,96,96));
            add(pausePlay);
            pausePlay.addActionListener(this);
            pausePlay.setFont(new Font("Serif", Font.BOLD, 20));
        }
        
        /**
         * Sets button text and starts a new game.
         */
        public void actionPerformed(ActionEvent e) {
            isPaused = !isPaused;
            if(isPaused) {
                pausePlay.setText(RESUME);
                timer.stop();
            }
            else {
                if(!gamePlay) {
                    startGame();
                }
                pausePlay.setText(PAUSE);
                timer.start();
                blockPanel.requestFocusInWindow();
            }     
        }
    }
    
    private void startGame() {
        blocks.nextColumn(drop);
        gamePlay = true;
    }
    
    /**
     * Inner class ScorePanel updates the score on the GUI
     * and checks for updates each second.  
     *
     */
    private class ScorePanel extends JPanel {
        JLabel textualTextduction = new JLabel();
        TitledBorder title = new TitledBorder("Current Score:");
        
        public ScorePanel() {
            this.add(textualTextduction);
            textualTextduction.setFont(new Font("Serif", Font.BOLD, 20));
            textualTextduction.setOpaque(true);
            textualTextduction.setBorder(title);
            textualTextduction.setPreferredSize(new Dimension(150,40));
            textualTextduction.setBackground(new Color(96,96,96));
            textualTextduction.setForeground(Color.WHITE);
            
            ActionListener scoreListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    updateScore(score);
                }
            };
            
            Timer scoreTimer = new Timer(100,scoreListener);
            scoreTimer.start();
        }
        
        public void updateScore(int curScore) {
            textualTextduction.setText(" " + score);
        }
    }
    
    /**
     * Inner class BlockPanel holds the game play in the main panel
     * and changes score, moves blocks with the timer, and listens to keys pressed.
     */
    public class BlockPanel extends JPanel implements KeyListener{
        int xPosition = 0;
        int yPosition = 0;
        int row, col;
        int bbNumber = 0;
        int DELAY = 800;
        char[][] board = blocks.getEmptyBoard();

        public BlockPanel() {
            setPreferredSize(new Dimension(numCols*CELLSIZE,height));
            setBackground(Color.RED);
            this.addKeyListener(this);
            //actionL is called by the game timer to continuously move blocks down
            ActionListener actionL = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(!isPaused) {
                        if(row < numRows -1) {
                            if(bonusBlocks)
                            {
                                bbNumber++;
                                if(bbNumber == 3)
                                {
                                    bbNumber = 0;
                                    bonusBlocks = false;
                                }
                            }
                            moveCurrentDown(false);
                        } else {
                            while(blocks.willRemove()) {
                                int goneBlocks = blocks.removeBlocks();
                                if(goneBlocks > 3) bonusBlocks = true;
                                score += 2*goneBlocks;
                                repaint();
                                blocks.shiftBlocks();
                                blocks.printBoard();
                            }
                            if(blocks.isGameOver()) 
                            {
                                gameOver = true;
                                isPaused = true;
                            }
                            blocks.nextColumn(drop);
                            row = 2;
                            col = drop;
                        }
                    }
                } 
            };
            timer = new Timer(DELAY, actionL);
            timer.start();
            row = 2;
            col = drop;
            
        }
        
        public void keyPressed(KeyEvent e) {
           if(!isPaused && !gameOver) {
                if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                    moveCurrentDown(false);
                }
                if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                    moveCurrentDown(true);
                }
                if(e.getKeyCode() == KeyEvent.VK_UP)  {
                    rotateThrough();
                }
                if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if(col < numCols-1) moveCurrentRight();
                }
                if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                    if(col > 0) moveCurrentLeft();
                }
           }
        }  
        
        /**
         * if all is true, moves the current column as far down screen as possible and ends the turn.
         * if all is false, moves the current column down by one
         * @param all
         */
        private void moveCurrentDown(boolean all) {
            if(all) {
                int nextAvailable = findNextRow(col);
                for(int i = 0; i < 3; i++) board[nextAvailable-i][col] = board[row-i][col];
                for(int i = 0; i < 3; i++) board[row-i][col] = EMPTY;
                row = numRows-1;
                repaint();
            }
            else {
                if(board[row+1][col] == EMPTY) {  
                    for(int i = 0; i < 3; i++) board[row-i+1][col] = board[row-i][col];
                    board[row-2][col] = EMPTY;
                }
                else {
                    row = numRows-2;
                }
                row++;
                repaint(); 
            }
        }
        
        //finds the next empty space in the parameter column
        private int findNextRow(int column) {
            int firstRow = numRows-1;
            for(int i = 0; i < numRows-1; i++) {
                if(board[firstRow-i][column] == EMPTY) {
                    firstRow -= i;
                    break;
                }
            }
            if(firstRow < 5) gameOver =true;
            return firstRow;
        }
        
        //swaps blocks in the called parameter direction
        private void changeBlockPosition(int dx, int dy) {
            if(col > 0 || col < numCols-1) {
                
                if(board[row][col+dy] == EMPTY) {
                    if(board[row-1][col+dy] == EMPTY) {
                        if(board[row-2][col+dy] == EMPTY) {
                            board[row][col+dy] = board[row][col];
                            board[row-1][col+dy] = board[row-1][col];
                            board[row-2][col+dy] = board[row-2][col];
                            board[row][col] = EMPTY;
                            board[row-1][col]  = EMPTY;
                            board[row-2][col]  = EMPTY;
                        }
                    }
                    col += dy;
                }
            }

        }   
          
        //moves current column to the right
        public void moveCurrentRight() {
            changeBlockPosition(0,1);
            repaint();
        }
        
        //moves current column to the left
        public void moveCurrentLeft() {
            changeBlockPosition(0,-1);
            repaint();
        }
        
        //changes column color order
        private void rotateThrough() {
            char buff = board[row][col];
            board[row][col] = board[row-2][col];
            board[row-2][col] = board[row-1][col];
            board[row-1][col] = buff;
            repaint();
        }
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for(int i = 3; i < numRows; i++) {
                for(int j = 0; j < numCols; j++) {
                    g.setColor(blocks.getColor(board[i][j]));
                    g.fillRoundRect(xPosition, yPosition, CELLSIZE, CELLSIZE, CELLSIZE/4, CELLSIZE/4);
                    xPosition += CELLSIZE;  
                }
                yPosition+= CELLSIZE;
                xPosition = 0;
            }
            yPosition = 0;
            //Displays BONUS BLOCKS when yellow or more than 3 are removed
            if(bonusBlocks) {
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.BOLD, 70)); 
                g.drawString("BONUS", (int)(0.2*numCols*CELLSIZE), CELLSIZE*4);
                g.drawString("BLOCKS!", (int)(0.1*numCols*CELLSIZE), CELLSIZE*8);
            } 
            
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
    
    /**
     * MainFrame class extends JFrame to create the main GUI.
     * fralPacino (frappacino al pacino - it was late and my brain was tired) is the  
     * game frame named "Columns".  It brings blockpanel and eastPanel onto one 
     * frame and packs/sets visible.  
     */
    public class MainFrame extends JFrame { 
        JFrame fralPacino = new JFrame("COLUMNS");
        public MainFrame() {
            fralPacino.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fralPacino.setBackground(Color.BLACK);
            fralPacino.setLayout(new BorderLayout());
            blockPanel = new BlockPanel();
            blockPanel.setFocusable(true);
            blockPanel.requestFocusInWindow();
            fralPacino.add(blockPanel, BorderLayout.CENTER);
            
            JPanel southPanel= new JPanel();
            JPanel buttonPanel = new ButtonPanel();
            JPanel scorePanel = new ScorePanel();
            
            southPanel.setBackground(new Color(0,76,153));
            southPanel.add(buttonPanel);
            southPanel.add(scorePanel);
            fralPacino.add(southPanel, BorderLayout.SOUTH);
                
            fralPacino.getContentPane();
            fralPacino.pack();
            fralPacino.setVisible(true);
        }
    }
    
    /**
     * Columns constructor calls mainframe method to build the game's frame.
     */
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