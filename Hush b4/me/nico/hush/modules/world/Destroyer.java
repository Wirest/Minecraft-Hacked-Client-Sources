// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.world;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.client.Minecraft;
import com.darkmagician6.eventapi.EventTarget;
import me.nico.hush.events.EventUpdate;
import de.Hero.settings.Setting;
import me.nico.hush.Client;
import java.util.ArrayList;
import me.nico.hush.modules.Category;
import me.nico.hush.utils.TimeHelper;
import me.nico.hush.modules.Module;

public class Destroyer extends Module
{
    public static String mode;
    public TimeHelper delay;
    private int xOffset;
    private int yOffset;
    private int zOffset;
    
    static {
        Destroyer.mode = "Bed";
    }
    
    public Destroyer() {
        super("Destroyer", "Destroyer", 14620696, 25, Category.WORLD);
        this.delay = new TimeHelper();
        final ArrayList<String> mode = new ArrayList<String>();
        mode.add("Bed");
        mode.add("Egg");
        mode.add("Cake");
        mode.add("Core");
        Client.instance.settingManager.rSetting(new Setting("Mode", "DestroyerMode", this, "Bed", mode));
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (Client.instance.settingManager.getSettingByName("DestroyerMode").getValString().equalsIgnoreCase("Bed")) {
            this.setDisplayname("Destroyer");
            this.Bed();
        }
        else if (Client.instance.settingManager.getSettingByName("DestroyerMode").getValString().equalsIgnoreCase("Egg")) {
            this.setDisplayname("Destroyer");
            this.Egg();
        }
        else if (Client.instance.settingManager.getSettingByName("DestroyerMode").getValString().equalsIgnoreCase("Cake")) {
            this.setDisplayname("Destroyer");
            this.Cake();
        }
        else if (Client.instance.settingManager.getSettingByName("DestroyerMode").getValString().equalsIgnoreCase("Core")) {
            this.setDisplayname("Destroyer");
            this.Core();
        }
    }
    
    private void Cake() {
        if (Destroyer.mc.theWorld == null) {
            return;
        }
        this.xOffset = -5;
        while (this.xOffset < 6) {
            this.yOffset = 5;
            while (this.yOffset > -5) {
                this.zOffset = -5;
                while (this.zOffset < 6) {
                    final Minecraft mc = Destroyer.mc;
                    final double x = Minecraft.thePlayer.posX + this.xOffset;
                    final Minecraft mc2 = Destroyer.mc;
                    final double y = Minecraft.thePlayer.posY + this.yOffset;
                    final Minecraft mc3 = Destroyer.mc;
                    final double z = Minecraft.thePlayer.posZ + this.zOffset;
                    final int id = Block.getIdFromBlock(Destroyer.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock());
                    if (id == 92) {
                        this.smashBlock(new BlockPos(x, y, z));
                        break;
                    }
                    ++this.zOffset;
                }
                --this.yOffset;
            }
            ++this.xOffset;
        }
    }
    
    public void smashBlock4(final BlockPos pos) {
        final Minecraft mc = Destroyer.mc;
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
        final Minecraft mc2 = Destroyer.mc;
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.UP));
    }
    
    private void Bed() {
        if (!this.isEnabled()) {
            return;
        }
        if (Destroyer.mc.theWorld == null) {
            return;
        }
        this.xOffset = -5;
        while (this.xOffset < 6) {
            this.yOffset = 5;
            while (this.yOffset > -5) {
                this.zOffset = -5;
                while (this.zOffset < 6) {
                    final Minecraft mc = Destroyer.mc;
                    final double x = Minecraft.thePlayer.posX + this.xOffset;
                    final Minecraft mc2 = Destroyer.mc;
                    final double y = Minecraft.thePlayer.posY + this.yOffset;
                    final Minecraft mc3 = Destroyer.mc;
                    final double z = Minecraft.thePlayer.posZ + this.zOffset;
                    final int id = Block.getIdFromBlock(Destroyer.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock());
                    if (id == 26) {
                        this.smashBlock(new BlockPos(x, y, z));
                        final Minecraft mc4 = Destroyer.mc;
                        Minecraft.thePlayer.swingItem();
                        break;
                    }
                    ++this.zOffset;
                }
                --this.yOffset;
            }
            ++this.xOffset;
        }
    }
    
    public void smashBlock(final BlockPos pos) {
        final Minecraft mc = Destroyer.mc;
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
        final Minecraft mc2 = Destroyer.mc;
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.UP));
    }
    
    private void Egg() {
        if (Destroyer.mc.theWorld == null) {
            return;
        }
        this.xOffset = -5;
        while (this.xOffset < 6) {
            this.yOffset = 5;
            while (this.yOffset > -5) {
                this.zOffset = -5;
                while (this.zOffset < 6) {
                    final Minecraft mc = Destroyer.mc;
                    final double x = Minecraft.thePlayer.posX + this.xOffset;
                    final Minecraft mc2 = Destroyer.mc;
                    final double y = Minecraft.thePlayer.posY + this.yOffset;
                    final Minecraft mc3 = Destroyer.mc;
                    final double z = Minecraft.thePlayer.posZ + this.zOffset;
                    final int id = Block.getIdFromBlock(Destroyer.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock());
                    if (id == 122) {
                        this.smashBlock(new BlockPos(x, y, z));
                        break;
                    }
                    ++this.zOffset;
                }
                --this.yOffset;
            }
            ++this.xOffset;
        }
    }
    
    public void smashBlock2(final BlockPos pos) {
        final Minecraft mc = Destroyer.mc;
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
        final Minecraft mc2 = Destroyer.mc;
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.UP));
    }
    
    public void Core() {
        if (Destroyer.mc.theWorld == null) {
            return;
        }
        this.xOffset = -5;
        while (this.xOffset < 6) {
            this.yOffset = 5;
            while (this.yOffset > -5) {
                this.zOffset = -5;
                while (this.zOffset < 6) {
                    final Minecraft mc = Destroyer.mc;
                    final double x = Minecraft.thePlayer.posX + this.xOffset;
                    final Minecraft mc2 = Destroyer.mc;
                    final double y = Minecraft.thePlayer.posY + this.yOffset;
                    final Minecraft mc3 = Destroyer.mc;
                    final double z = Minecraft.thePlayer.posZ + this.zOffset;
                    final int id = Block.getIdFromBlock(Destroyer.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock());
                    if (id == 138) {
                        this.smashBlock(new BlockPos(x, y, z));
                        break;
                    }
                    ++this.zOffset;
                }
                --this.yOffset;
            }
            ++this.xOffset;
        }
    }
    
    public void smashBlock3(final BlockPos pos) {
        final Minecraft mc = Destroyer.mc;
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
        final Minecraft mc2 = Destroyer.mc;
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.UP));
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
}
