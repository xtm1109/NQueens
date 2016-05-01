//import edu.colorado.collections.*;

/** Solve the n - queens problem */
public class NQueens {

	// used to indicate an empty square or a square containing a queen
	private enum Square {EMPTY, QUEEN};

	private static int boardsize = 5;   
	private Square board[][]; // chess board
	private StackInterface<Queen> stack;

	/**
	 * Constructor: Creates an empty BOARD_SIZE x BOARD_SIZE square board.
	 */
	public NQueens() {
		board = new Square[boardsize][boardsize];
		clearBoard();
		stack = new ArrayStack<Queen>(boardsize);
	}         

	/**
	 * Constructor: Creates an empty nxn square board.
	 */
	public NQueens(int n) {
		boardsize = n;
		board = new Square[boardsize][boardsize];
		clearBoard();
		stack = new ArrayStack<Queen>(boardsize);
	}  

	/** 
	 * Clears the board.
	 * Precondition: None.
	 * Postcondition: Sets all squares to EMPTY.
	 */
	public void clearBoard() {
		for (int i = 0; i < boardsize; i++) {
			for (int j = 0; j < boardsize; j++) {
				board[i][j] = Square.EMPTY;
			}
		}

	}  // end clearBoard

	/**
	 * Displays the board.
	 * Precondition: None.
	 * Postcondition: Board is written to standard 
	 * output; zero is an EMPTY square, one is a square 
	 * containing a queen (QUEEN).
	 */
	public void displayBoard() {
		for (int i = 0; i < boardsize; i++) {
			for (int j = 0; j < boardsize; j++) {
				if (board[i][j] == Square.EMPTY)
					System.out.print("0");
				else
					System.out.print("1");
			}
			System.out.println();
		}

	} // end displayBoard

	/** Solves Eight Queens problem.  
	 * Postcondition: If a solution is found, each 
	 * row of the board contains one queen and method 
	 * returns true; otherwise, returns false (no 
	 * solution exists for a queen anywhere in row 
	 * specified) and the entire board should be empty. 
	 */
	public boolean solve() {
		boolean success = false;
		Queen top;

		Queen guess = new Queen(0,0);

		push(guess);

		while (!success && !stack.isEmpty()) {
			if (isConflict()) {
				top = (Queen) stack.peek();
				
				while ((top.col == boardsize-1)) {
					pop();

					if (stack.isEmpty())
						break;
					else
						top = (Queen) stack.peek();
					}
				
				if (!stack.isEmpty()) {
					guess = pop();
					guess.col++;
					push(guess);
				}
			}
			else if (!(isConflict()) && (stack.size() == boardsize)){
				success = true;
			}
			else {
				guess = new Queen(stack.size(), 0);

				push(guess);
			}
		}

		/*  pseudocode from textbook (corrected for top left corner is 0,0)

		     push information onto the stack indicating the
		     first choice is a queen in row 0, column 0.

		  while (! success && !s.isEmpty())
		  {
		   Check whether the most recent choice (on the top of the stack)
		   is in the same row, same column, or same diagonal as any other 
		   choices (on the board). If so, we say there is a conflict. 
		   Otherwise, there is no conflict.

		   if (there is a conflict)
		     Pop items off the stack until stack becomes empty or the top of
		     the stack is a choice that is not in column (n-1).  If the stack 
		     is now not empty, increase the column number of the top choice by 1.

		   else if (no conflict and the stack size is n)
		     Set success to true because we have a solution to the n-queens problem.
		   else
		     Push information onto the stack indicating that the next choice is to place
		     a queen at the row number s.size() and column number 0.
		  }
		 */


		if (success)
			return true; 
		else {
			clearBoard(); 
			return false; // no solution to the game
		}
	}

	// --------------------------------------------------
	// Sets a queen at square indicated by row and 
	// column.
	// Precondition: None.
	// Postcondition: Sets the square on the board in a 
	// given row and column to QUEEN and adds a 
	// new queen to the stack.
	// --------------------------------------------------
	private void push(Queen q) {
		stack.push(q);
		board[q.row][q.col] = Square.QUEEN;
		// for debugging, you may wish to display board every time you add a queen
		//		displayBoard();
	}  // end push

	// --------------------------------------------------
	// Removes a queen at square indicated by row and
	// column.
	// Precondition: None.
	// Postcondition: Pops a queen off the stack and 
	// sets the square on the board in the 
	// given row and column to EMPTY.
	// --------------------------------------------------
	private Queen pop() {
		// To be implemented 
		Queen q = stack.pop();
		board[q.row][q.col]= Square.EMPTY;
		return q;
	}  // end pop

	// --------------------------------------------------
	// Determines whether the square on the board at a 
	// given row and column is under attack by any queens 
	// in the row 0 through row-1.
	// Precondition: Each row between 0 and row-1 
	// has a queen placed in a square at a specific column. 
	// None of these queens can be attacked by any other
	// queen.
	// Postcondition: If the designated square is under 
	// attack, returns true; otherwise, returns false.
	// --------------------------------------------------
	private boolean isConflict() {
		// get the current guess
		Queen guess = stack.peek();

		// check every row about this row for:
		// a queen in the same column as the guess
		if (!isColumnConflict(guess))

			// a queen to left-diagonal
			if (!isLeftDiagonalConflict(guess))

				// a queen to right-diagonal
				if (!isRightDiagonalConflict(guess))
					return false;  // no conflicts anywhere

		// conflict somewhere
		return true;
	}  // end isConflict

	// returns true if there is a queen in the same column, false otherwise
	// (above the current Queen)
	private boolean isColumnConflict(Queen current){ 
		for (int i = 0; i < current.row; i++) {
			if (board[i][current.col] == Square.QUEEN)
				return true;
		}
		return false;
	}

	// returns true if there is a queen in the left diagonal, false otherwise 
	// (above the current Queen)	
	private boolean isLeftDiagonalConflict(Queen current){
		int i = current.row-1;
		int j = current.col-1;

		while ((i >= 0) && (j >= 0)) {
			if (board[i][j] == Square.QUEEN)
				return true;
			i--;
			j--;
		} 
		return false;
	}	

	// returns true if there is a queen in the right diagonal, false otherwise
	// (above the current Queen)
	private boolean isRightDiagonalConflict(Queen current){
		int i = current.row-1;
		int j = current.col+1;

		while ((i >= 0) && (j < boardsize)) {
			if (board[i][j] == Square.QUEEN)
				return true;
			i--;
			j++;
		}

		return false;
	}	


	// inner class that represents a single queen's position 
	private class Queen {
		private int row;
		private int col;

		private Queen(int r, int c) {
			row = r;
			col = c;
		}
	}

	// good for final test - runs all boards sizes 3-8
	public static void solveForAllBoards() {
		for (int n=3; n<9; n++) {
			NQueens q = new NQueens(n);
			if (q.solve())
				q.displayBoard();
			else
				System.out.println("no solution for board size " + n);
			System.out.println();
		}
	}

	// good for debugging - runs solution for one board
	public static void solveForOneBoard(int n) {
		NQueens q = new NQueens(n);
		if (q.solve())
			q.displayBoard();
		else
			System.out.println("no solution for board size " + n);
	}

	/** Main method, use solveForAllBoards to solve for all solutions. */
	public static void main(String[] args) {
		//				solveForOneBoard(2);     
		solveForAllBoards();
		//		NQueens board5 = new NQueens();

		//		Queen test = new Queen(0,0);

		//		board5.push(test);

		//		board5.displayBoard();
	}  
} // end NQueens