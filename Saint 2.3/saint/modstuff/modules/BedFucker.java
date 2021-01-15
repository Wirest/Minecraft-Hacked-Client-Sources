package saint.modstuff.modules;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import saint.Saint;
import saint.eventstuff.Event;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.BlockHelper;
import saint.utilities.GLUtil;
import saint.utilities.RenderHelper;
import saint.utilities.TimeHelper;

public class BedFucker extends Module {
   private int posX;
   private int posY;
   private int posZ;
   float pitch;
   float yaw;
   private final TimeHelper time = new TimeHelper();
   private final TimeHelper time2 = new TimeHelper();
   private final TimeHelper time3 = new TimeHelper();

   public BedFucker() {
      super("BedFucker", -12763843, ModManager.Category.COMBAT);
      this.setTag("Bed Fucker");
   }

   public void faceBlock(double posX, double posY, double posZ) {
      double diffX = posX - mc.thePlayer.posX;
      double diffZ = posZ - mc.thePlayer.posZ;
      double diffY = posY - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
      double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
      float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
      float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
      EntityPlayerSP var10000 = mc.thePlayer;
      var10000.rotationPitch += MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch);
      var10000 = mc.thePlayer;
      var10000.rotationYaw += MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw);
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         for(int y = 4; y >= -4; --y) {
            for(int x = -4; x <= 4; ++x) {
               for(int z = -4; z <= 4; ++z) {
                  this.posX = (int)(mc.thePlayer.posX - 0.5D + (double)x);
                  this.posZ = (int)(mc.thePlayer.posZ - 0.5D + (double)z);
                  this.posY = (int)(mc.thePlayer.posY - 0.5D + (double)y);
                  Block block = BlockHelper.getBlockAtPos(new BlockPos(this.posX, this.posY, this.posZ));
                  if (block instanceof BlockBed) {
                     long timeLeft = (long)(mc.playerController.curBlockDamageMP / 2.0F);
                     if (this.time3.hasReached(5000L)) {
                        Saint.getNotificationManager().addInfo("Destroying bed!");
                        this.time3.reset();
                     }

                     this.setColor(-8689426);
                     PreMotion pre = (PreMotion)event;
                     float[] rotations = BlockHelper.getBlockRotations((double)this.posX, (double)this.posY, (double)this.posZ);
                     pre.setYaw(rotations[0]);
                     pre.setPitch(rotations[1]);
                     mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(this.posX, this.posY, this.posZ), EnumFacing.DOWN));
                     if (this.time.hasReached(timeLeft)) {
                        mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(this.posX, this.posY, this.posZ), EnumFacing.DOWN));
                        mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
                        this.time.reset();
                     }
                  } else {
                     this.setColor(-16711681);
                  }
               }
            }
         }
      }

   }

   public void drawESP(double x, double y, double z, double r, double g, double b) {
      GL11.glPushMatrix();
      GLUtil.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glLineWidth(1.5F);
      GLUtil.glDisable(2896);
      GLUtil.glDisable(3553);
      GLUtil.glEnable(2848);
      GLUtil.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4d(r, g, b, 0.4000000059604645D);
      GL11.glLineWidth(2.0F);
      RenderHelper.drawFilledBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
      RenderHelper.drawLines(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
      GL11.glColor4d(r, g, b, 0.6000000238418579D);
      GLUtil.revertAllCaps();
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
   }
}
