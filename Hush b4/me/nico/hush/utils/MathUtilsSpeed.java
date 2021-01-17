// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.utils;

import java.math.BigDecimal;

public class MathUtilsSpeed
{
    public boolean isEven(final int number) {
        return number % 2 == 0;
    }
    
    public double roundX(final double wert, final int stellen) {
        return Math.round(wert * Math.pow(10.0, stellen)) / Math.pow(10.0, stellen);
    }
    
    public double roundDown(final double wert, final int stellen) {
        return new BigDecimal(wert).setScale(stellen, 5).doubleValue();
    }
    
    public double addUntil(final double start, final double goal, final double steps) {
        if (start != goal) {
            if (start < goal - steps) {
                return steps;
            }
            if (start > goal - steps) {
                return goal - start;
            }
        }
        return goal - start;
    }
    
    public double removeUntil(final double start, final double goal, final double steps) {
        if (start != goal) {
            if (start > goal + steps) {
                return steps;
            }
            if (start < goal + steps) {
                return start - goal;
            }
        }
        return start - goal;
    }
    
    public double[] getBigger(final double first, final double second) {
        if (first > second) {
            return new double[] { first, second };
        }
        if (first < second) {
            return new double[] { second, first };
        }
        return new double[] { first, second };
    }
}
