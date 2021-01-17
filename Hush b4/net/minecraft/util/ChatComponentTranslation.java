// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.util.Arrays;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.IllegalFormatException;
import com.google.common.collect.Lists;
import java.util.regex.Pattern;
import java.util.List;

public class ChatComponentTranslation extends ChatComponentStyle
{
    private final String key;
    private final Object[] formatArgs;
    private final Object syncLock;
    private long lastTranslationUpdateTimeInMilliseconds;
    List<IChatComponent> children;
    public static final Pattern stringVariablePattern;
    
    static {
        stringVariablePattern = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
    }
    
    public ChatComponentTranslation(final String translationKey, final Object... args) {
        this.syncLock = new Object();
        this.lastTranslationUpdateTimeInMilliseconds = -1L;
        this.children = (List<IChatComponent>)Lists.newArrayList();
        this.key = translationKey;
        this.formatArgs = args;
        for (final Object object : args) {
            if (object instanceof IChatComponent) {
                ((IChatComponent)object).getChatStyle().setParentStyle(this.getChatStyle());
            }
        }
    }
    
    synchronized void ensureInitialized() {
        synchronized (this.syncLock) {
            final long i = StatCollector.getLastTranslationUpdateTimeInMilliseconds();
            if (i == this.lastTranslationUpdateTimeInMilliseconds) {
                // monitorexit(this.syncLock)
                return;
            }
            this.lastTranslationUpdateTimeInMilliseconds = i;
            this.children.clear();
        }
        // monitorexit(this.syncLock)
        try {
            this.initializeFromFormat(StatCollector.translateToLocal(this.key));
        }
        catch (ChatComponentTranslationFormatException chatcomponenttranslationformatexception) {
            this.children.clear();
            try {
                this.initializeFromFormat(StatCollector.translateToFallback(this.key));
            }
            catch (ChatComponentTranslationFormatException var5) {
                throw chatcomponenttranslationformatexception;
            }
        }
    }
    
    protected void initializeFromFormat(final String format) {
        final boolean flag = false;
        final Matcher matcher = ChatComponentTranslation.stringVariablePattern.matcher(format);
        int i = 0;
        int j = 0;
        try {
            while (matcher.find(j)) {
                final int k = matcher.start();
                final int l = matcher.end();
                if (k > j) {
                    final ChatComponentText chatcomponenttext = new ChatComponentText(String.format(format.substring(j, k), new Object[0]));
                    chatcomponenttext.getChatStyle().setParentStyle(this.getChatStyle());
                    this.children.add(chatcomponenttext);
                }
                final String s2 = matcher.group(2);
                final String s3 = format.substring(k, l);
                if ("%".equals(s2) && "%%".equals(s3)) {
                    final ChatComponentText chatcomponenttext2 = new ChatComponentText("%");
                    chatcomponenttext2.getChatStyle().setParentStyle(this.getChatStyle());
                    this.children.add(chatcomponenttext2);
                }
                else {
                    if (!"s".equals(s2)) {
                        throw new ChatComponentTranslationFormatException(this, "Unsupported format: '" + s3 + "'");
                    }
                    final String s4 = matcher.group(1);
                    final int i2 = (s4 != null) ? (Integer.parseInt(s4) - 1) : i++;
                    if (i2 < this.formatArgs.length) {
                        this.children.add(this.getFormatArgumentAsComponent(i2));
                    }
                }
                j = l;
            }
            if (j < format.length()) {
                final ChatComponentText chatcomponenttext3 = new ChatComponentText(String.format(format.substring(j), new Object[0]));
                chatcomponenttext3.getChatStyle().setParentStyle(this.getChatStyle());
                this.children.add(chatcomponenttext3);
            }
        }
        catch (IllegalFormatException illegalformatexception) {
            throw new ChatComponentTranslationFormatException(this, illegalformatexception);
        }
    }
    
    private IChatComponent getFormatArgumentAsComponent(final int index) {
        if (index >= this.formatArgs.length) {
            throw new ChatComponentTranslationFormatException(this, index);
        }
        final Object object = this.formatArgs[index];
        IChatComponent ichatcomponent;
        if (object instanceof IChatComponent) {
            ichatcomponent = (IChatComponent)object;
        }
        else {
            ichatcomponent = new ChatComponentText((object == null) ? "null" : object.toString());
            ichatcomponent.getChatStyle().setParentStyle(this.getChatStyle());
        }
        return ichatcomponent;
    }
    
    @Override
    public IChatComponent setChatStyle(final ChatStyle style) {
        super.setChatStyle(style);
        Object[] formatArgs;
        for (int length = (formatArgs = this.formatArgs).length, i = 0; i < length; ++i) {
            final Object object = formatArgs[i];
            if (object instanceof IChatComponent) {
                ((IChatComponent)object).getChatStyle().setParentStyle(this.getChatStyle());
            }
        }
        if (this.lastTranslationUpdateTimeInMilliseconds > -1L) {
            for (final IChatComponent ichatcomponent : this.children) {
                ichatcomponent.getChatStyle().setParentStyle(style);
            }
        }
        return this;
    }
    
    @Override
    public Iterator<IChatComponent> iterator() {
        this.ensureInitialized();
        return Iterators.concat((Iterator<? extends IChatComponent>)ChatComponentStyle.createDeepCopyIterator(this.children), (Iterator<? extends IChatComponent>)ChatComponentStyle.createDeepCopyIterator(this.siblings));
    }
    
    @Override
    public String getUnformattedTextForChat() {
        this.ensureInitialized();
        final StringBuilder stringbuilder = new StringBuilder();
        for (final IChatComponent ichatcomponent : this.children) {
            stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
        }
        return stringbuilder.toString();
    }
    
    @Override
    public ChatComponentTranslation createCopy() {
        final Object[] aobject = new Object[this.formatArgs.length];
        for (int i = 0; i < this.formatArgs.length; ++i) {
            if (this.formatArgs[i] instanceof IChatComponent) {
                aobject[i] = ((IChatComponent)this.formatArgs[i]).createCopy();
            }
            else {
                aobject[i] = this.formatArgs[i];
            }
        }
        final ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(this.key, aobject);
        chatcomponenttranslation.setChatStyle(this.getChatStyle().createShallowCopy());
        for (final IChatComponent ichatcomponent : this.getSiblings()) {
            chatcomponenttranslation.appendSibling(ichatcomponent.createCopy());
        }
        return chatcomponenttranslation;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChatComponentTranslation)) {
            return false;
        }
        final ChatComponentTranslation chatcomponenttranslation = (ChatComponentTranslation)p_equals_1_;
        return Arrays.equals(this.formatArgs, chatcomponenttranslation.formatArgs) && this.key.equals(chatcomponenttranslation.key) && super.equals(p_equals_1_);
    }
    
    @Override
    public int hashCode() {
        int i = super.hashCode();
        i = 31 * i + this.key.hashCode();
        i = 31 * i + Arrays.hashCode(this.formatArgs);
        return i;
    }
    
    @Override
    public String toString() {
        return "TranslatableComponent{key='" + this.key + '\'' + ", args=" + Arrays.toString(this.formatArgs) + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }
    
    public String getKey() {
        return this.key;
    }
    
    public Object[] getFormatArgs() {
        return this.formatArgs;
    }
}
