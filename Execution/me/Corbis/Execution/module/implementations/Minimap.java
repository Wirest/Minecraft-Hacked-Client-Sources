package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Keyboard;

public class Minimap extends Module {
    EntityOtherPlayerMP top;

    public Minimap() {
        super("Minimap", Keyboard.KEY_NONE, Category.RENDER);
    }

    @Override
    public void onDisable(){

    }
    @EventTarget
    public void onUpdate(EventUpdate event){
    }
    @Override
    public void onEnable(){

    }
}
