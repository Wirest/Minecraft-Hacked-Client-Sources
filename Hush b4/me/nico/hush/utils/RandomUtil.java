// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.utils;

import io.netty.util.internal.ThreadLocalRandom;

public class RandomUtil
{
    public static final int randomInt() {
        return ThreadLocalRandom.current().nextInt();
    }
    
    public static final int randomInt(final int max) {
        return ThreadLocalRandom.current().nextInt(max);
    }
    
    public static final int randomInt(final int min, final int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
    
    public static final double randomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }
    
    public static final double randomDouble(final double max) {
        return ThreadLocalRandom.current().nextDouble(max);
    }
    
    public static final double randomDouble(final double min, final double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
    
    public static final float randomFloat() {
        return ThreadLocalRandom.current().nextFloat();
    }
    
    public static final float randomFloat(final float max) {
        return (float)ThreadLocalRandom.current().nextDouble(max);
    }
    
    public static final float randomFloat(final float min, final float max) {
        return (float)ThreadLocalRandom.current().nextDouble(min, max);
    }
    
    public static final long randomLong() {
        return ThreadLocalRandom.current().nextLong();
    }
    
    public static final long randomLong(final long max) {
        return ThreadLocalRandom.current().nextLong(max);
    }
    
    public static final long randomLong(final long min, final long max) {
        return ThreadLocalRandom.current().nextLong(min, max);
    }
    
    public static final float randomAngle(final float min, final float max) {
        return randomFloat(min, max);
    }
    
    public static final float randomAngle(final float max) {
        return randomFloat(0.0f, max);
    }
    
    public static final float randomAngle() {
        return randomFloat(0.0f, 360.0f);
    }
}
