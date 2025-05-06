package grids.cells;

import java.util.HashSet;
import java.util.Set;

public class ClassicCell extends AbstractCell<ClassicCell> {

    protected ClassicCell(Integer value, boolean wasPrePlaced, Set<Integer> possibles) {
        super(new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'}, value, wasPrePlaced, possibles);
    }

    public ClassicCell(Integer value) {
        super(new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'}, value);
    }

    public ClassicCell() {
        super(new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'});
    }

    @Override
    public ClassicCell copy() {
        return new ClassicCell(this.valueId, this.wasPrePlaced, new HashSet<>(this.possibleValues));
    }
    
}
