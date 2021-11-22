package cs1302.game;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

/**
 * This class is the code representation of a minesweeper
 * game conataing all variables and methods used to play the game.
 */
public class MinesweeperGame {

    /* A string array representing the individual squares for the gameboard*/
    private String[][] gameBoard;

    /*A boolean array representing the locations of mines on the gameboard*/
    private boolean [][] mines;

    /*Number of rounds completed so far in current state of the game*/
    private int rounds;

    /*Number of rows and columns for the size of the gameboard*/
    private int rows;
    private int columns;

    /*The current score for the game*/
    private double score;

    /*The number of mines present on the gameboard*/
    private int mineCount;

    /*The one and only scanner reading from standard input*/
    private final Scanner stdInt;

    /*Scanner used to read necessary files for the program*/
    private Scanner fileRead;

    /*A string representing the path to a seed file*/
    private String seedPath;

    /*A file representing a seed file*/
    private File seedfile;

    /*A scanner used to read in user information from stdInt*/
    private Scanner userPrompt;

    /*Indicated how many squares have been properly revealed or identified*/
    private int revealed;

    /*Representing if the current state of the game requires the fog of war to be lifted*/
    private boolean isWon;

    /*Representing if the current state of the game is a winning one*/
    private boolean noFog;

    /*Representing the amount of turns a player has taken in teh game*/
    private int turns;

    /*Represnts if the state of the game is loss */
    private boolean isLoss;

    /**
     * This is a constructor to create an object that represents
     * a minesweeper game. Calls {@code readSeedFile} to intialize instance
     * variables related to {@code gameBoard} and {@code mineField}.
     *@param stdInt A Scanner object intialized to read from standard input.
     *@param seedPath A path to the seedfile for the game.
     */
    public MinesweeperGame(Scanner stdInt, String seedPath) {
        this.stdInt = stdInt;
        this.seedPath = seedPath;
        this.readSeedFile(seedPath);
        this.revealed = 0;
        this.turns = 0;
        this.isWon = false;
        this.isLoss = false;
        this.noFog = false;
    } // MinesweeperGame Constructor

    /**
     * Reads the data stored a seed file provided from the user.
     * uses the data stored in the seed to call methods to generate the
     * {@code gameBoard} and {@code mineField} arrays.
     *
     *@param seedPath a abstract seed path to a seed file.
     *@throws FileNotFoundException if seed file cannot be found.
     */
    private void readSeedFile(String seedPath) {
        try {
            File seedFile = new File (seedPath);
            this.fileRead = new Scanner(seedFile);
            this.rows = fileRead.nextInt();
            this.columns = fileRead.nextInt();
            /*this call fills in the gameBoard array with empty strings of 3 spaces*/
            this.generateGameBoard(this.rows, this.columns);
            /*this call fills in the mineField array with true values at mine locations*/
            this.generateMineField(fileRead);

            /*These catch blocks catch all the exceptions that could occur with Malformed Seed Files
              such as the file not being found, unexpected tokens in the file, mine locations are
              not int bounds, wrong number or rows, invalid mine counts, and empty or missing lines
              in the seedfile */
        } catch (FileNotFoundException fnfe) {
            System.err.println();
            System.err.println("Seed File Not Found Error: " + fnfe.getMessage());
            System.exit(2);
        } catch (IllegalArgumentException iae) {
            System.err.println();
            System.err.println("Seed File Malformed Error: " + iae.getMessage());
            System.exit(3);
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            System.err.println();
            System.err.println("Seed File Malformed Error: " + aioobe.getMessage());
            System.exit(3);
        } catch (InputMismatchException ime) {
            System.err.println();
            System.err.println("Seed File Malformed Error: " + ime.getMessage());
            System.exit(3);
        } catch (NoSuchElementException nsee) {
            System.err.println();
            System.err.println("Seed File Malformed Error: " + nsee.getMessage());
            System.exit(3);
        }
    } // readSeedFile

    /**
     * This method generates the {@code gameBoard} array, intializes it with
     * {@code rows} and {@code columns} and checks whether or not the number
     * of rows and columns is valid from the seed file. Calls {@code makeGameboard}
     * to fill in gameBoard with strings of 3 whitepaces.
     *@param rows the number of rows read from the seed file.
     *@param columns the number of columns read from the seed file.
     *@throws IllegalArgumentException if number of rows andn columns is not valid.
     */
    private void generateGameBoard(int rows, int columns) {
        /*this line of code checks to see if there is a valid number of rows and columns in the
          seed files read in using the paramaters rows and columns */
        if (rows < 5 || rows > 10 || columns < 5 || columns > 10) {
            throw new IllegalArgumentException("Cannot create a minefield with" +
            " that many rows and/or columns");
        }
        this.gameBoard = new String[rows][columns];
        this.makeGameboard();
    }

    /**
     *This method generates the {@mines} array, intializes it with {@code this.rows}
     * and {@code this.columns} and checks to see if the number of mines and location of mines
     * is valid. Sets the number of mines and location of mines from the input taken from
     * {@code fileRead} which reads from the seed file.
     * @param fileRead a {@code Scanner} object reading from a seed file.
     * @throws IllegalArgumentException if number of mines is not valid.
     */
    private void generateMineField(Scanner fileRead) {
        String err;
        this.mineCount = fileRead.nextInt();

        /*This line checks to see if the number of mines is positive and or is a valid
          number based on the number of rows and columns */
        if (this.mineCount < 1 || this.mineCount > ((this.rows * this.columns) - 1)) {
            throw new IllegalArgumentException("Invalid mine count");
        }

        int row = 0;
        int column = 0;
        int count = 0;
        this.mines = new boolean[this.rows][this.columns];

        /*Loop intializes the locations of the mines from the seed file to the mines array*/
        while (fileRead.hasNext()) {
            row = fileRead.nextInt();
            column = fileRead.nextInt();
            this.mines[row][column] = true;
            count++;
        }

        /*Last error check to see if the mine count expected from the seed file matches
          what was read in from the seed file and intialized in the array*/
        if (count != this.mineCount) {
            throw new IllegalArgumentException();
        } // if
    }

    /**
     * This method prints the welcome screen from tests/welcome.txt
     * when a new minesweeper game is started. If the file is not found
     * an error message is printed.
     */
    public void printWelcome() {
        try {
            File welcome = new File("resources/welcome.txt");
            this.fileRead = new Scanner(welcome);
            while (fileRead.hasNextLine()) {
                System.out.println(fileRead.nextLine());
            } // while
            System.out.println();

            /*Possible exception to be thrown if the file is not found which
              is highly unlikely unless file was moved from tests directory*/
        } catch (FileNotFoundException fnfe) {
            System.err.println();
            System.err.println(fnfe.getMessage());
        } // try
    } // printWelcome

    /**
     *This method prints the number of rounds completed and rows 0 - n of the length
     * of the {@code gameBoard} array, following the pattern "n |   | " for however
     * many columns there are in the gameBoard.
     */
    public void printMineRows() {
        try {
            /*To get the formatting for the spacing correct only after the turn is an extra
              new line printed before "Rounds Completed" */
            if (this.turns > 0) {
                System.out.println("\n Rounds Completed: " + this.getRounds() + "\n");
            } else {
                System.out.println(" Rounds Completed: " + this.getRounds() + "\n");
            }
            turns++;
            int x = 0;

            /*Formats the mine field by printing "rownumber |" followed by "|   |"
              for however many columns there are in the array */
            for (String[] i : gameBoard) {
                System.out.print( " " + x + " |");
                for (String j : i) {
                    System.out.print(j + "|");
                } // for
                System.out.println();
                x++;
            } // for

            /*in case a space in the array is not intialized the null pointer exception is catched*/
        } catch (NullPointerException npe) {
            System.err.println();
            System.err.println(npe.getMessage());
        } // try
    } // printMineRows

    /**
     * This method prints the bottom row, with the row numbers centered with the
     * squares in the minefield.
     */
    public void printCenteredRow() {
        try {
            int  x = 0;
            System.out.print("     " + x);
            for (int i = 0; i < gameBoard[0].length - 1; i++) {
                x++;
                System.out.print("   " + x);
            } // for
            System.out.println("\n");
        } catch (NullPointerException npe) {
            System.err.println();
            System.err.println(npe.getMessage());
        } // try
    } // printCenteredRow

    /**
     * This method prints the current
     * contents of the minefield to
     * standard output. Calling both {@code printMineRows} and {@code printCenteredRow}
     * @throws NullPointerException if when printing out the {@code gameBoard} array,
     * one or more of the indices is null.
     */
    public void printMineField() {
        this.printMineRows();
        this.printCenteredRow();
    } // printMineField

    /**
     * Prompts user for input on how to proceed in the game.
     * Captures user input and delegates to other methods to proceed
     * in the game.
     */
    public void promptUser() {
        /*values to keep track if user input is valid*/
        boolean valid  = true;
        do {
            try {
                this.printMineField();
                System.out.print("minesweeper-alpha: ");
                /*only asks for user input and delegate to other methods
                  if the state of the game is not one*/
                if (!this.isWon() || !this.isLoss) {
                    userPrompt = new Scanner(stdInt.nextLine());
                    this.delegate(userPrompt);
                    valid = false;
                    this.rounds++;
                } // if
                /*All the exceptions that can occur if the user input is invalid or
                  malformed in some way including, invalid commands and valid commands
                  to squares that are out of bounds */
            } catch (IllegalArgumentException iae) {
                System.err.println();
                System.err.println("Invalid Command: " + iae.getMessage());
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                System.err.println();
                System.err.println("Invalid Command: " + aioobe.getMessage());
            } catch (NullPointerException npe) {
                System.err.println();
                System.err.println("Invalid Command: " + npe.getMessage());
            } catch (NoSuchElementException nsee) {
                System.err.println();
                System.err.println("Invalid Command: " + nsee.getMessage());
            }
        } while (valid);
    } // promptUser

    /**
     * Uses the input from {@code userPromp} which was read in by {@code stdInt} to delegate
     * to the appropriate methods based on the command the user wanted to do.
     *@param userPrompt a scanner holding user input.
     *@throws IllegalArgumentException if the input is not valid.
     */
    private void delegate(Scanner userPrompt) throws IllegalArgumentException {
        String input = userPrompt.next();
        int row = 0;
        int column = 0;
        /* takes in the row and column of the command
           and error checks if there is other commands when there
           should be none */
        if (userPrompt.hasNext()) {
            row = userPrompt.nextInt();
            column = userPrompt.nextInt();
            if (userPrompt.hasNext()) {
                throw new NoSuchElementException();
            } // if
        } // if

        /*If nofog was called in the previous round, it is reversed*/
        if (noFog) {
            this.removeNoFog();
        } // if

        /*This is where the actual delegating happens based on the input from the user*/
        if (input.equals("m") || input.equals("mark")) {
            this.mark(row, column);
        } else if (input.equals("g") || input.equals("guess")) {
            this.guess(row, column);
        } else if (input.equals("r") || input.equals("reveal")) {
            this.reveal(row, column);
        } else if (input.equals("q") || input.equals("quit")) {
            this.quit();
        } else if (input.equals("h") || input.equals("help")) {
            this.help();
        } else if (input.equals("nofog")) {
            this.noFog();
        } else {
            throw new IllegalArgumentException("Command not recognized!");
        } // if
    }

    /**
     * This method quits the game and ends the program with a gracefull
     * exit and an exit status of 0.
     */
    private void quit() {
        System.out.println("\nQuitting the game...\nBye!");
        System.exit(0);
    } // quit

    /**
     * This method marks a spot on the minefield as potentially being a mine.
     * if the square was already marked or the spot is not a mine the number of
     * correctly revealed squares does not change.
     *@param row the row for the spot.
     *@param column the column for the spot.
     */
    private void mark(int row, int column) {
        this.isInBounds(row,column);
        if (mines[row][column] == true) {
            /*Checks to see if the spot was not already correctly marked*/
            if (!gameBoard[row][column].equals(" F ")) {
                revealed++;
            }
        }
        gameBoard[row][column] = " F ";
    }

    /**
     * This method reveals a spot on the minefield. If the spot is a mine, the user
     * loses and {@code printLoss()} is called. If the spot is not a mine, the number
     * of adjacent mines is displayed. If the spot is revealed and is not a mine, the
     * number of correctly revealed squares increases by 1 if it was not already revealed
     * before.
     *@param row the row for the spot.
     *@param column the column for the spot.
     */
    private void reveal(int row, int column) {
        this.isInBounds(row, column);
        boolean isMine = this.isMine(row, column);
        if (!isMine) {
            /*This line of code checks to see if the current square matches the pattern
              of a space followed by a number followed by a space, used to see if the spot was
              already previously revealed */
            if (!(gameBoard[row][column].matches(" .*\\d.* "))) {
                revealed++;
            }
            gameBoard[row][column] = " " + this.getNumAdjMines(row, column) + " ";
        } else {
            this.isLoss = true;
            this.printLoss();
        } // if
    }

    /**
     * This method marks the spot of the mine field with a question mark/ " ? ".
     * If the spot was previously revealed or was previosuly correctly marked,
     * the number of correctly revealed squares is decreased by 1.
     *@param row the row for the spot.
     *@param column the column for the spot.
     */
    private void guess(int row, int column) {
        this.isInBounds(row, column);
        /* checks to see if this spot was previously correctly marked */
        if (mines[row][column] == true && (gameBoard[row][column].equals(" F "))) {
            this.revealed--;
        }
        /*checks to see if this spot was previously correctly marked */
        if (gameBoard[row][column].matches(" .*\\d.* ")) {
            this.revealed--;
        }
        gameBoard[row][column] = " ? ";
    }

    /**
     * This method prints the help screen with a list of valid command.
     */
    private void help() {
        String help = "\nCommands Available...\n" +
            "- Reveal: r/reveal row col\n" +
            "-   Mark: m/mark   row col\n" +
            "-  Guess: g/guess  row col\n" +
            "-   Help: h/help\n" +
            "-   Quit: q/Quit";
        System.out.println(help);
    }

    /**
     *This command removes the fog of war and reveals the location of the mines for one
     *round by filling in the square with brackets around what was intially in the square.
     */
    private void noFog() {
        int i = 0;
        int j = 0;
        this.noFog = true;
        for (boolean[] b : mines) {
            for (boolean c : b) {
                if (mines[i][j] == true) {
                    /*The following if statements check to see if the square where equal to
                      F or ? in which brackets are placed around them, only if the spot is also
                      a mine location */
                    if (gameBoard[i][j].equals(" F ")) {
                        gameBoard[i][j] = "<F>";
                    } else if (gameBoard[i][j].equals(" ? ")) {
                        gameBoard[i][j] = "<?>";
                    } else {
                        gameBoard[i][j] = "< >";
                    } // if
                } //if
                j++;
            } // for
            i++;
            j = 0;
        } // for
    } // nofog

    /**
     * This method removes the effects of {@code noFog()} after
     * a round has passed.
     */
    private void removeNoFog() {
        noFog = false;
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                /*The following if statements check whether or not the
                  squares affected by noFog() where an F or a ? in which the brackets
                  around them are removed */
                if (gameBoard[i][j].equals("<F>")) {
                    gameBoard[i][j] = " F ";
                } else if (gameBoard[i][j].equals("<?>")) {
                    gameBoard[i][j] = " ? ";
                } else if (gameBoard[i][j].equals("< >")) {
                    gameBoard[i][j] = "   ";
                }
            }
        }
    }

    /**
     * This method checks to see if a spot is a mine, by checking the row and columns passed
     * into the method to the row and column of the {@code mines} array.
     *@param row the row of the spot.
     *@param column the column for the spot.
     *@return a boolean value that indicates if the spot is a mine.
     */
    private boolean isMine(int row, int column) {
        /*the location is a mine this will return true if not this will return false*/
        return mines[row][column];
    }

    /**
     * a method the returns true if
     * the current state of the game indicates that
     * a user has wone the game. This is detemined if the number of rounds
     * completed is greater than or equal to rows * columns and if the number
     * of correctly revealed square is equal to rows * columns.
     * @return isWon a boolean value to indicate whether a game is one or not
     */
    public boolean isWon() {
        /*checks if winning conditions are met*/
        if ((rounds >= columns * rows) && (revealed == columns * rows)) {
            isWon = true;
            /*prints the win screen*/
            this.printWin();
        } // if
        return this.isWon;
    } // isWon

    /**
     * This method prints the text for when a user wins a game. The method uses a
     * a scanner to read the text for the winning screen from "resources/gamewon.txt" and
     * prints the score using the {@code getScore()} method and exits the program
     * gracefully.
     */
    public void printWin() {
        try {
            String winString = "\n";
            File win = new File ("resources/gamewon.txt");
            fileRead = new Scanner(win);
            while (fileRead.hasNextLine()) {
                winString += fileRead.nextLine();
                if (fileRead.hasNextLine()) {
                    /*If scanner has more lines to print
                      it prints then on a new line*/
                    winString += "\n";
                } else {
                    /*Does not add the extra new line so the score can be formatted correctly
                      on the win screen */
                    String score = String.format("%.2f", this.getScore());
                    winString += " " + score;
                } // if
            } // while
            System.out.println(winString + "\n");
            System.exit(0);

            /*Unlike to every reach this catch block unless
              gamewon.txt is moved*/
        } catch (FileNotFoundException fnfe) {
            System.err.println();
            System.err.println(fnfe.getMessage());
        } // try
    } // printWin

    /**
     * This method prints the text for when a mine location is revealed. Uses a scanner
     * to print the text from the resources/gameover.txt and the system exits gracefully.
     */
    public void printLoss() {
        System.out.println();
        try {
            File loss = new File("resources/gameover.txt");
            fileRead = new Scanner(loss);
            while (fileRead.hasNext()) {
                System.out.println(fileRead.nextLine());
            } // while
            System.out.println();
            System.exit(0);
        } catch (FileNotFoundException fnfe) {
            System.err.println();
            System.err.println(fnfe.getMessage());
        } // try
    } // printLoss

    /**
     * This method returns the number of adjacent mines
     * for a spot on {@code gameBoard}.
     *@param row the row number for the index to be checked.
     *@param column the column number for the index to be checked.
     *@return num number of adjeacent mines for the index.
     */
    private int getNumAdjMines(int row, int column) {
        int count = 0;
        int i = 0;
        int j = 0;
        for (boolean[] b : mines) {
            for (boolean c : b) {
                if (c == true) {
                    /*A square is adjacent if the absolute value of the two index is less than or
                      equal to one. So this line checks to see if that condition is met plus the
                      condition that the ajacent spot is a mine*/
                    if ((Math.abs(row - i) <= 1) && (Math.abs(column - j) <= 1)) {
                        count++;
                    } // if
                } // if
                j++;
            } //for
            i++;
            j = 0;
        } // for
        return count;
    } // getNumAdjMines

    /**
     * This method checks to see if an array index is within the bounds of
     * the gameBoard array by checking if it is in bounds of the minefield array
     * they are the same size.
     *@param row the row number of the index being checked.
     *@param column the column number of the index checked.
     *@return inBounds a boolean value indicating if an array index is in bounds.
     *@throws ArrayIndexOutOfBoundsException if the array index is not in bounds.
     */
    private boolean isInBounds(int row, int column) throws ArrayIndexOutOfBoundsException {
        return mines[row][column];
    } // isInBounds

    /**
     * This method fills the {@code gameBoard} array
     * with strings of 3 whitspaces.
     */
    public void makeGameboard() {
        for (int i = 0; i < this.gameBoard.length; i++) {
            for (int j = 0; j <  this.gameBoard[i].length; j++) {
                gameBoard[i][j] = "   ";
            }
        }
    } // makeGameBoard

    /**
     * This method returns the rounds completed in the game.
     * @return rounds the amount of rounds that have been played
     * in the game so far.
     */
    public int getRounds() {
        return rounds;
    } // getRounds

    /**
     *Main loop for the game that iterates until a win or loss.
     * Once the game is won the loop breaks.
     */
    public void play () {
        this.printWelcome();
        while (!this.isWon()) {
            this.promptUser();
        }
    } // end play

    /**
     * This method returns the score of the user at the end of the game.
     *@return score the score at the end of the game.
     */
    public double getScore() {
        this.score = (this.rows * this.columns * 100.0) / rounds;
        return score;
    }
} // MinesweeperGame
