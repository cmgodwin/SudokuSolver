# SudokuSolver
This is a program written in Java that solves sudoku puzzles by brute force via backtracking. [This wikipedia article](https://en.wikipedia.org/wiki/Sudoku_solving_algorithms) is nice for comparing the different types of sudoku solving algorithms.

The three files that constitute the code are in the sudoku folder, and the other file is an executable if you want to try out the program. All of the Java files are thoroughly commented for those who are curious about how the code works.

About the project files in the sudoku folder:
  - SudokuSolverPanel.java -- contains most of the code. This includes the graphical interface, the puzzle solving function (with several helper functions), and input error checking. 
  - SudokuSquare.java -- consists of a single class to represent each square in the 9x9 puzzle. The square objects carry useful information that helps the algorithm backtrack effectively.
  - SudokuSolver.java -- displays the SudokuSolverPanel object and runs the program.

In [the wikipedia article](https://en.wikipedia.org/wiki/Sudoku_solving_algorithms), they give an example of a standard sudoku puzzle as well as a puzzle designed specifically to work against the brute force method. As an example of the sudoku solver interface, here are the two puzzles from the article and the solutions to them, as found by the program.

<img src="https://github.com/cmgodwin/SudokuSolver/blob/main/wikipedia_puzzles/easy_puzzle_start.png?raw=true" width="400" height="420"><img src="https://github.com/cmgodwin/SudokuSolver/blob/main/wikipedia_puzzles/hard_puzzle_start.png?raw=true" width="400" height="420">

<img src="https://github.com/cmgodwin/SudokuSolver/blob/main/wikipedia_puzzles/easy_puzzle_finish.png?raw=true" width="400" height="462"><img src="https://github.com/cmgodwin/SudokuSolver/blob/main/wikipedia_puzzles/hard_puzzle_finish.png?raw=true" width="400" height="462">








