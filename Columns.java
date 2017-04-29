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
    
    protected static final int numRows = 17;
    protected static final int numCols = 11;
    protected static final int numColors = 5;
    public static final int CELLSIZE = 30;
    public static final int drop = 5;
    protected static final int height = CELLSIZE*numRows + (1/2 * CELLSIZE);
    private boolean gamePlay = false;
    private boolean gameOver = false;
    private boolean isPaused = true;
    private boolean bonusBlocks = false;
    Timer timer;
    BlockManager blocks = new BlockManager(numRows,numCols,numColors);
    private static final String START = "New Game";
    private static final String PAUSE = "Pause";
    private static final String RESUME = "Resume";
    private static final String END = "Game Over";
    private char EMPTY = '.';
    protected JPanel blockPanel;

    public static int score = 0;
    
    public class ButtonPanel extends JPanel implements ActionListener {
        JButton pausePlay = new JButton(START);
        
        public ButtonPanel() {
            setBackground(new Color(96,96,96));
            add(pausePlay);
            pausePlay.addActionListener(this);
            pausePlay.setFont(new Font("Serif", Font.BOLD, 20));
        }
        
        public void actionPerformed(ActionEvent e) {
            isPaused = !isPaused;
            if(!gameOver) {
                if(isPaused) {
                    pausePlay.setText(RESUME);
                    timer.stop();
                }
                else {
                    if(!gamePlay) {
                        gamePlay = true;
                        blocks.nextColumn(drop);
                    }
                    pausePlay.setText(PAUSE);
                    timer.start();
                    blockPanel.requestFocusInWindow();
                }
            }
            else {
                if(isPaused) {
                    pausePlay.setText(START);
                    gamePlay = false;
                }
                else {
                    pausePlay.setText(END);
                }
            }       
        }
    }
    
    public class ScorePanel extends JPanel {
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
            Timer scoreTimer = new Timer(1000,scoreListener);
            scoreTimer.start();
        }
        
        public void updateScore(int curScore) {
            textualTextduction.setText(" " + score);
        }
    }
    
    public class EastPanel extends JPanel {
        public EastPanel() {
            setBackground(new Color(0,76,153));
            JPanel buttonPanel = new ButtonPanel();
            add(buttonPanel, BorderLayout.SOUTH);
            JPanel scorePanel = new ScorePanel();
            this.add(scorePanel, BorderLayout.SOUTH);
        }
    }
    
    public class BlockPanel extends JPanel implements KeyListener{
        int xPosition = 0;
        int yPosition = 0;
        int row, col;
        int DELAY = 800;
        char[][] board = blocks.getEmptyBoard();

        public BlockPanel() {
            setPreferredSize(new Dimension(numCols*CELLSIZE,height));
            setBackground(Color.BLACK);
            this.addKeyListener(this);
            ActionListener actionL = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(!isPaused) {
                        if(row < numRows -1) {
                            if(row == 3) bonusBlocks = false;
                            moveCurrentDown(false);
                        } else {
                            while(blocks.willRemove()) {
                                int goneBlocks = blocks.removeBlocks();
                                blocks.printBoard();
                                repaint();
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                if(goneBlocks > 3) bonusBlocks = true;
                                score += 2*goneBlocks;
                                blocks.shiftBlocks();
                                repaint();
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
           if(!isPaused) {
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
                row++;
                repaint(); 
            }
        }
        
        private int findNextRow(int column) {
            int firstRow = numRows-1;
            for(int i = 0; i < numRows-1; i++) {
                if(board[firstRow-i][column] == EMPTY) {
                    firstRow -= i;
                    break;
                }
            }
            return firstRow;
        }
        
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
          
        public void moveCurrentRight() {
            changeBlockPosition(0,1);
            repaint();
        }
        
        public void moveCurrentLeft() {
            changeBlockPosition(0,-1);
            repaint();
        }
        
        private void rotateThrough() {
            char buff = board[row][col];
            board[row][col] = board[row-2][col];
            board[row-2][col] = board[row-1][col];
            board[row-1][col] = buff;
            repaint();
        }
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for(int i = 0; i < numRows; i++) {
                for(int j = 0; j < numCols; j++) {
                    g.setColor(blocks.getColor(board[i][j]));
                    g.fillRoundRect(xPosition, yPosition, CELLSIZE, CELLSIZE, CELLSIZE/4, CELLSIZE/4);
                    xPosition += CELLSIZE;  
                }
                yPosition+= CELLSIZE;
                xPosition = 0;
            }
            yPosition = 0;
            if(bonusBlocks) {
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.BOLD, 70)); 
                g.drawString("BONUS", 40, CELLSIZE*4);
                g.drawString("BLOCKS!", CELLSIZE/5, CELLSIZE*8);
            } 
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
    
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
            JPanel eastPanel = new EastPanel();
            fralPacino.add(eastPanel, BorderLayout.SOUTH);
                
            fralPacino.getContentPane();
            fralPacino.pack();
            fralPacino.setVisible(true);
        }
    }

    /*protected boolean isGameOver() {
        char[][] board = blocks.getBoard();
        for(int i = 0; i < numCols; i++) {
            if(board[0][i] != EMPTY)  gameOver = true;
        }
        gameOver = false;
        return gameOver;
    }
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