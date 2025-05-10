import util.Action;
import util.ProgramState;

// TODO: history by cell changes
// TODO: statistics of used operations

public class App {
    public static void main(String[] args) throws Exception {
        printWelcomeMessage();
        while (true) {
            ProgramState.currentState.getState().printSelectionMenu();

            String input = util.Printer.getInput();

            Action action = ProgramState.currentState.getState().doSelection(input);
            if (action == Action.EXIT) {
                System.out.println("Exiting the program. Goodbye!");
                break;
            } else if (action == Action.NOTHING) {
                // Do nothing, just continue the loop
            } else {
                System.out.println("An unexpected action occurred: " + action);
            }
        }
    }


    private static void printWelcomeMessage() {
        System.out.println("Welcome to the Sudoku Solver!");
        System.out.println("This program will help you solve Sudoku puzzles.\n\n");
    }
}
