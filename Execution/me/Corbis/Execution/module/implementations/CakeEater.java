package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

public class CakeEater extends Module {
    public CakeEater(){
        super("CakeEater", Keyboard.KEY_NONE, Category.WORLD);
    }

    @EventTarget
    public void onUpdate(EventMotionUpdate event){
        if(event.isPre() && !Execution.instance.moduleManager.getModule(Flight.class).isEnabled){
            int radius = 4;
            for(int x = -radius; x < radius; x++){
                for(int y = -radius; y < radius; y++){
                    for(int z = -radius; z < radius; z++){
                        double posX = mc.thePlayer.posX;
                        double posY = mc.thePlayer.posY;
                        double posZ = mc.thePlayer.posZ;
                        BlockPos pos = new BlockPos(posX + x, posY + y, posZ + z);
                        if(pos.getBlock() instanceof BlockCake) {
                            BlockPos surroundTop = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
                            boolean surrounded = !((surroundTop.getBlock() instanceof BlockAir));
                            if (!surrounded) {
                                if (pos.getBlock() instanceof BlockCake) {
                                    mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), pos, EnumFacing.UP, new Vec3(pos.getX() * 0.5, pos.getY() * 0.5, pos.getZ() * 0.5));
                                }
                            } else {
                                if (surroundTop.getBlock() == Blocks.wool) {
                                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, surroundTop, EnumFacing.UP));

                                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, surroundTop, EnumFacing.UP));
                                } else if (surroundTop.getBlock() == Blocks.end_stone) {
                                    for (int i = 0; i < 9; i++) {
                                        if (mc.thePlayer.inventory.getStackInSlot(i) == null)
                                            continue;
                                        if (mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemPickaxe) {
                                            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = i));
                                        }
                                    }
                                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, surroundTop, EnumFacing.UP));

                                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, surroundTop, EnumFacing.UP));
                                } else if (surroundTop.getBlock() instanceof BlockPlanks) {
                                    for (int i = 0; i < 9; i++) {
                                        if (mc.thePlayer.inventory.getStackInSlot(i) == null)
                                            continue;
                                        if (mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemAxe) {
                                            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = i));
                                        }
                                    }
                                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, surroundTop, EnumFacing.UP));

                                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, surroundTop, EnumFacing.UP));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
