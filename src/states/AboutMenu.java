package states;

import java.util.LinkedHashMap;
import java.util.Map;

import util.Action;
import util.Printer;
import util.ProgramState;

public class AboutMenu extends AbstractState {

    public AboutMenu() {
        super(createMenuOptions());
    }

    private static Map<String, String> createMenuOptions() {
        Map<String, String> menuOptions = new LinkedHashMap<>();
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
            default:
                System.out.println("Invalid selection. Please try again.");
        }
        return Action.NOTHING;
    }

    @Override
    public void printSelectionMenu(String message) {
        System.out.println("\n\n\n" + message);
        System.out.println("About this program:\nAuthor: Night_Skumbry (https://t.me/night_skumbry)\nVersion: 0.1\nThis program is a Sudoku solver.\nIt can solve puzzles with explanations.");
        System.out.println("\nPlease select an option:");
        Printer.printSelectionMenu(selection);
    }


    private void backToMenu() {
        ProgramState.currentState = ProgramState.MENU;
    }
}
