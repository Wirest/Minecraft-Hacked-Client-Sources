package me.slowly.client.mod.mods.render;

import com.darkmagician6.eventapi.EventTarget;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import me.slowly.client.events.EventRender;
import me.slowly.client.events.EventRender2D;
import me.slowly.client.events.EventRenderEntity;
import me.slowly.client.events.EventRendererLivingEntity;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.mod.mods.combat.KillAura;
import me.slowly.client.shaders.fragment.OutlineShader;
import me.slowly.client.ui.hudcustomizer.customs.CustomHUDTabGui;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.FlatColors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class ESP extends Mod {
   private Value players;
   private Value animals;
   private Value mobs;
   private Value rainbowcol = new Value("ESP_Rainbow", false);
   private ArrayList entities = new ArrayList();
   public static Value modes = new Value("ESP", "Mode", 0);
   private OutlineShader outlineShader;

   public ESP() {
      super("ESP", Mod.Category.RENDER, Colors.YELLOW.c);
      modes.mode.add("Outline");
      modes.mode.add("2D");
      modes.mode.add("Other2D");
      modes.mode.add("Candy");
      modes.mode.add("Vanilla");
      modes.mode.add("Box");
      modes.addValue("Shader");
      this.players = new Value("ESP_Players", true);
      this.animals = new Value("ESP_Animals", true);
      this.mobs = new Value("ESP_Mobs", true);
      this.showValue = modes;
      this.outlineShader = new OutlineShader();
   }

   public void onEnable() {
      this.mc.gameSettings.guiScale = 2;
      ClientUtil.sendClientMessage("PlayerESP Enable", ClientNotification.Type.SUCCESS);
      super.onEnable();
   }

   public void onDisable() {
      this.entities.clear();
      this.outlineShader.deleteShader();
      super.onDisable();
      ClientUtil.sendClientMessage("PlayerESP Disable", ClientNotification.Type.ERROR);
   }

   @EventTarget
   public void onRender3D(EventRendererLivingEntity event) {
      this.showValue = modes;
      this.setColor(-16711883);
      if (modes.isCurrentMode("Outline")) {
         RenderUtil.checkSetupFBO();
         this.doOutlineESP(event);
      }

   }

   @EventTarget
   public void onRender(EventRender event) {
      if (modes.isCurrentMode("2D")) {
         this.doCornerESP();
      } else if (modes.isCurrentMode("Other2D")) {
         this.doOther2DESP();
      } else if (modes.isCurrentMode("Candy")) {
         this.doCandyESP();
      } else if (modes.isCurrentMode("Box")) {
         this.doBoxESP(event);
      } else if (modes.isCurrentMode("Vanilla") && this.mc.gameSettings.ofFastRender) {
         this.set(false);
         ClientUtil.sendClientMessage("Options->Video Settings->Performance->Fast Render->Off", ClientNotification.Type.ERROR);
         return;
      }

   }

   @EventTarget
   public void onRenderEntity(EventRenderEntity event) {
      if (modes.isCurrentMode("Shader") && modes.isCurrentMode("Shader") && event.entityData.getEntity() != null) {
         boolean add = true;
         Iterator var4 = this.entities.iterator();

         while(var4.hasNext()) {
            EventRenderEntity.Entity entity = (EventRenderEntity.Entity)var4.next();
            if (entity.getEntity().getName().equalsIgnoreCase(event.entityData.getEntity().getName())) {
               add = false;
            }
         }

         if (add && this.mc.thePlayer != event.entityData.getEntity()) {
            this.entities.add(event.entityData);
         }
      }

   }

   @EventTarget
   public void onRender(EventRender2D event) {
      if (modes.isCurrentMode("Shader")) {
         if (this.mc.gameSettings.ofFastRender) {
            this.set(false);
            ClientUtil.sendClientMessage("Options->Video Settings->Performance->Fast Render->Off", ClientNotification.Type.ERROR);
            return;
         }

         this.outlineShader.startShader();
         float partialTicks = this.mc.timer.renderPartialTicks;
         Minecraft.getMinecraft().entityRenderer.setupCameraTransform(partialTicks, 0);
         RenderItem.field_175058_l = false;
         LayerArmorBase.field_177193_i = true;
         Render.renderNameTag = false;
         Iterator var4 = this.entities.iterator();

         while(var4.hasNext()) {
            EventRenderEntity.Entity entity = (EventRenderEntity.Entity)var4.next();
            Minecraft.getMinecraft().entityRenderer.disableLightmap();
            RenderHelper.disableStandardItemLighting();
            Render entityRender = this.mc.getRenderManager().getEntityRenderObject(entity.getEntity());
            if (entityRender != null) {
               entityRender.doRender(entity.getEntity(), entity.getX(), entity.getY(), entity.getZ(), entity.getEntityYaw(), entity.getPartialTicks());
            }
         }

         Render.renderNameTag = true;
         LayerArmorBase.field_177193_i = false;
         RenderItem.field_175058_l = true;
         this.entities.clear();
         Minecraft.getMinecraft().entityRenderer.disableLightmap();
         RenderHelper.disableStandardItemLighting();
         Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
         this.outlineShader.stopShader();
         Gui.drawRect(0, 0, 0, 0, 0);
      }

   }

   private void doBoxESP(EventRender event) {
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3042);
      GL11.glEnable(2848);
      GL11.glLineWidth(2.0F);
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      Iterator var3 = this.mc.theWorld.loadedEntityList.iterator();

      while(var3.hasNext()) {
         Object o = var3.next();
         if (o instanceof EntityLivingBase) {
            EntityLivingBase e = (EntityLivingBase)o;
            if (this.isValid(e) && !e.isInvisible()) {
               RenderUtil.entityESPBox(e, this.getBoxColor(e), event);
            }
         }
      }

      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
   }

   private Color getBoxColor(EntityLivingBase entity) {
      if (entity.hurtTime > 0) {
         return new Color(192, 57, 43);
      } else {
         return entity != KillAura.curTarget && entity != KillAura.curBot ? new Color(80, 255, 150) : new Color(255, 255, 0);
      }
   }

   private void doOther2DESP() {
      Iterator var2 = this.mc.theWorld.playerEntities.iterator();

      while(true) {
         EntityPlayer entity;
         do {
            do {
               if (!var2.hasNext()) {
                  return;
               }

               entity = (EntityPlayer)var2.next();
            } while(entity == this.mc.thePlayer);
         } while(!this.isValid(entity));

         GL11.glPushMatrix();
         GL11.glEnable(3042);
         GL11.glDisable(2929);
         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
         GlStateManager.enableBlend();
         GL11.glBlendFunc(770, 771);
         GL11.glDisable(3553);
         float partialTicks = this.mc.timer.renderPartialTicks;
         this.mc.getRenderManager();
         double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - RenderManager.renderPosX;
         this.mc.getRenderManager();
         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - RenderManager.renderPosY;
         this.mc.getRenderManager();
         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - RenderManager.renderPosZ;
         float DISTANCE = this.mc.thePlayer.getDistanceToEntity(entity);
         float DISTANCE_SCALE = Math.min(DISTANCE * 0.15F, 0.15F);
         float SCALE = 0.035F;
         SCALE /= 2.0F;
         float xMid = (float)x;
         float yMid = (float)y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F);
         float zMid = (float)z;
         GlStateManager.translate((float)x, (float)y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F), (float)z);
         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
         GL11.glScalef(-SCALE, -SCALE, -SCALE);
         Tessellator tesselator = Tessellator.getInstance();
         WorldRenderer worldRenderer = tesselator.getWorldRenderer();
         float HEALTH = entity.getHealth();
         boolean COLOR = true;
         int COLOR1;
         if ((double)HEALTH > 20.0D) {
            COLOR1 = -65292;
         } else if ((double)HEALTH >= 10.0D) {
            COLOR1 = -16711936;
         } else if ((double)HEALTH >= 3.0D) {
            COLOR1 = -23296;
         } else {
            COLOR1 = -65536;
         }

         new Color(0, 0, 0);
         double thickness = (double)(1.5F + DISTANCE * 0.01F);
         double xLeft = -20.0D;
         double xRight = 20.0D;
         double yUp = 27.0D;
         double yDown = 130.0D;
         double size = 10.0D;
         Color color = new Color(255, 255, 255);
         if (entity.hurtTime > 0) {
            color = new Color(255, 0, 0);
         } else if ((double)this.mc.thePlayer.getDistanceToEntity(entity) < ((Double)KillAura.reach.getValueState()).doubleValue() + 1.0D && ModManager.getModByName("Killaura").isEnabled()) {
            color = new Color(255, 255, 0);
         } else if (entity == KillAura.curTarget && ModManager.getModByName("KillAura").isEnabled()) {
            color = new Color(255, 0, 0);
         }

         RenderHelper.drawBorderedRect((float)xLeft, (float)yUp, (float)xRight, (float)yDown, (float)thickness + 0.5F, Colors.BLACK.c, 0);
         RenderHelper.drawBorderedRect((float)xLeft, (float)yUp, (float)xRight, (float)yDown, (float)thickness, color.getRGB(), 0);
         RenderHelper.drawBorderedRect((float)xLeft - 3.0F - DISTANCE * 0.2F, (float)yDown - (float)(yDown - yUp), (float)xLeft - 2.0F, (float)yDown, 0.15F, Colors.BLACK.c, (new Color(100, 100, 100)).getRGB());
         RenderHelper.drawBorderedRect((float)xLeft - 3.0F - DISTANCE * 0.2F, (float)yDown - (float)(yDown - yUp) * Math.min(1.0F, entity.getHealth() / 20.0F), (float)xLeft - 2.0F, (float)yDown, 0.15F, Colors.BLACK.c, COLOR1);
         RenderHelper.drawBorderedRect((float)xLeft, (float)yDown + 2.0F, (float)xRight, (float)yDown + 3.0F + DISTANCE * 0.2F, 0.15F, Colors.BLACK.c, (new Color(100, 100, 100)).getRGB());
         RenderHelper.drawBorderedRect((float)xLeft, (float)yDown + 2.0F, (float)xLeft + (float)(xRight - xLeft) * Math.min(1.0F, (float)entity.getFoodStats().getFoodLevel() / 20.0F), (float)yDown + 3.0F + DISTANCE * 0.2F, 0.15F, Colors.BLACK.c, (new Color(0, 150, 255)).getRGB());
         GL11.glEnable(3553);
         GL11.glEnable(2929);
         GlStateManager.disableBlend();
         GL11.glDisable(3042);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glNormal3f(1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
      }
   }

   private void doCandyESP() {
      Iterator var2 = this.mc.theWorld.playerEntities.iterator();

      while(true) {
         EntityPlayer entity;
         do {
            do {
               if (!var2.hasNext()) {
                  return;
               }

               entity = (EntityPlayer)var2.next();
            } while(entity == this.mc.thePlayer);
         } while(!this.isValid(entity));

         GL11.glPushMatrix();
         GL11.glEnable(3042);
         GL11.glDisable(2929);
         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
         GlStateManager.enableBlend();
         GL11.glBlendFunc(770, 771);
         GL11.glDisable(3553);
         float partialTicks = this.mc.timer.renderPartialTicks;
         this.mc.getRenderManager();
         double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - RenderManager.renderPosX;
         this.mc.getRenderManager();
         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - RenderManager.renderPosY;
         this.mc.getRenderManager();
         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - RenderManager.renderPosZ;
         float DISTANCE = this.mc.thePlayer.getDistanceToEntity(entity);
         float DISTANCE_SCALE = Math.min(DISTANCE * 0.15F, 0.15F);
         float SCALE = 0.035F;
         SCALE /= 2.0F;
         float xMid = (float)x;
         float yMid = (float)y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F);
         float zMid = (float)z;
         GlStateManager.translate((float)x, (float)y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F), (float)z);
         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
         GL11.glScalef(-SCALE, -SCALE, -SCALE);
         Tessellator tesselator = Tessellator.getInstance();
         WorldRenderer worldRenderer = tesselator.getWorldRenderer();
         new Color(0, 0, 0);
         double thickness = 1.5D;
         double xLeft = -30.0D;
         double xRight = 30.0D;
         double yUp = 15.0D;
         double yDown = 140.0D;
         double size = 10.0D;
         Color color = new Color(0, 255, 0);
         if (entity.hurtTime > 0) {
            color = new Color(255, 0, 0);
         } else if ((double)this.mc.thePlayer.getDistanceToEntity(entity) < ((Double)KillAura.reach.getValueState()).doubleValue() + 1.0D && ModManager.getModByName("Killaura").isEnabled()) {
            color = new Color(255, 255, 0);
         } else if (entity == KillAura.curTarget && ModManager.getModByName("KillAura").isEnabled()) {
            color = new Color(255, 0, 0);
         }

         RenderHelper.drawBorderedRect((float)xLeft, (float)yUp, (float)xRight, (float)yDown, (float)thickness + 3.0F, Colors.BLACK.c, 0);
         RenderHelper.drawBorderedRect((float)xLeft, (float)yUp, (float)xRight, (float)yDown, (float)thickness + 1.0F, color.getRGB(), 0);
         GL11.glEnable(3553);
         GL11.glEnable(2929);
         GlStateManager.disableBlend();
         GL11.glDisable(3042);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glNormal3f(1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
      }
   }

   private void doCornerESP() {
      Iterator var2 = this.mc.theWorld.playerEntities.iterator();

      while(true) {
         EntityPlayer entity;
         do {
            if (!var2.hasNext()) {
               return;
            }

            entity = (EntityPlayer)var2.next();
         } while(entity == this.mc.thePlayer);

         if (!this.isValid(entity)) {
            return;
         }

         GL11.glPushMatrix();
         GL11.glEnable(3042);
         GL11.glDisable(2929);
         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
         GlStateManager.enableBlend();
         GL11.glBlendFunc(770, 771);
         GL11.glDisable(3553);
         float partialTicks = this.mc.timer.renderPartialTicks;
         this.mc.getRenderManager();
         double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - RenderManager.renderPosX;
         this.mc.getRenderManager();
         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - RenderManager.renderPosY;
         this.mc.getRenderManager();
         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - RenderManager.renderPosZ;
         float DISTANCE = this.mc.thePlayer.getDistanceToEntity(entity);
         float DISTANCE_SCALE = Math.min(DISTANCE * 0.15F, 2.5F);
         float SCALE = 0.035F;
         SCALE /= 2.0F;
         GlStateManager.translate((float)x, (float)y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F), (float)z);
         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
         GL11.glScalef(-SCALE, -SCALE, -SCALE);
         Tessellator tesselator = Tessellator.getInstance();
         WorldRenderer worldRenderer = tesselator.getWorldRenderer();
         Color color = new Color(FlatColors.DARK_GREEN.c);
         if (entity.hurtTime > 0) {
            color = new Color(255, 0, 0);
         } else if ((double)this.mc.thePlayer.getDistanceToEntity(entity) < ((Double)KillAura.reach.getValueState()).doubleValue() + 1.0D && ModManager.getModByName("Killaura").isEnabled()) {
            color = new Color(255, 255, 0);
         } else if (entity == KillAura.curTarget && ModManager.getModByName("KillAura").isEnabled()) {
            color = new Color(255, 0, 0);
         }

         Color gray = new Color(0, 0, 0);
         double thickness = (double)(2.0F + DISTANCE * 0.08F);
         double xLeft = -30.0D;
         double xRight = 30.0D;
         double yUp = 20.0D;
         double yDown = 130.0D;
         double size = 10.0D;
         drawVerticalLine(xLeft + size / 2.0D + 1.0D, yUp + 1.0D, size / 2.0D, thickness, gray);
         this.drawHorizontalLine(xLeft + 1.0D, yUp + size + 1.0D, size, thickness, gray);
         drawVerticalLine(xLeft + size / 2.0D, yUp, size / 2.0D, thickness, color);
         this.drawHorizontalLine(xLeft, yUp + size, size, thickness, color);
         drawVerticalLine(xRight - size / 2.0D + 1.0D, yUp + 1.0D, size / 2.0D, thickness, gray);
         this.drawHorizontalLine(xRight + 1.0D, yUp + size + 1.0D, size, thickness, gray);
         drawVerticalLine(xRight - size / 2.0D, yUp, size / 2.0D, thickness, color);
         this.drawHorizontalLine(xRight, yUp + size, size, thickness, color);
         drawVerticalLine(xLeft + size / 2.0D + 1.0D, yDown + 1.0D, size / 2.0D, thickness, gray);
         this.drawHorizontalLine(xLeft + 1.0D, yDown + 1.0D - size, size, thickness, gray);
         drawVerticalLine(xLeft + size / 2.0D, yDown, size / 2.0D, thickness, color);
         this.drawHorizontalLine(xLeft, yDown - size, size, thickness, color);
         drawVerticalLine(xRight - size / 2.0D + 1.0D, yDown + 1.0D, size / 2.0D, thickness, gray);
         this.drawHorizontalLine(xRight + 1.0D, yDown - size + 1.0D, size, thickness, gray);
         drawVerticalLine(xRight - size / 2.0D, yDown, size / 2.0D, thickness, color);
         this.drawHorizontalLine(xRight, yDown - size, size, thickness, color);
         GL11.glEnable(3553);
         GL11.glEnable(2929);
         GlStateManager.disableBlend();
         GL11.glDisable(3042);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glNormal3f(1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
      }
   }

   private static void drawVerticalLine(double xPos, double yPos, double xSize, double thickness, Color color) {
      Tessellator tesselator = Tessellator.getInstance();
      WorldRenderer worldRenderer = tesselator.getWorldRenderer();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
      worldRenderer.pos(xPos - xSize, yPos - thickness / 2.0D, 0.0D).color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F).endVertex();
      worldRenderer.pos(xPos - xSize, yPos + thickness / 2.0D, 0.0D).color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F).endVertex();
      worldRenderer.pos(xPos + xSize, yPos + thickness / 2.0D, 0.0D).color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F).endVertex();
      worldRenderer.pos(xPos + xSize, yPos - thickness / 2.0D, 0.0D).color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F).endVertex();
      tesselator.draw();
   }

   private void drawHorizontalLine(double xPos, double yPos, double ySize, double thickness, Color color) {
      Tessellator tesselator = Tessellator.getInstance();
      WorldRenderer worldRenderer = tesselator.getWorldRenderer();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
      worldRenderer.pos(xPos - thickness / 2.0D, yPos - ySize, 0.0D).color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F).endVertex();
      worldRenderer.pos(xPos - thickness / 2.0D, yPos + ySize, 0.0D).color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F).endVertex();
      worldRenderer.pos(xPos + thickness / 2.0D, yPos + ySize, 0.0D).color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F).endVertex();
      worldRenderer.pos(xPos + thickness / 2.0D, yPos - ySize, 0.0D).color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F).endVertex();
      tesselator.draw();
   }

   private void doOutlineESP(EventRendererLivingEntity event) {
      if (this.mc.thePlayer != event.entity) {
         if (this.isValid(event.entity)) {
            float f6 = event.f1;
            float f5 = event.f2;
            float f8 = event.f3;
            float f2 = event.f4;
            float f7 = event.f5;
            RenderUtil.outlineOne();
            event.rendererLivingEntity.renderModel(event.entity, f6, f5, f8, f2, f7, event.f6);
            RenderUtil.outlineTwo();
            event.rendererLivingEntity.renderModel(event.entity, f6, f5, f8, f2, f7, event.f6);
            RenderUtil.outlineThree();
            event.rendererLivingEntity.renderModel(event.entity, f6, f5, f8, f2, f7, event.f6);
            RenderUtil.outlineFour();
            GL11.glLineWidth(4.0F);
            Color rainbow = Gui.rainbow(System.nanoTime(), 1.0F, 1.0F);
            Color color = ((Boolean)this.rainbowcol.getValueState()).booleanValue() ? new Color((float)rainbow.getRed() / 255.0F, (float)rainbow.getGreen() / 255.0F, (float)rainbow.getBlue() / 255.0F, ((Double)CustomHUDTabGui.alphaSlider.getValueState()).floatValue()) : new Color(FlatColors.DARK_GREEN.c);
            if (event.entity.hurtTime > 0) {
               color = new Color(255, 0, 0);
            } else if ((double)this.mc.thePlayer.getDistanceToEntity(event.entity) < ((Double)KillAura.reach.getValueState()).doubleValue() + 1.0D && ModManager.getModByName("Killaura").isEnabled()) {
               color = new Color(255, 255, 0);
            } else if (event.entity == KillAura.curTarget && ModManager.getModByName("KillAura").isEnabled()) {
               color = new Color(255, 0, 0);
            }

            GL11.glColor3f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F);
            event.rendererLivingEntity.renderModel(event.entity, f6, f5, f8, f2, f7, event.f6);
            RenderUtil.outlineFive();
         }
      }
   }

   private boolean isValid(EntityLivingBase entity) {
      if (entity == this.mc.thePlayer) {
         return false;
      } else if (entity.getHealth() <= 0.0F) {
         return false;
      } else if (entity instanceof EntityPlayer && ((Boolean)this.players.getValueState()).booleanValue()) {
         return true;
      } else if (entity instanceof EntityAnimal && ((Boolean)this.animals.getValueState()).booleanValue()) {
         return true;
      } else {
         return entity instanceof EntityMob && ((Boolean)this.mobs.getValueState()).booleanValue();
      }
   }
}
