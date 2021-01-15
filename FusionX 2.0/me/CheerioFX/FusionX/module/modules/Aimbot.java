// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.item.Item;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemSnowball;
import me.CheerioFX.FusionX.utils.RenderUtils;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import me.CheerioFX.FusionX.utils.EntityUtils;
import net.minecraft.item.ItemBow;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import me.CheerioFX.FusionX.module.Category;
import org.hero.settings.Setting;
import me.CheerioFX.FusionX.FusionX;
import net.minecraft.entity.EntityLivingBase;
import me.CheerioFX.FusionX.module.Module;

public class Aimbot extends Module
{
    private EntityLivingBase target;
    private float velocity;
    private float eventYaw;
    private float eventPitch;
    
    public boolean isLockView() {
        return FusionX.theClient.setmgr.getSetting(this, "lockview").getValBoolean();
    }
    
    public boolean isTargetESP() {
        return FusionX.theClient.setmgr.getSetting(this, "targetesp").getValBoolean();
    }
    
    public boolean isTrajectories() {
        return FusionX.theClient.setmgr.getSetting(this, "trajectories").getValBoolean();
    }
    
    @Override
    public void setup() {
        FusionX.theClient.setmgr.rSetting(new Setting("LockView", this, true));
        FusionX.theClient.setmgr.rSetting(new Setting("TargetESP", this, true));
        FusionX.theClient.setmgr.rSetting(new Setting("Trajectories", this, true));
    }
    
    public Aimbot() {
        super("Aimbot", 0, Category.COMBAT);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @EventTarget
    public void onUpdate(final EventPreMotionUpdates event) {
        this.target = null;
        if (!Aimbot.mc.gameSettings.keyBindUseItem.pressed) {
            return;
        }
        final ItemStack item = Aimbot.mc.thePlayer.inventory.getCurrentItem();
        if (item == null || !(item.getItem() instanceof ItemBow)) {
            return;
        }
        this.target = EntityUtils.getClosestEntity();
        if (this.target == null) {
            return;
        }
        this.velocity = Aimbot.mc.thePlayer.getItemInUseDuration() / 20;
        this.velocity = (this.velocity * this.velocity + this.velocity * 2.0f) / 3.0f;
        if (this.velocity > 1.0f) {
            this.velocity = 1.0f;
        }
        if (this.velocity < 0.1 && this.target instanceof EntityLivingBase) {
            if (this.isLockView()) {
                EntityUtils.faceEntityClient(this.target);
            }
            else {
                this.eventPitch = Aimbot.mc.thePlayer.rotationPitch;
                this.eventYaw = Aimbot.mc.thePlayer.rotationYaw;
            }
            return;
        }
        final double posX = this.target.posX + (this.target.posX - this.target.prevPosX) * 5.0 - Aimbot.mc.thePlayer.posX;
        final double posY = this.target.posY + (this.target.posY - this.target.prevPosY) * 5.0 + this.target.getEyeHeight() - 0.15 - Aimbot.mc.thePlayer.posY - Aimbot.mc.thePlayer.getEyeHeight();
        final double posZ = this.target.posZ + (this.target.posZ - this.target.prevPosZ) * 5.0 - Aimbot.mc.thePlayer.posZ;
        if (this.isLockView()) {
            Aimbot.mc.thePlayer.rotationYaw = (float)(Math.atan2(posZ, posX) * 180.0 / 3.141592653589793) - 90.0f;
        }
        else {
            event.setYaw((float)(Math.atan2(posZ, posX) * 180.0 / 3.141592653589793) - 90.0f);
            this.eventYaw = (float)(Math.atan2(posZ, posX) * 180.0 / 3.141592653589793) - 90.0f;
        }
        final double distanceXZ = Math.sqrt(posX * posX + posZ * posZ);
        final float g = 0.006f;
        if (this.isLockView()) {
            Aimbot.mc.thePlayer.rotationPitch = (float)(-Math.toDegrees(Math.atan((this.velocity * this.velocity - Math.sqrt((float)(this.velocity * this.velocity * this.velocity * this.velocity - g * (g * (distanceXZ * distanceXZ) + 2.0 * posY * (this.velocity * this.velocity))))) / (g * distanceXZ))));
        }
        else {
            event.setPitch((float)(-Math.toDegrees(Math.atan((this.velocity * this.velocity - Math.sqrt((float)(this.velocity * this.velocity * this.velocity * this.velocity - g * (g * (distanceXZ * distanceXZ) + 2.0 * posY * (this.velocity * this.velocity))))) / (g * distanceXZ)))));
            this.eventPitch = (float)(-Math.toDegrees(Math.atan((this.velocity * this.velocity - Math.sqrt((float)(this.velocity * this.velocity * this.velocity * this.velocity - g * (g * (distanceXZ * distanceXZ) + 2.0 * posY * (this.velocity * this.velocity))))) / (g * distanceXZ))));
        }
    }
    
    @Override
    public void onRender() {
        if (this.target == null) {
            return;
        }
        if (this.isTargetESP()) {
            RenderUtils.entityESPBox(this.target, 3);
        }
        if (this.isTrajectories()) {
            final EntityPlayerSP player = Aimbot.mc.thePlayer;
            final ItemStack stack = player.getCurrentEquippedItem();
            if (stack == null) {
                return;
            }
            final Item item = stack.getItem();
            if (!(item instanceof ItemBow) && !(item instanceof ItemSnowball) && !(item instanceof ItemEgg) && !(item instanceof ItemEnderPearl) && (!(item instanceof ItemPotion) || !ItemPotion.isSplash(stack.getItemDamage()))) {
                return;
            }
            final boolean usingBow = player.getCurrentEquippedItem().getItem() instanceof ItemBow;
            double arrowPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * Aimbot.mc.timer.renderPartialTicks - MathHelper.cos((float)Math.toRadians(player.rotationYaw)) * 0.16f;
            double arrowPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * Minecraft.getMinecraft().timer.renderPartialTicks + player.getEyeHeight() - 0.1;
            double arrowPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * Minecraft.getMinecraft().timer.renderPartialTicks - MathHelper.sin((float)Math.toRadians(player.rotationYaw)) * 0.16f;
            final float arrowMotionFactor = usingBow ? 1.0f : 0.4f;
            float yaw;
            float pitch;
            if (this.isLockView()) {
                yaw = (float)Math.toRadians(player.rotationYaw);
                pitch = (float)Math.toRadians(player.rotationPitch);
            }
            else {
                yaw = (float)Math.toRadians(this.eventYaw);
                pitch = (float)Math.toRadians(this.eventPitch);
            }
            float arrowMotionX = -MathHelper.sin(yaw) * MathHelper.cos(pitch) * arrowMotionFactor;
            float arrowMotionY = -MathHelper.sin(pitch) * arrowMotionFactor;
            float arrowMotionZ = MathHelper.cos(yaw) * MathHelper.cos(pitch) * arrowMotionFactor;
            final double arrowMotion = Math.sqrt(arrowMotionX * arrowMotionX + arrowMotionY * arrowMotionY + arrowMotionZ * arrowMotionZ);
            arrowMotionX /= (float)arrowMotion;
            arrowMotionY /= (float)arrowMotion;
            arrowMotionZ /= (float)arrowMotion;
            if (usingBow) {
                float bowPower = (72000 - player.getItemInUseCount()) / 20.0f;
                bowPower = (bowPower * bowPower + bowPower * 2.0f) / 3.0f;
                if (bowPower > 1.0f) {
                    bowPower = 1.0f;
                }
                if (bowPower <= 0.1f) {
                    bowPower = 1.0f;
                }
                bowPower *= 3.0f;
                arrowMotionX *= bowPower;
                arrowMotionY *= bowPower;
                arrowMotionZ *= bowPower;
            }
            else {
                arrowMotionX *= 1.5;
                arrowMotionY *= 1.5;
                arrowMotionZ *= 1.5;
            }
            GL11.glPushMatrix();
            GL11.glEnable(2848);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            GL11.glEnable(32925);
            GL11.glDepthMask(false);
            GL11.glLineWidth(1.8f);
            final RenderManager renderManager = Aimbot.mc.getRenderManager();
            final double gravity = usingBow ? 0.05 : ((item instanceof ItemPotion) ? 0.4 : 0.03);
            final Vec3 playerVector = new Vec3(player.posX, player.posY + player.getEyeHeight(), player.posZ);
            GL11.glColor3d(1.0, 1.0, 0.0);
            GL11.glBegin(3);
            for (int i = 0; i < 1000; ++i) {
                GL11.glVertex3d(arrowPosX - RenderManager.renderPosX, arrowPosY - RenderManager.renderPosY, arrowPosZ - RenderManager.renderPosZ);
                arrowPosX += arrowMotionX;
                arrowPosY += arrowMotionY;
                arrowPosZ += arrowMotionZ;
                arrowMotionX *= 0.99;
                arrowMotionY *= 0.99;
                arrowMotionZ *= 0.99;
                arrowMotionY -= (float)gravity;
                if (Aimbot.mc.theWorld.rayTraceBlocks(playerVector, new Vec3(arrowPosX, arrowPosY, arrowPosZ)) != null) {
                    break;
                }
            }
            GL11.glEnd();
            final double renderX = arrowPosX - RenderManager.renderPosX;
            final double renderY = arrowPosY - RenderManager.renderPosY;
            final double renderZ = arrowPosZ - RenderManager.renderPosZ;
            final AxisAlignedBB bb = new AxisAlignedBB(renderX - 0.5, renderY - 0.5, renderZ - 0.5, renderX + 0.5, renderY + 0.5, renderZ + 0.5);
            GL11.glColor4d(1.0, 1.0, 0.0, 0.15000000596046448);
            RenderUtils.drawColorBox(bb);
            GL11.glColor4d(0.0, 0.0, 0.0, 0.5);
            RenderGlobal.drawOutlinedBoundingBox(bb, -1);
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDisable(32925);
            GL11.glDepthMask(true);
            GL11.glDisable(2848);
            GL11.glPopMatrix();
        }
    }
}
