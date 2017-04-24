/**
 * BlockManager class generates a board and can drop 
 * random columns and delete characters that are in groups
 * of 3 or more.  
 * 
 * @author Catherine Wright
 * CS251 Lab 9 - BlockManager
 * April 24th 2017
 */

import java.util.*;
import java.util.Random;

public class BlockManager {
    //Random class to find random numbers, seeded with 10
    //for mirrored test results
   private Random rand = new Random(10);
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
       emptyBoard();
   }
   
   /**
    * generates a new random column made of allowed characters 
    */
  protected void nextColumn() {
      colArray[0] = possibleChars[rand.nextInt(numColors)];
      colArray[1] = possibleChars[rand.nextInt(numColors)];
      colArray[2] = possibleChars[rand.nextInt(numColors)];
      
      for(int i = 0; i < 3; i++) { System.out.println(colArray[i]); }
  }
  
  /**
   * generates a new board
   * @param rows: number of rows in board[][]
   * @param cols: number of cols in board[][]
   * @param colors: int amount of how many characters to use
   */
  private void generateBoard(int rows, int cols, int colors) {
      board = new char[rows][cols];
      numColors = colors;
  }
  
  /**
   * Sets the board to empty
   */
   private void emptyBoard() {
       for(int i = 0; i < ROWS; i++) {
           for(int j = 0; j < COLS; j++) {
               board[i][j] = EMPTY;
           }
       }
       printBoard();
   }
   
   /**
    * My overridden toString() wasn't working, so instead
    * I used a stringBuilder similar to Gomoku to print a string
    * of characters to represent the board
    */
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
   
   /*@Override
   public String toString() {
       String str = " ";
       for(int i = 0; i < ROWS; i++) {
           for(int j = 0; j < COLS; j++) {
               str += board[i][j];
           }
           str += '\n';
       }
       return str;
   }*/
   
   /**
    * takes the new random column and drops it as far down
    * as allowed in the preferred column
    * @param drop is the preferred column on board[][]
    */
   protected void addNextColumn(int drop) {
       nextColumn();
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
   
   private boolean checkWin() {
       boolean flag = false;
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
       return flag;
   }
   
   /*private boolean checkVert() {
       boolean flag = false;
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
       return flag;
   }
   
   private boolean checkLDiag() {
       boolean flag = false;
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
       return flag;
   }
   
   private boolean checkRDiag() {
       boolean flag = false;
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
   }*/
   
   /**
    * @return true if any blocks will be removed
    */
   protected boolean willRemove() {
       boolean win = checkWin();
       /*boolean horiz = checkHoriz();
       boolean vert = checkVert();
       boolean lDiag = checkLDiag();
       boolean rDiag = checkRDiag();
       if(horiz || vert || lDiag || rDiag) return true;*/
       if(win) return true;
       else { 
           return false; 
       }
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
               board[sameRows.get(i)][sameCols.get(i)] = EMPTY;
               blocks++;
           }
       }
       return blocks;
   }
   
   /**
    * Checks for floating characters and moves them down
    */
   private void shiftBlocks() {
       boolean flag = true;
       while(flag) {
           flag = false;
           for(int i = 0; i < ROWS; i++) {
               for(int j = 0; j < COLS; j++) {
                   if(board[i][j] != EMPTY) {
                       if(i+1 < ROWS && board[i+1][j]== EMPTY) {
                           board[i+1][j] = board[i][j];
                           board[i][j] = EMPTY;
                           flag = true;
                       }
                   }
               }
           }
       }   
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
