package grids.rules;

import java.util.List;

import grids.cells.AbstractCell;

public interface MainDiagonal<K extends AbstractCell<K>> {
    public boolean isOnMainDiagonal(int cellIndex);
    public List<K> getMainDiagonal();
    public int getIndexOnMainDiagonal(int cellIndex);
}
