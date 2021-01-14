package com.etb.client.module.modules.render;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.etb.client.event.events.input.KeyPressEvent;
import com.etb.client.gui.tabgui.TabMain;
import com.etb.client.module.modules.combat.KillAura;
import com.etb.client.utils.MathUtils;
import com.etb.client.utils.RenderUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.opengl.GL11;

import com.etb.client.Client;
import com.etb.client.event.events.render.Render2DEvent;
import com.etb.client.gui.notification.Notification;
import com.etb.client.module.Module;
import com.etb.client.utils.font.Fonts;
import com.etb.client.utils.value.impl.BooleanValue;
import com.etb.client.utils.value.impl.NumberValue;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import tv.twitch.chat.Chat;

/**
 * made by etb for Client
 *
 * @since 5/29/2019
 **/
public class HUD extends Module {
    private BooleanValue armor = new BooleanValue("Armor", true);
    private BooleanValue potions = new BooleanValue("Potion", true);
    private BooleanValue info = new BooleanValue("Info", true);
    private BooleanValue notifications = new BooleanValue("Notifications", true);
    private BooleanValue suffix = new BooleanValue("Suffix", true);
    private BooleanValue logo = new BooleanValue("Logo", true);
    private BooleanValue rainbow = new BooleanValue("Rainbow", false);
    private BooleanValue tabgui = new BooleanValue("TabGui", true);
    private BooleanValue target = new BooleanValue("Target", true);
    private BooleanValue item = new BooleanValue("Item", true);
    private NumberValue<Float> itemscale = new NumberValue("ItemScale", 1.0F, 0.1f, 2.0f, 0.1f);
    private NumberValue<Float> targetscale = new NumberValue("TargetScale", 1.0F, 0.1f, 2.0f, 0.1f);
    private String[] directions = new String[]{"S", "SW", "W", "NW", "N", "NE", "E", "SE"};
    private TabMain tabGUI;

    public HUD() {
        super("HUD", Category.RENDER, -1);
        setDescription("UI");
        addValues(armor, potions, info, notifications, suffix, logo, tabgui, rainbow,target,item,itemscale,targetscale);
        setHidden(true);
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        KillAura killAura = (KillAura) Client.INSTANCE.getModuleManager().getModule("killaura");
        if (mc.gameSettings.showDebugInfo || mc.thePlayer == null) return;
        if (tabgui.isEnabled()) tabGUI.onRender(event.getSR());
        if (armor.isEnabled()) drawArmor(event.getSR());
        if (potions.isEnabled()) drawPotionStatus(event.getSR());
        if (notifications.isEnabled()) drawNotifications();
        if (item.isEnabled()) {
            if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.getHeldItem() != null) {
                RenderUtil.drawBorderedRect(2, (tabgui.isEnabled() ? 88 : 26) + (logo.isEnabled() ? 15 : 0), 46 + mc.fontRendererObj.getStringWidth("Name: " + StringUtils.stripControlCodes(Minecraft.getMinecraft().thePlayer.getHeldItem().getItem().getItemStackDisplayName(Minecraft.getMinecraft().thePlayer.getHeldItem()))), 50, 1.5, new Color(0, 0, 0, 244).getRGB(), new Color(0, 0, 0, 155).getRGB());
                GL11.glPushMatrix();
                GlStateManager.scale(2, 2, 2);
                RenderItem ir = new RenderItem(Minecraft.getMinecraft().getTextureManager(), Minecraft.getMinecraft().modelManager);
                RenderHelper.enableGUIStandardItemLighting();
                ir.renderItemIntoGUI(Minecraft.getMinecraft().thePlayer.getHeldItem(), (9) / 2, ((tabgui.isEnabled() ? 88 : 26) + (logo.isEnabled() ? 15 : 0) + 4) / 2);
                RenderHelper.disableStandardItemLighting();
                GlStateManager.scale(1, 1, 1);
                GlStateManager.enableAlpha();
                GlStateManager.disableCull();
                GlStateManager.disableBlend();
                GlStateManager.disableLighting();
                GlStateManager.clear(256);
                GL11.glPopMatrix();
                if (Minecraft.getMinecraft().thePlayer.getHeldItem().getMaxDamage() > 0) {
                    int damage = Minecraft.getMinecraft().thePlayer.getHeldItem().getMaxDamage() - Minecraft.getMinecraft().thePlayer.getHeldItem().getItemDamage();
                    mc.fontRendererObj.drawStringWithShadow("Dura: " + String.valueOf(damage), (float) 9, (tabgui.isEnabled() ? 88 : 26) + (logo.isEnabled() ? 15 : 0) + 39, -1);
                } else if (Minecraft.getMinecraft().thePlayer.getHeldItem().isStackable()) {
                    mc.fontRendererObj.drawStringWithShadow("Stack Size: " + Minecraft.getMinecraft().thePlayer.getHeldItem().stackSize, 9, (tabgui.isEnabled() ? 88 : 26) + (logo.isEnabled() ? 15 : 0) + 39, -1);
                }
                mc.fontRendererObj.drawStringWithShadow("Name: " + StringUtils.stripControlCodes(Minecraft.getMinecraft().thePlayer.getHeldItem().getItem().getItemStackDisplayName(Minecraft.getMinecraft().thePlayer.getHeldItem())), 42, (tabgui.isEnabled() ? 88 : 26) + (logo.isEnabled() ? 15 : 0) + 12, -1);
                mc.fontRendererObj.drawStringWithShadow("ID: " + Item.getIdFromItem(Minecraft.getMinecraft().thePlayer.getHeldItem().getItem()), 42, (tabgui.isEnabled() ? 88 : 26) + (logo.isEnabled() ? 15 : 0) + 24, -1);
            }
        }
        if (target.isEnabled()) {
            if (Minecraft.getMinecraft().thePlayer != null && killAura.target != null && killAura.target instanceof EntityPlayer) {
                NetworkPlayerInfo networkPlayerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(killAura.target.getUniqueID());
                String ping = (networkPlayerInfo == null) ? "0ms" : (networkPlayerInfo.getResponseTime() + "ms");
                RenderUtil.drawBorderedRect(2,(tabgui.isEnabled() ? 88 : 26) + (logo.isEnabled() ? 15 : 0) + ((item.isEnabled() && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.getHeldItem() != null) ? 58:0),130,47, 1.5, new Color(0, 0, 0, 244).getRGB(), new Color(0, 0, 0, 155).getRGB());
                final String playerName = killAura.target.getName();
                RenderUtil.drawRect(5.5f, (tabgui.isEnabled() ? 88 : 26) + (logo.isEnabled() ? 15 : 0) + ((item.isEnabled() && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.getHeldItem() != null) ? 58:0) + 3.5f, 29, 29, new Color(0, 0, 0).getRGB());
                mc.fontRendererObj.drawStringWithShadow(playerName + " " + getHealth(killAura.target) + MathUtils.round(killAura.target.getHealth() / 2, 2) +" " + ChatFormatting.DARK_GRAY + ping, 6, (tabgui.isEnabled() ? 88 : 26) + (logo.isEnabled() ? 15 : 0) + ((item.isEnabled() && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.getHeldItem() != null) ? 58:0) + 36, -1);
                try {
                    AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(playerName), playerName).loadTexture(Minecraft.getMinecraft().getResourceManager());
                    Minecraft.getMinecraft().getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(playerName));
                    GL11.glColor4f(1F, 1F, 1F, 1F);
                    Gui.drawScaledCustomSizeModalRect(6, (tabgui.isEnabled() ? 88 : 26) + (logo.isEnabled() ? 15 : 0) + ((item.isEnabled() && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.getHeldItem() != null) ? 58:0) + 4, 8, 8, 8, 8, 28, 28, 64, 64);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                RenderUtil.drawBorderedRect(45,(tabgui.isEnabled() ? 88 : 26) + (logo.isEnabled() ? 15 : 0) + ((item.isEnabled() && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.getHeldItem() != null) ? 58:0) + 10,76,16,0.5,new Color(0).getRGB(),new Color(35,35,35).getRGB());
                double inc = 75 / killAura.target.getMaxHealth();
                double end = inc * (killAura.target.getHealth() > killAura.target.getMaxHealth() ? killAura.target.getMaxHealth():killAura.target.getHealth());
                RenderUtil.drawRect(45.5f, (tabgui.isEnabled() ? 88 : 26) + (logo.isEnabled() ? 15 : 0) + ((item.isEnabled() && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.getHeldItem() != null) ? 58:0)+10.5f, end, 15,getHealthColor(killAura.target));
            }
        }
        if (logo.isEnabled()) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            RenderUtil.drawImage(new ResourceLocation("client/etblogo.png"), -4, -4, 35, 33);
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            mc.fontRendererObj.drawStringWithShadow(ChatFormatting.GRAY + String.valueOf(Client.INSTANCE.version), 24, 15, -1);
        } else
            mc.fontRendererObj.drawStringWithShadow(Client.INSTANCE.clientName + " " + ChatFormatting.GRAY + Client.INSTANCE.version, 2, 2, new Color(0xff4d4c).getRGB());
        int y = 2;
        ArrayList<Module> mods = new ArrayList(Client.INSTANCE.getModuleManager().getModuleMap().values());
        mods.sort(Comparator.comparingDouble(m -> -mc.fontRendererObj.getStringWidth(m.getLabel() + (suffix.isEnabled() ? (m.getSuffix() != null ? ChatFormatting.GRAY + " - " + m.getSuffix() : "") : ""))));
        for (Module m : mods) {
            if (m.isEnabled() && !m.isHidden()) {
                final String dispName = m.getLabel() + (suffix.isEnabled() ? (m.getSuffix() != null ? ChatFormatting.GRAY + " - " + m.getSuffix() : "") : "");
                mc.fontRendererObj.drawStringWithShadow(dispName, event.getSR().getScaledWidth() - mc.fontRendererObj.getStringWidth(dispName) - 2, y, rainbow.isEnabled() ? RenderUtil.getRainbow(3000, -(12 * y)) : m.getColor());
                y += mc.fontRendererObj.FONT_HEIGHT + 1;
            }
        }
        String text = ChatFormatting.GRAY + "X" + ChatFormatting.WHITE + ": " + MathHelper.floor_double(mc.thePlayer.posX) + " " + ChatFormatting.GRAY + "Y" + ChatFormatting.WHITE + ": " + MathHelper.floor_double(mc.thePlayer.posY) + " " + ChatFormatting.GRAY + "Z" + ChatFormatting.WHITE + ": " + MathHelper.floor_double(mc.thePlayer.posZ);
        int ychat = mc.ingameGUI.getChatGUI().getChatOpen() ? 25 : 10;
        if (info.getValue()) {
            mc.fontRendererObj.drawStringWithShadow(text, 4, new ScaledResolution(mc).getScaledHeight() - ychat, new Color(11, 12, 17).getRGB());
            mc.fontRendererObj.drawStringWithShadow(ChatFormatting.GRAY + "FPS: " + ChatFormatting.WHITE + mc.getDebugFPS(), 2, (tabgui.isEnabled() ? 76 : 14) + (logo.isEnabled() ? 15 : 0), -1);
            String direction = directions[MathUtils.wrapAngleToDirection(mc.thePlayer.rotationYaw, directions.length)];
            mc.fontRendererObj.drawStringWithShadow("[" + direction + "]", mc.fontRendererObj.getStringWidth(Client.INSTANCE.clientName + " " + Client.INSTANCE.version + 2), logo.isEnabled() ? 15 : 2, new Color(0xff4d4c).getRGB());
        }
        if (logo.isEnabled()) {
            if (tabGUI.getY() != 25) {
                tabGUI = null;
                tabGUI = new TabMain(2, 25);
                tabGUI.init();
            }
        } else {
            if (tabGUI.getY() != 12) {
                tabGUI = null;
                tabGUI = new TabMain(2, 12);
                tabGUI.init();
            }
        }
    }

    @Subscribe
    public void onKeyPress(KeyPressEvent event) {
        if (tabgui.isEnabled()) tabGUI.onKeypress(event.getKey());
    }

    private void drawPotionStatus(ScaledResolution sr) {
        int y = 0;
        for (final PotionEffect effect : (Collection<PotionEffect>) this.mc.thePlayer.getActivePotionEffects()) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String PType = I18n.format(potion.getName());
            switch (effect.getAmplifier()) {
                case 1:
                    PType = PType + " II";
                    break;
                case 2:
                    PType = PType + " III";
                    break;
                case 3:
                    PType = PType + " IV";
                    break;
                default:
                    break;
            }
            if (effect.getDuration() < 600 && effect.getDuration() > 300) {
                PType = PType + "\2477:\2476 " + Potion.getDurationString(effect);
            } else if (effect.getDuration() < 300) {
                PType = PType + "\2477:\247c " + Potion.getDurationString(effect);
            } else if (effect.getDuration() > 600) {
                PType = PType + "\2477:\2477 " + Potion.getDurationString(effect);
            }
            int ychat = mc.ingameGUI.getChatGUI().getChatOpen() ? 5 : -10;
            mc.fontRendererObj.drawStringWithShadow(PType, sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(PType) - 1, sr.getScaledHeight() - mc.fontRendererObj.FONT_HEIGHT + y - 12 - ychat, potion.getLiquidColor());
            y -= 10;
        }
    }

    private void drawArmor(ScaledResolution sr) {
        if (mc.thePlayer.capabilities.isCreativeMode) return;
        GL11.glPushMatrix();
        int divide = 0;
        RenderItem ir = new RenderItem(mc.getTextureManager(), mc.modelManager);
        List<ItemStack> stuff = new ArrayList<>();
        int split = 15;
        for (int index = 3; index >= 0; index--) {
            ItemStack armor = mc.thePlayer.inventory.armorInventory[index];
            if (armor != null) stuff.add(armor);
        }
        for (ItemStack everything : stuff) {
            divide++;
            boolean half = divide > 2;
            int x = half ? (sr.getScaledWidth() / 2) + 93 : (sr.getScaledWidth() / 2) - 110;
            int y = split + sr.getScaledHeight() - (half ? 48 + 28 : 48);
            if (mc.theWorld != null) {
                RenderHelper.disableStandardItemLighting();
                ir.renderItemIntoGUI(everything, x, y);
                ir.renderItemOverlays(mc.fontRendererObj, everything, x, y);
                RenderHelper.enableGUIStandardItemLighting();
                split += 15;
            }
            int damage = everything.getMaxDamage() - everything.getItemDamage();
            GlStateManager.enableAlpha();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            GlStateManager.clear(256);
            mc.fontRendererObj.drawStringWithShadow(String.valueOf(damage), x + (half ? 18 : -18), y + 5, 0xFFFFFFFF);
        }
        GL11.glPopMatrix();
    }

    private void drawNotifications() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        double startY = res.getScaledHeight() - 25;
        final double lastY = startY;
        for (int i = 0; i < Client.INSTANCE.getNotificationManager().getNotifications().size(); i++) {
            Notification not = Client.INSTANCE.getNotificationManager().getNotifications().get(i);
            if (not.shouldDelete()) Client.INSTANCE.getNotificationManager().getNotifications().remove(i);
            not.draw(startY, lastY);
            startY -= not.getHeight() + 1;
        }
    }
    private ChatFormatting getHealth(EntityLivingBase player) {
        final double health = Math.ceil(player.getHealth());
        final double maxHealth = player.getMaxHealth();
        final double percentage = 100 * (health / maxHealth);
        if (percentage > 85) return ChatFormatting.DARK_GREEN;
        else if (percentage > 75) return ChatFormatting.GREEN;
        else if (percentage > 50) return ChatFormatting.YELLOW;
        else if (percentage > 25) return ChatFormatting.RED;
        else if (percentage > 0) return ChatFormatting.DARK_RED;
        return ChatFormatting.BLACK;
    }
    private int getHealthColor(EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0F, 1.0F, 0.45F) | 0xFF000000;
    }
    @Override
    public void onEnable() {
        tabGUI = new TabMain(2, 12);
        tabGUI.init();
    }

    @Override
    public void onDisable() {
        tabGUI = null;
    }
}