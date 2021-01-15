package saint.modstuff.modules;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.BlockBB;
import saint.eventstuff.events.EventRederpzPhase;
import saint.eventstuff.events.InsideBlock;
import saint.eventstuff.events.PlayerMovement;
import saint.eventstuff.events.PreMotion;
import saint.eventstuff.events.PushOut;
import saint.eventstuff.events.RecievePacket;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public class Phase extends Module {
   private final TimeHelper time3 = new TimeHelper();
   private int resetNext;
   private double state;
   private final TimeHelper time = new TimeHelper();
   private final TimeHelper time2 = new TimeHelper();
   private final Value skip = new Value("phase_skip", true);
   private final Value silent = new Value("phase_silent", false);
   private final Value vanilla = new Value("phase_vanilla", false);
   private final Value vertical = new Value("phase_vertical", -0.005D);
   private final Value horizontal = new Value("phase_horizontal", 0.2D);
   private int phaseTicks;

   public Phase() {
      super("Phase", -9868951, ModManager.Category.EXPLOITS);
      Saint.getCommandManager().getContentList().add(new Command("phasemode", "<vanilla/silent/skip>", new String[0]) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("silent")) {
               Phase.this.vanilla.setValueState(false);
               Phase.this.skip.setValueState(false);
               Phase.this.silent.setValueState(true);
               Logger.writeChat("Phase Mode set to Silent!");
            } else if (message.split(" ")[1].equalsIgnoreCase("vanilla")) {
               Phase.this.silent.setValueState(false);
               Phase.this.skip.setValueState(false);
               Phase.this.vanilla.setValueState(true);
               Logger.writeChat("Phase Mode set to Vanilla!");
            } else if (message.split(" ")[1].equalsIgnoreCase("skip")) {
               Phase.this.silent.setValueState(false);
               Phase.this.vanilla.setValueState(false);
               Phase.this.skip.setValueState(true);
               Logger.writeChat("Phase Mode set to Skip!");
            } else {
               Logger.writeChat("Option not valid! Available options: silent, vanilla, skip.");
            }

         }
      });
      Saint.getCommandManager().getContentList().add(new Command("phasehorizontaloffset", "<offset>", new String[]{"phaseho", "pho"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               Phase.this.horizontal.setValueState((Double)Phase.this.horizontal.getDefaultValue());
            } else {
               Phase.this.horizontal.setValueState(Double.parseDouble(message.split(" ")[1]));
            }

            if ((Double)Phase.this.horizontal.getValueState() > 10.0D) {
               Phase.this.horizontal.setValueState(10.0D);
            } else if ((Double)Phase.this.horizontal.getValueState() < -1.0D) {
               Phase.this.horizontal.setValueState(-1.0D);
            }

            Logger.writeChat("Insta-Phase Horizontal Offset set to: " + Phase.this.horizontal.getValueState());
         }
      });
      Saint.getCommandManager().getContentList().add(new Command("phaseverticaloffset", "<offset>", new String[]{"phasevo", "pvo"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               Phase.this.vertical.setValueState((Double)Phase.this.vertical.getDefaultValue());
            } else {
               Phase.this.vertical.setValueState(Double.parseDouble(message.split(" ")[1]));
            }

            if ((Double)Phase.this.vertical.getValueState() > 10.0D) {
               Phase.this.vertical.setValueState(10.0D);
            } else if ((Double)Phase.this.vertical.getValueState() < -1.0D) {
               Phase.this.vertical.setValueState(-11.0D);
            }

            Logger.writeChat("Insta-Phase Vertical Offset set to: " + Phase.this.vertical.getValueState());
         }
      });
   }

   public void doPhase() {
      double x = (double)mc.thePlayer.func_174811_aO().getDirectionVec().getX() * 0.1D;
      double z = (double)mc.thePlayer.func_174811_aO().getDirectionVec().getZ() * 0.1D;
      mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.05D, mc.thePlayer.posZ, true));
      mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x * 4.0D, mc.thePlayer.posY, mc.thePlayer.posZ + z * 4.0D, true));
      mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.05D, mc.thePlayer.posZ, true));
      mc.thePlayer.setPosition(mc.thePlayer.posX + x * 4.0D, mc.thePlayer.posY - 0.05D, mc.thePlayer.posZ + z * 4.0D);
   }

   private boolean isInsideBlock() {
      for(int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
         for(int y = MathHelper.floor_double(mc.thePlayer.boundingBox.minY); y < MathHelper.floor_double(mc.thePlayer.boundingBox.maxY) + 1; ++y) {
            for(int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
               Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
               if (block != null && !(block instanceof BlockAir)) {
                  AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z), mc.theWorld.getBlockState(new BlockPos(x, y, z)));
                  if (boundingBox != null && mc.thePlayer.boundingBox.intersectsWith(boundingBox)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public void onEnabled() {
      super.onEnabled();
      if (mc.thePlayer != null && mc.theWorld != null && (Boolean)this.skip.getValueState() && mc.thePlayer.isSneaking()) {
         float dir = mc.thePlayer.rotationYaw;
         if (mc.thePlayer.moveForward < 0.0F) {
            dir += 180.0F;
         }

         if (mc.thePlayer.moveStrafing > 0.0F) {
            dir -= 90.0F * (mc.thePlayer.moveForward < 0.0F ? -0.5F : (mc.thePlayer.moveForward > 0.0F ? 0.5F : 1.0F));
         }

         if (mc.thePlayer.moveStrafing < 0.0F) {
            dir += 90.0F * (mc.thePlayer.moveForward < 0.0F ? -0.5F : (mc.thePlayer.moveForward > 0.0F ? 0.5F : 1.0F));
         }

         double hOff = 0.3D;
         double vOff = 0.2D;
         float xD = (float)((double)((float)Math.cos((double)(dir + 90.0F) * 3.141592653589793D / 180.0D)) * hOff);
         float yD = (float)vOff;
         float zD = (float)((double)((float)Math.sin((double)(dir + 90.0F) * 3.141592653589793D / 180.0D)) * hOff);
         double[] topkek = new double[]{-0.02500000037252903D, -0.028571428997176036D, -0.033333333830038704D, -0.04000000059604645D, -0.05000000074505806D, -0.06666666766007741D, -0.10000000149011612D, -0.20000000298023224D, -0.04000000059604645D, -0.033333333830038704D, -0.028571428997176036D, -0.02500000037252903D};

         for(int i = 0; i < topkek.length; ++i) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + topkek[i], mc.thePlayer.posZ, true));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + (double)(xD * (float)i), mc.thePlayer.posY, mc.thePlayer.posZ + (double)(zD * (float)i), true));
         }

         mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + (double)(xD * 2.0F), mc.thePlayer.posY, mc.thePlayer.posZ + (double)(zD * 2.0F), mc.thePlayer.onGround));
      }

   }

   public void onEvent(Event event) {
      double hOff;
      if (event instanceof PreMotion) {
         boolean hasPhased = false;
         if (this.isInsideBlock()) {
            this.setColor(-2448096);
         } else {
            this.setColor(-9868951);
         }

         if (!mc.thePlayer.isCollidedHorizontally) {
            this.time.reset();
         }

         if ((Boolean)this.skip.getValueState() && this.time.hasReached(85L) && mc.thePlayer.isCollidedHorizontally && (!this.isInsideBlock() || mc.thePlayer.isSneaking()) && mc.thePlayer.onGround) {
            float dir = mc.thePlayer.rotationYaw;
            if (mc.thePlayer.moveForward < 0.0F) {
               dir += 180.0F;
            }

            if (mc.thePlayer.moveStrafing > 0.0F) {
               dir -= 90.0F * (mc.thePlayer.moveForward < 0.0F ? -0.5F : (mc.thePlayer.moveForward > 0.0F ? 0.5F : 1.0F));
            }

            if (mc.thePlayer.moveStrafing < 0.0F) {
               dir += 90.0F * (mc.thePlayer.moveForward < 0.0F ? -0.5F : (mc.thePlayer.moveForward > 0.0F ? 0.5F : 1.0F));
            }

            hOff = 0.3D;
            float xD = (float)((double)((float)Math.cos((double)(dir + 90.0F) * 3.141592653589793D / 180.0D)) * hOff);
            float zD = (float)((double)((float)Math.sin((double)(dir + 90.0F) * 3.141592653589793D / 180.0D)) * hOff);

            for(int i = 0; i < 8; ++i) {
               mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.02D, mc.thePlayer.posZ, true));
               mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + (double)(xD * (float)i), mc.thePlayer.posY, mc.thePlayer.posZ + (double)(zD * (float)i), true));
            }
         }
      } else if (event instanceof BlockBB) {
         BlockBB bb = (BlockBB)event;
         if (!(Boolean)this.vanilla.getValueState() && (mc.thePlayer.isCollidedHorizontally || !mc.thePlayer.onGround) || this.isInsideBlock() || (double)bb.getY() > mc.thePlayer.boundingBox.maxY) {
            try {
               if (bb.getBoundingBox().maxY > mc.thePlayer.boundingBox.minY && (!(Boolean)this.skip.getValueState() || this.isInsideBlock())) {
                  bb.setBoundingBox((AxisAlignedBB)null);
               }
            } catch (Exception var12) {
            }
         }

         if ((Boolean)this.vanilla.getValueState() && !this.isInsideBlock() && bb.getBoundingBox() != null && bb.getBoundingBox().maxY > mc.thePlayer.boundingBox.minY && mc.thePlayer.isSneaking()) {
            bb.setBoundingBox((AxisAlignedBB)null);
         }
      } else if (event instanceof PlayerMovement) {
         PlayerMovement var14 = (PlayerMovement)event;
      } else if (event instanceof RecievePacket) {
         RecievePacket recive = (RecievePacket)event;
         if (recive.getPacket() instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)recive.getPacket()).func_149412_c() == mc.thePlayer.getEntityId() && this.isInsideBlock()) {
            recive.setCancelled(true);
         }
      } else if (event instanceof PushOut) {
         ((PushOut)event).setCancelled(true);
      } else if (event instanceof InsideBlock) {
         ((InsideBlock)event).setCancelled(true);
      }

      if (event instanceof PreMotion) {
         if ((Boolean)this.vanilla.getValueState()) {
            --this.resetNext;
            double xOff = 0.0D;
            hOff = 0.0D;
            double multiplier = 2.6D;
            double mx = Math.cos(Math.toRadians((double)(mc.thePlayer.rotationYaw + 90.0F)));
            double mz = Math.sin(Math.toRadians((double)(mc.thePlayer.rotationYaw + 90.0F)));
            xOff = (double)mc.thePlayer.movementInput.moveForward * multiplier * mx + (double)mc.thePlayer.movementInput.moveStrafe * multiplier * mz;
            hOff = (double)mc.thePlayer.movementInput.moveForward * multiplier * mz - (double)mc.thePlayer.movementInput.moveStrafe * multiplier * mx;
            if (this.isInsideBlock() && mc.thePlayer.isSneaking()) {
               this.time3.reset();
               this.resetNext = 1;
            }

            if (this.resetNext > 0) {
               mc.thePlayer.boundingBox.offsetAndUpdate(xOff, 0.0D, hOff);
            }
         }
      } else if (event instanceof EventRederpzPhase && (Boolean)this.silent.getValueState() && !this.isInsideBlock()) {
         ++this.phaseTicks;
         if (this.phaseTicks > 1) {
            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, -9.99999999E8D, mc.thePlayer.posZ, false));
            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            this.phaseTicks = 0;
         }
      }

   }
}
