// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.material;

public class MaterialLogic extends Material
{
    public MaterialLogic(final MapColor color) {
        super(color);
        this.setAdventureModeExempt();
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
