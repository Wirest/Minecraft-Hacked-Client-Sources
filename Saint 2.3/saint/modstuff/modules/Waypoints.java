package saint.modstuff.modules;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.vecmath.Point3d;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.RenderIn3D;
import saint.eventstuff.events.RenderTracers;
import saint.modstuff.Module;
import saint.modstuff.modules.addons.Point;
import saint.utilities.GLUtil;
import saint.utilities.Logger;
import saint.utilities.NahrFont;
import saint.utilities.RenderHelper;

public class Waypoints extends Module {
   private float[] pointColor;
   public List points = new CopyOnWriteArrayList();

   public Waypoints() {
      super("Waypoints");
      this.setEnabled(true);
      Saint.getCommandManager().getContentList().add(new Command("addwaypointhere", "<name>", new String[]{"addwayhere", "addwh"}) {
         private boolean isInteger(String text) {
            try {
               Integer.parseInt(text);
               return true;
            } catch (Exception var3) {
               return false;
            }
         }

         public void run(String message) {
            String[] arguments = message.split(" ");
            int x = (int)mc.thePlayer.posX;
            int y = (int)mc.thePlayer.posY;
            int z = (int)mc.thePlayer.posZ;
            String name = message.substring(12);
            String server;
            if (mc.getCurrentServerData() == null) {
               server = "singleplayer";
            } else {
               server = mc.getCurrentServerData().serverIP;
            }

            Waypoints.this.points.add(new Point(name, server, x, y, z));
            Logger.writeChat("Waypoint \"" + name + "\" added at " + x + ", " + y + ", " + z);
            Saint.getFileManager().getFileUsingName("waypointsconfiguration").saveFile();
         }
      });
      Saint.getCommandManager().getContentList().add(new Command("addwaypoint", "<x> <y> <z> <name>", new String[]{"addway", "addw"}) {
         private boolean isInteger(String text) {
            try {
               Integer.parseInt(text);
               return true;
            } catch (Exception var3) {
               return false;
            }
         }

         public void run(String message) {
            String[] arguments = message.split(" ");
            if (this.isInteger(arguments[1])) {
               int x = Integer.parseInt(arguments[1]);
               int y = Integer.parseInt(arguments[2]);
               int z = Integer.parseInt(arguments[3]);
               String name = message.substring((arguments[0] + " " + arguments[1] + " " + arguments[2] + " " + arguments[3] + " ").length());
               String server;
               if (mc.getCurrentServerData() == null) {
                  server = "singleplayer";
               } else {
                  server = mc.getCurrentServerData().serverIP;
               }

               Waypoints.this.points.add(new Point(name, server, x, y, z));
               Saint.getFileManager().getFileUsingName("waypointsconfiguration").saveFile();
               Logger.writeChat("Waypoint \"" + name + "\" added at " + x + ", " + y + ", " + z);
            }

            Saint.getFileManager().getFileUsingName("waypointsconfiguration").saveFile();
         }
      });
      Saint.getCommandManager().getContentList().add(new Command("delwaypoint", "<name>", new String[]{"delwayp", "dwp"}) {
         public void run(String message) {
            String[] arguments = message.split(" ");
            String name = message.substring((arguments[0] + " ").length());
            boolean found = false;
            Iterator var6 = Waypoints.this.points.iterator();

            while(var6.hasNext()) {
               Point point = (Point)var6.next();
               if (point.getName().toLowerCase().startsWith(name.toLowerCase())) {
                  Waypoints.this.points.remove(point);
                  Saint.getFileManager().getFileUsingName("waypointsconfiguration").saveFile();
                  Logger.writeChat("Waypoint \"" + point.getName() + "\" deleted.");
                  found = true;
               }
            }

            if (!found) {
               Logger.writeChat("Waypoint \"" + name + "\" not found.");
            }

            Saint.getFileManager().getFileUsingName("waypointsconfiguration").saveFile();
         }
      });
      this.pointColor = new float[]{0.32F, 0.32F, 0.32F};
   }

   public final List getPoints() {
      return this.points;
   }

   public void onEvent(Event event) {
      Point waypoint;
      Iterator var3;
      String server;
      if (event instanceof RenderIn3D) {
         GL11.glPushMatrix();
         GL11.glDisable(3553);
         GL11.glDisable(2896);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glDisable(2929);
         GL11.glEnable(2848);
         GL11.glDepthMask(false);
         GL11.glLineWidth(1.0F);
         var3 = this.points.iterator();

         while(var3.hasNext()) {
            waypoint = (Point)var3.next();
            if (mc.getCurrentServerData() == null) {
               server = "singleplayer";
            } else {
               server = mc.getCurrentServerData().serverIP;
            }

            if (waypoint.getServer().equalsIgnoreCase(server)) {
               this.renderPoint(waypoint);
               Point3d point = new Point3d((double)waypoint.getX() + 0.5D, (double)waypoint.getY() + 0.5D, (double)waypoint.getZ() + 0.5D);
               if (mc.thePlayer.getDistanceSq(point.getX(), point.getY(), point.getZ()) > 1089.0D) {
                  point = this.infDistanceToXAway(new Point3d((double)waypoint.getX() + 0.5D, (double)waypoint.getY() + 0.5D, (double)waypoint.getZ() + 0.5D), 32.0D);
               }

               String var10001 = waypoint.getName() + " " + (double)Math.round(mc.thePlayer.getDistance((double)waypoint.getX(), (double)waypoint.getY(), (double)waypoint.getZ()) * 10.0D) / 10.0D + "m";
               double var10002 = point.getX();
               mc.getRenderManager();
               var10002 -= RenderManager.renderPosX;
               double var10003 = point.getY() - 0.5D;
               mc.getRenderManager();
               var10003 -= RenderManager.renderPosY;
               double var10004 = point.getZ();
               mc.getRenderManager();
               this.renderTag(var10001, var10002, var10003, var10004 - RenderManager.renderPosZ);
            }
         }

         GL11.glDepthMask(true);
         GL11.glDisable(2848);
         GL11.glEnable(2929);
         GL11.glEnable(2896);
         GL11.glDisable(3042);
         GL11.glEnable(3553);
         GL11.glPopMatrix();
      } else if (event instanceof RenderTracers) {
         GL11.glPushMatrix();
         GL11.glDisable(3553);
         GL11.glDisable(2896);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glDisable(2929);
         GL11.glEnable(2848);
         GL11.glDepthMask(false);
         GL11.glLineWidth(1.0F);
         var3 = this.points.iterator();

         while(var3.hasNext()) {
            waypoint = (Point)var3.next();
            if (mc.getCurrentServerData() == null) {
               server = "singleplayer";
            } else {
               server = mc.getCurrentServerData().serverIP;
            }

            if (waypoint.getServer().equalsIgnoreCase(server)) {
               this.renderTracer(waypoint);
            }
         }

         GL11.glDepthMask(true);
         GL11.glDisable(2848);
         GL11.glEnable(2929);
         GL11.glEnable(2896);
         GL11.glDisable(3042);
         GL11.glEnable(3553);
         GL11.glPopMatrix();
      }

   }

   private Point3d infDistanceToXAway(Point3d point, double distance) {
      float[] angles = this.getAnglesXYZ(point.getX(), point.getY(), point.getZ());
      Vec3 lookVec = this.getLookVec(angles[0], angles[1]);
      return new Point3d(RenderHelper.interpPlayerX() + lookVec.xCoord * distance, RenderHelper.interpPlayerY() + lookVec.yCoord * distance, RenderHelper.interpPlayerZ() + lookVec.zCoord * distance);
   }

   private Vec3 getLookVec(float yaw, float pitch) {
      float var3 = MathHelper.cos(-yaw * 0.01745329F - 3.141593F);
      float var4 = MathHelper.sin(-yaw * 0.01745329F - 3.141593F);
      float var5 = -MathHelper.cos(-pitch * 0.01745329F);
      float var6 = MathHelper.sin(-pitch * 0.01745329F);
      return (new Vec3((double)(var4 * var5), (double)var6, (double)(var3 * var5))).normalize();
   }

   public float getYawChangeToEntityXYZ(double posX, double posY, double posZ) {
      double deltaX = posX - RenderHelper.interpPlayerX();
      double deltaZ = posZ - RenderHelper.interpPlayerZ();
      double yawToEntity;
      if (deltaZ < 0.0D && deltaX < 0.0D) {
         yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
      } else if (deltaZ < 0.0D && deltaX > 0.0D) {
         yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
      } else {
         yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
      }

      return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float)yawToEntity));
   }

   public float getPitchChangeToEntityXYZ(double posX, double posY, double posZ) {
      double deltaX = posX - RenderHelper.interpPlayerX();
      double deltaZ = posZ - RenderHelper.interpPlayerZ();
      double deltaY = posY - RenderHelper.interpPlayerY();
      double distanceXZ = (double)MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
      double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
      return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float)pitchToEntity);
   }

   public float[] getAnglesXYZ(double posX, double posY, double posZ) {
      return new float[]{this.getYawChangeToEntityXYZ(posX, posY, posZ) + mc.thePlayer.rotationYaw, this.getPitchChangeToEntityXYZ(posX, posY, posZ) + mc.thePlayer.rotationPitch};
   }

   private void renderTracer(Point point) {
      double x = (double)point.getX() - RenderManager.renderPosX;
      double y = (double)point.getY() - RenderManager.renderPosY;
      double z = (double)point.getZ() - RenderManager.renderPosZ;
      GL11.glColor4f(this.pointColor[0], this.pointColor[1], this.pointColor[2], 1.0F);
      GL11.glBegin(2);
      GL11.glVertex3d(0.0D, (double)mc.thePlayer.getEyeHeight(), 0.0D);
      GL11.glVertex3d(x + 0.5D, y, z + 0.5D);
      GL11.glEnd();
   }

   private void renderPoint(Point point) {
      double x = (double)point.getX() - RenderManager.renderPosX;
      double y = (double)point.getY() - RenderManager.renderPosY;
      double z = (double)point.getZ() - RenderManager.renderPosZ;
      AxisAlignedBB box = AxisAlignedBB.fromBounds(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D);
      GL11.glColor4f(this.pointColor[0], this.pointColor[1], this.pointColor[2], 0.2F);
      RenderHelper.drawFilledBox(box);
      GL11.glColor4f(this.pointColor[0], this.pointColor[1], this.pointColor[2], 0.8F);
      RenderHelper.drawOutlinedBoundingBox(box);
      double dist = mc.thePlayer.getDistance((double)point.getX(), (double)point.getY(), (double)point.getZ()) / 3.0D;
      float var13 = (float)(dist <= 3.0D ? 3.0D : dist);
      float var14 = 0.016666668F * var13;
      GL11.glPushMatrix();
      GL11.glTranslatef((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      GL11.glScalef(-var14, -var14, var14);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      byte var16 = false;
      String text = point.getName() + " " + (int)Math.round(mc.thePlayer.getDistance((double)point.getX(), (double)point.getY(), (double)point.getZ())) + "m";
      float var17 = RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(text)) / 2.0F;
      GL11.glPopMatrix();
   }

   public void renderTag(String tag, double pX, double pY, double pZ) {
      float var13 = (float)mc.thePlayer.getDistance(pX + mc.thePlayer.posX, pY + mc.thePlayer.posY, pZ + mc.thePlayer.posZ) / 4.0F;
      if (var13 < 1.6F) {
         var13 = 1.6F;
      }

      int colour = 16777215;
      RenderManager renderManager = mc.getRenderManager();
      int color = 16776960;
      float scale = var13 * 2.0F;
      scale /= 100.0F;
      GL11.glPushMatrix();
      GL11.glTranslatef((float)pX, (float)pY + 1.4F, (float)pZ);
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      GL11.glScalef(-scale, -scale, scale);
      GLUtil.setGLCap(2896, false);
      GLUtil.setGLCap(2929, false);
      Tessellator var14 = Tessellator.getInstance();
      WorldRenderer var15 = var14.getWorldRenderer();
      int width = (int)(RenderHelper.getNahrFont().getStringWidth(tag) / 2.0F);
      GLUtil.setGLCap(3042, true);
      GL11.glBlendFunc(770, 771);
      RenderHelper.drawBorderedRect((float)(-width - 2), -RenderHelper.getNahrFont().getStringHeight(tag), (float)(width + 2), 2.0F, 1.0F, 1610612736, Integer.MIN_VALUE);
      RenderHelper.getNahrFont().drawString(tag, (float)(-width), -(RenderHelper.getNahrFont().getStringHeight(tag) + 2.0F), NahrFont.FontType.SHADOW_THIN, -1, -16777216);
      GLUtil.revertAllCaps();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }
}
