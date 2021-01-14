package info.sigmaclient.module.impl.render;

import info.sigmaclient.event.impl.EventNametagRender;
import info.sigmaclient.event.impl.EventRender3D;
import info.sigmaclient.management.friend.FriendManager;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.event.RegisterEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;

public class Tags extends Module {

    public Tags(ModuleData data) {
        super(data);
        settings.put(ARMOR, new Setting(ARMOR, true, "Show armor."));
        settings.put(INVISIBLES, new Setting(INVISIBLES, false, "Show invisibles."));
    }

    private String INVISIBLES = "INVISIBLES";
    private String ARMOR = "ARMOR";

    @Override
    @RegisterEvent(events = {EventRender3D.class, EventNametagRender.class})
    public void onEvent(info.sigmaclient.event.Event event) {
        if (event instanceof EventRender3D) {
            EventRender3D er = (EventRender3D) event;
            for (Object o : mc.theWorld.playerEntities) {
                final EntityPlayer player = (EntityPlayer) o;
                if ((Boolean) settings.get(INVISIBLES).getValue() || !player.isInvisible() && !(player instanceof EntityPlayerSP)) {
                    final double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * er.renderPartialTicks - RenderManager.renderPosX;
                    final double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * er.renderPartialTicks - RenderManager.renderPosY;
                    final double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * er.renderPartialTicks - RenderManager.renderPosZ;
                    this.renderNametag(player, x, y, z);
                }
            }
        }
        if (event instanceof EventNametagRender) {
            event.setCancelled(true);
        }
    }

    public void renderNametag(final EntityPlayer player, final double x, final double y, final double z) {
        final double tempY = y + (player.isSneaking() ? 0.5 : 0.7);
        final double size = this.getSize(player) * -0.02;
        GlStateManager.pushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate((float) x, (float) tempY + 1.6f, (float) z);
        GL11.glNormal3f(0.0f, 2.0f, 0.0f);
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        final float var10001 = (mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f;
        GlStateManager.rotate(RenderManager.playerViewX, var10001, 0.0f, 0.0f);
        GL11.glScaled(size, size, size);
        GlStateManager.disableLighting();
        final int width = (mc.fontRendererObj.getStringWidth(player.getName() + " " + this.getHealth(player)) / 2);
        GlStateManager.enableTextures();
        RenderingUtil.rectangle(-width - 2, -(mc.fontRendererObj.FONT_HEIGHT - 6), width + 2, mc.fontRendererObj.FONT_HEIGHT + 1, -1728052224);
        GlStateManager.enableTextures();
        int color = -1;
        String str = player.getName();
        if (FriendManager.isFriend(str)) {
            color = 0x5CD3FF;
            str = FriendManager.getAlias(str);
        }
        mc.fontRendererObj.drawStringWithShadow(str, -mc.fontRendererObj.getStringWidth(String.valueOf(player.getName()) + " " + this.getHealth(player)) / 2, 0.0f, color);
        float health = player.getHealth();
        float[] fractions = new float[]{0f, 0.5f, 1f};
        Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
        float progress = (health * 5) * 0.01f;
        Color customColor = ESP.blendColors(fractions, colors, progress).brighter();
        mc.fontRendererObj.drawStringWithShadow((int) health + "", (mc.fontRendererObj.getStringWidth(String.valueOf(player.getName()) + " " + this.getHealth(player)) - mc.fontRendererObj.getStringWidth(this.getHealth(player)) * 2) / 2, 0.0f, customColor.getRGB());


        GlStateManager.disableBlend();
        if (((Boolean) settings.get(ARMOR).getValue()).booleanValue()) {
            this.renderArmor(player);
        }
        GlStateManager.enableBlend();
        GL11.glColor3d(1.0, 1.0, 1.0);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glPopMatrix();
    }

    public void renderArmor(final EntityPlayer player) {
        int xOffset = 0;
        for (final ItemStack armourStack : player.inventory.armorInventory) {
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
        final ItemStack[] renderStack = player.inventory.armorInventory;
        for (int index = 3; index >= 0; --index) {
            final ItemStack armourStack = renderStack[index];
            if (armourStack != null) {
                final ItemStack renderStack2 = armourStack;
                this.renderItemStack(renderStack2, xOffset, -20);
                xOffset += 16;
            }
        }
    }

    private int getHealthColorHEX(final EntityPlayer e) {
        final int health = Math.round(20.0f * (e.getHealth() / e.getMaxHealth()));
        int color = 0;
        switch (health) {
            case 18:
            case 19: {
                color = 9108247;
                break;
            }
            case 16:
            case 17: {
                color = 10026904;
                break;
            }
            case 14:
            case 15: {
                color = 12844472;
                break;
            }
            case 12:
            case 13: {
                color = 16633879;
                break;
            }
            case 10:
            case 11: {
                color = 15313687;
                break;
            }
            case 8:
            case 9: {
                color = 16285719;
                break;
            }
            case 6:
            case 7: {
                color = 16286040;
                break;
            }
            case 4:
            case 5: {
                color = 15031100;
                break;
            }
            case 2:
            case 3: {
                color = 16711680;
                break;
            }
            case -1:
            case 0:
            case 1: {
                color = 16190746;
                break;
            }
            default: {
                color = -11746281;
                break;
            }
        }
        return color;
    }

    private String getHealth(final EntityPlayer e) {
        String hp = "";
        final DecimalFormat numberFormat = new DecimalFormat("#.0");
        final double abs = 2.0f * (e.getAbsorptionAmount() / 4.0f);
        double health = (10.0 + abs) * (e.getHealth() / e.getMaxHealth());
        health = Double.valueOf(numberFormat.format(health));
        if (Math.floor(health) == health) {
            hp = String.valueOf((int) health);
        } else {
            hp = String.valueOf(health);
        }
        return hp;
    }

    private float getSize(final EntityPlayer player) {
        final Entity ent = mc.thePlayer;
        final double dist = ent.getDistanceToEntity(player) / 5;
        final float size = (dist <= 2.0f) ? 1.3f : (float) dist;
        return size;
    }

    private void renderItemStack(final ItemStack stack, final int x, final int y) {
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        mc.getRenderItem().zLevel = -150.0f;
        mc.getRenderItem().func_180450_b(stack, x, y);
        mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, x, y);
        mc.getRenderItem().zLevel = 0.0f;
        GlStateManager.disableBlend();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        this.renderEnchantText(stack, x, y);
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }

    private void renderEnchantText(final ItemStack stack, final int x, final int y) {
        int enchantmentY = y - 24;
        if (stack.getEnchantmentTagList() != null && stack.getEnchantmentTagList().tagCount() >= 6) {
            mc.fontRendererObj.drawStringWithShadow("god", x * 2, enchantmentY, 16711680);
            return;
        }
        if (stack.getItem() instanceof ItemArmor) {
            final int protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
            final int projectileProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180308_g.effectId, stack);
            final int blastProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack);
            final int fireProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack);
            final int thornsLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
            final int unbreakingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (protectionLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("pr" + protectionLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (projectileProtectionLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("pp" + projectileProtectionLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (blastProtectionLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("bp" + blastProtectionLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (fireProtectionLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("fp" + fireProtectionLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (thornsLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("t" + thornsLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (unbreakingLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("u" + unbreakingLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
        }
        if (stack.getItem() instanceof ItemBow) {
            final int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
            final int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
            final int flameLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
            final int unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (powerLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("po" + powerLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (punchLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("pu" + punchLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (flameLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("f" + flameLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (unbreakingLevel2 > 0) {
                mc.fontRendererObj.drawStringWithShadow("u" + unbreakingLevel2, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
        }
        if (stack.getItem() instanceof ItemSword) {
            final int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
            final int knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
            final int fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
            final int unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (sharpnessLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("sh" + sharpnessLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (knockbackLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("kn" + knockbackLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (fireAspectLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("f" + fireAspectLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (unbreakingLevel2 > 0) {
                mc.fontRendererObj.drawStringWithShadow("ub" + unbreakingLevel2, x * 2, enchantmentY, -1052689);
            }
        }
        if (stack.getItem() instanceof ItemTool) {
            final int unbreakingLevel3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            final int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
            final int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
            final int silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack);
            if (efficiencyLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("eff" + efficiencyLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (fortuneLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("fo" + fortuneLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (silkTouch > 0) {
                mc.fontRendererObj.drawStringWithShadow("st" + silkTouch, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (unbreakingLevel3 > 0) {
                mc.fontRendererObj.drawStringWithShadow("ub" + unbreakingLevel3, x * 2, enchantmentY, -1052689);
            }
        }
        if (stack.getItem() == Items.golden_apple && stack.hasEffect()) {
            mc.fontRendererObj.drawStringWithShadow("god", x * 2, enchantmentY, -1052689);
        }
    }

}
