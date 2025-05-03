package grids;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import grids.cells.AbstractCell;
import grids.rules.Copyable;

public abstract class AbstractGrid<T extends AbstractGrid<T, K>, K extends AbstractCell<K>> implements Iterable<K>, Copyable<T> {
    protected List<K> baseGrid;

    public AbstractGrid(List<K> baseGrid) {
        this.baseGrid = new ArrayList<>(baseGrid);
    }

    public K get(int index) {
        return baseGrid.get(index);
    }

    public void set(int index, int value) {
        baseGrid.get(index).setValue(value);
    }

    @Override
    public Iterator<K> iterator() {
        return baseGrid.iterator();
    }


    public abstract boolean isValid();

    public boolean isFull() {
        return baseGrid.stream().allMatch(cell -> cell.getValue() != null);
    }

    // @Override
    // public AbstractGrid clone() {
    //     List<AbstractCell> newGrid = new ArrayList<>();
    //     for (AbstractCell cell : this.baseGrid) {
    //         newGrid.add(cell.clone());
    //     }

    //     return new AbstractGrid(newGrid);
    // }

    


}
