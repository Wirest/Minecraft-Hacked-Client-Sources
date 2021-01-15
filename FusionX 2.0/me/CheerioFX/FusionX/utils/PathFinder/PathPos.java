// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils.PathFinder;

import net.minecraft.util.Vec3i;
import net.minecraft.util.BlockPos;

public class PathPos extends BlockPos
{
    private final boolean jumping;
    
    public PathPos(final BlockPos pos) {
        this(pos, false);
    }
    
    public PathPos(final BlockPos pos, final boolean jumping) {
        super(pos);
        this.jumping = jumping;
    }
    
    public boolean isJumping() {
        return this.jumping;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PathPos)) {
            return false;
        }
        final PathPos node = (PathPos)obj;
        return this.getX() == node.getX() && this.getY() == node.getY() && this.getZ() == node.getZ() && this.isJumping() == node.isJumping();
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() * 2 + (this.isJumping() ? 1 : 0);
    }
}
