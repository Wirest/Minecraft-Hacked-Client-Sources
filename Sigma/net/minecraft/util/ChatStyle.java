package net.minecraft.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;

public class ChatStyle {
    /**
     * The parent of this ChatStyle. Used for looking up values that this
     * instance does not override.
     */
    private ChatStyle parentStyle;
    private EnumChatFormatting color;
    private Boolean bold;
    private Boolean italic;
    private Boolean underlined;
    private Boolean strikethrough;
    private Boolean obfuscated;
    private ClickEvent chatClickEvent;
    private HoverEvent chatHoverEvent;
    private String insertion;

    /**
     * The base of the ChatStyle hierarchy. All ChatStyle instances are
     * implicitly children of this.
     */
    private static final ChatStyle rootStyle = new ChatStyle() {
        private static final String __OBFID = "CL_00001267";

        @Override
        public EnumChatFormatting getColor() {
            return null;
        }

        @Override
        public boolean getBold() {
            return false;
        }

        @Override
        public boolean getItalic() {
            return false;
        }

        @Override
        public boolean getStrikethrough() {
            return false;
        }

        @Override
        public boolean getUnderlined() {
            return false;
        }

        @Override
        public boolean getObfuscated() {
            return false;
        }

        @Override
        public ClickEvent getChatClickEvent() {
            return null;
        }

        @Override
        public HoverEvent getChatHoverEvent() {
            return null;
        }

        @Override
        public String getInsertion() {
            return null;
        }

        @Override
        public ChatStyle setColor(EnumChatFormatting color) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setBold(Boolean p_150227_1_) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setItalic(Boolean italic) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setStrikethrough(Boolean strikethrough) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setUnderlined(Boolean underlined) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setObfuscated(Boolean obfuscated) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setChatClickEvent(ClickEvent event) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setChatHoverEvent(HoverEvent event) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setParentStyle(ChatStyle parent) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "Style.ROOT";
        }

        @Override
        public ChatStyle createShallowCopy() {
            return this;
        }

        @Override
        public ChatStyle createDeepCopy() {
            return this;
        }

        @Override
        public String getFormattingCode() {
            return "";
        }
    };
    private static final String __OBFID = "CL_00001266";

    /**
     * Gets the effective color of this ChatStyle.
     */
    public EnumChatFormatting getColor() {
        return color == null ? getParent().getColor() : color;
    }

    /**
     * Whether or not text of this ChatStyle should be in bold.
     */
    public boolean getBold() {
        return bold == null ? getParent().getBold() : bold.booleanValue();
    }

    /**
     * Whether or not text of this ChatStyle should be italicized.
     */
    public boolean getItalic() {
        return italic == null ? getParent().getItalic() : italic.booleanValue();
    }

    /**
     * Whether or not to format text of this ChatStyle using strikethrough.
     */
    public boolean getStrikethrough() {
        return strikethrough == null ? getParent().getStrikethrough() : strikethrough.booleanValue();
    }

    /**
     * Whether or not text of this ChatStyle should be underlined.
     */
    public boolean getUnderlined() {
        return underlined == null ? getParent().getUnderlined() : underlined.booleanValue();
    }

    /**
     * Whether or not text of this ChatStyle should be obfuscated.
     */
    public boolean getObfuscated() {
        return obfuscated == null ? getParent().getObfuscated() : obfuscated.booleanValue();
    }

    /**
     * Whether or not this style is empty (inherits everything from the parent).
     */
    public boolean isEmpty() {
        return bold == null && italic == null && strikethrough == null && underlined == null && obfuscated == null && color == null && chatClickEvent == null && chatHoverEvent == null;
    }

    /**
     * The effective chat click event.
     */
    public ClickEvent getChatClickEvent() {
        return chatClickEvent == null ? getParent().getChatClickEvent() : chatClickEvent;
    }

    /**
     * The effective chat hover event.
     */
    public HoverEvent getChatHoverEvent() {
        return chatHoverEvent == null ? getParent().getChatHoverEvent() : chatHoverEvent;
    }

    /**
     * Get the text to be inserted into Chat when the component is shift-clicked
     */
    public String getInsertion() {
        return insertion == null ? getParent().getInsertion() : insertion;
    }

    /**
     * Sets the color for this ChatStyle to the given value. Only use color
     * values for this; set other values using the specific methods.
     */
    public ChatStyle setColor(EnumChatFormatting color) {
        this.color = color;
        return this;
    }

    /**
     * Sets whether or not text of this ChatStyle should be in bold. Set to
     * false if, e.g., the parent style is bold and you want text of this style
     * to be unbolded.
     */
    public ChatStyle setBold(Boolean p_150227_1_) {
        bold = p_150227_1_;
        return this;
    }

    /**
     * Sets whether or not text of this ChatStyle should be italicized. Set to
     * false if, e.g., the parent style is italicized and you want to override
     * that for this style.
     */
    public ChatStyle setItalic(Boolean italic) {
        this.italic = italic;
        return this;
    }

    /**
     * Sets whether or not to format text of this ChatStyle using strikethrough.
     * Set to false if, e.g., the parent style uses strikethrough and you want
     * to override that for this style.
     */
    public ChatStyle setStrikethrough(Boolean strikethrough) {
        this.strikethrough = strikethrough;
        return this;
    }

    /**
     * Sets whether or not text of this ChatStyle should be underlined. Set to
     * false if, e.g., the parent style is underlined and you want to override
     * that for this style.
     */
    public ChatStyle setUnderlined(Boolean underlined) {
        this.underlined = underlined;
        return this;
    }

    /**
     * Sets whether or not text of this ChatStyle should be obfuscated. Set to
     * false if, e.g., the parent style is obfuscated and you want to override
     * that for this style.
     */
    public ChatStyle setObfuscated(Boolean obfuscated) {
        this.obfuscated = obfuscated;
        return this;
    }

    /**
     * Sets the event that should be run when text of this ChatStyle is clicked
     * on.
     */
    public ChatStyle setChatClickEvent(ClickEvent event) {
        chatClickEvent = event;
        return this;
    }

    /**
     * Sets the event that should be run when text of this ChatStyle is hovered
     * over.
     */
    public ChatStyle setChatHoverEvent(HoverEvent event) {
        chatHoverEvent = event;
        return this;
    }

    /**
     * Set a text to be inserted into Chat when the component is shift-clicked
     */
    public ChatStyle setInsertion(String insertion) {
        this.insertion = insertion;
        return this;
    }

    /**
     * Sets the fallback ChatStyle to use if this ChatStyle does not override
     * some value. Without a parent, obvious defaults are used (bold: false,
     * underlined: false, etc).
     */
    public ChatStyle setParentStyle(ChatStyle parent) {
        parentStyle = parent;
        return this;
    }

    /**
     * Gets the equivalent text formatting code for this style, without the
     * initial section sign (U+00A7) character.
     */
    public String getFormattingCode() {
        if (isEmpty()) {
            return parentStyle != null ? parentStyle.getFormattingCode() : "";
        } else {
            StringBuilder var1 = new StringBuilder();

            if (getColor() != null) {
                var1.append(getColor());
            }

            if (getBold()) {
                var1.append(EnumChatFormatting.BOLD);
            }

            if (getItalic()) {
                var1.append(EnumChatFormatting.ITALIC);
            }

            if (getUnderlined()) {
                var1.append(EnumChatFormatting.UNDERLINE);
            }

            if (getObfuscated()) {
                var1.append(EnumChatFormatting.OBFUSCATED);
            }

            if (getStrikethrough()) {
                var1.append(EnumChatFormatting.STRIKETHROUGH);
            }

            return var1.toString();
        }
    }

    /**
     * Gets the immediate parent of this ChatStyle.
     */
    private ChatStyle getParent() {
        return parentStyle == null ? ChatStyle.rootStyle : parentStyle;
    }

    @Override
    public String toString() {
        return "Style{hasParent=" + (parentStyle != null) + ", color=" + color + ", bold=" + bold + ", italic=" + italic + ", underlined=" + underlined + ", obfuscated=" + obfuscated + ", clickEvent=" + getChatClickEvent() + ", hoverEvent=" + getChatHoverEvent() + ", insertion=" + getInsertion() + '}';
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else if (!(p_equals_1_ instanceof ChatStyle)) {
            return false;
        } else {
            ChatStyle var2 = (ChatStyle) p_equals_1_;
            boolean var10000;

            if (getBold() == var2.getBold() && getColor() == var2.getColor() && getItalic() == var2.getItalic() && getObfuscated() == var2.getObfuscated() && getStrikethrough() == var2.getStrikethrough() && getUnderlined() == var2.getUnderlined()) {
                label65:
                {
                    if (getChatClickEvent() != null) {
                        if (!getChatClickEvent().equals(var2.getChatClickEvent())) {
                            break label65;
                        }
                    } else if (var2.getChatClickEvent() != null) {
                        break label65;
                    }

                    if (getChatHoverEvent() != null) {
                        if (!getChatHoverEvent().equals(var2.getChatHoverEvent())) {
                            break label65;
                        }
                    } else if (var2.getChatHoverEvent() != null) {
                        break label65;
                    }

                    if (getInsertion() != null) {
                        if (!getInsertion().equals(var2.getInsertion())) {
                            break label65;
                        }
                    } else if (var2.getInsertion() != null) {
                        break label65;
                    }

                    var10000 = true;
                    return var10000;
                }
            }

            var10000 = false;
            return var10000;
        }
    }

    @Override
    public int hashCode() {
        int var1 = color.hashCode();
        var1 = 31 * var1 + bold.hashCode();
        var1 = 31 * var1 + italic.hashCode();
        var1 = 31 * var1 + underlined.hashCode();
        var1 = 31 * var1 + strikethrough.hashCode();
        var1 = 31 * var1 + obfuscated.hashCode();
        var1 = 31 * var1 + chatClickEvent.hashCode();
        var1 = 31 * var1 + chatHoverEvent.hashCode();
        var1 = 31 * var1 + insertion.hashCode();
        return var1;
    }

    /**
     * Creates a shallow copy of this style. Changes to this instance's values
     * will not be reflected in the copy, but changes to the parent style's
     * values WILL be reflected in both this instance and the copy, wherever
     * either does not override a value.
     */
    public ChatStyle createShallowCopy() {
        ChatStyle var1 = new ChatStyle();
        var1.bold = bold;
        var1.italic = italic;
        var1.strikethrough = strikethrough;
        var1.underlined = underlined;
        var1.obfuscated = obfuscated;
        var1.color = color;
        var1.chatClickEvent = chatClickEvent;
        var1.chatHoverEvent = chatHoverEvent;
        var1.parentStyle = parentStyle;
        var1.insertion = insertion;
        return var1;
    }

    /**
     * Creates a deep copy of this style. No changes to this instance or its
     * parent style will be reflected in the copy.
     */
    public ChatStyle createDeepCopy() {
        ChatStyle var1 = new ChatStyle();
        var1.setBold(Boolean.valueOf(getBold()));
        var1.setItalic(Boolean.valueOf(getItalic()));
        var1.setStrikethrough(Boolean.valueOf(getStrikethrough()));
        var1.setUnderlined(Boolean.valueOf(getUnderlined()));
        var1.setObfuscated(Boolean.valueOf(getObfuscated()));
        var1.setColor(getColor());
        var1.setChatClickEvent(getChatClickEvent());
        var1.setChatHoverEvent(getChatHoverEvent());
        var1.setInsertion(getInsertion());
        return var1;
    }

    public static class Serializer implements JsonDeserializer, JsonSerializer {
        private static final String __OBFID = "CL_00001268";

        @Override
        public ChatStyle deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) {
            if (p_deserialize_1_.isJsonObject()) {
                ChatStyle var4 = new ChatStyle();
                JsonObject var5 = p_deserialize_1_.getAsJsonObject();

                if (var5 == null) {
                    return null;
                } else {
                    if (var5.has("bold")) {
                        var4.bold = Boolean.valueOf(var5.get("bold").getAsBoolean());
                    }

                    if (var5.has("italic")) {
                        var4.italic = Boolean.valueOf(var5.get("italic").getAsBoolean());
                    }

                    if (var5.has("underlined")) {
                        var4.underlined = Boolean.valueOf(var5.get("underlined").getAsBoolean());
                    }

                    if (var5.has("strikethrough")) {
                        var4.strikethrough = Boolean.valueOf(var5.get("strikethrough").getAsBoolean());
                    }

                    if (var5.has("obfuscated")) {
                        var4.obfuscated = Boolean.valueOf(var5.get("obfuscated").getAsBoolean());
                    }

                    if (var5.has("color")) {
                        var4.color = (EnumChatFormatting) p_deserialize_3_.deserialize(var5.get("color"), EnumChatFormatting.class);
                    }

                    if (var5.has("insertion")) {
                        var4.insertion = var5.get("insertion").getAsString();
                    }

                    JsonObject var6;
                    JsonPrimitive var7;

                    if (var5.has("clickEvent")) {
                        var6 = var5.getAsJsonObject("clickEvent");

                        if (var6 != null) {
                            var7 = var6.getAsJsonPrimitive("action");
                            ClickEvent.Action var8 = var7 == null ? null : ClickEvent.Action.getValueByCanonicalName(var7.getAsString());
                            JsonPrimitive var9 = var6.getAsJsonPrimitive("value");
                            String var10 = var9 == null ? null : var9.getAsString();

                            if (var8 != null && var10 != null && var8.shouldAllowInChat()) {
                                var4.chatClickEvent = new ClickEvent(var8, var10);
                            }
                        }
                    }

                    if (var5.has("hoverEvent")) {
                        var6 = var5.getAsJsonObject("hoverEvent");

                        if (var6 != null) {
                            var7 = var6.getAsJsonPrimitive("action");
                            HoverEvent.Action var11 = var7 == null ? null : HoverEvent.Action.getValueByCanonicalName(var7.getAsString());
                            IChatComponent var12 = (IChatComponent) p_deserialize_3_.deserialize(var6.get("value"), IChatComponent.class);

                            if (var11 != null && var12 != null && var11.shouldAllowInChat()) {
                                var4.chatHoverEvent = new HoverEvent(var11, var12);
                            }
                        }
                    }

                    return var4;
                }
            } else {
                return null;
            }
        }

        public JsonElement serialize(ChatStyle p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
            if (p_serialize_1_.isEmpty()) {
                return null;
            } else {
                JsonObject var4 = new JsonObject();

                if (p_serialize_1_.bold != null) {
                    var4.addProperty("bold", p_serialize_1_.bold);
                }

                if (p_serialize_1_.italic != null) {
                    var4.addProperty("italic", p_serialize_1_.italic);
                }

                if (p_serialize_1_.underlined != null) {
                    var4.addProperty("underlined", p_serialize_1_.underlined);
                }

                if (p_serialize_1_.strikethrough != null) {
                    var4.addProperty("strikethrough", p_serialize_1_.strikethrough);
                }

                if (p_serialize_1_.obfuscated != null) {
                    var4.addProperty("obfuscated", p_serialize_1_.obfuscated);
                }

                if (p_serialize_1_.color != null) {
                    var4.add("color", p_serialize_3_.serialize(p_serialize_1_.color));
                }

                if (p_serialize_1_.insertion != null) {
                    var4.add("insertion", p_serialize_3_.serialize(p_serialize_1_.insertion));
                }

                JsonObject var5;

                if (p_serialize_1_.chatClickEvent != null) {
                    var5 = new JsonObject();
                    var5.addProperty("action", p_serialize_1_.chatClickEvent.getAction().getCanonicalName());
                    var5.addProperty("value", p_serialize_1_.chatClickEvent.getValue());
                    var4.add("clickEvent", var5);
                }

                if (p_serialize_1_.chatHoverEvent != null) {
                    var5 = new JsonObject();
                    var5.addProperty("action", p_serialize_1_.chatHoverEvent.getAction().getCanonicalName());
                    var5.add("value", p_serialize_3_.serialize(p_serialize_1_.chatHoverEvent.getValue()));
                    var4.add("hoverEvent", var5);
                }

                return var4;
            }
        }

        @Override
        public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
            return this.serialize((ChatStyle) p_serialize_1_, p_serialize_2_, p_serialize_3_);
        }
    }
}
