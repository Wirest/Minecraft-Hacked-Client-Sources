package dev.astroclient.client.feature.impl.combat;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import dev.astroclient.client.event.impl.packet.EventReceivePacket;
import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.property.impl.StringProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.util.ChatUtil;
import dev.astroclient.client.util.EntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S02PacketChat;

import java.util.ArrayList;
import java.util.List;


@Toggleable(label = "AntiBot", category = Category.COMBAT)
public class AntiBot extends ToggleableFeature {

    public StringProperty mode = new StringProperty("Mode", "Watchdog", new String[]{"Watchdog"});
    public NumberProperty<Integer> ticksExist = new NumberProperty<>("Ticks Existed", true, 300, 1, 0, 1000);
    public BooleanProperty debug = new BooleanProperty("Debug", true, false);

    public List<EntityPlayer> bots = new ArrayList<>();

    private final String[] strings = new String[]{"1st Killer - ", "1st Place - ", "You died! Want to play again? Click here!", " - Damage Dealt - ", "1st - ", "Winning Team - ", "Winners: ", "Winner: ", "Winning Team: ", " win the game!", "1st Place: ", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners - "};


    @Subscribe
    public void onEvent(EventReceivePacket eventReceivePacket) {
        if (bots.isEmpty())
            return;
        if (eventReceivePacket.getPacket() instanceof S02PacketChat) {
            for (String string : strings) {
                if (((S02PacketChat) eventReceivePacket.getPacket()).getChatComponent().getUnformattedText().contains(string))
                    this.bots.clear();
            }
        }
    }

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
        setSuffix(this.mode.getValue());
        if (mc.thePlayer.ticksExisted <= ticksExist.getValue()) {
            for (EntityPlayer entity : mc.theWorld.playerEntities) {
                if (entity.getDistanceToEntity(mc.thePlayer) <= 17)
                    if (Math.abs(mc.thePlayer.posY - entity.posY) > 2)
                        if (!EntityUtil.isOnSameTeam(entity) && entity != mc.thePlayer && !bots.contains(entity) && entity.ticksExisted != 0 && entity.ticksExisted <= 10) {
                            bots.add(entity);
                            if (debug.getValue())
                                ChatUtil.tellPlayer("Added bot: " + entity.getName() + ", Distance: " + entity.getDistanceToEntity(mc.thePlayer) + ", Ticks Existed: " + entity.ticksExisted);
                        }
            }
        }
    }
}
