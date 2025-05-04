package operations.steps;

import grids.AbstractGrid;
import grids.cells.AbstractCell;

public class RemovePossibleStep<T extends AbstractGrid<T, K>, K extends AbstractCell<K>> extends AbstractOperationStep<T, K> {
    private int cellIndex;
    private int value;
    private int reasonCell;

    public RemovePossibleStep(int cellIndex, int value, int reasonCell) {
        this.cellIndex = cellIndex;
        this.value = value;
        this.reasonCell = reasonCell;
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

    public int getReasonCell() {
        return reasonCell;
    }
}
