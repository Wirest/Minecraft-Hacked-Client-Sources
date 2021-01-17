package me.rigamortis.faurax.module.modules.render;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.helpers.RenderHelper;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.values.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.*;
import java.util.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.*;
import net.minecraft.enchantment.*;
import org.lwjgl.opengl.*;
import net.minecraft.item.*;
import java.text.*;
import me.rigamortis.faurax.friends.*;
import net.minecraft.client.gui.*;
import me.rigamortis.faurax.utils.*;

public class NameTags extends Module implements RenderHelper, CombatHelper
{
    public int color;
    private RenderItem itemRender;
    private String healthColor;
    public static Value armor;
    public static Value scale;
    
    static {
        NameTags.armor = new Value("NameTags", Boolean.TYPE, "Armor", true);
        NameTags.scale = new Value("NameTags", Float.TYPE, "Scale", 1.0f, 1.0f, 6.0f);
    }
    
    public NameTags() {
        this.setName("NameTags");
        this.setKey("M");
        this.setType(ModuleType.RENDER);
        this.setColor(-15096001);
        this.itemRender = new RenderItem(NameTags.mc.renderEngine, NameTags.mc.modelManager);
        this.setModInfo("");
        this.setVisible(true);
        Client.getValues();
        ValueManager.values.add(NameTags.armor);
        Client.getValues();
        ValueManager.values.add(NameTags.scale);
    }
    
    @EventTarget(4)
    public void renderOldNameTag(final EventRenderEntityName e) {
        if (this.isToggled()) {
            e.setCancelled(true);
        }
    }
    
    @EventTarget(4)
    public void renderOldNameTagSneak(final EventRenderEntityNameSneak e) {
        if (this.isToggled()) {
            e.setCancelled(true);
        }
    }
    
    @EventTarget(4)
    public void renderWorld(final EventRenderWorld e) {
        if (this.isToggled()) {
            for (final Object i : NameTags.mc.theWorld.loadedEntityList) {
                if (i instanceof EntityPlayer) {
                    final EntityPlayer entity = (EntityPlayer)i;
                    if (entity.getName() == NameTags.mc.thePlayer.getName()) {
                        continue;
                    }
                    final float posX = (float)((float)entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * NameTags.mc.timer.renderPartialTicks);
                    final float posY = (float)((float)entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * NameTags.mc.timer.renderPartialTicks);
                    final float posZ = (float)((float)entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * NameTags.mc.timer.renderPartialTicks);
                    this.drawName(entity, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY + entity.height + 0.550000011920929, posZ - RenderManager.renderPosZ);
                }
            }
        }
    }
    
    public void renderArmor(final Entity e) {
        final ArrayList<ItemStack> itemsToRender = new ArrayList<ItemStack>();
        for (int i = 4; i >= 0; --i) {
            final ItemStack stack = ((EntityPlayer)e).getEquipmentInSlot(i);
            if (stack != null) {
                itemsToRender.add(stack);
            }
        }
        int x = -(itemsToRender.size() * 8);
        final Iterator<ItemStack> iterator = itemsToRender.iterator();
        while (iterator.hasNext()) {
            final ItemStack stack = iterator.next();
            GlStateManager.pushMatrix();
            GlStateManager.depthMask(true);
            GlStateManager.clear(256);
            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            NameTags.mc.getRenderItem().zLevel = -100.0f;
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableAlpha();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            NameTags.mc.getRenderItem().renderItemIntoGUI(stack, x, -18);
            NameTags.mc.getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, stack, x, -18);
            NameTags.mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.disableCull();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
            NameTags.mc.getRenderItem().zLevel = 0.0f;
            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
            final String text = "";
            if (stack != null) {
                int y = 0;
                final int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.effectId, stack);
                final int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
                final int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180313_o.effectId, stack);
                if (sLevel > 0) {
                    GL11.glDisable(2896);
                    drawEnchantTag("Sh" + sLevel, x, y);
                    y -= 9;
                }
                if (fLevel > 0) {
                    GL11.glDisable(2896);
                    drawEnchantTag("Fir" + fLevel, x, y);
                    y -= 9;
                }
                if (kLevel > 0) {
                    GL11.glDisable(2896);
                    drawEnchantTag("Kb" + kLevel, x, y);
                }
                else if (stack.getItem() instanceof ItemArmor) {
                    final int pLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180310_c.effectId, stack);
                    final int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
                    final int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
                    if (pLevel > 0) {
                        GL11.glDisable(2896);
                        drawEnchantTag("P" + pLevel, x, y);
                        y -= 9;
                    }
                    if (tLevel > 0) {
                        GL11.glDisable(2896);
                        drawEnchantTag("Th" + tLevel, x, y);
                        y -= 9;
                    }
                    if (uLevel > 0) {
                        GL11.glDisable(2896);
                        drawEnchantTag("Unb" + uLevel, x, y);
                    }
                }
                else if (stack.getItem() instanceof ItemBow) {
                    final int powLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
                    final int punLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
                    final int fireLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
                    if (powLevel > 0) {
                        GL11.glDisable(2896);
                        drawEnchantTag("Pow" + powLevel, x, y);
                        y -= 9;
                    }
                    if (punLevel > 0) {
                        GL11.glDisable(2896);
                        drawEnchantTag("Pun" + punLevel, x, y);
                        y -= 9;
                    }
                    if (fireLevel > 0) {
                        GL11.glDisable(2896);
                        drawEnchantTag("Fir" + fireLevel, x, y);
                    }
                }
                else if (stack.getRarity() == EnumRarity.EPIC) {
                    drawEnchantTag("§6God", x, y);
                }
                x += 16;
            }
        }
    }
    
    private static void drawEnchantTag(final String text, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        x *= (int)1.75;
        y -= 4;
        GL11.glScalef(0.57f, 0.57f, 0.57f);
        NameTags.mc.fontRendererObj.func_175063_a(text, x, -36 - y, -1);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
    
    public void drawName(final Entity e, final double posX, final double posY, final double posZ) {
        final EntityPlayer ep = (EntityPlayer)e;
        String name = ep.getDisplayName().getFormattedText();
        float scale = (float)(NameTags.scale.getFloatValue() / 20.0f + NameTags.mc.thePlayer.getDistanceSq(e.posX, e.posY, e.posZ) / 10000.0);
        if (NameTags.mc.thePlayer.getDistance(e.posX, e.posY, e.posZ) <= 256.0) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(posX, posY, posZ);
            GL11.glNormal3f(0.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
            GlStateManager.scale(-scale, -scale, scale);
            if (scale >= 0.3f) {
                scale = 0.3f;
            }
            GL11.glScaled(0.5, 0.5, 0.5);
            if (NameTags.armor.getBooleanValue()) {
                this.renderArmor(e);
            }
            GL11.glScaled(1.5, 1.5, 1.5);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glScaled(0.5, 0.5, 0.5);
            final DecimalFormat decimal = new DecimalFormat("#.#");
            final float percent = Float.valueOf(decimal.format(ep.getHealth() / 2.0f));
            try {
                for (final Friend friend : FriendManager.friends) {
                    if (!ep.getName().toLowerCase().contains(friend.getName().toLowerCase())) {
                        continue;
                    }
                    char fix = 'r';
                    for (int i = ep.getName().indexOf(friend.getName()); i > 0; --i) {
                        if (ep.getName().charAt(i) == '§') {
                            fix = ep.getName().charAt(i + 1);
                            break;
                        }
                    }
                    name = ep.getName().replace(friend.getName(), "§3" + friend.getAlias() + "§" + fix);
                }
            }
            catch (Exception ex) {}
            if (percent > 6.0f) {
                this.healthColor = "§2";
            }
            if (percent < 5.0f) {
                this.healthColor = "§6";
            }
            if (percent < 3.0f) {
                this.healthColor = "§c";
            }
            final GuiUtils guiUtils = NameTags.guiUtils;
            Gui.drawRect(0 - NameTags.mc.fontRendererObj.getStringWidth(String.valueOf(name) + " " + percent) / 2 - 2, -2, 0 + NameTags.mc.fontRendererObj.getStringWidth(String.valueOf(name) + " " + percent) / 2 + 2, 10, -1441722095);
            GlStateManager.func_179098_w();
            GlStateManager.depthMask(true);
            NameTags.mc.fontRendererObj.func_175065_a(String.valueOf(name) + " " + this.healthColor + percent, -NameTags.mc.fontRendererObj.getStringWidth(String.valueOf(name) + " " + this.healthColor + percent) / 2, 0.0f, -1, true);
            GL11.glScaled(2.0, 2.0, 2.0);
            GL11.glDisable(3042);
            GL11.glEnable(2929);
            GL11.glEnable(2896);
            GlStateManager.popMatrix();
        }
    }
}
