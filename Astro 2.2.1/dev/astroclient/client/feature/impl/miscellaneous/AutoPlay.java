package dev.astroclient.client.feature.impl.miscellaneous;

import dev.astroclient.client.event.impl.packet.EventReceivePacket;
import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.StringProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.property.impl.number.Type;
import dev.astroclient.client.util.Timer;
import net.minecraft.network.play.server.S02PacketChat;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;

@Toggleable(label = "AutoPlay", category = Category.MISC, hidden = true)
public class AutoPlay extends ToggleableFeature {

    public NumberProperty<Integer> delay = new NumberProperty<>("Delay", true, 3000, 10, 0, 5000, Type.MILLISECONDS);

    public StringProperty mode = new StringProperty("Mode", "Insane", new String[]{"Insane", "Normal"});

    private final String[] strings = new String[]{"1st Killer - ", "1st Place - ", "You died! Want to play again? Click here!", " - Damage Dealt - ", "1st - ", "Winning Team - ", "Winners: ", "Winner: ", "Winning Team: ", " win the game!", "1st Place: ", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners - "};

    private Timer timer = new Timer();

    private boolean canJoin;

    public boolean teams, insane;

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
        if (timer.hasReached(delay.getValue())) {
            if (canJoin && mc.ingameGUI.skywars) {
                mc.thePlayer.sendChatMessage("/play " + (teams ? "teams" : "solo") + "_" + mode.getValue());
                canJoin = false;
            }
        }
        if (!canJoin) timer.reset();
    }

    @Subscribe
    public void onEvent(EventReceivePacket eventReceivePacket) {
        if (eventReceivePacket.getPacket() instanceof S02PacketChat) {
            for (String string : strings) {
                if (((S02PacketChat) eventReceivePacket.getPacket()).getChatComponent().getUnformattedText().contains(string))
                    canJoin = true;
            }
        }
    }

}
