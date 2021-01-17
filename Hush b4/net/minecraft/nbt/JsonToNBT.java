// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.nbt;

import com.google.common.collect.Iterables;
import com.google.common.base.Splitter;
import java.util.Iterator;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Stack;
import org.apache.logging.log4j.LogManager;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;

public class JsonToNBT
{
    private static final Logger logger;
    private static final Pattern field_179273_b;
    
    static {
        logger = LogManager.getLogger();
        field_179273_b = Pattern.compile("\\[[-+\\d|,\\s]+\\]");
    }
    
    public static NBTTagCompound getTagFromJson(String jsonString) throws NBTException {
        jsonString = jsonString.trim();
        if (!jsonString.startsWith("{")) {
            throw new NBTException("Invalid tag encountered, expected '{' as first char.");
        }
        if (func_150310_b(jsonString) != 1) {
            throw new NBTException("Encountered multiple top tags, only one expected");
        }
        return (NBTTagCompound)func_150316_a("tag", jsonString).parse();
    }
    
    static int func_150310_b(final String p_150310_0_) throws NBTException {
        int i = 0;
        boolean flag = false;
        final Stack<Character> stack = new Stack<Character>();
        for (int j = 0; j < p_150310_0_.length(); ++j) {
            final char c0 = p_150310_0_.charAt(j);
            if (c0 == '\"') {
                if (func_179271_b(p_150310_0_, j)) {
                    if (!flag) {
                        throw new NBTException("Illegal use of \\\": " + p_150310_0_);
                    }
                }
                else {
                    flag = !flag;
                }
            }
            else if (!flag) {
                if (c0 != '{' && c0 != '[') {
                    if (c0 == '}' && (stack.isEmpty() || stack.pop() != '{')) {
                        throw new NBTException("Unbalanced curly brackets {}: " + p_150310_0_);
                    }
                    if (c0 == ']' && (stack.isEmpty() || stack.pop() != '[')) {
                        throw new NBTException("Unbalanced square brackets []: " + p_150310_0_);
                    }
                }
                else {
                    if (stack.isEmpty()) {
                        ++i;
                    }
                    stack.push(c0);
                }
            }
        }
        if (flag) {
            throw new NBTException("Unbalanced quotation: " + p_150310_0_);
        }
        if (!stack.isEmpty()) {
            throw new NBTException("Unbalanced brackets: " + p_150310_0_);
        }
        if (i == 0 && !p_150310_0_.isEmpty()) {
            i = 1;
        }
        return i;
    }
    
    static Any func_179272_a(final String... p_179272_0_) throws NBTException {
        return func_150316_a(p_179272_0_[0], p_179272_0_[1]);
    }
    
    static Any func_150316_a(final String p_150316_0_, String p_150316_1_) throws NBTException {
        p_150316_1_ = p_150316_1_.trim();
        if (p_150316_1_.startsWith("{")) {
            p_150316_1_ = p_150316_1_.substring(1, p_150316_1_.length() - 1);
            final Compound jsontonbt$compound = new Compound(p_150316_0_);
            while (p_150316_1_.length() > 0) {
                final String s1 = func_150314_a(p_150316_1_, true);
                if (s1.length() > 0) {
                    final boolean flag1 = false;
                    jsontonbt$compound.field_150491_b.add(func_179270_a(s1, flag1));
                }
                if (p_150316_1_.length() < s1.length() + 1) {
                    break;
                }
                final char c1 = p_150316_1_.charAt(s1.length());
                if (c1 != ',' && c1 != '{' && c1 != '}' && c1 != '[' && c1 != ']') {
                    throw new NBTException("Unexpected token '" + c1 + "' at: " + p_150316_1_.substring(s1.length()));
                }
                p_150316_1_ = p_150316_1_.substring(s1.length() + 1);
            }
            return jsontonbt$compound;
        }
        if (p_150316_1_.startsWith("[") && !JsonToNBT.field_179273_b.matcher(p_150316_1_).matches()) {
            p_150316_1_ = p_150316_1_.substring(1, p_150316_1_.length() - 1);
            final List jsontonbt$list = new List(p_150316_0_);
            while (p_150316_1_.length() > 0) {
                final String s2 = func_150314_a(p_150316_1_, false);
                if (s2.length() > 0) {
                    final boolean flag2 = true;
                    jsontonbt$list.field_150492_b.add(func_179270_a(s2, flag2));
                }
                if (p_150316_1_.length() < s2.length() + 1) {
                    break;
                }
                final char c2 = p_150316_1_.charAt(s2.length());
                if (c2 != ',' && c2 != '{' && c2 != '}' && c2 != '[' && c2 != ']') {
                    throw new NBTException("Unexpected token '" + c2 + "' at: " + p_150316_1_.substring(s2.length()));
                }
                p_150316_1_ = p_150316_1_.substring(s2.length() + 1);
            }
            return jsontonbt$list;
        }
        return new Primitive(p_150316_0_, p_150316_1_);
    }
    
    private static Any func_179270_a(final String p_179270_0_, final boolean p_179270_1_) throws NBTException {
        final String s = func_150313_b(p_179270_0_, p_179270_1_);
        final String s2 = func_150311_c(p_179270_0_, p_179270_1_);
        return func_179272_a(s, s2);
    }
    
    private static String func_150314_a(final String p_150314_0_, final boolean p_150314_1_) throws NBTException {
        int i = func_150312_a(p_150314_0_, ':');
        final int j = func_150312_a(p_150314_0_, ',');
        if (p_150314_1_) {
            if (i == -1) {
                throw new NBTException("Unable to locate name/value separator for string: " + p_150314_0_);
            }
            if (j != -1 && j < i) {
                throw new NBTException("Name error at: " + p_150314_0_);
            }
        }
        else if (i == -1 || i > j) {
            i = -1;
        }
        return func_179269_a(p_150314_0_, i);
    }
    
    private static String func_179269_a(final String p_179269_0_, final int p_179269_1_) throws NBTException {
        final Stack<Character> stack = new Stack<Character>();
        int i = p_179269_1_ + 1;
        boolean flag = false;
        boolean flag2 = false;
        boolean flag3 = false;
        int j = 0;
        while (i < p_179269_0_.length()) {
            final char c0 = p_179269_0_.charAt(i);
            if (c0 == '\"') {
                if (func_179271_b(p_179269_0_, i)) {
                    if (!flag) {
                        throw new NBTException("Illegal use of \\\": " + p_179269_0_);
                    }
                }
                else {
                    flag = !flag;
                    if (flag && !flag3) {
                        flag2 = true;
                    }
                    if (!flag) {
                        j = i;
                    }
                }
            }
            else if (!flag) {
                if (c0 != '{' && c0 != '[') {
                    if (c0 == '}' && (stack.isEmpty() || stack.pop() != '{')) {
                        throw new NBTException("Unbalanced curly brackets {}: " + p_179269_0_);
                    }
                    if (c0 == ']' && (stack.isEmpty() || stack.pop() != '[')) {
                        throw new NBTException("Unbalanced square brackets []: " + p_179269_0_);
                    }
                    if (c0 == ',' && stack.isEmpty()) {
                        return p_179269_0_.substring(0, i);
                    }
                }
                else {
                    stack.push(c0);
                }
            }
            if (!Character.isWhitespace(c0)) {
                if (!flag && flag2 && j != i) {
                    return p_179269_0_.substring(0, j + 1);
                }
                flag3 = true;
            }
            ++i;
        }
        return p_179269_0_.substring(0, i);
    }
    
    private static String func_150313_b(String p_150313_0_, final boolean p_150313_1_) throws NBTException {
        if (p_150313_1_) {
            p_150313_0_ = p_150313_0_.trim();
            if (p_150313_0_.startsWith("{") || p_150313_0_.startsWith("[")) {
                return "";
            }
        }
        final int i = func_150312_a(p_150313_0_, ':');
        if (i != -1) {
            return p_150313_0_.substring(0, i).trim();
        }
        if (p_150313_1_) {
            return "";
        }
        throw new NBTException("Unable to locate name/value separator for string: " + p_150313_0_);
    }
    
    private static String func_150311_c(String p_150311_0_, final boolean p_150311_1_) throws NBTException {
        if (p_150311_1_) {
            p_150311_0_ = p_150311_0_.trim();
            if (p_150311_0_.startsWith("{") || p_150311_0_.startsWith("[")) {
                return p_150311_0_;
            }
        }
        final int i = func_150312_a(p_150311_0_, ':');
        if (i != -1) {
            return p_150311_0_.substring(i + 1).trim();
        }
        if (p_150311_1_) {
            return p_150311_0_;
        }
        throw new NBTException("Unable to locate name/value separator for string: " + p_150311_0_);
    }
    
    private static int func_150312_a(final String p_150312_0_, final char p_150312_1_) {
        int i = 0;
        boolean flag = true;
        while (i < p_150312_0_.length()) {
            final char c0 = p_150312_0_.charAt(i);
            if (c0 == '\"') {
                if (!func_179271_b(p_150312_0_, i)) {
                    flag = !flag;
                }
            }
            else if (flag) {
                if (c0 == p_150312_1_) {
                    return i;
                }
                if (c0 == '{' || c0 == '[') {
                    return -1;
                }
            }
            ++i;
        }
        return -1;
    }
    
    private static boolean func_179271_b(final String p_179271_0_, final int p_179271_1_) {
        return p_179271_1_ > 0 && p_179271_0_.charAt(p_179271_1_ - 1) == '\\' && !func_179271_b(p_179271_0_, p_179271_1_ - 1);
    }
    
    abstract static class Any
    {
        protected String json;
        
        public abstract NBTBase parse() throws NBTException;
    }
    
    static class Compound extends Any
    {
        protected java.util.List<Any> field_150491_b;
        
        public Compound(final String p_i45137_1_) {
            this.field_150491_b = (java.util.List<Any>)Lists.newArrayList();
            this.json = p_i45137_1_;
        }
        
        @Override
        public NBTBase parse() throws NBTException {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            for (final Any jsontonbt$any : this.field_150491_b) {
                nbttagcompound.setTag(jsontonbt$any.json, jsontonbt$any.parse());
            }
            return nbttagcompound;
        }
    }
    
    static class List extends Any
    {
        protected java.util.List<Any> field_150492_b;
        
        public List(final String json) {
            this.field_150492_b = (java.util.List<Any>)Lists.newArrayList();
            this.json = json;
        }
        
        @Override
        public NBTBase parse() throws NBTException {
            final NBTTagList nbttaglist = new NBTTagList();
            for (final Any jsontonbt$any : this.field_150492_b) {
                nbttaglist.appendTag(jsontonbt$any.parse());
            }
            return nbttaglist;
        }
    }
    
    static class Primitive extends Any
    {
        private static final Pattern DOUBLE;
        private static final Pattern FLOAT;
        private static final Pattern BYTE;
        private static final Pattern LONG;
        private static final Pattern SHORT;
        private static final Pattern INTEGER;
        private static final Pattern DOUBLE_UNTYPED;
        private static final Splitter SPLITTER;
        protected String jsonValue;
        
        static {
            DOUBLE = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+[d|D]");
            FLOAT = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+[f|F]");
            BYTE = Pattern.compile("[-+]?[0-9]+[b|B]");
            LONG = Pattern.compile("[-+]?[0-9]+[l|L]");
            SHORT = Pattern.compile("[-+]?[0-9]+[s|S]");
            INTEGER = Pattern.compile("[-+]?[0-9]+");
            DOUBLE_UNTYPED = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+");
            SPLITTER = Splitter.on(',').omitEmptyStrings();
        }
        
        public Primitive(final String p_i45139_1_, final String p_i45139_2_) {
            this.json = p_i45139_1_;
            this.jsonValue = p_i45139_2_;
        }
        
        @Override
        public NBTBase parse() throws NBTException {
            try {
                if (Primitive.DOUBLE.matcher(this.jsonValue).matches()) {
                    return new NBTTagDouble(Double.parseDouble(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
                }
                if (Primitive.FLOAT.matcher(this.jsonValue).matches()) {
                    return new NBTTagFloat(Float.parseFloat(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
                }
                if (Primitive.BYTE.matcher(this.jsonValue).matches()) {
                    return new NBTTagByte(Byte.parseByte(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
                }
                if (Primitive.LONG.matcher(this.jsonValue).matches()) {
                    return new NBTTagLong(Long.parseLong(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
                }
                if (Primitive.SHORT.matcher(this.jsonValue).matches()) {
                    return new NBTTagShort(Short.parseShort(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
                }
                if (Primitive.INTEGER.matcher(this.jsonValue).matches()) {
                    return new NBTTagInt(Integer.parseInt(this.jsonValue));
                }
                if (Primitive.DOUBLE_UNTYPED.matcher(this.jsonValue).matches()) {
                    return new NBTTagDouble(Double.parseDouble(this.jsonValue));
                }
                if (this.jsonValue.equalsIgnoreCase("true") || this.jsonValue.equalsIgnoreCase("false")) {
                    return new NBTTagByte((byte)(Boolean.parseBoolean(this.jsonValue) ? 1 : 0));
                }
            }
            catch (NumberFormatException var6) {
                this.jsonValue = this.jsonValue.replaceAll("\\\\\"", "\"");
                return new NBTTagString(this.jsonValue);
            }
            if (this.jsonValue.startsWith("[") && this.jsonValue.endsWith("]")) {
                final String s = this.jsonValue.substring(1, this.jsonValue.length() - 1);
                final String[] astring = Iterables.toArray(Primitive.SPLITTER.split(s), String.class);
                try {
                    final int[] aint = new int[astring.length];
                    for (int j = 0; j < astring.length; ++j) {
                        aint[j] = Integer.parseInt(astring[j].trim());
                    }
                    return new NBTTagIntArray(aint);
                }
                catch (NumberFormatException var7) {
                    return new NBTTagString(this.jsonValue);
                }
            }
            if (this.jsonValue.startsWith("\"") && this.jsonValue.endsWith("\"")) {
                this.jsonValue = this.jsonValue.substring(1, this.jsonValue.length() - 1);
            }
            this.jsonValue = this.jsonValue.replaceAll("\\\\\"", "\"");
            final StringBuilder stringbuilder = new StringBuilder();
            for (int i = 0; i < this.jsonValue.length(); ++i) {
                if (i < this.jsonValue.length() - 1 && this.jsonValue.charAt(i) == '\\' && this.jsonValue.charAt(i + 1) == '\\') {
                    stringbuilder.append('\\');
                    ++i;
                }
                else {
                    stringbuilder.append(this.jsonValue.charAt(i));
                }
            }
            return new NBTTagString(stringbuilder.toString());
        }
    }
}
