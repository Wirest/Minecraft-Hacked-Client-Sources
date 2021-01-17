package me.slowly.client.mod.mods.render;

import com.darkmagician6.eventapi.EventTarget;
import java.awt.Color;
import java.util.Iterator;
import me.slowly.client.events.EventRender;
import me.slowly.client.events.EventRender2D;
import me.slowly.client.events.EventRenderTileEntity;
import me.slowly.client.mod.Mod;
import me.slowly.client.shaders.fragment.ChestShader;
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
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

public class ChestESP extends Mod {
   private Value mode = new Value("ChestESP", "Mode", 0);
   private Value red = new Value("ChestESP_Red", 255.0D, 0.0D, 255.0D, 5.0D);
   private Value green = new Value("ChestESP_Green", 255.0D, 0.0D, 255.0D, 5.0D);
   private Value blue = new Value("ChestESP_Blue", 255.0D, 0.0D, 255.0D, 5.0D);
   private Value rainbowcol = new Value("ChestESP_Rainbow", true);
   private ChestShader outlineShader;

   public ChestESP() {
      super("ChestESP", Mod.Category.RENDER, Colors.YELLOW.c);
      this.mode.mode.add("Outline");
      this.mode.mode.add("Shader");
      this.mode.mode.add("Box");
      this.mode.mode.add("CSGO");
      this.outlineShader = new ChestShader();
      this.showValue = this.mode;
   }

   @EventTarget
   public void onRender(EventRender event) {
      Iterator var3;
      if (this.mode.isCurrentMode("Box")) {
         var3 = this.mc.theWorld.loadedTileEntityList.iterator();

         while(true) {
            TileEntity ent;
            do {
               if (!var3.hasNext()) {
                  return;
               }

               ent = (TileEntity)var3.next();
            } while(!(ent instanceof TileEntityChest) && !(ent instanceof TileEntityEnderChest));

            this.mc.getRenderManager();
            double x = (double)ent.getPos().getX() - RenderManager.renderPosX;
            this.mc.getRenderManager();
            double y = (double)ent.getPos().getY() - RenderManager.renderPosY;
            this.mc.getRenderManager();
            double z = (double)ent.getPos().getZ() - RenderManager.renderPosZ;
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            Color rainbow = Gui.rainbow(System.nanoTime(), 1.0F, 1.0F);
            GL11.glColor4f(((Boolean)this.rainbowcol.getValueState()).booleanValue() ? (float)rainbow.getRed() / 255.0F : ((Double)this.red.getValueState()).floatValue() / 255.0F, ((Boolean)this.rainbowcol.getValueState()).booleanValue() ? (float)rainbow.getGreen() / 255.0F : ((Double)this.green.getValueState()).floatValue() / 255.0F, ((Boolean)this.rainbowcol.getValueState()).booleanValue() ? (float)rainbow.getBlue() / 255.0F : ((Double)this.blue.getValueState()).floatValue() / 255.0F, 0.25F);
            RenderUtil.drawBoundingBox(new AxisAlignedBB(x + ent.getBlockType().getBlockBoundsMinX(), y + ent.getBlockType().getBlockBoundsMinY(), z + ent.getBlockType().getBlockBoundsMinZ(), x + ent.getBlockType().getBlockBoundsMaxX(), y + ent.getBlockType().getBlockBoundsMaxY(), z + ent.getBlockType().getBlockBoundsMaxZ()));
            GL11.glDisable(2848);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
         }
      } else if (this.mode.isCurrentMode("CSGO")) {
         var3 = this.mc.theWorld.loadedTileEntityList.iterator();

         while(var3.hasNext()) {
            Object o = var3.next();
            if (o instanceof TileEntityChest) {
               TileEntityChest chest = (TileEntityChest)o;
               GlStateManager.pushMatrix();
               GlStateManager.translate((double)chest.getPos().getX() - RenderManager.renderPosX + 0.5D, (double)chest.getPos().getY() - RenderManager.renderPosY + 0.5D, (double)chest.getPos().getZ() - RenderManager.renderPosZ + 0.5D);
               GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
               float SCALE = 0.03F;
               GL11.glScalef(-SCALE, -SCALE, -SCALE);
               GlStateManager.disableDepth();
               GlStateManager.disableLighting();
               Gui.drawRect(-23, -23, 23, -18, Colors.BLACK.c);
               Gui.drawRect(-23, 21, 23, 26, Colors.BLACK.c);
               Gui.drawRect(18, 25, 23, -22, Colors.BLACK.c);
               Gui.drawRect(-18, 25, -23, -22, Colors.BLACK.c);
               Gui.drawRect(-22, -22, 22, -19, Colors.RED.c);
               Gui.drawRect(-22, 22, 22, 25, Colors.RED.c);
               Gui.drawRect(19, 22, 22, -19, Colors.RED.c);
               Gui.drawRect(-19, 22, -22, -19, Colors.RED.c);
               GlStateManager.enableDepth();
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.popMatrix();
            }
         }
      }

   }

   @EventTarget
   public void onTileRender(EventRenderTileEntity event) {
      if (this.mode.isCurrentMode("Outline")) {
         this.setColor(Colors.DARKMAGENTA.c);
         TileEntity tileentityIn = event.getTileentityIn();
         float partialTicks = event.getPartialTicks();
         int destroyStage = event.getDestroyStage();
         BlockPos blockpos = tileentityIn.getPos();
         if (!(tileentityIn instanceof TileEntityChest)) {
            return;
         }

         RenderUtil.checkSetupFBO();
         event.getTileEntityRendererDispatcher().renderTileEntityAt(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks, destroyStage);
         RenderUtil.outlineOne();
         event.getTileEntityRendererDispatcher().renderTileEntityAt(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks, destroyStage);
         RenderUtil.outlineTwo();
         event.getTileEntityRendererDispatcher().renderTileEntityAt(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks, destroyStage);
         RenderUtil.outlineThree();
         RenderUtil.outlineFour();
         GL11.glLineWidth(2.0F);
         Color rainbow = Gui.rainbow(System.nanoTime(), 1.0F, 1.0F);
         Color color = ((Boolean)this.rainbowcol.getValueState()).booleanValue() ? new Color((float)rainbow.getRed() / 255.0F, (float)rainbow.getGreen() / 255.0F, (float)rainbow.getBlue() / 255.0F, ((Double)CustomHUDTabGui.alphaSlider.getValueState()).floatValue()) : new Color(FlatColors.ORANGE.c);
         RenderUtil.color(((Boolean)this.rainbowcol.getValueState()).booleanValue() ? color.getRGB() : (new Color(((Double)this.red.getValueState()).floatValue() / 255.0F, ((Double)this.green.getValueState()).floatValue() / 255.0F, ((Double)this.blue.getValueState()).floatValue() / 255.0F)).getRGB());
         event.getTileEntityRendererDispatcher().renderTileEntityAt(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks, destroyStage);
         RenderUtil.outlineFive();
         event.cancel = true;
      }

   }

   @EventTarget
   public void onRender(EventRender2D event) {
      if (this.mode.isCurrentMode("Shader")) {
         if (this.mc.gameSettings.ofFastRender) {
            this.set(false);
            ClientUtil.sendClientMessage("Options->Video Settings->Performance->Fast Render->Off", ClientNotification.Type.ERROR);
            return;
         }

         this.outlineShader.startShader();
         float partialTicks = this.mc.timer.renderPartialTicks;
         Minecraft.getMinecraft().entityRenderer.setupCameraTransform(partialTicks, 0);
         Iterator var4 = this.mc.theWorld.loadedTileEntityList.iterator();

         while(var4.hasNext()) {
            Object o = var4.next();
            TileEntity chest = (TileEntity)o;
            if (chest instanceof TileEntityChest) {
               Minecraft.getMinecraft().entityRenderer.disableLightmap();
               TileEntityRendererDispatcher.instance.renderTileEntityAt(chest, (double)chest.getPos().getX() - RenderManager.renderPosX, (double)chest.getPos().getY() - RenderManager.renderPosY, (double)chest.getPos().getZ() - RenderManager.renderPosZ, partialTicks);
            }
         }

         Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
         this.outlineShader.stopShader();
         Gui.drawRect(0, 0, 0, 0, 0);
      }

   }

   public void onDisable() {
      this.outlineShader.deleteShader();
      super.onDisable();
      ClientUtil.sendClientMessage("ChestESP Disable", ClientNotification.Type.ERROR);
   }
   public void onEnable() {
   	super.isEnabled();
      ClientUtil.sendClientMessage("ChestESP Enable", ClientNotification.Type.SUCCESS);
   }
}
