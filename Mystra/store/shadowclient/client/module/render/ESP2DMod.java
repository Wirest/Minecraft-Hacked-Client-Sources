package store.shadowclient.client.module.render;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.RenderGuiEvent;
import store.shadowclient.client.event.events.RenderNametagEvent;
import store.shadowclient.client.management.command.variables.FriendManager;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.ColorUtils;
import store.shadowclient.client.utils.player.PlayerUtils;
import store.shadowclient.client.utils.render.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;

public class ESP2DMod extends Module {
	
   public ESP2DMod() {
		super("ESP2D", 0, Category.RENDER);
		ArrayList<String> options = new ArrayList<>();
        options.add("Box2");
        options.add("Box");
        
        Shadow.instance.settingsManager.rSetting(new Setting("Box Mode", this, "Box", options));
        
		Shadow.instance.settingsManager.rSetting(new Setting("Dropped Items", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("Local Player", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("Invisibles", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("Health Bar", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("Armor Bar", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("Outline", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("Players", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("Animals", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("Mobs", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("Chests", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("Tag", this, true));
		this.collectedEntities = new ArrayList();
		this.viewport = GLAllocation.createDirectIntBuffer(16);
	    this.modelview = GLAllocation.createDirectFloatBuffer(16);
	    this.projection = GLAllocation.createDirectFloatBuffer(16);
	    this.vector = GLAllocation.createDirectFloatBuffer(4);
	    this.color = Color.WHITE.getRGB();
	    this.backgroundColor = (new Color(0, 0, 0, 120)).getRGB();
	    this.black = Color.BLACK.getRGB();
	}
   
   public final List collectedEntities;
   private final IntBuffer viewport;
   private final FloatBuffer modelview;
   private final FloatBuffer projection;
   private final FloatBuffer vector;
   private final int color;
   private final int backgroundColor;
   private final int black;

   @EventTarget
   public void onEvent(RenderNametagEvent event) {
      if (this.isValid(event.getEntity()) && Shadow.instance.settingsManager.getSettingByName("Tag").getValBoolean()) {
         event.setCancelled(true);
      }

   }

   @EventTarget
   public void onRender(RenderGuiEvent event) {
	   String mode = Shadow.instance.settingsManager.getSettingByName("Box Mode").getValString();
      GL11.glPushMatrix();
      this.collectEntities();
      float partialTicks = event.getPartialTicks();
      ScaledResolution scaledResolution = event.getScaledResolution();
      int scaleFactor = scaledResolution.getScaleFactor();
      double scaling = (double)scaleFactor / Math.pow((double)scaleFactor, 2.0D);
      GL11.glScaled(scaling, scaling, scaling);
      int black = this.black;
      int color = this.color;
      int background = this.backgroundColor;
      float scale = 0.65F;
      float upscale = 1.0F / scale;
      FontRenderer fr = mc.fontRendererObj;
      RenderManager renderMng = mc.getRenderManager();
      EntityRenderer entityRenderer = mc.entityRenderer;
      boolean tag = Shadow.instance.settingsManager.getSettingByName("Tag").getValBoolean();
      boolean outline = Shadow.instance.settingsManager.getSettingByName("Outline").getValBoolean();
      boolean health = Shadow.instance.settingsManager.getSettingByName("Health Bar").getValBoolean();
      boolean armor = Shadow.instance.settingsManager.getSettingByName("Armor Bar").getValBoolean();
      List collectedEntities = this.collectedEntities;
      int i = 0;

      for(int collectedEntitiesSize = collectedEntities.size(); i < collectedEntitiesSize; ++i) {
         Entity entity = (Entity)collectedEntities.get(i);
         if (this.isValid(entity) && RenderUtils.isInViewFrustrum(entity)) {
            double x = RenderUtils.interpolate(entity.posX, entity.lastTickPosX, (double)partialTicks);
            double y = RenderUtils.interpolate(entity.posY, entity.lastTickPosY, (double)partialTicks);
            double z = RenderUtils.interpolate(entity.posZ, entity.lastTickPosZ, (double)partialTicks);
            double width = (double)entity.width / 1.5D;
            double height = (double)entity.height + (entity.isSneaking() ? -0.3D : 0.2D);
            AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
            List vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
            entityRenderer.setupCameraTransform(partialTicks, 0);
            Vector4d position = null;
            Iterator var38 = vectors.iterator();

            while(var38.hasNext()) {
               Vector3d vector = (Vector3d)var38.next();
               vector = this.project2D(scaleFactor, vector.x - renderMng.viewerPosX, vector.y - renderMng.viewerPosY, vector.z - renderMng.viewerPosZ);
               if (vector != null && vector.z >= 0.0D && vector.z < 1.0D) {
                  if (position == null) {
                     position = new Vector4d(vector.x, vector.y, vector.z, 0.0D);
                  }

                  position.x = Math.min(vector.x, position.x);
                  position.y = Math.min(vector.y, position.y);
                  position.z = Math.max(vector.x, position.z);
                  position.w = Math.max(vector.y, position.w);
               }
            }

            if (position != null) {
               entityRenderer.setupOverlayRendering((double)scaledResolution.getScaledWidth(), (double)scaledResolution.getScaledHeight());
               double posX = position.x;
               double posY = position.y;
               double endPosX = position.z;
               double endPosY = position.w;
               if (outline) {
                  if (mode.equalsIgnoreCase("Box")) {
                     Gui.drawRect(posX - 1.0D, posY, posX + 0.5D, endPosY + 0.5D, black);
                     Gui.drawRect(posX - 1.0D, posY - 0.5D, endPosX + 0.5D, posY + 0.5D + 0.5D, black);
                     Gui.drawRect(endPosX - 0.5D - 0.5D, posY, endPosX + 0.5D, endPosY + 0.5D, black);
                     Gui.drawRect(posX - 1.0D, endPosY - 0.5D - 0.5D, endPosX + 0.5D, endPosY + 0.5D, black);
                     Gui.drawRect(posX - 0.5D, posY, posX + 0.5D - 0.5D, endPosY, color);
                     Gui.drawRect(posX, endPosY - 0.5D, endPosX, endPosY, color);
                     Gui.drawRect(posX - 0.5D, posY, endPosX, posY + 0.5D, color);
                     Gui.drawRect(endPosX - 0.5D, posY, endPosX, endPosY, color);
                  }
                  if(mode.equalsIgnoreCase("Box2"))
                     Gui.drawRect(posX + 0.5D, posY, posX - 1.0D, posY + (endPosY - posY) / 4.0D + 0.5D, black);
                     Gui.drawRect(posX - 1.0D, endPosY, posX + 0.5D, endPosY - (endPosY - posY) / 4.0D - 0.5D, black);
                     Gui.drawRect(posX - 1.0D, posY - 0.5D, posX + (endPosX - posX) / 3.0D + 0.5D, posY + 1.0D, black);
                     Gui.drawRect(endPosX - (endPosX - posX) / 3.0D - 0.5D, posY - 0.5D, endPosX, posY + 1.0D, black);
                     Gui.drawRect(endPosX - 1.0D, posY, endPosX + 0.5D, posY + (endPosY - posY) / 4.0D + 0.5D, black);
                     Gui.drawRect(endPosX - 1.0D, endPosY, endPosX + 0.5D, endPosY - (endPosY - posY) / 4.0D - 0.5D, black);
                     Gui.drawRect(posX - 1.0D, endPosY - 1.0D, posX + (endPosX - posX) / 3.0D + 0.5D, endPosY + 0.5D, black);
                     Gui.drawRect(endPosX - (endPosX - posX) / 3.0D - 0.5D, endPosY - 1.0D, endPosX + 0.5D, endPosY + 0.5D, black);
                     Gui.drawRect(posX, posY, posX - 0.5D, posY + (endPosY - posY) / 4.0D, color);
                     Gui.drawRect(posX, endPosY, posX - 0.5D, endPosY - (endPosY - posY) / 4.0D, color);
                     Gui.drawRect(posX - 0.5D, posY, posX + (endPosX - posX) / 3.0D, posY + 0.5D, color);
                     Gui.drawRect(endPosX - (endPosX - posX) / 3.0D, posY, endPosX, posY + 0.5D, color);
                     Gui.drawRect(endPosX - 0.5D, posY, endPosX, posY + (endPosY - posY) / 4.0D, color);
                     Gui.drawRect(endPosX - 0.5D, endPosY, endPosX, endPosY - (endPosY - posY) / 4.0D, color);
                     Gui.drawRect(posX, endPosY - 0.5D, posX + (endPosX - posX) / 3.0D, endPosY, color);
                     Gui.drawRect(endPosX - (endPosX - posX) / 3.0D, endPosY - 0.5D, endPosX - 0.5D, endPosY, color);
               }

               boolean living = entity instanceof EntityLivingBase;
               EntityLivingBase entityLivingBase;
               float armorValue;
               float itemDurability;
               double durabilityWidth;
               double textWidth;
               float tagY;
               if (living) {
                  entityLivingBase = (EntityLivingBase)entity;
                  if (health) {
                     armorValue = entityLivingBase.getHealth();
                     itemDurability = entityLivingBase.getMaxHealth();
                     if (armorValue > itemDurability) {
                        armorValue = itemDurability;
                     }

                     durabilityWidth = (double)(armorValue / itemDurability);
                     textWidth = (endPosY - posY) * durabilityWidth;
                     Gui.drawRect(posX - 3.5D, posY - 0.5D, posX - 1.5D, endPosY + 0.5D, background);
                     if (armorValue > 0.0F) {
                        int healthColor = ColorUtils.getHealthColor(armorValue, itemDurability).getRGB();
                        Gui.drawRect(posX - 3.0D, endPosY, posX - 2.0D, endPosY - textWidth, healthColor);
                        tagY = entityLivingBase.getAbsorptionAmount();
                        if (tagY > 0.0F) {
                           Gui.drawRect(posX - 3.0D, endPosY, posX - 2.0D, endPosY - (endPosY - posY) / 6.0D * (double)tagY / 2.0D, (new Color(Potion.absorption.getLiquidColor())).getRGB());
                        }
                     }
                  }
               }

               if (tag) {
                  float scaledHeight = 10.0F;
                  String name = entity.getName();
                  if (entity instanceof EntityItem) {
                     name = ((EntityItem)entity).getEntityItem().getDisplayName();
                  }

                  String prefix = "";
                  if (entity instanceof EntityPlayer) {
                     prefix = this.isFriendly((EntityPlayer)entity) ? "§a" : "§c";
                  }

                  durabilityWidth = (endPosX - posX) / 2.0D;
                  textWidth = (double)((float)fr.getStringWidth(name) * scale);
                  float tagX = (float)((posX + durabilityWidth - textWidth / 2.0D) * (double)upscale);
                  tagY = (float)(posY * (double)upscale) - scaledHeight;
                  GL11.glPushMatrix();
                  GL11.glScalef(scale, scale, scale);
                  if (living) {
                     Gui.drawRect((double)(tagX - 2.0F), (double)(tagY - 2.0F), (double)tagX + textWidth * (double)upscale + 2.0D, (double)(tagY + 9.0F), (new Color(0, 0, 0, 140)).getRGB());
                  }

                  Shadow.fontManager.getFont("SFL 14").drawStringWithShadow(prefix + name, tagX, tagY, -1);
                  GL11.glPopMatrix();
               }

               if (armor) {
                  if (living) {
                     entityLivingBase = (EntityLivingBase)entity;
                     armorValue = (float)entityLivingBase.getTotalArmorValue();
                     double armorWidth = (endPosX - posX) * (double)armorValue / 20.0D;
                     Gui.drawRect(posX - 0.5D, endPosY + 1.5D, posX - 0.5D + endPosX - posX + 1.0D, endPosY + 1.5D + 2.0D, background);
                     if (armorValue > 0.0F) {
                        Gui.drawRect(posX, endPosY + 2.0D, posX + armorWidth, endPosY + 3.0D, 16777215);
                     }
                  } else if (entity instanceof EntityItem) {
                     ItemStack itemStack = ((EntityItem)entity).getEntityItem();
                     if (itemStack.isItemStackDamageable()) {
                        int maxDamage = itemStack.getMaxDamage();
                        itemDurability = (float)(maxDamage - itemStack.getItemDamage());
                        durabilityWidth = (endPosX - posX) * (double)itemDurability / (double)maxDamage;
                        Gui.drawRect(posX - 0.5D, endPosY + 1.5D, posX - 0.5D + endPosX - posX + 1.0D, endPosY + 1.5D + 2.0D, background);
                        Gui.drawRect(posX, endPosY + 2.0D, posX + durabilityWidth, endPosY + 3.0D, 16777215);
                     }
                  }
               }
            }
         }
      }

      GL11.glPopMatrix();
      GlStateManager.enableBlend();
      entityRenderer.setupOverlayRendering((double)scaledResolution.getScaledWidth(), (double)scaledResolution.getScaledHeight());
   }

   private boolean isFriendly(EntityPlayer player) {
      return PlayerUtils.isOnSameTeam(player) || FriendManager.isFriend(player.getName());
   }

   private void collectEntities() {
      this.collectedEntities.clear();
      List playerEntities = mc.theWorld.loadedEntityList;
      int i = 0;

      for(int playerEntitiesSize = playerEntities.size(); i < playerEntitiesSize; ++i) {
         Entity entity = (Entity)playerEntities.get(i);
         if (this.isValid(entity)) {
            this.collectedEntities.add(entity);
         }
      }

   }

   private Vector3d project2D(int scaleFactor, double x, double y, double z) {
      GL11.glGetFloat(2982, this.modelview);
      GL11.glGetFloat(2983, this.projection);
      GL11.glGetInteger(2978, this.viewport);
      return GLU.gluProject((float)x, (float)y, (float)z, this.modelview, this.projection, this.viewport, this.vector) ? new Vector3d((double)(this.vector.get(0) / (float)scaleFactor), (double)(((float)Display.getHeight() - this.vector.get(1)) / (float)scaleFactor), (double)this.vector.get(2)) : null;
   }

   private boolean isValid(Entity entity) {
      if (entity != mc.thePlayer || Shadow.instance.settingsManager.getSettingByName("Armor Bar").getValBoolean() && mc.gameSettings.thirdPersonView != 0) {
         if (entity.isDead) {
            return false;
         } else if (!Shadow.instance.settingsManager.getSettingByName("Invisibles").getValBoolean() && entity.isInvisible()) {
            return false;
         } else if (Shadow.instance.settingsManager.getSettingByName("Dropped Items").getValBoolean() && entity instanceof EntityItem && mc.thePlayer.getDistanceToEntity(entity) < 10.0F) {
            return true;
         } else if (Shadow.instance.settingsManager.getSettingByName("Animals").getValBoolean() && entity instanceof EntityAnimal) {
            return true;
         } else if (Shadow.instance.settingsManager.getSettingByName("Players").getValBoolean() && entity instanceof EntityPlayer) {
            return true;
         } else {
            return Shadow.instance.settingsManager.getSettingByName("Mobs").getValBoolean() && (entity instanceof EntityMob || entity instanceof EntitySlime || entity instanceof EntityDragon || entity instanceof EntityGolem);
         }
      } else {
         return false;
      }
   }
}
