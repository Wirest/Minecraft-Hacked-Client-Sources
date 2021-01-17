// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.List;
import java.util.ArrayList;
import com.google.common.collect.Iterators;
import java.util.HashSet;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.util.BlockPos;
import net.minecraft.world.NextTickListEntry;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.util.LongHashMap;
import java.util.TreeSet;

public class NextTickHashSet extends TreeSet
{
    private LongHashMap longHashMap;
    private int minX;
    private int minZ;
    private int maxX;
    private int maxZ;
    private static final int UNDEFINED = Integer.MIN_VALUE;
    
    public NextTickHashSet(final Set p_i70_1_) {
        this.longHashMap = new LongHashMap();
        this.minX = Integer.MIN_VALUE;
        this.minZ = Integer.MIN_VALUE;
        this.maxX = Integer.MIN_VALUE;
        this.maxZ = Integer.MIN_VALUE;
        for (final Object object : p_i70_1_) {
            this.add(object);
        }
    }
    
    @Override
    public boolean contains(final Object p_contains_1_) {
        if (!(p_contains_1_ instanceof NextTickListEntry)) {
            return false;
        }
        final NextTickListEntry nextticklistentry = (NextTickListEntry)p_contains_1_;
        final Set set = this.getSubSet(nextticklistentry, false);
        return set != null && set.contains(nextticklistentry);
    }
    
    @Override
    public boolean add(final Object p_add_1_) {
        if (!(p_add_1_ instanceof NextTickListEntry)) {
            return false;
        }
        final NextTickListEntry nextticklistentry = (NextTickListEntry)p_add_1_;
        if (nextticklistentry == null) {
            return false;
        }
        final Set set = this.getSubSet(nextticklistentry, true);
        final boolean flag = set.add(nextticklistentry);
        final boolean flag2 = super.add(p_add_1_);
        if (flag != flag2) {
            throw new IllegalStateException("Added: " + flag + ", addedParent: " + flag2);
        }
        return flag2;
    }
    
    @Override
    public boolean remove(final Object p_remove_1_) {
        if (!(p_remove_1_ instanceof NextTickListEntry)) {
            return false;
        }
        final NextTickListEntry nextticklistentry = (NextTickListEntry)p_remove_1_;
        final Set set = this.getSubSet(nextticklistentry, false);
        if (set == null) {
            return false;
        }
        final boolean flag = set.remove(nextticklistentry);
        final boolean flag2 = super.remove(nextticklistentry);
        if (flag != flag2) {
            throw new IllegalStateException("Added: " + flag + ", addedParent: " + flag2);
        }
        return flag2;
    }
    
    private Set getSubSet(final NextTickListEntry p_getSubSet_1_, final boolean p_getSubSet_2_) {
        if (p_getSubSet_1_ == null) {
            return null;
        }
        final BlockPos blockpos = p_getSubSet_1_.position;
        final int i = blockpos.getX() >> 4;
        final int j = blockpos.getZ() >> 4;
        return this.getSubSet(i, j, p_getSubSet_2_);
    }
    
    private Set getSubSet(final int p_getSubSet_1_, final int p_getSubSet_2_, final boolean p_getSubSet_3_) {
        final long i = ChunkCoordIntPair.chunkXZ2Int(p_getSubSet_1_, p_getSubSet_2_);
        HashSet hashset = (HashSet)this.longHashMap.getValueByKey(i);
        if (hashset == null && p_getSubSet_3_) {
            hashset = new HashSet();
            this.longHashMap.add(i, hashset);
        }
        return hashset;
    }
    
    @Override
    public Iterator iterator() {
        if (this.minX == Integer.MIN_VALUE) {
            return super.iterator();
        }
        if (this.size() <= 0) {
            return Iterators.emptyIterator();
        }
        final int i = this.minX >> 4;
        final int j = this.minZ >> 4;
        final int k = this.maxX >> 4;
        final int l = this.maxZ >> 4;
        final List list = new ArrayList();
        for (int i2 = i; i2 <= k; ++i2) {
            for (int j2 = j; j2 <= l; ++j2) {
                final Set set = this.getSubSet(i2, j2, false);
                if (set != null) {
                    list.add(set.iterator());
                }
            }
        }
        if (list.size() <= 0) {
            return Iterators.emptyIterator();
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        return Iterators.concat(list.iterator());
    }
    
    public void setIteratorLimits(final int p_setIteratorLimits_1_, final int p_setIteratorLimits_2_, final int p_setIteratorLimits_3_, final int p_setIteratorLimits_4_) {
        this.minX = Math.min(p_setIteratorLimits_1_, p_setIteratorLimits_3_);
        this.minZ = Math.min(p_setIteratorLimits_2_, p_setIteratorLimits_4_);
        this.maxX = Math.max(p_setIteratorLimits_1_, p_setIteratorLimits_3_);
        this.maxZ = Math.max(p_setIteratorLimits_2_, p_setIteratorLimits_4_);
    }
    
    public void clearIteratorLimits() {
        this.minX = Integer.MIN_VALUE;
        this.minZ = Integer.MIN_VALUE;
        this.maxX = Integer.MIN_VALUE;
        this.maxZ = Integer.MIN_VALUE;
    }
}
