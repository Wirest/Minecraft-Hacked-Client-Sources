// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.Vec3;

public class PositionTextureVertex
{
    public Vec3 vector3D;
    public float texturePositionX;
    public float texturePositionY;
    
    public PositionTextureVertex(final float p_i1158_1_, final float p_i1158_2_, final float p_i1158_3_, final float p_i1158_4_, final float p_i1158_5_) {
        this(new Vec3(p_i1158_1_, p_i1158_2_, p_i1158_3_), p_i1158_4_, p_i1158_5_);
    }
    
    public PositionTextureVertex setTexturePosition(final float p_78240_1_, final float p_78240_2_) {
        return new PositionTextureVertex(this, p_78240_1_, p_78240_2_);
    }
    
    public PositionTextureVertex(final PositionTextureVertex textureVertex, final float texturePositionXIn, final float texturePositionYIn) {
        this.vector3D = textureVertex.vector3D;
        this.texturePositionX = texturePositionXIn;
        this.texturePositionY = texturePositionYIn;
    }
    
    public PositionTextureVertex(final Vec3 vector3DIn, final float texturePositionXIn, final float texturePositionYIn) {
        this.vector3D = vector3DIn;
        this.texturePositionX = texturePositionXIn;
        this.texturePositionY = texturePositionYIn;
    }
}
