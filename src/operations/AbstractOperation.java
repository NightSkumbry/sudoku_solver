package operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import grids.AbstractGrid;
import grids.cells.AbstractCell;
import operations.steps.AbstractOperationStep;
import util.Action;


public abstract class AbstractOperation<T extends AbstractGrid<T, K>, K extends AbstractCell<K>, L extends AbstractOperationStep<T, K>> {
    protected List<L> steps;

    public AbstractOperation() {
        this.steps = new ArrayList<>();
    }

    public int apply(T grid) {
        for (L step : steps) {
            step.apply(grid);
        }
        return 0;
    }

    public void undo(T grid) {
        List<L> reversedSteps = new ArrayList<>(steps);
        Collections.reverse(reversedSteps);
        for (L step : reversedSteps) {
            step.undo(grid);
        }
    }

    public void addStep(L step) {
        steps.add(step);
    }

    public abstract Map<String, String> getSelectionOptions(T grid);

    public abstract Action doSelection(String selection, T grid);

    public abstract void printGrid(T grid);

    public abstract void completeInitialization();

}
