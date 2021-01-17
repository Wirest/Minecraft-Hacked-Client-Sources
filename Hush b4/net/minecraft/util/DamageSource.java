// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.world.Explosion;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class DamageSource
{
    public static DamageSource inFire;
    public static DamageSource lightningBolt;
    public static DamageSource onFire;
    public static DamageSource lava;
    public static DamageSource inWall;
    public static DamageSource drown;
    public static DamageSource starve;
    public static DamageSource cactus;
    public static DamageSource fall;
    public static DamageSource outOfWorld;
    public static DamageSource generic;
    public static DamageSource magic;
    public static DamageSource wither;
    public static DamageSource anvil;
    public static DamageSource fallingBlock;
    private boolean isUnblockable;
    private boolean isDamageAllowedInCreativeMode;
    private boolean damageIsAbsolute;
    private float hungerDamage;
    private boolean fireDamage;
    private boolean projectile;
    private boolean difficultyScaled;
    private boolean magicDamage;
    private boolean explosion;
    public String damageType;
    
    static {
        DamageSource.inFire = new DamageSource("inFire").setFireDamage();
        DamageSource.lightningBolt = new DamageSource("lightningBolt");
        DamageSource.onFire = new DamageSource("onFire").setDamageBypassesArmor().setFireDamage();
        DamageSource.lava = new DamageSource("lava").setFireDamage();
        DamageSource.inWall = new DamageSource("inWall").setDamageBypassesArmor();
        DamageSource.drown = new DamageSource("drown").setDamageBypassesArmor();
        DamageSource.starve = new DamageSource("starve").setDamageBypassesArmor().setDamageIsAbsolute();
        DamageSource.cactus = new DamageSource("cactus");
        DamageSource.fall = new DamageSource("fall").setDamageBypassesArmor();
        DamageSource.outOfWorld = new DamageSource("outOfWorld").setDamageBypassesArmor().setDamageAllowedInCreativeMode();
        DamageSource.generic = new DamageSource("generic").setDamageBypassesArmor();
        DamageSource.magic = new DamageSource("magic").setDamageBypassesArmor().setMagicDamage();
        DamageSource.wither = new DamageSource("wither").setDamageBypassesArmor();
        DamageSource.anvil = new DamageSource("anvil");
        DamageSource.fallingBlock = new DamageSource("fallingBlock");
    }
    
    public static DamageSource causeMobDamage(final EntityLivingBase mob) {
        return new EntityDamageSource("mob", mob);
    }
    
    public static DamageSource causePlayerDamage(final EntityPlayer player) {
        return new EntityDamageSource("player", player);
    }
    
    public static DamageSource causeArrowDamage(final EntityArrow arrow, final Entity p_76353_1_) {
        return new EntityDamageSourceIndirect("arrow", arrow, p_76353_1_).setProjectile();
    }
    
    public static DamageSource causeFireballDamage(final EntityFireball fireball, final Entity p_76362_1_) {
        return (p_76362_1_ == null) ? new EntityDamageSourceIndirect("onFire", fireball, fireball).setFireDamage().setProjectile() : new EntityDamageSourceIndirect("fireball", fireball, p_76362_1_).setFireDamage().setProjectile();
    }
    
    public static DamageSource causeThrownDamage(final Entity p_76356_0_, final Entity p_76356_1_) {
        return new EntityDamageSourceIndirect("thrown", p_76356_0_, p_76356_1_).setProjectile();
    }
    
    public static DamageSource causeIndirectMagicDamage(final Entity p_76354_0_, final Entity p_76354_1_) {
        return new EntityDamageSourceIndirect("indirectMagic", p_76354_0_, p_76354_1_).setDamageBypassesArmor().setMagicDamage();
    }
    
    public static DamageSource causeThornsDamage(final Entity p_92087_0_) {
        return new EntityDamageSource("thorns", p_92087_0_).setIsThornsDamage().setMagicDamage();
    }
    
    public static DamageSource setExplosionSource(final Explosion explosionIn) {
        return (explosionIn != null && explosionIn.getExplosivePlacedBy() != null) ? new EntityDamageSource("explosion.player", explosionIn.getExplosivePlacedBy()).setDifficultyScaled().setExplosion() : new DamageSource("explosion").setDifficultyScaled().setExplosion();
    }
    
    public boolean isProjectile() {
        return this.projectile;
    }
    
    public DamageSource setProjectile() {
        this.projectile = true;
        return this;
    }
    
    public boolean isExplosion() {
        return this.explosion;
    }
    
    public DamageSource setExplosion() {
        this.explosion = true;
        return this;
    }
    
    public boolean isUnblockable() {
        return this.isUnblockable;
    }
    
    public float getHungerDamage() {
        return this.hungerDamage;
    }
    
    public boolean canHarmInCreative() {
        return this.isDamageAllowedInCreativeMode;
    }
    
    public boolean isDamageAbsolute() {
        return this.damageIsAbsolute;
    }
    
    protected DamageSource(final String damageTypeIn) {
        this.hungerDamage = 0.3f;
        this.damageType = damageTypeIn;
    }
    
    public Entity getSourceOfDamage() {
        return this.getEntity();
    }
    
    public Entity getEntity() {
        return null;
    }
    
    protected DamageSource setDamageBypassesArmor() {
        this.isUnblockable = true;
        this.hungerDamage = 0.0f;
        return this;
    }
    
    protected DamageSource setDamageAllowedInCreativeMode() {
        this.isDamageAllowedInCreativeMode = true;
        return this;
    }
    
    protected DamageSource setDamageIsAbsolute() {
        this.damageIsAbsolute = true;
        this.hungerDamage = 0.0f;
        return this;
    }
    
    protected DamageSource setFireDamage() {
        this.fireDamage = true;
        return this;
    }
    
    public IChatComponent getDeathMessage(final EntityLivingBase p_151519_1_) {
        final EntityLivingBase entitylivingbase = p_151519_1_.func_94060_bK();
        final String s = "death.attack." + this.damageType;
        final String s2 = String.valueOf(s) + ".player";
        return (entitylivingbase != null && StatCollector.canTranslate(s2)) ? new ChatComponentTranslation(s2, new Object[] { p_151519_1_.getDisplayName(), entitylivingbase.getDisplayName() }) : new ChatComponentTranslation(s, new Object[] { p_151519_1_.getDisplayName() });
    }
    
    public boolean isFireDamage() {
        return this.fireDamage;
    }
    
    public String getDamageType() {
        return this.damageType;
    }
    
    public DamageSource setDifficultyScaled() {
        this.difficultyScaled = true;
        return this;
    }
    
    public boolean isDifficultyScaled() {
        return this.difficultyScaled;
    }
    
    public boolean isMagicDamage() {
        return this.magicDamage;
    }
    
    public DamageSource setMagicDamage() {
        this.magicDamage = true;
        return this;
    }
    
    public boolean isCreativePlayer() {
        final Entity entity = this.getEntity();
        return entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode;
    }
}
