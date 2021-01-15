// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import java.text.DecimalFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemTool;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.gui.FontRenderer;
import me.CheerioFX.FusionX.utils.R2DUtils;
import net.minecraft.client.renderer.Tessellator;
import me.CheerioFX.FusionX.utils.R3DUtils;
import org.lwjgl.opengl.GL11;
import me.CheerioFX.FusionX.GUI.clickgui.Targets;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.events.Event3D;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class Nametags extends Module
{
    public static boolean health;
    public static boolean armour;
    public static boolean bars;
    
    static {
        Nametags.health = true;
        Nametags.armour = true;
        Nametags.bars = false;
    }
    
    public Nametags() {
        super("Nametags", 0, Category.RENDER);
    }
    
    @EventTarget(4)
    public void onRender3D(final Event3D render) {
        final List list = Wrapper.mc.theWorld.playerEntities;
        for (final Object player : Wrapper.mc.theWorld.playerEntities) {
            if (((EntityPlayer)player).isEntityAlive() && !(player instanceof EntityPlayerSP)) {
                final double x = ((EntityPlayer)player).lastTickPosX + (((EntityPlayer)player).posX - ((EntityPlayer)player).lastTickPosX) * Event3D.getPartialTicks() - RenderManager.renderPosX;
                final double y = ((EntityPlayer)player).lastTickPosY + (((EntityPlayer)player).posY - ((EntityPlayer)player).lastTickPosY) * Event3D.getPartialTicks() - RenderManager.renderPosY;
                final double z = ((EntityPlayer)player).lastTickPosZ + (((EntityPlayer)player).posZ - ((EntityPlayer)player).lastTickPosZ) * Event3D.getPartialTicks() - RenderManager.renderPosZ;
                this.renderNametag((EntityPlayer)player, x, y, z);
            }
        }
    }
    
    public void renderNametag(final EntityPlayer player, final double x, final double y, final double z) {
        if (Targets.antibot() && (!player.isEntityAlive() || player.ticksExisted <= 100 || player.isInvisible() || player.getHealth() <= 0.0f)) {
            return;
        }
        final double size = this.getSize(player) * -0.0225;
        final FontRenderer var13 = Wrapper.fr;
        GL11.glPushMatrix();
        R3DUtils.start3DOGLConstants();
        GL11.glTranslated((double)(float)x, (float)y + player.height + 0.5, (double)(float)z);
        GL11.glRotatef(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glScaled(size, size, size);
        final Tessellator var14 = Tessellator.getInstance();
        final WorldRenderer var15 = var14.getWorldRenderer();
        final int var16 = Nametags.health ? (var13.getStringWidth(String.valueOf(String.valueOf(this.getPlayerName(player))) + " " + this.getHealth(player)) / 2) : (var13.getStringWidth(this.getPlayerName(player)) / 2);
        final int bordercolor = 1879048192;
        final int maincolor = 1879048192;
        R2DUtils.drawBorderedRect(-var16 - 2, -(Wrapper.mc.fontRendererObj.FONT_HEIGHT - 6), var16 + 2, (float)(Wrapper.mc.fontRendererObj.FONT_HEIGHT + 0.5), 1.0f, -1879048192, bordercolor);
        GL11.glDisable(2929);
        if (!Nametags.health) {
            var13.drawStringWithShadow(this.getPlayerName(player), var16, 0.0f, 15790320);
        }
        else if (Nametags.health) {
            var13.drawStringWithShadow(this.getPlayerName(player), -var13.getStringWidth(String.valueOf(String.valueOf(this.getPlayerName(player))) + " " + this.getHealth(player)) / 2, 0.0f, 15790320);
            var13.drawStringWithShadow(this.getHealth(player), (var13.getStringWidth(String.valueOf(String.valueOf(this.getPlayerName(player))) + " " + this.getHealth(player)) - var13.getStringWidth(this.getHealth(player)) * 2) / 2, 0.0f, this.getHealthColorHEX(player));
        }
        GL11.glEnable(2929);
        if (Nametags.armour) {
            this.renderArmor(player);
        }
        R3DUtils.finish3DOGLConstants();
        GL11.glPopMatrix();
    }
    
    public void renderArmor(final EntityPlayer player) {
        int xOffset = 0;
        ItemStack[] arrayOfItemStack1;
        for (int j = (arrayOfItemStack1 = player.inventory.armorInventory).length, i = 0; i < j; ++i) {
            final ItemStack armourStack = arrayOfItemStack1[i];
            if (armourStack != null) {
                xOffset -= 8;
            }
        }
        if (player.getHeldItem() != null) {
            xOffset -= 8;
            final ItemStack stock = player.getHeldItem().copy();
            if (stock.hasEffect() && (stock.getItem() instanceof ItemTool || stock.getItem() instanceof ItemArmor)) {
                stock.stackSize = 1;
            }
            R3DUtils.renderItemStack(stock, xOffset, -19);
            xOffset += 16;
        }
        final ItemStack[] renderStack = player.inventory.armorInventory;
        for (int index = 3; index >= 0; --index) {
            final ItemStack armourStack2 = renderStack[index];
            if (armourStack2 != null) {
                final ItemStack renderStack2 = armourStack2;
                R3DUtils.renderItemStack(renderStack2, xOffset, -19);
                xOffset += 16;
            }
        }
    }
    
    private int getHealthColorHEX(final EntityPlayer e) {
        final int health = Math.round(20.0f * (e.getHealth() / e.getMaxHealth()));
        int color = -1;
        if (health >= 20) {
            color = 5030935;
        }
        else if (health >= 18) {
            color = 9108247;
        }
        else if (health >= 16) {
            color = 10026904;
        }
        else if (health >= 14) {
            color = 12844472;
        }
        else if (health >= 12) {
            color = 16633879;
        }
        else if (health >= 10) {
            color = 15313687;
        }
        else if (health >= 8) {
            color = 16285719;
        }
        else if (health >= 6) {
            color = 16286040;
        }
        else if (health >= 4) {
            color = 15031100;
        }
        else if (health >= 2) {
            color = 16711680;
        }
        else if (health >= 0) {
            color = 16190746;
        }
        return color;
    }
    
    private String getHealth(final EntityPlayer e) {
        String hp = "";
        final DecimalFormat numberFormat = new DecimalFormat("#.0");
        final double abs = 2.0f * (e.getAbsorptionAmount() / 4.0f);
        double health = (10.0 + abs) * (e.getHealth() / e.getMaxHealth());
        health = Double.valueOf(numberFormat.format(health));
        final int ihealth = (int)health;
        if (health % 1.0 != 0.0) {
            hp = String.valueOf(health);
        }
        else {
            hp = String.valueOf(ihealth);
        }
        return hp;
    }
    
    private String getPlayerName(final EntityPlayer player) {
        String name = "";
        name = player.getDisplayName().getFormattedText();
        return name;
    }
    
    private float getSize(final EntityPlayer player) {
        final Entity ent = Wrapper.mc.thePlayer;
        final boolean angle = isFacingAtEntity(player, 22.0);
        final float dist = ent.getDistanceToEntity(player) / 6.0f;
        final float size = (dist <= 2.0f) ? 1.3f : dist;
        return size;
    }
    
    public static boolean isFacingAtEntity(final Entity cunt, double angleHowClose) {
        final Entity ent = Wrapper.mc.thePlayer;
        final float[] yawPitch = getYawAndPitch(cunt);
        angleHowClose /= 4.5;
        final float yaw = yawPitch[0];
        final float pitch = yawPitch[1];
        return AngleDistance(ent.rotationYaw, yaw) < angleHowClose && AngleDistance(ent.rotationPitch, pitch) < angleHowClose;
    }
    
    public static float[] getYawAndPitch(final Entity target) {
        final Entity ent = Wrapper.mc.thePlayer;
        final double x = target.posX - ent.posX;
        final double z = target.posZ - ent.posZ;
        final double y = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - Wrapper.mc.thePlayer.posY;
        final double helper = MathHelper.sqrt_double(x * x + z * z);
        final float newYaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float newPitch = (float)(Math.atan2(y * 1.0, helper) * 180.0 / 3.141592653589793);
        return new float[] { newYaw, newPitch };
    }
    
    private static float AngleDistance(final float par1, final float par2) {
        float angle = Math.abs(par1 - par2) % 360.0f;
        if (angle > 180.0f) {
            angle = 360.0f - angle;
        }
        return angle;
    }
}
