package grids.rules;

import java.util.List;

import grids.cells.AbstractCell;

public interface Box<K extends AbstractCell<K>> {
    public int getBoxIndex(int cellIndex);
    public List<K> getBox(int Boxindex);
    public int getIndexByBoxIndex(int boxIndex, int cellIndex);
}
