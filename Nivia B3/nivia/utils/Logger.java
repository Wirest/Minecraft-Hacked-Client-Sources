package nivia.utils;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import nivia.Pandora;
import nivia.managers.PropertyManager.Property;

import java.util.Arrays;
import java.util.Objects;

public class Logger {

	public static void logChat(Object message){
		if (Objects.nonNull(Helper.player()))
			Helper.player().addChatComponentMessage(new ChatComponentText("\2478[\247b" + Pandora.getClientName() + "\2478]\2477 "  + message));
		else System.out.println(message);
	}
	/**
	 * Credits to anodise for this awesome shit
	 */
	public static void logToggleMessage(String context, boolean enabled) {
		logChat(String.format("%s%s%s has been %s%s.", EnumChatFormatting.GOLD, context, EnumChatFormatting.GRAY, enabled ? (EnumChatFormatting.GREEN + "enabled") : (EnumChatFormatting.RED + "disabled"), EnumChatFormatting.GRAY));
	}

	public static void logToggleMessage(String context, boolean enabled, String moduleLabel) {
		logChat(String.format("%s%s%s has been %s for %s.", EnumChatFormatting.GOLD, context, EnumChatFormatting.GRAY, (enabled ? (EnumChatFormatting.GREEN + "enabled") : (EnumChatFormatting.RED + "disabled")) + EnumChatFormatting.GRAY, moduleLabel));
	}

	public static void logSetMessage(String context, String property) {
		logChat(String.format("%s%s%s set to %s%s%s.", EnumChatFormatting.GOLD, context, EnumChatFormatting.GRAY, EnumChatFormatting.GOLD, property, EnumChatFormatting.GRAY));
	}
	public static void logSetMessage(String Module, String context, Property property) {
		logChat(String.format("%s's %s%s \2477set to %s%s%s.", Module, EnumChatFormatting.GOLD, context, EnumChatFormatting.GRAY, EnumChatFormatting.GOLD, ((property.value instanceof Boolean) ? ((boolean)property.value ? (EnumChatFormatting.GREEN + "enabled") : (EnumChatFormatting.RED + "disabled")) : String.valueOf(property.value)), EnumChatFormatting.GRAY));
	}
	public static String LogExecutionFail(final String context, String[] executors) {
		logChat(String.format("Invalid %s %s.", context, Arrays.toString(executors)));
		return String.format("Invalid %s %s.", context, Arrays.toString(executors));
	}
	public static String LogExecutionFail(final String context) {
		logChat(String.format("Invalid %s", context));
		return String.format("Invalid %s.", context);
	}
}