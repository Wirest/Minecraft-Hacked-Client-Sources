// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.text.CharacterIterator;
import java.util.List;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.ibm.icu.impl.PatternProps;
import java.util.Date;
import java.text.ChoiceFormat;
import com.ibm.icu.impl.Utility;
import java.text.ParseException;
import java.util.HashMap;
import java.text.ParsePosition;
import java.util.Iterator;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.text.Format;
import java.util.Map;
import com.ibm.icu.util.ULocale;

public class MessageFormat extends UFormat
{
    static final long serialVersionUID = 7136212545847378652L;
    private transient ULocale ulocale;
    private transient MessagePattern msgPattern;
    private transient Map<Integer, Format> cachedFormatters;
    private transient Set<Integer> customFormatArgStarts;
    private transient Format stockDateFormatter;
    private transient Format stockNumberFormatter;
    private transient PluralSelectorProvider pluralProvider;
    private transient PluralSelectorProvider ordinalProvider;
    private static final String[] typeList;
    private static final int TYPE_NUMBER = 0;
    private static final int TYPE_DATE = 1;
    private static final int TYPE_TIME = 2;
    private static final int TYPE_SPELLOUT = 3;
    private static final int TYPE_ORDINAL = 4;
    private static final int TYPE_DURATION = 5;
    private static final String[] modifierList;
    private static final int MODIFIER_EMPTY = 0;
    private static final int MODIFIER_CURRENCY = 1;
    private static final int MODIFIER_PERCENT = 2;
    private static final int MODIFIER_INTEGER = 3;
    private static final String[] dateModifierList;
    private static final int DATE_MODIFIER_EMPTY = 0;
    private static final int DATE_MODIFIER_SHORT = 1;
    private static final int DATE_MODIFIER_MEDIUM = 2;
    private static final int DATE_MODIFIER_LONG = 3;
    private static final int DATE_MODIFIER_FULL = 4;
    private static final Locale rootLocale;
    private static final char SINGLE_QUOTE = '\'';
    private static final char CURLY_BRACE_LEFT = '{';
    private static final char CURLY_BRACE_RIGHT = '}';
    private static final int STATE_INITIAL = 0;
    private static final int STATE_SINGLE_QUOTE = 1;
    private static final int STATE_IN_QUOTE = 2;
    private static final int STATE_MSG_ELEMENT = 3;
    
    public MessageFormat(final String pattern) {
        this.ulocale = ULocale.getDefault(ULocale.Category.FORMAT);
        this.applyPattern(pattern);
    }
    
    public MessageFormat(final String pattern, final Locale locale) {
        this(pattern, ULocale.forLocale(locale));
    }
    
    public MessageFormat(final String pattern, final ULocale locale) {
        this.ulocale = locale;
        this.applyPattern(pattern);
    }
    
    public void setLocale(final Locale locale) {
        this.setLocale(ULocale.forLocale(locale));
    }
    
    public void setLocale(final ULocale locale) {
        final String existingPattern = this.toPattern();
        this.ulocale = locale;
        final Format format = null;
        this.stockDateFormatter = format;
        this.stockNumberFormatter = format;
        this.pluralProvider = null;
        this.ordinalProvider = null;
        this.applyPattern(existingPattern);
    }
    
    public Locale getLocale() {
        return this.ulocale.toLocale();
    }
    
    public ULocale getULocale() {
        return this.ulocale;
    }
    
    public void applyPattern(final String pttrn) {
        try {
            if (this.msgPattern == null) {
                this.msgPattern = new MessagePattern(pttrn);
            }
            else {
                this.msgPattern.parse(pttrn);
            }
            this.cacheExplicitFormats();
        }
        catch (RuntimeException e) {
            this.resetPattern();
            throw e;
        }
    }
    
    public void applyPattern(final String pattern, final MessagePattern.ApostropheMode aposMode) {
        if (this.msgPattern == null) {
            this.msgPattern = new MessagePattern(aposMode);
        }
        else if (aposMode != this.msgPattern.getApostropheMode()) {
            this.msgPattern.clearPatternAndSetApostropheMode(aposMode);
        }
        this.applyPattern(pattern);
    }
    
    public MessagePattern.ApostropheMode getApostropheMode() {
        if (this.msgPattern == null) {
            this.msgPattern = new MessagePattern();
        }
        return this.msgPattern.getApostropheMode();
    }
    
    public String toPattern() {
        if (this.customFormatArgStarts != null) {
            throw new IllegalStateException("toPattern() is not supported after custom Format objects have been set via setFormat() or similar APIs");
        }
        if (this.msgPattern == null) {
            return "";
        }
        final String originalPattern = this.msgPattern.getPatternString();
        return (originalPattern == null) ? "" : originalPattern;
    }
    
    private int nextTopLevelArgStart(int partIndex) {
        if (partIndex != 0) {
            partIndex = this.msgPattern.getLimitPartIndex(partIndex);
        }
        while (true) {
            final MessagePattern.Part.Type type = this.msgPattern.getPartType(++partIndex);
            if (type == MessagePattern.Part.Type.ARG_START) {
                return partIndex;
            }
            if (type == MessagePattern.Part.Type.MSG_LIMIT) {
                return -1;
            }
        }
    }
    
    private boolean argNameMatches(final int partIndex, final String argName, final int argNumber) {
        final MessagePattern.Part part = this.msgPattern.getPart(partIndex);
        return (part.getType() == MessagePattern.Part.Type.ARG_NAME) ? this.msgPattern.partSubstringMatches(part, argName) : (part.getValue() == argNumber);
    }
    
    private String getArgName(final int partIndex) {
        final MessagePattern.Part part = this.msgPattern.getPart(partIndex);
        if (part.getType() == MessagePattern.Part.Type.ARG_NAME) {
            return this.msgPattern.getSubstring(part);
        }
        return Integer.toString(part.getValue());
    }
    
    public void setFormatsByArgumentIndex(final Format[] newFormats) {
        if (this.msgPattern.hasNamedArguments()) {
            throw new IllegalArgumentException("This method is not available in MessageFormat objects that use alphanumeric argument names.");
        }
        int partIndex = 0;
        while ((partIndex = this.nextTopLevelArgStart(partIndex)) >= 0) {
            final int argNumber = this.msgPattern.getPart(partIndex + 1).getValue();
            if (argNumber < newFormats.length) {
                this.setCustomArgStartFormat(partIndex, newFormats[argNumber]);
            }
        }
    }
    
    public void setFormatsByArgumentName(final Map<String, Format> newFormats) {
        int partIndex = 0;
        while ((partIndex = this.nextTopLevelArgStart(partIndex)) >= 0) {
            final String key = this.getArgName(partIndex + 1);
            if (newFormats.containsKey(key)) {
                this.setCustomArgStartFormat(partIndex, newFormats.get(key));
            }
        }
    }
    
    public void setFormats(final Format[] newFormats) {
        for (int formatNumber = 0, partIndex = 0; formatNumber < newFormats.length && (partIndex = this.nextTopLevelArgStart(partIndex)) >= 0; ++formatNumber) {
            this.setCustomArgStartFormat(partIndex, newFormats[formatNumber]);
        }
    }
    
    public void setFormatByArgumentIndex(final int argumentIndex, final Format newFormat) {
        if (this.msgPattern.hasNamedArguments()) {
            throw new IllegalArgumentException("This method is not available in MessageFormat objects that use alphanumeric argument names.");
        }
        int partIndex = 0;
        while ((partIndex = this.nextTopLevelArgStart(partIndex)) >= 0) {
            if (this.msgPattern.getPart(partIndex + 1).getValue() == argumentIndex) {
                this.setCustomArgStartFormat(partIndex, newFormat);
            }
        }
    }
    
    public void setFormatByArgumentName(final String argumentName, final Format newFormat) {
        final int argNumber = MessagePattern.validateArgumentName(argumentName);
        if (argNumber < -1) {
            return;
        }
        int partIndex = 0;
        while ((partIndex = this.nextTopLevelArgStart(partIndex)) >= 0) {
            if (this.argNameMatches(partIndex + 1, argumentName, argNumber)) {
                this.setCustomArgStartFormat(partIndex, newFormat);
            }
        }
    }
    
    public void setFormat(final int formatElementIndex, final Format newFormat) {
        int formatNumber = 0;
        int partIndex = 0;
        while ((partIndex = this.nextTopLevelArgStart(partIndex)) >= 0) {
            if (formatNumber == formatElementIndex) {
                this.setCustomArgStartFormat(partIndex, newFormat);
                return;
            }
            ++formatNumber;
        }
        throw new ArrayIndexOutOfBoundsException(formatElementIndex);
    }
    
    public Format[] getFormatsByArgumentIndex() {
        if (this.msgPattern.hasNamedArguments()) {
            throw new IllegalArgumentException("This method is not available in MessageFormat objects that use alphanumeric argument names.");
        }
        final ArrayList<Format> list = new ArrayList<Format>();
        int partIndex = 0;
        while ((partIndex = this.nextTopLevelArgStart(partIndex)) >= 0) {
            final int argNumber = this.msgPattern.getPart(partIndex + 1).getValue();
            while (argNumber >= list.size()) {
                list.add(null);
            }
            list.set(argNumber, (this.cachedFormatters == null) ? null : this.cachedFormatters.get(partIndex));
        }
        return list.toArray(new Format[list.size()]);
    }
    
    public Format[] getFormats() {
        final ArrayList<Format> list = new ArrayList<Format>();
        int partIndex = 0;
        while ((partIndex = this.nextTopLevelArgStart(partIndex)) >= 0) {
            list.add((this.cachedFormatters == null) ? null : this.cachedFormatters.get(partIndex));
        }
        return list.toArray(new Format[list.size()]);
    }
    
    public Set<String> getArgumentNames() {
        final Set<String> result = new HashSet<String>();
        int partIndex = 0;
        while ((partIndex = this.nextTopLevelArgStart(partIndex)) >= 0) {
            result.add(this.getArgName(partIndex + 1));
        }
        return result;
    }
    
    public Format getFormatByArgumentName(final String argumentName) {
        if (this.cachedFormatters == null) {
            return null;
        }
        final int argNumber = MessagePattern.validateArgumentName(argumentName);
        if (argNumber < -1) {
            return null;
        }
        int partIndex = 0;
        while ((partIndex = this.nextTopLevelArgStart(partIndex)) >= 0) {
            if (this.argNameMatches(partIndex + 1, argumentName, argNumber)) {
                return this.cachedFormatters.get(partIndex);
            }
        }
        return null;
    }
    
    public final StringBuffer format(final Object[] arguments, final StringBuffer result, final FieldPosition pos) {
        this.format(arguments, null, new AppendableWrapper(result), pos);
        return result;
    }
    
    public final StringBuffer format(final Map<String, Object> arguments, final StringBuffer result, final FieldPosition pos) {
        this.format(null, arguments, new AppendableWrapper(result), pos);
        return result;
    }
    
    public static String format(final String pattern, final Object... arguments) {
        final MessageFormat temp = new MessageFormat(pattern);
        return temp.format(arguments);
    }
    
    public static String format(final String pattern, final Map<String, Object> arguments) {
        final MessageFormat temp = new MessageFormat(pattern);
        return temp.format(arguments);
    }
    
    public boolean usesNamedArguments() {
        return this.msgPattern.hasNamedArguments();
    }
    
    @Override
    public final StringBuffer format(final Object arguments, final StringBuffer result, final FieldPosition pos) {
        this.format(arguments, new AppendableWrapper(result), pos);
        return result;
    }
    
    @Override
    public AttributedCharacterIterator formatToCharacterIterator(final Object arguments) {
        if (arguments == null) {
            throw new NullPointerException("formatToCharacterIterator must be passed non-null object");
        }
        final StringBuilder result = new StringBuilder();
        final AppendableWrapper wrapper = new AppendableWrapper(result);
        wrapper.useAttributes();
        this.format(arguments, wrapper, null);
        final AttributedString as = new AttributedString(result.toString());
        for (final AttributeAndPosition a : wrapper.attributes) {
            as.addAttribute(a.key, a.value, a.start, a.limit);
        }
        return as.getIterator();
    }
    
    public Object[] parse(final String source, final ParsePosition pos) {
        if (this.msgPattern.hasNamedArguments()) {
            throw new IllegalArgumentException("This method is not available in MessageFormat objects that use named argument.");
        }
        int maxArgId = -1;
        int partIndex = 0;
        while ((partIndex = this.nextTopLevelArgStart(partIndex)) >= 0) {
            final int argNumber = this.msgPattern.getPart(partIndex + 1).getValue();
            if (argNumber > maxArgId) {
                maxArgId = argNumber;
            }
        }
        final Object[] resultArray = new Object[maxArgId + 1];
        final int backupStartPos = pos.getIndex();
        this.parse(0, source, pos, resultArray, null);
        if (pos.getIndex() == backupStartPos) {
            return null;
        }
        return resultArray;
    }
    
    public Map<String, Object> parseToMap(final String source, final ParsePosition pos) {
        final Map<String, Object> result = new HashMap<String, Object>();
        final int backupStartPos = pos.getIndex();
        this.parse(0, source, pos, null, result);
        if (pos.getIndex() == backupStartPos) {
            return null;
        }
        return result;
    }
    
    public Object[] parse(final String source) throws ParseException {
        final ParsePosition pos = new ParsePosition(0);
        final Object[] result = this.parse(source, pos);
        if (pos.getIndex() == 0) {
            throw new ParseException("MessageFormat parse error!", pos.getErrorIndex());
        }
        return result;
    }
    
    private void parse(final int msgStart, final String source, final ParsePosition pos, final Object[] args, final Map<String, Object> argsMap) {
        if (source == null) {
            return;
        }
        final String msgString = this.msgPattern.getPatternString();
        int prevIndex = this.msgPattern.getPart(msgStart).getLimit();
        int sourceOffset = pos.getIndex();
        final ParsePosition tempStatus = new ParsePosition(0);
        int i = msgStart + 1;
        while (true) {
            MessagePattern.Part part = this.msgPattern.getPart(i);
            final MessagePattern.Part.Type type = part.getType();
            final int index = part.getIndex();
            final int len = index - prevIndex;
            if (len != 0 && !msgString.regionMatches(prevIndex, source, sourceOffset, len)) {
                pos.setErrorIndex(sourceOffset);
                return;
            }
            sourceOffset += len;
            prevIndex += len;
            if (type == MessagePattern.Part.Type.MSG_LIMIT) {
                pos.setIndex(sourceOffset);
                return;
            }
            if (type == MessagePattern.Part.Type.SKIP_SYNTAX || type == MessagePattern.Part.Type.INSERT_CHAR) {
                prevIndex = part.getLimit();
            }
            else {
                assert type == MessagePattern.Part.Type.ARG_START : "Unexpected Part " + part + " in parsed message.";
                final int argLimit = this.msgPattern.getLimitPartIndex(i);
                final MessagePattern.ArgType argType = part.getArgType();
                part = this.msgPattern.getPart(++i);
                Object argId = null;
                int argNumber = 0;
                String key = null;
                if (args != null) {
                    argNumber = part.getValue();
                    argId = argNumber;
                }
                else {
                    if (part.getType() == MessagePattern.Part.Type.ARG_NAME) {
                        key = this.msgPattern.getSubstring(part);
                    }
                    else {
                        key = Integer.toString(part.getValue());
                    }
                    argId = key;
                }
                ++i;
                Format formatter = null;
                boolean haveArgResult = false;
                Object argResult = null;
                if (this.cachedFormatters != null && (formatter = this.cachedFormatters.get(i - 2)) != null) {
                    tempStatus.setIndex(sourceOffset);
                    argResult = formatter.parseObject(source, tempStatus);
                    if (tempStatus.getIndex() == sourceOffset) {
                        pos.setErrorIndex(sourceOffset);
                        return;
                    }
                    haveArgResult = true;
                    sourceOffset = tempStatus.getIndex();
                }
                else if (argType == MessagePattern.ArgType.NONE || (this.cachedFormatters != null && this.cachedFormatters.containsKey(i - 2))) {
                    final String stringAfterArgument = this.getLiteralStringUntilNextArgument(argLimit);
                    int next;
                    if (stringAfterArgument.length() != 0) {
                        next = source.indexOf(stringAfterArgument, sourceOffset);
                    }
                    else {
                        next = source.length();
                    }
                    if (next < 0) {
                        pos.setErrorIndex(sourceOffset);
                        return;
                    }
                    final String strValue = source.substring(sourceOffset, next);
                    if (!strValue.equals("{" + argId.toString() + "}")) {
                        haveArgResult = true;
                        argResult = strValue;
                    }
                    sourceOffset = next;
                }
                else if (argType == MessagePattern.ArgType.CHOICE) {
                    tempStatus.setIndex(sourceOffset);
                    final double choiceResult = parseChoiceArgument(this.msgPattern, i, source, tempStatus);
                    if (tempStatus.getIndex() == sourceOffset) {
                        pos.setErrorIndex(sourceOffset);
                        return;
                    }
                    argResult = choiceResult;
                    haveArgResult = true;
                    sourceOffset = tempStatus.getIndex();
                }
                else {
                    if (argType.hasPluralStyle() || argType == MessagePattern.ArgType.SELECT) {
                        throw new UnsupportedOperationException("Parsing of plural/select/selectordinal argument is not supported.");
                    }
                    throw new IllegalStateException("unexpected argType " + argType);
                }
                if (haveArgResult) {
                    if (args != null) {
                        args[argNumber] = argResult;
                    }
                    else if (argsMap != null) {
                        argsMap.put(key, argResult);
                    }
                }
                prevIndex = this.msgPattern.getPart(argLimit).getLimit();
                i = argLimit;
            }
            ++i;
        }
    }
    
    public Map<String, Object> parseToMap(final String source) throws ParseException {
        final ParsePosition pos = new ParsePosition(0);
        final Map<String, Object> result = new HashMap<String, Object>();
        this.parse(0, source, pos, null, result);
        if (pos.getIndex() == 0) {
            throw new ParseException("MessageFormat parse error!", pos.getErrorIndex());
        }
        return result;
    }
    
    @Override
    public Object parseObject(final String source, final ParsePosition pos) {
        if (!this.msgPattern.hasNamedArguments()) {
            return this.parse(source, pos);
        }
        return this.parseToMap(source, pos);
    }
    
    @Override
    public Object clone() {
        final MessageFormat other = (MessageFormat)super.clone();
        if (this.customFormatArgStarts != null) {
            other.customFormatArgStarts = new HashSet<Integer>();
            for (final Integer key : this.customFormatArgStarts) {
                other.customFormatArgStarts.add(key);
            }
        }
        else {
            other.customFormatArgStarts = null;
        }
        if (this.cachedFormatters != null) {
            other.cachedFormatters = new HashMap<Integer, Format>();
            for (final Map.Entry<Integer, Format> entry : this.cachedFormatters.entrySet()) {
                other.cachedFormatters.put(entry.getKey(), entry.getValue());
            }
        }
        else {
            other.cachedFormatters = null;
        }
        other.msgPattern = ((this.msgPattern == null) ? null : ((MessagePattern)this.msgPattern.clone()));
        other.stockDateFormatter = ((this.stockDateFormatter == null) ? null : ((Format)this.stockDateFormatter.clone()));
        other.stockNumberFormatter = ((this.stockNumberFormatter == null) ? null : ((Format)this.stockNumberFormatter.clone()));
        other.pluralProvider = null;
        other.ordinalProvider = null;
        return other;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final MessageFormat other = (MessageFormat)obj;
        return Utility.objectEquals(this.ulocale, other.ulocale) && Utility.objectEquals(this.msgPattern, other.msgPattern) && Utility.objectEquals(this.cachedFormatters, other.cachedFormatters) && Utility.objectEquals(this.customFormatArgStarts, other.customFormatArgStarts);
    }
    
    @Override
    public int hashCode() {
        return this.msgPattern.getPatternString().hashCode();
    }
    
    private void format(final int msgStart, final double pluralNumber, final Object[] args, final Map<String, Object> argsMap, final AppendableWrapper dest, FieldPosition fp) {
        final String msgString = this.msgPattern.getPatternString();
        int prevIndex = this.msgPattern.getPart(msgStart).getLimit();
        int i = msgStart + 1;
        while (true) {
            MessagePattern.Part part = this.msgPattern.getPart(i);
            final MessagePattern.Part.Type type = part.getType();
            final int index = part.getIndex();
            dest.append(msgString, prevIndex, index);
            if (type == MessagePattern.Part.Type.MSG_LIMIT) {
                return;
            }
            prevIndex = part.getLimit();
            if (type == MessagePattern.Part.Type.REPLACE_NUMBER) {
                if (this.stockNumberFormatter == null) {
                    this.stockNumberFormatter = NumberFormat.getInstance(this.ulocale);
                }
                dest.formatAndAppend(this.stockNumberFormatter, pluralNumber);
            }
            else if (type == MessagePattern.Part.Type.ARG_START) {
                final int argLimit = this.msgPattern.getLimitPartIndex(i);
                final MessagePattern.ArgType argType = part.getArgType();
                part = this.msgPattern.getPart(++i);
                String noArg = null;
                Object argId = null;
                Object arg;
                if (args != null) {
                    final int argNumber = part.getValue();
                    if (dest.attributes != null) {
                        argId = argNumber;
                    }
                    if (0 <= argNumber && argNumber < args.length) {
                        arg = args[argNumber];
                    }
                    else {
                        arg = null;
                        noArg = "{" + argNumber + "}";
                    }
                }
                else {
                    String key;
                    if (part.getType() == MessagePattern.Part.Type.ARG_NAME) {
                        key = this.msgPattern.getSubstring(part);
                    }
                    else {
                        key = Integer.toString(part.getValue());
                    }
                    argId = key;
                    if (argsMap != null && argsMap.containsKey(key)) {
                        arg = argsMap.get(key);
                    }
                    else {
                        arg = null;
                        noArg = "{" + key + "}";
                    }
                }
                ++i;
                final int prevDestLength = dest.length;
                Format formatter = null;
                if (noArg != null) {
                    dest.append(noArg);
                }
                else if (arg == null) {
                    dest.append("null");
                }
                else if (this.cachedFormatters != null && (formatter = this.cachedFormatters.get(i - 2)) != null) {
                    if (formatter instanceof ChoiceFormat || formatter instanceof PluralFormat || formatter instanceof SelectFormat) {
                        final String subMsgString = formatter.format(arg);
                        if (subMsgString.indexOf(123) >= 0 || (subMsgString.indexOf(39) >= 0 && !this.msgPattern.jdkAposMode())) {
                            final MessageFormat subMsgFormat = new MessageFormat(subMsgString, this.ulocale);
                            subMsgFormat.format(0, 0.0, args, argsMap, dest, null);
                        }
                        else if (dest.attributes == null) {
                            dest.append(subMsgString);
                        }
                        else {
                            dest.formatAndAppend(formatter, arg);
                        }
                    }
                    else {
                        dest.formatAndAppend(formatter, arg);
                    }
                }
                else if (argType == MessagePattern.ArgType.NONE || (this.cachedFormatters != null && this.cachedFormatters.containsKey(i - 2))) {
                    if (arg instanceof Number) {
                        if (this.stockNumberFormatter == null) {
                            this.stockNumberFormatter = NumberFormat.getInstance(this.ulocale);
                        }
                        dest.formatAndAppend(this.stockNumberFormatter, arg);
                    }
                    else if (arg instanceof Date) {
                        if (this.stockDateFormatter == null) {
                            this.stockDateFormatter = DateFormat.getDateTimeInstance(3, 3, this.ulocale);
                        }
                        dest.formatAndAppend(this.stockDateFormatter, arg);
                    }
                    else {
                        dest.append(arg.toString());
                    }
                }
                else if (argType == MessagePattern.ArgType.CHOICE) {
                    if (!(arg instanceof Number)) {
                        throw new IllegalArgumentException("'" + arg + "' is not a Number");
                    }
                    final double number = ((Number)arg).doubleValue();
                    final int subMsgStart = findChoiceSubMessage(this.msgPattern, i, number);
                    this.formatComplexSubMessage(subMsgStart, 0.0, args, argsMap, dest);
                }
                else if (argType.hasPluralStyle()) {
                    if (!(arg instanceof Number)) {
                        throw new IllegalArgumentException("'" + arg + "' is not a Number");
                    }
                    final double number = ((Number)arg).doubleValue();
                    PluralFormat.PluralSelector selector;
                    if (argType == MessagePattern.ArgType.PLURAL) {
                        if (this.pluralProvider == null) {
                            this.pluralProvider = new PluralSelectorProvider(this.ulocale, PluralRules.PluralType.CARDINAL);
                        }
                        selector = this.pluralProvider;
                    }
                    else {
                        if (this.ordinalProvider == null) {
                            this.ordinalProvider = new PluralSelectorProvider(this.ulocale, PluralRules.PluralType.ORDINAL);
                        }
                        selector = this.ordinalProvider;
                    }
                    final int subMsgStart2 = PluralFormat.findSubMessage(this.msgPattern, i, selector, number);
                    final double offset = this.msgPattern.getPluralOffset(i);
                    this.formatComplexSubMessage(subMsgStart2, number - offset, args, argsMap, dest);
                }
                else {
                    if (argType != MessagePattern.ArgType.SELECT) {
                        throw new IllegalStateException("unexpected argType " + argType);
                    }
                    final int subMsgStart3 = SelectFormat.findSubMessage(this.msgPattern, i, arg.toString());
                    this.formatComplexSubMessage(subMsgStart3, 0.0, args, argsMap, dest);
                }
                fp = this.updateMetaData(dest, prevDestLength, fp, argId);
                prevIndex = this.msgPattern.getPart(argLimit).getLimit();
                i = argLimit;
            }
            ++i;
        }
    }
    
    private void formatComplexSubMessage(final int msgStart, final double pluralNumber, final Object[] args, final Map<String, Object> argsMap, final AppendableWrapper dest) {
        if (!this.msgPattern.jdkAposMode()) {
            this.format(msgStart, pluralNumber, args, argsMap, dest, null);
            return;
        }
        final String msgString = this.msgPattern.getPatternString();
        StringBuilder sb = null;
        int prevIndex = this.msgPattern.getPart(msgStart).getLimit();
        int i = msgStart;
        int index;
        while (true) {
            final MessagePattern.Part part = this.msgPattern.getPart(++i);
            final MessagePattern.Part.Type type = part.getType();
            index = part.getIndex();
            if (type == MessagePattern.Part.Type.MSG_LIMIT) {
                break;
            }
            if (type == MessagePattern.Part.Type.REPLACE_NUMBER || type == MessagePattern.Part.Type.SKIP_SYNTAX) {
                if (sb == null) {
                    sb = new StringBuilder();
                }
                sb.append(msgString, prevIndex, index);
                if (type == MessagePattern.Part.Type.REPLACE_NUMBER) {
                    if (this.stockNumberFormatter == null) {
                        this.stockNumberFormatter = NumberFormat.getInstance(this.ulocale);
                    }
                    sb.append(this.stockNumberFormatter.format(pluralNumber));
                }
                prevIndex = part.getLimit();
            }
            else {
                if (type != MessagePattern.Part.Type.ARG_START) {
                    continue;
                }
                if (sb == null) {
                    sb = new StringBuilder();
                }
                sb.append(msgString, prevIndex, index);
                prevIndex = index;
                i = this.msgPattern.getLimitPartIndex(i);
                index = this.msgPattern.getPart(i).getLimit();
                MessagePattern.appendReducedApostrophes(msgString, prevIndex, index, sb);
                prevIndex = index;
            }
        }
        String subMsgString;
        if (sb == null) {
            subMsgString = msgString.substring(prevIndex, index);
        }
        else {
            subMsgString = sb.append(msgString, prevIndex, index).toString();
        }
        if (subMsgString.indexOf(123) >= 0) {
            final MessageFormat subMsgFormat = new MessageFormat("", this.ulocale);
            subMsgFormat.applyPattern(subMsgString, MessagePattern.ApostropheMode.DOUBLE_REQUIRED);
            subMsgFormat.format(0, 0.0, args, argsMap, dest, null);
        }
        else {
            dest.append(subMsgString);
        }
    }
    
    private String getLiteralStringUntilNextArgument(final int from) {
        final StringBuilder b = new StringBuilder();
        final String msgString = this.msgPattern.getPatternString();
        int prevIndex = this.msgPattern.getPart(from).getLimit();
        int i = from + 1;
        while (true) {
            final MessagePattern.Part part = this.msgPattern.getPart(i);
            final MessagePattern.Part.Type type = part.getType();
            final int index = part.getIndex();
            b.append(msgString, prevIndex, index);
            if (type == MessagePattern.Part.Type.ARG_START || type == MessagePattern.Part.Type.MSG_LIMIT) {
                return b.toString();
            }
            assert type == MessagePattern.Part.Type.INSERT_CHAR : "Unexpected Part " + part + " in parsed message.";
            prevIndex = part.getLimit();
            ++i;
        }
    }
    
    private FieldPosition updateMetaData(final AppendableWrapper dest, final int prevLength, final FieldPosition fp, final Object argId) {
        if (dest.attributes != null && prevLength < dest.length) {
            dest.attributes.add(new AttributeAndPosition(argId, prevLength, dest.length));
        }
        if (fp != null && Field.ARGUMENT.equals(fp.getFieldAttribute())) {
            fp.setBeginIndex(prevLength);
            fp.setEndIndex(dest.length);
            return null;
        }
        return fp;
    }
    
    private static int findChoiceSubMessage(final MessagePattern pattern, int partIndex, final double number) {
        final int count = pattern.countParts();
        partIndex += 2;
        int msgStart;
        while (true) {
            msgStart = partIndex;
            partIndex = pattern.getLimitPartIndex(partIndex);
            if (++partIndex >= count) {
                break;
            }
            final MessagePattern.Part part = pattern.getPart(partIndex++);
            final MessagePattern.Part.Type type = part.getType();
            if (type == MessagePattern.Part.Type.ARG_LIMIT) {
                break;
            }
            assert type.hasNumericValue();
            final double boundary = pattern.getNumericValue(part);
            final int selectorIndex = pattern.getPatternIndex(partIndex++);
            final char boundaryChar = pattern.getPatternString().charAt(selectorIndex);
            if (boundaryChar == '<') {
                if (number <= boundary) {
                    break;
                }
                continue;
            }
            else {
                if (number < boundary) {
                    break;
                }
                continue;
            }
        }
        return msgStart;
    }
    
    private static double parseChoiceArgument(final MessagePattern pattern, int partIndex, final String source, final ParsePosition pos) {
        int furthest;
        final int start = furthest = pos.getIndex();
        double bestNumber = Double.NaN;
        double tempNumber = 0.0;
        while (pattern.getPartType(partIndex) != MessagePattern.Part.Type.ARG_LIMIT) {
            tempNumber = pattern.getNumericValue(pattern.getPart(partIndex));
            partIndex += 2;
            final int msgLimit = pattern.getLimitPartIndex(partIndex);
            final int len = matchStringUntilLimitPart(pattern, partIndex, msgLimit, source, start);
            if (len >= 0) {
                final int newIndex = start + len;
                if (newIndex > furthest) {
                    furthest = newIndex;
                    bestNumber = tempNumber;
                    if (furthest == source.length()) {
                        break;
                    }
                }
            }
            partIndex = msgLimit + 1;
        }
        if (furthest == start) {
            pos.setErrorIndex(start);
        }
        else {
            pos.setIndex(furthest);
        }
        return bestNumber;
    }
    
    private static int matchStringUntilLimitPart(final MessagePattern pattern, int partIndex, final int limitPartIndex, final String source, final int sourceOffset) {
        int matchingSourceLength = 0;
        final String msgString = pattern.getPatternString();
        int prevIndex = pattern.getPart(partIndex).getLimit();
        while (true) {
            final MessagePattern.Part part = pattern.getPart(++partIndex);
            if (partIndex == limitPartIndex || part.getType() == MessagePattern.Part.Type.SKIP_SYNTAX) {
                final int index = part.getIndex();
                final int length = index - prevIndex;
                if (length != 0 && !source.regionMatches(sourceOffset, msgString, prevIndex, length)) {
                    return -1;
                }
                matchingSourceLength += length;
                if (partIndex == limitPartIndex) {
                    return matchingSourceLength;
                }
                prevIndex = part.getLimit();
            }
        }
    }
    
    private void format(final Object arguments, final AppendableWrapper result, final FieldPosition fp) {
        if (arguments == null || arguments instanceof Map) {
            this.format(null, (Map<String, Object>)arguments, result, fp);
        }
        else {
            this.format((Object[])arguments, null, result, fp);
        }
    }
    
    private void format(final Object[] arguments, final Map<String, Object> argsMap, final AppendableWrapper dest, final FieldPosition fp) {
        if (arguments != null && this.msgPattern.hasNamedArguments()) {
            throw new IllegalArgumentException("This method is not available in MessageFormat objects that use alphanumeric argument names.");
        }
        this.format(0, 0.0, arguments, argsMap, dest, fp);
    }
    
    private void resetPattern() {
        if (this.msgPattern != null) {
            this.msgPattern.clear();
        }
        if (this.cachedFormatters != null) {
            this.cachedFormatters.clear();
        }
        this.customFormatArgStarts = null;
    }
    
    private Format createAppropriateFormat(final String type, final String style) {
        Format newFormat = null;
        final int subformatType = findKeyword(type, MessageFormat.typeList);
        Label_0558: {
            switch (subformatType) {
                case 0: {
                    switch (findKeyword(style, MessageFormat.modifierList)) {
                        case 0: {
                            newFormat = NumberFormat.getInstance(this.ulocale);
                            break Label_0558;
                        }
                        case 1: {
                            newFormat = NumberFormat.getCurrencyInstance(this.ulocale);
                            break Label_0558;
                        }
                        case 2: {
                            newFormat = NumberFormat.getPercentInstance(this.ulocale);
                            break Label_0558;
                        }
                        case 3: {
                            newFormat = NumberFormat.getIntegerInstance(this.ulocale);
                            break Label_0558;
                        }
                        default: {
                            newFormat = new DecimalFormat(style, new DecimalFormatSymbols(this.ulocale));
                            break Label_0558;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (findKeyword(style, MessageFormat.dateModifierList)) {
                        case 0: {
                            newFormat = DateFormat.getDateInstance(2, this.ulocale);
                            break Label_0558;
                        }
                        case 1: {
                            newFormat = DateFormat.getDateInstance(3, this.ulocale);
                            break Label_0558;
                        }
                        case 2: {
                            newFormat = DateFormat.getDateInstance(2, this.ulocale);
                            break Label_0558;
                        }
                        case 3: {
                            newFormat = DateFormat.getDateInstance(1, this.ulocale);
                            break Label_0558;
                        }
                        case 4: {
                            newFormat = DateFormat.getDateInstance(0, this.ulocale);
                            break Label_0558;
                        }
                        default: {
                            newFormat = new SimpleDateFormat(style, this.ulocale);
                            break Label_0558;
                        }
                    }
                    break;
                }
                case 2: {
                    switch (findKeyword(style, MessageFormat.dateModifierList)) {
                        case 0: {
                            newFormat = DateFormat.getTimeInstance(2, this.ulocale);
                            break Label_0558;
                        }
                        case 1: {
                            newFormat = DateFormat.getTimeInstance(3, this.ulocale);
                            break Label_0558;
                        }
                        case 2: {
                            newFormat = DateFormat.getTimeInstance(2, this.ulocale);
                            break Label_0558;
                        }
                        case 3: {
                            newFormat = DateFormat.getTimeInstance(1, this.ulocale);
                            break Label_0558;
                        }
                        case 4: {
                            newFormat = DateFormat.getTimeInstance(0, this.ulocale);
                            break Label_0558;
                        }
                        default: {
                            newFormat = new SimpleDateFormat(style, this.ulocale);
                            break Label_0558;
                        }
                    }
                    break;
                }
                case 3: {
                    final RuleBasedNumberFormat rbnf = new RuleBasedNumberFormat(this.ulocale, 1);
                    final String ruleset = style.trim();
                    if (ruleset.length() != 0) {
                        try {
                            rbnf.setDefaultRuleSet(ruleset);
                        }
                        catch (Exception ex) {}
                    }
                    newFormat = rbnf;
                    break;
                }
                case 4: {
                    final RuleBasedNumberFormat rbnf = new RuleBasedNumberFormat(this.ulocale, 2);
                    final String ruleset = style.trim();
                    if (ruleset.length() != 0) {
                        try {
                            rbnf.setDefaultRuleSet(ruleset);
                        }
                        catch (Exception ex2) {}
                    }
                    newFormat = rbnf;
                    break;
                }
                case 5: {
                    final RuleBasedNumberFormat rbnf = new RuleBasedNumberFormat(this.ulocale, 3);
                    final String ruleset = style.trim();
                    if (ruleset.length() != 0) {
                        try {
                            rbnf.setDefaultRuleSet(ruleset);
                        }
                        catch (Exception ex3) {}
                    }
                    newFormat = rbnf;
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unknown format type \"" + type + "\"");
                }
            }
        }
        return newFormat;
    }
    
    private static final int findKeyword(String s, final String[] list) {
        s = PatternProps.trimWhiteSpace(s).toLowerCase(MessageFormat.rootLocale);
        for (int i = 0; i < list.length; ++i) {
            if (s.equals(list[i])) {
                return i;
            }
        }
        return -1;
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(this.ulocale.toLanguageTag());
        if (this.msgPattern == null) {
            this.msgPattern = new MessagePattern();
        }
        out.writeObject(this.msgPattern.getApostropheMode());
        out.writeObject(this.msgPattern.getPatternString());
        if (this.customFormatArgStarts == null || this.customFormatArgStarts.isEmpty()) {
            out.writeInt(0);
        }
        else {
            out.writeInt(this.customFormatArgStarts.size());
            int formatIndex = 0;
            int partIndex = 0;
            while ((partIndex = this.nextTopLevelArgStart(partIndex)) >= 0) {
                if (this.customFormatArgStarts.contains(partIndex)) {
                    out.writeInt(formatIndex);
                    out.writeObject(this.cachedFormatters.get(partIndex));
                }
                ++formatIndex;
            }
        }
        out.writeInt(0);
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        final String languageTag = (String)in.readObject();
        this.ulocale = ULocale.forLanguageTag(languageTag);
        final MessagePattern.ApostropheMode aposMode = (MessagePattern.ApostropheMode)in.readObject();
        if (this.msgPattern == null || aposMode != this.msgPattern.getApostropheMode()) {
            this.msgPattern = new MessagePattern(aposMode);
        }
        final String msg = (String)in.readObject();
        if (msg != null) {
            this.applyPattern(msg);
        }
        for (int numFormatters = in.readInt(); numFormatters > 0; --numFormatters) {
            final int formatIndex = in.readInt();
            final Format formatter = (Format)in.readObject();
            this.setFormat(formatIndex, formatter);
        }
        for (int numPairs = in.readInt(); numPairs > 0; --numPairs) {
            in.readInt();
            in.readObject();
        }
    }
    
    private void cacheExplicitFormats() {
        if (this.cachedFormatters != null) {
            this.cachedFormatters.clear();
        }
        this.customFormatArgStarts = null;
        for (int limit = this.msgPattern.countParts() - 2, i = 1; i < limit; ++i) {
            MessagePattern.Part part = this.msgPattern.getPart(i);
            if (part.getType() == MessagePattern.Part.Type.ARG_START) {
                final MessagePattern.ArgType argType = part.getArgType();
                if (argType == MessagePattern.ArgType.SIMPLE) {
                    final int index = i;
                    i += 2;
                    final String explicitType = this.msgPattern.getSubstring(this.msgPattern.getPart(i++));
                    String style = "";
                    if ((part = this.msgPattern.getPart(i)).getType() == MessagePattern.Part.Type.ARG_STYLE) {
                        style = this.msgPattern.getSubstring(part);
                        ++i;
                    }
                    final Format formatter = this.createAppropriateFormat(explicitType, style);
                    this.setArgStartFormat(index, formatter);
                }
            }
        }
    }
    
    private void setArgStartFormat(final int argStart, final Format formatter) {
        if (this.cachedFormatters == null) {
            this.cachedFormatters = new HashMap<Integer, Format>();
        }
        this.cachedFormatters.put(argStart, formatter);
    }
    
    private void setCustomArgStartFormat(final int argStart, final Format formatter) {
        this.setArgStartFormat(argStart, formatter);
        if (this.customFormatArgStarts == null) {
            this.customFormatArgStarts = new HashSet<Integer>();
        }
        this.customFormatArgStarts.add(argStart);
    }
    
    public static String autoQuoteApostrophe(final String pattern) {
        final StringBuilder buf = new StringBuilder(pattern.length() * 2);
        int state = 0;
        int braceCount = 0;
        for (int i = 0, j = pattern.length(); i < j; ++i) {
            final char c = pattern.charAt(i);
            Label_0242: {
                switch (state) {
                    case 0: {
                        switch (c) {
                            case '\'': {
                                state = 1;
                                break;
                            }
                            case '{': {
                                state = 3;
                                ++braceCount;
                                break;
                            }
                        }
                        break;
                    }
                    case 1: {
                        switch (c) {
                            case '\'': {
                                state = 0;
                                break Label_0242;
                            }
                            case '{':
                            case '}': {
                                state = 2;
                                break Label_0242;
                            }
                            default: {
                                buf.append('\'');
                                state = 0;
                                break Label_0242;
                            }
                        }
                        break;
                    }
                    case 2: {
                        switch (c) {
                            case '\'': {
                                state = 0;
                                break;
                            }
                        }
                        break;
                    }
                    case 3: {
                        switch (c) {
                            case '{': {
                                ++braceCount;
                                break;
                            }
                            case '}': {
                                if (--braceCount == 0) {
                                    state = 0;
                                    break;
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            buf.append(c);
        }
        if (state == 1 || state == 2) {
            buf.append('\'');
        }
        return new String(buf);
    }
    
    static {
        typeList = new String[] { "number", "date", "time", "spellout", "ordinal", "duration" };
        modifierList = new String[] { "", "currency", "percent", "integer" };
        dateModifierList = new String[] { "", "short", "medium", "long", "full" };
        rootLocale = new Locale("");
    }
    
    public static class Field extends Format.Field
    {
        private static final long serialVersionUID = 7510380454602616157L;
        public static final Field ARGUMENT;
        
        protected Field(final String name) {
            super(name);
        }
        
        @Override
        protected Object readResolve() throws InvalidObjectException {
            if (this.getClass() != Field.class) {
                throw new InvalidObjectException("A subclass of MessageFormat.Field must implement readResolve.");
            }
            if (this.getName().equals(Field.ARGUMENT.getName())) {
                return Field.ARGUMENT;
            }
            throw new InvalidObjectException("Unknown attribute name.");
        }
        
        static {
            ARGUMENT = new Field("message argument field");
        }
    }
    
    private static final class PluralSelectorProvider implements PluralFormat.PluralSelector
    {
        private ULocale locale;
        private PluralRules rules;
        private PluralRules.PluralType type;
        
        public PluralSelectorProvider(final ULocale loc, final PluralRules.PluralType type) {
            this.locale = loc;
            this.type = type;
        }
        
        public String select(final double number) {
            if (this.rules == null) {
                this.rules = PluralRules.forLocale(this.locale, this.type);
            }
            return this.rules.select(number);
        }
    }
    
    private static final class AppendableWrapper
    {
        private Appendable app;
        private int length;
        private List<AttributeAndPosition> attributes;
        
        public AppendableWrapper(final StringBuilder sb) {
            this.app = sb;
            this.length = sb.length();
            this.attributes = null;
        }
        
        public AppendableWrapper(final StringBuffer sb) {
            this.app = sb;
            this.length = sb.length();
            this.attributes = null;
        }
        
        public void useAttributes() {
            this.attributes = new ArrayList<AttributeAndPosition>();
        }
        
        public void append(final CharSequence s) {
            try {
                this.app.append(s);
                this.length += s.length();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        public void append(final CharSequence s, final int start, final int limit) {
            try {
                this.app.append(s, start, limit);
                this.length += limit - start;
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        public void append(final CharacterIterator iterator) {
            this.length += append(this.app, iterator);
        }
        
        public static int append(final Appendable result, final CharacterIterator iterator) {
            try {
                int start = iterator.getBeginIndex();
                final int limit = iterator.getEndIndex();
                final int length = limit - start;
                if (start < limit) {
                    result.append(iterator.first());
                    while (++start < limit) {
                        result.append(iterator.next());
                    }
                }
                return length;
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        public void formatAndAppend(final Format formatter, final Object arg) {
            if (this.attributes == null) {
                this.append(formatter.format(arg));
            }
            else {
                final AttributedCharacterIterator formattedArg = formatter.formatToCharacterIterator(arg);
                final int prevLength = this.length;
                this.append(formattedArg);
                formattedArg.first();
                int start = formattedArg.getIndex();
                final int limit = formattedArg.getEndIndex();
                final int offset = prevLength - start;
                while (start < limit) {
                    final Map<AttributedCharacterIterator.Attribute, Object> map = formattedArg.getAttributes();
                    final int runLimit = formattedArg.getRunLimit();
                    if (map.size() != 0) {
                        for (final Map.Entry<AttributedCharacterIterator.Attribute, Object> entry : map.entrySet()) {
                            this.attributes.add(new AttributeAndPosition(entry.getKey(), entry.getValue(), offset + start, offset + runLimit));
                        }
                    }
                    start = runLimit;
                    formattedArg.setIndex(start);
                }
            }
        }
    }
    
    private static final class AttributeAndPosition
    {
        private AttributedCharacterIterator.Attribute key;
        private Object value;
        private int start;
        private int limit;
        
        public AttributeAndPosition(final Object fieldValue, final int startIndex, final int limitIndex) {
            this.init(Field.ARGUMENT, fieldValue, startIndex, limitIndex);
        }
        
        public AttributeAndPosition(final AttributedCharacterIterator.Attribute field, final Object fieldValue, final int startIndex, final int limitIndex) {
            this.init(field, fieldValue, startIndex, limitIndex);
        }
        
        public void init(final AttributedCharacterIterator.Attribute field, final Object fieldValue, final int startIndex, final int limitIndex) {
            this.key = field;
            this.value = fieldValue;
            this.start = startIndex;
            this.limit = limitIndex;
        }
    }
}
