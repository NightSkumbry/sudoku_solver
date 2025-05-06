package grids.cells;

import java.util.HashSet;
import java.util.Set;

import grids.rules.Copyable;
import util.ConsoleColors;

public abstract class AbstractCell<K extends AbstractCell<K>> implements Copyable<K>{

    protected final char[] possibleValueChars;
    protected Integer valueId;
    protected Set<Integer> possibleValues;
    public final boolean wasPrePlaced;
    private ConsoleColors color;

    protected AbstractCell(char[] possibleValueChars, Integer value, boolean wasPrePlaced) {
        if (value != null && (value < 0 || value >= possibleValueChars.length)) {
            throw new IllegalArgumentException("Value " + value + " is not in the list of possible values.");
        }

        this.possibleValueChars = possibleValueChars;
        this.valueId = value;
        this.possibleValues = new HashSet<>();
        this.wasPrePlaced = wasPrePlaced;

        if (value == null) {
            for (int i = 0; i < possibleValueChars.length; i++) {
                this.possibleValues.add(i);
            }
        }
    }

    public AbstractCell(char[] possibleValueChars, Integer value) {
        this(possibleValueChars, value, true);
    }

    public AbstractCell(char[] possibleValueChars) {
        this(possibleValueChars, null, false);
    }

    public void setColor(ConsoleColors color) {
        this.color = color;
    }

    public char getChar(Integer value) {
        if (value == null) return '.';
        return possibleValueChars[value];
    }

    @Override
    public String toString() {
        if (color == null) {
            if (!wasPrePlaced) return (valueId != null) ? Character.toString(possibleValueChars[valueId]) : ".";
            return ConsoleColors.WHITE_UNDERLINED.getSequence() + ((valueId != null) ? Character.toString(possibleValueChars[valueId]) : ".") + ConsoleColors.RESET.getSequence();
        }
        String s;
        if (!wasPrePlaced) s = color.getSequence() + ((valueId != null) ? Character.toString(possibleValueChars[valueId]) : ".") + ConsoleColors.RESET.getSequence();
        else s = color.getUnderlinedVariant().getSequence() + ((valueId != null) ? Character.toString(possibleValueChars[valueId]) : ".") + ConsoleColors.RESET.getSequence();;
        color = null; // Reset color after use
        return s;
    }

    public Integer getValue() {
        return valueId;
    }

    public Set<Integer> getPossibleValues() {
        return possibleValues;
    }

    public boolean isSet() {
        return valueId != null;
    }

    public void setValue(int value) {
        if (!possibleValues.contains(value)) {
            throw new IllegalArgumentException("Value " + value + " is not in the list of possible values.");
        }
        this.valueId = value;
    }

    public void unsetValue() {
        this.valueId = null;
    }

    public boolean removePossibleValue(int value) {
        if (possibleValues.contains(value)) {
            possibleValues.remove(value);
            return true;
        }
        return false;
    }

    public boolean addPossibleValue(int value) {
        if (!possibleValues.contains(value) && value >= 0 && value < possibleValueChars.length) {
            possibleValues.add(value);
            return true;
        }
        return false;
    }
}
