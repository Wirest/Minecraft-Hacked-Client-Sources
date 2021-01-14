package net.minecraft.util;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;

public class ObjectIntIdentityMap implements IObjectIntIterable {
    private final IdentityHashMap field_148749_a = new IdentityHashMap(512);
    private final List field_148748_b = Lists.newArrayList();
    private static final String __OBFID = "CL_00001203";

    public void put(Object key, int value) {
        field_148749_a.put(key, Integer.valueOf(value));

        while (field_148748_b.size() <= value) {
            field_148748_b.add((Object) null);
        }

        field_148748_b.set(value, key);
    }

    public int get(Object key) {
        Integer var2 = (Integer) field_148749_a.get(key);
        return var2 == null ? -1 : var2.intValue();
    }

    public final Object getByValue(int value) {
        return value >= 0 && value < field_148748_b.size() ? field_148748_b.get(value) : null;
    }

    @Override
    public Iterator iterator() {
        return Iterators.filter(field_148748_b.iterator(), Predicates.notNull());
    }
}
