package me.rigamortis.faurax.values;

import java.util.*;

public class ValueManager
{
    public static ArrayList<Value> values;
    
    static {
        ValueManager.values = new ArrayList<Value>();
    }
    
    public Value getValue(final String name) {
        for (final Value value : ValueManager.values) {
            if (value.getName().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }
}
