package states;


import java.util.Map;

import util.Action;
import util.Printer;

public abstract class AbstractState {

    protected Map<String, String> selection;

    public AbstractState(Map<String, String> selection) {
        this.selection = selection;
    }

    public void printSelectionMenu() {
        System.out.println("\n\n\n");
        Printer.printSelectionMenu(selection);
    }

    public abstract Action doSelection(String selectionIndex);
}
