package net.minecraft.util;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ClassUtils.Interfaces;

public class ClassInheratanceMultiMap extends AbstractSet
{
    private final Multimap field_180218_a = HashMultimap.create();
    private final Set field_180216_b = Sets.newIdentityHashSet();
    private final Class field_180217_c;
    private static final String __OBFID = "CL_00002266";

    public ClassInheratanceMultiMap(Class p_i45909_1_)
    {
        this.field_180217_c = p_i45909_1_;
        this.field_180216_b.add(p_i45909_1_);
    }

    public void func_180213_a(Class p_180213_1_)
    {
        Iterator var2 = this.field_180218_a.get(this.func_180212_a(p_180213_1_, false)).iterator();

        while (var2.hasNext())
        {
            Object var3 = var2.next();

            if (p_180213_1_.isAssignableFrom(var3.getClass()))
            {
                this.field_180218_a.put(p_180213_1_, var3);
            }
        }

        this.field_180216_b.add(p_180213_1_);
    }

    protected Class func_180212_a(Class p_180212_1_, boolean p_180212_2_)
    {
        Iterator var3 = ClassUtils.hierarchy(p_180212_1_, Interfaces.INCLUDE).iterator();
        Class var4;

        do
        {
            if (!var3.hasNext())
            {
                throw new IllegalArgumentException("Don\'t know how to search for " + p_180212_1_);
            }

            var4 = (Class)var3.next();
        }
        while (!this.field_180216_b.contains(var4));

        if (var4 == this.field_180217_c && p_180212_2_)
        {
            this.func_180213_a(p_180212_1_);
        }

        return var4;
    }

    public boolean add(Object p_add_1_)
    {
        Iterator var2 = this.field_180216_b.iterator();

        while (var2.hasNext())
        {
            Class var3 = (Class)var2.next();

            if (var3.isAssignableFrom(p_add_1_.getClass()))
            {
                this.field_180218_a.put(var3, p_add_1_);
            }
        }

        return true;
    }

    public boolean remove(Object p_remove_1_)
    {
        Object var2 = p_remove_1_;
        boolean var3 = false;
        Iterator var4 = this.field_180216_b.iterator();

        while (var4.hasNext())
        {
            Class var5 = (Class)var4.next();

            if (var5.isAssignableFrom(var2.getClass()))
            {
                var3 |= this.field_180218_a.remove(var5, var2);
            }
        }

        return var3;
    }

    public Iterable func_180215_b(final Class p_180215_1_)
    {
        return new Iterable()
        {
            private static final String __OBFID = "CL_00002265";
            public Iterator iterator()
            {
                Iterator var1 = ClassInheratanceMultiMap.this.field_180218_a.get(ClassInheratanceMultiMap.this.func_180212_a(p_180215_1_, true)).iterator();
                return Iterators.filter(var1, p_180215_1_);
            }
        };
    }

    public Iterator iterator()
    {
        final Iterator var1 = this.field_180218_a.get(this.field_180217_c).iterator();
        return new AbstractIterator()
        {
            private static final String __OBFID = "CL_00002264";
            protected Object computeNext()
            {
                return !var1.hasNext() ? this.endOfData() : var1.next();
            }
        };
    }

    public int size()
    {
        return this.field_180218_a.get(this.field_180217_c).size();
    }
}
