// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.material;

public class MaterialTransparent extends Material
{
    public MaterialTransparent(final MapColor color) {
        super(color);
        this.setReplaceable();
    }
    
    @Override
    public boolean isSolid() {
        return false;
    }
    
    @Override
    public boolean blocksLight() {
        return false;
    }
    
    @Override
    public boolean blocksMovement() {
        return false;
    }
}
