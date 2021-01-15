// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.block.Block;
import me.CheerioFX.FusionX.utils.RenderUtils;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockSign;
import me.CheerioFX.FusionX.events.Event3D;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import me.CheerioFX.FusionX.module.Category;
import net.minecraft.util.BlockPos;
import me.CheerioFX.FusionX.module.Module;

public class Teleport extends Module
{
    public static boolean isModEnabled;
    private boolean atp;
    private int delay;
    private BlockPos endPos;
    public static boolean antiAccidents;
    double r;
    double g;
    double b;
    
    static {
        Teleport.isModEnabled = false;
        Teleport.antiAccidents = true;
    }
    
    public Teleport() {
        super("Click Teleport", 0, Category.OTHER);
    }
    
    @Override
    public void onEnable() {
        Teleport.isModEnabled = true;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        Teleport.isModEnabled = false;
        super.onDisable();
    }
    
    @EventTarget
    public void onPreUpdate(final EventPreMotionUpdates event) {
        if ((Teleport.mc.thePlayer.isEating() || Teleport.mc.thePlayer.isSneaking() || Teleport.mc.thePlayer.isBlocking()) && Teleport.antiAccidents) {
            return;
        }
        if (this.atp && Teleport.mc.gameSettings.keyBindUseItem.pressed && !Teleport.mc.thePlayer.isSneaking() && this.delay == 0 && Teleport.mc.inGameHasFocus) {
            this.endPos = Teleport.mc.objectMouseOver.func_178782_a();
            final double[] startPos = { Teleport.mc.thePlayer.posX, Teleport.mc.thePlayer.posY, Teleport.mc.thePlayer.posZ };
            Wrapper.blinkToPos(startPos, this.endPos, 0.0);
            Teleport.mc.thePlayer.setPosition(this.endPos.getX() + 0.5, this.endPos.getY() + 1, this.endPos.getZ() + 0.5);
            Wrapper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(this.endPos.getX() + 0.5, this.endPos.getY() - 1.0, this.endPos.getZ() + 0.5, false));
            this.delay = 5;
        }
        if (this.delay > 0) {
            --this.delay;
        }
    }
    
    @EventTarget
    public void onRender(final Event3D event) {
        if ((Teleport.mc.thePlayer.isEating() || Teleport.mc.thePlayer.isSneaking() || Teleport.mc.thePlayer.isBlocking()) && Teleport.antiAccidents) {
            return;
        }
        final int x = Teleport.mc.objectMouseOver.func_178782_a().getX();
        final int y = Teleport.mc.objectMouseOver.func_178782_a().getY();
        final int z = Teleport.mc.objectMouseOver.func_178782_a().getZ();
        final Block block1 = Wrapper.getBlockAtPos(new BlockPos(x, y, z));
        final Block block2 = Wrapper.getBlockAtPos(new BlockPos(x, y + 1.0, z));
        final Block block3 = Wrapper.getBlockAtPos(new BlockPos(x, y + 2.0, z));
        final boolean blockBelow = !(block1 instanceof BlockSign) && block1.getMaterial().isSolid();
        final boolean blockLevel = !(block2 instanceof BlockSign) && block1.getMaterial().isSolid();
        final boolean blockAbove = !(block3 instanceof BlockSign) && block1.getMaterial().isSolid();
        if (Wrapper.getBlockAtPos(Teleport.mc.objectMouseOver.func_178782_a()).getMaterial() != Material.air && blockBelow && blockLevel && blockAbove) {
            this.atp = true;
            GL11.glPushMatrix();
            this.r = 255.0;
            this.g = 102.0;
            this.b = 51.0;
            final double mp = 0.00392156862745098;
            GL11.glColor4d(this.r * mp, this.g * mp, this.b * mp, 1.0);
            RenderUtils.drawBoundingBox(new AxisAlignedBB(x - RenderManager.renderPosX, y + 1 - RenderManager.renderPosY, z - RenderManager.renderPosZ, x - RenderManager.renderPosX + 1.0, y + 1.1 - RenderManager.renderPosY, z - RenderManager.renderPosZ + 1.0));
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
            RenderUtils.drawOutlinedBoundingBox(new AxisAlignedBB(x - RenderManager.renderPosX, y + 1 - RenderManager.renderPosY, z - RenderManager.renderPosZ, x - RenderManager.renderPosX + 1.0, y + 1.1 - RenderManager.renderPosY, z - RenderManager.renderPosZ + 1.0));
            GL11.glPopMatrix();
        }
        else {
            this.atp = false;
        }
    }
}
