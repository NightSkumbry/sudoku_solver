package grids.cells;

import java.util.HashSet;
import java.util.Set;

import grids.rules.Copyable;

public abstract class AbstractCell<K extends AbstractCell<K>> implements Copyable<K>{

    protected final char[] possibleValueChars;
    protected Integer valueId;
    protected Set<Integer> values;
    public final boolean wasPrePlaced;

    protected AbstractCell(char[] possibleValueChars, Integer value, boolean wasPrePlaced) {
        if (value != null && (value < 0 || value >= possibleValueChars.length)) {
            throw new IllegalArgumentException("Value " + value + " is not in the list of possible values.");
        }

        this.possibleValueChars = possibleValueChars;
        this.valueId = value;
        this.values = new HashSet<>();
        this.wasPrePlaced = wasPrePlaced;

        if (value == null) {
            for (int i = 0; i < possibleValueChars.length; i++) {
                this.values.add(i);
            }
        }
    }

    public AbstractCell(char[] possibleValueChars, Integer value) {
        this(possibleValueChars, value, true);
    }

    public AbstractCell(char[] possibleValueChars) {
        this(possibleValueChars, null, false);
    }

    @Override
    public String toString() {
        return (valueId != null) ? Character.toString(possibleValueChars[valueId]) : ".";
    }

    public Integer getValue() {
        return valueId;
    }

    public Set<Integer> getValues() {
        return values;
    }

    public boolean isSet() {
        return valueId != null;
    }

    public void setValue(int value) {
        if (!values.contains(value)) {
            throw new IllegalArgumentException("Value " + value + " is not in the list of possible values.");
        }
        this.valueId = value;
    }

    public void unsetValue() {
        this.valueId = null;
    }

    public boolean removePossibleValue(int value) {
        if (values.contains(value)) {
            values.remove(value);
            return true;
        }
        return false;
    }

    public boolean addPossibleValue(int value) {
        if (!values.contains(value) && value >= 0 && value < possibleValueChars.length) {
            values.add(value);
            return true;
        }
        return false;
    }
}
