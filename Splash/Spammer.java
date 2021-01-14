package splash.client.modules.misc;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.client.C01PacketChatMessage;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.NumberValue;
import splash.client.events.player.EventPlayerUpdate;
import splash.utilities.time.Stopwatch;

import java.util.Random;

/**
 * Author: Ice
 * Created: 22:17, 13-Jun-20
 * Project: Client
 */

public class Spammer extends Module {

    private Stopwatch stopwatch = new Stopwatch();
    private String[] messages = new String[]{"Splash truly is the best cheat ;)", "splash", "i have a very nice minecraft utility mod"
    		, "follow me on twitter @customKKK", "This is a spambot o;", "This server sucks!", "My dad works for mogang",
    		"Splash is best hacker client!!11!"};
    public NumberValue<Integer> chatDelayValue = new NumberValue<>("Delay", 100, 1, 2000, this);

    public Spammer() {
        super("Spammer", "Spams chat.", ModuleCategory.MISC);
    }

    @Collect
    public void onUpdate(EventPlayerUpdate eventPlayerUpdate) {
        if(stopwatch.delay(chatDelayValue.getValue())) {
            mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C01PacketChatMessage(randomPhrase()));
            stopwatch.reset();
        }
    }



    private String randomPhrase() {
       Random random = new Random();
       return messages[random.nextInt(messages.length)];
    }
}
