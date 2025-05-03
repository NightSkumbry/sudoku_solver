package sudokues;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import grids.AbstractGrid;
import grids.cells.AbstractCell;
import sudokues.history.Branch;
import sudokues.history.History;

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

    @Override
    public String toString() {
        return currentGrid.toString();
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
