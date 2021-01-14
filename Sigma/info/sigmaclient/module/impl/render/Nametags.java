package info.sigmaclient.module.impl.render;

import static info.sigmaclient.util.MinecraftUtil.mc;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.EventSystem;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventNametagRender;
import info.sigmaclient.event.impl.EventRender3D;
import info.sigmaclient.event.impl.EventRenderGui;
import info.sigmaclient.management.friend.FriendManager;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.combat.Killaura;
import info.sigmaclient.module.impl.other.StreamerMode;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.RotationUtils;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import tv.twitch.chat.Chat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class Nametags extends Module {

    private boolean hideInvisibles;
    private double gradualFOVModifier;
    private Character formatChar = new Character('\247');
    public static Map<EntityLivingBase, double[]> entityPositions = new HashMap();
    public static String ARMOR = "ARMOR";
    public static String HEALTH = "HEALTH";
    public static String IMASPECIALCUNT = "SCALE";
    private final String INVISIBLES = "INVISIBLES";

    public Nametags(ModuleData data) {
        super(data);
        settings.put(ARMOR, new Setting<>(ARMOR, true, "Show armor when not hovering."));
        settings.put(HEALTH, new Setting<>(HEALTH, false, "Show health when not hovering."));
        settings.put(INVISIBLES, new Setting<>(INVISIBLES, false, "Show invisibles."));
    }

    @Override
    @RegisterEvent(events = {EventRender3D.class, EventRenderGui.class, EventNametagRender.class})
    public void onEvent(Event event) {
        if (event instanceof EventRender3D) {
            try {
                updatePositions();
            } catch (Exception e) {

            }
        }
        if (event instanceof EventRenderGui) {
            EventRenderGui er = (EventRenderGui) event;
            GlStateManager.pushMatrix();
            ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

            for (Entity ent : entityPositions.keySet()) {
                if (ent != mc.thePlayer && ((Boolean) settings.get(INVISIBLES).getValue()) || !ent.isInvisible()) {

                    GlStateManager.pushMatrix();
                    if ((ent instanceof EntityPlayer)) {
                        String str = ent.getDisplayName().getFormattedText();
                        String name = ent.getName();
                        str = str.replace(ent.getDisplayName().getFormattedText(), FriendManager.isFriend(ent.getName()) ? "\247b" + FriendManager.getAlias(ent.getName()) : "\247f" + ent.getDisplayName().getFormattedText());
                        if (((EntityPlayer) ent).isMurderer) {
                            str = str.replace(ent.getDisplayName().getFormattedText(), "\2475[M] " + ent.getName());
                        }
                        
                        double[] renderPositions = entityPositions.get(ent);
                        if ((renderPositions[3] < 0.0D) || (renderPositions[3] >= 1.0D)) {
                            GlStateManager.popMatrix();
                            continue;
                        } 
                        if (StreamerMode.scrambleNames) {
                        	String newstr = "";
                        	char[] rdm = {'l','i','j','\'',';',':', '|'};
                        	for(int i = 0; i < name.length(); i++){
                        		char ch = rdm[Killaura.randomNumber(rdm.length-1, 0)];                       
                        		newstr = newstr.concat(ch + "");
                        	}
                        	str = "§r" + newstr;    
                       }
                        TTFFontRenderer font = Client.fm.getFont("SFB 8");

                        GlStateManager.translate(renderPositions[0] / scaledRes.getScaleFactor(), renderPositions[1] / scaledRes.getScaleFactor(), 0.0D);
                        scale();
                        String healthInfo = (int) ((EntityLivingBase) ent).getHealth() + "";
                        GlStateManager.translate(0.0D, -2.5D, 0.0D);
                        float strWidth = font.getWidth(str);
                        float strWidth2 = font.getWidth(healthInfo);
                        RenderingUtil.rectangle(-strWidth / 2 - 1, -10.0D, strWidth / 2 + 1, 0, Colors.getColor(0, 130));
                        int x3 = ((int) (renderPositions[0] + -strWidth / 2 - 3) / 2) - 26;
                        int x4 = ((int) (renderPositions[0] + strWidth / 2 + 3) / 2) + 20;
                        int y1 = ((int) (renderPositions[1] + -30) / 2);
                        int y2 = ((int) (renderPositions[1] + 11) / 2);
                        int mouseY = (er.getResolution().getScaledHeight() / 2);
                        int mouseX = (er.getResolution().getScaledWidth() / 2);
                        font.drawStringWithShadow(str, -strWidth / 2, -7.0F,
                                Colors.getColor(255, 50, 50));
                        boolean healthOption = !((Boolean) settings.get(HEALTH).getValue());
                        boolean armor = !((Boolean) settings.get(ARMOR).getValue());
                        boolean hovered = x3 < mouseX && mouseX < x4 && y1 < mouseY && mouseY < y2;
                        if (!healthOption || hovered) {
                            float health = ((EntityPlayer) ent).getHealth();
                            float[] fractions = new float[]{0f, 0.5f, 1f};
                            Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
                            float progress = (health * 5) * 0.01f;
                            Color customColor = ESP.blendColors(fractions, colors, progress).brighter();
                            try {
                                RenderingUtil.rectangle(strWidth / 2 + 2, -10.0D, strWidth / 2 + 1 + strWidth2,
                                        0, Colors.getColor(0, 130));
                                font.drawStringWithShadow(healthInfo, strWidth / 2 + 2, (int) -7.0D,
                                        customColor.getRGB());
                            } catch (Exception e) {

                            }
                        }
                        if (hovered || !armor) {
                            List<ItemStack> itemsToRender = new ArrayList<>();
                            for (int i = 0; i < 5; i++) {
                                ItemStack stack = ((EntityPlayer) ent).getEquipmentInSlot(i);
                                if (stack != null) {
                                    itemsToRender.add(stack);
                                }
                            }
                            int x = -(itemsToRender.size() * 9);
                            for (ItemStack stack : itemsToRender) {
                                RenderHelper.enableGUIStandardItemLighting();
                                mc.getRenderItem().remderItemIntoGUI(stack, x, -27);
                                mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, x, -27);
                                x += 3;
                                RenderHelper.disableStandardItemLighting();
                                if (stack != null) {
                                    int y = 21;
                                    int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId,
                                            stack);
                                    int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId,
                                            stack);
                                    int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId,
                                            stack);
                                    if (sLevel > 0) {
                                        drawEnchantTag("Sh" + getColor(sLevel) + sLevel, x, y);
                                        y -= 9;
                                    }
                                    if (fLevel > 0) {
                                        drawEnchantTag("Fir" + getColor(fLevel) + fLevel, x, y);
                                        y -= 9;
                                    }
                                    if (kLevel > 0) {
                                        drawEnchantTag("Kb" + getColor(kLevel) + kLevel, x, y);
                                    } else if ((stack.getItem() instanceof ItemArmor)) {
                                        int pLevel = EnchantmentHelper
                                                .getEnchantmentLevel(Enchantment.protection.effectId, stack);
                                        int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId,
                                                stack);
                                        int uLevel = EnchantmentHelper
                                                .getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
                                        if (pLevel > 0) {
                                            drawEnchantTag("P" + getColor(pLevel) + pLevel, x, y);
                                            y -= 9;
                                        }
                                        if (tLevel > 0) {
                                            drawEnchantTag("Th" + getColor(tLevel) + tLevel, x, y);
                                            y -= 9;
                                        }
                                        if (uLevel > 0) {
                                            drawEnchantTag("Unb" + getColor(uLevel) + uLevel, x, y);
                                        }
                                    } else if ((stack.getItem() instanceof ItemBow)) {
                                        int powLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId,
                                                stack);
                                        int punLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId,
                                                stack);
                                        int fireLevel = EnchantmentHelper
                                                .getEnchantmentLevel(Enchantment.flame.effectId, stack);
                                        if (powLevel > 0) {
                                            drawEnchantTag("Pow" + getColor(powLevel) + powLevel, x, y);
                                            y -= 9;
                                        }
                                        if (punLevel > 0) {
                                            drawEnchantTag("Pun" + getColor(punLevel) + punLevel, x, y);
                                            y -= 9;
                                        }
                                        if (fireLevel > 0) {
                                            drawEnchantTag("Fir" + getColor(fireLevel) + fireLevel, x, y);
                                        }
                                    } else if (stack.getRarity() == EnumRarity.EPIC) {
                                        drawEnchantTag("\2476\247lGod", x, y);
                                    }
                                    int var7 = (int) Math.round(255.0D
                                            - (double) stack.getItemDamage() * 255.0D / (double) stack.getMaxDamage());
                                    int var10 = 255 - var7 << 16 | var7 << 8;
                                    Color customColor = new Color(var10).brighter();

                                    float x2 = (float) (x * 1.75D);
                                    if ((stack.getMaxDamage() - stack.getItemDamage()) > 0) {
                                        GlStateManager.pushMatrix();
                                        GlStateManager.disableDepth();
                                        GL11.glScalef(0.57F, 0.57F, 0.57F);
                                        font.drawStringWithShadow("" + (stack.getMaxDamage() - stack.getItemDamage()), x2, -54, customColor.getRGB());
                                        GlStateManager.enableDepth();
                                        GlStateManager.popMatrix();
                                    }
                                    y = -20 - 33;
                                    for (Object o : ((EntityPlayer) ent).getActivePotionEffects()) {
                                        PotionEffect pot = (PotionEffect) o;
                                        String potName = StringUtils.capitalize(pot.getEffectName().substring(pot.getEffectName().lastIndexOf(".") + 1));
                                        int XD = pot.getDuration() / 20;
                                        SimpleDateFormat df = new SimpleDateFormat("m:ss");
                                        String time = df.format(XD * 1000);
                                        font.drawStringWithShadow((XD > 0 ? potName + " " + time : ""), -30, y, -1);
                                        y -= 8;
                                    }
                                    x += 12;
                                }
                            }
                        }
                    }
                    GlStateManager.popMatrix();
                }
            }
            GlStateManager.popMatrix();
        }
        Event enr = EventSystem.getInstance(EventNametagRender.class);
        enr.setCancelled(true);
    }

    private String getColor(int level) {
        if (level == 1) {

        } else if (level == 2) {
            return "\247a";
        } else if (level == 3) {
            return "\2473";
        } else if (level == 4) {
            return "\2474";
        } else if (level >= 5) {
            return "\2476";
        }
        return "\247f";
    }

    private static void drawEnchantTag(String text, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        x = (int) (x * 1.75D);
        y -= 4;
        GL11.glScalef(0.57F, 0.57F, 0.57F);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, -30 - y, Colors.getColor(255));
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    private void scale() {
        float scale = 1;
        scale *= ((mc.currentScreen == null) && (GameSettings.isKeyDown(mc.gameSettings.ofKeyBindZoom)) ? 2 : 1);
        GlStateManager.scale(scale, scale, scale);
    }

    private void updatePositions() {
        entityPositions.clear();
        float pTicks = mc.timer.renderPartialTicks;
        for (Object o : mc.theWorld.loadedEntityList) {
            Entity ent = (Entity) o;
            if ((ent != mc.thePlayer) && ((ent instanceof EntityPlayer))
                    && ((!ent.isInvisible()) || (!this.hideInvisibles))) {
                double x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks - mc.getRenderManager().viewerPosX;
                double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - mc.getRenderManager().viewerPosY;
                double z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks - mc.getRenderManager().viewerPosZ;
                y += ent.height + 0.2D;
                if ((convertTo2D(x, y, z)[2] >= 0.0D) && (convertTo2D(x, y, z)[2] < 1.0D)) {
                    entityPositions.put((EntityPlayer) ent,
                            new double[]{convertTo2D(x, y, z)[0], convertTo2D(x, y, z)[1],
                                    Math.abs(convertTo2D(x, y + 1.0D, z, ent)[1] - convertTo2D(x, y, z, ent)[1]),
                                    convertTo2D(x, y, z)[2]});
                }
            }
        }
    }

    private double[] convertTo2D(double x, double y, double z, Entity ent) {
        float pTicks = mc.timer.renderPartialTicks;
        float prevYaw = mc.thePlayer.rotationYaw;
        float prevPrevYaw = mc.thePlayer.prevRotationYaw;
        float[] rotations = RotationUtils.getRotationFromPosition(
                ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks,
                ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks,
                ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - 1.6D);
        mc.getRenderViewEntity().rotationYaw = (mc.getRenderViewEntity().prevRotationYaw = rotations[0]);
        mc.entityRenderer.setupCameraTransform(pTicks, 0);
        double[] convertedPoints = convertTo2D(x, y, z);
        mc.getRenderViewEntity().rotationYaw = prevYaw;
        mc.getRenderViewEntity().prevRotationYaw = prevPrevYaw;
        mc.entityRenderer.setupCameraTransform(pTicks, 0);
        return convertedPoints;
    }

    private double[] convertTo2D(double x, double y, double z) {
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        boolean result = GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCoords);
        if (result) {
            return new double[]{screenCoords.get(0), Display.getHeight() - screenCoords.get(1), screenCoords.get(2)};
        }
        return null;
    }

}
