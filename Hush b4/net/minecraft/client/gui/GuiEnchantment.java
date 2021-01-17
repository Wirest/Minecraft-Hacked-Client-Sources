// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.enchantment.Enchantment;
import com.google.common.collect.Lists;
import net.minecraft.util.EnchantmentNameParts;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.util.glu.Project;
import net.minecraft.client.renderer.GlStateManager;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraft.world.IWorldNameable;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.ContainerEnchantment;
import java.util.Random;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiEnchantment extends GuiContainer
{
    private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE;
    private static final ResourceLocation ENCHANTMENT_TABLE_BOOK_TEXTURE;
    private static final ModelBook MODEL_BOOK;
    private final InventoryPlayer playerInventory;
    private Random random;
    private ContainerEnchantment container;
    public int field_147073_u;
    public float field_147071_v;
    public float field_147069_w;
    public float field_147082_x;
    public float field_147081_y;
    public float field_147080_z;
    public float field_147076_A;
    ItemStack field_147077_B;
    private final IWorldNameable field_175380_I;
    
    static {
        ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/enchanting_table.png");
        ENCHANTMENT_TABLE_BOOK_TEXTURE = new ResourceLocation("textures/entity/enchanting_table_book.png");
        MODEL_BOOK = new ModelBook();
    }
    
    public GuiEnchantment(final InventoryPlayer inventory, final World worldIn, final IWorldNameable p_i45502_3_) {
        super(new ContainerEnchantment(inventory, worldIn));
        this.random = new Random();
        this.playerInventory = inventory;
        this.container = (ContainerEnchantment)this.inventorySlots;
        this.field_175380_I = p_i45502_3_;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        this.fontRendererObj.drawString(this.field_175380_I.getDisplayName().getUnformattedText(), 12.0, 5.0, 4210752);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        this.func_147068_g();
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final int i = (this.width - this.xSize) / 2;
        final int j = (this.height - this.ySize) / 2;
        for (int k = 0; k < 3; ++k) {
            final int l = mouseX - (i + 60);
            final int i2 = mouseY - (j + 14 + 19 * k);
            if (l >= 0 && i2 >= 0 && l < 108 && i2 < 19 && this.container.enchantItem(Minecraft.thePlayer, k)) {
                this.mc.playerController.sendEnchantPacket(this.container.windowId, k);
            }
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiEnchantment.ENCHANTMENT_TABLE_GUI_TEXTURE);
        final int i = (this.width - this.xSize) / 2;
        final int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        GlStateManager.viewport((scaledresolution.getScaledWidth() - 320) / 2 * scaledresolution.getScaleFactor(), (scaledresolution.getScaledHeight() - 240) / 2 * scaledresolution.getScaleFactor(), 320 * scaledresolution.getScaleFactor(), 240 * scaledresolution.getScaleFactor());
        GlStateManager.translate(-0.34f, 0.23f, 0.0f);
        Project.gluPerspective(90.0f, 1.3333334f, 9.0f, 80.0f);
        final float f = 1.0f;
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate(0.0f, 3.3f, -16.0f);
        GlStateManager.scale(f, f, f);
        final float f2 = 5.0f;
        GlStateManager.scale(f2, f2, f2);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiEnchantment.ENCHANTMENT_TABLE_BOOK_TEXTURE);
        GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
        final float f3 = this.field_147076_A + (this.field_147080_z - this.field_147076_A) * partialTicks;
        GlStateManager.translate((1.0f - f3) * 0.2f, (1.0f - f3) * 0.1f, (1.0f - f3) * 0.25f);
        GlStateManager.rotate(-(1.0f - f3) * 90.0f - 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
        float f4 = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * partialTicks + 0.25f;
        float f5 = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * partialTicks + 0.75f;
        f4 = (f4 - MathHelper.truncateDoubleToInt(f4)) * 1.6f - 0.3f;
        f5 = (f5 - MathHelper.truncateDoubleToInt(f5)) * 1.6f - 0.3f;
        if (f4 < 0.0f) {
            f4 = 0.0f;
        }
        if (f5 < 0.0f) {
            f5 = 0.0f;
        }
        if (f4 > 1.0f) {
            f4 = 1.0f;
        }
        if (f5 > 1.0f) {
            f5 = 1.0f;
        }
        GlStateManager.enableRescaleNormal();
        GuiEnchantment.MODEL_BOOK.render(null, 0.0f, f4, f5, f3, 0.0f, 0.0625f);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.matrixMode(5889);
        GlStateManager.viewport(0, 0, Minecraft.displayWidth, Minecraft.displayHeight);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        EnchantmentNameParts.getInstance().reseedRandomGenerator(this.container.xpSeed);
        final int k = this.container.getLapisAmount();
        for (int l = 0; l < 3; ++l) {
            final int i2 = i + 60;
            final int j2 = i2 + 20;
            final int k2 = 86;
            final String s = EnchantmentNameParts.getInstance().generateNewRandomName();
            this.zLevel = 0.0f;
            this.mc.getTextureManager().bindTexture(GuiEnchantment.ENCHANTMENT_TABLE_GUI_TEXTURE);
            final int l2 = this.container.enchantLevels[l];
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            if (l2 == 0) {
                this.drawTexturedModalRect(i2, j + 14 + 19 * l, 0, 185, 108, 19);
            }
            else {
                final String s2 = new StringBuilder().append(l2).toString();
                FontRenderer fontrenderer = this.mc.standardGalacticFontRenderer;
                int i3 = 6839882;
                if ((k < l + 1 || Minecraft.thePlayer.experienceLevel < l2) && !Minecraft.thePlayer.capabilities.isCreativeMode) {
                    this.drawTexturedModalRect(i2, j + 14 + 19 * l, 0, 185, 108, 19);
                    this.drawTexturedModalRect(i2 + 1, j + 15 + 19 * l, 16 * l, 239, 16, 16);
                    fontrenderer.drawSplitString(s, j2, j + 16 + 19 * l, k2, (i3 & 0xFEFEFE) >> 1);
                    i3 = 4226832;
                }
                else {
                    final int j3 = mouseX - (i + 60);
                    final int k3 = mouseY - (j + 14 + 19 * l);
                    if (j3 >= 0 && k3 >= 0 && j3 < 108 && k3 < 19) {
                        this.drawTexturedModalRect(i2, j + 14 + 19 * l, 0, 204, 108, 19);
                        i3 = 16777088;
                    }
                    else {
                        this.drawTexturedModalRect(i2, j + 14 + 19 * l, 0, 166, 108, 19);
                    }
                    this.drawTexturedModalRect(i2 + 1, j + 15 + 19 * l, 16 * l, 223, 16, 16);
                    fontrenderer.drawSplitString(s, j2, j + 16 + 19 * l, k2, i3);
                    i3 = 8453920;
                }
                fontrenderer = this.mc.fontRendererObj;
                fontrenderer.drawStringWithShadow(s2, (float)(j2 + 86 - fontrenderer.getStringWidth(s2)), (float)(j + 16 + 19 * l + 7), i3);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        final boolean flag = Minecraft.thePlayer.capabilities.isCreativeMode;
        final int i = this.container.getLapisAmount();
        for (int j = 0; j < 3; ++j) {
            final int k = this.container.enchantLevels[j];
            final int l = this.container.field_178151_h[j];
            final int i2 = j + 1;
            if (this.isPointInRegion(60, 14 + 19 * j, 108, 17, mouseX, mouseY) && k > 0 && l >= 0) {
                final List<String> list = (List<String>)Lists.newArrayList();
                if (l >= 0 && Enchantment.getEnchantmentById(l & 0xFF) != null) {
                    final String s = Enchantment.getEnchantmentById(l & 0xFF).getTranslatedName((l & 0xFF00) >> 8);
                    list.add(String.valueOf(EnumChatFormatting.WHITE.toString()) + EnumChatFormatting.ITALIC.toString() + I18n.format("container.enchant.clue", s));
                }
                if (!flag) {
                    if (l >= 0) {
                        list.add("");
                    }
                    if (Minecraft.thePlayer.experienceLevel < k) {
                        list.add(String.valueOf(EnumChatFormatting.RED.toString()) + "Level Requirement: " + this.container.enchantLevels[j]);
                    }
                    else {
                        String s2 = "";
                        if (i2 == 1) {
                            s2 = I18n.format("container.enchant.lapis.one", new Object[0]);
                        }
                        else {
                            s2 = I18n.format("container.enchant.lapis.many", i2);
                        }
                        if (i >= i2) {
                            list.add(String.valueOf(EnumChatFormatting.GRAY.toString()) + s2);
                        }
                        else {
                            list.add(String.valueOf(EnumChatFormatting.RED.toString()) + s2);
                        }
                        if (i2 == 1) {
                            s2 = I18n.format("container.enchant.level.one", new Object[0]);
                        }
                        else {
                            s2 = I18n.format("container.enchant.level.many", i2);
                        }
                        list.add(String.valueOf(EnumChatFormatting.GRAY.toString()) + s2);
                    }
                }
                this.drawHoveringText(list, mouseX, mouseY);
                break;
            }
        }
    }
    
    public void func_147068_g() {
        final ItemStack itemstack = this.inventorySlots.getSlot(0).getStack();
        if (!ItemStack.areItemStacksEqual(itemstack, this.field_147077_B)) {
            this.field_147077_B = itemstack;
            do {
                this.field_147082_x += this.random.nextInt(4) - this.random.nextInt(4);
            } while (this.field_147071_v <= this.field_147082_x + 1.0f && this.field_147071_v >= this.field_147082_x - 1.0f);
        }
        ++this.field_147073_u;
        this.field_147069_w = this.field_147071_v;
        this.field_147076_A = this.field_147080_z;
        boolean flag = false;
        for (int i = 0; i < 3; ++i) {
            if (this.container.enchantLevels[i] != 0) {
                flag = true;
            }
        }
        if (flag) {
            this.field_147080_z += 0.2f;
        }
        else {
            this.field_147080_z -= 0.2f;
        }
        this.field_147080_z = MathHelper.clamp_float(this.field_147080_z, 0.0f, 1.0f);
        float f1 = (this.field_147082_x - this.field_147071_v) * 0.4f;
        final float f2 = 0.2f;
        f1 = MathHelper.clamp_float(f1, -f2, f2);
        this.field_147081_y += (f1 - this.field_147081_y) * 0.9f;
        this.field_147071_v += this.field_147081_y;
    }
}
