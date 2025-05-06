package operations.steps;

import java.util.List;

import grids.AbstractGrid;
import grids.cells.AbstractCell;

public class SingleInStep<T extends AbstractGrid<T, K>, K extends AbstractCell<K>> extends AbstractOperationStep<T, K> {
    private int cellIndex;
    private int value;
    private List<Integer> reasonCellIndices;

    public SingleInStep(int cellIndex, int value, List<Integer> reasonCellIndices) {
        this.reasonCellIndices = reasonCellIndices;
        this.cellIndex = cellIndex;
        this.value = value;
    }
    
    public List<Integer> getReasonCellIndices() {
        return reasonCellIndices;
    }

    @Override
    public void apply(T grid) {
        grid.get(cellIndex).setValue(value);
    }

    @Override
    public void undo(T grid) {
        grid.get(cellIndex).unsetValue();
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public int getValue() {
        return value;
    }
}
