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

public class History<T extends AbstractGrid<T, K>, K extends AbstractCell<K>> {
    private List<AbstractOperation<T, K, ?>> history;
    private int currentIndex;
    private T grid;

    public History(T grid) {
        this.history = new ArrayList<>();
        this.history.add(new InitialCreateOperation<>(0));
        this.currentIndex = 0;
        this.grid = grid;
    }

    public int getNextOperationID() {
        return history.size();
    }

    public void addOperation(AbstractOperation<T, K, ?> operation) {
        history.add(operation);
    }

    public void printGrid() {
        history.get(currentIndex).printGrid(grid);
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
        options.putAll(history.get(currentIndex).getSelectionOptions(grid));
        return options;
    }

    public Action doSelection(String selection) {
        if (selection.equals("<")) {
            try {
                moveBack();
            }
            catch (IndexOutOfBoundsException e) {
                System.out.println("No previous operation to undo.");
            }
            return Action.NOTHING;
        } else if (selection.equals(">")) {
            try {
                moveForward();
            }
            catch (IndexOutOfBoundsException e) {
                System.out.println("No next operation to redo.");
            }
            return Action.NOTHING;
        } else {
            return history.get(currentIndex).doSelection(selection, grid);
        }
    }
}
