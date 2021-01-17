// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.util.Random;

public class EnchantmentNameParts
{
    private static final EnchantmentNameParts instance;
    private Random rand;
    private String[] namePartsArray;
    
    static {
        instance = new EnchantmentNameParts();
    }
    
    public EnchantmentNameParts() {
        this.rand = new Random();
        this.namePartsArray = "the elder scrolls klaatu berata niktu xyzzy bless curse light darkness fire air earth water hot dry cold wet ignite snuff embiggen twist shorten stretch fiddle destroy imbue galvanize enchant free limited range of towards inside sphere cube self other ball mental physical grow shrink demon elemental spirit animal creature beast humanoid undead fresh stale ".split(" ");
    }
    
    public static EnchantmentNameParts getInstance() {
        return EnchantmentNameParts.instance;
    }
    
    public String generateNewRandomName() {
        final int i = this.rand.nextInt(2) + 3;
        String s = "";
        for (int j = 0; j < i; ++j) {
            if (j > 0) {
                s = String.valueOf(s) + " ";
            }
            s = String.valueOf(s) + this.namePartsArray[this.rand.nextInt(this.namePartsArray.length)];
        }
        return s;
    }
    
    public void reseedRandomGenerator(final long seed) {
        this.rand.setSeed(seed);
    }
}
