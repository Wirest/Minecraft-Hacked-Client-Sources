package saint.modstuff.modules;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.RenderTracers;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.ListenerUtil;
import saint.utilities.Logger;
import saint.utilities.RenderHelper;
import saint.valuestuff.Value;

public class PositionHelper extends Module {
   private final Value tracer = new Value("positionhelper_tracer", false);
   private final Value box = new Value("positionhelper_box", false);

   public PositionHelper() {
      super("PositionHelper", -10185235, ModManager.Category.RENDER);
      this.setTag("Position Helper");
      Saint.getCommandManager().getContentList().add(new Command("positionhelper", "<tracer/box>", new String[0]) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("tracer")) {
               PositionHelper.this.tracer.setValueState(!(Boolean)PositionHelper.this.tracer.getValueState());
               Logger.writeChat("Position Helper will " + ((Boolean)PositionHelper.this.tracer.getValueState() ? "now" : "no longer") + " draw a tracer.");
            } else if (message.split(" ")[1].equalsIgnoreCase("box")) {
               PositionHelper.this.box.setValueState(!(Boolean)PositionHelper.this.box.getValueState());
               Logger.writeChat("Position Helper will " + ((Boolean)PositionHelper.this.box.getValueState() ? "now" : "no longer") + " draw a box.");
            } else {
               Logger.writeChat("Option not valid! Available options: tracer, box.");
            }

         }
      });
   }

   public void onEnabled() {
      super.onEnabled();
   }

   public void onEvent(Event event) {
      if (event instanceof RenderTracers) {
         Saint.getListenerUtil();
         double rX = ListenerUtil.serverPos.posX;
         Saint.getListenerUtil();
         double rY = ListenerUtil.serverPos.posY + (double)mc.thePlayer.getEyeHeight();
         Saint.getListenerUtil();
         double rZ = ListenerUtil.serverPos.posZ;
         Saint.getListenerUtil();
         float rotYaw = ListenerUtil.serverPos.rotationYaw;
         Saint.getListenerUtil();
         float rotPitch = ListenerUtil.serverPos.rotationPitch;
         mc.getRenderManager();
         rX -= RenderManager.renderPosX;
         mc.getRenderManager();
         rY -= RenderManager.renderPosY;
         mc.getRenderManager();
         rZ -= RenderManager.renderPosZ;
         float var3 = MathHelper.cos(-rotYaw * 0.01745329F - 3.141593F);
         float var4 = MathHelper.sin(-rotYaw * 0.01745329F - 3.141593F);
         float var5 = -MathHelper.cos(-rotPitch * 0.01745329F);
         float var6 = MathHelper.sin(-rotPitch * 0.01745329F);
         Vec3 lookVec = (new Vec3((double)(var4 * var5), (double)var6, (double)(var3 * var5))).normalize();
         OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
         GL11.glEnable(3553);
         OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 200.0F, 0.0F);
         GL11.glDisable(2896);
         GL11.glEnable(2848);
         GL11.glPushMatrix();
         GL11.glColor4f(2.55F, 2.55F, 0.0F, 1.0F);
         GL11.glDisable(3553);
         GL11.glDepthMask(false);
         GL11.glBlendFunc(770, 771);
         GL11.glEnable(3042);
         GL11.glLineWidth(1.0F);
         if ((Boolean)this.tracer.getValueState()) {
            GL11.glBegin(1);
            GL11.glVertex3d(rX, rY, rZ);
            GL11.glVertex3d(rX + lookVec.xCoord * 100.0D, rY + lookVec.yCoord * 100.0D, rZ + lookVec.zCoord * 100.0D);
            GL11.glEnd();
         }

         if ((Boolean)this.box.getValueState()) {
            AxisAlignedBB box = AxisAlignedBB.fromBounds(rX - (double)mc.thePlayer.width, rY - (double)mc.thePlayer.getEyeHeight(), rZ - (double)mc.thePlayer.width, rX + (double)mc.thePlayer.width, rY - (double)mc.thePlayer.getEyeHeight() + (double)mc.thePlayer.height + 0.2D, rZ + (double)mc.thePlayer.width);
            GL11.glColor4f(2.55F, 0.0F, 0.0F, 0.11F);
            RenderHelper.drawFilledBox(box);
            GL11.glColor4f(2.55F, 2.55F, 0.0F, 1.0F);
            RenderHelper.drawOutlinedBoundingBox(box);
         }

         GL11.glDisable(3042);
         GL11.glDepthMask(true);
         GL11.glEnable(3553);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glDisable(2848);
         GL11.glPopMatrix();
      }

   }
}
