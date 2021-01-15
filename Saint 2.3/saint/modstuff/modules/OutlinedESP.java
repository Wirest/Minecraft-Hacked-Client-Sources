package saint.modstuff.modules;

import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.RenderIn3D;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Camera;
import saint.utilities.Logger;
import saint.utilities.Stencil;
import saint.valuestuff.Value;

public class OutlinedESP extends Module {
   private final Value entities = new Value("outlinedesp_entities", true);
   private final Value chests = new Value("outlinedesp_chests", true);
   private final Value width = new Value("outlinedesp_linewidth", 3.0F);

   public OutlinedESP() {
      super("OutlinedESP", -1, ModManager.Category.RENDER);
      this.setTag("Outlined ESP");
      Saint.getCommandManager().getContentList().add(new Command("outlinedesp", "<entities/chests>", new String[0]) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("entities")) {
               OutlinedESP.this.entities.setValueState(!(Boolean)OutlinedESP.this.entities.getValueState());
               Logger.writeChat("Outlined ESP will " + ((Boolean)OutlinedESP.this.entities.getValueState() ? "now" : "no longer") + " outline entities.");
            } else if (message.split(" ")[1].equalsIgnoreCase("chests")) {
               OutlinedESP.this.chests.setValueState(!(Boolean)OutlinedESP.this.chests.getValueState());
               Logger.writeChat("Outlined ESP will " + ((Boolean)OutlinedESP.this.chests.getValueState() ? "now" : "no longer") + " outline chests.");
            } else {
               Logger.writeChat("Option not valid! Available options: entities, chests.");
            }

         }
      });
      Saint.getCommandManager().getContentList().add(new Command("outlinedesplinewidth", "<width>", new String[]{"esplinewidth", "oelw"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               OutlinedESP.this.width.setValueState((Float)OutlinedESP.this.width.getDefaultValue());
            } else {
               OutlinedESP.this.width.setValueState(Float.parseFloat(message.split(" ")[1]));
            }

            if ((Float)OutlinedESP.this.width.getValueState() > 15.0F) {
               OutlinedESP.this.width.setValueState(15.0F);
            } else if ((Float)OutlinedESP.this.width.getValueState() < 1.0F) {
               OutlinedESP.this.width.setValueState(1.0F);
            }

            Logger.writeChat("Outliend ESP Line Width set to: " + OutlinedESP.this.width.getValueState());
         }
      });
   }

   public void onEvent(Event event) {
      if (event instanceof RenderIn3D && mc.theWorld != null) {
         checkSetupFBO();
         if ((Boolean)this.entities.getValueState()) {
            this.drawEntityOutlines(((RenderIn3D)event).getPartialTicks());
         }

         if ((Boolean)this.chests.getValueState()) {
            this.drawChestOutlines(((RenderIn3D)event).getPartialTicks());
         }

         mc.getFramebuffer().bindFramebuffer(true);
         mc.getFramebuffer().bindFramebufferTexture();
      }

   }

   public static void checkSetupFBO() {
      Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
      if (fbo != null && fbo.depthBuffer > -1) {
         setupFBO(fbo);
         fbo.depthBuffer = -1;
      }

   }

   public static void setupFBO(Framebuffer fbo) {
      EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
      int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
      EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
      EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
      EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
      EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
   }

   public void drawEntityOutlines(float partialTicks) {
      int entityDispList = GL11.glGenLists(1);
      Stencil.getInstance().startLayer();
      GL11.glPushMatrix();
      mc.entityRenderer.setupCameraTransform(partialTicks, 0);
      GL11.glMatrixMode(5888);
      RenderHelper.enableStandardItemLighting();
      GL11.glEnable(2884);
      Camera playerCam = new Camera(Minecraft.getMinecraft().thePlayer);
      Frustrum frustrum = new Frustrum();
      frustrum.setPosition(playerCam.getPosX(), playerCam.getPosY(), playerCam.getPosZ());
      GL11.glDisable(2929);
      GL11.glDepthMask(true);
      Stencil.getInstance().setBuffer(true);
      GL11.glNewList(entityDispList, 4864);
      Iterator var6 = Minecraft.getMinecraft().theWorld.loadedEntityList.iterator();

      while(true) {
         Entity entity;
         do {
            if (!var6.hasNext()) {
               GL11.glEndList();
               GL11.glPolygonMode(1032, 6913);
               GL11.glCallList(entityDispList);
               GL11.glPolygonMode(1032, 6912);
               GL11.glCallList(entityDispList);
               Stencil.getInstance().setBuffer(false);
               GL11.glPolygonMode(1032, 6914);
               GL11.glCallList(entityDispList);
               Stencil.getInstance().cropInside();
               GL11.glPolygonMode(1032, 6913);
               GL11.glCallList(entityDispList);
               GL11.glPolygonMode(1032, 6912);
               GL11.glCallList(entityDispList);
               GL11.glPolygonMode(1032, 6914);
               Minecraft.getMinecraft().entityRenderer.func_175072_h();
               RenderHelper.disableStandardItemLighting();
               Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
               Stencil.getInstance().stopLayer();
               GL11.glDisable(2960);
               GL11.glPopMatrix();
               Minecraft.getMinecraft().entityRenderer.func_175072_h();
               RenderHelper.disableStandardItemLighting();
               Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
               GL11.glDeleteLists(entityDispList, 1);
               return;
            }

            Object obj = var6.next();
            entity = (Entity)obj;
         } while(entity == Minecraft.getMinecraft().thePlayer);

         GL11.glLineWidth((Float)this.width.getValueState());
         GL11.glEnable(3042);
         GL11.glEnable(2848);
         Camera entityCam = new Camera(entity);
         GL11.glPushMatrix();
         GL11.glDisable(3553);
         GL11.glTranslated(entityCam.getPosX() - playerCam.getPosX(), entityCam.getPosY() - playerCam.getPosY(), entityCam.getPosZ() - playerCam.getPosZ());
         Render entityRender = mc.getRenderManager().getEntityRenderObject(entity);
         if (entityRender != null) {
            float distance = mc.thePlayer.getDistanceToEntity(entity);
            if (entity instanceof EntityLivingBase) {
               if (entity instanceof EntityPlayer && Saint.getFriendManager().isFriend(entity.getName())) {
                  GL11.glColor4f(0.92F, 0.72F, 0.0F, 1.0F);
               } else if (Saint.getModuleManager().getModuleUsingName("paralyze").isEnabled() && (double)distance <= 0.5D) {
                  GL11.glColor4f(2.55F, 2.55F, 0.0F, 1.0F);
               } else if (((EntityLivingBase)entity).hurtTime > 0) {
                  GL11.glColor4f(2.55F, 0.0F, 0.0F, 1.0F);
               } else if (entity != KillAura.lastAttackedTarget && entity != KillAura.lastAttackedTarget2) {
                  GL11.glColor4f(0.0F, 2.55F, 2.55F, 1.0F);
               } else {
                  GL11.glColor4f(0.0F, 0.4F, 2.55F, 1.0F);
               }

               RendererLivingEntity.shouldRenderParts = false;
               entityRender.doRender(entity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
               RendererLivingEntity.shouldRenderParts = true;
            }
         }

         GL11.glEnable(3553);
         GL11.glPopMatrix();
      }
   }

   public void drawChestOutlines(float partialTicks) {
      int entityDispList = GL11.glGenLists(1);
      Stencil.getInstance().startLayer();
      GL11.glPushMatrix();
      mc.entityRenderer.setupCameraTransform(partialTicks, 0);
      GL11.glMatrixMode(5888);
      RenderHelper.enableStandardItemLighting();
      GL11.glEnable(2884);
      Camera playerCam = new Camera(Minecraft.getMinecraft().thePlayer);
      Frustrum frustrum = new Frustrum();
      frustrum.setPosition(playerCam.getPosX(), playerCam.getPosY(), playerCam.getPosZ());
      GL11.glDisable(2929);
      GL11.glDepthMask(true);
      Stencil.getInstance().setBuffer(true);
      GL11.glNewList(entityDispList, 4864);
      Iterator var6 = Minecraft.getMinecraft().theWorld.loadedTileEntityList.iterator();

      while(var6.hasNext()) {
         Object obj = var6.next();
         TileEntity entity = (TileEntity)obj;
         if (entity instanceof TileEntityLockable) {
            GL11.glLineWidth((Float)this.width.getValueState());
            GL11.glEnable(3042);
            GL11.glEnable(2848);
            GL11.glDisable(3553);
            GL11.glPushMatrix();
            GL11.glTranslated((double)entity.getPos().getX() - RenderManager.renderPosX, (double)entity.getPos().getY() - RenderManager.renderPosY, (double)entity.getPos().getZ() - RenderManager.renderPosZ);
            GL11.glColor4f(2.18F, 1.65F, 0.32F, 1.0F);
            TileEntityRendererDispatcher.instance.renderTileEntityAt(entity, 0.0D, 0.0D, 0.0D, partialTicks);
            GL11.glEnable(3553);
            GL11.glPopMatrix();
         }
      }

      GL11.glEndList();
      GL11.glPolygonMode(1032, 6913);
      GL11.glCallList(entityDispList);
      GL11.glPolygonMode(1032, 6912);
      GL11.glCallList(entityDispList);
      Stencil.getInstance().setBuffer(false);
      GL11.glPolygonMode(1032, 6914);
      GL11.glCallList(entityDispList);
      Stencil.getInstance().cropInside();
      GL11.glPolygonMode(1032, 6913);
      GL11.glCallList(entityDispList);
      GL11.glPolygonMode(1032, 6912);
      GL11.glCallList(entityDispList);
      GL11.glPolygonMode(1032, 6914);
      Minecraft.getMinecraft().entityRenderer.func_175072_h();
      RenderHelper.disableStandardItemLighting();
      Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
      Stencil.getInstance().stopLayer();
      GL11.glDisable(2960);
      GL11.glPopMatrix();
      Minecraft.getMinecraft().entityRenderer.func_175072_h();
      RenderHelper.disableStandardItemLighting();
      Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
      GL11.glDeleteLists(entityDispList, 1);
   }
}
