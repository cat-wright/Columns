**README.txt**********************************
* Columns.java and BlockManager.java         *
* Catherine Wright                           *
* April 29th, 2017                           *
**********************************************

GAME PLAY:
	Button controls new game, pause, and resume.
	Use the arrow keys and space bar to control the falling blocks.
	Right, left, and down arrows make the columns move respectively.  
	Up arrow changes the order of the blocks in the column.
	Space bar sends the entire column to the bottom of the board.

	The game is in greyscale, making it slightly more challenging to see matching blocks in advance.
	There are bonus blocks - colored yellow.

SCORING:
	Matching three or more blocks that are not bonus blocks will earn 2x the number of removed blocks. 
	Matching three blocks that are bonus blocks will earn 12 points (always double of non bonus blocks).
	Matching more than three grey blocks or three bonus blocks will send the message "BONUS BLOCKS" to the screen.  

CLASSES:
	Class BlockManager contains all game logic, including creating an empty board, generating a random
		column, adding it to the bottom of the board, and removing/shifting winning blocks.
	Class Columns holds all other classes, and creates the GUI in the main frame.  
	Class ButtonPanel contains the button and its actionlistener, that listens to the rest of the game and 
		changes the text of the button based on whether the game is paused or not.  
	Class ScorePanel contains the JLabel for the score, including a border with text.  It contains an 			actionlistener that updates the score based on whether blocks have been removed. 
	Class EastPanel is not east (it was originally), but it is the south panel of the game, and it contains 
		the score panel and the button panel in one.  
	Class BlockPanel contains the gameplay.  It has a timer that uses an actionlistener to move blocks down, 	 as well as a key listener to respond to key pressed.  It sets the score value and returns when the 
	    game is over.  When bonus blocks are scored or the game is over it sets text across the screen.
	Class MainFrame is the JFrame that holds the game together, with the BlockPanel and the EastPanel in a 
		border layout, and displays the GUI on the screen.

ALGORITHM DETAIL:
	- To generate random pieces I used Random class, and picked a random number between 0 and the max number of colors.  I then used the random number as an index of my array of colors, and set random colors to an array of 3 characters, which represented my new column.
	- My moving/placing pieces algorithm is elementary, yet for the most part works.  Each time a new column is dropped, the board keeps place of one board coordinate, at board[row][col], such that board[row][col] is the character of the bottom block of the new column.  To move pieces, I would swap board[row][col], as well as board[row-1][col] and board[row-2][col] with blocks in the direction I wish to travel.  Then i set the original coordinates to empty and updated row and col.
	- To detect three or more blocks in a row, I created 2 array lists, one for row numbers and one for column numbers.  I then went through the board each direction and if two were matching, I added their row coordinates and col coordinates to the array lists.  In the end, the coordinates of each block to be removed were in corresponding array list row[i] and col[i] places.  
	- To remove the same blocks I went through the array lists and at each coordinate I changed the board character to EMPTY.  
	- To shift blocks I used a while loop to go through the board over and over, each time checking if a board coordinate is not empty, and checking if the board coordinate below it is empty.  If it is, it switches the blocks.  The entire function of looking for three, removing, and shifting were inside a while loop that would check until no winning rows were found.  
	- I never got my endOfGame flag to be bug-free, but I checked by every time a new column was about to be dropped, if the row was at the top, a endOfGame flag was marked true.  My timer running in the ButtonPanel would check for this flag every second, and if true, it paused the game and displayed GAME OVER on the board.    


KNOWN BUGS AND NEW FEATURES:

	Game over only registers when it feels like it, also if space is hit too far down screen
	the block may drop too far.  

	The biggest bug in my Columns game is to do with block movement.  
	If arrow keys are hit sporadically, some blocks that should not be able to move, move.

	My next step would be to set an "End of move" flag to signify when one column is done moving.  
	This would fix my game over bug and my key/row movement bug, because it would no longer try to 
	move blocks that should no longer be moved.  


