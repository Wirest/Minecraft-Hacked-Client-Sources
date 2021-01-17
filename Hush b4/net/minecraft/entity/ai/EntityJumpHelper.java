// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityJumpHelper
{
    private EntityLiving entity;
    protected boolean isJumping;
    
    public EntityJumpHelper(final EntityLiving entityIn) {
        this.entity = entityIn;
    }
    
    public void setJumping() {
        this.isJumping = true;
    }
    
    public void doJump() {
        this.entity.setJumping(this.isJumping);
        this.isJumping = false;
    }
}
