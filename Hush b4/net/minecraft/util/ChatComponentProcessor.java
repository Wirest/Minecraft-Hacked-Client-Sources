// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.command.CommandException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.Entity;
import net.minecraft.command.ICommandSender;

public class ChatComponentProcessor
{
    public static IChatComponent processComponent(final ICommandSender commandSender, final IChatComponent component, final Entity entityIn) throws CommandException {
        IChatComponent ichatcomponent = null;
        if (component instanceof ChatComponentScore) {
            final ChatComponentScore chatcomponentscore = (ChatComponentScore)component;
            String s = chatcomponentscore.getName();
            if (PlayerSelector.hasArguments(s)) {
                final List<Entity> list = PlayerSelector.matchEntities(commandSender, s, (Class<? extends Entity>)Entity.class);
                if (list.size() != 1) {
                    throw new EntityNotFoundException();
                }
                s = list.get(0).getName();
            }
            ichatcomponent = ((entityIn != null && s.equals("*")) ? new ChatComponentScore(entityIn.getName(), chatcomponentscore.getObjective()) : new ChatComponentScore(s, chatcomponentscore.getObjective()));
            ((ChatComponentScore)ichatcomponent).setValue(chatcomponentscore.getUnformattedTextForChat());
        }
        else if (component instanceof ChatComponentSelector) {
            final String s2 = ((ChatComponentSelector)component).getSelector();
            ichatcomponent = PlayerSelector.matchEntitiesToChatComponent(commandSender, s2);
            if (ichatcomponent == null) {
                ichatcomponent = new ChatComponentText("");
            }
        }
        else if (component instanceof ChatComponentText) {
            ichatcomponent = new ChatComponentText(((ChatComponentText)component).getChatComponentText_TextValue());
        }
        else {
            if (!(component instanceof ChatComponentTranslation)) {
                return component;
            }
            final Object[] aobject = ((ChatComponentTranslation)component).getFormatArgs();
            for (int i = 0; i < aobject.length; ++i) {
                final Object object = aobject[i];
                if (object instanceof IChatComponent) {
                    aobject[i] = processComponent(commandSender, (IChatComponent)object, entityIn);
                }
            }
            ichatcomponent = new ChatComponentTranslation(((ChatComponentTranslation)component).getKey(), aobject);
        }
        final ChatStyle chatstyle = component.getChatStyle();
        if (chatstyle != null) {
            ichatcomponent.setChatStyle(chatstyle.createShallowCopy());
        }
        for (final IChatComponent ichatcomponent2 : component.getSiblings()) {
            ichatcomponent.appendSibling(processComponent(commandSender, ichatcomponent2, entityIn));
        }
        return ichatcomponent;
    }
}
