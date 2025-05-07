package operations;

import java.util.HashMap;
import java.util.Map;

import grids.AbstractGrid;
import grids.cells.AbstractCell;
import operations.steps.AbstractOperationStep;
import util.Action;

public class InitialCreateOperation<T extends AbstractGrid<T, K>, K extends AbstractCell<K>> extends AbstractOperation<T, K, AbstractOperationStep<T, K>> {

    public InitialCreateOperation(int operationID) {
        super(operationID);
    }

    @Override
    public Map<String, String> getSelectionOptions(T grid) {
        return new HashMap<> ();
    }

    @Override
    public boolean isDoingNothing() {
        return false;
    }

    @Override
    public Action doSelection(String selection, T grid) {
        return Action.NOTHING;
    }

    @Override
    public void printGrid(T grid, int historySize) {
        System.out.println(grid.toString());
        System.out.println(this.operationID + "/" + historySize + ": Grid initialized.\n");
    }

    @Override
    public void completeInitialization() {  
        // No additional initialization needed for the initial operation
    }
}
