package operations.steps;

import grids.AbstractGrid;
import grids.cells.AbstractCell;

public class PlaceObviousStep<T extends AbstractGrid<T, K>, K extends AbstractCell<K>> extends AbstractOperationStep<T, K> {
    private int cellIndex;
    private int value;

    public PlaceObviousStep(int cellIndex, int value) {
        this.cellIndex = cellIndex;
        this.value = value;
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
