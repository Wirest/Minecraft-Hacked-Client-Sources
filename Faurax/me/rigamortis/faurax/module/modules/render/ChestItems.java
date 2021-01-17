package me.rigamortis.faurax.module.modules.render;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.helpers.RenderHelper;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.tileentity.*;
import net.minecraft.client.renderer.entity.*;
import me.rigamortis.faurax.*;
import net.minecraft.inventory.*;
import java.util.*;
import com.darkmagician6.eventapi.*;
import net.minecraft.entity.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.*;
import net.minecraft.enchantment.*;
import org.lwjgl.opengl.*;
import net.minecraft.item.*;
import me.rigamortis.faurax.utils.*;

public class ChestItems extends Module implements RenderHelper, CombatHelper, WorldHelper
{
    private RenderItem itemRender;
    public ArrayList<TileEntityChest> chests;
    public ArrayList<ItemStack> items;
    public ArrayList<Integer> IDS;
    
    public ChestItems() {
        this.chests = new ArrayList<TileEntityChest>();
        this.items = new ArrayList<ItemStack>();
        this.IDS = new ArrayList<Integer>();
        this.setName("ChestItems");
        this.setKey("");
        this.setType(ModuleType.RENDER);
        this.setColor(-15096001);
        this.itemRender = new RenderItem(ChestItems.mc.renderEngine, ChestItems.mc.modelManager);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget(4)
    public void renderWorld(final EventRenderWorld e) {
        if (this.isToggled()) {
            for (final Object o : ChestItems.mc.theWorld.loadedTileEntityList) {
                final TileEntity tileEntity = (TileEntity)o;
                if (tileEntity instanceof TileEntityChest) {
                    final TileEntityChest tileChest = (TileEntityChest)tileEntity;
                    final double n = tileChest.getPos().getX();
                    ChestItems.mc.getRenderManager();
                    final double x = n - RenderManager.renderPosX;
                    final double n2 = tileChest.getPos().getY();
                    ChestItems.mc.getRenderManager();
                    final double y = n2 - RenderManager.renderPosY;
                    final double n3 = tileChest.getPos().getZ();
                    ChestItems.mc.getRenderManager();
                    final double z = n3 - RenderManager.renderPosZ;
                    this.drawBackgroundAndItems(tileChest, x + 0.5, y + 1.100000023841858, z + 0.5);
                    if (!Client.getClientHelper().isContainerEmpty(ChestItems.mc.thePlayer.openContainer) && ChestItems.mc.thePlayer.openContainer instanceof ContainerChest) {
                        final int slotId = Client.getClientHelper().getNextSlotInContainer(ChestItems.mc.thePlayer.openContainer);
                        for (int i = 0; i < 36; ++i) {
                            final ArrayList<Integer> ids = this.IDS;
                            ChestItems.mc.thePlayer.openContainer.getSlot(slotId + i).getStack().getItem();
                            if (!ids.contains(Item.getIdFromItem(ChestItems.mc.thePlayer.openContainer.getSlot(slotId + i).getStack().getItem())) && ChestItems.mc.thePlayer.openContainer.getSlot(slotId + i).getStack() != null) {
                                System.out.println(ChestItems.mc.thePlayer.openContainer.getSlot(slotId + i).getStack());
                                final ArrayList<Integer> ids2 = this.IDS;
                                ChestItems.mc.thePlayer.openContainer.getSlot(slotId + i).getStack().getItem();
                                ids2.add(Item.getIdFromItem(ChestItems.mc.thePlayer.openContainer.getSlot(slotId + i).getStack().getItem()));
                                this.items.add(ChestItems.mc.thePlayer.openContainer.getSlot(slotId + i).getStack());
                            }
                        }
                    }
                    System.out.print(this.items.size() + this.IDS.size());
                }
            }
        }
    }
    
    public void renderArmor(final Entity e) {
        int x = -(this.items.size() * 8);
        for (final ItemStack stack : this.items) {
            GlStateManager.pushMatrix();
            GlStateManager.depthMask(true);
            GlStateManager.clear(256);
            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            ChestItems.mc.getRenderItem().zLevel = -100.0f;
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
            ChestItems.mc.getRenderItem().renderItemIntoGUI(stack, x, -14);
            ChestItems.mc.getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, stack, x, -14);
            ChestItems.mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.disableCull();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
            ChestItems.mc.getRenderItem().zLevel = 0.0f;
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
        ChestItems.mc.fontRendererObj.func_175063_a(text, x, -36 - y, -1);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
    
    public void drawBackgroundAndItems(final TileEntityChest e, final double posX, final double posY, final double posZ) {
        final float scale = (float)(0.07 + ChestItems.mc.thePlayer.getDistanceSq(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ()) / 10000.0);
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, posZ);
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glScaled(0.5, 0.5, 0.5);
        final GuiUtils guiUtils = ChestItems.guiUtils;
        GuiUtils.drawRect(-14.0f - this.items.size() * 2.5f, -8.0, 14.0f + this.items.size() * 2.5f, 3.0, -1441722095);
        GlStateManager.func_179098_w();
        GlStateManager.depthMask(true);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glEnable(2896);
        GL11.glScaled(0.2, 0.2, 0.2);
        this.renderArmor(null);
        GL11.glScaled(2.0, 2.0, 2.0);
        GlStateManager.popMatrix();
    }
}
