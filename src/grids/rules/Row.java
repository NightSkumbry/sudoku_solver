package grids.rules;

import java.util.List;

import grids.cells.AbstractCell;

public interface Row<K extends AbstractCell<K>> {
    public int getRowIndex(int cellIndex);
    public List<K> getRow(int rowIndex);
    public int getIndexByRowIndex(int rowIndex, int cellIndex);
}
