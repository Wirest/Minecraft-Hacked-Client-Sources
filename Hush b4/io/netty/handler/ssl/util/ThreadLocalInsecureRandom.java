// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.ssl.util;

import io.netty.util.internal.ThreadLocalRandom;
import java.util.Random;
import java.security.SecureRandom;

final class ThreadLocalInsecureRandom extends SecureRandom
{
    private static final long serialVersionUID = -8209473337192526191L;
    private static final SecureRandom INSTANCE;
    
    static SecureRandom current() {
        return ThreadLocalInsecureRandom.INSTANCE;
    }
    
    private ThreadLocalInsecureRandom() {
    }
    
    @Override
    public String getAlgorithm() {
        return "insecure";
    }
    
    @Override
    public void setSeed(final byte[] seed) {
    }
    
    @Override
    public void setSeed(final long seed) {
    }
    
    @Override
    public void nextBytes(final byte[] bytes) {
        random().nextBytes(bytes);
    }
    
    @Override
    public byte[] generateSeed(final int numBytes) {
        final byte[] seed = new byte[numBytes];
        random().nextBytes(seed);
        return seed;
    }
    
    @Override
    public int nextInt() {
        return random().nextInt();
    }
    
    @Override
    public int nextInt(final int n) {
        return random().nextInt(n);
    }
    
    @Override
    public boolean nextBoolean() {
        return random().nextBoolean();
    }
    
    @Override
    public long nextLong() {
        return random().nextLong();
    }
    
    @Override
    public float nextFloat() {
        return random().nextFloat();
    }
    
    @Override
    public double nextDouble() {
        return random().nextDouble();
    }
    
    @Override
    public double nextGaussian() {
        return random().nextGaussian();
    }
    
    private static Random random() {
        return ThreadLocalRandom.current();
    }
    
    static {
        INSTANCE = new ThreadLocalInsecureRandom();
    }
}
