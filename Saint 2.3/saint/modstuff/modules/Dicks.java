package saint.modstuff.modules;

import java.util.Iterator;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.RenderIn3D;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.valuestuff.Value;

public class Dicks extends Module {
   private final Value dance = new Value("dicks_dance", true);
   private int amount = 0;
   private float spin;
   private float cumSize;

   public Dicks() {
      super("Dicks", -16181, ModManager.Category.RENDER);
      Saint.getCommandManager().getContentList().add(new Command("dicksdance", "none", new String[]{"dancedicks", "dd"}) {
         public void run(String message) {
            Dicks.this.dance.setValueState(!(Boolean)Dicks.this.dance.getValueState());
            Logger.writeChat("Dicks will " + ((Boolean)Dicks.this.dance.getValueState() ? "now" : "no longer") + " dance.");
         }
      });
   }

   public void onEvent(Event event) {
      if (event instanceof RenderIn3D) {
         Iterator var3 = mc.theWorld.loadedEntityList.iterator();

         while(var3.hasNext()) {
            Object o = var3.next();
            if (o instanceof EntityPlayer) {
               EntityPlayer player = (EntityPlayer)o;
               if (player != mc.thePlayer) {
                  double var10000 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)mc.timer.renderPartialTicks;
                  mc.getRenderManager();
                  double x = var10000 - RenderManager.renderPosX;
                  var10000 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)mc.timer.renderPartialTicks;
                  mc.getRenderManager();
                  double y = var10000 - RenderManager.renderPosY;
                  var10000 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)mc.timer.renderPartialTicks;
                  mc.getRenderManager();
                  double z = var10000 - RenderManager.renderPosZ;
                  GL11.glPushMatrix();
                  RenderHelper.disableStandardItemLighting();
                  this.esp(player, x, y, z);
                  RenderHelper.enableStandardItemLighting();
                  GL11.glPopMatrix();
               }
            }

            ++this.amount;
            if (this.amount > 25) {
               ++this.spin;
               if (this.spin > 50.0F) {
                  this.spin = -50.0F;
               } else if (this.spin < -50.0F) {
                  this.spin = 50.0F;
               }

               this.amount = 0;
            }

            ++this.cumSize;
            if (this.cumSize > 180.0F) {
               this.cumSize = -180.0F;
            } else if (this.cumSize < -180.0F) {
               this.cumSize = 180.0F;
            }
         }
      }

   }

   public void esp(EntityPlayer player, double x, double y, double z) {
      GL11.glDisable(2896);
      GL11.glDisable(3553);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(2929);
      GL11.glEnable(2848);
      GL11.glDepthMask(true);
      GL11.glLineWidth(1.0F);
      GL11.glTranslated(x, y, z);
      GL11.glRotatef(-player.rotationYaw, 0.0F, player.height, 0.0F);
      GL11.glTranslated(-x, -y, -z);
      GL11.glTranslated(x, y + (double)(player.height / 2.0F) - 0.22499999403953552D, z);
      GL11.glColor4f(1.38F, 0.55F, 2.38F, 1.0F);
      GL11.glRotatef((float)(player.isSneaking() ? 35 : 0) + this.spin, 1.0F + this.spin, 0.0F + ((Boolean)this.dance.getValueState() ? this.cumSize : 0.0F), 0.0F + ((Boolean)this.dance.getValueState() ? this.cumSize : 0.0F));
      int lines = 20;
      GL11.glTranslated(0.0D, 0.0D, 0.07500000298023224D);
      Cylinder shaft = new Cylinder();
      shaft.setDrawStyle(100013);
      shaft.draw(0.1F, 0.11F, 0.4F, 25, lines);
      GL11.glColor4f(1.38F, 0.85F, 1.38F, 1.0F);
      GL11.glTranslated(0.0D, 0.0D, -0.12500000298023223D);
      GL11.glTranslated(-0.09000000074505805D, 0.0D, 0.0D);
      Sphere right = new Sphere();
      right.setDrawStyle(100013);
      right.draw(0.14F, 10, lines);
      GL11.glTranslated(0.16000000149011612D, 0.0D, 0.0D);
      Sphere left = new Sphere();
      left.setDrawStyle(100013);
      left.draw(0.14F, 10, lines);
      GL11.glColor4f(1.35F, 0.0F, 0.0F, 1.0F);
      GL11.glTranslated(-0.07000000074505806D, 0.0D, 0.589999952316284D);
      Sphere tip = new Sphere();
      tip.setDrawStyle(100013);
      tip.draw(0.13F, 15, lines);
      GL11.glDepthMask(true);
      GL11.glDisable(2848);
      GL11.glEnable(2929);
      GL11.glDisable(3042);
      GL11.glEnable(2896);
      GL11.glEnable(3553);
   }
}
