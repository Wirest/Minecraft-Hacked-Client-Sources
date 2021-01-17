// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAILookAtTradePlayer extends EntityAIWatchClosest
{
    private final EntityVillager theMerchant;
    
    public EntityAILookAtTradePlayer(final EntityVillager theMerchantIn) {
        super(theMerchantIn, EntityPlayer.class, 8.0f);
        this.theMerchant = theMerchantIn;
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.theMerchant.isTrading()) {
            this.closestEntity = this.theMerchant.getCustomer();
            return true;
        }
        return false;
    }
}
