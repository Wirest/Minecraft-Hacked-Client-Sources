// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.CheerioFX.FusionX.module.Category;
import org.hero.settings.Setting;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.module.Module;

public class SafeFall extends Module
{
    public boolean noFallOn;
    public boolean autoTP;
    boolean lessDamage;
    boolean bypass;
    double x;
    double y;
    double z;
    
    public double getFD() {
        return FusionX.theClient.setmgr.getSetting(this, "FallDistance").getValDouble();
    }
    
    @Override
    public void setup() {
        FusionX.theClient.setmgr.rSetting(new Setting("FallDistance", this, 3.3, 1.0, 10.0, false));
    }
    
    public SafeFall() {
        super("SafeFall", 0, Category.MOVEMENT);
        this.autoTP = false;
        this.lessDamage = false;
        this.bypass = true;
    }
    
    @Override
    public void onEnable() {
        this.noFallOn = this.lessDamage;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.noFallOn = false;
        super.onDisable();
    }
    
    @Override
    public void onUpdate() {
        if (this.getState()) {
            if (SafeFall.mc.thePlayer.fallDistance > 2.0f) {
                SafeFall.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            }
            if (SafeFall.mc.thePlayer.onGround) {
                this.x = SafeFall.mc.thePlayer.posX;
                this.y = SafeFall.mc.thePlayer.posY;
                this.z = SafeFall.mc.thePlayer.posZ;
            }
            else if (SafeFall.mc.thePlayer.fallDistance > this.getFD() && !SafeFall.mc.thePlayer.onGround) {
                SafeFall.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                SafeFall.mc.thePlayer.onGround = true;
                if (!this.bypass) {
                    SafeFall.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.x, this.y, this.z, true));
                    SafeFall.mc.thePlayer.setPosition(this.x, this.y, this.z);
                }
                else {
                    SafeFall.mc.thePlayer.motionY = -111119.0;
                }
            }
        }
        super.onUpdate();
    }
}
