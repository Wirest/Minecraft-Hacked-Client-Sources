package cn.kody.debug.events;

import com.darkmagician6.eventapi.events.*;

import net.minecraft.entity.EntityLivingBase;

public class EventRenderPlayer implements Event, Cancellable
{
    private EntityLivingBase entity;
    private boolean pre;
    private float limbSwing;
    private float limbSwingAmount;
    private float ageInTicks;
    private float rotationYawHead;
    private float rotationPitch;
    private float chestRot;
    private float offset;
    public boolean cancelled;
    
    public EventRenderPlayer(EntityLivingBase entity, boolean pre, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYawHead, float rotationPitch, float chestRot, float offset) {
        super();
        this.entity = entity;
        this.pre = pre;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.ageInTicks = ageInTicks;
        this.rotationYawHead = rotationYawHead;
        this.rotationPitch = rotationPitch;
        this.chestRot = chestRot;
        this.offset = offset;
        this.cancelled = false;
    }
    
    public EventRenderPlayer(EntityLivingBase entity, boolean pre) {
        super();
        this.entity = entity;
        this.pre = pre;
    }
    
    public EntityLivingBase getEntity() {
        return this.entity;
    }
    
    public boolean isPre() {
        return this.pre;
    }
    
    public boolean isPost() {
        boolean b;
        if (!this.pre) {
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    public float getLimbSwing() {
        return this.limbSwing;
    }
    
    public void setLimbSwing(float limbSwing) {
        this.limbSwing = limbSwing;
    }
    
    public float getLimbSwingAmount() {
        return this.limbSwingAmount;
    }
    
    public void setLimbSwingAmount(float limbSwingAmount) {
        this.limbSwingAmount = limbSwingAmount;
    }
    
    public float getAgeInTicks() {
        return this.ageInTicks;
    }
    
    public void setAgeInTicks(float ageInTicks) {
        this.ageInTicks = ageInTicks;
    }
    
    public float getRotationYawHead() {
        return this.rotationYawHead;
    }
    
    public void setRotationYawHead(float rotationYawHead) {
        this.rotationYawHead = rotationYawHead;
    }
    
    public float getRotationPitch() {
        return this.rotationPitch;
    }
    
    public void setRotationPitch(float rotationPitch) {
        this.rotationPitch = rotationPitch;
    }
    
    public float getOffset() {
        return this.offset;
    }
    
    public void setOffset(float offset) {
        this.offset = offset;
    }
    
    public float getRotationChest() {
        return this.chestRot;
    }
    
    public void setRotationChest(float chestRot) {
        this.chestRot = chestRot;
    }
    
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
