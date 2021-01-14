package cn.kody.debug.mod.mods.RENDER;

import cn.kody.debug.events.EventRender;
import cn.kody.debug.events.EventRender2D;
import cn.kody.debug.events.EventUpdate;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.utils.render.GLUProjection;
import cn.kody.debug.utils.render.RenderUtil;
import cn.kody.debug.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class ESP
extends Mod {
    private static Map<EntityPlayer, float[][]> entities = new HashMap<EntityPlayer, float[][]>();
    public Value<Boolean> skeletal = new Value<Boolean>("ESP_Skeletal", true);
    public Value<Boolean> health = new Value<Boolean>("ESP_HealthBar", true);
    public Value<Boolean> armor = new Value<Boolean>("ESP_ArmorBar", true);
    public Value<Boolean> players = new Value<Boolean>("ESP_Players", true);
    public Value<Boolean> mobs = new Value<Boolean>("ESP_Mobs", false);
    public Value<Boolean> animals = new Value<Boolean>("ESP_Animals", false);
    public Value<Boolean> invisibles = new Value<Boolean>("ESP_Invisibles", false);
    public Value<String> mode = new Value("ESP", "Mode", 0);

    public ESP() {
        super("ESP", "ESP", Category.RENDER);
        this.mode.addValue("Box");
        this.mode.addValue("2D");
        this.mode.addValue("None");
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (this.mode.isCurrentMode("Box")) {
            this.setDisplayName("Box");
        } else if (this.mode.isCurrentMode("2D")) {
            this.setDisplayName("2D");
        } else if (this.mode.isCurrentMode("None")) {
            this.setDisplayName("None");
        }
    }

    @EventTarget
    public void onRender2(EventRender event) {
        if (this.mode.isCurrentMode("Box")) {
            for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
                if (!this.isValid((EntityLivingBase)o)) continue;
                Entity ent = (Entity)o;
                this.renderBox(ent, 1.0, 1.0, 1.0);
            }
        }
    }

    public void renderBox(Entity entity, double r, double g, double b) {
        double x = RenderUtil.interpolate(entity.posX, entity.lastTickPosX);
        double y = RenderUtil.interpolate(entity.posY, entity.lastTickPosY);
        double z = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ);
        GL11.glPushMatrix();
        RenderUtil.pre();
        GL11.glLineWidth((float)1.0f);
        GL11.glEnable((int)2848);
        GL11.glColor3d((double)r, (double)g, (double)b);
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        double var10002 = entity.boundingBox.minX - 0.05 - entity.posX;
        double var10003 = entity.posX;
        this.mc.getRenderManager();
        var10003 = entity.boundingBox.minY - entity.posY;
        double var10004 = entity.posY;
        this.mc.getRenderManager();
        var10004 = entity.boundingBox.minZ - 0.05 - entity.posZ;
        double var10005 = entity.posZ;
        this.mc.getRenderManager();
        var10005 = entity.boundingBox.maxX + 0.05 - entity.posX;
        double var10006 = entity.posX;
        this.mc.getRenderManager();
        var10006 = entity.boundingBox.maxY + 0.1 - entity.posY;
        double var10007 = entity.posY;
        this.mc.getRenderManager();
        var10007 = entity.boundingBox.maxZ + 0.05 - entity.posZ;
        double var10008 = entity.posZ;
        this.mc.getRenderManager();
        RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(var10002 += var10003 - RenderManager.renderPosX, var10003 += var10004 - RenderManager.renderPosY, var10004 += var10005 - RenderManager.renderPosZ, var10005 += var10006 - RenderManager.renderPosX, var10006 += var10007 - RenderManager.renderPosY, var10007 + (var10008 - RenderManager.renderPosZ)));
        GL11.glDisable((int)2848);
        RenderUtil.post();
        GL11.glPopMatrix();
    }

    public static void func_181561_a(AxisAlignedBB p_181561_0_) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(1, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        tessellator.draw();
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        if (this.mode.isCurrentMode("2D")) {
            ScaledResolution scaledRes = new ScaledResolution(this.mc);
            GlStateManager.color(0.0f, 0.0f, 0.0f, 0.0f);
            this.mc.theWorld.getLoadedEntityList().forEach(entity -> {
                EntityLivingBase ent;
                if (entity instanceof EntityLivingBase && this.isValid(ent = (EntityLivingBase)entity)) {
                    double posX = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)event.getPartialTicks();
                    double posY = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * (double)event.getPartialTicks();
                    double posZ = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)event.getPartialTicks();
                    AxisAlignedBB bb = entity.getEntityBoundingBox().expand(0.1, 0.1, 0.1);
                    Vector3d[] corners = new Vector3d[]{new Vector3d(posX + bb.minX - bb.maxX + (double)(entity.width / 2.0f), posY, posZ + bb.minZ - bb.maxZ + (double)(entity.width / 2.0f)), new Vector3d(posX + bb.maxX - bb.minX - (double)(entity.width / 2.0f), posY, posZ + bb.minZ - bb.maxZ + (double)(entity.width / 2.0f)), new Vector3d(posX + bb.minX - bb.maxX + (double)(entity.width / 2.0f), posY, posZ + bb.maxZ - bb.minZ - (double)(entity.width / 2.0f)), new Vector3d(posX + bb.maxX - bb.minX - (double)(entity.width / 2.0f), posY, posZ + bb.maxZ - bb.minZ - (double)(entity.width / 2.0f)), new Vector3d(posX + bb.minX - bb.maxX + (double)(entity.width / 2.0f), posY + bb.maxY - bb.minY, posZ + bb.minZ - bb.maxZ + (double)(entity.width / 2.0f)), new Vector3d(posX + bb.maxX - bb.minX - (double)(entity.width / 2.0f), posY + bb.maxY - bb.minY, posZ + bb.minZ - bb.maxZ + (double)(entity.width / 2.0f)), new Vector3d(posX + bb.minX - bb.maxX + (double)(entity.width / 2.0f), posY + bb.maxY - bb.minY, posZ + bb.maxZ - bb.minZ - (double)(entity.width / 2.0f)), new Vector3d(posX + bb.maxX - bb.minX - (double)(entity.width / 2.0f), posY + bb.maxY - bb.minY, posZ + bb.maxZ - bb.minZ - (double)(entity.width / 2.0f))};
                    GLUProjection.Projection result = null;
                    Vector4f transformed = new Vector4f((float)scaledRes.getScaledWidth() * 2.0f, (float)scaledRes.getScaledHeight() * 2.0f, -1.0f, -1.0f);
                    for (Vector3d vec : corners) {
                        result = GLUProjection.getInstance().project(vec.x - this.mc.getRenderManager().viewerPosX, vec.y - this.mc.getRenderManager().viewerPosY, vec.z - this.mc.getRenderManager().viewerPosZ, GLUProjection.ClampMode.NONE, true);
                        transformed.setX((float)Math.min((double)transformed.getX(), result.getX()));
                        transformed.setY((float)Math.min((double)transformed.getY(), result.getY()));
                        transformed.setW((float)Math.max((double)transformed.getW(), result.getX()));
                        transformed.setZ((float)Math.max((double)transformed.getZ(), result.getY()));
                    }
                    GlStateManager.pushMatrix();
                    if (RenderUtil.isInViewFrustrum(ent)) {
                        GlStateManager.pushMatrix();
                        GlStateManager.enableBlend();
                        GlStateManager.scale(0.5f, 0.5f, 0.5f);
                        float x = transformed.x * 2.0f;
                        float x2 = transformed.w * 2.0f;
                        float y = transformed.y * 2.0f;
                        float y2 = transformed.z * 2.0f;
                        RenderUtil.drawHollowBox(x, y, x2, y2, 3.0f, Color.BLACK.getRGB());
                        RenderUtil.drawHollowBox(x + 1.0f, y + 1.0f, x2 - 1.0f, y2 + 1.0f, 1.0f, new Color(16775672).getRGB());
                        if (this.health.getValueState().booleanValue()) {
                            float healthHeight = (y2 - y) * (((EntityLivingBase)entity).getHealth() / ((EntityLivingBase)entity).getMaxHealth());
                            RenderUtil.drawRect2(x - 4.0f, y, x - 1.0f, y2 + 3.0f, -16777216);
                            RenderUtil.drawRect2(x - 3.0f, y2 - healthHeight + 1.0f, x - 2.0f, y2 + 2.0f, this.getHealthColor((EntityLivingBase)entity));
                        }
                        if (this.armor.getValueState().booleanValue() && entity instanceof EntityPlayer) {
                            double armorstrength = 0.0;
                            EntityPlayer player = (EntityPlayer)entity;
                            for (int index = 3; index >= 0; --index) {
                                ItemStack stack = player.inventory.armorInventory[index];
                                if (stack == null) continue;
                                armorstrength += this.getArmorStrength(stack);
                            }
                            if (armorstrength > 0.0 && this.armor.getValueState().booleanValue()) {
                                double offset = y2 - y;
                                double percentoffset = offset / 40.0;
                                double finalnumber = percentoffset * armorstrength;
                                RenderUtil.drawRect2(x2 + 1.0f, y, x2 + 4.0f, y2 + 3.0f, -16777216);
                                RenderUtil.drawRect2(x2 + 2.0f, (double)y2 - finalnumber + 1.0, x2 + 3.0f, y2 + 2.0f, new Color(4096995).getRGB());
                            }
                        }
                        GlStateManager.popMatrix();
                    }
                    GlStateManager.popMatrix();
                }
            });
        }
    }

    private double getArmorStrength(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemArmor)) {
            return -1.0;
        }
        float damageReduction = ((ItemArmor)itemStack.getItem()).damageReduceAmount;
        Map<Integer, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        if (enchantments.containsKey(Enchantment.protection.effectId)) {
            int level = enchantments.get(Enchantment.protection.effectId);
            damageReduction += (float)Enchantment.protection.calcModifierDamage(level, DamageSource.generic);
        }
        return damageReduction;
    }

    private int getHealthColor(EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0f, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0f, 1.0f, 1.0f) | -16777216;
    }

    private boolean isValid(EntityLivingBase entity) {
        return Minecraft.thePlayer != entity && this.isValidType(entity) && entity.isEntityAlive() && (!entity.isInvisible() || this.invisibles.getValueState() != false);
    }

    private boolean isValidType(EntityLivingBase entity) {
        return this.players.getValueState() != false && entity instanceof EntityPlayer || this.mobs.getValueState() != false && (entity instanceof EntityMob || entity instanceof EntitySlime) || this.animals.getValueState() != false && (entity instanceof EntityVillager || entity instanceof EntityGolem) || this.animals.getValueState() != false && entity instanceof EntityAnimal;
    }

    @EventTarget
    public void onRender(EventRender e) {
        if (this.skeletal.getValueState().booleanValue()) {
            this.startEnd(true);
            GL11.glEnable((int)2903);
            GL11.glDisable((int)2848);
            entities.keySet().removeIf(this::doesntContain);
            this.mc.theWorld.playerEntities.forEach(player -> this.drawSkeleton(e, (EntityPlayer)player));
            Gui.drawRect(0, 0, 0, 0, 0);
            this.startEnd(false);
        }
    }

    private void drawSkeleton(EventRender event, EntityPlayer e) {
        float[][] entPos;
        Color color = new Color(16775672);
        if (!e.isInvisible() && (entPos = entities.get(e)) != null && e.isEntityAlive() && RenderUtil.isInViewFrustrum(e) && !e.isDead && e != Minecraft.thePlayer && !e.isPlayerSleeping()) {
            GL11.glEnable((int)2848);
            GL11.glPushMatrix();
            GL11.glLineWidth((float)1.3f);
            GlStateManager.color(color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255, 1.0f);
            Vec3 vec = this.getVec3(event, e);
            double x = vec.xCoord - RenderManager.renderPosX;
            double y = vec.yCoord - RenderManager.renderPosY;
            double z = vec.zCoord - RenderManager.renderPosZ;
            GL11.glTranslated((double)x, (double)y, (double)z);
            float xOff = e.prevRenderYawOffset + (e.renderYawOffset - e.prevRenderYawOffset) * event.getPartialTicks();
            GL11.glRotatef((float)(- xOff), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glTranslated((double)0.0, (double)0.0, (double)(e.isSneaking() ? -0.235 : 0.0));
            float yOff = e.isSneaking() ? 0.6f : 0.75f;
            GL11.glPushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslated((double)-0.125, (double)yOff, (double)0.0);
            if (entPos[3][0] != 0.0f) {
                GL11.glRotatef((float)(entPos[3][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (entPos[3][1] != 0.0f) {
                GL11.glRotatef((float)(entPos[3][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (entPos[3][2] != 0.0f) {
                GL11.glRotatef((float)(entPos[3][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)(- yOff), (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslated((double)0.125, (double)yOff, (double)0.0);
            if (entPos[4][0] != 0.0f) {
                GL11.glRotatef((float)(entPos[4][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (entPos[4][1] != 0.0f) {
                GL11.glRotatef((float)(entPos[4][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (entPos[4][2] != 0.0f) {
                GL11.glRotatef((float)(entPos[4][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)(- yOff), (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glTranslated((double)0.0, (double)0.0, (double)(e.isSneaking() ? 0.25 : 0.0));
            GL11.glPushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslated((double)0.0, (double)(e.isSneaking() ? -0.05 : 0.0), (double)(e.isSneaking() ? -0.01725 : 0.0));
            GL11.glPushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslated((double)-0.375, (double)((double)yOff + 0.55), (double)0.0);
            if (entPos[1][0] != 0.0f) {
                GL11.glRotatef((float)(entPos[1][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (entPos[1][1] != 0.0f) {
                GL11.glRotatef((float)(entPos[1][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (entPos[1][2] != 0.0f) {
                GL11.glRotatef((float)((- entPos[1][2]) * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)-0.5, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.375, (double)((double)yOff + 0.55), (double)0.0);
            if (entPos[2][0] != 0.0f) {
                GL11.glRotatef((float)(entPos[2][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (entPos[2][1] != 0.0f) {
                GL11.glRotatef((float)(entPos[2][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (entPos[2][2] != 0.0f) {
                GL11.glRotatef((float)((- entPos[2][2]) * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)-0.5, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glRotatef((float)(xOff - e.rotationYawHead), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glPushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslated((double)0.0, (double)((double)yOff + 0.55), (double)0.0);
            if (entPos[0][0] != 0.0f) {
                GL11.glRotatef((float)(entPos[0][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)0.3, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GL11.glRotatef((float)(e.isSneaking() ? 25.0f : 0.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glTranslated((double)0.0, (double)(e.isSneaking() ? -0.16175 : 0.0), (double)(e.isSneaking() ? -0.48025 : 0.0));
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.0, (double)yOff, (double)0.0);
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)-0.125, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.125, (double)0.0, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslated((double)0.0, (double)yOff, (double)0.0);
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)0.55, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.0, (double)((double)yOff + 0.55), (double)0.0);
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)-0.375, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.375, (double)0.0, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    public static void addEntity(EntityPlayer e, ModelPlayer model) {
        entities.put(e, new float[][]{{model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ}, {model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY, model.bipedRightArm.rotateAngleZ}, {model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY, model.bipedLeftArm.rotateAngleZ}, {model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY, model.bipedRightLeg.rotateAngleZ}, {model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ}});
    }

    private Vec3 getVec3(EventRender event, EntityPlayer var0) {
        float timer = event.getPartialTicks();
        double x = var0.lastTickPosX + (var0.posX - var0.lastTickPosX) * (double)timer;
        double y = var0.lastTickPosY + (var0.posY - var0.lastTickPosY) * (double)timer;
        double z = var0.lastTickPosZ + (var0.posZ - var0.lastTickPosZ) * (double)timer;
        return new Vec3(x, y, z);
    }

    private boolean doesntContain(EntityPlayer var0) {
        return !this.mc.theWorld.playerEntities.contains(var0);
    }

    private void startEnd(boolean revert) {
        if (revert) {
            GlStateManager.enableBlend();
            GL11.glEnable((int)2848);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.blendFunc(770, 771);
            GL11.glHint((int)3154, (int)4354);
        } else {
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GL11.glDisable((int)2848);
            GlStateManager.enableDepth();
        }
        GlStateManager.depthMask(!revert);
    }
}

