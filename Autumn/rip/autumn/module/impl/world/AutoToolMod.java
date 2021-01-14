package rip.autumn.module.impl.world;

import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Mouse;
import rip.autumn.annotations.Label;
import rip.autumn.core.Autumn;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.impl.combat.AuraMod;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.BoolOption;
import rip.autumn.utils.InventoryUtils;

@Label("Auto Tool")
@Category(ModuleCategory.WORLD)
@Aliases({"autotool", "autosword"})
public final class AutoToolMod extends Module {
   private final BoolOption swords = new BoolOption("Swords", true);
   private final BoolOption tools = new BoolOption("Tools", true);
   private AuraMod aura;

   public AutoToolMod() {
      this.addOptions(new Option[]{this.swords, this.tools});
   }

   public void onEnabled() {
      if (this.aura == null) {
         this.aura = (AuraMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(AuraMod.class);
      }

   }

   @Listener(MotionUpdateEvent.class)
   public void onEvent(MotionUpdateEvent event) {
      if (event.isPre()) {
         if (this.tools.getValue() && mc.currentScreen == null && Mouse.isButtonDown(0) && mc.objectMouseOver != null) {
            BlockPos blockPos = mc.objectMouseOver.getBlockPos();
            if (blockPos != null) {
               Block block = mc.theWorld.getBlockState(blockPos).getBlock();
               float strength = 1.0F;
               int bestToolSlot = -1;

               for(int i = 0; i < 9; ++i) {
                  ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
                  if (itemStack != null && itemStack.getStrVsBlock(block) > strength) {
                     strength = itemStack.getStrVsBlock(block);
                     bestToolSlot = i;
                  }
               }

               if (bestToolSlot != -1) {
                  mc.thePlayer.inventory.currentItem = bestToolSlot;
               }
            }
         }

         if (this.aura.getTarget() != null && this.aura.isEnabled() && this.swords.getValue()) {
            float damage = 1.0F;
            int bestSwordSlot = -1;

            for(int i = 0; i < 9; ++i) {
               ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
               if (itemStack != null && itemStack.getItem() instanceof ItemSword) {
                  float damageLevel = InventoryUtils.getDamageLevel(itemStack);
                  if (damageLevel > damage) {
                     damage = damageLevel;
                     bestSwordSlot = i;
                  }
               }
            }

            if (bestSwordSlot != -1) {
               mc.thePlayer.inventory.currentItem = bestSwordSlot;
            }
         }
      }

   }
}
