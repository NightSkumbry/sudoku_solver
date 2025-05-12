package grids.rules;

import java.util.List;

import grids.cells.AbstractCell;

public interface SideDiagonal<K extends AbstractCell<K>> {
    public boolean isOnSideDiagonal(int cellIndex);
    public List<K> getSideDiagonal();
    public int getIndexOnSideDiagonal(int cellIndex);
}
