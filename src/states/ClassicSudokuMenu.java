package states;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import util.Action;
import util.ProgramState;
import util.Printer;

public class ClassicSudokuMenu extends AbstractState {
    


    public ClassicSudokuMenu() {
        super(createMenuOptions());
    }

    private static Map<String, String> createMenuOptions() {
        Map<String, String> menuOptions = new LinkedHashMap<>();
        menuOptions.put("classic", "Classic Sudoku.");
        menuOptions.put("tm", "Back to main menu.");
        menuOptions.put("e", "Exit.");
        return menuOptions;
    }

    @Override
    public Action doSelection(String selectionIndex) {
        switch (selectionIndex) {
            case "tm":
                this.backToMenu();
                break;
            case "e":
                return Action.EXIT;
            case "classic":
                this.loadClassicSudoku();
                break;
            default:
                System.out.println("Invalid selection. Please try again.");
        }
        return Action.NOTHING;
    }


    private void backToMenu() {
        ProgramState.currentState = ProgramState.MENU;
    }

    private void loadClassicSudoku() {
        System.out.println("Sudoku should be written as nine lines of nine digits, separated by spaces.");
        System.out.println("Use '0' for empty cells.");
        System.out.println("Empty line means input interrupt.");

        List<int[]> buffer = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            while (true) {
                String line = Printer.getInput();

                if (line.isEmpty()) {
                    System.out.println("Input interrupted.");
                    return;
                }

                String[] splitLine = line.split("\\s+");
                if (splitLine.length != 9) {
                    System.out.println("Invalid input. Please enter exactly nine digits.");
                    continue;
                }

                try {
                    int[] row = new int[9];
                    boolean valid = true;

                    for (int j = 0; j < 9; j++) {
                        if (!splitLine[j].matches("\\d")) {
                            valid = false;
                            break;
                        }
                        row[j] = Integer.parseInt(splitLine[j]);
                    }

                    if (!valid) {
                        System.out.println("Invalid input. Please enter digits only.");
                        continue;
                    }

                    buffer.add(row);
                    break;
                }
                catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter nine digits separated by spaces.");
                }
            }
        }

        ProgramState.currentState = ProgramState.CLASSIC_SUDOKU;
    }
}
