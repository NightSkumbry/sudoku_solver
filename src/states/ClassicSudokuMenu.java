package states;

import java.util.LinkedHashMap;
import java.util.Map;

import sudokues.ClassicSudoku;
import util.Action;
import util.ProgramState;
import util.Printer;

public class ClassicSudokuMenu extends AbstractState {
    public static ClassicSudoku sudoku;
    private static boolean isHistoryView = false;


    public ClassicSudokuMenu() {
        super(createMenuOptions());
    }

    private static Map<String, String> createMenuOptions() {
        Map<String, String> menuOptions = new LinkedHashMap<>();
        menuOptions.put("solve", "Generate a solution.");
        menuOptions.put("sudo_solve", "Calculate all solutions.");
        menuOptions.put("print", "Print Sudoku.");
        menuOptions.put("history", "Enter History view.");
        menuOptions.put("tm", "Back to main menu.");
        menuOptions.put("e", "Exit.");
        return menuOptions;
    }

    private static Map<String, String> createHistoryOptions() {
        Map<String, String> menuOptions = new LinkedHashMap<>();
        menuOptions.put("r", "Return.");
        return menuOptions;
    }

    @Override
    public Action doSelection(String selection) {
        if (sudoku == null) {
            System.out.println("No Sudoku loaded. Please load a Sudoku first.");
            backToMenu();
            return Action.NOTHING;
        }

        if (isHistoryView) {
            switch (selection) {
                case "r":
                    this.returnFromHistoryView();
                    break;
                default:
                    return sudoku.getHistory().doSelection(selection);
                }
            return Action.NOTHING;
        }

        else {
            switch (selection) {
                case "tm":
                    this.backToMenu();
                    break;
                case "e":
                    return Action.EXIT;
                case "solve":
                    this.solve();
                    break;
                case "sudo_solve":
                    this.sudoSolve();
                    break;
                case "print":
                    this.printSudoku();
                    break;
                case "history":
                    this.EnterHistoryView();
                    break;
                default:
                    System.out.println("Invalid selection. Please try again.");
            }
            return Action.NOTHING;
        }
    }

    @Override
    public void printSelectionMenu() {
        System.out.println("\n\n");
        this.printSudoku();

        Map<String, String> s = new LinkedHashMap<>();
        if (isHistoryView) s.putAll(sudoku.getHistory().getSelectionOptions());
        s.putAll(selection);

        Printer.printSelectionMenu(s);
    }


    private void backToMenu() {
        ProgramState.currentState = ProgramState.MENU;
    }

    private void solve() {
        sudoku.findOneSolution();
    }
    private void sudoSolve() {
        sudoku.findAllSolutions();
    }
    private void printSudoku() {
        if (sudoku != null) {
            System.out.println("Current Sudoku grid:\n");
            if (!isHistoryView) System.out.println(sudoku.toString());
            else sudoku.getHistory().printGrid();
        } else {
            System.out.println("No Sudoku loaded. Please load a Sudoku first.");
        }
    }
    private void EnterHistoryView() {
        isHistoryView = true;
        selection = createHistoryOptions();
    }
    private void returnFromHistoryView() {
        isHistoryView = false;
        selection = createMenuOptions();
    }
}
