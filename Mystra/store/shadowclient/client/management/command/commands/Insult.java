package store.shadowclient.client.management.command.commands;

import java.util.Random;

import store.shadowclient.client.management.command.Command;
import net.minecraft.client.Minecraft;

public class Insult extends Command {

	public Insult(String[] names, String description) {
		super(names, description);
	}

	@Override
	public String getAlias() {
		return "insult";
	}

	@Override
	public String getDescription() {
		return "Insults a player";
	}

	@Override
	public String getSyntax() {
		return ".insult";
	}

	@Override
	public String executeCommand(String line, String[] args) {
		String[] pre = new String[]{"You", "u", "You fucking", "I hate you, you", "Go die in a hole you", "Go fuck a duck you", "Go have a gay orgy with your dad you", "Kill your self you", "Go die in a fire you", "Eat my pants you", "Go have sex with your cousin you", "Go cry about your micropenis you", "Go fist a cat you", "Go take some more ritalin you", "dull", "bloody", "you bloody", "you fucking shit filled"};
        String[] post = new String[]{"nodus user", "fat ass", "lard ass", "fatty", "pig", "homosexual", "terrorist", "muslim", "sand nigger", "piece of shit", "nigger", "cunt", "jew", "fag", "faggot", "fucker", "motherfucker", "nazi", "bitch", "woman", "gay ass mother fucker", "runt", "asshole", "ass", "douche", "douchebag", "asshat", "smart ass", "tampon", "dumbass", "retard", "douchefag", "monkey", "Windows 8 user", "pretentious piece of shit", "femo-nazi", "mutant", "wet piece of shit", "cocksucker", "moron", "anus", "anal bouncer", "ball licker", "ballsack", "cumslut", "cum eater", "cum sponge", "cum bucket", "crackwhore", "cum guzzler", "parasite infested piece of shit", "twat", "shitface", "shit sniffer", "redneck", "swag fag", "unclefucker", "cumrag", "dick cheese", "cum stain", "ADD piece of shit", "ADHD piece of shit", "piece of flying shit", "frozen condom", "autist", "re-fucking-tard"};
        Random rand = new Random();
        int i = rand.nextInt(pre.length - 1);
        int i1 = rand.nextInt(post.length - 1);
        String insult = pre[i].concat(" ").concat(post[i1]);
        if (args != null) {
            insult = args[0] + " " + insult;
        }
        Minecraft.getMinecraft().thePlayer.sendChatMessage(insult);
		return insult;
	}

}
