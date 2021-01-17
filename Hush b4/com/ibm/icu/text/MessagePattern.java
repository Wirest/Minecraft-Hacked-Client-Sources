// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.impl.ICUConfig;
import java.util.Locale;
import com.ibm.icu.impl.PatternProps;
import java.util.ArrayList;
import com.ibm.icu.util.Freezable;

public final class MessagePattern implements Cloneable, Freezable<MessagePattern>
{
    public static final int ARG_NAME_NOT_NUMBER = -1;
    public static final int ARG_NAME_NOT_VALID = -2;
    public static final double NO_NUMERIC_VALUE = -1.23456789E8;
    private static final int MAX_PREFIX_LENGTH = 24;
    private ApostropheMode aposMode;
    private String msg;
    private ArrayList<Part> parts;
    private ArrayList<Double> numericValues;
    private boolean hasArgNames;
    private boolean hasArgNumbers;
    private boolean needsAutoQuoting;
    private boolean frozen;
    private static final ApostropheMode defaultAposMode;
    private static final ArgType[] argTypes;
    
    public MessagePattern() {
        this.parts = new ArrayList<Part>();
        this.aposMode = MessagePattern.defaultAposMode;
    }
    
    public MessagePattern(final ApostropheMode mode) {
        this.parts = new ArrayList<Part>();
        this.aposMode = mode;
    }
    
    public MessagePattern(final String pattern) {
        this.parts = new ArrayList<Part>();
        this.aposMode = MessagePattern.defaultAposMode;
        this.parse(pattern);
    }
    
    public MessagePattern parse(final String pattern) {
        this.preParse(pattern);
        this.parseMessage(0, 0, 0, ArgType.NONE);
        this.postParse();
        return this;
    }
    
    public MessagePattern parseChoiceStyle(final String pattern) {
        this.preParse(pattern);
        this.parseChoiceStyle(0, 0);
        this.postParse();
        return this;
    }
    
    public MessagePattern parsePluralStyle(final String pattern) {
        this.preParse(pattern);
        this.parsePluralOrSelectStyle(ArgType.PLURAL, 0, 0);
        this.postParse();
        return this;
    }
    
    public MessagePattern parseSelectStyle(final String pattern) {
        this.preParse(pattern);
        this.parsePluralOrSelectStyle(ArgType.SELECT, 0, 0);
        this.postParse();
        return this;
    }
    
    public void clear() {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to clear() a frozen MessagePattern instance.");
        }
        this.msg = null;
        final boolean b = false;
        this.hasArgNumbers = b;
        this.hasArgNames = b;
        this.needsAutoQuoting = false;
        this.parts.clear();
        if (this.numericValues != null) {
            this.numericValues.clear();
        }
    }
    
    public void clearPatternAndSetApostropheMode(final ApostropheMode mode) {
        this.clear();
        this.aposMode = mode;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final MessagePattern o = (MessagePattern)other;
        if (this.aposMode.equals(o.aposMode)) {
            if (this.msg == null) {
                if (o.msg != null) {
                    return false;
                }
            }
            else if (!this.msg.equals(o.msg)) {
                return false;
            }
            if (this.parts.equals(o.parts)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (this.aposMode.hashCode() * 37 + ((this.msg != null) ? this.msg.hashCode() : 0)) * 37 + this.parts.hashCode();
    }
    
    public ApostropheMode getApostropheMode() {
        return this.aposMode;
    }
    
    boolean jdkAposMode() {
        return this.aposMode == ApostropheMode.DOUBLE_REQUIRED;
    }
    
    public String getPatternString() {
        return this.msg;
    }
    
    public boolean hasNamedArguments() {
        return this.hasArgNames;
    }
    
    public boolean hasNumberedArguments() {
        return this.hasArgNumbers;
    }
    
    @Override
    public String toString() {
        return this.msg;
    }
    
    public static int validateArgumentName(final String name) {
        if (!PatternProps.isIdentifier(name)) {
            return -2;
        }
        return parseArgNumber(name, 0, name.length());
    }
    
    public String autoQuoteApostropheDeep() {
        if (!this.needsAutoQuoting) {
            return this.msg;
        }
        StringBuilder modified = null;
        int i;
        final int count = i = this.countParts();
        while (i > 0) {
            final Part part;
            if ((part = this.getPart(--i)).getType() == Part.Type.INSERT_CHAR) {
                if (modified == null) {
                    modified = new StringBuilder(this.msg.length() + 10).append(this.msg);
                }
                modified.insert(part.index, (char)part.value);
            }
        }
        if (modified == null) {
            return this.msg;
        }
        return modified.toString();
    }
    
    public int countParts() {
        return this.parts.size();
    }
    
    public Part getPart(final int i) {
        return this.parts.get(i);
    }
    
    public Part.Type getPartType(final int i) {
        return this.parts.get(i).type;
    }
    
    public int getPatternIndex(final int partIndex) {
        return this.parts.get(partIndex).index;
    }
    
    public String getSubstring(final Part part) {
        final int index = part.index;
        return this.msg.substring(index, index + part.length);
    }
    
    public boolean partSubstringMatches(final Part part, final String s) {
        return this.msg.regionMatches(part.index, s, 0, part.length);
    }
    
    public double getNumericValue(final Part part) {
        final Part.Type type = part.type;
        if (type == Part.Type.ARG_INT) {
            return part.value;
        }
        if (type == Part.Type.ARG_DOUBLE) {
            return this.numericValues.get(part.value);
        }
        return -1.23456789E8;
    }
    
    public double getPluralOffset(final int pluralStart) {
        final Part part = this.parts.get(pluralStart);
        if (part.type.hasNumericValue()) {
            return this.getNumericValue(part);
        }
        return 0.0;
    }
    
    public int getLimitPartIndex(final int start) {
        final int limit = this.parts.get(start).limitPartIndex;
        if (limit < start) {
            return start;
        }
        return limit;
    }
    
    public Object clone() {
        if (this.isFrozen()) {
            return this;
        }
        return this.cloneAsThawed();
    }
    
    public MessagePattern cloneAsThawed() {
        MessagePattern newMsg;
        try {
            newMsg = (MessagePattern)super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        newMsg.parts = (ArrayList<Part>)this.parts.clone();
        if (this.numericValues != null) {
            newMsg.numericValues = (ArrayList<Double>)this.numericValues.clone();
        }
        newMsg.frozen = false;
        return newMsg;
    }
    
    public MessagePattern freeze() {
        this.frozen = true;
        return this;
    }
    
    public boolean isFrozen() {
        return this.frozen;
    }
    
    private void preParse(final String pattern) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to parse(" + prefix(pattern) + ") on frozen MessagePattern instance.");
        }
        this.msg = pattern;
        final boolean b = false;
        this.hasArgNumbers = b;
        this.hasArgNames = b;
        this.needsAutoQuoting = false;
        this.parts.clear();
        if (this.numericValues != null) {
            this.numericValues.clear();
        }
    }
    
    private void postParse() {
    }
    
    private int parseMessage(int index, final int msgStartLength, final int nestingLevel, final ArgType parentType) {
        if (nestingLevel > 32767) {
            throw new IndexOutOfBoundsException();
        }
        final int msgStart = this.parts.size();
        this.addPart(Part.Type.MSG_START, index, msgStartLength, nestingLevel);
        index += msgStartLength;
        while (index < this.msg.length()) {
            char c = this.msg.charAt(index++);
            if (c == '\'') {
                if (index == this.msg.length()) {
                    this.addPart(Part.Type.INSERT_CHAR, index, 0, 39);
                    this.needsAutoQuoting = true;
                }
                else {
                    c = this.msg.charAt(index);
                    if (c == '\'') {
                        this.addPart(Part.Type.SKIP_SYNTAX, index++, 1, 0);
                    }
                    else if (this.aposMode == ApostropheMode.DOUBLE_REQUIRED || c == '{' || c == '}' || (parentType == ArgType.CHOICE && c == '|') || (parentType.hasPluralStyle() && c == '#')) {
                        this.addPart(Part.Type.SKIP_SYNTAX, index - 1, 1, 0);
                        while (true) {
                            index = this.msg.indexOf(39, index + 1);
                            if (index < 0) {
                                index = this.msg.length();
                                this.addPart(Part.Type.INSERT_CHAR, index, 0, 39);
                                this.needsAutoQuoting = true;
                                break;
                            }
                            if (index + 1 >= this.msg.length() || this.msg.charAt(index + 1) != '\'') {
                                this.addPart(Part.Type.SKIP_SYNTAX, index++, 1, 0);
                                break;
                            }
                            this.addPart(Part.Type.SKIP_SYNTAX, ++index, 1, 0);
                        }
                    }
                    else {
                        this.addPart(Part.Type.INSERT_CHAR, index, 0, 39);
                        this.needsAutoQuoting = true;
                    }
                }
            }
            else if (parentType.hasPluralStyle() && c == '#') {
                this.addPart(Part.Type.REPLACE_NUMBER, index - 1, 1, 0);
            }
            else if (c == '{') {
                index = this.parseArg(index - 1, 1, nestingLevel);
            }
            else {
                if ((nestingLevel <= 0 || c != '}') && (parentType != ArgType.CHOICE || c != '|')) {
                    continue;
                }
                final int limitLength = (parentType != ArgType.CHOICE || c != '}') ? 1 : 0;
                this.addLimitPart(msgStart, Part.Type.MSG_LIMIT, index - 1, limitLength, nestingLevel);
                if (parentType == ArgType.CHOICE) {
                    return index - 1;
                }
                return index;
            }
        }
        if (nestingLevel > 0 && !this.inTopLevelChoiceMessage(nestingLevel, parentType)) {
            throw new IllegalArgumentException("Unmatched '{' braces in message " + this.prefix());
        }
        this.addLimitPart(msgStart, Part.Type.MSG_LIMIT, index, 0, nestingLevel);
        return index;
    }
    
    private int parseArg(int index, final int argStartLength, final int nestingLevel) {
        final int argStart = this.parts.size();
        ArgType argType = ArgType.NONE;
        this.addPart(Part.Type.ARG_START, index, argStartLength, argType.ordinal());
        final int nameIndex;
        index = (nameIndex = this.skipWhiteSpace(index + argStartLength));
        if (index == this.msg.length()) {
            throw new IllegalArgumentException("Unmatched '{' braces in message " + this.prefix());
        }
        index = this.skipIdentifier(index);
        final int number = this.parseArgNumber(nameIndex, index);
        if (number >= 0) {
            final int length = index - nameIndex;
            if (length > 65535 || number > 32767) {
                throw new IndexOutOfBoundsException("Argument number too large: " + this.prefix(nameIndex));
            }
            this.hasArgNumbers = true;
            this.addPart(Part.Type.ARG_NUMBER, nameIndex, length, number);
        }
        else {
            if (number != -1) {
                throw new IllegalArgumentException("Bad argument syntax: " + this.prefix(nameIndex));
            }
            final int length = index - nameIndex;
            if (length > 65535) {
                throw new IndexOutOfBoundsException("Argument name too long: " + this.prefix(nameIndex));
            }
            this.hasArgNames = true;
            this.addPart(Part.Type.ARG_NAME, nameIndex, length, 0);
        }
        index = this.skipWhiteSpace(index);
        if (index == this.msg.length()) {
            throw new IllegalArgumentException("Unmatched '{' braces in message " + this.prefix());
        }
        char c = this.msg.charAt(index);
        if (c != '}') {
            if (c != ',') {
                throw new IllegalArgumentException("Bad argument syntax: " + this.prefix(nameIndex));
            }
            int typeIndex;
            for (index = (typeIndex = this.skipWhiteSpace(index + 1)); index < this.msg.length() && isArgTypeChar(this.msg.charAt(index)); ++index) {}
            final int length2 = index - typeIndex;
            index = this.skipWhiteSpace(index);
            if (index == this.msg.length()) {
                throw new IllegalArgumentException("Unmatched '{' braces in message " + this.prefix());
            }
            if (length2 == 0 || ((c = this.msg.charAt(index)) != ',' && c != '}')) {
                throw new IllegalArgumentException("Bad argument syntax: " + this.prefix(nameIndex));
            }
            if (length2 > 65535) {
                throw new IndexOutOfBoundsException("Argument type name too long: " + this.prefix(nameIndex));
            }
            argType = ArgType.SIMPLE;
            if (length2 == 6) {
                if (this.isChoice(typeIndex)) {
                    argType = ArgType.CHOICE;
                }
                else if (this.isPlural(typeIndex)) {
                    argType = ArgType.PLURAL;
                }
                else if (this.isSelect(typeIndex)) {
                    argType = ArgType.SELECT;
                }
            }
            else if (length2 == 13 && this.isSelect(typeIndex) && this.isOrdinal(typeIndex + 6)) {
                argType = ArgType.SELECTORDINAL;
            }
            this.parts.get(argStart).value = (short)argType.ordinal();
            if (argType == ArgType.SIMPLE) {
                this.addPart(Part.Type.ARG_TYPE, typeIndex, length2, 0);
            }
            if (c == '}') {
                if (argType != ArgType.SIMPLE) {
                    throw new IllegalArgumentException("No style field for complex argument: " + this.prefix(nameIndex));
                }
            }
            else {
                ++index;
                if (argType == ArgType.SIMPLE) {
                    index = this.parseSimpleStyle(index);
                }
                else if (argType == ArgType.CHOICE) {
                    index = this.parseChoiceStyle(index, nestingLevel);
                }
                else {
                    index = this.parsePluralOrSelectStyle(argType, index, nestingLevel);
                }
            }
        }
        this.addLimitPart(argStart, Part.Type.ARG_LIMIT, index, 1, argType.ordinal());
        return index + 1;
    }
    
    private int parseSimpleStyle(int index) {
        final int start = index;
        int nestedBraces = 0;
        while (index < this.msg.length()) {
            final char c = this.msg.charAt(index++);
            if (c == '\'') {
                index = this.msg.indexOf(39, index);
                if (index < 0) {
                    throw new IllegalArgumentException("Quoted literal argument style text reaches to the end of the message: " + this.prefix(start));
                }
                ++index;
            }
            else if (c == '{') {
                ++nestedBraces;
            }
            else {
                if (c != '}') {
                    continue;
                }
                if (nestedBraces > 0) {
                    --nestedBraces;
                }
                else {
                    final int length = --index - start;
                    if (length > 65535) {
                        throw new IndexOutOfBoundsException("Argument style text too long: " + this.prefix(start));
                    }
                    this.addPart(Part.Type.ARG_STYLE, start, length, 0);
                    return index;
                }
            }
        }
        throw new IllegalArgumentException("Unmatched '{' braces in message " + this.prefix());
    }
    
    private int parseChoiceStyle(int index, final int nestingLevel) {
        final int start = index;
        index = this.skipWhiteSpace(index);
        if (index == this.msg.length() || this.msg.charAt(index) == '}') {
            throw new IllegalArgumentException("Missing choice argument pattern in " + this.prefix());
        }
        while (true) {
            final int numberIndex = index;
            index = this.skipDouble(index);
            final int length = index - numberIndex;
            if (length == 0) {
                throw new IllegalArgumentException("Bad choice pattern syntax: " + this.prefix(start));
            }
            if (length > 65535) {
                throw new IndexOutOfBoundsException("Choice number too long: " + this.prefix(numberIndex));
            }
            this.parseDouble(numberIndex, index, true);
            index = this.skipWhiteSpace(index);
            if (index == this.msg.length()) {
                throw new IllegalArgumentException("Bad choice pattern syntax: " + this.prefix(start));
            }
            final char c = this.msg.charAt(index);
            if (c != '#' && c != '<' && c != '\u2264') {
                throw new IllegalArgumentException("Expected choice separator (#<\u2264) instead of '" + c + "' in choice pattern " + this.prefix(start));
            }
            this.addPart(Part.Type.ARG_SELECTOR, index, 1, 0);
            index = this.parseMessage(++index, 0, nestingLevel + 1, ArgType.CHOICE);
            if (index == this.msg.length()) {
                return index;
            }
            if (this.msg.charAt(index) == '}') {
                if (!this.inMessageFormatPattern(nestingLevel)) {
                    throw new IllegalArgumentException("Bad choice pattern syntax: " + this.prefix(start));
                }
                return index;
            }
            else {
                index = this.skipWhiteSpace(index + 1);
            }
        }
    }
    
    private int parsePluralOrSelectStyle(final ArgType argType, int index, final int nestingLevel) {
        final int start = index;
        boolean isEmpty = true;
        boolean hasOther = false;
        while (true) {
            index = this.skipWhiteSpace(index);
            final boolean eos = index == this.msg.length();
            if (eos || this.msg.charAt(index) == '}') {
                if (eos == this.inMessageFormatPattern(nestingLevel)) {
                    throw new IllegalArgumentException("Bad " + argType.toString().toLowerCase(Locale.ENGLISH) + " pattern syntax: " + this.prefix(start));
                }
                if (!hasOther) {
                    throw new IllegalArgumentException("Missing 'other' keyword in " + argType.toString().toLowerCase(Locale.ENGLISH) + " pattern in " + this.prefix());
                }
                return index;
            }
            else {
                final int selectorIndex = index;
                if (argType.hasPluralStyle() && this.msg.charAt(selectorIndex) == '=') {
                    index = this.skipDouble(index + 1);
                    final int length = index - selectorIndex;
                    if (length == 1) {
                        throw new IllegalArgumentException("Bad " + argType.toString().toLowerCase(Locale.ENGLISH) + " pattern syntax: " + this.prefix(start));
                    }
                    if (length > 65535) {
                        throw new IndexOutOfBoundsException("Argument selector too long: " + this.prefix(selectorIndex));
                    }
                    this.addPart(Part.Type.ARG_SELECTOR, selectorIndex, length, 0);
                    this.parseDouble(selectorIndex + 1, index, false);
                }
                else {
                    index = this.skipIdentifier(index);
                    final int length = index - selectorIndex;
                    if (length == 0) {
                        throw new IllegalArgumentException("Bad " + argType.toString().toLowerCase(Locale.ENGLISH) + " pattern syntax: " + this.prefix(start));
                    }
                    if (argType.hasPluralStyle() && length == 6 && index < this.msg.length() && this.msg.regionMatches(selectorIndex, "offset:", 0, 7)) {
                        if (!isEmpty) {
                            throw new IllegalArgumentException("Plural argument 'offset:' (if present) must precede key-message pairs: " + this.prefix(start));
                        }
                        final int valueIndex = this.skipWhiteSpace(index + 1);
                        index = this.skipDouble(valueIndex);
                        if (index == valueIndex) {
                            throw new IllegalArgumentException("Missing value for plural 'offset:' " + this.prefix(start));
                        }
                        if (index - valueIndex > 65535) {
                            throw new IndexOutOfBoundsException("Plural offset value too long: " + this.prefix(valueIndex));
                        }
                        this.parseDouble(valueIndex, index, false);
                        isEmpty = false;
                        continue;
                    }
                    else {
                        if (length > 65535) {
                            throw new IndexOutOfBoundsException("Argument selector too long: " + this.prefix(selectorIndex));
                        }
                        this.addPart(Part.Type.ARG_SELECTOR, selectorIndex, length, 0);
                        if (this.msg.regionMatches(selectorIndex, "other", 0, length)) {
                            hasOther = true;
                        }
                    }
                }
                index = this.skipWhiteSpace(index);
                if (index == this.msg.length() || this.msg.charAt(index) != '{') {
                    throw new IllegalArgumentException("No message fragment after " + argType.toString().toLowerCase(Locale.ENGLISH) + " selector: " + this.prefix(selectorIndex));
                }
                index = this.parseMessage(index, 1, nestingLevel + 1, argType);
                isEmpty = false;
            }
        }
    }
    
    private static int parseArgNumber(final CharSequence s, int start, final int limit) {
        if (start >= limit) {
            return -2;
        }
        char c = s.charAt(start++);
        int number;
        boolean badNumber;
        if (c == '0') {
            if (start == limit) {
                return 0;
            }
            number = 0;
            badNumber = true;
        }
        else {
            if ('1' > c || c > '9') {
                return -1;
            }
            number = c - '0';
            badNumber = false;
        }
        while (start < limit) {
            c = s.charAt(start++);
            if ('0' > c || c > '9') {
                return -1;
            }
            if (number >= 214748364) {
                badNumber = true;
            }
            number = number * 10 + (c - '0');
        }
        if (badNumber) {
            return -2;
        }
        return number;
    }
    
    private int parseArgNumber(final int start, final int limit) {
        return parseArgNumber(this.msg, start, limit);
    }
    
    private void parseDouble(final int start, final int limit, final boolean allowInfinity) {
        assert start < limit;
        int value = 0;
        int isNegative = 0;
        int index = start;
        char c = this.msg.charAt(index++);
        if (c == '-') {
            isNegative = 1;
            if (index == limit) {
                throw new NumberFormatException("Bad syntax for numeric value: " + this.msg.substring(start, limit));
            }
            c = this.msg.charAt(index++);
        }
        else if (c == '+') {
            if (index == limit) {
                throw new NumberFormatException("Bad syntax for numeric value: " + this.msg.substring(start, limit));
            }
            c = this.msg.charAt(index++);
        }
        if (c != '\u221e') {
            while ('0' <= c && c <= '9') {
                value = value * 10 + (c - '0');
                if (value > 32767 + isNegative) {
                    break;
                }
                if (index == limit) {
                    this.addPart(Part.Type.ARG_INT, start, limit - start, (isNegative != 0) ? (-value) : value);
                    return;
                }
                c = this.msg.charAt(index++);
            }
            final double numericValue = Double.parseDouble(this.msg.substring(start, limit));
            this.addArgDoublePart(numericValue, start, limit - start);
            return;
        }
        if (allowInfinity && index == limit) {
            this.addArgDoublePart((isNegative != 0) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY, start, limit - start);
            return;
        }
        throw new NumberFormatException("Bad syntax for numeric value: " + this.msg.substring(start, limit));
    }
    
    static void appendReducedApostrophes(final String s, int start, final int limit, final StringBuilder sb) {
        int doubleApos = -1;
        while (true) {
            final int i = s.indexOf(39, start);
            if (i < 0 || i >= limit) {
                break;
            }
            if (i == doubleApos) {
                sb.append('\'');
                ++start;
                doubleApos = -1;
            }
            else {
                sb.append(s, start, i);
                start = (doubleApos = i + 1);
            }
        }
        sb.append(s, start, limit);
    }
    
    private int skipWhiteSpace(final int index) {
        return PatternProps.skipWhiteSpace(this.msg, index);
    }
    
    private int skipIdentifier(final int index) {
        return PatternProps.skipIdentifier(this.msg, index);
    }
    
    private int skipDouble(int index) {
        while (index < this.msg.length()) {
            final char c = this.msg.charAt(index);
            if (c < '0' && "+-.".indexOf(c) < 0) {
                break;
            }
            if (c > '9' && c != 'e' && c != 'E' && c != '\u221e') {
                break;
            }
            ++index;
        }
        return index;
    }
    
    private static boolean isArgTypeChar(final int c) {
        return (97 <= c && c <= 122) || (65 <= c && c <= 90);
    }
    
    private boolean isChoice(int index) {
        char c;
        return ((c = this.msg.charAt(index++)) == 'c' || c == 'C') && ((c = this.msg.charAt(index++)) == 'h' || c == 'H') && ((c = this.msg.charAt(index++)) == 'o' || c == 'O') && ((c = this.msg.charAt(index++)) == 'i' || c == 'I') && ((c = this.msg.charAt(index++)) == 'c' || c == 'C') && ((c = this.msg.charAt(index)) == 'e' || c == 'E');
    }
    
    private boolean isPlural(int index) {
        char c;
        return ((c = this.msg.charAt(index++)) == 'p' || c == 'P') && ((c = this.msg.charAt(index++)) == 'l' || c == 'L') && ((c = this.msg.charAt(index++)) == 'u' || c == 'U') && ((c = this.msg.charAt(index++)) == 'r' || c == 'R') && ((c = this.msg.charAt(index++)) == 'a' || c == 'A') && ((c = this.msg.charAt(index)) == 'l' || c == 'L');
    }
    
    private boolean isSelect(int index) {
        char c;
        return ((c = this.msg.charAt(index++)) == 's' || c == 'S') && ((c = this.msg.charAt(index++)) == 'e' || c == 'E') && ((c = this.msg.charAt(index++)) == 'l' || c == 'L') && ((c = this.msg.charAt(index++)) == 'e' || c == 'E') && ((c = this.msg.charAt(index++)) == 'c' || c == 'C') && ((c = this.msg.charAt(index)) == 't' || c == 'T');
    }
    
    private boolean isOrdinal(int index) {
        char c;
        return ((c = this.msg.charAt(index++)) == 'o' || c == 'O') && ((c = this.msg.charAt(index++)) == 'r' || c == 'R') && ((c = this.msg.charAt(index++)) == 'd' || c == 'D') && ((c = this.msg.charAt(index++)) == 'i' || c == 'I') && ((c = this.msg.charAt(index++)) == 'n' || c == 'N') && ((c = this.msg.charAt(index++)) == 'a' || c == 'A') && ((c = this.msg.charAt(index)) == 'l' || c == 'L');
    }
    
    private boolean inMessageFormatPattern(final int nestingLevel) {
        return nestingLevel > 0 || this.parts.get(0).type == Part.Type.MSG_START;
    }
    
    private boolean inTopLevelChoiceMessage(final int nestingLevel, final ArgType parentType) {
        return nestingLevel == 1 && parentType == ArgType.CHOICE && this.parts.get(0).type != Part.Type.MSG_START;
    }
    
    private void addPart(final Part.Type type, final int index, final int length, final int value) {
        this.parts.add(new Part(type, index, length, value));
    }
    
    private void addLimitPart(final int start, final Part.Type type, final int index, final int length, final int value) {
        this.parts.get(start).limitPartIndex = this.parts.size();
        this.addPart(type, index, length, value);
    }
    
    private void addArgDoublePart(final double numericValue, final int start, final int length) {
        int numericIndex;
        if (this.numericValues == null) {
            this.numericValues = new ArrayList<Double>();
            numericIndex = 0;
        }
        else {
            numericIndex = this.numericValues.size();
            if (numericIndex > 32767) {
                throw new IndexOutOfBoundsException("Too many numeric values");
            }
        }
        this.numericValues.add(numericValue);
        this.addPart(Part.Type.ARG_DOUBLE, start, length, numericIndex);
    }
    
    private static String prefix(final String s, final int start) {
        final StringBuilder prefix = new StringBuilder(44);
        if (start == 0) {
            prefix.append("\"");
        }
        else {
            prefix.append("[at pattern index ").append(start).append("] \"");
        }
        final int substringLength = s.length() - start;
        if (substringLength <= 24) {
            prefix.append((start == 0) ? s : s.substring(start));
        }
        else {
            int limit = start + 24 - 4;
            if (Character.isHighSurrogate(s.charAt(limit - 1))) {
                --limit;
            }
            prefix.append(s, start, limit).append(" ...");
        }
        return prefix.append("\"").toString();
    }
    
    private static String prefix(final String s) {
        return prefix(s, 0);
    }
    
    private String prefix(final int start) {
        return prefix(this.msg, start);
    }
    
    private String prefix() {
        return prefix(this.msg, 0);
    }
    
    static {
        defaultAposMode = ApostropheMode.valueOf(ICUConfig.get("com.ibm.icu.text.MessagePattern.ApostropheMode", "DOUBLE_OPTIONAL"));
        argTypes = ArgType.values();
    }
    
    public enum ApostropheMode
    {
        DOUBLE_OPTIONAL, 
        DOUBLE_REQUIRED;
    }
    
    public static final class Part
    {
        private static final int MAX_LENGTH = 65535;
        private static final int MAX_VALUE = 32767;
        private final Type type;
        private final int index;
        private final char length;
        private short value;
        private int limitPartIndex;
        
        private Part(final Type t, final int i, final int l, final int v) {
            this.type = t;
            this.index = i;
            this.length = (char)l;
            this.value = (short)v;
        }
        
        public Type getType() {
            return this.type;
        }
        
        public int getIndex() {
            return this.index;
        }
        
        public int getLength() {
            return this.length;
        }
        
        public int getLimit() {
            return this.index + this.length;
        }
        
        public int getValue() {
            return this.value;
        }
        
        public ArgType getArgType() {
            final Type type = this.getType();
            if (type == Type.ARG_START || type == Type.ARG_LIMIT) {
                return MessagePattern.argTypes[this.value];
            }
            return ArgType.NONE;
        }
        
        @Override
        public String toString() {
            final String valueString = (this.type == Type.ARG_START || this.type == Type.ARG_LIMIT) ? this.getArgType().name() : Integer.toString(this.value);
            return this.type.name() + "(" + valueString + ")@" + this.index;
        }
        
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || this.getClass() != other.getClass()) {
                return false;
            }
            final Part o = (Part)other;
            return this.type.equals(o.type) && this.index == o.index && this.length == o.length && this.value == o.value && this.limitPartIndex == o.limitPartIndex;
        }
        
        @Override
        public int hashCode() {
            return ((this.type.hashCode() * 37 + this.index) * 37 + this.length) * 37 + this.value;
        }
        
        public enum Type
        {
            MSG_START, 
            MSG_LIMIT, 
            SKIP_SYNTAX, 
            INSERT_CHAR, 
            REPLACE_NUMBER, 
            ARG_START, 
            ARG_LIMIT, 
            ARG_NUMBER, 
            ARG_NAME, 
            ARG_TYPE, 
            ARG_STYLE, 
            ARG_SELECTOR, 
            ARG_INT, 
            ARG_DOUBLE;
            
            public boolean hasNumericValue() {
                return this == Type.ARG_INT || this == Type.ARG_DOUBLE;
            }
        }
    }
    
    public enum ArgType
    {
        NONE, 
        SIMPLE, 
        CHOICE, 
        PLURAL, 
        SELECT, 
        SELECTORDINAL;
        
        public boolean hasPluralStyle() {
            return this == ArgType.PLURAL || this == ArgType.SELECTORDINAL;
        }
    }
}
