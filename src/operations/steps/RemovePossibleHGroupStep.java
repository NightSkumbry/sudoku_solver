package operations.steps;

import java.util.List;

import grids.AbstractGrid;
import grids.cells.AbstractCell;

public class RemovePossibleHGroupStep<T extends AbstractGrid<T, K>, K extends AbstractCell<K>> extends AbstractOperationStep<T, K> {
    private int cellIndex;
    private int value;
    private List<Integer> groupCellIndices;
    private List<Integer> nonGroupCellIndices;
    private List<Integer> toPurple;

    public RemovePossibleHGroupStep(int cellIndex, int value, List<Integer> groupCellIndices, List<Integer> nonGroupCellIndices, List<Integer> toPurple) {
        this.toPurple = toPurple;
        this.nonGroupCellIndices = nonGroupCellIndices;
        this.groupCellIndices = groupCellIndices;
        this.cellIndex = cellIndex;
        this.value = value;
    }

    public List<Integer> getToPurple() {
        return toPurple;
    }

    public List<Integer> getGroupCellIndices() {
        return groupCellIndices;
    }

    public List<Integer> getNonGroupCellIndices() {
        return nonGroupCellIndices;
    }

    @Override
    public void apply(T grid) {
        grid.get(cellIndex).removePossibleValue(value);
    }

    @Override
    public void undo(T grid) {
        grid.get(cellIndex).addPossibleValue(value);
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public int getValue() {
        return value;
    }
}
