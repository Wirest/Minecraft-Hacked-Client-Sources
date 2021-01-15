package net.minecraft.client.renderer.chunk;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.util.EnumFacing;

public class SetVisibility
{
    private static final int COUNT_FACES = EnumFacing.values().length;
    private final BitSet bitSet;

    public SetVisibility()
    {
        this.bitSet = new BitSet(COUNT_FACES * COUNT_FACES);
    }

    public void setManyVisible(Set p_178620_1_)
    {
        Iterator var2 = p_178620_1_.iterator();

        while (var2.hasNext())
        {
            EnumFacing var3 = (EnumFacing)var2.next();
            Iterator var4 = p_178620_1_.iterator();

            while (var4.hasNext())
            {
                EnumFacing var5 = (EnumFacing)var4.next();
                this.setVisible(var3, var5, true);
            }
        }
    }

    public void setVisible(EnumFacing facing, EnumFacing facing2, boolean p_178619_3_)
    {
        this.bitSet.set(facing.ordinal() + facing2.ordinal() * COUNT_FACES, p_178619_3_);
        this.bitSet.set(facing2.ordinal() + facing.ordinal() * COUNT_FACES, p_178619_3_);
    }

    public void setAllVisible(boolean visible)
    {
        this.bitSet.set(0, this.bitSet.size(), visible);
    }

    public boolean isVisible(EnumFacing facing, EnumFacing facing2)
    {
        return this.bitSet.get(facing.ordinal() + facing2.ordinal() * COUNT_FACES);
    }

    @Override
	public String toString()
    {
        StringBuilder var1 = new StringBuilder();
        var1.append(' ');
        EnumFacing[] var2 = EnumFacing.values();
        int var3 = var2.length;
        int var4;
        EnumFacing var5;

        for (var4 = 0; var4 < var3; ++var4)
        {
            var5 = var2[var4];
            var1.append(' ').append(var5.toString().toUpperCase().charAt(0));
        }

        var1.append('\n');
        var2 = EnumFacing.values();
        var3 = var2.length;

        for (var4 = 0; var4 < var3; ++var4)
        {
            var5 = var2[var4];
            var1.append(var5.toString().toUpperCase().charAt(0));
            EnumFacing[] var6 = EnumFacing.values();
            int var7 = var6.length;

            for (int var8 = 0; var8 < var7; ++var8)
            {
                EnumFacing var9 = var6[var8];

                if (var5 == var9)
                {
                    var1.append("  ");
                }
                else
                {
                    boolean var10 = this.isVisible(var5, var9);
                    var1.append(' ').append(var10 ? 'Y' : 'n');
                }
            }

            var1.append('\n');
        }

        return var1.toString();
    }
}
