/*
 * Decompiled with CFR 0.145.
 *
 * Could not load the following classes:
 *  org.apache.commons.lang3.RandomUtils
 */
package me.memewaredevs.client.module.misc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.PacketInEvent;
import me.memewaredevs.client.module.Module;
import net.minecraft.network.play.server.S02PacketChat;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.function.Consumer;

public class DeathInsult extends Module {
	public DeathInsult() {
		super("Death Insult", 0, Module.Category.MISC);
	}

	public static String randomMessage() {
		final String[] randomMessages = { "Did ur parents ask you to run away",
				"I'd tell you to uninstall, but your aim is so bad you wouldn't hit the button.",
				"You do be lookin' kinda bad at the game", "Did someone leave your cage open?",
				"rage at me on discord Dort#0001", "Is being in the spectator mode fun?",
				" you're the type of guy to quickdrop irl", "you got a solid 0% on the iq test.",
				"I understand why your parents abused you", "Do you practice being this bad?",
				"your aim is sponsored by Parkinson's", "go take a long walk on a short bridge",
				"mans probably plays fortnite lmao.", "plz no repotr i no want ban !",
				"you probably have the coronavirus.", "you really like taking L's.", "drown in your own salt",
				"I'm not saying you're worthless, but i'd unplug ur life support to charge my phone.",
				"could you please commit not alive?", "I don't cheat, you just need to click faster",
				"I speak english not your gibberish", "Your mom do be lookin' kinda black doe",
				"Hey look! It's a fortnite player", "Need some pvp advice? ", " do you really like dying this much?",
				"you probably reported me, which means you have geay.", "you're the type to get 3rd place in a 1v1.",
				"how does it feel to get stomped on?", "you are the type of guy to use sigma.",
				" that's a #VictoryRoyale! better luck next time!", "lol you probably speak dog eater",
				"is a fricking niggler", "I'm black and this a robbery",
				"even your mom is better than you in this game.", "go back to fortnite you degenerate.",
				"your iq is that of a steve.", "go commit stop breathing plz",
				"your parents abandoned you, then the orphanage did the same", "you probably bought sigma premium",
				"mans probably got an error on his hello world program lmao",
				"how'd you hit the download button with that aim", "Someone in 1940 forgot to gas you",
				"did your dad go get milk and went missing?", "mans thinks that his ping is equal to his iq.",
				"stop eating dogs", "if the body is 70% water then how is your body 100% salt?",
				"mans got thrown at a brick wall by his parents when he was 2 years old.",
				"you don't have parents, now that's an L",
				"how are you so bad? im losing brain cells while watching you play",
				"even lolitsalex has more wins than you",
				"some kids were dropped at birth, but you got thrown at the wall.", "i dont hack i just meme", "memeware by dort", "did you forget the attack button?"};

		return randomMessages[RandomUtils.nextInt(0, randomMessages.length - 1)];
	}

	@Handler
	public Consumer<PacketInEvent> eventConsumer0 = event -> {
		if (this.mc.thePlayer != null && this.mc.thePlayer.ticksExisted >= 0
				&& event.getPacket() instanceof S02PacketChat) {
			final String look = "killed by " + mc.thePlayer.getName();
			final String look2 = "slain by " + mc.thePlayer.getName();
			final String look3 = "void while escaping " + mc.thePlayer.getName();
			final String look4 = "was killed with magic while fighting " + mc.thePlayer.getName();
			final String look5 = "couldn't fly while escaping " + mc.thePlayer.getName();
			final String look6 = "fell to their death while escaping " + mc.thePlayer.getName();
			final String look7 = "You recieved a reward for killing ";
			final S02PacketChat cp = (S02PacketChat) event.getPacket();
			final String cp21 = cp.func_148915_c().getUnformattedText();
			if ((cp21.startsWith(mc.thePlayer.getName() + "(") && cp21.contains("asesino ha")) || cp21.contains(look)
					|| cp21.contains(look2) || cp21.contains(look3)
					|| cp21.contains("You have been rewarded $50 and 2 point(s)!") || cp21.contains(look4) || cp21.contains(look5) || cp21.contains(look6) || cp21.contains(look7)) {
				this.mc.thePlayer.sendChatMessage("" + DeathInsult.randomMessage() + " ["
						+ RandomStringUtils.random(12, "abcdef0123456789") + "]");
			}
		}
	};
}
