package operations.steps;

import grids.AbstractGrid;
import grids.cells.AbstractCell;

public abstract class AbstractOperationStep<T extends AbstractGrid<T, K>, K extends AbstractCell<K>> {
    public abstract void apply(T grid);
    public abstract void undo(T grid);
}
