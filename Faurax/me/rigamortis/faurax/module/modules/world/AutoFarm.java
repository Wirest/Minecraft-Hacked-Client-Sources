package me.rigamortis.faurax.module.modules.world;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.values.*;
import javax.vecmath.*;
import net.minecraft.block.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.item.*;
import com.darkmagician6.eventapi.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import org.lwjgl.opengl.*;
import me.rigamortis.faurax.events.*;

public class AutoFarm extends Module implements WorldHelper, PlayerHelper, RenderHelper
{
    private float oldPitch;
    private float oldYaw;
    private int lookDelay;
    public static Value mode;
    public static Value radius;
    public static Value delay;
    
    static {
        AutoFarm.mode = new Value("AutoFarm", String.class, "Mode", "Seeds", new String[] { "Seeds", "Tilling", "NetherWart", "Cactus" });
        AutoFarm.radius = new Value("AutoFarm", Float.TYPE, "Radius", 4.0f, 1.0f, 5.0f);
        AutoFarm.delay = new Value("AutoFarm", Float.TYPE, "Delay", 2.0f, 0.0f, 10.0f);
    }
    
    public AutoFarm() {
        this.setType(ModuleType.WORLD);
        this.setName("AutoFarm");
        this.setKey("");
        this.setModInfo("");
        this.setColor(-6402356);
        this.setVisible(true);
        Client.getValues();
        ValueManager.values.add(AutoFarm.mode);
        Client.getValues();
        ValueManager.values.add(AutoFarm.radius);
        Client.getValues();
        ValueManager.values.add(AutoFarm.delay);
    }
    
    public Vector3f getBlock(final float radius, final int block) {
        Vector3f tempVec = null;
        float dist = radius;
        for (float i = radius; i >= -radius; --i) {
            for (float j = -radius; j <= radius; ++j) {
                for (float k = radius; k >= -radius; --k) {
                    final int posX = (int)(AutoFarm.mc.thePlayer.posX + i);
                    final int posY = (int)(AutoFarm.mc.thePlayer.posY + j);
                    final int posZ = (int)(AutoFarm.mc.thePlayer.posZ + k);
                    final float curDist = Client.getClientHelper().getDistance(posX, posY, posZ);
                    if (Block.getIdFromBlock(Client.getClientHelper().getBlock(posX, posY - 1, posZ)) == block && Client.getClientHelper().getBlock(posX, posY, posZ) instanceof BlockAir && curDist <= dist) {
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
            this.oldPitch = AutoFarm.mc.thePlayer.rotationPitch;
            this.oldYaw = AutoFarm.mc.thePlayer.rotationYaw;
            if (AutoFarm.mode.getSelectedOption().equalsIgnoreCase("Seeds")) {
                this.setModInfo(" §7Seeds");
                final Vector3f block = this.getBlock(AutoFarm.radius.getFloatValue(), this.getPlantType());
                if (block != null && AutoFarm.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSeeds) {
                    Client.getClientHelper().faceBlock((int)block.x + 0.5f, (int)block.y, (int)block.z + 0.5f);
                    final int posX = (int)block.x;
                    final int posY = (int)block.y;
                    final int posZ = (int)block.z;
                    ++this.lookDelay;
                    if (this.lookDelay >= AutoFarm.delay.getFloatValue()) {
                        AutoFarm.mc.thePlayer.swingItem();
                        AutoFarm.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Client.getClientHelper().getBlockPos(posX, posY - 0.1f, posZ), 1, AutoFarm.mc.thePlayer.getCurrentEquippedItem(), 0.0f, 0.0f, 0.0f));
                        this.lookDelay = 0;
                    }
                }
            }
            if (AutoFarm.mode.getSelectedOption().equalsIgnoreCase("Tilling")) {
                this.setModInfo(" §7Tilling");
                final Vector3f block = this.getBlock(AutoFarm.radius.getFloatValue(), this.getPlantType());
                if (block != null && AutoFarm.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemHoe) {
                    Client.getClientHelper().faceBlock((int)block.x + 0.5f, (int)block.y, (int)block.z + 0.5f);
                    final int posX = (int)block.x;
                    final int posY = (int)block.y;
                    final int posZ = (int)block.z;
                    ++this.lookDelay;
                    if (this.lookDelay >= AutoFarm.delay.getFloatValue()) {
                        AutoFarm.mc.thePlayer.swingItem();
                        AutoFarm.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Client.getClientHelper().getBlockPos(posX, posY - 0.1f, posZ), 1, AutoFarm.mc.thePlayer.getCurrentEquippedItem(), 0.0f, 0.0f, 0.0f));
                        this.lookDelay = 0;
                    }
                }
            }
            if (AutoFarm.mode.getSelectedOption().equalsIgnoreCase("NetherWart")) {
                this.setModInfo(" §7NetherWart");
                final Vector3f block = this.getBlock(AutoFarm.radius.getFloatValue(), this.getPlantType());
                if ((block != null && AutoFarm.mc.thePlayer.getCurrentEquippedItem().getDisplayName().equalsIgnoreCase("Nether Wart")) || AutoFarm.mc.thePlayer.getCurrentEquippedItem().getDisplayName().contains("Meth")) {
                    Client.getClientHelper().faceBlock((int)block.x + 0.5f, (int)block.y, (int)block.z + 0.5f);
                    final int posX = (int)block.x;
                    final int posY = (int)block.y;
                    final int posZ = (int)block.z;
                    ++this.lookDelay;
                    if (this.lookDelay >= AutoFarm.delay.getFloatValue()) {
                        AutoFarm.mc.thePlayer.swingItem();
                        AutoFarm.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Client.getClientHelper().getBlockPos(posX, posY - 0.1f, posZ), 1, AutoFarm.mc.thePlayer.getCurrentEquippedItem(), 0.0f, 0.0f, 0.0f));
                        this.lookDelay = 0;
                    }
                }
            }
            if (AutoFarm.mode.getSelectedOption().equalsIgnoreCase("Cactus")) {
                this.setModInfo(" §7Cactus");
                final Vector3f block = this.getBlock(AutoFarm.radius.getFloatValue(), this.getPlantType());
                if (block != null && AutoFarm.mc.thePlayer.getCurrentEquippedItem().getDisplayName().equalsIgnoreCase("Cactus")) {
                    Client.getClientHelper().faceBlock((int)block.x + 0.5f, (int)block.y, (int)block.z + 0.5f);
                    final int posX = (int)block.x;
                    final int posY = (int)block.y;
                    final int posZ = (int)block.z;
                    ++this.lookDelay;
                    if (this.lookDelay >= AutoFarm.delay.getFloatValue()) {
                        AutoFarm.mc.thePlayer.swingItem();
                        AutoFarm.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Client.getClientHelper().getBlockPos(posX, posY - 0.1f, posZ), 1, AutoFarm.mc.thePlayer.getCurrentEquippedItem(), 0.0f, 0.0f, 0.0f));
                        this.lookDelay = 0;
                    }
                }
            }
        }
    }
    
    @EventTarget
    public void renderWorld(final EventRenderWorld e) {
        if (this.isToggled()) {
            final Vector3f block = this.getBlock(AutoFarm.radius.getFloatValue(), this.getPlantType());
            AutoFarm.guiUtils.drawFilledBBESP(new AxisAlignedBB(block.x - RenderManager.renderPosX, block.y - RenderManager.renderPosY, block.z - RenderManager.renderPosZ, block.x + 1.0f - RenderManager.renderPosX, block.y + 1.0f - RenderManager.renderPosY, block.z + 1.0f - RenderManager.renderPosZ), -16777182);
            AutoFarm.guiUtils.drawBoundingBoxESP(new AxisAlignedBB(block.x - RenderManager.renderPosX, block.y - RenderManager.renderPosY, block.z - RenderManager.renderPosZ, block.x + 1.0f - RenderManager.renderPosX, block.y + Client.getClientHelper().blockDamage - RenderManager.renderPosY, block.z + 1.0f - RenderManager.renderPosZ), 1.0f + Client.getClientHelper().blockDamage, -16777131);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    public int getPlantType() {
        int tempType = 0;
        if (AutoFarm.mode.getSelectedOption().equalsIgnoreCase("Seeds")) {
            tempType = 60;
        }
        if (AutoFarm.mode.getSelectedOption().equalsIgnoreCase("Tilling")) {
            tempType = 3;
        }
        if (AutoFarm.mode.getSelectedOption().equalsIgnoreCase("NetherWart")) {
            tempType = 88;
        }
        if (AutoFarm.mode.getSelectedOption().equalsIgnoreCase("Cactus")) {
            tempType = 12;
        }
        return tempType;
    }
    
    @EventTarget(0)
    public void postTick(final EventPostTick e) {
        if (this.isToggled()) {
            AutoFarm.mc.thePlayer.rotationPitch = this.oldPitch;
            AutoFarm.mc.thePlayer.rotationYaw = this.oldYaw;
        }
    }
}
