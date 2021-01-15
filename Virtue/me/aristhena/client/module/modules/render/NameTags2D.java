// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.render;

import me.aristhena.event.events.NametagRenderEvent;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.BufferUtils;
import me.aristhena.utils.RotationUtils;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import java.util.List;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemArmor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import me.aristhena.utils.RenderUtils;
import me.aristhena.utils.MathUtils;
import me.aristhena.client.friend.FriendManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.ScaledResolution;
import me.aristhena.utils.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import me.aristhena.event.events.Render2DEvent;
import me.aristhena.event.EventTarget;
import me.aristhena.event.events.Render3DEvent;
import java.util.HashMap;
import net.minecraft.entity.EntityLivingBase;
import java.util.Map;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(displayName = "2D NameTags", shown = false)
public class NameTags2D extends Module
{
    @Option.Op(min = 1.0, max = 20.0, increment = 1.0)
    private double distance;
    @Option.Op(min = 0.0, max = 2.0, increment = 0.1)
    private double scale;
    @Option.Op
    private boolean armor;
    @Option.Op
    private boolean hideInvisibles;
    private double gradualFOVModifier;
    private Character formatChar;
    public static Map<EntityLivingBase, double[]> entityPositions;
    
    static {
        NameTags2D.entityPositions = new HashMap<EntityLivingBase, double[]>();
    }
    
    public NameTags2D() {
        this.distance = 8.0;
        this.scale = 1.0;
        this.armor = true;
        this.formatChar = new Character('§');
    }
    
    @EventTarget
    private void onRender3DEvent(final Render3DEvent event) {
        this.updatePositions();
    }
    
    @EventTarget
    private void onRender2DEvent(final Render2DEvent event) {
        GlStateManager.pushMatrix();
        final ScaledResolution scaledRes = new ScaledResolution(ClientUtils.mc(), ClientUtils.mc().displayWidth, ClientUtils.mc().displayHeight);
        final double twoDscale = scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 2.0);
        GlStateManager.scale(twoDscale, twoDscale, twoDscale);
        for (final Entity ent : NameTags2D.entityPositions.keySet()) {
            GlStateManager.pushMatrix();
            if (ent == ClientUtils.player()) {
                continue;
            }
            if (ent instanceof EntityPlayer) {
                String str = ent.getDisplayName().getFormattedText();
                if (FriendManager.isFriend(ent.getName())) {
                    str = str.replace(ent.getDisplayName().getFormattedText(), "§b" + FriendManager.getAliasName(ent.getName()));
                }
                String colorString = this.formatChar.toString();
                final double health = MathUtils.roundToPlace(((EntityPlayer)ent).getHealth() / 2.0f, 2);
                if (health >= 6.0) {
                    colorString = String.valueOf(colorString) + "2";
                }
                else if (health >= 3.5) {
                    colorString = String.valueOf(colorString) + "6";
                }
                else {
                    colorString = String.valueOf(colorString) + "4";
                }
                str = String.valueOf(str) + " " + colorString + health + " \u2764";
                final double[] renderPositions = NameTags2D.entityPositions.get(ent);
                if (renderPositions[3] < 0.0 || renderPositions[3] >= 1.0) {
                    GlStateManager.popMatrix();
                    continue;
                }
                GlStateManager.translate(renderPositions[0], renderPositions[1], 0.0);
                this.scale(ent);
                GlStateManager.translate(0.0, -2.5, 0.0);
                final int strWidth = ClientUtils.clientFont().getStringWidth(str);
                RenderUtils.rectangle(-strWidth / 2 - 3, -12.0, strWidth / 2 + 3, 1.0, -1191182336);
                GlStateManager.color(0.0f, 0.0f, 0.0f);
                ClientUtils.clientFont().drawString(str, -strWidth / 2, -9.0, -1);
                GlStateManager.color(1.0f, 1.0f, 1.0f);
                if (this.armor) {
                    final List<ItemStack> itemsToRender = new ArrayList<ItemStack>();
                    for (int i = 0; i < 5; ++i) {
                        final ItemStack stack = ((EntityPlayer)ent).getEquipmentInSlot(i);
                        if (stack != null) {
                            itemsToRender.add(stack);
                        }
                    }
                    int x = -(itemsToRender.size() * 9);
                    final Iterator<ItemStack> iterator2 = itemsToRender.iterator();
                    while (iterator2.hasNext()) {
                        final ItemStack stack = iterator2.next();
                        RenderHelper.enableGUIStandardItemLighting();
                        ClientUtils.mc().getRenderItem().renderItemIntoGUI(stack, x, -30);
                        ClientUtils.mc().getRenderItem().renderItemOverlays(ClientUtils.mc().fontRendererObj, stack, x, -30);
                        x += 2;
                        RenderHelper.disableStandardItemLighting();
                        final String text = "";
                        if (stack != null) {
                            int y = 21;
                            final int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
                            final int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
                            final int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
                            if (sLevel > 0) {
                                drawEnchantTag("Sh" + sLevel, x, y);
                                y -= 9;
                            }
                            if (fLevel > 0) {
                                drawEnchantTag("Fir" + fLevel, x, y);
                                y -= 9;
                            }
                            if (kLevel > 0) {
                                drawEnchantTag("Kb" + kLevel, x, y);
                            }
                            else if (stack.getItem() instanceof ItemArmor) {
                                final int pLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
                                final int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
                                final int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
                                if (pLevel > 0) {
                                    drawEnchantTag("P" + pLevel, x, y);
                                    y -= 9;
                                }
                                if (tLevel > 0) {
                                    drawEnchantTag("Th" + tLevel, x, y);
                                    y -= 9;
                                }
                                if (uLevel > 0) {
                                    drawEnchantTag("Unb" + uLevel, x, y);
                                }
                            }
                            else if (stack.getItem() instanceof ItemBow) {
                                final int powLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
                                final int punLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
                                final int fireLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
                                if (powLevel > 0) {
                                    drawEnchantTag("Pow" + powLevel, x, y);
                                    y -= 9;
                                }
                                if (punLevel > 0) {
                                    drawEnchantTag("Pun" + punLevel, x, y);
                                    y -= 9;
                                }
                                if (fireLevel > 0) {
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
            }
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
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
    
    private void scale(final Entity ent) {
        float scale = (float)this.scale;
        final float target = scale * (ClientUtils.gamesettings().fovSetting / (ClientUtils.gamesettings().fovSetting * ClientUtils.player().func_175156_o()));
        if (this.gradualFOVModifier == 0.0 || Double.isNaN(this.gradualFOVModifier)) {
            this.gradualFOVModifier = target;
        }
        final double gradualFOVModifier = this.gradualFOVModifier;
        final double n = target - this.gradualFOVModifier;
        ClientUtils.mc();
        this.gradualFOVModifier = gradualFOVModifier + n / (Minecraft.debugFPS * 0.7);
        scale *= (float)this.gradualFOVModifier;
        scale *= ((ClientUtils.mc().currentScreen == null && GameSettings.isKeyDown(ClientUtils.gamesettings().ofKeyBindZoom)) ? 3 : 1);
        GlStateManager.scale(scale, scale, scale);
    }
    
    private void updatePositions() {
        this.entityPositions.clear();
        final float pTicks = ClientUtils.mc().timer.renderPartialTicks;
        for (final Object o : ClientUtils.world().loadedEntityList) {
            Entity ent = (Entity)o;
            if (ent != ClientUtils.player() && ent instanceof EntityPlayer) {
                if (ent.isInvisible() && this.hideInvisibles) {
                    continue;
                }
                final double x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks - ClientUtils.mc().getRenderManager().viewerPosX;
                double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - ClientUtils.mc().getRenderManager().viewerPosY;
                final double z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks - ClientUtils.mc().getRenderManager().viewerPosZ;
                y += ent.height + 0.2;
                if (this.convertTo2D(x, y, z)[2] < 0.0) {
                    continue;
                }
                if (this.convertTo2D(x, y, z)[2] >= 1.0) {
                    continue;
                }
                this.entityPositions.put((EntityPlayer)ent, new double[] { this.convertTo2D(x, y, z)[0], this.convertTo2D(x, y, z)[1], Math.abs(this.convertTo2D(x, y + 1.0, z, ent)[1] - this.convertTo2D(x, y, z, ent)[1]), this.convertTo2D(x, y, z)[2] });
            }
        }
    }
    
    private double[] convertTo2D(final double x, final double y, final double z, final Entity ent) {
        final float pTicks = ClientUtils.mc().timer.renderPartialTicks;
        final float prevYaw = ClientUtils.player().rotationYaw;
        final float prevPrevYaw = ClientUtils.player().prevRotationYaw;
        final float[] rotations = RotationUtils.getRotationFromPosition(ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks, ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks, ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - 1.6);
        final Entity renderViewEntity = ClientUtils.mc().getRenderViewEntity();
        final Entity renderViewEntity2 = ClientUtils.mc().getRenderViewEntity();
        final float n = rotations[0];
        renderViewEntity2.prevRotationYaw = n;
        renderViewEntity.rotationYaw = n;
        ClientUtils.mc().entityRenderer.setupCameraTransform(pTicks, 0);
        final double[] convertedPoints = this.convertTo2D(x, y, z);
        ClientUtils.mc().getRenderViewEntity().rotationYaw = prevYaw;
        ClientUtils.mc().getRenderViewEntity().prevRotationYaw = prevPrevYaw;
        ClientUtils.mc().entityRenderer.setupCameraTransform(pTicks, 0);
        return convertedPoints;
    }
    
    private double[] convertTo2D(final double x, final double y, final double z) {
        final FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
        final IntBuffer viewport = BufferUtils.createIntBuffer(16);
        final FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        final FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        final boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCoords);
        if (result) {
            return new double[] { screenCoords.get(0), Display.getHeight() - screenCoords.get(1), screenCoords.get(2) };
        }
        return null;
    }
    
    
    @EventTarget
    private void onNametagRender(final NametagRenderEvent event) {
        event.setCancelled(true);
    }
}
