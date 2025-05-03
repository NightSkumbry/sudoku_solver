package sudokues;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grids.AbstractGrid;
import grids.cells.AbstractCell;
import operations.AbstractOperation;
import operations.InitialCreateOperation;
import util.Action;

public abstract class AbstractSudoku<T extends AbstractGrid<T, K>, K extends AbstractCell<K>> {
    protected T baseGrid;
    protected T currentGrid;
    protected int currentIndex;
    protected List<T> grids;
    protected Deque<Branch> openBranches;
    protected List<T> solutions;
    protected boolean fullySolved;
    protected boolean isValid;
    protected History<T, K> history;

    public AbstractSudoku(T grid) {
        this.baseGrid = grid;
        this.currentGrid = grid.copy();
        this.currentIndex = 0;
        this.grids = new ArrayList<>();
        this.grids.add(this.currentGrid);
        this.openBranches = new ArrayDeque<>();
        this.solutions = new ArrayList<>();
        this.fullySolved = grid.isFull() && grid.isValid();
        this.isValid = grid.isValid();
        this.history = new History<> (grid.copy());
    }

    public abstract void findOneSolution();

    public abstract void findAllSolutions();

    public void setCurrentGrid(int gridIndex) {
        this.currentGrid = this.grids.get(gridIndex);
        this.currentIndex = gridIndex;
    }

    public History<T, K> getHistory() {
        return history;
    }

    public T getCurrentGrid() {
        return currentGrid;
    }
}


record Branch(int startingGridIndex, int branchOnCell, List<Integer> branchesIndex, boolean isClosed) {
    public Branch(int startingGridIndex, int branchOnCell, List<Integer> branchesIndex) {
        this(startingGridIndex, branchOnCell, branchesIndex, false);
    }
}


class History<L extends AbstractGrid<L, K>, K extends AbstractCell<K>> {
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



