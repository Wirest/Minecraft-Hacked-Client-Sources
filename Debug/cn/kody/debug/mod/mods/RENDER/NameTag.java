package cn.kody.debug.mod.mods.RENDER;

import java.util.HashMap;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import net.minecraft.util.MathHelper;
import java.util.Iterator;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import com.darkmagician6.eventapi.EventTarget;

import cn.kody.debug.Client;
import cn.kody.debug.events.EventRender;
import cn.kody.debug.events.EventRender2D;
import cn.kody.debug.events.EventUpdate;
import cn.kody.debug.friend.FriendsManager;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.mod.mods.COMBAT.AntiBot;
import cn.kody.debug.mod.mods.PLAYER.Teams;
import cn.kody.debug.ui.Notification;
import cn.kody.debug.ui.font.UnicodeFontRenderer;
import cn.kody.debug.utils.angle.RotationUtil;
import cn.kody.debug.utils.color.Colors;
import cn.kody.debug.utils.render.GLUtils;
import cn.kody.debug.utils.render.RenderUtil;
import cn.kody.debug.value.Value;

import java.text.DecimalFormat;
import java.util.Map;
/**
 * 
 * @author Sigma
 *
 */
public class NameTag extends Mod
{
    public static Map<EntityLivingBase, double[]> entityPositions = new HashMap<EntityLivingBase, double[]>();;
    public Value<Boolean> invis = new Value<Boolean>("NameTag_Invisible", false);
    public Value<Boolean> armor = new Value<Boolean>("NameTag_Armor", false);
    public DecimalFormat format = new DecimalFormat("0.0");
    public float animationX;
    
    public NameTag() {
        super("NameTag", Category.RENDER);
    }
    
    @EventTarget
    public void update(EventRender class1170) {
        try {
            this.updatePositions();
        }
        catch (Exception ex) {
            
        }
    }
    
    @EventTarget
    public void onRender2D(EventRender2D class112) {
        GlStateManager.pushMatrix();
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        for (Entity entity : entityPositions.keySet()) {
            if (entity != this.mc.thePlayer && (this.invis.getValueState() || !entity.isInvisible())) {
                GlStateManager.pushMatrix();
                if (entity instanceof EntityPlayer) {
                    double[] array = entityPositions.get(entity);
                    if (array[3] < 0.0D || array[3] >= 1.0D) {
                        GlStateManager.popMatrix();
                        continue;
                    }
                    
                        UnicodeFontRenderer wqy16 = Client.instance.fontMgr.wqy16;
                        GlStateManager.translate(array[0] / scaledResolution.getScaleFactor(), array[1] / scaledResolution.getScaleFactor(), 0.0D);
                        this.scale();
                        GlStateManager.translate(0.0D, -2.5D, 0.0D);
                        String s;
                        if (AntiBot.isBot(entity)) {
                            s = "§9[BOT]";
                        }else {
                            s = "";
                        }
                        String s2;
                        if (FriendsManager.isFriend((EntityPlayer)entity)) {
                            s2 = "§6[Friend]";
                        }
                        else {
                            s2 = "";
                        }
                        String s3 = "";
//                        if (Teams.isOnSameTeam2(entity)) {
//                            s3 = "[TEAM]";
//                        }else {
//                            s3 = "";
//                        }
                        String s4;
                        if (Teams.isClientFriend((EntityPlayer) entity)) {
                            s4 = "§e[ClientFriend]";
                        }
                        else {
                            s4 = "";
                        }
                        if ((s3 + s).equals("")) {
                            s3 = "§a";
                        }
                        String string = "Health: " + this.format.format(((EntityLivingBase) entity).getHealth());
                        String string2 = s2 + s3 + s + s4 + entity.getDisplayName().getUnformattedText();
                        float n = (float)wqy16.getStringWidth(string2);
                        float n2 = (float)Client.instance.fontMgr.tahoma13.getStringWidth(string);
                        float n3;
                        if (n > n2) {
                            n3 = n;
                        }
                        else {
                            n3 = n2;
                        }
                        float n4 = n3 + 8.0f;
                        RenderUtil.drawRect(-n4 / 2.0f, -25.0f, n4 / 2.0f, 0.0f, Colors.getColor(0, 100));
                        int n5 = (int)(array[0] + -n4 / 2.0f - 3.0) / 2 - 26;
                        int n6 = (int)(array[0] + n4 / 2.0f + 3.0) / 2 + 20;
                        int n7 = (int)(array[1] - 30.0) / 2;
                        int n8 = (int)(array[1] + 11.0) / 2;
                        int n9 = scaledResolution.getScaledHeight() / 2;
                        int n10 = scaledResolution.getScaledWidth() / 2;
                        wqy16.drawStringWithColor(string2, -n4 / 2.0f + 4.0f, -22.0f, Colors.WHITE.c, 255);
                        Client.instance.fontMgr.tahoma13.drawString(string, -n4 / 2.0f + 4.0f, -10.0f, Colors.WHITE.c);
                        EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
                        float n11 = (float)Math.ceil(entityLivingBase.getHealth() + entityLivingBase.getAbsorptionAmount()) / (entityLivingBase.getMaxHealth() + entityLivingBase.getAbsorptionAmount());
                        int n12 = Colors.RED.c;
                        String formattedText = entity.getDisplayName().getFormattedText();
                        int i = 0;
                        while (i < formattedText.length()) {
                            if (formattedText.charAt(i) == '§' && i + 1 < formattedText.length()) {
                                int index = "0123456789abcdefklmnorg".indexOf(Character.toLowerCase(formattedText.charAt(i + 1)));
                                if (index < 16) {
                                    try {
                                        Color color = new Color(this.mc.fontRendererObj.colorCode[index]);
                                        n12 = this.getColor(color.getRed(), color.getGreen(), color.getBlue(), 255);
                                    }catch (ArrayIndexOutOfBoundsException ex) {
                                    }
                                }
                            }
                            ++i;
                        }
                        RenderUtil.drawRect(-n4 / 2.0f, -2.0f, n4 / 2.0f - n4 / 2.0f * (1.0f - n11) * 2.0f, 0.0f, Notification.reAlpha(n12, 0.8f));
                        if (this.armor.getValueState()) {
                            ArrayList<ItemStack> list = new ArrayList<ItemStack>();
                            int j = 0;
                            while (j < 5) {
                                ItemStack equipmentInSlot = ((EntityLivingBase)entity).getEquipmentInSlot(j);
                                if (equipmentInSlot != null) {
                                    list.add(equipmentInSlot);
                                }
                                ++j;
                            }
                            int p_renderItemOverlays_3_ = -(list.size() * 9);
                            for (ItemStack p_getEnchantmentLevel_1_ : list) {
                                GLUtils.enableGUIStandardItemLighting();
                                this.mc.getRenderItem().zLevel = -150.0f;
                                this.fixGlintShit();
                                this.mc.getRenderItem().renderItemIntoGUI(p_getEnchantmentLevel_1_, (int)(p_renderItemOverlays_3_ + 6), (int)(-42.0f));
                                this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, p_getEnchantmentLevel_1_, p_renderItemOverlays_3_, -42);
                                this.mc.getRenderItem().zLevel = 0.0f;
                                p_renderItemOverlays_3_ += 3;
                                GLUtils.disableStandardItemLighting();
                                if (p_getEnchantmentLevel_1_ != null) {
                                    int n13 = 21;
                                    int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, p_getEnchantmentLevel_1_);
                                    int enchantmentLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, p_getEnchantmentLevel_1_);
                                    int enchantmentLevel3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, p_getEnchantmentLevel_1_);
                                    if (enchantmentLevel > 0) {
                                        this.drawEnchantTag("Sh" + this.getColor(enchantmentLevel) + enchantmentLevel, p_renderItemOverlays_3_, n13);
                                        n13 += 6;
                                    }
                                    if (enchantmentLevel2 > 0) {
                                        this.drawEnchantTag("Fir" + this.getColor(enchantmentLevel2) + enchantmentLevel2, p_renderItemOverlays_3_, n13);
                                        n13 += 6;
                                    }
                                    if (enchantmentLevel3 > 0) {
                                        this.drawEnchantTag("Kb" + this.getColor(enchantmentLevel3) + enchantmentLevel3, p_renderItemOverlays_3_, n13);
                                    }
                                    else if (p_getEnchantmentLevel_1_.getItem() instanceof ItemArmor) {
                                        int enchantmentLevel4 = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, p_getEnchantmentLevel_1_);
                                        int enchantmentLevel5 = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, p_getEnchantmentLevel_1_);
                                        int enchantmentLevel6 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, p_getEnchantmentLevel_1_);
                                        if (enchantmentLevel4 > 0) {
                                            this.drawEnchantTag("P" + this.getColor(enchantmentLevel4) + enchantmentLevel4, p_renderItemOverlays_3_, n13);
                                            n13 += 6;
                                        }
                                        if (enchantmentLevel5 > 0) {
                                            this.drawEnchantTag("Th" + this.getColor(enchantmentLevel5) + enchantmentLevel5, p_renderItemOverlays_3_, n13);
                                            n13 += 6;
                                        }
                                        if (enchantmentLevel6 > 0) {
                                            this.drawEnchantTag("Unb" + this.getColor(enchantmentLevel6) + enchantmentLevel6, p_renderItemOverlays_3_, n13);
                                        }
                                    }
                                    else if (p_getEnchantmentLevel_1_.getItem() instanceof ItemBow) {
                                        int enchantmentLevel7 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, p_getEnchantmentLevel_1_);
                                        int enchantmentLevel8 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, p_getEnchantmentLevel_1_);
                                        int enchantmentLevel9 = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, p_getEnchantmentLevel_1_);
                                        if (enchantmentLevel7 > 0) {
                                            this.drawEnchantTag("Pow" + this.getColor(enchantmentLevel7) + enchantmentLevel7, p_renderItemOverlays_3_, n13);
                                            n13 += 6;
                                        }
                                        if (enchantmentLevel8 > 0) {
                                            this.drawEnchantTag("Pun" + this.getColor(enchantmentLevel8) + enchantmentLevel8, p_renderItemOverlays_3_, n13);
                                            n13 += 6;
                                        }
                                        if (enchantmentLevel9 > 0) {
                                            this.drawEnchantTag("Fir" + this.getColor(enchantmentLevel9) + enchantmentLevel9, p_renderItemOverlays_3_, n13);
                                        }
                                    }
                                    else if (p_getEnchantmentLevel_1_.getRarity() == EnumRarity.EPIC) {
                                        this.drawEnchantTag("§6§lGod", p_renderItemOverlays_3_ - 2, n13);
                                    }
                                    float n14 = (float)(p_renderItemOverlays_3_ * 1.05) - 2.0f;
                                
                                    if (p_getEnchantmentLevel_1_.getMaxDamage() - p_getEnchantmentLevel_1_.getItemDamage() > 0) {
                                        GlStateManager.pushMatrix();
                                        GlStateManager.disableDepth();
                                        Client.instance.fontMgr.tahoma12.drawString("" + (p_getEnchantmentLevel_1_.getMaxDamage() - p_getEnchantmentLevel_1_.getItemDamage()), n14 + 6.0f, -32.0f, Colors.WHITE.c);
                                        GlStateManager.enableDepth();
                                        GlStateManager.popMatrix();
                                    }
                                    p_renderItemOverlays_3_ += 12;
                                }
                            }
                        }
                }
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.popMatrix();
    }
    
    private String getColor(int n) {
        if (n == 1) {
        }
        else {
            if (n == 2) {
                return "§a";
            }
            if (n == 3) {
                return "§3";
            }
            if (n == 4) {
                return "§4";
            }
            if (n >= 5) {
                return "§6";
            }
        }
        return "§f";
    }
    
    private void drawEnchantTag(String s, int n, int n2) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        n *= (int)1.05;
        n2 -= 6;
        Client.instance.fontMgr.tahoma10.drawStringWithColor(s, (float)(n + 9), (float)(-30 - n2), Colors.getColor(255), 255);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
    
    public int getColor(int p_clamp_int_0_, int p_clamp_int_0_2, int p_clamp_int_0_3, int p_clamp_int_0_4) {
        return MathHelper.clamp_int(p_clamp_int_0_4, 0, 255) << 24 | MathHelper.clamp_int(p_clamp_int_0_, 0, 255) << 16 | MathHelper.clamp_int(p_clamp_int_0_2, 0, 255) << 8 | MathHelper.clamp_int(p_clamp_int_0_3, 0, 255);
    }
    
    private void scale() {
        final float n = 1.0f;
        GlStateManager.scale(n, n, n);
    }
    
    
    
    private void fixGlintShit() {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }
    
    private void updatePositions() {
        entityPositions.clear();
        float pTicks = mc.timer.renderPartialTicks;
        for (Object o : mc.theWorld.loadedEntityList) {
            Entity ent = (Entity) o;
            if ((ent != mc.thePlayer) && ((ent instanceof EntityPlayer))
                    && ((!ent.isInvisible()) || (!this.invis.getValueState().booleanValue()))) {
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
	        float[] rotations = RotationUtil.getRotationFromPosition(
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
