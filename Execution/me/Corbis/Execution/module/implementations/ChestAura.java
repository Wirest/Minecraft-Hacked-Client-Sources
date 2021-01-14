package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.event.events.EventReceivePacket;
import me.Corbis.Execution.event.events.EventUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.setBlockAndFacing;
import net.minecraft.block.BlockChest;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class ChestAura extends Module {
    boolean started = false;
    public ChestAura(){
        super("ChestAura", Keyboard.KEY_NONE, Category.WORLD);
    }

    public ArrayList<BlockPos> clicked = new ArrayList<>();

    @EventTarget
    public void onUpdate(EventMotionUpdate event){
        if(mc.thePlayer.ticksExisted % 70 == 0){
            clicked.clear();
        }
        if(mc.thePlayer == null){
            started = false;
            clicked.clear();
        }
        if(Execution.instance.moduleManager.getModule(Stealer.class).silentCurrent != null || mc.currentScreen != null){
            return;
        }
        int radius = 4;
        for(int x = -radius; x < radius; x++){
            for(int y = -radius; y < radius; y++){
                for(int z = -radius; z < radius; z++){
                    BlockPos pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
                    if(pos.getBlock() instanceof BlockChest){
                        if(!clicked.contains(pos)){
                            float rots[] = setBlockAndFacing.BlockUtil.getDirectionToBlock(pos.getX(), pos.getY(), pos.getZ(), EnumFacing.UP);
                            event.setYaw(rots[0]);
                            event.setPitch(rots[1]);
                            if(mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), pos, EnumFacing.UP, new Vec3(pos.getX() * 0.5, pos.getY() * 0.5, pos.getZ() * 0.5))){
                                mc.thePlayer.swingItem();
                            }
                            clicked.add(pos);
                        }
                    }
                }
            }
        }
    }

    @EventTarget
    public void onReceive(EventReceivePacket event){
        if(event.getPacket() instanceof S02PacketChat){
            if(((S02PacketChat) event.getPacket()).getChatComponent().getUnformattedText().contains("FIGHT")) {
                clicked.clear();
                this.started = true;
            }
        }
    }

}
