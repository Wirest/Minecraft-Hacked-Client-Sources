// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLivingBase;
import java.util.List;

public class CombatTracker
{
    private final List<CombatEntry> combatEntries;
    private final EntityLivingBase fighter;
    private int field_94555_c;
    private int field_152775_d;
    private int field_152776_e;
    private boolean field_94552_d;
    private boolean field_94553_e;
    private String field_94551_f;
    
    public CombatTracker(final EntityLivingBase fighterIn) {
        this.combatEntries = (List<CombatEntry>)Lists.newArrayList();
        this.fighter = fighterIn;
    }
    
    public void func_94545_a() {
        this.func_94542_g();
        if (this.fighter.isOnLadder()) {
            final Block block = this.fighter.worldObj.getBlockState(new BlockPos(this.fighter.posX, this.fighter.getEntityBoundingBox().minY, this.fighter.posZ)).getBlock();
            if (block == Blocks.ladder) {
                this.field_94551_f = "ladder";
            }
            else if (block == Blocks.vine) {
                this.field_94551_f = "vines";
            }
        }
        else if (this.fighter.isInWater()) {
            this.field_94551_f = "water";
        }
    }
    
    public void trackDamage(final DamageSource damageSrc, final float healthIn, final float damageAmount) {
        this.reset();
        this.func_94545_a();
        final CombatEntry combatentry = new CombatEntry(damageSrc, this.fighter.ticksExisted, healthIn, damageAmount, this.field_94551_f, this.fighter.fallDistance);
        this.combatEntries.add(combatentry);
        this.field_94555_c = this.fighter.ticksExisted;
        this.field_94553_e = true;
        if (combatentry.isLivingDamageSrc() && !this.field_94552_d && this.fighter.isEntityAlive()) {
            this.field_94552_d = true;
            this.field_152775_d = this.fighter.ticksExisted;
            this.field_152776_e = this.field_152775_d;
            this.fighter.sendEnterCombat();
        }
    }
    
    public IChatComponent getDeathMessage() {
        if (this.combatEntries.size() == 0) {
            return new ChatComponentTranslation("death.attack.generic", new Object[] { this.fighter.getDisplayName() });
        }
        final CombatEntry combatentry = this.func_94544_f();
        final CombatEntry combatentry2 = this.combatEntries.get(this.combatEntries.size() - 1);
        final IChatComponent ichatcomponent1 = combatentry2.getDamageSrcDisplayName();
        final Entity entity = combatentry2.getDamageSrc().getEntity();
        IChatComponent ichatcomponent3;
        if (combatentry != null && combatentry2.getDamageSrc() == DamageSource.fall) {
            final IChatComponent ichatcomponent2 = combatentry.getDamageSrcDisplayName();
            if (combatentry.getDamageSrc() != DamageSource.fall && combatentry.getDamageSrc() != DamageSource.outOfWorld) {
                if (ichatcomponent2 != null && (ichatcomponent1 == null || !ichatcomponent2.equals(ichatcomponent1))) {
                    final Entity entity2 = combatentry.getDamageSrc().getEntity();
                    final ItemStack itemstack1 = (entity2 instanceof EntityLivingBase) ? ((EntityLivingBase)entity2).getHeldItem() : null;
                    if (itemstack1 != null && itemstack1.hasDisplayName()) {
                        ichatcomponent3 = new ChatComponentTranslation("death.fell.assist.item", new Object[] { this.fighter.getDisplayName(), ichatcomponent2, itemstack1.getChatComponent() });
                    }
                    else {
                        ichatcomponent3 = new ChatComponentTranslation("death.fell.assist", new Object[] { this.fighter.getDisplayName(), ichatcomponent2 });
                    }
                }
                else if (ichatcomponent1 != null) {
                    final ItemStack itemstack2 = (entity instanceof EntityLivingBase) ? ((EntityLivingBase)entity).getHeldItem() : null;
                    if (itemstack2 != null && itemstack2.hasDisplayName()) {
                        ichatcomponent3 = new ChatComponentTranslation("death.fell.finish.item", new Object[] { this.fighter.getDisplayName(), ichatcomponent1, itemstack2.getChatComponent() });
                    }
                    else {
                        ichatcomponent3 = new ChatComponentTranslation("death.fell.finish", new Object[] { this.fighter.getDisplayName(), ichatcomponent1 });
                    }
                }
                else {
                    ichatcomponent3 = new ChatComponentTranslation("death.fell.killer", new Object[] { this.fighter.getDisplayName() });
                }
            }
            else {
                ichatcomponent3 = new ChatComponentTranslation("death.fell.accident." + this.func_94548_b(combatentry), new Object[] { this.fighter.getDisplayName() });
            }
        }
        else {
            ichatcomponent3 = combatentry2.getDamageSrc().getDeathMessage(this.fighter);
        }
        return ichatcomponent3;
    }
    
    public EntityLivingBase func_94550_c() {
        EntityLivingBase entitylivingbase = null;
        EntityPlayer entityplayer = null;
        float f = 0.0f;
        float f2 = 0.0f;
        for (final CombatEntry combatentry : this.combatEntries) {
            if (combatentry.getDamageSrc().getEntity() instanceof EntityPlayer && (entityplayer == null || combatentry.func_94563_c() > f2)) {
                f2 = combatentry.func_94563_c();
                entityplayer = (EntityPlayer)combatentry.getDamageSrc().getEntity();
            }
            if (combatentry.getDamageSrc().getEntity() instanceof EntityLivingBase && (entitylivingbase == null || combatentry.func_94563_c() > f)) {
                f = combatentry.func_94563_c();
                entitylivingbase = (EntityLivingBase)combatentry.getDamageSrc().getEntity();
            }
        }
        if (entityplayer != null && f2 >= f / 3.0f) {
            return entityplayer;
        }
        return entitylivingbase;
    }
    
    private CombatEntry func_94544_f() {
        CombatEntry combatentry = null;
        CombatEntry combatentry2 = null;
        final int i = 0;
        float f = 0.0f;
        for (int j = 0; j < this.combatEntries.size(); ++j) {
            final CombatEntry combatentry3 = this.combatEntries.get(j);
            final CombatEntry combatentry4 = (j > 0) ? this.combatEntries.get(j - 1) : null;
            if ((combatentry3.getDamageSrc() == DamageSource.fall || combatentry3.getDamageSrc() == DamageSource.outOfWorld) && combatentry3.getDamageAmount() > 0.0f && (combatentry == null || combatentry3.getDamageAmount() > f)) {
                if (j > 0) {
                    combatentry = combatentry4;
                }
                else {
                    combatentry = combatentry3;
                }
                f = combatentry3.getDamageAmount();
            }
            if (combatentry3.func_94562_g() != null && (combatentry2 == null || combatentry3.func_94563_c() > i)) {
                combatentry2 = combatentry3;
            }
        }
        if (f > 5.0f && combatentry != null) {
            return combatentry;
        }
        if (i > 5 && combatentry2 != null) {
            return combatentry2;
        }
        return null;
    }
    
    private String func_94548_b(final CombatEntry p_94548_1_) {
        return (p_94548_1_.func_94562_g() == null) ? "generic" : p_94548_1_.func_94562_g();
    }
    
    public int func_180134_f() {
        return this.field_94552_d ? (this.fighter.ticksExisted - this.field_152775_d) : (this.field_152776_e - this.field_152775_d);
    }
    
    private void func_94542_g() {
        this.field_94551_f = null;
    }
    
    public void reset() {
        final int i = this.field_94552_d ? 300 : 100;
        if (this.field_94553_e && (!this.fighter.isEntityAlive() || this.fighter.ticksExisted - this.field_94555_c > i)) {
            final boolean flag = this.field_94552_d;
            this.field_94553_e = false;
            this.field_94552_d = false;
            this.field_152776_e = this.fighter.ticksExisted;
            if (flag) {
                this.fighter.sendEndCombat();
            }
            this.combatEntries.clear();
        }
    }
    
    public EntityLivingBase getFighter() {
        return this.fighter;
    }
}
