package nivia.modules.miscellanous;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import nivia.events.EventTarget;
import nivia.events.events.EventPacketSend;
import nivia.events.events.EventPostMotionUpdates;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;

import java.util.Objects;

public class AutoTool extends Module {
	public AutoTool() {
		super("AutoTool", 0, 0x005C00, Category.MISCELLANEOUS, "Automatically switches to the best weapon and tool",
				new String[] { "asword", "atool", "autot" }, true);
	}
	public Property<Boolean> autoweapon = new Property<Boolean>(this, "Auto Weapon", true);
	@EventTarget
	  public void onAttack(EventPacketSend e) {	   
		if(!autoweapon.value) return;
		if(e.getPacket() instanceof C02PacketUseEntity){
			if(((C02PacketUseEntity)e.getPacket()).getAction().equals(Action.ATTACK)){
				boolean checks = !mc.thePlayer.isEating();
				if (checks) 
					bestSword(((C02PacketUseEntity)e.getPacket()).getEntityFromWorld(mc.theWorld));	    	
			}
		 }
	  }
	
	@EventTarget
	  public void onClickBlock(EventPostMotionUpdates e) {	   
		boolean checks = !mc.thePlayer.isEating();
	    if (checks && mc.playerController.isHittingBlock && !Objects.isNull(mc.objectMouseOver.func_178782_a())) {
	    	 bestTool(mc.objectMouseOver.func_178782_a().getX(), mc.objectMouseOver.func_178782_a().getY(), mc.objectMouseOver.func_178782_a().getZ());
	    }
	  }
	public void bestSword(Entity targetEntity) {
	    int bestSlot = 0;
	    float f = (1.0F / -1.0F);
	    for (int i1 = 36; i1 < 45; i1++) {
	      if ((mc.thePlayer.inventoryContainer.inventorySlots.toArray()[i1] != null) && (targetEntity != null))
	      {
	        ItemStack curSlot = mc.thePlayer.inventoryContainer.getSlot(i1).getStack();
	        if ((curSlot != null) && 
	          ((curSlot.getItem() instanceof ItemSword)))
	        {
	          ItemSword sword = (ItemSword)curSlot.getItem();
	          if (sword.func_150931_i() > f)
	          {
	            bestSlot = i1 - 36;
	            f = sword.func_150931_i();
	          }
	        }
	      }
	    }
	    if (f > (1.0F / -1.0F)) {
	    	
	      mc.thePlayer.inventory.currentItem = bestSlot;
	      mc.playerController.updateController();
	    }
	  }
	public void bestTool(int x, int y, int z)
	  {
	    int blockId = Block.getIdFromBlock(mc.theWorld.getBlockState(new net.minecraft.util.BlockPos(x, y, z)).getBlock());
	    int bestSlot = 0;
	    float f = -1.0F;
	    for (int i1 = 36; i1 < 45; i1++) {
	      try
	      {
	        ItemStack curSlot = mc.thePlayer.inventoryContainer.getSlot(i1).getStack();
	        if ((((curSlot.getItem() instanceof net.minecraft.item.ItemTool)) || ((curSlot.getItem() instanceof ItemSword)) || ((curSlot.getItem() instanceof net.minecraft.item.ItemShears))) && 
	          (curSlot.getStrVsBlock(Block.getBlockById(blockId)) > f))
	        {
	          bestSlot = i1 - 36;
	          f = curSlot.getStrVsBlock(Block.getBlockById(blockId));
	        }
	      }
	      catch (Exception localException) {}
	    }
	    
	    if (f != -1.0F) {
	      mc.thePlayer.inventory.currentItem = bestSlot;
	      mc.playerController.updateController();
	    }
	  }
}
