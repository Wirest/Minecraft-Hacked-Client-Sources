package saint.modstuff.modules;

import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PostMotion;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.valuestuff.Value;

public class FastUse extends Module {
   public final Value ticks = new Value("instantuse_ticks", 8);

   public FastUse() {
      super("InstantUse", -5985391, ModManager.Category.EXPLOITS);
      this.setTag("Instant Use");
      Saint.getCommandManager().getContentList().add(new Command("instantuseticks", "<ticks>", new String[]{"instantticks", "iut"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               FastUse.this.ticks.setValueState((Integer)FastUse.this.ticks.getDefaultValue());
            } else {
               FastUse.this.ticks.setValueState(Integer.parseInt(message.split(" ")[1]));
            }

            if ((Integer)FastUse.this.ticks.getValueState() > 15) {
               FastUse.this.ticks.setValueState(15);
            } else if ((Integer)FastUse.this.ticks.getValueState() < 0) {
               FastUse.this.ticks.setValueState(0);
            }

            Logger.writeChat("Instant Use Ticks set to: " + FastUse.this.ticks.getValueState());
         }
      });
   }

   public boolean isUsable(ItemStack stack) {
      if (stack == null) {
         return false;
      } else {
         if (mc.thePlayer.isUsingItem()) {
            if (stack.getItem() instanceof ItemFood) {
               return true;
            }

            if (stack.getItem() instanceof ItemPotion) {
               return true;
            }

            if (stack.getItem() instanceof ItemBucketMilk) {
               return true;
            }
         }

         return false;
      }
   }

   public void onEvent(Event event) {
      if (event instanceof PostMotion && this.isUsable(mc.thePlayer.getCurrentEquippedItem()) && mc.thePlayer.getItemInUseDuration() > (Integer)this.ticks.getValueState()) {
         for(int x = 0; x < 96; ++x) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
         }

         mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
         mc.thePlayer.stopUsingItem();
      } else if (event instanceof PreMotion) {
         if (this.isUsable(mc.thePlayer.getCurrentEquippedItem()) && mc.thePlayer.isUsingItem()) {
            this.setColor(-17664);
         } else {
            this.setColor(-5985391);
         }
      }

   }
}
