package cs1302.game;

import cs1302.game.MinesweeperGame;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;

/**
 * A driver class for the {@code MinesweeperGame} class to play
 * a MinesweeperGame.
 */
public class MinesweeperDriver {

    /**
     * Main entry point of the minesweeper program.
     * uses {@code MinesweeperGame} objects to play a
     * minesweeper game. Takes in a seed file path via commandline arguments
     * stored in {@code args}. If there is 0 or more than 1 command line argument
     * supplied, an error message is printed and the program terminates with an exit
     * status of 1. Method creates the one and only standard input scanner and supplies
     * the {@code MinesweeperGame} constturctor with the scanner and the seed file path.
     * @param args an array of Strings taken from the command line.
     */
    public static void main(String[] args) {
        /*
         *checks if args.length if not 1 and terminates if not
         */
        if (args.length != 1) {
            System.err.println();
            System.err.println("Usage: MinesweeperDriver SEED_FILE_PATH");
            System.exit(1);
        } // if

        /*
         *creates a new scanner object that reads from standard input
         */
        Scanner stdInt = new Scanner(System.in);

        /*
         *Creates a new MinesweeperGame object and calls its play method
         */
        MinesweeperGame msg = new MinesweeperGame(stdInt, args[0]);
        msg.play();
    } // main
} // MinesweeperDriver
