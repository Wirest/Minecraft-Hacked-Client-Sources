package saint.modstuff.modules;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PacketSent;
import saint.eventstuff.events.PlayerMovement;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public class Fly extends Module {
   private Value speed = new Value("fly_speed", 0.5F);
   private Value motion = new Value("fly_motion", 1.0F);
   private Value nocheat = new Value("fly_nocheat", false);
   private Value gomme = new Value("fly_gomme", false);
   private int uptime = 0;
   private int ticks = 0;
   private double lasty;
   private double firsty;
   private float height;
   private boolean setLoc;
   private final TimeHelper time = new TimeHelper();
   private final TimeHelper time2 = new TimeHelper();

   public Fly() {
      super("Fly", -9868951, ModManager.Category.MOVEMENT);
      Saint.getCommandManager().getContentList().add(new Command("flyspeed", "<speed-value>", new String[]{"flysp", "fs"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               Fly.this.speed.setValueState((Float)Fly.this.speed.getDefaultValue());
            } else {
               Fly.this.speed.setValueState(Float.parseFloat(message.split(" ")[1]));
            }

            if ((Float)Fly.this.speed.getValueState() > 10.0F) {
               Fly.this.speed.setValueState(10.0F);
            } else if ((Float)Fly.this.speed.getValueState() < 0.5F) {
               Fly.this.speed.setValueState(0.5F);
            }

            Logger.writeChat("Fly Speed set to: " + Fly.this.speed.getValueState());
         }
      });
      Saint.getCommandManager().getContentList().add(new Command("flymotion", "<factor>", new String[]{"flym", "fm"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               Fly.this.motion.setValueState((Float)Fly.this.motion.getDefaultValue());
            } else {
               Fly.this.motion.setValueState(Float.parseFloat(message.split(" ")[1]));
            }

            if ((Float)Fly.this.motion.getValueState() > 5.0F) {
               Fly.this.motion.setValueState(5.0F);
            } else if ((Float)Fly.this.motion.getValueState() < 0.0F) {
               Fly.this.motion.setValueState(0.001F);
            }

            Logger.writeChat("Fly Motion Speed has been multiplied by: " + Fly.this.motion.getValueState());
         }
      });
      Saint.getCommandManager().getContentList().add(new Command("flynocheat", "none", new String[]{"fnocheat", "fnc"}) {
         public void run(String message) {
            Fly.this.nocheat.setValueState(!(Boolean)Fly.this.nocheat.getValueState());
            Logger.writeChat("Fly will " + ((Boolean)Fly.this.nocheat.getValueState() ? "now" : "no longer") + " bypass nocheat.");
         }
      });
      Saint.getCommandManager().getContentList().add(new Command("flygomme", "none", new String[]{"fgomme", "fg"}) {
         public void run(String message) {
            Fly.this.gomme.setValueState(!(Boolean)Fly.this.gomme.getValueState());
            Logger.writeChat("Fly will " + ((Boolean)Fly.this.gomme.getValueState() ? "now" : "no longer") + " bypass Gomme.");
         }
      });
   }

   public void onEnabled() {
      super.onEnabled();
      if (mc.thePlayer != null && (Boolean)this.nocheat.getValueState()) {
         this.time.reset();
         if (mc.thePlayer.onGround) {
            double[] d = new double[]{0.2D, 0.24D};

            for(int a = 0; a < 100; ++a) {
               for(int i = 0; i < d.length; ++i) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + d[i], mc.thePlayer.posZ, false));
               }
            }
         }

         this.firsty = mc.thePlayer.posY;
      }

   }

   public void onDisabled() {
      super.onDisabled();
      if (mc.thePlayer != null) {
         if (mc.thePlayer.capabilities.isFlying) {
            mc.thePlayer.capabilities.isFlying = false;
         }

         mc.thePlayer.capabilities.setFlySpeed(0.05F);
      }

   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion && !(Boolean)this.nocheat.getValueState()) {
         if (mc.thePlayer.onGround) {
            this.setColor(-9868951);
         } else {
            this.setColor(-7876885);
         }

         this.setTag("Fly");
         if (!mc.thePlayer.capabilities.isFlying) {
            mc.thePlayer.capabilities.isFlying = true;
         }

         mc.thePlayer.capabilities.setFlySpeed((Float)this.speed.getValueState() / 10.0F);
         if (mc.gameSettings.keyBindJump.pressed) {
            mc.thePlayer.motionY = (double)((Float)this.speed.getValueState() / 2.0F);
         }

         if (mc.gameSettings.keyBindSneak.pressed) {
            mc.thePlayer.motionY = (double)(-(Float)this.speed.getValueState() / 2.0F);
         }
      } else if (event instanceof PreMotion && (Boolean)this.nocheat.getValueState() && this.time.hasReached(500L)) {
         PreMotion pre = (PreMotion)event;
         if (mc.thePlayer.onGround) {
            this.setColor(-9868951);
         } else {
            this.setColor(-7876885);
         }

         if (this.time.hasReached(1000L) && this.time2.hasReached(500L) && (Boolean)this.gomme.getValueState()) {
            mc.thePlayer.onGround = true;
            if (this.time2.hasReached(800L)) {
               this.time2.reset();
            }
         }

         this.setTag("Fly §fNCP §7" + this.uptime);
         if (mc.gameSettings.keyBindJump.getIsKeyPressed() && mc.thePlayer.posY < this.firsty - 1.0D) {
            mc.thePlayer.motionY = 0.5D;
         } else if (mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
            mc.thePlayer.motionY = -0.5D;
         }

         if (mc.thePlayer.fallDistance < 2.0F) {
            mc.thePlayer.fallDistance = 2.0F;
         }

         if (!mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.getIsKeyPressed() && !mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
            mc.thePlayer.motionY = -0.03127D;
            Block highBlock = mc.theWorld.getBlockState(new BlockPos((int)Math.round(mc.thePlayer.posX), (int)Math.round(mc.thePlayer.boundingBox.minY - 1.5D), (int)Math.round(mc.thePlayer.posZ))).getBlock();
            if (!(highBlock instanceof BlockAir)) {
               this.setLoc = true;
            } else {
               this.setLoc = false;
               this.height = 0.6F;
            }
         } else if (this.setLoc && mc.thePlayer.onGround && (double)this.height >= 0.11D) {
            this.height = (float)((double)this.height - 0.005D);
         }
      } else if (event instanceof PacketSent) {
         if ((Boolean)this.nocheat.getValueState()) {
            PacketSent sent = (PacketSent)event;
            if (sent.getPacket() instanceof C03PacketPlayer) {
               C03PacketPlayer player = (C03PacketPlayer)sent.getPacket();
               if (player.getPositionY() > this.lasty && !mc.thePlayer.onGround) {
                  ++this.uptime;
               }

               if (Keyboard.isKeyDown(21)) {
                  player.field_149474_g = false;
               }

               if (mc.thePlayer.onGround) {
                  this.uptime = 0;
               }

               this.lasty = player.getPositionY();
               if (this.time.hasReached(1000L)) {
                  player.y = mc.thePlayer.posY + (double)this.height;
                  player.field_149474_g = false;
               }
            }
         }
      } else if (event instanceof PlayerMovement && (Boolean)this.nocheat.getValueState() && this.time.hasReached(100L) && !mc.gameSettings.keyBindJump.pressed && !mc.gameSettings.keyBindSneak.pressed) {
         ((PlayerMovement)event).setY(((PlayerMovement)event).getY() * (double)(Float)this.motion.getValueState());
      }

   }
}
