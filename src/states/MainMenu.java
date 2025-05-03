package states;

import java.util.LinkedHashMap;
import java.util.Map;

import util.Action;
import util.ProgramState;

public class MainMenu extends AbstractState {

    public MainMenu() {
        super(createMenuOptions());
    }

    private static Map<String, String> createMenuOptions() {
        Map<String, String> menuOptions = new LinkedHashMap<>();
        menuOptions.put("lc", "Load Sudoku from clipboard.");
        menuOptions.put("lf", "Load Sudoku from file.");
        menuOptions.put("a", "About.");
        menuOptions.put("e", "Exit.");
        return menuOptions;
    }

    @Override
    public Action doSelection(String selection) {
        switch (selection) {
            case "lc":
                this.loadSudokuFromClipboard();
                break;
            case "lf":
                System.out.println("Not ready yet..."); // TODO: implement this
                break;
            case "a":
                this.about();
                break;
            case "e":
                return Action.EXIT;
            default:
                System.out.println("Invalid selection. Please try again.");
        }
        return Action.NOTHING;
    }

    private void about() {
        ProgramState.currentState = ProgramState.ABOUT;
    }

    private void loadSudokuFromClipboard() {
        ProgramState.currentState = ProgramState.CLIPBOARD_TYPE_SELECTION;
    }
}
