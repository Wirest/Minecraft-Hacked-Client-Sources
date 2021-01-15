package net.minecraft.util;

import java.util.Iterator;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.Entity;

public class ChatComponentProcessor
{

    public static IChatComponent processComponent(ICommandSender commandSender, IChatComponent component, Entity entityIn) throws CommandException
    {
        Object var3 = null;

        if (component instanceof ChatComponentScore)
        {
            ChatComponentScore var4 = (ChatComponentScore)component;
            String var5 = var4.getName();

            if (PlayerSelector.hasArguments(var5))
            {
                List var6 = PlayerSelector.matchEntities(commandSender, var5, Entity.class);

                if (var6.size() != 1)
                {
                    throw new EntityNotFoundException();
                }

                var5 = ((Entity)var6.get(0)).getCommandSenderName();
            }

            var3 = entityIn != null && var5.equals("*") ? new ChatComponentScore(entityIn.getCommandSenderName(), var4.getObjective()) : new ChatComponentScore(var5, var4.getObjective());
            ((ChatComponentScore)var3).setValue(var4.getUnformattedTextForChat());
        }
        else if (component instanceof ChatComponentSelector)
        {
            String var7 = ((ChatComponentSelector)component).getSelector();
            var3 = PlayerSelector.matchEntitiesToChatComponent(commandSender, var7);

            if (var3 == null)
            {
                var3 = new ChatComponentText("");
            }
        }
        else if (component instanceof ChatComponentText)
        {
            var3 = new ChatComponentText(((ChatComponentText)component).getChatComponentText_TextValue());
        }
        else
        {
            if (!(component instanceof ChatComponentTranslation))
            {
                return component;
            }

            Object[] var8 = ((ChatComponentTranslation)component).getFormatArgs();

            for (int var10 = 0; var10 < var8.length; ++var10)
            {
                Object var12 = var8[var10];

                if (var12 instanceof IChatComponent)
                {
                    var8[var10] = processComponent(commandSender, (IChatComponent)var12, entityIn);
                }
            }

            var3 = new ChatComponentTranslation(((ChatComponentTranslation)component).getKey(), var8);
        }

        ChatStyle var9 = component.getChatStyle();

        if (var9 != null)
        {
            ((IChatComponent)var3).setChatStyle(var9.createShallowCopy());
        }

        Iterator var11 = component.getSiblings().iterator();

        while (var11.hasNext())
        {
            IChatComponent var13 = (IChatComponent)var11.next();
            ((IChatComponent)var3).appendSibling(processComponent(commandSender, var13, entityIn));
        }

        return (IChatComponent)var3;
    }
}
