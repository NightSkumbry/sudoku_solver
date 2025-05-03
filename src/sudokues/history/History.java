package sudokues.history;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grids.AbstractGrid;
import grids.cells.AbstractCell;
import operations.AbstractOperation;
import operations.InitialCreateOperation;
import util.Action;

public class History<L extends AbstractGrid<L, K>, K extends AbstractCell<K>> {
    private List<AbstractOperation<L, K>> history;
    private int currentIndex;
    private L grid;

    public History(L grid) {
        this.history = new ArrayList<>();
        this.history.add(new InitialCreateOperation<>());
        this.currentIndex = 0;
        this.grid = grid;
    }

    public void addOperation(AbstractOperation<L, K> operation) {
        history.add(operation);
    }

    public void moveBack() {
        if (currentIndex <= 0) {
            throw new IndexOutOfBoundsException("No previous operation to undo.");
        }
        history.get(currentIndex).undo(grid);
        currentIndex--;
    }

    public void moveForward() {
        if (currentIndex >= history.size() - 1) {
            throw new IndexOutOfBoundsException("No next operation to redo.");
        }
        currentIndex++;
        int offset = history.get(currentIndex).apply(grid);
        if (offset != 0) {
            currentIndex += offset;
            moveForward();
        }
    }

    public Map<String, String> getSelectionOptions() {
        HashMap<String, String> options = new HashMap<>();
        if (currentIndex > 0) {
            options.put("<", "Undo last operation");
        }
        if (currentIndex < history.size() - 1) {
            options.put(">", "Do next operation");
        }
        options.putAll(history.get(currentIndex).getSelectionOptions());
        return options;
    }

    public Action doSelection(String selection) {
        if (selection.equals("<")) {
            moveBack();
            return Action.NOTHING;
        } else if (selection.equals(">")) {
            moveForward();
            return Action.NOTHING;
        } else {
            return history.get(currentIndex).doSelection(selection);
        }
    }
}
