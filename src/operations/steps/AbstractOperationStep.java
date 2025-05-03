package operations.steps;

import grids.AbstractGrid;
import grids.cells.AbstractCell;

public abstract class AbstractOperationStep<T extends AbstractGrid<T, K>, K extends AbstractCell<K>> {
    public abstract void apply(AbstractGrid<T, K> grid);
    public abstract void undo(AbstractGrid<T, K> grid);
}
