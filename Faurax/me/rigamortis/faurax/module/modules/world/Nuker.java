package me.rigamortis.faurax.module.modules.world;

import me.rigamortis.faurax.module.helpers.*;
import javax.vecmath.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import net.minecraft.block.*;
import com.darkmagician6.eventapi.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import org.lwjgl.opengl.*;
import me.rigamortis.faurax.events.*;

public class Nuker extends Module implements WorldHelper, RenderHelper, PlayerHelper
{
    public int radius;
    public int height;
    public Block currentBlock;
    public Vector3f blockVec;
    public float oldYaw;
    private float oldPitch;
    public static int mode;
    
    public Nuker() {
        this.currentBlock = null;
        this.blockVec = null;
        this.setName("Nuker");
        this.setType(ModuleType.WORLD);
        this.setKey("");
        this.setModInfo("");
        this.setVisible(true);
        this.setColor(-6402356);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
        Nuker.mode = 1;
        this.radius = 4;
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        this.setModInfo("");
        this.currentBlock = null;
        Client.getClientHelper().hitDelay = 5.0f;
        Client.getClientHelper().blockDamage = 0.0f;
    }
    
    @Override
    public void onToggled() {
        super.onToggled();
    }
    
    public Vector3f getBestBlock(final int radius) {
        Vector3f tempVec = null;
        double dist = radius;
        for (int i = radius; i >= -radius; --i) {
            for (int j = -radius; j <= radius; ++j) {
                for (int k = radius; k >= -radius; --k) {
                    final int posX = (int)(Nuker.mc.thePlayer.posX + i);
                    final int posY = (int)(Nuker.mc.thePlayer.posY + j);
                    final int posZ = (int)(Nuker.mc.thePlayer.posZ + k);
                    final double curDist = Client.getClientHelper().getDistance(posX, posY, posZ);
                    if (Nuker.mode == 1) {
                        if (Client.getClientHelper().getBlock(posX, posY, posZ) != this.currentBlock || Client.getClientHelper().getBlock(posX, posY, posZ) instanceof BlockAir) {
                            continue;
                        }
                    }
                    else if (Client.getClientHelper().getBlock(posX, posY, posZ) instanceof BlockAir) {
                        continue;
                    }
                    if (curDist <= dist) {
                        dist = curDist;
                        tempVec = new Vector3f((float)posX, (float)posY, (float)posZ);
                    }
                }
            }
        }
        return tempVec;
    }
    
    @EventTarget(4)
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            this.oldPitch = Nuker.mc.thePlayer.rotationPitch;
            this.oldYaw = Nuker.mc.thePlayer.rotationYaw;
            final Vector3f block = this.getBestBlock(this.radius);
            if (block != null) {
                this.setModInfo(" §7" + Client.getClientHelper().getBlock(block.x, block.y, block.z).getLocalizedName());
                Client.getClientHelper().faceBlock(block.x + 0.5f, block.y + 0.5f, block.z + 0.5f);
                final BlockPos blockPos = new BlockPos(block.x, block.y, block.z);
                Nuker.mc.thePlayer.swingItem();
                Nuker.mc.playerController.func_180512_c(blockPos, Client.getClientHelper().getEnumFacing(block.x, block.y, block.z));
            }
        }
    }
    
    @EventTarget(0)
    public void postTick(final EventPostTick e) {
        if (this.isToggled()) {
            Nuker.mc.thePlayer.rotationPitch = this.oldPitch;
            Nuker.mc.thePlayer.rotationYaw = this.oldYaw;
        }
    }
    
    @EventTarget
    public void renderWorld(final EventRenderWorld e) {
        if (this.isToggled()) {
            final Vector3f block = this.getBestBlock(this.radius);
            if (block != null) {
                Nuker.guiUtils.drawFilledBBESP(new AxisAlignedBB(block.x - RenderManager.renderPosX, block.y - RenderManager.renderPosY, block.z - RenderManager.renderPosZ, block.x + 1.0f - RenderManager.renderPosX, block.y + 1.0f - RenderManager.renderPosY, block.z + 1.0f - RenderManager.renderPosZ), -16777165);
                Nuker.guiUtils.drawBoundingBoxESP(new AxisAlignedBB(block.x - RenderManager.renderPosX, block.y - RenderManager.renderPosY, block.z - RenderManager.renderPosZ, block.x + 1.0f - RenderManager.renderPosX, block.y + Nuker.mc.playerController.curBlockDamageMP - RenderManager.renderPosY, block.z + 1.0f - RenderManager.renderPosZ), 2.0f + Nuker.mc.playerController.curBlockDamageMP, -16777080);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }
    
    @EventTarget
    public void onClickBlock(final EventPlaceBlock e) {
        if (this.isToggled()) {
            final int posX = Nuker.mc.objectMouseOver.func_178782_a().getX();
            final int posY = Nuker.mc.objectMouseOver.func_178782_a().getY();
            final int posZ = Nuker.mc.objectMouseOver.func_178782_a().getZ();
            this.currentBlock = Client.getClientHelper().getBlock(posX, posY, posZ);
        }
    }
}
