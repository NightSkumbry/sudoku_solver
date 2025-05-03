package grids.cells;

public class ClassicCell extends AbstractCell<ClassicCell> {

    protected ClassicCell(Integer value, boolean wasPrePlaced) {
        super(new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'}, value, wasPrePlaced);
    }

    public ClassicCell(Integer value) {
        super(new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'}, value);
    }

    public ClassicCell() {
        super(new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'});
    }

    @Override
    public ClassicCell copy() {
        return new ClassicCell(this.valueId, this.wasPrePlaced);
    }
    
}
