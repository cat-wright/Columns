/**
 * BlockManager class generates a board and can drop 
 * random columns and delete characters that are in groups
 * of 3 or more.  
 * 
 * @author Catherine Wright
 * CS251 Lab 9 - BlockManager
 * April 24th 2017
 */

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.Random;

public class BlockManager {
    //Random class to find random numbers, seeded with 10
    //for mirrored test results
   private Random rand = new Random();
   //private boolean isPlaying = false;
   //private boolean gameOver = false;

   private char[][] board;
   private char[] colArray = new char[3];
   private int numColors;
   protected int ROWS;
   protected int COLS;
   private StringBuilder bStr;
   
   //Stores coordinates of matching characters
   protected ArrayList<Integer> sameRows = new ArrayList<>();
   protected ArrayList<Integer> sameCols = new ArrayList<>();
  
   private char EMPTY = '.';
   private char[] possibleChars = {'*', '#', '$','&','@'};
   
   /**
    * Constructor with parameters to generate an empty board[][]
    * @param rows: number of rows in board[][], set to global ROWS
    * @param cols: number of cols in board[][], set to global COLS
    * @param numcolors: number of characters allowed to use
    */
   protected BlockManager(int rows, int cols, int numcolors) {
       this.COLS = cols;
       this.ROWS = rows;
       generateBoard(rows, cols, numcolors);
       //emptyBoard();
   }

   /**
    * generates a new random column made of allowed characters 
    */
  protected void nextColumn(int drop) {
      colArray[0] = possibleChars[rand.nextInt(numColors)];
      colArray[1] = possibleChars[rand.nextInt(numColors)];
      colArray[2] = possibleChars[rand.nextInt(numColors)];
      
      //for(int i = 0; i < 3; i++) { System.out.println(colArray[i]); }
      board[0][drop] = colArray[0];
      board[1][drop] = colArray[1];
      board[2][drop] = colArray[2];
      //printBoard();
      //return colArray;
  }
  
  protected Color getColor(char c) {
      if(c == '.') return new Color(224,224,224);
      if(c == '*') return new Color(128,128,128);
      if(c == '#') return new Color(192,192,192);
      if(c == '$') return new Color(255,255,0);
      if(c == '&') return new Color(160,160,160);
      if(c == '@') return new Color(96,96,96);
      return Color.WHITE;
  }
  
  /**
   * generates a new board
   * @param rows: number of rows in board[][]
   * @param cols: number of cols in board[][]
   * @param colors: int amount of how many characters to use
   */
  protected void generateBoard(int rows, int cols, int colors) {
      board = new char[rows][cols];
      numColors = colors;
  }
  
  /**
   * Sets the board to empty
   */
   protected void emptyBoard() {
       for(int i = 0; i < ROWS; i++) {
           for(int j = 0; j < COLS; j++) {
               board[i][j] = EMPTY;
           }
       }
       //printBoard();
   }
   
//printBoard() method using StringBuilder class
   protected void printBoard() {
       StringBuilder sB = new StringBuilder();
       for(int i = 0; i < ROWS; i++) {
           for(int j = 0; j < COLS; j++) {
               sB.append(board[i][j]);
           }
           sB.append('\n');
       }
       bStr = sB;
       System.out.println(bStr);
   }
   
   protected char[][] getBoard() {
       return board;
   }
   
   protected char[][] getEmptyBoard() {
       emptyBoard();
       return board;
   }
   
   /**
    * takes the new random column and drops it as far down
    * as allowed in the preferred column
    * @param drop is the preferred column on board[][]
    */
   protected void addNextColumn(int drop) {
       int firstRow = 0;
       for(int i = 1; i <= ROWS; i++) {
           if(board[ROWS-i][drop] == EMPTY) {
               firstRow = ROWS - i;
               break;
           }
       }
       board[firstRow-2][drop] = colArray[0];
       board[firstRow-1][drop] = colArray[1];
       board[firstRow][drop] = colArray[2];
   }
   
   /**
    * checks horizontal, vertical, and diagonals for 3 or more in a row
    * @return boolean returned true if there is at least one matching set
    * 
    */
   private boolean checkWin() {
       boolean flag = false;
       //
       for(int i = 0; i < ROWS; i++) {
           for(int j = 0; j < COLS-2; j++) {
               if(board[i][j] != EMPTY) {
                   if(board[i][j] == board[i][j+1] && board[i][j+1] == board[i][j+2]) {
                       sameRows.add(i); sameRows.add(i); sameRows.add(i);
                       sameCols.add(j); sameCols.add(j+1); sameCols.add(j+2);
                       flag = true;
                   }
               }
           }
       }
       for(int j = 0; j < COLS; j++) {
           for(int i = 0; i < ROWS-2; i++) {
               if(board[i][j] != EMPTY) {
                   if(board[i][j] == board[i+1][j] && board[i+1][j] == board[i+2][j]) {
                       sameRows.add(i); sameRows.add(i+1); sameRows.add(i+2); 
                       sameCols.add(j);  sameCols.add(j);  sameCols.add(j); 
                       flag = true;
                   }
               }
           }
       }
       for(int i = 0; i < ROWS-2; i++) {
           for(int j = 0; j < COLS-2; j++) {
               if(board[i][j] != EMPTY) {
                   if(board[i][j] == board[i+1][j+1] && board[i+1][j+1] == board[i+2][j+2]) {
                       sameRows.add(i); sameRows.add(i+1); sameRows.add(i+2);
                       sameCols.add(j); sameCols.add(j+1); sameCols.add(j+2);
                       flag = true;
                   }
               }
           }
       }
       for(int i = ROWS-1; i >= 1 ; i--) {
           for(int j = 0; j < COLS-2; j++) {
               if(board[i][j] != EMPTY) {
                   if(board[i][j] == board[i-1][j+1] && board[i-1][j+1] == board[i-2][j+2]) {
                       sameRows.add(i); sameRows.add(i-1); sameRows.add(i-2);
                       sameCols.add(j); sameCols.add(j+1); sameCols.add(j+2);
                       flag = true;
                   }
               }
           }
       }
       return flag;
   }
   
   //REFACTOR YOUR WIN SEARCH
   
   /**
    * @return true if any blocks will be removed
    */
   protected boolean willRemove() {
       boolean win = checkWin();
       if(win) return true;
       else { 
           return false; 
       }
   }
   
   protected boolean inBoard(int x, int y) {
       for(int i=0; i<ROWS; i++) {
           for(int j = 0; j < COLS; j++) {
               if(x == i && y == j) {
                   return true;
               }
           }
       }
       return false;
   }
   
   /**
    * Uses created sameRows[] and sameCols[] arrays to 
    * set marked coordinates of board to EMPTY
    * @return the number of blocks removed, stored globally
    */
   protected int removeBlocks() {
       int blocks = 0;
       for(int i = 0; i < sameRows.size(); i++) {
           if(board[sameRows.get(i)][sameCols.get(i)] != EMPTY) {
               if(board[sameRows.get(i)][sameCols.get(i)] == '$') blocks++;
               board[sameRows.get(i)][sameCols.get(i)] = EMPTY;
               blocks++;
           }
       }
       sameRows.clear(); sameCols.clear();
       return blocks;
   }
   
   protected char getPieceAt(int x, int y) {
       return board[x][y];
   }
   
   /**
    * Checks for floating characters and moves them down
    */
   protected void shiftBlocks() {
       boolean flag = true;
       while(flag) {
           flag = false;
           for(int i = 0; i < ROWS; i++) {
               for(int j = 0; j < COLS; j++) {
                   if(board[i][j] != EMPTY) {
                       if(i+1 < ROWS && board[i+1][j] == EMPTY) {
                           board[i+1][j] = board[i][j];
                           board[i][j] = EMPTY;
                           flag = true;
                       }
                   }
               }
           }
       }   
   }
   
   protected boolean isGameOver() {
       for(int i = 0; i < COLS; i++)
       {
           if(board[3][i] != EMPTY)
           {
               System.out.println("GAME OVER FLAG");
               return true;
           }
       }
       return false;
   }
   
   /**Pseudo-main function called in BlockManagerTest
    * @param drop - which column to drop current column in
    * adds a new column, checks for
    * characters to delete, and move floating characters down.  
    */
   protected void printValues(int drop) {
       System.out.println("dropping piece");
       addNextColumn(drop);
       System.out.println("in column " + drop);
       printBoard();
       while(willRemove()) {
           int goneBlocks = removeBlocks();
           System.out.println(goneBlocks + " blocks will be removed");
           sameRows.clear(); sameCols.clear();
           shiftBlocks();
           printBoard();
       }
       System.out.println("drop complete");
   }
}
