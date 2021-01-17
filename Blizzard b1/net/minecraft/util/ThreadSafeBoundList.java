package net.minecraft.util;

import java.lang.reflect.Array;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeBoundList
{
    private final Object[] field_152759_a;
    private final Class field_152760_b;
    private final ReadWriteLock field_152761_c = new ReentrantReadWriteLock();
    private int field_152762_d;
    private int field_152763_e;
    private static final String __OBFID = "CL_00001868";

    public ThreadSafeBoundList(Class p_i1126_1_, int p_i1126_2_)
    {
        this.field_152760_b = p_i1126_1_;
        this.field_152759_a = (Object[])((Object[])Array.newInstance(p_i1126_1_, p_i1126_2_));
    }

    public Object func_152757_a(Object p_152757_1_)
    {
        this.field_152761_c.writeLock().lock();
        this.field_152759_a[this.field_152763_e] = p_152757_1_;
        this.field_152763_e = (this.field_152763_e + 1) % this.func_152758_b();

        if (this.field_152762_d < this.func_152758_b())
        {
            ++this.field_152762_d;
        }

        this.field_152761_c.writeLock().unlock();
        return p_152757_1_;
    }

    public int func_152758_b()
    {
        this.field_152761_c.readLock().lock();
        int var1 = this.field_152759_a.length;
        this.field_152761_c.readLock().unlock();
        return var1;
    }

    public Object[] func_152756_c()
    {
        Object[] var1 = (Object[])((Object[])Array.newInstance(this.field_152760_b, this.field_152762_d));
        this.field_152761_c.readLock().lock();

        for (int var2 = 0; var2 < this.field_152762_d; ++var2)
        {
            int var3 = (this.field_152763_e - this.field_152762_d + var2) % this.func_152758_b();

            if (var3 < 0)
            {
                var3 += this.func_152758_b();
            }

            var1[var2] = this.field_152759_a[var3];
        }

        this.field_152761_c.readLock().unlock();
        return var1;
    }
}
