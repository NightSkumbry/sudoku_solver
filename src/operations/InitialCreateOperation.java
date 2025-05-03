package operations;

import java.util.HashMap;
import java.util.Map;

import grids.AbstractGrid;
import grids.cells.AbstractCell;
import util.Action;

public class InitialCreateOperation<T extends AbstractGrid<T, K>, K extends AbstractCell<K>> extends AbstractOperation<T, K> {

    public InitialCreateOperation() {
        super();
    }

    @Override
    public Map<String, String> getSelectionOptions() {
        return new HashMap<> ();
    }

    @Override
    public Action doSelection(String selection) {
        return Action.NOTHING;
    }


}
