package grids.rules;

import java.util.List;

import grids.cells.AbstractCell;

public interface Row<K extends AbstractCell<K>> {
    public int get_row_index();
    public List<AbstractCell<K>> get_row(int index);
}
