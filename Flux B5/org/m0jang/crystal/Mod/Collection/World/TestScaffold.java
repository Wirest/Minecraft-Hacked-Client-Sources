package org.m0jang.crystal.Mod.Collection.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import org.m0jang.crystal.Events.EventSafeWalk;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.TimeHelper;
import org.m0jang.crystal.Values.Value;

public class TestScaffold extends Module {
   public static Value silent;
   public static Value tower;
   TimeHelper timer = new TimeHelper();
   int delay = 0;
   int placeDelay = 0;

   static {
      silent = new Value("TestScaffold", Boolean.TYPE, "Silent", false);
      tower = new Value("TestScaffold", Boolean.TYPE, "Tower", false);
   }

   public TestScaffold() {
      super("TestScaffold", Category.Movement, false);
   }

   @EventTarget
   public void onSafewalk(EventSafeWalk e) {
      e.cancelled = true;
   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      this.scaffold();
   }

   public void scaffold() {
      BlockPos pos = (new BlockPos(Minecraft.thePlayer)).down();
      if (Minecraft.thePlayer.fallDistance <= 3.0F) {
         if (this.material(pos).isReplaceable()) {
            int var1 = -1;

            int var2;
            for(var2 = 0; var2 < 9; ++var2) {
               ItemStack item = Minecraft.thePlayer.inventory.getStackInSlot(var2);
               if (isValid(item) && !isEmpty(item) && item.getItem() instanceof ItemBlock) {
                  var1 = var2;
               }
            }

            if (var1 != -1) {
               var2 = Minecraft.thePlayer.inventory.currentItem;
               Minecraft.thePlayer.inventory.currentItem = var1;
               if (tower.getBooleanValue() && Minecraft.gameSettings.keyBindJump.pressed) {
                  ++this.placeDelay;
                  if (this.placeDelay > 7) {
                     this.place(pos);
                     this.placeDelay = 0;
                  }
               }

               if (Minecraft.thePlayer.onGround) {
                  ++this.placeDelay;
                  if (this.placeDelay > 2 && !this.timer.hasPassed(1400.0D)) {
                     this.place(pos);
                     this.placeDelay = 0;
                  } else if (this.timer.hasPassed(1700.0D)) {
                     this.timer.reset();
                  }
               }

               if (silent.getBooleanValue()) {
                  Minecraft.thePlayer.inventory.currentItem = var2;
               }

            }
         }
      }
   }

   public static boolean isValid(ItemStack item) {
      if (isEmpty(item)) {
         return false;
      } else {
         return !item.getUnlocalizedName().equalsIgnoreCase("tile.cactus");
      }
   }

   public IBlockState blockState(BlockPos pos) {
      return Minecraft.theWorld.getBlockState(pos);
   }

   public Block blockPos(BlockPos pos) {
      return this.blockState(pos).getBlock();
   }

   public Material material(BlockPos pos) {
      return this.blockPos(pos).getMaterial();
   }

   public static boolean isEmpty(ItemStack stack) {
      return stack == null;
   }

   public void look(Vec3 vector) {
      double diffX = vector.xCoord - Minecraft.thePlayer.posX;
      double diffY = vector.yCoord - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
      double diffZ = vector.zCoord - Minecraft.thePlayer.posZ;
      double distance = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
      float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
      float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, distance)));
      Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(Minecraft.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.thePlayer.rotationYaw), Minecraft.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.thePlayer.rotationPitch), Minecraft.thePlayer.onGround));
   }

   private boolean place(BlockPos pos) {
      Vec3 eye = new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
      EnumFacing[] face;
      int j = (face = EnumFacing.values()).length;

      for(int i = 0; i < j; ++i) {
         EnumFacing side = face[i];
         BlockPos blockPos = pos.offset(side);
         EnumFacing otherSide = side.getOpposite();
         if (eye.squareDistanceTo((new Vec3((double)pos.getX(), (double)pos.getY(), (double)pos.getZ())).addVector(0.5D, 0.5D, 0.5D)) < eye.squareDistanceTo((new Vec3((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ())).addVector(0.5D, 0.5D, 0.5D)) && this.blockPos(blockPos).canCollideCheck(this.blockState(blockPos), false)) {
            Vec3 vec = (new Vec3((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ())).addVector(0.5D, 0.5D, 0.5D).add((new Vec3((double)otherSide.getDirectionVec().getX(), (double)otherSide.getDirectionVec().getY(), (double)otherSide.getDirectionVec().getZ())).multi(0.5D));
            if (eye.squareDistanceTo(vec) <= 18.0D) {
               this.look(vec);
               this.mc.playerController.func_178890_a(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.getCurrentEquippedItem(), blockPos, otherSide, vec);
               if (!silent.getBooleanValue()) {
                  Minecraft.thePlayer.swingItem();
               } else {
                  Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
               }

               return true;
            }
         }
      }

      return false;
   }

   public void onDisable() {
      Timer var10000 = this.mc.timer;
      Timer.timerSpeed = 1.0F;
      super.onDisable();
   }
}
