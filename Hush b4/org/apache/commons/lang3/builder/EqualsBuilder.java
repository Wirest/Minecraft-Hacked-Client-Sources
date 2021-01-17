// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.builder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.ArrayUtils;
import java.lang.reflect.AccessibleObject;
import java.util.Collection;
import java.util.HashSet;
import org.apache.commons.lang3.tuple.Pair;
import java.util.Set;

public class EqualsBuilder implements Builder<Boolean>
{
    private static final ThreadLocal<Set<Pair<IDKey, IDKey>>> REGISTRY;
    private boolean isEquals;
    
    static Set<Pair<IDKey, IDKey>> getRegistry() {
        return EqualsBuilder.REGISTRY.get();
    }
    
    static Pair<IDKey, IDKey> getRegisterPair(final Object lhs, final Object rhs) {
        final IDKey left = new IDKey(lhs);
        final IDKey right = new IDKey(rhs);
        return Pair.of(left, right);
    }
    
    static boolean isRegistered(final Object lhs, final Object rhs) {
        final Set<Pair<IDKey, IDKey>> registry = getRegistry();
        final Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
        final Pair<IDKey, IDKey> swappedPair = Pair.of(pair.getLeft(), pair.getRight());
        return registry != null && (registry.contains(pair) || registry.contains(swappedPair));
    }
    
    static void register(final Object lhs, final Object rhs) {
        synchronized (EqualsBuilder.class) {
            if (getRegistry() == null) {
                EqualsBuilder.REGISTRY.set(new HashSet<Pair<IDKey, IDKey>>());
            }
        }
        final Set<Pair<IDKey, IDKey>> registry = getRegistry();
        final Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
        registry.add(pair);
    }
    
    static void unregister(final Object lhs, final Object rhs) {
        Set<Pair<IDKey, IDKey>> registry = getRegistry();
        if (registry != null) {
            final Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
            registry.remove(pair);
            synchronized (EqualsBuilder.class) {
                registry = getRegistry();
                if (registry != null && registry.isEmpty()) {
                    EqualsBuilder.REGISTRY.remove();
                }
            }
        }
    }
    
    public EqualsBuilder() {
        this.isEquals = true;
    }
    
    public static boolean reflectionEquals(final Object lhs, final Object rhs, final Collection<String> excludeFields) {
        return reflectionEquals(lhs, rhs, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
    }
    
    public static boolean reflectionEquals(final Object lhs, final Object rhs, final String... excludeFields) {
        return reflectionEquals(lhs, rhs, false, null, excludeFields);
    }
    
    public static boolean reflectionEquals(final Object lhs, final Object rhs, final boolean testTransients) {
        return reflectionEquals(lhs, rhs, testTransients, null, new String[0]);
    }
    
    public static boolean reflectionEquals(final Object lhs, final Object rhs, final boolean testTransients, final Class<?> reflectUpToClass, final String... excludeFields) {
        if (lhs == rhs) {
            return true;
        }
        if (lhs == null || rhs == null) {
            return false;
        }
        final Class<?> lhsClass = lhs.getClass();
        final Class<?> rhsClass = rhs.getClass();
        Class<?> testClass;
        if (lhsClass.isInstance(rhs)) {
            testClass = lhsClass;
            if (!rhsClass.isInstance(lhs)) {
                testClass = rhsClass;
            }
        }
        else {
            if (!rhsClass.isInstance(lhs)) {
                return false;
            }
            testClass = rhsClass;
            if (!lhsClass.isInstance(rhs)) {
                testClass = lhsClass;
            }
        }
        final EqualsBuilder equalsBuilder = new EqualsBuilder();
        try {
            if (testClass.isArray()) {
                equalsBuilder.append(lhs, rhs);
            }
            else {
                reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
                while (testClass.getSuperclass() != null && testClass != reflectUpToClass) {
                    testClass = testClass.getSuperclass();
                    reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
                }
            }
        }
        catch (IllegalArgumentException e) {
            return false;
        }
        return equalsBuilder.isEquals();
    }
    
    private static void reflectionAppend(final Object lhs, final Object rhs, final Class<?> clazz, final EqualsBuilder builder, final boolean useTransients, final String[] excludeFields) {
        if (isRegistered(lhs, rhs)) {
            return;
        }
        try {
            register(lhs, rhs);
            final Field[] fields = clazz.getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);
            for (int i = 0; i < fields.length && builder.isEquals; ++i) {
                final Field f = fields[i];
                if (!ArrayUtils.contains(excludeFields, f.getName()) && f.getName().indexOf(36) == -1 && (useTransients || !Modifier.isTransient(f.getModifiers())) && !Modifier.isStatic(f.getModifiers())) {
                    try {
                        builder.append(f.get(lhs), f.get(rhs));
                    }
                    catch (IllegalAccessException e) {
                        throw new InternalError("Unexpected IllegalAccessException");
                    }
                }
            }
        }
        finally {
            unregister(lhs, rhs);
        }
    }
    
    public EqualsBuilder appendSuper(final boolean superEquals) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = superEquals;
        return this;
    }
    
    public EqualsBuilder append(final Object lhs, final Object rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            this.setEquals(false);
            return this;
        }
        final Class<?> lhsClass = lhs.getClass();
        if (!lhsClass.isArray()) {
            this.isEquals = lhs.equals(rhs);
        }
        else if (lhs.getClass() != rhs.getClass()) {
            this.setEquals(false);
        }
        else if (lhs instanceof long[]) {
            this.append((long[])lhs, (long[])rhs);
        }
        else if (lhs instanceof int[]) {
            this.append((int[])lhs, (int[])rhs);
        }
        else if (lhs instanceof short[]) {
            this.append((short[])lhs, (short[])rhs);
        }
        else if (lhs instanceof char[]) {
            this.append((char[])lhs, (char[])rhs);
        }
        else if (lhs instanceof byte[]) {
            this.append((byte[])lhs, (byte[])rhs);
        }
        else if (lhs instanceof double[]) {
            this.append((double[])lhs, (double[])rhs);
        }
        else if (lhs instanceof float[]) {
            this.append((float[])lhs, (float[])rhs);
        }
        else if (lhs instanceof boolean[]) {
            this.append((boolean[])lhs, (boolean[])rhs);
        }
        else {
            this.append((Object[])lhs, (Object[])rhs);
        }
        return this;
    }
    
    public EqualsBuilder append(final long lhs, final long rhs) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = (lhs == rhs);
        return this;
    }
    
    public EqualsBuilder append(final int lhs, final int rhs) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = (lhs == rhs);
        return this;
    }
    
    public EqualsBuilder append(final short lhs, final short rhs) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = (lhs == rhs);
        return this;
    }
    
    public EqualsBuilder append(final char lhs, final char rhs) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = (lhs == rhs);
        return this;
    }
    
    public EqualsBuilder append(final byte lhs, final byte rhs) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = (lhs == rhs);
        return this;
    }
    
    public EqualsBuilder append(final double lhs, final double rhs) {
        if (!this.isEquals) {
            return this;
        }
        return this.append(Double.doubleToLongBits(lhs), Double.doubleToLongBits(rhs));
    }
    
    public EqualsBuilder append(final float lhs, final float rhs) {
        if (!this.isEquals) {
            return this;
        }
        return this.append(Float.floatToIntBits(lhs), Float.floatToIntBits(rhs));
    }
    
    public EqualsBuilder append(final boolean lhs, final boolean rhs) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = (lhs == rhs);
        return this;
    }
    
    public EqualsBuilder append(final Object[] lhs, final Object[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            this.setEquals(false);
            return this;
        }
        if (lhs.length != rhs.length) {
            this.setEquals(false);
            return this;
        }
        for (int i = 0; i < lhs.length && this.isEquals; ++i) {
            this.append(lhs[i], rhs[i]);
        }
        return this;
    }
    
    public EqualsBuilder append(final long[] lhs, final long[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            this.setEquals(false);
            return this;
        }
        if (lhs.length != rhs.length) {
            this.setEquals(false);
            return this;
        }
        for (int i = 0; i < lhs.length && this.isEquals; ++i) {
            this.append(lhs[i], rhs[i]);
        }
        return this;
    }
    
    public EqualsBuilder append(final int[] lhs, final int[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            this.setEquals(false);
            return this;
        }
        if (lhs.length != rhs.length) {
            this.setEquals(false);
            return this;
        }
        for (int i = 0; i < lhs.length && this.isEquals; ++i) {
            this.append(lhs[i], rhs[i]);
        }
        return this;
    }
    
    public EqualsBuilder append(final short[] lhs, final short[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            this.setEquals(false);
            return this;
        }
        if (lhs.length != rhs.length) {
            this.setEquals(false);
            return this;
        }
        for (int i = 0; i < lhs.length && this.isEquals; ++i) {
            this.append(lhs[i], rhs[i]);
        }
        return this;
    }
    
    public EqualsBuilder append(final char[] lhs, final char[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            this.setEquals(false);
            return this;
        }
        if (lhs.length != rhs.length) {
            this.setEquals(false);
            return this;
        }
        for (int i = 0; i < lhs.length && this.isEquals; ++i) {
            this.append(lhs[i], rhs[i]);
        }
        return this;
    }
    
    public EqualsBuilder append(final byte[] lhs, final byte[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            this.setEquals(false);
            return this;
        }
        if (lhs.length != rhs.length) {
            this.setEquals(false);
            return this;
        }
        for (int i = 0; i < lhs.length && this.isEquals; ++i) {
            this.append(lhs[i], rhs[i]);
        }
        return this;
    }
    
    public EqualsBuilder append(final double[] lhs, final double[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            this.setEquals(false);
            return this;
        }
        if (lhs.length != rhs.length) {
            this.setEquals(false);
            return this;
        }
        for (int i = 0; i < lhs.length && this.isEquals; ++i) {
            this.append(lhs[i], rhs[i]);
        }
        return this;
    }
    
    public EqualsBuilder append(final float[] lhs, final float[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            this.setEquals(false);
            return this;
        }
        if (lhs.length != rhs.length) {
            this.setEquals(false);
            return this;
        }
        for (int i = 0; i < lhs.length && this.isEquals; ++i) {
            this.append(lhs[i], rhs[i]);
        }
        return this;
    }
    
    public EqualsBuilder append(final boolean[] lhs, final boolean[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            this.setEquals(false);
            return this;
        }
        if (lhs.length != rhs.length) {
            this.setEquals(false);
            return this;
        }
        for (int i = 0; i < lhs.length && this.isEquals; ++i) {
            this.append(lhs[i], rhs[i]);
        }
        return this;
    }
    
    public boolean isEquals() {
        return this.isEquals;
    }
    
    @Override
    public Boolean build() {
        return this.isEquals();
    }
    
    protected void setEquals(final boolean isEquals) {
        this.isEquals = isEquals;
    }
    
    public void reset() {
        this.isEquals = true;
    }
    
    static {
        REGISTRY = new ThreadLocal<Set<Pair<IDKey, IDKey>>>();
    }
}
