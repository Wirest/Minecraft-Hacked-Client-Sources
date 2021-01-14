package com.mentalfrostbyte.jello.modules;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.mentalfrostbyte.jello.event.EventManager;
import com.mentalfrostbyte.jello.event.EventTarget;
import com.mentalfrostbyte.jello.event.events.EventMotion;
import com.mentalfrostbyte.jello.event.events.EventPacketSent;
import com.mentalfrostbyte.jello.event.events.EventReceivePacket;
import com.mentalfrostbyte.jello.event.types.EventType2;
import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.main.Module;
import com.mentalfrostbyte.jello.util.TimerUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class BlockFly extends Module {

	public Random rand = new Random();
	
	public TimerUtil timer = new TimerUtil();
	public TimerUtil timerMotion = new TimerUtil();
	public float yaw;
	public float pitch;
	public int startSlot;
	BlockPos currentBlockPos = new BlockPos(-1, -1, -1);
	public static boolean isEnabled;
	private static List<Block> invalid = Arrays.asList(new Block[] { Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava,
            Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars,
            Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore,
            Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox,
            Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore,
            Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate,
            Blocks.stone_button, Blocks.wooden_button, Blocks.lever });
	private float rotation = 999.0F;
	int itemStackSize;
	  int currentSlot;
	  int currentItem;
	  //private BlockData blockData;
	  boolean placing;
	  private int slot;
	  public static float progressYaw;
		public static float progressPitch;
		public boolean grounded;
		public boolean headTurned;
	  
	public BlockFly() {
        super("Block Fly", Keyboard.KEY_J);
        this.jelloCat = Jello.tabgui.cats.get(0);
       }
	double normalise( double value,  double start,  double end ) 
	{
	   double width       = end - start   ;   // 
	   double offsetValue = value - start ;   // value relative to 0

	  return ( offsetValue - ( Math.floor( offsetValue / width ) * width ) ) + start ;
	}
	public void onEnable(){
		grounded = mc.thePlayer.onGround;
		headTurned = false;
        yaw = mc.thePlayer.rotationYaw;
          pitch = mc.thePlayer.rotationPitch;
		//progressYaw = mc.thePlayer.rotationYaw;
		//yaw = mc.thePlayer.rotationYaw;
		//progressYaw = (float)this.normalise(mc.thePlayer.rotationYaw, -180, 180);
		//yaw = (float)this.normalise(mc.thePlayer.rotationYaw, -180, 180);
		//startSlot = Jello.core.player().inventory.currentItem;
		//Jello.core.player().inventory.currentItem = this.getBlockSlot();
		isEnabled = true;
		EventManager.register(this);
		this.rotation = 999.0F;
		//String server = mc.isSingleplayer() == true ? "":mc.getCurrentServerData().serverIP;
		//Jello.addChatMessage(server);
	}
	
	public void onDisable(){
		grounded = mc.thePlayer.onGround;
		headTurned = false;
        yaw = mc.thePlayer.rotationYaw;
          pitch = mc.thePlayer.rotationPitch;
		//Jello.core.player().inventory.currentItem = startSlot;
		EventManager.unregister(this);
		//timer.reset();
		isEnabled = false;
		mc.timer.timerSpeed = 1.0F;
	    if (mc.thePlayer.isSwingInProgress)
	    {
	      mc.thePlayer.swingProgress = 0.0F;
	      mc.thePlayer.swingProgressInt = 0;
	      mc.thePlayer.isSwingInProgress = false;
	    }
	}
	
	@EventTarget
	public void onMotion(EventMotion e){
		int k = 0;
		if (hasBlocksInHotbar())
	      {
	        ItemStack localObject1 = new ItemStack(Item.getItemById(261));
	        for (int i = 9; i < 36; i++) {
	          if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
	          {
	            
	            if (((mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock)) && (isValidItem(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem())))
	            {
	              for (int m = 36; m < 45; m++) {
	                if (Container.canAddItemToSlot(mc.thePlayer.inventoryContainer.getSlot(m), (ItemStack)localObject1, true))
	                {
	                  swap(i, m - 36);
	                  k++;
	                  break;
	                }
	              }
	              if (k != 0) {
	                break;
	              }
	              swap(i, 7); break;
	            }
	          }
	        }
	      }
	      boolean bool1 = true;
	      boolean bool2 = true;
	      
	      mc.thePlayer.field_175165_bM = 999.0F;
	      if (!hasBlocks()) {
	        return;
	      }
	      double d1 = mc.thePlayer.posX;double d2 = mc.thePlayer.posZ;
	      double d3 = mc.thePlayer.movementInput.moveForward;
	      double d4 = mc.thePlayer.movementInput.moveStrafe;
	      float f = mc.thePlayer.rotationYaw;
	      if (!mc.thePlayer.isCollidedHorizontally) {
	        
	          d1 += (d3 * 0.45D * Math.cos(Math.toRadians(f + 90.0F)) + d4 * 0.45D * Math.sin(Math.toRadians(f + 90.0F))) * 1.0D;
	          d2 += (d3 * 0.45D * Math.sin(Math.toRadians(f + 90.0F)) - d4 * 0.45D * Math.cos(Math.toRadians(f + 90.0F))) * 1.0D;
	        
	      }
	      BlockPos localBlockPos = new BlockPos(d1, mc.thePlayer.posY - 1.0D, d2);
	      Block localBlock = mc.theWorld.getBlockState(localBlockPos).getBlock();
	      BlockData localBlockData = getBlockData(localBlockPos);
	      if (e.getType() == EventType2.PRE)
	      {
	        
	        eventMove(e);
	        if ((mc.gameSettings.keyBindJump.getIsKeyPressed()) && (bool1) && (mc.thePlayer.moveForward == 0.0F) && (mc.thePlayer.moveStrafing == 0.0F)) {
	          eventMotion(e);
	        }
	      }
	      if ((isBlockAccessible(localBlock)) && (localBlockData != null))
	      {
	        this.currentBlockPos = localBlockData.pos;
	        if (e.getType() == EventType2.PRE)
	        {
	          float[] arrayOfFloat = getRotations(localBlockData.pos, localBlockData.facing);
	          
	          headTurned = true;
	          yaw = arrayOfFloat[0];
	          pitch = arrayOfFloat[1];
	        //  (e).setYaw(arrayOfFloat[0]);
	         //   (e).setPitch(arrayOfFloat[1]);
	          
	          f = arrayOfFloat[0];
	          this.rotation = arrayOfFloat[1];
	          if ((!mc.gameSettings.keyBindJump.getIsKeyPressed()) && (mc.thePlayer.onGround) && (isNotCollidingBelow(0.001D)) && (mc.thePlayer.isCollidedVertically)) {
	            e.setOnGround(false);
	            grounded = false;
	          }else{
	        	  grounded = mc.thePlayer.onGround;
	          }
	        }
	        else
	        {
	         
	          this.timerMotion.reset();
	          int n = mc.thePlayer.inventory.currentItem;
	          updateHotbar();
	          mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), localBlockData.pos, localBlockData.facing, blockDataToVec3(localBlockData.pos, localBlockData.facing));
	            mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
	            mc.thePlayer.inventory.currentItem = n;
	            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n));
	            mc.playerController.updateController();
	            headTurned = false;
	            yaw = mc.thePlayer.rotationYaw;
		          pitch = mc.thePlayer.rotationPitch;
	          this.timer.reset();
	        }
	      }
	}
	
	@EventTarget
	public void onPacket(EventReceivePacket e){
		      
		      Packet packet = e.getPacket();
		      int k;
		      if (packet instanceof S2FPacketSetSlot)
		      {

		          e.setCancelled(true);
		        
		        S2FPacketSetSlot localS2FPacketSetSlot = (S2FPacketSetSlot)packet;
		        k = localS2FPacketSetSlot.getSlot();
		        ItemStack localItemStack1 = localS2FPacketSetSlot.getItem();
		        if (k != -1)
		        {
		          this.currentSlot = k;
		          if (localItemStack1 != null)
		          {
		            this.itemStackSize = localItemStack1.stackSize;
		          }
		          else
		          {
		            if (mc.thePlayer.inventoryContainer.getSlot(k).getStack() != null)
		            {
		              ItemStack localItemStack2 = new ItemStack(Item.getItemById(261));
		              mc.thePlayer.inventoryContainer.getSlot(k).putStack(null);
		            }
		            this.itemStackSize = 0;
		          }
		          mc.playerController.updateController();
		        }
		      }
		    
	}
	
	@EventTarget
	public void onSend(EventPacketSent e){
		if(headTurned){
		if(e.getPacket() instanceof C03PacketPlayer || e.getPacket() instanceof C05PacketPlayerLook || e.getPacket() instanceof C06PacketPlayerPosLook) {
	   	  		e.setPacket(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, yaw, pitch, grounded));
		}
		}
	}
	
	private BlockData getBlockData(BlockPos paramBlockPos)
	  {
	    if (isValidBlock(paramBlockPos.add(0, -1, 0))) {
	      return new BlockData(paramBlockPos.add(0, -1, 0), EnumFacing.UP);
	    }
	    if (isValidBlock(paramBlockPos.add(-1, 0, 0))) {
	      return new BlockData(paramBlockPos.add(-1, 0, 0), EnumFacing.EAST);
	    }
	    if (isValidBlock(paramBlockPos.add(1, 0, 0))) {
	      return new BlockData(paramBlockPos.add(1, 0, 0), EnumFacing.WEST);
	    }
	    if (isValidBlock(paramBlockPos.add(0, 0, 1))) {
	      return new BlockData(paramBlockPos.add(0, 0, 1), EnumFacing.NORTH);
	    }
	    if (isValidBlock(paramBlockPos.add(0, 0, -1))) {
	      return new BlockData(paramBlockPos.add(0, 0, -1), EnumFacing.SOUTH);
	    }
	    BlockPos localBlockPos1 = paramBlockPos.add(-1, 0, 0);
	    if (isValidBlock(localBlockPos1.add(0, -1, 0))) {
	      return new BlockData(localBlockPos1.add(0, -1, 0), EnumFacing.UP);
	    }
	    if (isValidBlock(localBlockPos1.add(-1, 0, 0))) {
	      return new BlockData(localBlockPos1.add(-1, 0, 0), EnumFacing.EAST);
	    }
	    if (isValidBlock(localBlockPos1.add(1, 0, 0))) {
	      return new BlockData(localBlockPos1.add(1, 0, 0), EnumFacing.WEST);
	    }
	    if (isValidBlock(localBlockPos1.add(0, 0, 1))) {
	      return new BlockData(localBlockPos1.add(0, 0, 1), EnumFacing.NORTH);
	    }
	    if (isValidBlock(localBlockPos1.add(0, 0, -1))) {
	      return new BlockData(localBlockPos1.add(0, 0, -1), EnumFacing.SOUTH);
	    }
	    BlockPos localBlockPos2 = paramBlockPos.add(1, 0, 0);
	    if (isValidBlock(localBlockPos2.add(0, -1, 0))) {
	      return new BlockData(localBlockPos2.add(0, -1, 0), EnumFacing.UP);
	    }
	    if (isValidBlock(localBlockPos2.add(-1, 0, 0))) {
	      return new BlockData(localBlockPos2.add(-1, 0, 0), EnumFacing.EAST);
	    }
	    if (isValidBlock(localBlockPos2.add(1, 0, 0))) {
	      return new BlockData(localBlockPos2.add(1, 0, 0), EnumFacing.WEST);
	    }
	    if (isValidBlock(localBlockPos2.add(0, 0, 1))) {
	      return new BlockData(localBlockPos2.add(0, 0, 1), EnumFacing.NORTH);
	    }
	    if (isValidBlock(localBlockPos2.add(0, 0, -1))) {
	      return new BlockData(localBlockPos2.add(0, 0, -1), EnumFacing.SOUTH);
	    }
	    BlockPos localBlockPos3 = paramBlockPos.add(0, 0, 1);
	    if (isValidBlock(localBlockPos3.add(0, -1, 0))) {
	      return new BlockData(localBlockPos3.add(0, -1, 0), EnumFacing.UP);
	    }
	    if (isValidBlock(localBlockPos3.add(-1, 0, 0))) {
	      return new BlockData(localBlockPos3.add(-1, 0, 0), EnumFacing.EAST);
	    }
	    if (isValidBlock(localBlockPos3.add(1, 0, 0))) {
	      return new BlockData(localBlockPos3.add(1, 0, 0), EnumFacing.WEST);
	    }
	    if (isValidBlock(localBlockPos3.add(0, 0, 1))) {
	      return new BlockData(localBlockPos3.add(0, 0, 1), EnumFacing.NORTH);
	    }
	    if (isValidBlock(localBlockPos3.add(0, 0, -1))) {
	      return new BlockData(localBlockPos3.add(0, 0, -1), EnumFacing.SOUTH);
	    }
	    BlockPos localBlockPos4 = paramBlockPos.add(0, 0, -1);
	    if (isValidBlock(localBlockPos4.add(0, -1, 0))) {
	      return new BlockData(localBlockPos4.add(0, -1, 0), EnumFacing.UP);
	    }
	    if (isValidBlock(localBlockPos4.add(-1, 0, 0))) {
	      return new BlockData(localBlockPos4.add(-1, 0, 0), EnumFacing.EAST);
	    }
	    if (isValidBlock(localBlockPos4.add(1, 0, 0))) {
	      return new BlockData(localBlockPos4.add(1, 0, 0), EnumFacing.WEST);
	    }
	    if (isValidBlock(localBlockPos4.add(0, 0, 1))) {
	      return new BlockData(localBlockPos4.add(0, 0, 1), EnumFacing.NORTH);
	    }
	    if (isValidBlock(localBlockPos4.add(0, 0, -1))) {
	      return new BlockData(localBlockPos4.add(0, 0, -1), EnumFacing.SOUTH);
	    }
	    BlockPos localBlockPos5 = paramBlockPos.add(0, -1, 0);
	    if (isValidBlock(localBlockPos5.add(0, -1, 0))) {
	      return new BlockData(localBlockPos5.add(0, -1, 0), EnumFacing.UP);
	    }
	    if (isValidBlock(localBlockPos5.add(-1, 0, 0))) {
	      return new BlockData(localBlockPos5.add(-1, 0, 0), EnumFacing.EAST);
	    }
	    if (isValidBlock(localBlockPos5.add(1, 0, 0))) {
	      return new BlockData(localBlockPos5.add(1, 0, 0), EnumFacing.WEST);
	    }
	    if (isValidBlock(localBlockPos5.add(0, 0, 1))) {
	      return new BlockData(localBlockPos5.add(0, 0, 1), EnumFacing.NORTH);
	    }
	    if (isValidBlock(localBlockPos5.add(0, 0, -1))) {
	      return new BlockData(localBlockPos5.add(0, 0, -1), EnumFacing.SOUTH);
	    }
	    BlockPos localBlockPos6 = localBlockPos5.add(1, 0, 0);
	    if (isValidBlock(localBlockPos6.add(0, -1, 0))) {
	      return new BlockData(localBlockPos6.add(0, -1, 0), EnumFacing.UP);
	    }
	    if (isValidBlock(localBlockPos6.add(-1, 0, 0))) {
	      return new BlockData(localBlockPos6.add(-1, 0, 0), EnumFacing.EAST);
	    }
	    if (isValidBlock(localBlockPos6.add(1, 0, 0))) {
	      return new BlockData(localBlockPos6.add(1, 0, 0), EnumFacing.WEST);
	    }
	    if (isValidBlock(localBlockPos6.add(0, 0, 1))) {
	      return new BlockData(localBlockPos6.add(0, 0, 1), EnumFacing.NORTH);
	    }
	    if (isValidBlock(localBlockPos6.add(0, 0, -1))) {
	      return new BlockData(localBlockPos6.add(0, 0, -1), EnumFacing.SOUTH);
	    }
	    BlockPos localBlockPos7 = localBlockPos5.add(-1, 0, 0);
	    if (isValidBlock(localBlockPos7.add(0, -1, 0))) {
	      return new BlockData(localBlockPos7.add(0, -1, 0), EnumFacing.UP);
	    }
	    if (isValidBlock(localBlockPos7.add(-1, 0, 0))) {
	      return new BlockData(localBlockPos7.add(-1, 0, 0), EnumFacing.EAST);
	    }
	    if (isValidBlock(localBlockPos7.add(1, 0, 0))) {
	      return new BlockData(localBlockPos7.add(1, 0, 0), EnumFacing.WEST);
	    }
	    if (isValidBlock(localBlockPos7.add(0, 0, 1))) {
	      return new BlockData(localBlockPos7.add(0, 0, 1), EnumFacing.NORTH);
	    }
	    if (isValidBlock(localBlockPos7.add(0, 0, -1))) {
	      return new BlockData(localBlockPos7.add(0, 0, -1), EnumFacing.SOUTH);
	    }
	    BlockPos localBlockPos8 = localBlockPos5.add(0, 0, 1);
	    if (isValidBlock(localBlockPos8.add(0, -1, 0))) {
	      return new BlockData(localBlockPos8.add(0, -1, 0), EnumFacing.UP);
	    }
	    if (isValidBlock(localBlockPos8.add(-1, 0, 0))) {
	      return new BlockData(localBlockPos8.add(-1, 0, 0), EnumFacing.EAST);
	    }
	    if (isValidBlock(localBlockPos8.add(1, 0, 0))) {
	      return new BlockData(localBlockPos8.add(1, 0, 0), EnumFacing.WEST);
	    }
	    if (isValidBlock(localBlockPos8.add(0, 0, 1))) {
	      return new BlockData(localBlockPos8.add(0, 0, 1), EnumFacing.NORTH);
	    }
	    if (isValidBlock(localBlockPos8.add(0, 0, -1))) {
	      return new BlockData(localBlockPos8.add(0, 0, -1), EnumFacing.SOUTH);
	    }
	    BlockPos localBlockPos9 = localBlockPos5.add(0, 0, -1);
	    if (isValidBlock(localBlockPos9.add(0, -1, 0))) {
	      return new BlockData(localBlockPos9.add(0, -1, 0), EnumFacing.UP);
	    }
	    if (isValidBlock(localBlockPos9.add(-1, 0, 0))) {
	      return new BlockData(localBlockPos9.add(-1, 0, 0), EnumFacing.EAST);
	    }
	    if (isValidBlock(localBlockPos9.add(1, 0, 0))) {
	      return new BlockData(localBlockPos9.add(1, 0, 0), EnumFacing.WEST);
	    }
	    if (isValidBlock(localBlockPos9.add(0, 0, 1))) {
	      return new BlockData(localBlockPos9.add(0, 0, 1), EnumFacing.NORTH);
	    }
	    if (isValidBlock(localBlockPos9.add(0, 0, -1))) {
	      return new BlockData(localBlockPos9.add(0, 0, -1), EnumFacing.SOUTH);
	    }
	    return null;
	  }
	
	private boolean isValidBlock(BlockPos paramBlockPos)
	  {
	    Block localBlock = mc.theWorld.getBlockState(paramBlockPos).getBlock();
	    if ((localBlock.getMaterial().isSolid()) || (!localBlock.isTranslucent()) || (localBlock.isSolidFullCube()) || ((localBlock instanceof BlockLadder)) || ((localBlock instanceof BlockCarpet)) || ((localBlock instanceof BlockSnow)) || ((localBlock instanceof BlockSkull))) {
	      if (!localBlock.getMaterial().isLiquid()) {
	        return true;
	      }
	    }
	    return false;
	  }
	
	public static void scaffoldMove(double paramDouble)
	  {
	    float f1 = mc.thePlayer.rotationYaw * 0.017453292F;
	    float f2 = mc.thePlayer.rotationYaw * 0.017453292F - 4.712389F;
	    float f3 = mc.thePlayer.rotationYaw * 0.017453292F + 4.712389F;
	    float f4 = mc.thePlayer.rotationYaw * 0.017453292F + 0.5969026F;
	    float f5 = mc.thePlayer.rotationYaw * 0.017453292F + -0.5969026F;
	    float f6 = mc.thePlayer.rotationYaw * 0.017453292F - 2.3876104F;
	    float f7 = mc.thePlayer.rotationYaw * 0.017453292F - -2.3876104F;
	    if ((mc.gameSettings.keyBindForward.pressed) && (!isMoving()))
	    {
	      if ((mc.gameSettings.keyBindLeft.pressed) && (!mc.gameSettings.keyBindRight.pressed))
	      {
	        mc.thePlayer.motionX -= MathHelper.sin(f5) * paramDouble;
	        mc.thePlayer.motionZ += MathHelper.cos(f5) * paramDouble;
	      }
	      else if ((mc.gameSettings.keyBindRight.pressed) && (!mc.gameSettings.keyBindLeft.pressed))
	      {
	        mc.thePlayer.motionX -= MathHelper.sin(f4) * paramDouble;
	        mc.thePlayer.motionZ += MathHelper.cos(f4) * paramDouble;
	      }
	      else
	      {
	        mc.thePlayer.motionX -= MathHelper.sin(f1) * paramDouble;
	        mc.thePlayer.motionZ += MathHelper.cos(f1) * paramDouble;
	      }
	    }
	    else if ((mc.gameSettings.keyBindBack.pressed) && (!isMoving()))
	    {
	      if ((mc.gameSettings.keyBindLeft.pressed) && (!mc.gameSettings.keyBindRight.pressed))
	      {
	        mc.thePlayer.motionX -= MathHelper.sin(f6) * paramDouble;
	        mc.thePlayer.motionZ += MathHelper.cos(f6) * paramDouble;
	      }
	      else if ((mc.gameSettings.keyBindRight.pressed) && (!mc.gameSettings.keyBindLeft.pressed))
	      {
	        mc.thePlayer.motionX -= MathHelper.sin(f7) * paramDouble;
	        mc.thePlayer.motionZ += MathHelper.cos(f7) * paramDouble;
	      }
	      else
	      {
	        mc.thePlayer.motionX += MathHelper.sin(f1) * paramDouble;
	        mc.thePlayer.motionZ -= MathHelper.cos(f1) * paramDouble;
	      }
	    }
	    else if ((mc.gameSettings.keyBindLeft.pressed) && (!mc.gameSettings.keyBindRight.pressed) && (!mc.gameSettings.keyBindForward.pressed) && (!mc.gameSettings.keyBindBack.pressed) && (!isMoving()))
	    {
	      mc.thePlayer.motionX += MathHelper.sin(f2) * paramDouble;
	      mc.thePlayer.motionZ -= MathHelper.cos(f2) * paramDouble;
	    }
	    else if ((mc.gameSettings.keyBindRight.pressed) && (!mc.gameSettings.keyBindLeft.pressed) && (!mc.gameSettings.keyBindForward.pressed) && (!mc.gameSettings.keyBindBack.pressed) && (!isMoving()))
	    {
	      mc.thePlayer.motionX += MathHelper.sin(f3) * paramDouble;
	      mc.thePlayer.motionZ -= MathHelper.cos(f3) * paramDouble;
	    }
	  }
	
	public static boolean isMoving()
	  {
	    if ((mc.gameSettings.keyBindForward.isPressed()) || (mc.gameSettings.keyBindBack.isPressed()) || (mc.gameSettings.keyBindLeft.isPressed()) || (mc.gameSettings.keyBindRight.isPressed())) {
	      return false;
	    }
	    return false;
	  }
	  
	  public static boolean isNotCollidingBelow(double paramDouble)
	  {
	    if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -paramDouble, 0.0D)).isEmpty()) {
	      return true;
	    }
	    return false;
	  }
		    
	  private boolean hasBlocksInHotbar()
	  {
	    for (int i = 36; i < 45; i++) {
	      if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
	      {
	        Item localItem = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
	        if (((localItem instanceof ItemBlock)) && (isValidItem(localItem))) {
	          return false;
	        }
	      }
	    }
	    return true;
	  }
	  
	  private boolean isValidItem(Item paramItem)
	  {
	    if (!(paramItem instanceof ItemBlock)) {
	      return false;
	    }
	    ItemBlock localItemBlock = (ItemBlock)paramItem;
	    Block localBlock = localItemBlock.getBlock();
	    if (invalid.contains(localBlock)) {
	      return false;
	    }
	    return true;
	  }
	  
	  protected void swap(int paramInt1, int paramInt2)
	  {
	    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, paramInt1, paramInt2, 2, mc.thePlayer);
	  }
	  
	  private boolean hasBlocks()
	  {
	    int i = 36;
	    while (i < 45) {
	      try
	      {
	        ItemStack localItemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
	        if ((localItemStack == null) || (localItemStack.getItem() == null) || (!(localItemStack.getItem() instanceof ItemBlock)) || (!isValidItem(localItemStack.getItem()))) {
	          i++;
	        } else {
	          return true;
	        }
	      }
	      catch (Exception localException) {}
	    }
	    return false;
	  }
	  
	  public class BlockData
	  {
	    public BlockPos pos;
	    public EnumFacing facing;
	    
	    private BlockData(BlockPos paramBlockPos, EnumFacing paramEnumFacing)
	    {
	      this.pos = paramBlockPos;
	      this.facing = paramEnumFacing;
	    }
	  }
	  
	  public boolean isBlockAccessible(Block paramBlock)
	  {
	    if (paramBlock.getMaterial().isReplaceable())
	    {
	      if (((paramBlock instanceof BlockSnow)) && (paramBlock.getBlockBoundsMaxY() > 0.125D)) {
	        return false;
	      }
	      return true;
	    }
	    return false;
	  }
	  
	  private Vec3 blockDataToVec3(BlockPos paramBlockPos, EnumFacing paramEnumFacing)
	  {
	    double d1 = paramBlockPos.getX() + 0.5D;
	    double d2 = paramBlockPos.getY() + 0.5D;
	    double d3 = paramBlockPos.getZ() + 0.5D;
	    d1 += paramEnumFacing.getFrontOffsetX() / 2.0D;
	    d3 += paramEnumFacing.getFrontOffsetZ() / 2.0D;
	    d2 += paramEnumFacing.getFrontOffsetY() / 2.0D;
	    return new Vec3(d1, d2, d3);
	  }
		  
	  private void updateHotbar()
	  {
	    ItemStack localItemStack = new ItemStack(Item.getItemById(261));
	    try
	    {
	      for (int i = 36; i < 45; i++)
	      {
	        int j = i - 36;
	        if ((!Container.canAddItemToSlot(mc.thePlayer.inventoryContainer.getSlot(i), localItemStack, true)) && 
	          ((mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock)) && (mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null) && 
	          (isValidItem(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem())) && (mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize != 0))
	        {
	          if ((this.currentSlot == j) && (this.itemStackSize == 0))
	          {
	            this.itemStackSize = 120;
	            return;
	          }
	          if (mc.thePlayer.inventory.currentItem == j) {
	            break;
	          }
	          mc.thePlayer.inventory.currentItem = j;
	          this.currentItem = j;
	          mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
	          mc.playerController.updateController(); break;
	        }
	      }
	    }
	    catch (Exception localException) {}
	  }
	  
	  public static float[] getRotations(BlockPos paramBlockPos, EnumFacing paramEnumFacing)
	  {
	    double d1 = paramBlockPos.getX() + 0.5D - mc.thePlayer.posX + paramEnumFacing.getFrontOffsetX() / 2.0D;
	    double d2 = paramBlockPos.getZ() + 0.5D - mc.thePlayer.posZ + paramEnumFacing.getFrontOffsetZ() / 2.0D;
	    double d3 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - (paramBlockPos.getY() + 0.5D);
	    double d4 = MathHelper.sqrt_double(d1 * d1 + d2 * d2);
	    float f1 = (float)(Math.atan2(d2, d1) * 180.0D / 3.141592653589793D) - 90.0F;
	    float f2 = (float)(Math.atan2(d3, d4) * 180.0D / 3.141592653589793D);
	    if (f1 < 0.0F) {
	      f1 += 360.0F;
	    }
	    return new float[] { f1, f2 };
	  }
	  
	  public void eventMove(EventMotion paramEventMotion)
	  {
	    double d1 = mc.thePlayer.motionX;
	    double d2 = mc.thePlayer.motionZ;
	    if (!Jello.getModule("Fast").isToggled()) {
	        mc.thePlayer.motionX *= 0.0D;
	        mc.thePlayer.motionZ *= 0.0D;
	        if ((mc.thePlayer.onGround) && (mc.thePlayer.isCollidedVertically) && (isNotCollidingBelow(0.01D))) {
	          scaffoldMove(0.14D);
	        } else {
	          scaffoldMove(0.2D);
	        }
	      
	    }
	  }
	  
	  public void eventMotion(EventMotion paramEventMotion)
	  {
	    BlockPos localBlockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
	    Block localBlock = mc.theWorld.getBlockState(localBlockPos).getBlock();
	    BlockData localBlockData = getBlockData(localBlockPos);
	    
	   	if ((isBlockAccessible(localBlock)) && (localBlockData != null))
	    {
	      mc.thePlayer.motionY = 0.4196D;
	      mc.thePlayer.motionX *= 0.0D;
	      mc.thePlayer.motionZ *= 0.0D;
	    }
	  }
	  
}
