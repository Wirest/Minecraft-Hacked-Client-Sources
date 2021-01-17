// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.entity.EntityLivingBase;

public class CombatEntry
{
    private final DamageSource damageSrc;
    private final int field_94567_b;
    private final float damage;
    private final float health;
    private final String field_94566_e;
    private final float fallDistance;
    
    public CombatEntry(final DamageSource damageSrcIn, final int p_i1564_2_, final float healthAmount, final float damageAmount, final String p_i1564_5_, final float fallDistanceIn) {
        this.damageSrc = damageSrcIn;
        this.field_94567_b = p_i1564_2_;
        this.damage = damageAmount;
        this.health = healthAmount;
        this.field_94566_e = p_i1564_5_;
        this.fallDistance = fallDistanceIn;
    }
    
    public DamageSource getDamageSrc() {
        return this.damageSrc;
    }
    
    public float func_94563_c() {
        return this.damage;
    }
    
    public boolean isLivingDamageSrc() {
        return this.damageSrc.getEntity() instanceof EntityLivingBase;
    }
    
    public String func_94562_g() {
        return this.field_94566_e;
    }
    
    public IChatComponent getDamageSrcDisplayName() {
        return (this.getDamageSrc().getEntity() == null) ? null : this.getDamageSrc().getEntity().getDisplayName();
    }
    
    public float getDamageAmount() {
        return (this.damageSrc == DamageSource.outOfWorld) ? Float.MAX_VALUE : this.fallDistance;
    }
}
