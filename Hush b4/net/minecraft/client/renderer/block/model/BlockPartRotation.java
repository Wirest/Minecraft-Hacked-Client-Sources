// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Vector3f;

public class BlockPartRotation
{
    public final Vector3f origin;
    public final EnumFacing.Axis axis;
    public final float angle;
    public final boolean rescale;
    
    public BlockPartRotation(final Vector3f originIn, final EnumFacing.Axis axisIn, final float angleIn, final boolean rescaleIn) {
        this.origin = originIn;
        this.axis = axisIn;
        this.angle = angleIn;
        this.rescale = rescaleIn;
    }
}
