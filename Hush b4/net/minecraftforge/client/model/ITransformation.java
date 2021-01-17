// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraftforge.client.model;

import net.minecraft.util.EnumFacing;
import javax.vecmath.Matrix4f;

public interface ITransformation
{
    Matrix4f getMatrix();
    
    EnumFacing rotate(final EnumFacing p0);
    
    int rotate(final EnumFacing p0, final int p1);
}
