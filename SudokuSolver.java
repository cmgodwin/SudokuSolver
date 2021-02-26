/*
	Independent project by Curtis Godwin

	July 2017

	This is the driver class for the sudoku solver. Its main use is to connect the user with the interface
	implemented in the SudokuSolverPanel class.

	To do this, it initializes the JFrame 'sudokuWindow' so that a SudokuSolverPanel object can be added to it.
	It then displays the JFrame so that the user can use the interface.

	To run the program, run this file with the SudokuSolverPanel and SudokuSquare classes in the same folder.
*/

import javax.swing.JFrame;

public class SudokuSolver {
	public static void main(String [] args){
		JFrame sudokuWindow = new JFrame("Sudoku Solver");
		sudokuWindow.setResizable(false); // resizing the window messes up the interface!
		sudokuWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		SudokuSolverPanel panel = new SudokuSolverPanel();
		sudokuWindow.getContentPane().add(panel);

		sudokuWindow.pack();
		sudokuWindow.setLocationRelativeTo(null);
		sudokuWindow.setVisible(true);
	}
}
