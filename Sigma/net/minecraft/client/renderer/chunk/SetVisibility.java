package net.minecraft.client.renderer.chunk;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.util.EnumFacing;

public class SetVisibility {
    private static final int field_178623_a = EnumFacing.values().length;
    private final BitSet field_178622_b;
    private static final String __OBFID = "CL_00002448";

    public SetVisibility() {
        this.field_178622_b = new BitSet(field_178623_a * field_178623_a);
    }

    public void func_178620_a(Set p_178620_1_) {
        Iterator var2 = p_178620_1_.iterator();

        while (var2.hasNext()) {
            EnumFacing var3 = (EnumFacing) var2.next();
            Iterator var4 = p_178620_1_.iterator();

            while (var4.hasNext()) {
                EnumFacing var5 = (EnumFacing) var4.next();
                this.func_178619_a(var3, var5, true);
            }
        }
    }

    public void func_178619_a(EnumFacing p_178619_1_, EnumFacing p_178619_2_, boolean p_178619_3_) {
        this.field_178622_b.set(p_178619_1_.ordinal() + p_178619_2_.ordinal() * field_178623_a, p_178619_3_);
        this.field_178622_b.set(p_178619_2_.ordinal() + p_178619_1_.ordinal() * field_178623_a, p_178619_3_);
    }

    public void func_178618_a(boolean p_178618_1_) {
        this.field_178622_b.set(0, this.field_178622_b.size(), p_178618_1_);
    }

    public boolean func_178621_a(EnumFacing p_178621_1_, EnumFacing p_178621_2_) {
        return this.field_178622_b.get(p_178621_1_.ordinal() + p_178621_2_.ordinal() * field_178623_a);
    }

    public String toString() {
        StringBuilder var1 = new StringBuilder();
        var1.append(' ');
        EnumFacing[] var2 = EnumFacing.values();
        int var3 = var2.length;
        int var4;
        EnumFacing var5;

        for (var4 = 0; var4 < var3; ++var4) {
            var5 = var2[var4];
            var1.append(' ').append(var5.toString().toUpperCase().charAt(0));
        }

        var1.append('\n');
        var2 = EnumFacing.values();
        var3 = var2.length;

        for (var4 = 0; var4 < var3; ++var4) {
            var5 = var2[var4];
            var1.append(var5.toString().toUpperCase().charAt(0));
            EnumFacing[] var6 = EnumFacing.values();
            int var7 = var6.length;

            for (int var8 = 0; var8 < var7; ++var8) {
                EnumFacing var9 = var6[var8];

                if (var5 == var9) {
                    var1.append("  ");
                } else {
                    boolean var10 = this.func_178621_a(var5, var9);
                    var1.append(' ').append((char) (var10 ? 'Y' : 'n'));
                }
            }

            var1.append('\n');
        }

        return var1.toString();
    }
}
