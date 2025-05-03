package grids.rules;

import java.util.List;

import grids.cells.AbstractCell;

public interface Column<K extends AbstractCell<K>> {
    public int getColumnIndex(int cellIndex);
    public List<K> getColumn(int ColumnIndex);
}
