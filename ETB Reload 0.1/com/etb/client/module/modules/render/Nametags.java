package com.etb.client.module.modules.render;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import com.etb.client.event.events.render.Render3DEvent;
import com.etb.client.utils.value.impl.EnumValue;
import com.etb.client.utils.value.impl.NumberValue;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.*;
import net.minecraft.util.MathHelper;
import optifine.Config;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.opengl.GL11;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.etb.client.Client;
import com.etb.client.event.events.render.Render2DEvent;
import com.etb.client.event.events.render.RenderNametagEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.RenderUtil;
import com.etb.client.utils.value.impl.BooleanValue;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;

/**
 * made by oHare for ETB Reloaded
 *
 * @since 6/29/2019
 **/
public class Nametags extends Module {
    private EnumValue<HealthMode> health = new EnumValue("Health Mode", HealthMode.HEALTH);
    private BooleanValue armor = new BooleanValue("Armor", true);
    private BooleanValue potions = new BooleanValue("Potions", true);
    private BooleanValue dura = new BooleanValue("Durability", true);
    private BooleanValue invis = new BooleanValue("Invisibles", false);
    private BooleanValue mobs = new BooleanValue("Mobs", false);
    private BooleanValue animals = new BooleanValue("Animals", false);
    private BooleanValue players = new BooleanValue("Players", true);
    private BooleanValue passives = new BooleanValue("Passives", false);
    private NumberValue<Double> scale = new NumberValue("Scale", 3.0, 1.0, 5.0, 0.1);

    public Nametags() {
        super("Nametags", Category.RENDER, new Color(29, 187, 102).getRGB());
        addValues(health, mobs, animals, players, passives, armor, potions, dura, invis, scale);
    }

    @Subscribe
    public void onRender2D(Render3DEvent event) {
        mc.theWorld.getLoadedEntityList().forEach(ent -> {
            if (ent instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) ent;
                if (isValid(entity)) {
                    float x = (float) (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - RenderManager.renderPosX);
                    float y = (float) (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - RenderManager.renderPosY) - 1.75f + entity.height + (0.015f * mc.thePlayer.getDistanceToEntity(entity));
                    float z = (float) (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - RenderManager.renderPosZ);
                    this.renderNametag(entity, x, y, z);
                }
            }
        });
    }

    private String getHealth(final EntityLivingBase player) {
        final DecimalFormat numberFormat = new DecimalFormat("0.#");
        return (this.health.getValue() == HealthMode.PERCENT) ? numberFormat.format((100 / player.getMaxHealth()) *player.getHealth()) : numberFormat.format(player.getHealth() / 2.0f + player.getAbsorptionAmount() / 2.0f);
    }

    private void drawNames(final EntityLivingBase player) {
        final float xP = 2.2f;
        float width = this.getWidth(this.getPlayerName(player)) / 2.0f + xP;
        final float w;
        width = (w = (float) (width + (this.getWidth(" " + this.getHealth(player)) / 2 + 2.5)));
        final float nw = -width - xP;
        float offset = this.getWidth(this.getPlayerName(player)) + 4;
        if (this.health.getValue() == HealthMode.PERCENT) {
            RenderUtil.drawBorderedRect(nw, -3.0f, width * 2, 13.0f, 1.0f, new Color(20, 20, 20, 180).getRGB(), new Color(10, 10, 10, 200).getRGB());
        } else {
            RenderUtil.drawBorderedRect(nw + 5.0f, -3.0f, width * 2 - 4, 13.0f, 1.0f, new Color(20, 20, 20, 180).getRGB(), new Color(10, 10, 10, 200).getRGB());
        }
        GlStateManager.disableDepth();
        if (this.health.getValue() == HealthMode.PERCENT) {
            offset += this.getWidth(this.getHealth(player)) + this.getWidth(" %") - 1;
        } else {
            offset += this.getWidth(this.getHealth(player)) + this.getWidth(" ") - 1;
        }
        this.drawString(this.getPlayerName(player), w - offset, 0.0f, 16777215);
        if (this.health.getValue() == HealthMode.PERCENT) {
            this.drawString(String.valueOf(this.getHealth(player)) + "%", w - this.getWidth(String.valueOf(this.getHealth(player)) + " %"), 0.0f, RenderUtil.getHealthColor(player));
        } else {
            this.drawString(this.getHealth(player), w - this.getWidth(String.valueOf(this.getHealth(player)) + " "), 0.0f, RenderUtil.getHealthColor(player));
        }
        GlStateManager.enableDepth();
    }

    private void drawString(final String text, final float x, final float y, final int color) {
        this.mc.fontRendererObj.drawStringWithShadow(text, x, y, color);
    }

    private int getWidth(final String text) {
        return this.mc.fontRendererObj.getStringWidth(text);
    }

    private void startDrawing(final float x, final float y, final float z, final EntityLivingBase player) {
        final float var10001 = (this.mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f;
        final double size = Config.zoomMode ? (this.getSize(player) / 10.0f * this.scale.getValue() * 0.5) : (this.getSize(player) / 10.0f * this.scale.getValue() * 1.5);
        GL11.glPushMatrix();
        RenderUtil.startDrawing();
        GL11.glTranslatef(x, y, z);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-this.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(this.mc.getRenderManager().playerViewX, var10001, 0.0f, 0.0f);
        GL11.glScaled(-0.01666666753590107 * size, -0.01666666753590107 * size, 0.01666666753590107 * size);
    }

    private void stopDrawing() {
        RenderUtil.stopDrawing();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    private void renderNametag(final EntityLivingBase player, final float x, float y, final float z) {
        y += (float) (1.55 + (player.isSneaking() ? 0.5 : 0.7));
        this.startDrawing(x, y, z, player);
        this.drawNames(player);
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        if (this.armor.getValue() && player instanceof EntityPlayer) {
            this.renderArmor((EntityPlayer) player);
        }
        this.stopDrawing();
    }

    private void renderArmor(final EntityPlayer player) {
        ItemStack[] renderStack = player.inventory.armorInventory;
        final int length = renderStack.length;
        int xOffset = 0;
        ItemStack[] array;
        for (int length2 = (array = renderStack).length, i = 0; i < length2; ++i) {
            final ItemStack armourStack = array[i];
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
            this.renderItemStack(stock, xOffset, -20);
            xOffset += 16;
        }
        renderStack = player.inventory.armorInventory;
        for (int index = 3; index >= 0; --index) {
            final ItemStack armourStack = renderStack[index];
            if (armourStack != null) {
                this.renderItemStack(armourStack, xOffset, -20);
                xOffset += 16;
            }
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private String getPlayerName(final EntityLivingBase player) {
        String name = player.getDisplayName().getFormattedText();
        if (Client.INSTANCE.getFriendManager().isFriend(player.getName())) {
            name = "§b" + (Client.INSTANCE.getFriendManager().getFriend(player.getName()).getAlias() != null ? Client.INSTANCE.getFriendManager().getFriend(player.getName()).getAlias() : player.getName());
        }
        return name;
    }

    private float getSize(final EntityLivingBase player) {
        return (this.mc.thePlayer.getDistanceToEntity(player) / 4.0f <= 2.0f) ? 2.0f : (this.mc.thePlayer.getDistanceToEntity(player) / 4.0f);
    }

    private void renderItemStack(final ItemStack stack, final int x, final int y) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        this.mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        this.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, stack, x, y);
        this.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        final double s = 0.5;
        GlStateManager.scale(s, s, s);
        GlStateManager.disableDepth();
        this.renderEnchantText(stack, x, y);
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }

    private void renderEnchantText(final ItemStack stack, final int x, final int y) {
        int enchantmentY = y - 24;
        if (stack.getEnchantmentTagList() != null && stack.getEnchantmentTagList().tagCount() >= 6) {
            this.mc.fontRendererObj.drawStringWithShadow("god", x * 2, enchantmentY, 16711680);
            return;
        }
        if (stack.getItem() instanceof ItemArmor) {
            final int protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
            final int projectileProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack);
            final int blastProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack);
            final int fireProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack);
            final int thornsLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
            final int unbreakingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            final int damage = stack.getMaxDamage() - stack.getItemDamage();
            if (this.dura.getValue()) {
                this.mc.fontRendererObj.drawStringWithShadow(new StringBuilder().append(damage).toString(), x * 2, y, 16777215);
            }
            if (protectionLevel > 0) {
                this.mc.fontRendererObj.drawStringWithShadow("prot" + protectionLevel, x * 2, enchantmentY, -1);
                enchantmentY += 8;
            }
            if (projectileProtectionLevel > 0) {
                this.mc.fontRendererObj.drawStringWithShadow("proj" + projectileProtectionLevel, x * 2, enchantmentY, -1);
                enchantmentY += 8;
            }
            if (blastProtectionLevel > 0) {
                this.mc.fontRendererObj.drawStringWithShadow("bp" + blastProtectionLevel, x * 2, enchantmentY, -1);
                enchantmentY += 8;
            }
            if (fireProtectionLevel > 0) {
                this.mc.fontRendererObj.drawStringWithShadow("frp" + fireProtectionLevel, x * 2, enchantmentY, -1);
                enchantmentY += 8;
            }
            if (thornsLevel > 0) {
                this.mc.fontRendererObj.drawStringWithShadow("th" + thornsLevel, x * 2, enchantmentY, -1);
                enchantmentY += 8;
            }
            if (unbreakingLevel > 0) {
                this.mc.fontRendererObj.drawStringWithShadow("unb" + unbreakingLevel, x * 2, enchantmentY, -1);
                enchantmentY += 8;
            }
        }
        if (stack.getItem() instanceof ItemBow) {
            final int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
            final int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
            final int flameLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
            final int unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (powerLevel > 0) {
                this.mc.fontRendererObj.drawStringWithShadow("pow" + powerLevel, x * 2, enchantmentY, -1);
                enchantmentY += 8;
            }
            if (punchLevel > 0) {
                this.mc.fontRendererObj.drawStringWithShadow("pun" + punchLevel, x * 2, enchantmentY, -1);
                enchantmentY += 8;
            }
            if (flameLevel > 0) {
                this.mc.fontRendererObj.drawStringWithShadow("flame" + flameLevel, x * 2, enchantmentY, -1);
                enchantmentY += 8;
            }
            if (unbreakingLevel2 > 0) {
                this.mc.fontRendererObj.drawStringWithShadow("unb" + unbreakingLevel2, x * 2, enchantmentY, -1);
                enchantmentY += 8;
            }
        }
        if (stack.getItem() instanceof ItemSword) {
            final int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
            final int knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
            final int fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
            final int unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (sharpnessLevel > 0) {
                this.mc.fontRendererObj.drawStringWithShadow("sh" + sharpnessLevel, x * 2, enchantmentY, -1);
                enchantmentY += 8;
            }
            if (knockbackLevel > 0) {
                this.mc.fontRendererObj.drawStringWithShadow("kb" + knockbackLevel, x * 2, enchantmentY, -1);
                enchantmentY += 8;
            }
            if (fireAspectLevel > 0) {
                this.mc.fontRendererObj.drawStringWithShadow("fire" + fireAspectLevel, x * 2, enchantmentY, -1);
                enchantmentY += 8;
            }
            if (unbreakingLevel2 > 0) {
                this.mc.fontRendererObj.drawStringWithShadow("unb" + unbreakingLevel2, x * 2, enchantmentY, -1);
            }
        }
        if (stack.getItem() instanceof ItemTool) {
            final int unbreakingLevel3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            final int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
            final int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
            final int silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack);
            if (efficiencyLevel > 0) {
                this.mc.fontRendererObj.drawStringWithShadow("eff" + efficiencyLevel, x * 2, enchantmentY, -1);
                enchantmentY += 8;
            }
            if (fortuneLevel > 0) {
                this.mc.fontRendererObj.drawStringWithShadow("fo" + fortuneLevel, x * 2, enchantmentY, -1);
                enchantmentY += 8;
            }
            if (silkTouch > 0) {
                this.mc.fontRendererObj.drawStringWithShadow("silk" + silkTouch, x * 2, enchantmentY, -1);
                enchantmentY += 8;
            }
            if (unbreakingLevel3 > 0) {
                this.mc.fontRendererObj.drawStringWithShadow("ub" + unbreakingLevel3, x * 2, enchantmentY, -1);
            }
        }
        if (stack.getItem() == Items.golden_apple && stack.hasEffect()) {
            this.mc.fontRendererObj.drawStringWithShadow("god", x * 2, enchantmentY, -1);
        }
    }

    @Subscribe
    public void onRenderNametag(RenderNametagEvent event) {
        event.setCanceled(true);
    }

    public boolean isValid(EntityLivingBase ent) {
        return ent != mc.thePlayer && !(ent instanceof EntityArmorStand) && ent.isEntityAlive() && (((ent instanceof EntityVillager || ent instanceof EntityIronGolem) && passives.isEnabled()) || (ent instanceof EntityMob && mobs.isEnabled()) || (ent instanceof EntityAnimal && animals.isEnabled()) || (ent instanceof EntityPlayer && players.isEnabled()));
    }

    private enum HealthMode {
        HEALTH, PERCENT
    }
}
