package com.etb.client.module.modules.render;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.*;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import javax.vecmath.Vector4f;

import com.etb.client.utils.GLUProjection;
import com.etb.client.utils.value.impl.NumberValue;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.opengl.GL11;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.etb.client.Client;
import com.etb.client.event.events.render.Render2DEvent;
import com.etb.client.event.events.render.Render3DEvent;
import com.etb.client.event.events.render.RenderNametagEvent;
import com.etb.client.module.Module;
import com.etb.client.module.modules.combat.KillAura;
import com.etb.client.utils.RenderUtil;
import com.etb.client.utils.value.impl.BooleanValue;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StringUtils;

/**
 * made by etb for ETB Reloaded
 *
 * @since 5/29/2019
 **/
public class ESP extends Module {
    private BooleanValue players = new BooleanValue("Players", true);
    private BooleanValue animals = new BooleanValue("Animals", true);
    private BooleanValue mobs = new BooleanValue("Mobs", false);
    private BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private BooleanValue passives = new BooleanValue("Passives", true);
    private BooleanValue box = new BooleanValue("Box", true);
    private BooleanValue health = new BooleanValue("Health", true);
    private BooleanValue armor = new BooleanValue("Armor", true);
    private BooleanValue items = new BooleanValue("Items", false);
    private BooleanValue skeleton = new BooleanValue("Skeleton", false);
    private NumberValue<Float> skeletonwidth = new NumberValue("SkeletonWidth", 1.0f, 0.5f, 10.0f, 0.1f);
    private static Map<EntityPlayer, float[][]> entities = new HashMap<>();
    public ESP() {
        super("ESP", Category.RENDER, new Color(255, 120, 120, 255).getRGB());
        setDescription("ESP");
        addValues(players, animals, mobs, invisibles, passives, box, health, armor, items, skeleton,skeletonwidth);
    }
    @Subscribe
    public void onRender3D(Render3DEvent e) {
        if (!skeleton.isEnabled()) return;
        startEnd(true);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glDisable(2848);
        entities.keySet().removeIf(this::doesntContain);
        mc.theWorld.playerEntities.forEach(player -> drawSkeleton(e,player));
        Gui.drawRect(0, 0, 0, 0, 0);
        startEnd(false);
    }
    @Subscribe
    public void onRender2D(Render2DEvent event) {
        final ScaledResolution scaledRes = new ScaledResolution(mc);
        mc.theWorld.getLoadedEntityList().forEach(entity -> {
            if (entity instanceof EntityItem) {
                EntityItem ent = (EntityItem) entity;
                double posX = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * event.getPT();
                double posY = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * event.getPT();
                double posZ = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * event.getPT();
                AxisAlignedBB bb = entity.getEntityBoundingBox().expand(0.1, 0.1, 0.1);
                Vector3d[] corners = {new Vector3d(posX + bb.minX - bb.maxX + entity.width / 2.0f, posY, posZ + bb.minZ - bb.maxZ + entity.width / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - entity.width / 2.0f, posY, posZ + bb.minZ - bb.maxZ + entity.width / 2.0f), new Vector3d(posX + bb.minX - bb.maxX + entity.width / 2.0f, posY, posZ + bb.maxZ - bb.minZ - entity.width / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - entity.width / 2.0f, posY, posZ + bb.maxZ - bb.minZ - entity.width / 2.0f), new Vector3d(posX + bb.minX - bb.maxX + entity.width / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.minZ - bb.maxZ + entity.width / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - entity.width / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.minZ - bb.maxZ + entity.width / 2.0f), new Vector3d(posX + bb.minX - bb.maxX + entity.width / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.maxZ - bb.minZ - entity.width / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - entity.width / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.maxZ - bb.minZ - entity.width / 2.0f)};
                GLUProjection.Projection result = null;
                Vector4f transformed = new Vector4f(scaledRes.getScaledWidth() * 2.0f, scaledRes.getScaledHeight() * 2.0f, -1.0f, -1.0f);
                for (Vector3d vec : corners) {
                    result = GLUProjection.getInstance().project(vec.x - mc.getRenderManager().viewerPosX, vec.y - mc.getRenderManager().viewerPosY, vec.z - mc.getRenderManager().viewerPosZ, GLUProjection.ClampMode.NONE, true);
                    transformed.setX((float) Math.min(transformed.getX(), result.getX()));
                    transformed.setY((float) Math.min(transformed.getY(), result.getY()));
                    transformed.setW((float) Math.max(transformed.getW(), result.getX()));
                    transformed.setZ((float) Math.max(transformed.getZ(), result.getY()));
                }
                GlStateManager.pushMatrix();
                if (RenderUtil.isInViewFrustrum(ent)) {
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.scale(.5f, .5f, .5f);
                    float x = transformed.x * 2;
                    float x2 = transformed.w * 2;
                    float y = transformed.y * 2;
                    float y2 = transformed.z * 2;
                    if (box.isEnabled()) {
                        RenderUtil.drawHollowBox(x, y, x2, y2, 3f, Color.BLACK.getRGB());
                        RenderUtil.drawHollowBox(x + 1f, y + 1f, x2 - 1f, y2 + 1f, 1f, new Color(255, 255, 255).getRGB());
                    }
                    if (ent.getEntityItem().getMaxDamage() > 0) {
                        double offset = y2 - y;
                        double percentoffset = offset / ent.getEntityItem().getMaxDamage();
                        double finalnumber = percentoffset * (ent.getEntityItem().getMaxDamage() - ent.getEntityItem().getItemDamage());
                        RenderUtil.drawRect2(x - 4f, y, x - 1f, y2 + 3f, -0x1000000);
                        RenderUtil.drawRect2(x - 3f, y2 - finalnumber + 1f, x - 2f, y2 + 2f, new Color(0x3E83E3).getRGB());
                    }
                    final String nametext = StringUtils.stripControlCodes(ent.getEntityItem().getItem().getItemStackDisplayName(ent.getEntityItem())) + (ent.getEntityItem().getMaxDamage() > 0 ? "§9 : " + (ent.getEntityItem().getMaxDamage() - ent.getEntityItem().getItemDamage()) : "");
                    mc.fontRendererObj.drawStringWithShadow(nametext, (x + ((x2 - x) / 2)) - (mc.fontRendererObj.getStringWidth(nametext) / 2), y - mc.fontRendererObj.FONT_HEIGHT - 2, -1);
                    GlStateManager.popMatrix();
                }
                GlStateManager.popMatrix();
            }
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) entity;
                if (isValid(ent)) {
                    double posX = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * event.getPT();
                    double posY = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * event.getPT();
                    double posZ = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * event.getPT();
                    AxisAlignedBB bb = entity.getEntityBoundingBox().expand(0.1, 0.1, 0.1);
                    Vector3d[] corners = {new Vector3d(posX + bb.minX - bb.maxX + entity.width / 2.0f, posY, posZ + bb.minZ - bb.maxZ + entity.width / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - entity.width / 2.0f, posY, posZ + bb.minZ - bb.maxZ + entity.width / 2.0f), new Vector3d(posX + bb.minX - bb.maxX + entity.width / 2.0f, posY, posZ + bb.maxZ - bb.minZ - entity.width / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - entity.width / 2.0f, posY, posZ + bb.maxZ - bb.minZ - entity.width / 2.0f), new Vector3d(posX + bb.minX - bb.maxX + entity.width / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.minZ - bb.maxZ + entity.width / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - entity.width / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.minZ - bb.maxZ + entity.width / 2.0f), new Vector3d(posX + bb.minX - bb.maxX + entity.width / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.maxZ - bb.minZ - entity.width / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - entity.width / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.maxZ - bb.minZ - entity.width / 2.0f)};
                    GLUProjection.Projection result = null;
                    Vector4f transformed = new Vector4f(scaledRes.getScaledWidth() * 2.0f, scaledRes.getScaledHeight() * 2.0f, -1.0f, -1.0f);
                    for (Vector3d vec : corners) {
                        result = GLUProjection.getInstance().project(vec.x - mc.getRenderManager().viewerPosX, vec.y - mc.getRenderManager().viewerPosY, vec.z - mc.getRenderManager().viewerPosZ, GLUProjection.ClampMode.NONE, true);
                        transformed.setX((float) Math.min(transformed.getX(), result.getX()));
                        transformed.setY((float) Math.min(transformed.getY(), result.getY()));
                        transformed.setW((float) Math.max(transformed.getW(), result.getX()));
                        transformed.setZ((float) Math.max(transformed.getZ(), result.getY()));
                    }
                    GlStateManager.pushMatrix();
                    if (RenderUtil.isInViewFrustrum(ent)) {
                        GlStateManager.pushMatrix();
                        GlStateManager.enableBlend();
                        GlStateManager.scale(.5f, .5f, .5f);
                        float x = transformed.x * 2;
                        float x2 = transformed.w * 2;
                        float y = transformed.y * 2;
                        float y2 = transformed.z * 2;
                        if (box.isEnabled()) {
                            RenderUtil.drawHollowBox(x, y, x2, y2, 3f, Color.BLACK.getRGB());
                            RenderUtil.drawHollowBox(x + 1f, y + 1f, x2 - 1f, y2 + 1f, 1f, Client.INSTANCE.getFriendManager().isFriend((entity).getName()) ? 0xFF7FCDFF : ((entity).getName().equalsIgnoreCase(mc.thePlayer.getName()) ? 0xFF99ff99 : new Color(0xFFF9F8).getRGB()));
                        }
                        if (health.isEnabled()) {
                            float healthHeight = (y2 - y) * (((EntityLivingBase) entity).getHealth() / ((EntityLivingBase) entity).getMaxHealth());
                            RenderUtil.drawRect2(x - 4f, y, x - 1f, y2 + 3f, -0x1000000);
                            RenderUtil.drawRect2(x - 3f, y2 - healthHeight + 1f, x - 2f, y2 + 2f, getHealthColor(((EntityLivingBase) entity)));
                        }
                        if (armor.isEnabled() && entity instanceof EntityPlayer) {
                            double armorstrength = 0;
                            EntityPlayer player = (EntityPlayer) entity;
                            for (int index = 3; index >= 0; index--) {
                                ItemStack stack = player.inventory.armorInventory[index];
                                if (stack != null) {
                                    armorstrength += getArmorStrength(stack);
                                }
                            }
                            if (armorstrength > 0.0f && armor.isEnabled()) {
                                double offset = y2 - y;
                                double percentoffset = offset / 40;
                                double finalnumber = percentoffset * armorstrength;
                                RenderUtil.drawRect2(x2 + 1f, y, x2 + 4f, y2 + 3f, -0x1000000);
                                RenderUtil.drawRect2(x2 + 2f, y2 - finalnumber + 1f, x2 + 3f, y2 + 2f, new Color(0x3E83E3).getRGB());
                            }
                        }
                        GlStateManager.popMatrix();
                    }
                    GlStateManager.popMatrix();
                }
            }
        });
    }
    private void drawSkeleton(Render3DEvent event, EntityPlayer e) {
        final Color color = new Color(Client.INSTANCE.getFriendManager().isFriend(e.getName()) ? 0xFF7FCDFF : (e.getName().equalsIgnoreCase(mc.thePlayer.getName()) ? 0xFF99ff99 : new Color(0xFFF9F8).getRGB()));
        if (!e.isInvisible()) {
            float[][] entPos = entities.get(e);
            if (entPos != null && e.isEntityAlive() && RenderUtil.isInViewFrustrum(e) && !e.isDead && e != mc.thePlayer && !e.isPlayerSleeping()) {
                GL11.glPushMatrix();
                GL11.glLineWidth(skeletonwidth.getValue());
                GlStateManager.color(color.getRed() / 255,color.getGreen() / 255,color.getBlue() / 255,1);
                Vec3 vec = getVec3(event,e);
                double x = vec.xCoord - RenderManager.renderPosX;
                double y = vec.yCoord - RenderManager.renderPosY;
                double z = vec.zCoord - RenderManager.renderPosZ;
                GL11.glTranslated(x, y, z);
                float xOff = e.prevRenderYawOffset + (e.renderYawOffset - e.prevRenderYawOffset) * event.getPartialTicks();
                GL11.glRotatef(-xOff, 0.0F, 1.0F, 0.0F);
                GL11.glTranslated(0.0D, 0.0D, e.isSneaking() ? -0.235D : 0.0D);
                float yOff = e.isSneaking() ? 0.6F : 0.75F;
                GL11.glPushMatrix();
                GlStateManager.color(1, 1, 1, 1);
                GL11.glTranslated(-0.125D, yOff, 0.0D);
                if (entPos[3][0] != 0.0F) {
                    GL11.glRotatef(entPos[3][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
                }

                if (entPos[3][1] != 0.0F) {
                    GL11.glRotatef(entPos[3][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
                }

                if (entPos[3][2] != 0.0F) {
                    GL11.glRotatef(entPos[3][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
                }

                GL11.glBegin(3);
                GL11.glVertex3d(0.0D, 0.0D, 0.0D);
                GL11.glVertex3d(0.0D, (-yOff), 0.0D);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GlStateManager.color(1, 1, 1, 1);
                GL11.glTranslated(0.125D, yOff, 0.0D);
                if (entPos[4][0] != 0.0F) {
                    GL11.glRotatef(entPos[4][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
                }

                if (entPos[4][1] != 0.0F) {
                    GL11.glRotatef(entPos[4][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
                }

                if (entPos[4][2] != 0.0F) {
                    GL11.glRotatef(entPos[4][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
                }

                GL11.glBegin(3);
                GL11.glVertex3d(0.0D, 0.0D, 0.0D);
                GL11.glVertex3d(0.0D, (-yOff), 0.0D);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glTranslated(0.0D, 0.0D, e.isSneaking() ? 0.25D : 0.0D);
                GL11.glPushMatrix();
                GlStateManager.color(1, 1, 1, 1);
                GL11.glTranslated(0.0D, e.isSneaking() ? -0.05D : 0.0D, e.isSneaking() ? -0.01725D : 0.0D);
                GL11.glPushMatrix();
                GlStateManager.color(1, 1, 1, 1);
                GL11.glTranslated(-0.375D, yOff + 0.55D, 0.0D);
                if (entPos[1][0] != 0.0F) {
                    GL11.glRotatef(entPos[1][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
                }

                if (entPos[1][1] != 0.0F) {
                    GL11.glRotatef(entPos[1][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
                }

                if (entPos[1][2] != 0.0F) {
                    GL11.glRotatef(-entPos[1][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
                }

                GL11.glBegin(3);
                GL11.glVertex3d(0.0D, 0.0D, 0.0D);
                GL11.glVertex3d(0.0D, -0.5D, 0.0D);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glTranslated(0.375D, yOff + 0.55D, 0.0D);
                if (entPos[2][0] != 0.0F) {
                    GL11.glRotatef(entPos[2][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
                }

                if (entPos[2][1] != 0.0F) {
                    GL11.glRotatef(entPos[2][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
                }

                if (entPos[2][2] != 0.0F) {
                    GL11.glRotatef(-entPos[2][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
                }

                GL11.glBegin(3);
                GL11.glVertex3d(0.0D, 0.0D, 0.0D);
                GL11.glVertex3d(0.0D, -0.5D, 0.0D);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glRotatef(xOff - e.rotationYawHead, 0.0F, 1.0F, 0.0F);
                GL11.glPushMatrix();
                GlStateManager.color(1, 1, 1, 1);
                GL11.glTranslated(0.0D, yOff + 0.55D, 0.0D);
                if (entPos[0][0] != 0.0F) {
                    GL11.glRotatef(entPos[0][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
                }

                GL11.glBegin(3);
                GL11.glVertex3d(0.0D, 0.0D, 0.0D);
                GL11.glVertex3d(0.0D, 0.3D, 0.0D);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
                GL11.glRotatef(e.isSneaking() ? 25.0F : 0.0F, 1.0F, 0.0F, 0.0F);
                GL11.glTranslated(0.0D, e.isSneaking() ? -0.16175D : 0.0D, e.isSneaking() ? -0.48025D : 0.0D);
                GL11.glPushMatrix();
                GL11.glTranslated(0.0D, yOff, 0.0D);
                GL11.glBegin(3);
                GL11.glVertex3d(-0.125D, 0.0D, 0.0D);
                GL11.glVertex3d(0.125D, 0.0D, 0.0D);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GlStateManager.color(1,1,1,1);
                GL11.glTranslated(0.0D, yOff, 0.0D);
                GL11.glBegin(3);
                GL11.glVertex3d(0.0D, 0.0D, 0.0D);
                GL11.glVertex3d(0.0D, 0.55D, 0.0D);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glTranslated(0.0D, yOff + 0.55D, 0.0D);
                GL11.glBegin(3);
                GL11.glVertex3d(-0.375D, 0.0D, 0.0D);
                GL11.glVertex3d(0.375D, 0.0D, 0.0D);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
                GlStateManager.color(1, 1, 1, 1);
            }
        }
    }

    private Vec3 getVec3(Render3DEvent event, EntityPlayer var0) {
        float timer = event.getPartialTicks();
        double x = var0.lastTickPosX + (var0.posX - var0.lastTickPosX) * timer;
        double y = var0.lastTickPosY + (var0.posY - var0.lastTickPosY) * timer;
        double z = var0.lastTickPosZ + (var0.posZ - var0.lastTickPosZ) * timer;
        return new Vec3(x, y, z);
    }

    public static void addEntity(EntityPlayer e, ModelPlayer model) {
        entities.put(e, new float[][]{{model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ}, {model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY, model.bipedRightArm.rotateAngleZ}, {model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY, model.bipedLeftArm.rotateAngleZ}, {model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY, model.bipedRightLeg.rotateAngleZ}, {model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ}});
    }

    private boolean doesntContain(EntityPlayer var0) {
        return !mc.theWorld.playerEntities.contains(var0);
    }
    private void startEnd(boolean revert) {
        if (revert) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GL11.glEnable(2848);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.blendFunc(770, 771);
            GL11.glHint(3154, 4354);
        } else {
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GL11.glDisable(2848);
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }

        GlStateManager.depthMask(!revert);
    }
    private boolean isValid(EntityLivingBase entity) {
        return mc.thePlayer != entity && isValidType(entity) && entity.isEntityAlive() && (!entity.isInvisible() || invisibles.isEnabled());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return (players.isEnabled() && entity instanceof EntityPlayer) || (mobs.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime) || (passives.isEnabled() && (entity instanceof EntityVillager || entity instanceof EntityGolem)) || (animals.isEnabled() && entity instanceof EntityAnimal));
    }

    private double getArmorStrength(final ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemArmor)) return -1;
        float damageReduction = ((ItemArmor) itemStack.getItem()).damageReduceAmount;
        Map enchantments = EnchantmentHelper.getEnchantments(itemStack);
        if (enchantments.containsKey(Enchantment.protection.effectId)) {
            int level = (int) enchantments.get(Enchantment.protection.effectId);
            damageReduction += Enchantment.protection.calcModifierDamage(level, DamageSource.generic);
        }
        return damageReduction;
    }

    private int getHealthColor(EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0F, 1.0F, 1.0F) | 0xFF000000;
    }
}