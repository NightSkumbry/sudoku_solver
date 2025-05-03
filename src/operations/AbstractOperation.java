package operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import grids.AbstractGrid;
import grids.cells.AbstractCell;
import operations.steps.AbstractOperationStep;
import util.Action;


public abstract class AbstractOperation<T extends AbstractGrid<T, K>, K extends AbstractCell<K>> {
    private List<AbstractOperationStep<T, K>> steps;

    public AbstractOperation() {
        this.steps = new ArrayList<>();
    }

    public int apply(AbstractGrid<T, K> grid) {
        for (AbstractOperationStep<T, K> step : steps) {
            step.apply(grid);
        }
        return 0;
    }

    public void undo(AbstractGrid<T, K> grid) {
        List<AbstractOperationStep<T, K>> reversedSteps = new ArrayList<>(steps);
        Collections.reverse(reversedSteps);
        for (AbstractOperationStep<T, K> step : reversedSteps) {
            step.undo(grid);
        }
    }

    public void addStep(AbstractOperationStep<T, K> step) {
        steps.add(step);
    }

    public abstract Map<String, String> getSelectionOptions();

    public abstract Action doSelection(String selection);

}
