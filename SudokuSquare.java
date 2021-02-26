/*
	Independent project by Curtis Godwin

	July 2017

	This class implements the SudokuSquare object. Each of the 81 squares in the 9x9 puzzle are objects of this type.
	The class consists only of private variables, a constructor, and getters and setters for the variables.

	The main points of the class are:
			- to hold a value between 1 and 9 pertaining to the larger puzzle
			- to hold the coordinates of the square that was previously processed

	There are also the booleans 'revisited' and 'known' which indicate special cases to be treated in specific ways
	by the algorithm. For more details on how these are used, check out the solvePuzzle() method in SudokuSolverPanel.
*/

public class SudokuSquare {

	private int lastI;
	private int lastJ;
	private int value;
	private boolean revisited;
	private boolean known;

	// the constructor is used to process user inputs
	public SudokuSquare(int value){
		this.value = value;
		known = false;
		// if the user gives the square a value initially, we don't want to increment that value in the algorithm
		if(value!=0)
			known = true; // we set a flag so the algorithm can check for it

		// the initial lastI value of 30 is used to indicate that lastI and lastJ are not yet initialized
		lastI = 30;

		// if revisited is true, this indicates that the square is going to be revisited in the next step
		revisited = false;
	}

	// getters:

	public int getValue(){
		return value;
	}

	public int getLastI(){
		return lastI;
	}

	public int getLastJ(){
		return lastJ;
	}

	public boolean getRevisited(){
		return this.revisited;
	}

	public boolean getKnown(){
		return this.known;
	}

	// setters:

	public void setValue(int value){
		this.value = value;
	}

	public void setLastI(int lastI){
		this.lastI = lastI;
	}

	public void setLastJ(int lastJ){
		this.lastJ = lastJ;
	}

	public void setRevisited(boolean revisited){
		this.revisited = revisited;
	}
}
