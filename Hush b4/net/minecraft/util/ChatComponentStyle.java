// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import com.google.common.collect.Lists;
import java.util.List;

public abstract class ChatComponentStyle implements IChatComponent
{
    protected List<IChatComponent> siblings;
    private ChatStyle style;
    
    public ChatComponentStyle() {
        this.siblings = (List<IChatComponent>)Lists.newArrayList();
    }
    
    @Override
    public IChatComponent appendSibling(final IChatComponent component) {
        component.getChatStyle().setParentStyle(this.getChatStyle());
        this.siblings.add(component);
        return this;
    }
    
    @Override
    public List<IChatComponent> getSiblings() {
        return this.siblings;
    }
    
    @Override
    public IChatComponent appendText(final String text) {
        return this.appendSibling(new ChatComponentText(text));
    }
    
    @Override
    public IChatComponent setChatStyle(final ChatStyle style) {
        this.style = style;
        for (final IChatComponent ichatcomponent : this.siblings) {
            ichatcomponent.getChatStyle().setParentStyle(this.getChatStyle());
        }
        return this;
    }
    
    @Override
    public ChatStyle getChatStyle() {
        if (this.style == null) {
            this.style = new ChatStyle();
            for (final IChatComponent ichatcomponent : this.siblings) {
                ichatcomponent.getChatStyle().setParentStyle(this.style);
            }
        }
        return this.style;
    }
    
    @Override
    public Iterator<IChatComponent> iterator() {
        return Iterators.concat((Iterator<? extends IChatComponent>)Iterators.forArray(this), (Iterator<? extends IChatComponent>)createDeepCopyIterator(this.siblings));
    }
    
    @Override
    public final String getUnformattedText() {
        final StringBuilder stringbuilder = new StringBuilder();
        for (final IChatComponent ichatcomponent : this) {
            stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
        }
        return stringbuilder.toString();
    }
    
    @Override
    public final String getFormattedText() {
        final StringBuilder stringbuilder = new StringBuilder();
        for (final IChatComponent ichatcomponent : this) {
            stringbuilder.append(ichatcomponent.getChatStyle().getFormattingCode());
            stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
            stringbuilder.append(EnumChatFormatting.RESET);
        }
        return stringbuilder.toString();
    }
    
    public static Iterator<IChatComponent> createDeepCopyIterator(final Iterable<IChatComponent> components) {
        Iterator<IChatComponent> iterator = Iterators.concat(Iterators.transform(components.iterator(), (Function<? super IChatComponent, ? extends Iterator<? extends IChatComponent>>)new Function<IChatComponent, Iterator<IChatComponent>>() {
            @Override
            public Iterator<IChatComponent> apply(final IChatComponent p_apply_1_) {
                return p_apply_1_.iterator();
            }
        }));
        iterator = Iterators.transform(iterator, (Function<? super IChatComponent, ? extends IChatComponent>)new Function<IChatComponent, IChatComponent>() {
            @Override
            public IChatComponent apply(final IChatComponent p_apply_1_) {
                final IChatComponent ichatcomponent = p_apply_1_.createCopy();
                ichatcomponent.setChatStyle(ichatcomponent.getChatStyle().createDeepCopy());
                return ichatcomponent;
            }
        });
        return iterator;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChatComponentStyle)) {
            return false;
        }
        final ChatComponentStyle chatcomponentstyle = (ChatComponentStyle)p_equals_1_;
        return this.siblings.equals(chatcomponentstyle.siblings) && this.getChatStyle().equals(chatcomponentstyle.getChatStyle());
    }
    
    @Override
    public int hashCode() {
        return 31 * this.style.hashCode() + this.siblings.hashCode();
    }
    
    @Override
    public String toString() {
        return "BaseComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
    }
}
