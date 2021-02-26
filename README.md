# SudokuSolver
This is a program written in Java that solves sudoku puzzles by brute force via backtracking. 

The three files that constitute the code are in the sudoku folder, and the other file is an executable if you want to try out the program. All of the Java files are thoroughly commented for those who are curious about how the code works.

The files in the sudoku folder are:
  - SudokuSolverPanel.java
  - SudokuSquare.java
  - SudokuSolver.java

SudokuSolverPanel.java contains most of the code. This includes the graphical interface, the puzzle solving function (with several helper functions), and input error checking. 

SudokuSquare.java consists of a single class to represent each square in the 9x9 puzzle. The square objects carry useful information that helps the algorithm backtrack effectively.

SudokuSolver.java -- this displays the SudokuSolverPanel object and runs the program.


[This wikipedia article](https://en.wikipedia.org/wiki/Sudoku_solving_algorithms) is nice for comparing the different types of sudoku solving algorithms.
