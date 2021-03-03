# SudokuSolver
This is a program written in Java that solves sudoku puzzles by brute force via backtracking. [This wikipedia article](https://en.wikipedia.org/wiki/Sudoku_solving_algorithms) is nice for comparing the different types of sudoku solving algorithms.

The three files that constitute the code are in the sudoku folder, and there is an executable if you want to try out the program. All of the Java files are thoroughly commented for those who are curious about how the code works.

About the project files in the sudoku folder:
  - SudokuSolverPanel.java -- contains most of the code. This includes the graphical interface, the puzzle solving function (with several helper functions), and input error checking. 
  - SudokuSquare.java -- consists of a single class to represent each square in the 9x9 puzzle. The square objects carry useful information that helps the algorithm backtrack effectively.
  - SudokuSolver.java -- displays the SudokuSolverPanel object and runs the program.

In [the aforementioned wikipedia article](https://en.wikipedia.org/wiki/Sudoku_solving_algorithms), they give an example of a standard sudoku puzzle as well as a puzzle designed specifically to work against the brute force method. As an example of the sudoku solver interface, here are the two puzzles from the article and the solutions to them, as found by the program:


<img src="https://github.com/cmgodwin/SudokuSolver/blob/main/wikipedia_puzzles/easy_puzzle_start.png?raw=true" width="400" height="420"><img src="https://github.com/cmgodwin/SudokuSolver/blob/main/wikipedia_puzzles/hard_puzzle_start.png?raw=true" width="400" height="420">

<img src="https://github.com/cmgodwin/SudokuSolver/blob/main/wikipedia_puzzles/easy_puzzle_finish.png?raw=true" width="400" height="462"><img src="https://github.com/cmgodwin/SudokuSolver/blob/main/wikipedia_puzzles/hard_puzzle_finish.png?raw=true" width="400" height="462">

As you can see, we have a ~0.008s execution time for the normal puzzle and an ~8.9s execution for the more stubborn one. The easiest way to understand the significant increase in difficulty for the algorithm with the harder puzzle is by looking at the top left number. With a '9' in the top left, the algorithm has to backtrack all the way to the initial square 8 times, maximizing the number of iterations needed to solve the puzzle (this is also why the 9 is followed by 8-7-6-5-4-3-2-1 on the top row, taking advantage of the depth-first structure). 

The most intuitive workaround for this kind of puzzle would be to randomize the order in which the squares are processed, utilizing a doubly-linked list structure of squares with more index tracking. I did not implement this here, but may include it in the future.








