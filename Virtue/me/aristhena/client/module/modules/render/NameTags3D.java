// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.render;

import me.aristhena.event.events.NametagRenderEvent;
import me.aristhena.event.EventTarget;
import java.util.List;
import net.minecraft.client.renderer.WorldRenderer;
import java.util.Iterator;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemArmor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import me.aristhena.utils.MathUtils;
import me.aristhena.client.friend.FriendManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import me.aristhena.utils.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import me.aristhena.event.events.Render3DEvent;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(displayName = "3D NameTags", shown = false)
public class NameTags3D extends Module
{
    @Option.Op(min = 0.01, max = 0.2, increment = 0.01)
    private double scale;
    @Option.Op
    private boolean armor;
    
    public NameTags3D() {
        this.scale = 0.1;
        this.armor = true;
    }
    
    @EventTarget(3)
    private void onRender3DEvent(Render3DEvent event)
    {
      for (Object o : ClientUtils.mc().theWorld.loadedEntityList)
      {
        Entity ent = (Entity)o;
        if (ent != ClientUtils.mc().thePlayer)
        {
          if ((ent instanceof EntityPlayer))
          {
            if (ent instanceof EntityPlayer) {
                final double posX = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * event.getPartialTicks() - RenderManager.renderPosX;
                final double posY = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * event.getPartialTicks() - RenderManager.renderPosY + ent.height + 0.5;
                final double posZ = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * event.getPartialTicks() - RenderManager.renderPosZ;
                String str = ent.getDisplayName().getFormattedText();
                if (FriendManager.isFriend(ent.getName())) {
                    str = str.replace(ent.getDisplayName().getFormattedText(), "§b" + FriendManager.getAliasName(ent.getName()));
                }
                String colorString = "§";
                final double health = MathUtils.roundToPlace(((EntityPlayer)ent).getHealth(), 2);
                if (health >= 12.0) {
                    colorString = String.valueOf(colorString) + "2";
                }
                else if (health >= 4.0) {
                    colorString = String.valueOf(colorString) + "6";
                }
                else {
                    colorString = String.valueOf(colorString) + "4";
                }
                str = String.valueOf(str) + " | " + colorString + health;
                final float dist = ClientUtils.mc().thePlayer.getDistanceToEntity(ent);
                float scale = 0.02672f;
                final float factor = (float)((dist <= 8.0f) ? (8.0 * this.scale) : (dist * this.scale));
                scale *= factor;
                GlStateManager.pushMatrix();
                GlStateManager.disableDepth();
                GlStateManager.translate(posX, posY, posZ);
                GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                final RenderManager renderManager = ClientUtils.mc().renderManager;
                GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                GL11.glRotatef(ClientUtils.mc().renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
                GlStateManager.scale(-scale, -scale, scale);
                GlStateManager.disableLighting();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                final Tessellator tessellator = Tessellator.getInstance();
                final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                GlStateManager.disableTexture2D();
                worldRenderer.startDrawingQuads();
                final int stringWidth = ClientUtils.clientFont().getStringWidth(str) / 2;
                GL11.glColor3f(0.0f, 0.0f, 0.0f);
                GL11.glLineWidth(1.0E-6f);
                GL11.glBegin(3);
                GL11.glVertex2d((double)(-stringWidth - 2), -0.8);
                GL11.glVertex2d((double)(-stringWidth - 2), 8.8);
                GL11.glVertex2d((double)(-stringWidth - 2), 8.8);
                GL11.glVertex2d((double)(stringWidth + 2), 8.8);
                GL11.glVertex2d((double)(stringWidth + 2), 8.8);
                GL11.glVertex2d((double)(stringWidth + 2), -0.8);
                GL11.glVertex2d((double)(stringWidth + 2), -0.8);
                GL11.glVertex2d((double)(-stringWidth - 2), -0.8);
                GL11.glEnd();
                worldRenderer.setColorRGBA_I(0, 100);
                worldRenderer.addVertex(-stringWidth - 2, -0.8, 0.0);
                worldRenderer.addVertex(-stringWidth - 2, 8.8, 0.0);
                worldRenderer.addVertex(stringWidth + 2, 8.8, 0.0);
                worldRenderer.addVertex(stringWidth + 2, -0.8, 0.0);
                tessellator.draw();
                GlStateManager.enableTexture2D();
                ClientUtils.clientFont().drawString(str, -ClientUtils.clientFont().getStringWidth(str) / 2, 0.0, -1);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                if (this.armor && ent instanceof EntityPlayer) {
                    final List<ItemStack> itemsToRender = new ArrayList<ItemStack>();
                    final ItemStack hand = ((EntityPlayer)ent).getEquipmentInSlot(0);
                    if (hand != null) {
                        itemsToRender.add(hand);
                    }
                    for (int i = 4; i > 0; --i) {
                        final ItemStack stack = ((EntityPlayer)ent).getEquipmentInSlot(i);
                        if (stack != null) {
                            itemsToRender.add(stack);
                        }
                    }
                    int x = -(itemsToRender.size() * 8);
                    final Iterator<ItemStack> iterator2 = itemsToRender.iterator();
                    while (iterator2.hasNext()) {
                        final ItemStack stack = iterator2.next();
                        GlStateManager.disableDepth();
                        RenderHelper.enableGUIStandardItemLighting();
                        ClientUtils.mc().getRenderItem().zLevel = -100.0f;
                        ClientUtils.mc().getRenderItem().renderItemIntoGUI(stack, x, -18, false);
                        ClientUtils.mc().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, stack, x, -18);
                        ClientUtils.mc().getRenderItem().zLevel = 0.0f;
                        RenderHelper.disableStandardItemLighting();
                        GlStateManager.enableDepth();
                        final String text = "";
                        if (stack != null) {
                            int y = 0;
                            final int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
                            final int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
                            final int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
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
                                final int pLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
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
                                drawEnchantTag("§lGod", x, y);
                            }
                            x += 16;
                        }
                    }
                }
                GlStateManager.disableLighting();
                GlStateManager.popMatrix();
            }
            GlStateManager.disableBlend();
        }
        GlStateManager.popMatrix();
    }
   }
  }
    private static void drawEnchantTag(final String text, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        x *= (int)1.75;
        y -= 4;
        GL11.glScalef(0.57f, 0.57f, 0.57f);
        ClientUtils.clientFont().drawStringWithShadow(text, x, -36 - y, -1286);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
    
    @EventTarget
    private void onNametagRender(final NametagRenderEvent event) {
        event.setCancelled(true);
    }
}
