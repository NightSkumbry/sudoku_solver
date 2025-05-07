package operations.steps;

import java.util.List;

import grids.AbstractGrid;
import grids.cells.AbstractCell;

public class RemovePointingStep<T extends AbstractGrid<T, K>, K extends AbstractCell<K>> extends AbstractOperationStep<T, K> {
    private int cellIndex;
    private int value;
    private List<Integer> reasonCellIndices;
    private List<Integer> pointingCellIndices;
    private List<Integer> toPurpleCellIndices;

    public RemovePointingStep(int cellIndex, int value, List<Integer> reasonCellIndices, List<Integer> pointingCellIndices, List<Integer> toPurpleCellIndices) {
        this.toPurpleCellIndices = toPurpleCellIndices;
        this.pointingCellIndices = pointingCellIndices;
        this.reasonCellIndices = reasonCellIndices;
        this.cellIndex = cellIndex;
        this.value = value;
    }

    public List<Integer> getToPurpleCellIndices() {
        return toPurpleCellIndices;
    }
    
    public List<Integer> getReasonCellIndices() {
        return reasonCellIndices;
    }

    public List<Integer> getPointingCellIndices() {
        return pointingCellIndices;
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
