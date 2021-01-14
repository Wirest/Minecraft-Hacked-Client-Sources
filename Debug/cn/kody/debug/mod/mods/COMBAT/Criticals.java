/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package cn.kody.debug.mod.mods.COMBAT;

import cn.kody.debug.events.EventAttack;
import cn.kody.debug.events.EventUpdate;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.mod.ModManager;
import cn.kody.debug.utils.time.TimeHelper;
import cn.kody.debug.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Criticals
extends Mod {
    TimeHelper timer = new TimeHelper();
    private double[] offsetArray;
    public static Value<String> mode = new Value("Criticals", "Mode", 0);
    public static Value<Double> delay = new Value<Double>("Criticals_Delay", 4.0d, 0.0d, 20.0d, 1.0d);;
    public static Value<Double> hurttime = new Value<Double>("Criticals_HurtTime", 15.0d, 1.0d, 20.0d, 1.0d);;
    
    public Criticals() {
        super("Criticals", Category.COMBAT);
        this.offsetArray = new double[] { 0.015099999997473787, 0.051099999997473784, 9.999999747378752E-6, 0.012599999997473788, 9.999999747378752E-5 };
        this.mode.addValue("Hypixel");
    }
    
    public boolean canCrit() {
        boolean b;
        if (!this.mc.thePlayer.isOnLadder() 
        	&& !this.mc.thePlayer.isInWater() 
        	&& !this.mc.thePlayer.isPotionActive(Potion.blindness) 
        	&& this.mc.thePlayer.ridingEntity == null 
        	&& this.mc.thePlayer.onGround 
        	&& !ModManager.getModByName("Fly").isEnabled()) {
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        this.setDisplayName(mode.getModeAt(mode.getCurrentMode()));
    }

}

