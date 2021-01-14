package net.minecraft.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatComponentTranslation extends ChatComponentStyle {
    private final String key;
    private final Object[] formatArgs;
    private final Object syncLock = new Object();
    private long lastTranslationUpdateTimeInMilliseconds = -1L;

    /**
     * The discrete elements that make up this component. For example, this
     * would be ["Prefix, ", "FirstArg", "SecondArg", " again ", "SecondArg",
     * " and ", "FirstArg", " lastly ", "ThirdArg", " and also ", "FirstArg", "
     * again!"] for "translation.test.complex" (see en-US.lang)
     */
    List children = Lists.newArrayList();
    public static final Pattern stringVariablePattern = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
    private static final String __OBFID = "CL_00001270";

    public ChatComponentTranslation(String translationKey, Object... args) {
        key = translationKey;
        formatArgs = args;
        Object[] var3 = args;
        int var4 = args.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Object var6 = var3[var5];

            if (var6 instanceof IChatComponent) {
                ((IChatComponent) var6).getChatStyle().setParentStyle(getChatStyle());
            }
        }
    }

    /**
     * ensures that our children are initialized from the most recent string
     * translation mapping.
     */
    synchronized void ensureInitialized() {
        Object var1 = syncLock;

        synchronized (syncLock) {
            long var2 = StatCollector.getLastTranslationUpdateTimeInMilliseconds();

            if (var2 == lastTranslationUpdateTimeInMilliseconds) {
                return;
            }

            lastTranslationUpdateTimeInMilliseconds = var2;
            children.clear();
        }

        try {
            initializeFromFormat(StatCollector.translateToLocal(key));
        } catch (ChatComponentTranslationFormatException var6) {
            children.clear();

            try {
                initializeFromFormat(StatCollector.translateToFallback(key));
            } catch (ChatComponentTranslationFormatException var5) {
                throw var6;
            }
        }
    }

    /**
     * initializes our children from a format string, using the format args to
     * fill in the placeholder variables.
     */
    protected void initializeFromFormat(String format) {
        boolean var2 = false;
        Matcher var3 = ChatComponentTranslation.stringVariablePattern.matcher(format);
        int var4 = 0;
        int var5 = 0;

        try {
            int var7;

            for (; var3.find(var5); var5 = var7) {
                int var6 = var3.start();
                var7 = var3.end();

                if (var6 > var5) {
                    ChatComponentText var8 = new ChatComponentText(String.format(format.substring(var5, var6), new Object[0]));
                    var8.getChatStyle().setParentStyle(getChatStyle());
                    children.add(var8);
                }

                String var14 = var3.group(2);
                String var9 = format.substring(var6, var7);

                if ("%".equals(var14) && "%%".equals(var9)) {
                    ChatComponentText var15 = new ChatComponentText("%");
                    var15.getChatStyle().setParentStyle(getChatStyle());
                    children.add(var15);
                } else {
                    if (!"s".equals(var14)) {
                        throw new ChatComponentTranslationFormatException(this, "Unsupported format: \'" + var9 + "\'");
                    }

                    String var10 = var3.group(1);
                    int var11 = var10 != null ? Integer.parseInt(var10) - 1 : var4++;

                    if (var11 < formatArgs.length) {
                        children.add(getFormatArgumentAsComponent(var11));
                    }
                }
            }

            if (var5 < format.length()) {
                ChatComponentText var13 = new ChatComponentText(String.format(format.substring(var5), new Object[0]));
                var13.getChatStyle().setParentStyle(getChatStyle());
                children.add(var13);
            }
        } catch (IllegalFormatException var12) {
            throw new ChatComponentTranslationFormatException(this, var12);
        }
    }

    private IChatComponent getFormatArgumentAsComponent(int index) {
        if (index >= formatArgs.length) {
            throw new ChatComponentTranslationFormatException(this, index);
        } else {
            Object var2 = formatArgs[index];
            Object var3;

            if (var2 instanceof IChatComponent) {
                var3 = var2;
            } else {
                var3 = new ChatComponentText(var2 == null ? "null" : var2.toString());
                ((IChatComponent) var3).getChatStyle().setParentStyle(getChatStyle());
            }

            return (IChatComponent) var3;
        }
    }

    @Override
    public IChatComponent setChatStyle(ChatStyle style) {
        super.setChatStyle(style);
        Object[] var2 = formatArgs;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            Object var5 = var2[var4];

            if (var5 instanceof IChatComponent) {
                ((IChatComponent) var5).getChatStyle().setParentStyle(getChatStyle());
            }
        }

        if (lastTranslationUpdateTimeInMilliseconds > -1L) {
            Iterator var6 = children.iterator();

            while (var6.hasNext()) {
                IChatComponent var7 = (IChatComponent) var6.next();
                var7.getChatStyle().setParentStyle(style);
            }
        }

        return this;
    }

    @Override
    public Iterator iterator() {
        ensureInitialized();
        return Iterators.concat(ChatComponentStyle.createDeepCopyIterator(children), ChatComponentStyle.createDeepCopyIterator(siblings));
    }

    /**
     * Gets the text of this component, without any special formatting codes
     * added, for chat. TODO: why is this two different methods?
     */
    @Override
    public String getUnformattedTextForChat() {
        ensureInitialized();
        StringBuilder var1 = new StringBuilder();
        Iterator var2 = children.iterator();

        while (var2.hasNext()) {
            IChatComponent var3 = (IChatComponent) var2.next();
            var1.append(var3.getUnformattedTextForChat());
        }

        return var1.toString();
    }

    /**
     * Creates a copy of this component. Almost a deep copy, except the style is
     * shallow-copied.
     */
    @Override
    public ChatComponentTranslation createCopy() {
        Object[] var1 = new Object[formatArgs.length];

        for (int var2 = 0; var2 < formatArgs.length; ++var2) {
            if (formatArgs[var2] instanceof IChatComponent) {
                var1[var2] = ((IChatComponent) formatArgs[var2]).createCopy();
            } else {
                var1[var2] = formatArgs[var2];
            }
        }

        ChatComponentTranslation var5 = new ChatComponentTranslation(key, var1);
        var5.setChatStyle(getChatStyle().createShallowCopy());
        Iterator var3 = getSiblings().iterator();

        while (var3.hasNext()) {
            IChatComponent var4 = (IChatComponent) var3.next();
            var5.appendSibling(var4.createCopy());
        }

        return var5;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else if (!(p_equals_1_ instanceof ChatComponentTranslation)) {
            return false;
        } else {
            ChatComponentTranslation var2 = (ChatComponentTranslation) p_equals_1_;
            return Arrays.equals(formatArgs, var2.formatArgs) && key.equals(var2.key) && super.equals(p_equals_1_);
        }
    }

    @Override
    public int hashCode() {
        int var1 = super.hashCode();
        var1 = 31 * var1 + key.hashCode();
        var1 = 31 * var1 + Arrays.hashCode(formatArgs);
        return var1;
    }

    @Override
    public String toString() {
        return "TranslatableComponent{key=\'" + key + '\'' + ", args=" + Arrays.toString(formatArgs) + ", siblings=" + siblings + ", style=" + getChatStyle() + '}';
    }

    public String getKey() {
        return key;
    }

    public Object[] getFormatArgs() {
        return formatArgs;
    }
}
