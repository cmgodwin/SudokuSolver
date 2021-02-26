/*
	Independent project by Curtis Godwin

	July 2017

	This class contains the bulk of the code. It implements the graphical interface using a JPanel object from the
	Java Swing library. The interface consists of a 9x9 grid of equally sized text boxes and two buttons, one to solve
	the puzzle and one to clear the interface. When the user enters their unsolved puzzle into the interface, the inputs
	to the text boxes are translated to a 9x9 array of SudokuSquare objects, which can then be processed through the
	solvePuzzle() method.

	The solving algorithm is brute force. Its main logic is to increment the values in the squares starting at the top
	left square, checking the legality of the puzzle each time it increments; if the puzzle is legal after a given increment,
	the algorithm moves to the next square to the right and increments that square; otherwise, if the puzzle is determined
	to be illegal, it continues by incrementing the current square again. If the current square reaches 10, this means
	the problem necessarily arose due to a previous square being illegal, so the algorithm moves to the previous square
	and begins incrementing it.

	After the entire puzzle is determined to be legal, the results are converted back into text to be displayed in the
	text boxes on the interface.
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
public class SudokuSolverPanel extends JPanel {

	private JPanel sudokuOverlayZone;
	private JPanel south;
	private JTextPane[][] puzzleInterface;
	private JTextPane north, east, west;
	private StyledDocument doc;
	private Font font;
	private JButton solvePuzzleButton, clearPuzzle;
	private Border fullBorder, noLeft, noRight;
	private Border topLeftBorder, topRightBorder, bottomLeftBorder, bottomRightBorder, leftBorder, rightBorder, topBorder, bottomBorder;
	private SudokuSquare[][] puzzle;
	private int lastI;
	private int lastJ;
	private int outerThickness;
	private Color outerColor, innerColor;
	private boolean vet;

	public SudokuSolverPanel(){
		setPreferredSize(new Dimension(600, 600));

		// initialize overlay zone, puzzle interface and border preferences (specified for inner and outer borders)
		sudokuOverlayZone = new JPanel();
		sudokuOverlayZone.setLayout(new GridLayout(9,9));
		puzzleInterface = new JTextPane[9][9];
		outerThickness = 3;
		outerColor = Color.blue;
		innerColor = Color.gray;

		// initialize borders to attach to the text boxes that correspond to the sudoku squares
		// these borders are specifically for the 3x3 boxes within the larger 9x9 box
		topLeftBorder = BorderFactory.createMatteBorder(outerThickness, outerThickness, 0, 0, outerColor);
		bottomLeftBorder = BorderFactory.createMatteBorder(0, outerThickness, outerThickness, 0, outerColor);
		topRightBorder = BorderFactory.createMatteBorder(outerThickness, 0, 0, outerThickness, outerColor);
		bottomRightBorder = BorderFactory.createMatteBorder(0, 0, outerThickness, outerThickness, outerColor);
		leftBorder = BorderFactory.createMatteBorder(0, outerThickness, 0, 0, outerColor);
		rightBorder = BorderFactory.createMatteBorder(0, 0, 0, outerThickness, outerColor);
		topBorder = BorderFactory.createMatteBorder(outerThickness, 0, 0, 0, outerColor);
		bottomBorder = BorderFactory.createMatteBorder(0, 0, outerThickness, 0, outerColor);

		// this loop handles the initialization and border formatting of the squares
		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
			{
				// initialize each text box to be empty and set the preferred font settings
				puzzleInterface[i][j] = new JTextPane();
				font = new Font("Calibri", Font.PLAIN, 40);
				setJTextPaneFont(puzzleInterface[i][j], font, Color.black);
				puzzleInterface[i][j].setText("");

				// center the text for each square
				doc = puzzleInterface[i][j].getStyledDocument();
				SimpleAttributeSet center = new SimpleAttributeSet();
				StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
				doc.setParagraphAttributes(0, doc.getLength(), center, false);

				// there is weird behavior when using overlapping 1-pixel wide borders in java swing
				// some vertical lines appear thinner and some thicker, even though they are allegedly all 1 pixel wide
				// because of this, the border settings here for the inner squares were pure guess and check
				fullBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, innerColor);
				noLeft = BorderFactory.createMatteBorder(1, 0, 1, 1, innerColor);
				noRight = BorderFactory.createMatteBorder(1, 1, 1, 0, innerColor);
				if (j%2 == 0)
					puzzleInterface[i][j].setBorder(fullBorder);
				else if (j==3 || j==7)
					puzzleInterface[i][j].setBorder(noRight);
				else
					puzzleInterface[i][j].setBorder(noLeft);


				// thankfully the behavior is not weird for overlapping borders thicker than 1 pixel!
				// here the thicker borders are placed based on where the 3x3 boxes start and end on both axes
				if((i==0 || i==3 || i==6) && j%3 != 0 && j!=8)
					puzzleInterface[i][j].setBorder(new CompoundBorder(topBorder, puzzleInterface[i][j].getBorder()));
				else if(i==8 && j%3 != 0 && j!=8)
					puzzleInterface[i][j].setBorder(new CompoundBorder(bottomBorder, puzzleInterface[i][j].getBorder()));

				if((j==0||j==3||j==6) && i%3 != 0 && i!=8)
					puzzleInterface[i][j].setBorder(new CompoundBorder(leftBorder, puzzleInterface[i][j].getBorder()));
				else if(j==8 && i%3 != 0 && i!=8)
					puzzleInterface[i][j].setBorder(new CompoundBorder(rightBorder, puzzleInterface[i][j].getBorder()));

				if((i==0 || i==3 || i==6) && (j==0||j==3||j==6))
					puzzleInterface[i][j].setBorder(new CompoundBorder(topLeftBorder, puzzleInterface[i][j].getBorder()));

				else if((i==0 || i==3 || i==6) && (j==8))
					puzzleInterface[i][j].setBorder(new CompoundBorder(topRightBorder, puzzleInterface[i][j].getBorder()));

				else if((i==8) && (j==0||j==3||j==6))
					puzzleInterface[i][j].setBorder(new CompoundBorder(bottomLeftBorder, puzzleInterface[i][j].getBorder()));

				else if((i==8) && (j==8))
					puzzleInterface[i][j].setBorder(new CompoundBorder(bottomRightBorder, puzzleInterface[i][j].getBorder()));

				sudokuOverlayZone.add(puzzleInterface[i][j]);
			}
		}
		// set the overall layout of the JPanel to BorderLayout
		// this allows us to partition the layout into north, south, east, west, and center panels
		// this way the buttons and the display will be set up in well-centered positions
		// NOTE: the north, east and west text panes initialized below are not interactible and are only
		//			 there to make a thin frame around the main puzzle interface
		setLayout(new BorderLayout());
		north = new JTextPane();
		east = new JTextPane();
		west = new JTextPane();
		north.setEditable(false);
		east.setEditable(false);
		west.setEditable(false);
		add(north, BorderLayout.NORTH);
		add(east, BorderLayout.EAST);
		add(west, BorderLayout.WEST);

		// initialize buttons and add them to the south panel
		south = new JPanel();
		solvePuzzleButton = new JButton("Solve Puzzle");
		solvePuzzleButton.addActionListener(new SolvePuzzleListener());
		south.add(solvePuzzleButton);
		clearPuzzle = new JButton("Clear Puzzle");
		clearPuzzle.addActionListener(new ClearPuzzleListener());
		south.add(clearPuzzle);
		//add the south panel to the main panel
		add(south, BorderLayout.SOUTH);

		// add the initialized puzzle interface to the main panel
		add(sudokuOverlayZone, BorderLayout.CENTER);
	}

	/*
			setJTextPaneFont() is sourced from the Java 6 Programming Black Book, pg. 767
			link: https://www.google.com/books/edition/Java_6_Programming_Black_Book_New_Ed/SSyuJa04uv8C?hl=en&gbpv=1&dq=%22setJTextPaneFont%22&pg=PA766&printsec=frontcover

			this method is used to initialize the font styling for the text boxes corresponding to the squares
	*/
	public static void setJTextPaneFont(JTextPane jtp, Font font, Color c) {

        MutableAttributeSet attrs = jtp.getInputAttributes();
        StyleConstants.setFontFamily(attrs, font.getFamily());
        StyleConstants.setFontSize(attrs, font.getSize());
        StyleConstants.setItalic(attrs, (font.getStyle() & Font.ITALIC) != 0);
        StyleConstants.setBold(attrs, (font.getStyle() & Font.BOLD) != 0);
        StyleConstants.setForeground(attrs, c);
        StyledDocument doc = jtp.getStyledDocument();
        doc.setCharacterAttributes(0, doc.getLength() + 1, attrs, false);
    }

	// getUserPuzzle() -- takes the user input and saves it in a 9x9 array of SudokuSquare objects
	public SudokuSquare[][] getUserPuzzle(){
		puzzle = new SudokuSquare[9][9];
		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
			{
				if(puzzleInterface[i][j].getText().equals(""))
				{
					puzzle[i][j] = new SudokuSquare(0);
				}
				else
				{
					try{
						// set the value of the square to the user input
						puzzle[i][j] = new SudokuSquare(Integer.parseInt(puzzleInterface[i][j].getText()));
					}
					catch(NumberFormatException oops){
						// if the user enters something that is not an integer, set the square's value to 100
						// this ensures that the puzzle will not pass the vetting process and an error message will be displayed
						puzzle[i][j] = new SudokuSquare(100);
					}
				}
			}
		}
		return puzzle;
	}

	// displayPuzzle() -- a method to display the puzzle on the interface
	public void displayPuzzle(SudokuSquare[][] puzzle){
		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
			{
				puzzleInterface[i][j].setText("" + puzzle[i][j].getValue());
			}
		}
	}

	/*
		determineLegality() -- checks the legality of the given square
					-if the square's row, column, or box has duplicates of the square's value, the square is illegal

		parameters:
					puzzle -- the puzzle the square is from
					i, j -- the coordinates of the square within the puzzle
					vet -- indicates whether the function is being used to check a new puzzle for errors
	*/
	public boolean determineLegality(SudokuSquare[][] puzzle, int i, int j){
		int numRepetitions = 0;
		int actualValue = puzzle[i][j].getValue();

		if(actualValue > 9)
			return false; // anything above 9 is not a legal value

		// special logic for when we are vetting the user input
		if(this.vet){
			if(actualValue == 0)
				return true; // if we are vetting the puzzle, we can pass the zeroes
		}
		else{
			if(actualValue == 0)
				return false; // if we are not vetting the puzzle, zeroes are considered illegal
		}

		for(int c=0;c<9;c++) //counts the repeats in the column of the current square
		{
			if(puzzle[i][c].getValue() == actualValue)
				numRepetitions++;
		}
		if(numRepetitions >= 2) //returns false if the column has a repeat
			return false;
		numRepetitions = 0;

		for(int c=0;c<9;c++) //counts the repeats in the row of the current square
		{
			if(puzzle[c][j].getValue() == actualValue)
				numRepetitions++;
		}
		if(numRepetitions >= 2) //returns false if the row has a repeat
			return false;
		numRepetitions = 0;

		//this series of if statements determines which 3x3 box the square is in and checks for repeats within that box
		if(i<3 && j<3)
		{
			if(boxRepetitions(puzzle, i, j, 0, 0) >= 2)
				return false;
		}
		else if((i<6 && i>2) && j<3)
		{
			if(boxRepetitions(puzzle, i, j, 3, 0) >= 2)
				return false;
		}
		else if((i<9 && i>5) && j<3)
		{
			if(boxRepetitions(puzzle, i, j, 6, 0) >= 2)
				return false;
		}
		else if(i<3 && (j<6 && j>2))
		{
			if(boxRepetitions(puzzle, i, j, 0, 3) >= 2)
				return false;
		}
		else if((i<6 && i>2) && (j<6 && j>2))
		{
			if(boxRepetitions(puzzle, i, j, 3, 3) >= 2)
				return false;
		}
		else if((i<9 && i>5) && (j<6 && j>2))
		{
			if(boxRepetitions(puzzle, i, j, 6, 3) >= 2)
				return false;
		}
		else if(i<3 && (j<9 && j>5))
		{
			if(boxRepetitions(puzzle, i, j, 0, 6) >= 2)
				return false;
		}
		else if((i<6 && i>2) && (j<9 && j>5))
		{
			if(boxRepetitions(puzzle, i, j, 3, 6) >= 2)
				return false;
		}
		else if((i<9 && i>5) && (j<9 && j>5))
		{
			if(boxRepetitions(puzzle, i, j, 6, 6) >= 2)
				return false;
		}
		return true;
	}
	// vetPuzzle() -- tells whether the user-entered puzzle is legal so an error can be thrown if needed
	public boolean vetPuzzle(SudokuSquare[][] puzzle){
		this.vet = true;
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				if(!determineLegality(puzzle, i, j)){
					this.vet = false;
					return false;
				}
			}
		}
		this.vet = false;
		return true;
	}
	/*
		solvePuzzle() -- solves the puzzle! the logic is described abstractly at the top of this file; there are
										also a lot more details in the body of the method
		parameters:
				-puzzle -- the puzzle that is being solved
	*/
	public SudokuSquare[][] solvePuzzle(SudokuSquare[][] puzzle){
		// keep track of the coordinates of the previous square
		lastI = 0;
		lastJ = 0;
		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
			{
				// if the solution for the square was known at the start, we skip the square
				if(!puzzle[i][j].getKnown())
				{
					// lastI==30 indicates that the square is new, meaning its lastI and lastJ values are not initialized
					// if the square is new, we set its lastI and lastJ variables to be the i and j of the previous relevant square
					if(puzzle[i][j].getLastI() == 30)
					{
						puzzle[i][j].setLastI(lastI);
						puzzle[i][j].setLastJ(lastJ);
					}

					// iterate the value in the square until it is legal
					while(determineLegality(puzzle, i, j) == false && puzzle[i][j].getValue() != 10)
					{
						puzzle[i][j].setValue(puzzle[i][j].getValue() + 1);
					}
					// if we reach the value 10, we know there are no legal values for the square
					// this means that the conflict arose earlier in the puzzle
					// so, we set the i and j coordinates to that of the last legal box
					if(puzzle[i][j].getValue() == 10)
					{
						// first reset the value of the current square
						puzzle[i][j].setValue(0);
						// set a flag in the square at (lastI, lastJ) indicating that it is being revisited
						puzzle[puzzle[i][j].getLastI()][puzzle[i][j].getLastJ()].setRevisited(true);

						// set i and j to the previous i and j as indicated by the current square
						int savedI = i;
						i = puzzle[i][j].getLastI();
						j = puzzle[savedI][j].getLastJ();
					}
					else
					{
						// since the value in the square is legal, we save its coordinates before moving to the next square
						lastI = i;
						lastJ = j;
					}
					// if we are revisiting a square, we know we need to continue iterating through the values in that square
					if(j>=0 && puzzle[i][j].getRevisited() == true){
						// increment the value in the square and turn off the revisited flag
						puzzle[i][j].setValue(puzzle[i][j].getValue() + 1);
						puzzle[i][j].setRevisited(false);
						// decrement the j value if we are not at j==0
						// since we are revisiting, we need to backtrack the loop to the square we are revisiting
						j--;
						// if we are at j==0, we need to move to the end of the previous row instead of getting an out of bounds error
						if(j<0 && i!=0){
							i--;
							j=8;
						}
					}
				}
			}
		}
		return puzzle;
	}

	// clearPuzzle() iteratively resets the text of each box in the puzzle
	public void clearPuzzle(){
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				puzzleInterface[i][j].setText("");
			}
		}
	}

	/*
		boxRepitions() -- finds the number of repititions of a square within its box
		parameters:
				- puzzle is the nested array of square objects corresponding to the desired puzzle
				- i and j are the coordinates of the square within the puzzle
				- boxStartI and boxStartJ are the coordinates of the upper left corner of the box
	*/
	public int boxRepetitions(SudokuSquare[][] puzzle, int i, int j, int boxStartI, int boxStartJ){
		int numRepetitions = 0;
		for(int c=boxStartJ;c<boxStartJ+3;c++)
		{
			if(puzzle[boxStartI][c].getValue() == puzzle[i][j].getValue())
			{
				numRepetitions++;
			}
		}
		for(int c=boxStartJ;c<boxStartJ+3;c++)
		{
			if(puzzle[boxStartI+1][c].getValue() == puzzle[i][j].getValue())
			{
				numRepetitions++;
			}
		}
		for(int c=boxStartJ;c<boxStartJ+3;c++)
		{
			if(puzzle[boxStartI+2][c].getValue() == puzzle[i][j].getValue())
			{
				numRepetitions++;
			}
		}
		return numRepetitions;
	}

	/*
		SolvePuzzleListener
		 		-starts the solvePuzzle() method when the solve button is clicked
				-also tracks the execution time
	*/
	private class SolvePuzzleListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			SudokuSquare[][] puzzle = getUserPuzzle();
			// if the user input is valid, we solve the puzzle
			if(vetPuzzle(puzzle)){
				double startTime = System.nanoTime();
				puzzle = solvePuzzle(puzzle);
				double endTime = System.nanoTime();
				double duration = (endTime-startTime)/1000000000;
				displayPuzzle(puzzle);
				JOptionPane.showMessageDialog(null, "This puzzle took " + duration + " seconds to solve.");
			}
			// if the user input is invalid, we give an error message
			else
				JOptionPane.showMessageDialog(null, "Input was not legal! Try again.");
		}
	}
	// ClearPuzzleListener -- runs the clearPuzzle() function when the clear puzzle button is clicked
	private class ClearPuzzleListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			clearPuzzle();
		}
	}
}
