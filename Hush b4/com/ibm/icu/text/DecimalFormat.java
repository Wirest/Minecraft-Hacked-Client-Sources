// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator;
import java.text.Format;
import com.ibm.icu.impl.Utility;
import java.math.RoundingMode;
import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.impl.ICUConfig;
import java.util.HashSet;
import java.util.Iterator;
import com.ibm.icu.util.CurrencyAmount;
import java.text.ParsePosition;
import java.math.BigInteger;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.ULocale;
import java.util.Set;
import java.text.FieldPosition;
import java.util.ArrayList;
import com.ibm.icu.math.MathContext;
import java.math.BigDecimal;
import java.text.ChoiceFormat;

public class DecimalFormat extends NumberFormat
{
    private static double epsilon;
    private static final int CURRENCY_SIGN_COUNT_IN_SYMBOL_FORMAT = 1;
    private static final int CURRENCY_SIGN_COUNT_IN_ISO_FORMAT = 2;
    private static final int CURRENCY_SIGN_COUNT_IN_PLURAL_FORMAT = 3;
    private static final int STATUS_INFINITE = 0;
    private static final int STATUS_POSITIVE = 1;
    private static final int STATUS_UNDERFLOW = 2;
    private static final int STATUS_LENGTH = 3;
    private static final UnicodeSet dotEquivalents;
    private static final UnicodeSet commaEquivalents;
    private static final UnicodeSet strictDotEquivalents;
    private static final UnicodeSet strictCommaEquivalents;
    private static final UnicodeSet defaultGroupingSeparators;
    private static final UnicodeSet strictDefaultGroupingSeparators;
    private int PARSE_MAX_EXPONENT;
    static final double roundingIncrementEpsilon = 1.0E-9;
    private transient DigitList digitList;
    private String positivePrefix;
    private String positiveSuffix;
    private String negativePrefix;
    private String negativeSuffix;
    private String posPrefixPattern;
    private String posSuffixPattern;
    private String negPrefixPattern;
    private String negSuffixPattern;
    private ChoiceFormat currencyChoice;
    private int multiplier;
    private byte groupingSize;
    private byte groupingSize2;
    private boolean decimalSeparatorAlwaysShown;
    private DecimalFormatSymbols symbols;
    private boolean useSignificantDigits;
    private int minSignificantDigits;
    private int maxSignificantDigits;
    private boolean useExponentialNotation;
    private byte minExponentDigits;
    private boolean exponentSignAlwaysShown;
    private BigDecimal roundingIncrement;
    private transient com.ibm.icu.math.BigDecimal roundingIncrementICU;
    private transient double roundingDouble;
    private transient double roundingDoubleReciprocal;
    private int roundingMode;
    private MathContext mathContext;
    private int formatWidth;
    private char pad;
    private int padPosition;
    private boolean parseBigDecimal;
    static final int currentSerialVersion = 3;
    private int serialVersionOnStream;
    public static final int PAD_BEFORE_PREFIX = 0;
    public static final int PAD_AFTER_PREFIX = 1;
    public static final int PAD_BEFORE_SUFFIX = 2;
    public static final int PAD_AFTER_SUFFIX = 3;
    static final char PATTERN_ZERO_DIGIT = '0';
    static final char PATTERN_ONE_DIGIT = '1';
    static final char PATTERN_TWO_DIGIT = '2';
    static final char PATTERN_THREE_DIGIT = '3';
    static final char PATTERN_FOUR_DIGIT = '4';
    static final char PATTERN_FIVE_DIGIT = '5';
    static final char PATTERN_SIX_DIGIT = '6';
    static final char PATTERN_SEVEN_DIGIT = '7';
    static final char PATTERN_EIGHT_DIGIT = '8';
    static final char PATTERN_NINE_DIGIT = '9';
    static final char PATTERN_GROUPING_SEPARATOR = ',';
    static final char PATTERN_DECIMAL_SEPARATOR = '.';
    static final char PATTERN_DIGIT = '#';
    static final char PATTERN_SIGNIFICANT_DIGIT = '@';
    static final char PATTERN_EXPONENT = 'E';
    static final char PATTERN_PLUS_SIGN = '+';
    private static final char PATTERN_PER_MILLE = '\u2030';
    private static final char PATTERN_PERCENT = '%';
    static final char PATTERN_PAD_ESCAPE = '*';
    private static final char PATTERN_MINUS = '-';
    private static final char PATTERN_SEPARATOR = ';';
    private static final char CURRENCY_SIGN = '¤';
    private static final char QUOTE = '\'';
    static final int DOUBLE_INTEGER_DIGITS = 309;
    static final int DOUBLE_FRACTION_DIGITS = 340;
    static final int MAX_SCIENTIFIC_INTEGER_DIGITS = 8;
    private static final long serialVersionUID = 864413376551465018L;
    private ArrayList<FieldPosition> attributes;
    private String formatPattern;
    private int style;
    private int currencySignCount;
    private transient Set<AffixForCurrency> affixPatternsForCurrency;
    private transient boolean isReadyForParsing;
    private CurrencyPluralInfo currencyPluralInfo;
    static final Unit NULL_UNIT;
    
    public DecimalFormat() {
        this.PARSE_MAX_EXPONENT = 1000;
        this.digitList = new DigitList();
        this.positivePrefix = "";
        this.positiveSuffix = "";
        this.negativePrefix = "-";
        this.negativeSuffix = "";
        this.multiplier = 1;
        this.groupingSize = 3;
        this.groupingSize2 = 0;
        this.decimalSeparatorAlwaysShown = false;
        this.symbols = null;
        this.useSignificantDigits = false;
        this.minSignificantDigits = 1;
        this.maxSignificantDigits = 6;
        this.exponentSignAlwaysShown = false;
        this.roundingIncrement = null;
        this.roundingIncrementICU = null;
        this.roundingDouble = 0.0;
        this.roundingDoubleReciprocal = 0.0;
        this.roundingMode = 6;
        this.mathContext = new MathContext(0, 0);
        this.formatWidth = 0;
        this.pad = ' ';
        this.padPosition = 0;
        this.parseBigDecimal = false;
        this.serialVersionOnStream = 3;
        this.attributes = new ArrayList<FieldPosition>();
        this.formatPattern = "";
        this.style = 0;
        this.currencySignCount = 0;
        this.affixPatternsForCurrency = null;
        this.isReadyForParsing = false;
        this.currencyPluralInfo = null;
        final ULocale def = ULocale.getDefault(ULocale.Category.FORMAT);
        final String pattern = NumberFormat.getPattern(def, 0);
        this.symbols = new DecimalFormatSymbols(def);
        this.setCurrency(Currency.getInstance(def));
        this.applyPatternWithoutExpandAffix(pattern, false);
        if (this.currencySignCount == 3) {
            this.currencyPluralInfo = new CurrencyPluralInfo(def);
        }
        else {
            this.expandAffixAdjustWidth(null);
        }
    }
    
    public DecimalFormat(final String pattern) {
        this.PARSE_MAX_EXPONENT = 1000;
        this.digitList = new DigitList();
        this.positivePrefix = "";
        this.positiveSuffix = "";
        this.negativePrefix = "-";
        this.negativeSuffix = "";
        this.multiplier = 1;
        this.groupingSize = 3;
        this.groupingSize2 = 0;
        this.decimalSeparatorAlwaysShown = false;
        this.symbols = null;
        this.useSignificantDigits = false;
        this.minSignificantDigits = 1;
        this.maxSignificantDigits = 6;
        this.exponentSignAlwaysShown = false;
        this.roundingIncrement = null;
        this.roundingIncrementICU = null;
        this.roundingDouble = 0.0;
        this.roundingDoubleReciprocal = 0.0;
        this.roundingMode = 6;
        this.mathContext = new MathContext(0, 0);
        this.formatWidth = 0;
        this.pad = ' ';
        this.padPosition = 0;
        this.parseBigDecimal = false;
        this.serialVersionOnStream = 3;
        this.attributes = new ArrayList<FieldPosition>();
        this.formatPattern = "";
        this.style = 0;
        this.currencySignCount = 0;
        this.affixPatternsForCurrency = null;
        this.isReadyForParsing = false;
        this.currencyPluralInfo = null;
        final ULocale def = ULocale.getDefault(ULocale.Category.FORMAT);
        this.symbols = new DecimalFormatSymbols(def);
        this.setCurrency(Currency.getInstance(def));
        this.applyPatternWithoutExpandAffix(pattern, false);
        if (this.currencySignCount == 3) {
            this.currencyPluralInfo = new CurrencyPluralInfo(def);
        }
        else {
            this.expandAffixAdjustWidth(null);
        }
    }
    
    public DecimalFormat(final String pattern, final DecimalFormatSymbols symbols) {
        this.PARSE_MAX_EXPONENT = 1000;
        this.digitList = new DigitList();
        this.positivePrefix = "";
        this.positiveSuffix = "";
        this.negativePrefix = "-";
        this.negativeSuffix = "";
        this.multiplier = 1;
        this.groupingSize = 3;
        this.groupingSize2 = 0;
        this.decimalSeparatorAlwaysShown = false;
        this.symbols = null;
        this.useSignificantDigits = false;
        this.minSignificantDigits = 1;
        this.maxSignificantDigits = 6;
        this.exponentSignAlwaysShown = false;
        this.roundingIncrement = null;
        this.roundingIncrementICU = null;
        this.roundingDouble = 0.0;
        this.roundingDoubleReciprocal = 0.0;
        this.roundingMode = 6;
        this.mathContext = new MathContext(0, 0);
        this.formatWidth = 0;
        this.pad = ' ';
        this.padPosition = 0;
        this.parseBigDecimal = false;
        this.serialVersionOnStream = 3;
        this.attributes = new ArrayList<FieldPosition>();
        this.formatPattern = "";
        this.style = 0;
        this.currencySignCount = 0;
        this.affixPatternsForCurrency = null;
        this.isReadyForParsing = false;
        this.currencyPluralInfo = null;
        this.createFromPatternAndSymbols(pattern, symbols);
    }
    
    private void createFromPatternAndSymbols(final String pattern, final DecimalFormatSymbols inputSymbols) {
        this.symbols = (DecimalFormatSymbols)inputSymbols.clone();
        this.setCurrencyForSymbols();
        this.applyPatternWithoutExpandAffix(pattern, false);
        if (this.currencySignCount == 3) {
            this.currencyPluralInfo = new CurrencyPluralInfo(this.symbols.getULocale());
        }
        else {
            this.expandAffixAdjustWidth(null);
        }
    }
    
    public DecimalFormat(final String pattern, final DecimalFormatSymbols symbols, final CurrencyPluralInfo infoInput, final int style) {
        this.PARSE_MAX_EXPONENT = 1000;
        this.digitList = new DigitList();
        this.positivePrefix = "";
        this.positiveSuffix = "";
        this.negativePrefix = "-";
        this.negativeSuffix = "";
        this.multiplier = 1;
        this.groupingSize = 3;
        this.groupingSize2 = 0;
        this.decimalSeparatorAlwaysShown = false;
        this.symbols = null;
        this.useSignificantDigits = false;
        this.minSignificantDigits = 1;
        this.maxSignificantDigits = 6;
        this.exponentSignAlwaysShown = false;
        this.roundingIncrement = null;
        this.roundingIncrementICU = null;
        this.roundingDouble = 0.0;
        this.roundingDoubleReciprocal = 0.0;
        this.roundingMode = 6;
        this.mathContext = new MathContext(0, 0);
        this.formatWidth = 0;
        this.pad = ' ';
        this.padPosition = 0;
        this.parseBigDecimal = false;
        this.serialVersionOnStream = 3;
        this.attributes = new ArrayList<FieldPosition>();
        this.formatPattern = "";
        this.style = 0;
        this.currencySignCount = 0;
        this.affixPatternsForCurrency = null;
        this.isReadyForParsing = false;
        this.currencyPluralInfo = null;
        CurrencyPluralInfo info = infoInput;
        if (style == 6) {
            info = (CurrencyPluralInfo)infoInput.clone();
        }
        this.create(pattern, symbols, info, style);
    }
    
    private void create(final String pattern, final DecimalFormatSymbols inputSymbols, final CurrencyPluralInfo info, final int inputStyle) {
        if (inputStyle != 6) {
            this.createFromPatternAndSymbols(pattern, inputSymbols);
        }
        else {
            this.symbols = (DecimalFormatSymbols)inputSymbols.clone();
            this.currencyPluralInfo = info;
            final String currencyPluralPatternForOther = this.currencyPluralInfo.getCurrencyPluralPattern("other");
            this.applyPatternWithoutExpandAffix(currencyPluralPatternForOther, false);
            this.setCurrencyForSymbols();
        }
        this.style = inputStyle;
    }
    
    DecimalFormat(final String pattern, final DecimalFormatSymbols inputSymbols, final int style) {
        this.PARSE_MAX_EXPONENT = 1000;
        this.digitList = new DigitList();
        this.positivePrefix = "";
        this.positiveSuffix = "";
        this.negativePrefix = "-";
        this.negativeSuffix = "";
        this.multiplier = 1;
        this.groupingSize = 3;
        this.groupingSize2 = 0;
        this.decimalSeparatorAlwaysShown = false;
        this.symbols = null;
        this.useSignificantDigits = false;
        this.minSignificantDigits = 1;
        this.maxSignificantDigits = 6;
        this.exponentSignAlwaysShown = false;
        this.roundingIncrement = null;
        this.roundingIncrementICU = null;
        this.roundingDouble = 0.0;
        this.roundingDoubleReciprocal = 0.0;
        this.roundingMode = 6;
        this.mathContext = new MathContext(0, 0);
        this.formatWidth = 0;
        this.pad = ' ';
        this.padPosition = 0;
        this.parseBigDecimal = false;
        this.serialVersionOnStream = 3;
        this.attributes = new ArrayList<FieldPosition>();
        this.formatPattern = "";
        this.style = 0;
        this.currencySignCount = 0;
        this.affixPatternsForCurrency = null;
        this.isReadyForParsing = false;
        this.currencyPluralInfo = null;
        CurrencyPluralInfo info = null;
        if (style == 6) {
            info = new CurrencyPluralInfo(inputSymbols.getULocale());
        }
        this.create(pattern, inputSymbols, info, style);
    }
    
    @Override
    public StringBuffer format(final double number, final StringBuffer result, final FieldPosition fieldPosition) {
        return this.format(number, result, fieldPosition, false);
    }
    
    private boolean isNegative(final double number) {
        return number < 0.0 || (number == 0.0 && 1.0 / number < 0.0);
    }
    
    private double round(double number) {
        final boolean isNegative = this.isNegative(number);
        if (isNegative) {
            number = -number;
        }
        if (this.roundingDouble > 0.0) {
            return round(number, this.roundingDouble, this.roundingDoubleReciprocal, this.roundingMode, isNegative);
        }
        return number;
    }
    
    private double multiply(final double number) {
        if (this.multiplier != 1) {
            return number * this.multiplier;
        }
        return number;
    }
    
    private StringBuffer format(double number, final StringBuffer result, final FieldPosition fieldPosition, final boolean parseAttr) {
        fieldPosition.setBeginIndex(0);
        fieldPosition.setEndIndex(0);
        if (Double.isNaN(number)) {
            if (fieldPosition.getField() == 0) {
                fieldPosition.setBeginIndex(result.length());
            }
            else if (fieldPosition.getFieldAttribute() == Field.INTEGER) {
                fieldPosition.setBeginIndex(result.length());
            }
            result.append(this.symbols.getNaN());
            if (parseAttr) {
                this.addAttribute(Field.INTEGER, result.length() - this.symbols.getNaN().length(), result.length());
            }
            if (fieldPosition.getField() == 0) {
                fieldPosition.setEndIndex(result.length());
            }
            else if (fieldPosition.getFieldAttribute() == Field.INTEGER) {
                fieldPosition.setEndIndex(result.length());
            }
            this.addPadding(result, fieldPosition, 0, 0);
            return result;
        }
        number = this.multiply(number);
        final boolean isNegative = this.isNegative(number);
        number = this.round(number);
        if (Double.isInfinite(number)) {
            final int prefixLen = this.appendAffix(result, isNegative, true, parseAttr);
            if (fieldPosition.getField() == 0) {
                fieldPosition.setBeginIndex(result.length());
            }
            else if (fieldPosition.getFieldAttribute() == Field.INTEGER) {
                fieldPosition.setBeginIndex(result.length());
            }
            result.append(this.symbols.getInfinity());
            if (parseAttr) {
                this.addAttribute(Field.INTEGER, result.length() - this.symbols.getInfinity().length(), result.length());
            }
            if (fieldPosition.getField() == 0) {
                fieldPosition.setEndIndex(result.length());
            }
            else if (fieldPosition.getFieldAttribute() == Field.INTEGER) {
                fieldPosition.setEndIndex(result.length());
            }
            final int suffixLen = this.appendAffix(result, isNegative, false, parseAttr);
            this.addPadding(result, fieldPosition, prefixLen, suffixLen);
            return result;
        }
        synchronized (this.digitList) {
            this.digitList.set(number, this.precision(false), !this.useExponentialNotation && !this.areSignificantDigitsUsed());
            return this.subformat(number, result, fieldPosition, isNegative, false, parseAttr);
        }
    }
    
    @Deprecated
    double adjustNumberAsInFormatting(double number) {
        if (Double.isNaN(number)) {
            return number;
        }
        number = this.round(this.multiply(number));
        if (Double.isInfinite(number)) {
            return number;
        }
        final DigitList dl = new DigitList();
        dl.set(number, this.precision(false), false);
        return dl.getDouble();
    }
    
    @Deprecated
    boolean isNumberNegative(final double number) {
        return !Double.isNaN(number) && this.isNegative(this.multiply(number));
    }
    
    private static double round(double number, final double roundingInc, final double roundingIncReciprocal, final int mode, final boolean isNegative) {
        double div = (roundingIncReciprocal == 0.0) ? (number / roundingInc) : (number * roundingIncReciprocal);
        Label_0381: {
            switch (mode) {
                case 2: {
                    div = (isNegative ? Math.floor(div + DecimalFormat.epsilon) : Math.ceil(div - DecimalFormat.epsilon));
                    break;
                }
                case 3: {
                    div = (isNegative ? Math.ceil(div - DecimalFormat.epsilon) : Math.floor(div + DecimalFormat.epsilon));
                    break;
                }
                case 1: {
                    div = Math.floor(div + DecimalFormat.epsilon);
                    break;
                }
                case 0: {
                    div = Math.ceil(div - DecimalFormat.epsilon);
                    break;
                }
                case 7: {
                    if (div != Math.floor(div)) {
                        throw new ArithmeticException("Rounding necessary");
                    }
                    return number;
                }
                default: {
                    final double ceil = Math.ceil(div);
                    final double ceildiff = ceil - div;
                    final double floor = Math.floor(div);
                    final double floordiff = div - floor;
                    switch (mode) {
                        case 6: {
                            if (floordiff + DecimalFormat.epsilon < ceildiff) {
                                div = floor;
                                break Label_0381;
                            }
                            if (ceildiff + DecimalFormat.epsilon < floordiff) {
                                div = ceil;
                                break Label_0381;
                            }
                            final double testFloor = floor / 2.0;
                            div = ((testFloor == Math.floor(testFloor)) ? floor : ceil);
                            break Label_0381;
                        }
                        case 5: {
                            div = ((floordiff <= ceildiff + DecimalFormat.epsilon) ? floor : ceil);
                            break Label_0381;
                        }
                        case 4: {
                            div = ((ceildiff <= floordiff + DecimalFormat.epsilon) ? ceil : floor);
                            break Label_0381;
                        }
                        default: {
                            throw new IllegalArgumentException("Invalid rounding mode: " + mode);
                        }
                    }
                    break;
                }
            }
        }
        number = ((roundingIncReciprocal == 0.0) ? (div * roundingInc) : (div / roundingIncReciprocal));
        return number;
    }
    
    @Override
    public StringBuffer format(final long number, final StringBuffer result, final FieldPosition fieldPosition) {
        return this.format(number, result, fieldPosition, false);
    }
    
    private StringBuffer format(long number, final StringBuffer result, final FieldPosition fieldPosition, final boolean parseAttr) {
        fieldPosition.setBeginIndex(0);
        fieldPosition.setEndIndex(0);
        if (this.roundingIncrementICU != null) {
            return this.format(com.ibm.icu.math.BigDecimal.valueOf(number), result, fieldPosition);
        }
        final boolean isNegative = number < 0L;
        if (isNegative) {
            number = -number;
        }
        if (this.multiplier != 1) {
            boolean tooBig = false;
            if (number < 0L) {
                final long cutoff = Long.MIN_VALUE / this.multiplier;
                tooBig = (number <= cutoff);
            }
            else {
                final long cutoff = Long.MAX_VALUE / this.multiplier;
                tooBig = (number > cutoff);
            }
            if (tooBig) {
                return this.format(BigInteger.valueOf(isNegative ? (-number) : number), result, fieldPosition, parseAttr);
            }
        }
        number *= this.multiplier;
        synchronized (this.digitList) {
            this.digitList.set(number, this.precision(true));
            return this.subformat((double)number, result, fieldPosition, isNegative, true, parseAttr);
        }
    }
    
    @Override
    public StringBuffer format(final BigInteger number, final StringBuffer result, final FieldPosition fieldPosition) {
        return this.format(number, result, fieldPosition, false);
    }
    
    private StringBuffer format(BigInteger number, final StringBuffer result, final FieldPosition fieldPosition, final boolean parseAttr) {
        if (this.roundingIncrementICU != null) {
            return this.format(new com.ibm.icu.math.BigDecimal(number), result, fieldPosition);
        }
        if (this.multiplier != 1) {
            number = number.multiply(BigInteger.valueOf(this.multiplier));
        }
        synchronized (this.digitList) {
            this.digitList.set(number, this.precision(true));
            return this.subformat(number.intValue(), result, fieldPosition, number.signum() < 0, true, parseAttr);
        }
    }
    
    @Override
    public StringBuffer format(final BigDecimal number, final StringBuffer result, final FieldPosition fieldPosition) {
        return this.format(number, result, fieldPosition, false);
    }
    
    private StringBuffer format(BigDecimal number, final StringBuffer result, final FieldPosition fieldPosition, final boolean parseAttr) {
        if (this.multiplier != 1) {
            number = number.multiply(BigDecimal.valueOf(this.multiplier));
        }
        if (this.roundingIncrement != null) {
            number = number.divide(this.roundingIncrement, 0, this.roundingMode).multiply(this.roundingIncrement);
        }
        synchronized (this.digitList) {
            this.digitList.set(number, this.precision(false), !this.useExponentialNotation && !this.areSignificantDigitsUsed());
            return this.subformat(number.doubleValue(), result, fieldPosition, number.signum() < 0, false, parseAttr);
        }
    }
    
    @Override
    public StringBuffer format(com.ibm.icu.math.BigDecimal number, final StringBuffer result, final FieldPosition fieldPosition) {
        if (this.multiplier != 1) {
            number = number.multiply(com.ibm.icu.math.BigDecimal.valueOf(this.multiplier), this.mathContext);
        }
        if (this.roundingIncrementICU != null) {
            number = number.divide(this.roundingIncrementICU, 0, this.roundingMode).multiply(this.roundingIncrementICU, this.mathContext);
        }
        synchronized (this.digitList) {
            this.digitList.set(number, this.precision(false), !this.useExponentialNotation && !this.areSignificantDigitsUsed());
            return this.subformat(number.doubleValue(), result, fieldPosition, number.signum() < 0, false, false);
        }
    }
    
    private boolean isGroupingPosition(final int pos) {
        boolean result = false;
        if (this.isGroupingUsed() && pos > 0 && this.groupingSize > 0) {
            if (this.groupingSize2 > 0 && pos > this.groupingSize) {
                result = ((pos - this.groupingSize) % this.groupingSize2 == 0);
            }
            else {
                result = (pos % this.groupingSize == 0);
            }
        }
        return result;
    }
    
    private int precision(final boolean isIntegral) {
        if (this.areSignificantDigitsUsed()) {
            return this.getMaximumSignificantDigits();
        }
        if (this.useExponentialNotation) {
            return this.getMinimumIntegerDigits() + this.getMaximumFractionDigits();
        }
        return isIntegral ? 0 : this.getMaximumFractionDigits();
    }
    
    private StringBuffer subformat(final int number, final StringBuffer result, final FieldPosition fieldPosition, final boolean isNegative, final boolean isInteger, final boolean parseAttr) {
        if (this.currencySignCount == 3) {
            return this.subformat(this.currencyPluralInfo.select(number), result, fieldPosition, isNegative, isInteger, parseAttr);
        }
        return this.subformat(result, fieldPosition, isNegative, isInteger, parseAttr);
    }
    
    private StringBuffer subformat(final double number, final StringBuffer result, final FieldPosition fieldPosition, final boolean isNegative, final boolean isInteger, final boolean parseAttr) {
        if (this.currencySignCount == 3) {
            return this.subformat(this.currencyPluralInfo.select(number), result, fieldPosition, isNegative, isInteger, parseAttr);
        }
        return this.subformat(result, fieldPosition, isNegative, isInteger, parseAttr);
    }
    
    private StringBuffer subformat(final String pluralCount, final StringBuffer result, final FieldPosition fieldPosition, final boolean isNegative, final boolean isInteger, final boolean parseAttr) {
        if (this.style == 6) {
            final String currencyPluralPattern = this.currencyPluralInfo.getCurrencyPluralPattern(pluralCount);
            if (!this.formatPattern.equals(currencyPluralPattern)) {
                this.applyPatternWithoutExpandAffix(currencyPluralPattern, false);
            }
        }
        this.expandAffixAdjustWidth(pluralCount);
        return this.subformat(result, fieldPosition, isNegative, isInteger, parseAttr);
    }
    
    private StringBuffer subformat(final StringBuffer result, final FieldPosition fieldPosition, final boolean isNegative, final boolean isInteger, final boolean parseAttr) {
        if (this.digitList.isZero()) {
            this.digitList.decimalAt = 0;
        }
        final int prefixLen = this.appendAffix(result, isNegative, true, parseAttr);
        if (this.useExponentialNotation) {
            this.subformatExponential(result, fieldPosition, parseAttr);
        }
        else {
            this.subformatFixed(result, fieldPosition, isInteger, parseAttr);
        }
        final int suffixLen = this.appendAffix(result, isNegative, false, parseAttr);
        this.addPadding(result, fieldPosition, prefixLen, suffixLen);
        return result;
    }
    
    private void subformatFixed(final StringBuffer result, final FieldPosition fieldPosition, final boolean isInteger, final boolean parseAttr) {
        final char[] digits = this.symbols.getDigitsLocal();
        final char grouping = (this.currencySignCount > 0) ? this.symbols.getMonetaryGroupingSeparator() : this.symbols.getGroupingSeparator();
        final char decimal = (this.currencySignCount > 0) ? this.symbols.getMonetaryDecimalSeparator() : this.symbols.getDecimalSeparator();
        final boolean useSigDig = this.areSignificantDigitsUsed();
        final int maxIntDig = this.getMaximumIntegerDigits();
        final int minIntDig = this.getMinimumIntegerDigits();
        final int intBegin = result.length();
        if (fieldPosition.getField() == 0) {
            fieldPosition.setBeginIndex(result.length());
        }
        else if (fieldPosition.getFieldAttribute() == Field.INTEGER) {
            fieldPosition.setBeginIndex(result.length());
        }
        int sigCount = 0;
        int minSigDig = this.getMinimumSignificantDigits();
        int maxSigDig = this.getMaximumSignificantDigits();
        if (!useSigDig) {
            minSigDig = 0;
            maxSigDig = Integer.MAX_VALUE;
        }
        int count = useSigDig ? Math.max(1, this.digitList.decimalAt) : minIntDig;
        if (this.digitList.decimalAt > 0 && count < this.digitList.decimalAt) {
            count = this.digitList.decimalAt;
        }
        int digitIndex = 0;
        if (count > maxIntDig && maxIntDig >= 0) {
            count = maxIntDig;
            digitIndex = this.digitList.decimalAt - count;
        }
        final int sizeBeforeIntegerPart = result.length();
        for (int i = count - 1; i >= 0; --i) {
            if (i < this.digitList.decimalAt && digitIndex < this.digitList.count && sigCount < maxSigDig) {
                result.append(digits[this.digitList.getDigitValue(digitIndex++)]);
                ++sigCount;
            }
            else {
                result.append(digits[0]);
                if (sigCount > 0) {
                    ++sigCount;
                }
            }
            if (this.isGroupingPosition(i)) {
                result.append(grouping);
                if (parseAttr) {
                    this.addAttribute(Field.GROUPING_SEPARATOR, result.length() - 1, result.length());
                }
            }
        }
        if (fieldPosition.getField() == 0) {
            fieldPosition.setEndIndex(result.length());
        }
        else if (fieldPosition.getFieldAttribute() == Field.INTEGER) {
            fieldPosition.setEndIndex(result.length());
        }
        final boolean fractionPresent = (!isInteger && digitIndex < this.digitList.count) || (useSigDig ? (sigCount < minSigDig) : (this.getMinimumFractionDigits() > 0));
        if (!fractionPresent && result.length() == sizeBeforeIntegerPart) {
            result.append(digits[0]);
        }
        if (parseAttr) {
            this.addAttribute(Field.INTEGER, intBegin, result.length());
        }
        if (this.decimalSeparatorAlwaysShown || fractionPresent) {
            if (fieldPosition.getFieldAttribute() == Field.DECIMAL_SEPARATOR) {
                fieldPosition.setBeginIndex(result.length());
            }
            result.append(decimal);
            if (fieldPosition.getFieldAttribute() == Field.DECIMAL_SEPARATOR) {
                fieldPosition.setEndIndex(result.length());
            }
            if (parseAttr) {
                this.addAttribute(Field.DECIMAL_SEPARATOR, result.length() - 1, result.length());
            }
        }
        if (fieldPosition.getField() == 1) {
            fieldPosition.setBeginIndex(result.length());
        }
        else if (fieldPosition.getFieldAttribute() == Field.FRACTION) {
            fieldPosition.setBeginIndex(result.length());
        }
        final int fracBegin = result.length();
        count = (useSigDig ? Integer.MAX_VALUE : this.getMaximumFractionDigits());
        if (useSigDig && (sigCount == maxSigDig || (sigCount >= minSigDig && digitIndex == this.digitList.count))) {
            count = 0;
        }
        for (int i = 0; i < count; ++i) {
            if (!useSigDig && i >= this.getMinimumFractionDigits()) {
                if (isInteger) {
                    break;
                }
                if (digitIndex >= this.digitList.count) {
                    break;
                }
            }
            if (-1 - i > this.digitList.decimalAt - 1) {
                result.append(digits[0]);
            }
            else {
                if (!isInteger && digitIndex < this.digitList.count) {
                    result.append(digits[this.digitList.getDigitValue(digitIndex++)]);
                }
                else {
                    result.append(digits[0]);
                }
                ++sigCount;
                if (useSigDig) {
                    if (sigCount == maxSigDig) {
                        break;
                    }
                    if (digitIndex == this.digitList.count && sigCount >= minSigDig) {
                        break;
                    }
                }
            }
        }
        if (fieldPosition.getField() == 1) {
            fieldPosition.setEndIndex(result.length());
        }
        else if (fieldPosition.getFieldAttribute() == Field.FRACTION) {
            fieldPosition.setEndIndex(result.length());
        }
        if (parseAttr && (this.decimalSeparatorAlwaysShown || fractionPresent)) {
            this.addAttribute(Field.FRACTION, fracBegin, result.length());
        }
    }
    
    private void subformatExponential(final StringBuffer result, final FieldPosition fieldPosition, final boolean parseAttr) {
        final char[] digits = this.symbols.getDigitsLocal();
        final char decimal = (this.currencySignCount > 0) ? this.symbols.getMonetaryDecimalSeparator() : this.symbols.getDecimalSeparator();
        final boolean useSigDig = this.areSignificantDigitsUsed();
        int maxIntDig = this.getMaximumIntegerDigits();
        int minIntDig = this.getMinimumIntegerDigits();
        if (fieldPosition.getField() == 0) {
            fieldPosition.setBeginIndex(result.length());
            fieldPosition.setEndIndex(-1);
        }
        else if (fieldPosition.getField() == 1) {
            fieldPosition.setBeginIndex(-1);
        }
        else if (fieldPosition.getFieldAttribute() == Field.INTEGER) {
            fieldPosition.setBeginIndex(result.length());
            fieldPosition.setEndIndex(-1);
        }
        else if (fieldPosition.getFieldAttribute() == Field.FRACTION) {
            fieldPosition.setBeginIndex(-1);
        }
        final int intBegin = result.length();
        int intEnd = -1;
        int fracBegin = -1;
        int minFracDig = 0;
        if (useSigDig) {
            minIntDig = (maxIntDig = 1);
            minFracDig = this.getMinimumSignificantDigits() - 1;
        }
        else {
            minFracDig = this.getMinimumFractionDigits();
            if (maxIntDig > 8) {
                maxIntDig = 1;
                if (maxIntDig < minIntDig) {
                    maxIntDig = minIntDig;
                }
            }
            if (maxIntDig > minIntDig) {
                minIntDig = 1;
            }
        }
        int exponent = this.digitList.decimalAt;
        if (maxIntDig > 1 && maxIntDig != minIntDig) {
            exponent = ((exponent > 0) ? ((exponent - 1) / maxIntDig) : (exponent / maxIntDig - 1));
            exponent *= maxIntDig;
        }
        else {
            exponent -= ((minIntDig > 0 || minFracDig > 0) ? minIntDig : 1);
        }
        final int minimumDigits = minIntDig + minFracDig;
        final int integerDigits = this.digitList.isZero() ? minIntDig : (this.digitList.decimalAt - exponent);
        int totalDigits = this.digitList.count;
        if (minimumDigits > totalDigits) {
            totalDigits = minimumDigits;
        }
        if (integerDigits > totalDigits) {
            totalDigits = integerDigits;
        }
        for (int i = 0; i < totalDigits; ++i) {
            if (i == integerDigits) {
                if (fieldPosition.getField() == 0) {
                    fieldPosition.setEndIndex(result.length());
                }
                else if (fieldPosition.getFieldAttribute() == Field.INTEGER) {
                    fieldPosition.setEndIndex(result.length());
                }
                if (parseAttr) {
                    intEnd = result.length();
                    this.addAttribute(Field.INTEGER, intBegin, result.length());
                }
                result.append(decimal);
                if (parseAttr) {
                    final int decimalSeparatorBegin = result.length() - 1;
                    this.addAttribute(Field.DECIMAL_SEPARATOR, decimalSeparatorBegin, result.length());
                    fracBegin = result.length();
                }
                if (fieldPosition.getField() == 1) {
                    fieldPosition.setBeginIndex(result.length());
                }
                else if (fieldPosition.getFieldAttribute() == Field.FRACTION) {
                    fieldPosition.setBeginIndex(result.length());
                }
            }
            result.append((i < this.digitList.count) ? digits[this.digitList.getDigitValue(i)] : digits[0]);
        }
        if (this.digitList.isZero() && totalDigits == 0) {
            result.append(digits[0]);
        }
        if (fieldPosition.getField() == 0) {
            if (fieldPosition.getEndIndex() < 0) {
                fieldPosition.setEndIndex(result.length());
            }
        }
        else if (fieldPosition.getField() == 1) {
            if (fieldPosition.getBeginIndex() < 0) {
                fieldPosition.setBeginIndex(result.length());
            }
            fieldPosition.setEndIndex(result.length());
        }
        else if (fieldPosition.getFieldAttribute() == Field.INTEGER) {
            if (fieldPosition.getEndIndex() < 0) {
                fieldPosition.setEndIndex(result.length());
            }
        }
        else if (fieldPosition.getFieldAttribute() == Field.FRACTION) {
            if (fieldPosition.getBeginIndex() < 0) {
                fieldPosition.setBeginIndex(result.length());
            }
            fieldPosition.setEndIndex(result.length());
        }
        if (parseAttr) {
            if (intEnd < 0) {
                this.addAttribute(Field.INTEGER, intBegin, result.length());
            }
            if (fracBegin > 0) {
                this.addAttribute(Field.FRACTION, fracBegin, result.length());
            }
        }
        result.append(this.symbols.getExponentSeparator());
        if (parseAttr) {
            this.addAttribute(Field.EXPONENT_SYMBOL, result.length() - this.symbols.getExponentSeparator().length(), result.length());
        }
        if (this.digitList.isZero()) {
            exponent = 0;
        }
        final boolean negativeExponent = exponent < 0;
        if (negativeExponent) {
            exponent = -exponent;
            result.append(this.symbols.getMinusSign());
            if (parseAttr) {
                this.addAttribute(Field.EXPONENT_SIGN, result.length() - 1, result.length());
            }
        }
        else if (this.exponentSignAlwaysShown) {
            result.append(this.symbols.getPlusSign());
            if (parseAttr) {
                final int expSignBegin = result.length() - 1;
                this.addAttribute(Field.EXPONENT_SIGN, expSignBegin, result.length());
            }
        }
        final int expBegin = result.length();
        this.digitList.set(exponent);
        int expDig = this.minExponentDigits;
        if (this.useExponentialNotation && expDig < 1) {
            expDig = 1;
        }
        for (int i = this.digitList.decimalAt; i < expDig; ++i) {
            result.append(digits[0]);
        }
        for (int i = 0; i < this.digitList.decimalAt; ++i) {
            result.append((i < this.digitList.count) ? digits[this.digitList.getDigitValue(i)] : digits[0]);
        }
        if (parseAttr) {
            this.addAttribute(Field.EXPONENT, expBegin, result.length());
        }
    }
    
    private final void addPadding(final StringBuffer result, final FieldPosition fieldPosition, final int prefixLen, final int suffixLen) {
        if (this.formatWidth > 0) {
            final int len = this.formatWidth - result.length();
            if (len > 0) {
                final char[] padding = new char[len];
                for (int i = 0; i < len; ++i) {
                    padding[i] = this.pad;
                }
                switch (this.padPosition) {
                    case 1: {
                        result.insert(prefixLen, padding);
                        break;
                    }
                    case 0: {
                        result.insert(0, padding);
                        break;
                    }
                    case 2: {
                        result.insert(result.length() - suffixLen, padding);
                        break;
                    }
                    case 3: {
                        result.append(padding);
                        break;
                    }
                }
                if (this.padPosition == 0 || this.padPosition == 1) {
                    fieldPosition.setBeginIndex(fieldPosition.getBeginIndex() + len);
                    fieldPosition.setEndIndex(fieldPosition.getEndIndex() + len);
                }
            }
        }
    }
    
    @Override
    public Number parse(final String text, final ParsePosition parsePosition) {
        return (Number)this.parse(text, parsePosition, null);
    }
    
    @Override
    public CurrencyAmount parseCurrency(final CharSequence text, final ParsePosition pos) {
        final Currency[] currency = { null };
        return (CurrencyAmount)this.parse(text.toString(), pos, currency);
    }
    
    private Object parse(final String text, final ParsePosition parsePosition, final Currency[] currency) {
        int i;
        final int backup = i = parsePosition.getIndex();
        if (this.formatWidth > 0 && (this.padPosition == 0 || this.padPosition == 1)) {
            i = this.skipPadding(text, i);
        }
        if (text.regionMatches(i, this.symbols.getNaN(), 0, this.symbols.getNaN().length())) {
            i += this.symbols.getNaN().length();
            if (this.formatWidth > 0 && (this.padPosition == 2 || this.padPosition == 3)) {
                i = this.skipPadding(text, i);
            }
            parsePosition.setIndex(i);
            return new Double(Double.NaN);
        }
        i = backup;
        final boolean[] status = new boolean[3];
        if (this.currencySignCount > 0) {
            if (!this.parseForCurrency(text, parsePosition, currency, status)) {
                return null;
            }
        }
        else if (!this.subparse(text, parsePosition, this.digitList, status, currency, this.negPrefixPattern, this.negSuffixPattern, this.posPrefixPattern, this.posSuffixPattern, 0)) {
            parsePosition.setIndex(backup);
            return null;
        }
        Number n = null;
        if (status[0]) {
            n = new Double(status[1] ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY);
        }
        else if (status[2]) {
            n = (status[1] ? new Double("0.0") : new Double("-0.0"));
        }
        else if (!status[1] && this.digitList.isZero()) {
            n = new Double("-0.0");
        }
        else {
            int mult;
            for (mult = this.multiplier; mult % 10 == 0; mult /= 10) {
                final DigitList digitList = this.digitList;
                --digitList.decimalAt;
            }
            if (!this.parseBigDecimal && mult == 1 && this.digitList.isIntegral()) {
                if (this.digitList.decimalAt < 12) {
                    long l = 0L;
                    if (this.digitList.count > 0) {
                        int nx;
                        for (nx = 0; nx < this.digitList.count; l = l * 10L + (char)this.digitList.digits[nx++] - 48L) {}
                        while (nx++ < this.digitList.decimalAt) {
                            l *= 10L;
                        }
                        if (!status[1]) {
                            l = -l;
                        }
                    }
                    n = l;
                }
                else {
                    final BigInteger big = this.digitList.getBigInteger(status[1]);
                    n = ((big.bitLength() < 64) ? Long.valueOf(big.longValue()) : big);
                }
            }
            else {
                final com.ibm.icu.math.BigDecimal big2 = (com.ibm.icu.math.BigDecimal)(n = this.digitList.getBigDecimalICU(status[1]));
                if (mult != 1) {
                    n = big2.divide(com.ibm.icu.math.BigDecimal.valueOf(mult), this.mathContext);
                }
            }
        }
        return (currency != null) ? new CurrencyAmount(n, currency[0]) : n;
    }
    
    private boolean parseForCurrency(final String text, final ParsePosition parsePosition, final Currency[] currency, final boolean[] status) {
        final int origPos = parsePosition.getIndex();
        if (!this.isReadyForParsing) {
            final int savedCurrencySignCount = this.currencySignCount;
            this.setupCurrencyAffixForAllPatterns();
            if (savedCurrencySignCount == 3) {
                this.applyPatternWithoutExpandAffix(this.formatPattern, false);
            }
            else {
                this.applyPattern(this.formatPattern, false);
            }
            this.isReadyForParsing = true;
        }
        int maxPosIndex = origPos;
        int maxErrorPos = -1;
        boolean[] savedStatus = null;
        boolean[] tmpStatus = new boolean[3];
        ParsePosition tmpPos = new ParsePosition(origPos);
        DigitList tmpDigitList = new DigitList();
        boolean found;
        if (this.style == 6) {
            found = this.subparse(text, tmpPos, tmpDigitList, tmpStatus, currency, this.negPrefixPattern, this.negSuffixPattern, this.posPrefixPattern, this.posSuffixPattern, 1);
        }
        else {
            found = this.subparse(text, tmpPos, tmpDigitList, tmpStatus, currency, this.negPrefixPattern, this.negSuffixPattern, this.posPrefixPattern, this.posSuffixPattern, 0);
        }
        if (found) {
            if (tmpPos.getIndex() > maxPosIndex) {
                maxPosIndex = tmpPos.getIndex();
                savedStatus = tmpStatus;
                this.digitList = tmpDigitList;
            }
        }
        else {
            maxErrorPos = tmpPos.getErrorIndex();
        }
        for (final AffixForCurrency affix : this.affixPatternsForCurrency) {
            tmpStatus = new boolean[3];
            tmpPos = new ParsePosition(origPos);
            tmpDigitList = new DigitList();
            final boolean result = this.subparse(text, tmpPos, tmpDigitList, tmpStatus, currency, affix.getNegPrefix(), affix.getNegSuffix(), affix.getPosPrefix(), affix.getPosSuffix(), affix.getPatternType());
            if (result) {
                found = true;
                if (tmpPos.getIndex() <= maxPosIndex) {
                    continue;
                }
                maxPosIndex = tmpPos.getIndex();
                savedStatus = tmpStatus;
                this.digitList = tmpDigitList;
            }
            else {
                maxErrorPos = ((tmpPos.getErrorIndex() > maxErrorPos) ? tmpPos.getErrorIndex() : maxErrorPos);
            }
        }
        tmpStatus = new boolean[3];
        tmpPos = new ParsePosition(origPos);
        tmpDigitList = new DigitList();
        final int savedCurrencySignCount2 = this.currencySignCount;
        this.currencySignCount = -1;
        final boolean result2 = this.subparse(text, tmpPos, tmpDigitList, tmpStatus, currency, this.negativePrefix, this.negativeSuffix, this.positivePrefix, this.positiveSuffix, 0);
        this.currencySignCount = savedCurrencySignCount2;
        if (result2) {
            if (tmpPos.getIndex() > maxPosIndex) {
                maxPosIndex = tmpPos.getIndex();
                savedStatus = tmpStatus;
                this.digitList = tmpDigitList;
            }
            found = true;
        }
        else {
            maxErrorPos = ((tmpPos.getErrorIndex() > maxErrorPos) ? tmpPos.getErrorIndex() : maxErrorPos);
        }
        if (!found) {
            parsePosition.setErrorIndex(maxErrorPos);
        }
        else {
            parsePosition.setIndex(maxPosIndex);
            parsePosition.setErrorIndex(-1);
            for (int index = 0; index < 3; ++index) {
                status[index] = savedStatus[index];
            }
        }
        return found;
    }
    
    private void setupCurrencyAffixForAllPatterns() {
        if (this.currencyPluralInfo == null) {
            this.currencyPluralInfo = new CurrencyPluralInfo(this.symbols.getULocale());
        }
        this.affixPatternsForCurrency = new HashSet<AffixForCurrency>();
        final String savedFormatPattern = this.formatPattern;
        this.applyPatternWithoutExpandAffix(NumberFormat.getPattern(this.symbols.getULocale(), 1), false);
        AffixForCurrency affixes = new AffixForCurrency(this.negPrefixPattern, this.negSuffixPattern, this.posPrefixPattern, this.posSuffixPattern, 0);
        this.affixPatternsForCurrency.add(affixes);
        final Iterator<String> iter = this.currencyPluralInfo.pluralPatternIterator();
        final Set<String> currencyUnitPatternSet = new HashSet<String>();
        while (iter.hasNext()) {
            final String pluralCount = iter.next();
            final String currencyPattern = this.currencyPluralInfo.getCurrencyPluralPattern(pluralCount);
            if (currencyPattern != null && !currencyUnitPatternSet.contains(currencyPattern)) {
                currencyUnitPatternSet.add(currencyPattern);
                this.applyPatternWithoutExpandAffix(currencyPattern, false);
                affixes = new AffixForCurrency(this.negPrefixPattern, this.negSuffixPattern, this.posPrefixPattern, this.posSuffixPattern, 1);
                this.affixPatternsForCurrency.add(affixes);
            }
        }
        this.formatPattern = savedFormatPattern;
    }
    
    private final boolean subparse(final String text, final ParsePosition parsePosition, final DigitList digits, final boolean[] status, final Currency[] currency, final String negPrefix, final String negSuffix, final String posPrefix, final String posSuffix, final int type) {
        int position = parsePosition.getIndex();
        final int oldStart = parsePosition.getIndex();
        if (this.formatWidth > 0 && this.padPosition == 0) {
            position = this.skipPadding(text, position);
        }
        int posMatch = this.compareAffix(text, position, false, true, posPrefix, type, currency);
        int negMatch = this.compareAffix(text, position, true, true, negPrefix, type, currency);
        if (posMatch >= 0 && negMatch >= 0) {
            if (posMatch > negMatch) {
                negMatch = -1;
            }
            else if (negMatch > posMatch) {
                posMatch = -1;
            }
        }
        if (posMatch >= 0) {
            position += posMatch;
        }
        else {
            if (negMatch < 0) {
                parsePosition.setErrorIndex(position);
                return false;
            }
            position += negMatch;
        }
        if (this.formatWidth > 0 && this.padPosition == 1) {
            position = this.skipPadding(text, position);
        }
        status[0] = false;
        if (text.regionMatches(position, this.symbols.getInfinity(), 0, this.symbols.getInfinity().length())) {
            position += this.symbols.getInfinity().length();
            status[0] = true;
        }
        else {
            final int n = 0;
            digits.count = n;
            digits.decimalAt = n;
            final char[] digitSymbols = this.symbols.getDigitsLocal();
            char decimal = (this.currencySignCount == 0) ? this.symbols.getDecimalSeparator() : this.symbols.getMonetaryDecimalSeparator();
            char grouping = this.symbols.getGroupingSeparator();
            final String exponentSep = this.symbols.getExponentSeparator();
            boolean sawDecimal = false;
            boolean sawGrouping = false;
            boolean sawExponent = false;
            boolean sawDigit = false;
            long exponent = 0L;
            int digit = 0;
            final boolean strictParse = this.isParseStrict();
            boolean strictFail = false;
            int lastGroup = -1;
            final int digitStart = position;
            final int gs2 = (this.groupingSize2 == 0) ? this.groupingSize : this.groupingSize2;
            final boolean skipExtendedSeparatorParsing = ICUConfig.get("com.ibm.icu.text.DecimalFormat.SkipExtendedSeparatorParsing", "false").equals("true");
            final UnicodeSet decimalEquiv = skipExtendedSeparatorParsing ? UnicodeSet.EMPTY : this.getEquivalentDecimals(decimal, strictParse);
            final UnicodeSet groupEquiv = skipExtendedSeparatorParsing ? UnicodeSet.EMPTY : (strictParse ? DecimalFormat.strictDefaultGroupingSeparators : DecimalFormat.defaultGroupingSeparators);
            int digitCount = 0;
            int backup = -1;
            while (position < text.length()) {
                int ch = UTF16.charAt(text, position);
                digit = ch - digitSymbols[0];
                if (digit < 0 || digit > 9) {
                    digit = UCharacter.digit(ch, 10);
                }
                if (digit < 0 || digit > 9) {
                    for (digit = 0; digit < 10; ++digit) {
                        if (ch == digitSymbols[digit]) {
                            break;
                        }
                    }
                }
                if (digit == 0) {
                    if (strictParse && backup != -1) {
                        if ((lastGroup != -1 && this.countCodePoints(text, lastGroup, backup) - 1 != gs2) || (lastGroup == -1 && this.countCodePoints(text, digitStart, position) - 1 > gs2)) {
                            strictFail = true;
                            break;
                        }
                        lastGroup = backup;
                    }
                    backup = -1;
                    sawDigit = true;
                    if (digits.count == 0) {
                        if (sawDecimal) {
                            --digits.decimalAt;
                        }
                    }
                    else {
                        ++digitCount;
                        digits.append((char)(digit + 48));
                    }
                }
                else if (digit > 0 && digit <= 9) {
                    if (strictParse && backup != -1) {
                        if ((lastGroup != -1 && this.countCodePoints(text, lastGroup, backup) - 1 != gs2) || (lastGroup == -1 && this.countCodePoints(text, digitStart, position) - 1 > gs2)) {
                            strictFail = true;
                            break;
                        }
                        lastGroup = backup;
                    }
                    sawDigit = true;
                    ++digitCount;
                    digits.append((char)(digit + 48));
                    backup = -1;
                }
                else if (ch == decimal) {
                    if (strictParse && (backup != -1 || (lastGroup != -1 && this.countCodePoints(text, lastGroup, position) != this.groupingSize + 1))) {
                        strictFail = true;
                        break;
                    }
                    if (this.isParseIntegerOnly()) {
                        break;
                    }
                    if (sawDecimal) {
                        break;
                    }
                    digits.decimalAt = digitCount;
                    sawDecimal = true;
                }
                else if (this.isGroupingUsed() && ch == grouping) {
                    if (sawDecimal) {
                        break;
                    }
                    if (strictParse && (!sawDigit || backup != -1)) {
                        strictFail = true;
                        break;
                    }
                    backup = position;
                    sawGrouping = true;
                }
                else if (!sawDecimal && decimalEquiv.contains(ch)) {
                    if (strictParse && (backup != -1 || (lastGroup != -1 && this.countCodePoints(text, lastGroup, position) != this.groupingSize + 1))) {
                        strictFail = true;
                        break;
                    }
                    if (this.isParseIntegerOnly()) {
                        break;
                    }
                    digits.decimalAt = digitCount;
                    decimal = (char)ch;
                    sawDecimal = true;
                }
                else if (this.isGroupingUsed() && !sawGrouping && groupEquiv.contains(ch)) {
                    if (sawDecimal) {
                        break;
                    }
                    if (strictParse && (!sawDigit || backup != -1)) {
                        strictFail = true;
                        break;
                    }
                    grouping = (char)ch;
                    backup = position;
                    sawGrouping = true;
                }
                else {
                    if (sawExponent || !text.regionMatches(true, position, exponentSep, 0, exponentSep.length())) {
                        break;
                    }
                    boolean negExp = false;
                    int pos = position + exponentSep.length();
                    if (pos < text.length()) {
                        ch = UTF16.charAt(text, pos);
                        if (ch == this.symbols.getPlusSign()) {
                            ++pos;
                        }
                        else if (ch == this.symbols.getMinusSign()) {
                            ++pos;
                            negExp = true;
                        }
                    }
                    final DigitList exponentDigits = new DigitList();
                    exponentDigits.count = 0;
                    while (pos < text.length()) {
                        digit = UTF16.charAt(text, pos) - digitSymbols[0];
                        if (digit < 0 || digit > 9) {
                            digit = UCharacter.digit(UTF16.charAt(text, pos), 10);
                        }
                        if (digit < 0 || digit > 9) {
                            break;
                        }
                        exponentDigits.append((char)(digit + 48));
                        pos += UTF16.getCharCount(UTF16.charAt(text, pos));
                    }
                    if (exponentDigits.count <= 0) {
                        break;
                    }
                    if (strictParse && (backup != -1 || lastGroup != -1)) {
                        strictFail = true;
                        break;
                    }
                    if (exponentDigits.count > 10) {
                        if (negExp) {
                            status[2] = true;
                        }
                        else {
                            status[0] = true;
                        }
                    }
                    else {
                        exponentDigits.decimalAt = exponentDigits.count;
                        exponent = exponentDigits.getLong();
                        if (negExp) {
                            exponent = -exponent;
                        }
                    }
                    position = pos;
                    sawExponent = true;
                    break;
                }
                position += UTF16.getCharCount(ch);
            }
            if (backup != -1) {
                position = backup;
            }
            if (!sawDecimal) {
                digits.decimalAt = digitCount;
            }
            if (strictParse && !sawDecimal && lastGroup != -1 && this.countCodePoints(text, lastGroup, position) != this.groupingSize + 1) {
                strictFail = true;
            }
            if (strictFail) {
                parsePosition.setIndex(oldStart);
                parsePosition.setErrorIndex(position);
                return false;
            }
            exponent += digits.decimalAt;
            if (exponent < -this.getParseMaxDigits()) {
                status[2] = true;
            }
            else if (exponent > this.getParseMaxDigits()) {
                status[0] = true;
            }
            else {
                digits.decimalAt = (int)exponent;
            }
            if (!sawDigit && digitCount == 0) {
                parsePosition.setIndex(oldStart);
                parsePosition.setErrorIndex(oldStart);
                return false;
            }
        }
        if (this.formatWidth > 0 && this.padPosition == 2) {
            position = this.skipPadding(text, position);
        }
        if (posMatch >= 0) {
            posMatch = this.compareAffix(text, position, false, false, posSuffix, type, currency);
        }
        if (negMatch >= 0) {
            negMatch = this.compareAffix(text, position, true, false, negSuffix, type, currency);
        }
        if (posMatch >= 0 && negMatch >= 0) {
            if (posMatch > negMatch) {
                negMatch = -1;
            }
            else if (negMatch > posMatch) {
                posMatch = -1;
            }
        }
        if (posMatch >= 0 == negMatch >= 0) {
            parsePosition.setErrorIndex(position);
            return false;
        }
        position += ((posMatch >= 0) ? posMatch : negMatch);
        if (this.formatWidth > 0 && this.padPosition == 3) {
            position = this.skipPadding(text, position);
        }
        parsePosition.setIndex(position);
        status[1] = (posMatch >= 0);
        if (parsePosition.getIndex() == oldStart) {
            parsePosition.setErrorIndex(position);
            return false;
        }
        return true;
    }
    
    private int countCodePoints(final String str, final int start, final int end) {
        int count = 0;
        for (int index = start; index < end; index += UTF16.getCharCount(UTF16.charAt(str, index))) {
            ++count;
        }
        return count;
    }
    
    private UnicodeSet getEquivalentDecimals(final char decimal, final boolean strictParse) {
        UnicodeSet equivSet = UnicodeSet.EMPTY;
        if (strictParse) {
            if (DecimalFormat.strictDotEquivalents.contains(decimal)) {
                equivSet = DecimalFormat.strictDotEquivalents;
            }
            else if (DecimalFormat.strictCommaEquivalents.contains(decimal)) {
                equivSet = DecimalFormat.strictCommaEquivalents;
            }
        }
        else if (DecimalFormat.dotEquivalents.contains(decimal)) {
            equivSet = DecimalFormat.dotEquivalents;
        }
        else if (DecimalFormat.commaEquivalents.contains(decimal)) {
            equivSet = DecimalFormat.commaEquivalents;
        }
        return equivSet;
    }
    
    private final int skipPadding(final String text, int position) {
        while (position < text.length() && text.charAt(position) == this.pad) {
            ++position;
        }
        return position;
    }
    
    private int compareAffix(final String text, final int pos, final boolean isNegative, final boolean isPrefix, final String affixPat, final int type, final Currency[] currency) {
        if (currency != null || this.currencyChoice != null || this.currencySignCount > 0) {
            return this.compareComplexAffix(affixPat, text, pos, type, currency);
        }
        if (isPrefix) {
            return compareSimpleAffix(isNegative ? this.negativePrefix : this.positivePrefix, text, pos);
        }
        return compareSimpleAffix(isNegative ? this.negativeSuffix : this.positiveSuffix, text, pos);
    }
    
    private static int compareSimpleAffix(final String affix, final String input, int pos) {
        final int start = pos;
        int i = 0;
        while (i < affix.length()) {
            int c = UTF16.charAt(affix, i);
            int len = UTF16.getCharCount(c);
            if (PatternProps.isWhiteSpace(c)) {
                boolean literalMatch = false;
                while (pos < input.length() && UTF16.charAt(input, pos) == c) {
                    literalMatch = true;
                    i += len;
                    pos += len;
                    if (i == affix.length()) {
                        break;
                    }
                    c = UTF16.charAt(affix, i);
                    len = UTF16.getCharCount(c);
                    if (!PatternProps.isWhiteSpace(c)) {
                        break;
                    }
                }
                i = skipPatternWhiteSpace(affix, i);
                final int s = pos;
                pos = skipUWhiteSpace(input, pos);
                if (pos == s && !literalMatch) {
                    return -1;
                }
                i = skipUWhiteSpace(affix, i);
            }
            else {
                if (pos >= input.length() || UTF16.charAt(input, pos) != c) {
                    return -1;
                }
                i += len;
                pos += len;
            }
        }
        return pos - start;
    }
    
    private static int skipPatternWhiteSpace(final String text, int pos) {
        while (pos < text.length()) {
            final int c = UTF16.charAt(text, pos);
            if (!PatternProps.isWhiteSpace(c)) {
                break;
            }
            pos += UTF16.getCharCount(c);
        }
        return pos;
    }
    
    private static int skipUWhiteSpace(final String text, int pos) {
        while (pos < text.length()) {
            final int c = UTF16.charAt(text, pos);
            if (!UCharacter.isUWhiteSpace(c)) {
                break;
            }
            pos += UTF16.getCharCount(c);
        }
        return pos;
    }
    
    private int compareComplexAffix(final String affixPat, final String text, int pos, final int type, final Currency[] currency) {
        final int start = pos;
        int i = 0;
    Label_0006:
        while (i < affixPat.length() && pos >= 0) {
            char c = affixPat.charAt(i++);
            if (c == '\'') {
                int j;
                while (true) {
                    j = affixPat.indexOf(39, i);
                    if (j == i) {
                        break;
                    }
                    if (j <= i) {
                        throw new RuntimeException();
                    }
                    pos = match(text, pos, affixPat.substring(i, j));
                    i = j + 1;
                    if (i >= affixPat.length() || affixPat.charAt(i) != '\'') {
                        continue Label_0006;
                    }
                    pos = match(text, pos, 39);
                    ++i;
                }
                pos = match(text, pos, 39);
                i = j + 1;
                continue;
            }
            switch (c) {
                case '¤': {
                    boolean intl = i < affixPat.length() && affixPat.charAt(i) == '¤';
                    if (intl) {
                        ++i;
                    }
                    final boolean plural = i < affixPat.length() && affixPat.charAt(i) == '¤';
                    if (plural) {
                        ++i;
                        intl = false;
                    }
                    ULocale uloc = this.getLocale(ULocale.VALID_LOCALE);
                    if (uloc == null) {
                        uloc = this.symbols.getLocale(ULocale.VALID_LOCALE);
                    }
                    final ParsePosition ppos = new ParsePosition(pos);
                    final String iso = Currency.parse(uloc, text, type, ppos);
                    if (iso != null) {
                        if (currency != null) {
                            currency[0] = Currency.getInstance(iso);
                        }
                        else {
                            final Currency effectiveCurr = this.getEffectiveCurrency();
                            if (iso.compareTo(effectiveCurr.getCurrencyCode()) != 0) {
                                pos = -1;
                                continue;
                            }
                        }
                        pos = ppos.getIndex();
                        continue;
                    }
                    pos = -1;
                    continue;
                }
                case '%': {
                    c = this.symbols.getPercent();
                    break;
                }
                case '\u2030': {
                    c = this.symbols.getPerMill();
                    break;
                }
                case '-': {
                    c = this.symbols.getMinusSign();
                    break;
                }
            }
            pos = match(text, pos, c);
            if (!PatternProps.isWhiteSpace(c)) {
                continue;
            }
            i = skipPatternWhiteSpace(affixPat, i);
        }
        return pos - start;
    }
    
    static final int match(final String text, int pos, final int ch) {
        if (pos >= text.length()) {
            return -1;
        }
        if (!PatternProps.isWhiteSpace(ch)) {
            return (pos >= 0 && UTF16.charAt(text, pos) == ch) ? (pos + UTF16.getCharCount(ch)) : -1;
        }
        final int s = pos;
        pos = skipPatternWhiteSpace(text, pos);
        if (pos == s) {
            return -1;
        }
        return pos;
    }
    
    static final int match(final String text, int pos, final String str) {
        for (int i = 0; i < str.length() && pos >= 0; i = skipPatternWhiteSpace(str, i)) {
            final int ch = UTF16.charAt(str, i);
            i += UTF16.getCharCount(ch);
            pos = match(text, pos, ch);
            if (PatternProps.isWhiteSpace(ch)) {}
        }
        return pos;
    }
    
    public DecimalFormatSymbols getDecimalFormatSymbols() {
        try {
            return (DecimalFormatSymbols)this.symbols.clone();
        }
        catch (Exception foo) {
            return null;
        }
    }
    
    public void setDecimalFormatSymbols(final DecimalFormatSymbols newSymbols) {
        this.symbols = (DecimalFormatSymbols)newSymbols.clone();
        this.setCurrencyForSymbols();
        this.expandAffixes(null);
    }
    
    private void setCurrencyForSymbols() {
        final DecimalFormatSymbols def = new DecimalFormatSymbols(this.symbols.getULocale());
        if (this.symbols.getCurrencySymbol().equals(def.getCurrencySymbol()) && this.symbols.getInternationalCurrencySymbol().equals(def.getInternationalCurrencySymbol())) {
            this.setCurrency(Currency.getInstance(this.symbols.getULocale()));
        }
        else {
            this.setCurrency(null);
        }
    }
    
    public String getPositivePrefix() {
        return this.positivePrefix;
    }
    
    public void setPositivePrefix(final String newValue) {
        this.positivePrefix = newValue;
        this.posPrefixPattern = null;
    }
    
    public String getNegativePrefix() {
        return this.negativePrefix;
    }
    
    public void setNegativePrefix(final String newValue) {
        this.negativePrefix = newValue;
        this.negPrefixPattern = null;
    }
    
    public String getPositiveSuffix() {
        return this.positiveSuffix;
    }
    
    public void setPositiveSuffix(final String newValue) {
        this.positiveSuffix = newValue;
        this.posSuffixPattern = null;
    }
    
    public String getNegativeSuffix() {
        return this.negativeSuffix;
    }
    
    public void setNegativeSuffix(final String newValue) {
        this.negativeSuffix = newValue;
        this.negSuffixPattern = null;
    }
    
    public int getMultiplier() {
        return this.multiplier;
    }
    
    public void setMultiplier(final int newValue) {
        if (newValue == 0) {
            throw new IllegalArgumentException("Bad multiplier: " + newValue);
        }
        this.multiplier = newValue;
    }
    
    public BigDecimal getRoundingIncrement() {
        if (this.roundingIncrementICU == null) {
            return null;
        }
        return this.roundingIncrementICU.toBigDecimal();
    }
    
    public void setRoundingIncrement(final BigDecimal newValue) {
        if (newValue == null) {
            this.setRoundingIncrement((com.ibm.icu.math.BigDecimal)null);
        }
        else {
            this.setRoundingIncrement(new com.ibm.icu.math.BigDecimal(newValue));
        }
    }
    
    public void setRoundingIncrement(final com.ibm.icu.math.BigDecimal newValue) {
        final int i = (newValue == null) ? 0 : newValue.compareTo(com.ibm.icu.math.BigDecimal.ZERO);
        if (i < 0) {
            throw new IllegalArgumentException("Illegal rounding increment");
        }
        if (i == 0) {
            this.setInternalRoundingIncrement(null);
        }
        else {
            this.setInternalRoundingIncrement(newValue);
        }
        this.setRoundingDouble();
    }
    
    public void setRoundingIncrement(final double newValue) {
        if (newValue < 0.0) {
            throw new IllegalArgumentException("Illegal rounding increment");
        }
        this.roundingDouble = newValue;
        this.roundingDoubleReciprocal = 0.0;
        if (newValue == 0.0) {
            this.setRoundingIncrement((com.ibm.icu.math.BigDecimal)null);
        }
        else {
            this.roundingDouble = newValue;
            if (this.roundingDouble < 1.0) {
                final double rawRoundedReciprocal = 1.0 / this.roundingDouble;
                this.setRoundingDoubleReciprocal(rawRoundedReciprocal);
            }
            this.setInternalRoundingIncrement(new com.ibm.icu.math.BigDecimal(newValue));
        }
    }
    
    private void setRoundingDoubleReciprocal(final double rawRoundedReciprocal) {
        this.roundingDoubleReciprocal = Math.rint(rawRoundedReciprocal);
        if (Math.abs(rawRoundedReciprocal - this.roundingDoubleReciprocal) > 1.0E-9) {
            this.roundingDoubleReciprocal = 0.0;
        }
    }
    
    @Override
    public int getRoundingMode() {
        return this.roundingMode;
    }
    
    @Override
    public void setRoundingMode(final int roundingMode) {
        if (roundingMode < 0 || roundingMode > 7) {
            throw new IllegalArgumentException("Invalid rounding mode: " + roundingMode);
        }
        this.roundingMode = roundingMode;
        if (this.getRoundingIncrement() == null) {
            this.setRoundingIncrement(Math.pow(10.0, -this.getMaximumFractionDigits()));
        }
    }
    
    public int getFormatWidth() {
        return this.formatWidth;
    }
    
    public void setFormatWidth(final int width) {
        if (width < 0) {
            throw new IllegalArgumentException("Illegal format width");
        }
        this.formatWidth = width;
    }
    
    public char getPadCharacter() {
        return this.pad;
    }
    
    public void setPadCharacter(final char padChar) {
        this.pad = padChar;
    }
    
    public int getPadPosition() {
        return this.padPosition;
    }
    
    public void setPadPosition(final int padPos) {
        if (padPos < 0 || padPos > 3) {
            throw new IllegalArgumentException("Illegal pad position");
        }
        this.padPosition = padPos;
    }
    
    public boolean isScientificNotation() {
        return this.useExponentialNotation;
    }
    
    public void setScientificNotation(final boolean useScientific) {
        this.useExponentialNotation = useScientific;
    }
    
    public byte getMinimumExponentDigits() {
        return this.minExponentDigits;
    }
    
    public void setMinimumExponentDigits(final byte minExpDig) {
        if (minExpDig < 1) {
            throw new IllegalArgumentException("Exponent digits must be >= 1");
        }
        this.minExponentDigits = minExpDig;
    }
    
    public boolean isExponentSignAlwaysShown() {
        return this.exponentSignAlwaysShown;
    }
    
    public void setExponentSignAlwaysShown(final boolean expSignAlways) {
        this.exponentSignAlwaysShown = expSignAlways;
    }
    
    public int getGroupingSize() {
        return this.groupingSize;
    }
    
    public void setGroupingSize(final int newValue) {
        this.groupingSize = (byte)newValue;
    }
    
    public int getSecondaryGroupingSize() {
        return this.groupingSize2;
    }
    
    public void setSecondaryGroupingSize(final int newValue) {
        this.groupingSize2 = (byte)newValue;
    }
    
    public MathContext getMathContextICU() {
        return this.mathContext;
    }
    
    public java.math.MathContext getMathContext() {
        try {
            return (this.mathContext == null) ? null : new java.math.MathContext(this.mathContext.getDigits(), RoundingMode.valueOf(this.mathContext.getRoundingMode()));
        }
        catch (Exception foo) {
            return null;
        }
    }
    
    public void setMathContextICU(final MathContext newValue) {
        this.mathContext = newValue;
    }
    
    public void setMathContext(final java.math.MathContext newValue) {
        this.mathContext = new MathContext(newValue.getPrecision(), 1, false, newValue.getRoundingMode().ordinal());
    }
    
    public boolean isDecimalSeparatorAlwaysShown() {
        return this.decimalSeparatorAlwaysShown;
    }
    
    public void setDecimalSeparatorAlwaysShown(final boolean newValue) {
        this.decimalSeparatorAlwaysShown = newValue;
    }
    
    public CurrencyPluralInfo getCurrencyPluralInfo() {
        try {
            return (this.currencyPluralInfo == null) ? null : ((CurrencyPluralInfo)this.currencyPluralInfo.clone());
        }
        catch (Exception foo) {
            return null;
        }
    }
    
    public void setCurrencyPluralInfo(final CurrencyPluralInfo newInfo) {
        this.currencyPluralInfo = (CurrencyPluralInfo)newInfo.clone();
        this.isReadyForParsing = false;
    }
    
    @Override
    public Object clone() {
        try {
            final DecimalFormat other = (DecimalFormat)super.clone();
            other.symbols = (DecimalFormatSymbols)this.symbols.clone();
            other.digitList = new DigitList();
            if (this.currencyPluralInfo != null) {
                other.currencyPluralInfo = (CurrencyPluralInfo)this.currencyPluralInfo.clone();
            }
            other.attributes = new ArrayList<FieldPosition>();
            return other;
        }
        catch (Exception e) {
            throw new IllegalStateException();
        }
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final DecimalFormat other = (DecimalFormat)obj;
        return this.currencySignCount == other.currencySignCount && (this.style != 6 || (this.equals(this.posPrefixPattern, other.posPrefixPattern) && this.equals(this.posSuffixPattern, other.posSuffixPattern) && this.equals(this.negPrefixPattern, other.negPrefixPattern) && this.equals(this.negSuffixPattern, other.negSuffixPattern))) && this.multiplier == other.multiplier && this.groupingSize == other.groupingSize && this.groupingSize2 == other.groupingSize2 && this.decimalSeparatorAlwaysShown == other.decimalSeparatorAlwaysShown && this.useExponentialNotation == other.useExponentialNotation && (!this.useExponentialNotation || this.minExponentDigits == other.minExponentDigits) && this.useSignificantDigits == other.useSignificantDigits && (!this.useSignificantDigits || (this.minSignificantDigits == other.minSignificantDigits && this.maxSignificantDigits == other.maxSignificantDigits)) && this.symbols.equals(other.symbols) && Utility.objectEquals(this.currencyPluralInfo, other.currencyPluralInfo);
    }
    
    private boolean equals(final String pat1, final String pat2) {
        if (pat1 == null || pat2 == null) {
            return pat1 == null && pat2 == null;
        }
        return pat1.equals(pat2) || this.unquote(pat1).equals(this.unquote(pat2));
    }
    
    private String unquote(final String pat) {
        final StringBuilder buf = new StringBuilder(pat.length());
        int i = 0;
        while (i < pat.length()) {
            final char ch = pat.charAt(i++);
            if (ch != '\'') {
                buf.append(ch);
            }
        }
        return buf.toString();
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() * 37 + this.positivePrefix.hashCode();
    }
    
    public String toPattern() {
        if (this.style == 6) {
            return this.formatPattern;
        }
        return this.toPattern(false);
    }
    
    public String toLocalizedPattern() {
        if (this.style == 6) {
            return this.formatPattern;
        }
        return this.toPattern(true);
    }
    
    private void expandAffixes(final String pluralCount) {
        this.currencyChoice = null;
        final StringBuffer buffer = new StringBuffer();
        if (this.posPrefixPattern != null) {
            this.expandAffix(this.posPrefixPattern, pluralCount, buffer, false);
            this.positivePrefix = buffer.toString();
        }
        if (this.posSuffixPattern != null) {
            this.expandAffix(this.posSuffixPattern, pluralCount, buffer, false);
            this.positiveSuffix = buffer.toString();
        }
        if (this.negPrefixPattern != null) {
            this.expandAffix(this.negPrefixPattern, pluralCount, buffer, false);
            this.negativePrefix = buffer.toString();
        }
        if (this.negSuffixPattern != null) {
            this.expandAffix(this.negSuffixPattern, pluralCount, buffer, false);
            this.negativeSuffix = buffer.toString();
        }
    }
    
    private void expandAffix(final String pattern, final String pluralCount, final StringBuffer buffer, final boolean doFormat) {
        buffer.setLength(0);
        int i = 0;
    Label_0008:
        while (i < pattern.length()) {
            char c = pattern.charAt(i++);
            if (c == '\'') {
                int j;
                while (true) {
                    j = pattern.indexOf(39, i);
                    if (j == i) {
                        break;
                    }
                    if (j <= i) {
                        throw new RuntimeException();
                    }
                    buffer.append(pattern.substring(i, j));
                    i = j + 1;
                    if (i >= pattern.length() || pattern.charAt(i) != '\'') {
                        continue Label_0008;
                    }
                    buffer.append('\'');
                    ++i;
                }
                buffer.append('\'');
                i = j + 1;
                continue;
            }
            switch (c) {
                case '¤': {
                    boolean intl = i < pattern.length() && pattern.charAt(i) == '¤';
                    boolean plural = false;
                    if (intl && ++i < pattern.length() && pattern.charAt(i) == '¤') {
                        plural = true;
                        intl = false;
                        ++i;
                    }
                    String s = null;
                    final Currency currency = this.getCurrency();
                    if (currency != null) {
                        if (plural && pluralCount != null) {
                            final boolean[] isChoiceFormat = { false };
                            s = currency.getName(this.symbols.getULocale(), 2, pluralCount, isChoiceFormat);
                        }
                        else if (!intl) {
                            final boolean[] isChoiceFormat = { false };
                            s = currency.getName(this.symbols.getULocale(), 0, isChoiceFormat);
                            if (isChoiceFormat[0]) {
                                if (doFormat) {
                                    final FieldPosition pos = new FieldPosition(0);
                                    this.currencyChoice.format(this.digitList.getDouble(), buffer, pos);
                                    continue;
                                }
                                if (this.currencyChoice == null) {
                                    this.currencyChoice = new ChoiceFormat(s);
                                }
                                s = String.valueOf('¤');
                            }
                        }
                        else {
                            s = currency.getCurrencyCode();
                        }
                    }
                    else {
                        s = (intl ? this.symbols.getInternationalCurrencySymbol() : this.symbols.getCurrencySymbol());
                    }
                    buffer.append(s);
                    continue;
                }
                case '%': {
                    c = this.symbols.getPercent();
                    break;
                }
                case '\u2030': {
                    c = this.symbols.getPerMill();
                    break;
                }
                case '-': {
                    c = this.symbols.getMinusSign();
                    break;
                }
            }
            buffer.append(c);
        }
    }
    
    private int appendAffix(final StringBuffer buf, final boolean isNegative, final boolean isPrefix, final boolean parseAttr) {
        if (this.currencyChoice != null) {
            String affixPat = null;
            if (isPrefix) {
                affixPat = (isNegative ? this.negPrefixPattern : this.posPrefixPattern);
            }
            else {
                affixPat = (isNegative ? this.negSuffixPattern : this.posSuffixPattern);
            }
            final StringBuffer affixBuf = new StringBuffer();
            this.expandAffix(affixPat, null, affixBuf, true);
            buf.append(affixBuf);
            return affixBuf.length();
        }
        String affix = null;
        if (isPrefix) {
            affix = (isNegative ? this.negativePrefix : this.positivePrefix);
        }
        else {
            affix = (isNegative ? this.negativeSuffix : this.positiveSuffix);
        }
        if (parseAttr) {
            int offset = affix.indexOf(this.symbols.getCurrencySymbol());
            if (-1 == offset) {
                offset = affix.indexOf(this.symbols.getPercent());
                if (-1 == offset) {
                    offset = 0;
                }
            }
            this.formatAffix2Attribute(affix, buf.length() + offset, buf.length() + affix.length());
        }
        buf.append(affix);
        return affix.length();
    }
    
    private void formatAffix2Attribute(final String affix, final int begin, final int end) {
        if (affix.indexOf(this.symbols.getCurrencySymbol()) > -1) {
            this.addAttribute(Field.CURRENCY, begin, end);
        }
        else if (affix.indexOf(this.symbols.getMinusSign()) > -1) {
            this.addAttribute(Field.SIGN, begin, end);
        }
        else if (affix.indexOf(this.symbols.getPercent()) > -1) {
            this.addAttribute(Field.PERCENT, begin, end);
        }
        else if (affix.indexOf(this.symbols.getPerMill()) > -1) {
            this.addAttribute(Field.PERMILLE, begin, end);
        }
    }
    
    private void addAttribute(final Field field, final int begin, final int end) {
        final FieldPosition pos = new FieldPosition(field);
        pos.setBeginIndex(begin);
        pos.setEndIndex(end);
        this.attributes.add(pos);
    }
    
    @Override
    public AttributedCharacterIterator formatToCharacterIterator(final Object obj) {
        return this.formatToCharacterIterator(obj, DecimalFormat.NULL_UNIT);
    }
    
    AttributedCharacterIterator formatToCharacterIterator(final Object obj, final Unit unit) {
        if (!(obj instanceof Number)) {
            throw new IllegalArgumentException();
        }
        final Number number = (Number)obj;
        final StringBuffer text = new StringBuffer();
        unit.writePrefix(text);
        this.attributes.clear();
        if (obj instanceof BigInteger) {
            this.format((BigInteger)number, text, new FieldPosition(0), true);
        }
        else if (obj instanceof BigDecimal) {
            this.format((BigDecimal)number, text, new FieldPosition(0), true);
        }
        else if (obj instanceof Double) {
            this.format(number.doubleValue(), text, new FieldPosition(0), true);
        }
        else {
            if (!(obj instanceof Integer) && !(obj instanceof Long)) {
                throw new IllegalArgumentException();
            }
            this.format(number.longValue(), text, new FieldPosition(0), true);
        }
        unit.writeSuffix(text);
        final AttributedString as = new AttributedString(text.toString());
        for (int i = 0; i < this.attributes.size(); ++i) {
            final FieldPosition pos = this.attributes.get(i);
            final Format.Field attribute = pos.getFieldAttribute();
            as.addAttribute(attribute, attribute, pos.getBeginIndex(), pos.getEndIndex());
        }
        return as.getIterator();
    }
    
    private void appendAffixPattern(final StringBuffer buffer, final boolean isNegative, final boolean isPrefix, final boolean localized) {
        String affixPat = null;
        if (isPrefix) {
            affixPat = (isNegative ? this.negPrefixPattern : this.posPrefixPattern);
        }
        else {
            affixPat = (isNegative ? this.negSuffixPattern : this.posSuffixPattern);
        }
        if (affixPat == null) {
            String affix = null;
            if (isPrefix) {
                affix = (isNegative ? this.negativePrefix : this.positivePrefix);
            }
            else {
                affix = (isNegative ? this.negativeSuffix : this.positiveSuffix);
            }
            buffer.append('\'');
            for (int i = 0; i < affix.length(); ++i) {
                final char ch = affix.charAt(i);
                if (ch == '\'') {
                    buffer.append(ch);
                }
                buffer.append(ch);
            }
            buffer.append('\'');
            return;
        }
        if (!localized) {
            buffer.append(affixPat);
        }
        else {
            for (int j = 0; j < affixPat.length(); ++j) {
                char ch = affixPat.charAt(j);
                switch (ch) {
                    case '\'': {
                        final int k = affixPat.indexOf(39, j + 1);
                        if (k < 0) {
                            throw new IllegalArgumentException("Malformed affix pattern: " + affixPat);
                        }
                        buffer.append(affixPat.substring(j, k + 1));
                        j = k;
                        continue;
                    }
                    case '\u2030': {
                        ch = this.symbols.getPerMill();
                        break;
                    }
                    case '%': {
                        ch = this.symbols.getPercent();
                        break;
                    }
                    case '-': {
                        ch = this.symbols.getMinusSign();
                        break;
                    }
                }
                if (ch == this.symbols.getDecimalSeparator() || ch == this.symbols.getGroupingSeparator()) {
                    buffer.append('\'');
                    buffer.append(ch);
                    buffer.append('\'');
                }
                else {
                    buffer.append(ch);
                }
            }
        }
    }
    
    private String toPattern(final boolean localized) {
        final StringBuffer result = new StringBuffer();
        final char zero = localized ? this.symbols.getZeroDigit() : '0';
        final char digit = localized ? this.symbols.getDigit() : '#';
        char sigDigit = '\0';
        final boolean useSigDig = this.areSignificantDigitsUsed();
        if (useSigDig) {
            sigDigit = (localized ? this.symbols.getSignificantDigit() : '@');
        }
        final char group = localized ? this.symbols.getGroupingSeparator() : ',';
        int roundingDecimalPos = 0;
        String roundingDigits = null;
        final int padPos = (this.formatWidth > 0) ? this.padPosition : -1;
        final String padSpec = (this.formatWidth > 0) ? new StringBuffer(2).append(localized ? this.symbols.getPadEscape() : '*').append(this.pad).toString() : null;
        if (this.roundingIncrementICU != null) {
            final int i = this.roundingIncrementICU.scale();
            roundingDigits = this.roundingIncrementICU.movePointRight(i).toString();
            roundingDecimalPos = roundingDigits.length() - i;
        }
        for (int part = 0; part < 2; ++part) {
            if (padPos == 0) {
                result.append(padSpec);
            }
            this.appendAffixPattern(result, part != 0, true, localized);
            if (padPos == 1) {
                result.append(padSpec);
            }
            final int sub0Start = result.length();
            int g = this.isGroupingUsed() ? Math.max(0, this.groupingSize) : 0;
            if (g > 0 && this.groupingSize2 > 0 && this.groupingSize2 != this.groupingSize) {
                g += this.groupingSize2;
            }
            int maxDig = 0;
            int minDig = 0;
            int maxSigDig = 0;
            if (useSigDig) {
                minDig = this.getMinimumSignificantDigits();
                maxSigDig = (maxDig = this.getMaximumSignificantDigits());
            }
            else {
                minDig = this.getMinimumIntegerDigits();
                maxDig = this.getMaximumIntegerDigits();
            }
            if (this.useExponentialNotation) {
                if (maxDig > 8) {
                    maxDig = 1;
                }
            }
            else if (useSigDig) {
                maxDig = Math.max(maxDig, g + 1);
            }
            else {
                maxDig = Math.max(Math.max(g, this.getMinimumIntegerDigits()), roundingDecimalPos) + 1;
            }
            for (int i = maxDig; i > 0; --i) {
                if (!this.useExponentialNotation && i < maxDig && this.isGroupingPosition(i)) {
                    result.append(group);
                }
                if (useSigDig) {
                    result.append((maxSigDig >= i && i > maxSigDig - minDig) ? sigDigit : digit);
                }
                else {
                    if (roundingDigits != null) {
                        final int pos = roundingDecimalPos - i;
                        if (pos >= 0 && pos < roundingDigits.length()) {
                            result.append((char)(roundingDigits.charAt(pos) - '0' + zero));
                            continue;
                        }
                    }
                    result.append((i <= minDig) ? zero : digit);
                }
            }
            if (!useSigDig) {
                if (this.getMaximumFractionDigits() > 0 || this.decimalSeparatorAlwaysShown) {
                    result.append(localized ? this.symbols.getDecimalSeparator() : '.');
                }
                int pos = roundingDecimalPos;
                for (int i = 0; i < this.getMaximumFractionDigits(); ++i) {
                    if (roundingDigits != null && pos < roundingDigits.length()) {
                        result.append((pos < 0) ? zero : ((char)(roundingDigits.charAt(pos) - '0' + zero)));
                        ++pos;
                    }
                    else {
                        result.append((i < this.getMinimumFractionDigits()) ? zero : digit);
                    }
                }
            }
            if (this.useExponentialNotation) {
                if (localized) {
                    result.append(this.symbols.getExponentSeparator());
                }
                else {
                    result.append('E');
                }
                if (this.exponentSignAlwaysShown) {
                    result.append(localized ? this.symbols.getPlusSign() : '+');
                }
                for (int i = 0; i < this.minExponentDigits; ++i) {
                    result.append(zero);
                }
            }
            if (padSpec != null && !this.useExponentialNotation) {
                for (int add = this.formatWidth - result.length() + sub0Start - ((part == 0) ? (this.positivePrefix.length() + this.positiveSuffix.length()) : (this.negativePrefix.length() + this.negativeSuffix.length())); add > 0; --add) {
                    result.insert(sub0Start, digit);
                    ++maxDig;
                    if (--add > 1 && this.isGroupingPosition(maxDig)) {
                        result.insert(sub0Start, group);
                    }
                }
            }
            if (padPos == 2) {
                result.append(padSpec);
            }
            this.appendAffixPattern(result, part != 0, false, localized);
            if (padPos == 3) {
                result.append(padSpec);
            }
            if (part == 0) {
                if (this.negativeSuffix.equals(this.positiveSuffix) && this.negativePrefix.equals('-' + this.positivePrefix)) {
                    break;
                }
                result.append(localized ? this.symbols.getPatternSeparator() : ';');
            }
        }
        return result.toString();
    }
    
    public void applyPattern(final String pattern) {
        this.applyPattern(pattern, false);
    }
    
    public void applyLocalizedPattern(final String pattern) {
        this.applyPattern(pattern, true);
    }
    
    private void applyPattern(final String pattern, final boolean localized) {
        this.applyPatternWithoutExpandAffix(pattern, localized);
        this.expandAffixAdjustWidth(null);
    }
    
    private void expandAffixAdjustWidth(final String pluralCount) {
        this.expandAffixes(pluralCount);
        if (this.formatWidth > 0) {
            this.formatWidth += this.positivePrefix.length() + this.positiveSuffix.length();
        }
    }
    
    private void applyPatternWithoutExpandAffix(final String pattern, final boolean localized) {
        char zeroDigit = '0';
        char sigDigit = '@';
        char groupingSeparator = ',';
        char decimalSeparator = '.';
        char percent = '%';
        char perMill = '\u2030';
        char digit = '#';
        char separator = ';';
        String exponent = String.valueOf('E');
        char plus = '+';
        char padEscape = '*';
        char minus = '-';
        if (localized) {
            zeroDigit = this.symbols.getZeroDigit();
            sigDigit = this.symbols.getSignificantDigit();
            groupingSeparator = this.symbols.getGroupingSeparator();
            decimalSeparator = this.symbols.getDecimalSeparator();
            percent = this.symbols.getPercent();
            perMill = this.symbols.getPerMill();
            digit = this.symbols.getDigit();
            separator = this.symbols.getPatternSeparator();
            exponent = this.symbols.getExponentSeparator();
            plus = this.symbols.getPlusSign();
            padEscape = this.symbols.getPadEscape();
            minus = this.symbols.getMinusSign();
        }
        final char nineDigit = (char)(zeroDigit + '\t');
        boolean gotNegative = false;
        for (int pos = 0, part = 0; part < 2 && pos < pattern.length(); ++part) {
            int subpart = 1;
            int sub0Start = 0;
            int sub0Limit = 0;
            int sub2Limit = 0;
            final StringBuilder prefix = new StringBuilder();
            final StringBuilder suffix = new StringBuilder();
            int decimalPos = -1;
            int multpl = 1;
            int digitLeftCount = 0;
            int zeroDigitCount = 0;
            int digitRightCount = 0;
            int sigDigitCount = 0;
            byte groupingCount = -1;
            byte groupingCount2 = -1;
            int padPos = -1;
            char padChar = '\0';
            int incrementPos = -1;
            long incrementVal = 0L;
            byte expDigits = -1;
            boolean expSignAlways = false;
            int currencySignCnt = 0;
            StringBuilder affix = prefix;
            final int start = pos;
        Label_1422:
            while (pos < pattern.length()) {
                char ch = pattern.charAt(pos);
                switch (subpart) {
                    case 0: {
                        if (ch == digit) {
                            if (zeroDigitCount > 0 || sigDigitCount > 0) {
                                ++digitRightCount;
                            }
                            else {
                                ++digitLeftCount;
                            }
                            if (groupingCount >= 0 && decimalPos < 0) {
                                ++groupingCount;
                                break;
                            }
                            break;
                        }
                        else if ((ch >= zeroDigit && ch <= nineDigit) || ch == sigDigit) {
                            if (digitRightCount > 0) {
                                this.patternError("Unexpected '" + ch + '\'', pattern);
                            }
                            if (ch == sigDigit) {
                                ++sigDigitCount;
                            }
                            else {
                                ++zeroDigitCount;
                                if (ch != zeroDigit) {
                                    final int p = digitLeftCount + zeroDigitCount + digitRightCount;
                                    if (incrementPos >= 0) {
                                        while (incrementPos < p) {
                                            incrementVal *= 10L;
                                            ++incrementPos;
                                        }
                                    }
                                    else {
                                        incrementPos = p;
                                    }
                                    incrementVal += ch - zeroDigit;
                                }
                            }
                            if (groupingCount >= 0 && decimalPos < 0) {
                                ++groupingCount;
                                break;
                            }
                            break;
                        }
                        else {
                            if (ch == groupingSeparator) {
                                if (ch == '\'' && pos + 1 < pattern.length()) {
                                    final char after = pattern.charAt(pos + 1);
                                    if (after != digit && (after < zeroDigit || after > nineDigit)) {
                                        if (after == '\'') {
                                            ++pos;
                                        }
                                        else {
                                            if (groupingCount < 0) {
                                                subpart = 3;
                                                break;
                                            }
                                            subpart = 2;
                                            affix = suffix;
                                            sub0Limit = pos--;
                                            break;
                                        }
                                    }
                                }
                                if (decimalPos >= 0) {
                                    this.patternError("Grouping separator after decimal", pattern);
                                }
                                groupingCount2 = groupingCount;
                                groupingCount = 0;
                                break;
                            }
                            if (ch == decimalSeparator) {
                                if (decimalPos >= 0) {
                                    this.patternError("Multiple decimal separators", pattern);
                                }
                                decimalPos = digitLeftCount + zeroDigitCount + digitRightCount;
                                break;
                            }
                            if (pattern.regionMatches(pos, exponent, 0, exponent.length())) {
                                if (expDigits >= 0) {
                                    this.patternError("Multiple exponential symbols", pattern);
                                }
                                if (groupingCount >= 0) {
                                    this.patternError("Grouping separator in exponential", pattern);
                                }
                                pos += exponent.length();
                                if (pos < pattern.length() && pattern.charAt(pos) == plus) {
                                    expSignAlways = true;
                                    ++pos;
                                }
                                expDigits = 0;
                                while (pos < pattern.length() && pattern.charAt(pos) == zeroDigit) {
                                    ++expDigits;
                                    ++pos;
                                }
                                if ((digitLeftCount + zeroDigitCount < 1 && sigDigitCount + digitRightCount < 1) || (sigDigitCount > 0 && digitLeftCount > 0) || expDigits < 1) {
                                    this.patternError("Malformed exponential", pattern);
                                }
                            }
                            subpart = 2;
                            affix = suffix;
                            sub0Limit = pos--;
                            break;
                        }
                        break;
                    }
                    case 1:
                    case 2: {
                        if (ch == digit || ch == groupingSeparator || ch == decimalSeparator || (ch >= zeroDigit && ch <= nineDigit) || ch == sigDigit) {
                            if (subpart == 1) {
                                subpart = 0;
                                sub0Start = pos--;
                                break;
                            }
                            if (ch == '\'') {
                                if (pos + 1 < pattern.length() && pattern.charAt(pos + 1) == '\'') {
                                    ++pos;
                                    affix.append(ch);
                                    break;
                                }
                                subpart += 2;
                                break;
                            }
                            else {
                                this.patternError("Unquoted special character '" + ch + '\'', pattern);
                            }
                        }
                        else if (ch == '¤') {
                            final boolean doubled = pos + 1 < pattern.length() && pattern.charAt(pos + 1) == '¤';
                            if (doubled) {
                                ++pos;
                                affix.append(ch);
                                if (pos + 1 < pattern.length() && pattern.charAt(pos + 1) == '¤') {
                                    ++pos;
                                    affix.append(ch);
                                    currencySignCnt = 3;
                                }
                                else {
                                    currencySignCnt = 2;
                                }
                            }
                            else {
                                currencySignCnt = 1;
                            }
                        }
                        else if (ch == '\'') {
                            if (pos + 1 < pattern.length() && pattern.charAt(pos + 1) == '\'') {
                                ++pos;
                                affix.append(ch);
                            }
                            else {
                                subpart += 2;
                            }
                        }
                        else {
                            if (ch == separator) {
                                if (subpart == 1 || part == 1) {
                                    this.patternError("Unquoted special character '" + ch + '\'', pattern);
                                }
                                sub2Limit = pos++;
                                break Label_1422;
                            }
                            if (ch == percent || ch == perMill) {
                                if (multpl != 1) {
                                    this.patternError("Too many percent/permille characters", pattern);
                                }
                                multpl = ((ch == percent) ? 100 : 1000);
                                ch = ((ch == percent) ? '%' : '\u2030');
                            }
                            else if (ch == minus) {
                                ch = '-';
                            }
                            else if (ch == padEscape) {
                                if (padPos >= 0) {
                                    this.patternError("Multiple pad specifiers", pattern);
                                }
                                if (pos + 1 == pattern.length()) {
                                    this.patternError("Invalid pad specifier", pattern);
                                }
                                padPos = pos++;
                                padChar = pattern.charAt(pos);
                                break;
                            }
                        }
                        affix.append(ch);
                        break;
                    }
                    case 3:
                    case 4: {
                        if (ch == '\'') {
                            if (pos + 1 < pattern.length() && pattern.charAt(pos + 1) == '\'') {
                                ++pos;
                                affix.append(ch);
                            }
                            else {
                                subpart -= 2;
                            }
                        }
                        affix.append(ch);
                        break;
                    }
                }
                ++pos;
            }
            if (subpart == 3 || subpart == 4) {
                this.patternError("Unterminated quote", pattern);
            }
            if (sub0Limit == 0) {
                sub0Limit = pattern.length();
            }
            if (sub2Limit == 0) {
                sub2Limit = pattern.length();
            }
            if (zeroDigitCount == 0 && sigDigitCount == 0 && digitLeftCount > 0 && decimalPos >= 0) {
                int n = decimalPos;
                if (n == 0) {
                    ++n;
                }
                digitRightCount = digitLeftCount - n;
                digitLeftCount = n - 1;
                zeroDigitCount = 1;
            }
            if ((decimalPos < 0 && digitRightCount > 0 && sigDigitCount == 0) || (decimalPos >= 0 && (sigDigitCount > 0 || decimalPos < digitLeftCount || decimalPos > digitLeftCount + zeroDigitCount)) || groupingCount == 0 || groupingCount2 == 0 || (sigDigitCount > 0 && zeroDigitCount > 0) || subpart > 2) {
                this.patternError("Malformed pattern", pattern);
            }
            if (padPos >= 0) {
                if (padPos == start) {
                    padPos = 0;
                }
                else if (padPos + 2 == sub0Start) {
                    padPos = 1;
                }
                else if (padPos == sub0Limit) {
                    padPos = 2;
                }
                else if (padPos + 2 == sub2Limit) {
                    padPos = 3;
                }
                else {
                    this.patternError("Illegal pad position", pattern);
                }
            }
            if (part == 0) {
                final String string = prefix.toString();
                this.negPrefixPattern = string;
                this.posPrefixPattern = string;
                final String string2 = suffix.toString();
                this.negSuffixPattern = string2;
                this.posSuffixPattern = string2;
                this.useExponentialNotation = (expDigits >= 0);
                if (this.useExponentialNotation) {
                    this.minExponentDigits = expDigits;
                    this.exponentSignAlwaysShown = expSignAlways;
                }
                final int digitTotalCount = digitLeftCount + zeroDigitCount + digitRightCount;
                final int effectiveDecimalPos = (decimalPos >= 0) ? decimalPos : digitTotalCount;
                final boolean useSigDig = sigDigitCount > 0;
                this.setSignificantDigitsUsed(useSigDig);
                if (useSigDig) {
                    this.setMinimumSignificantDigits(sigDigitCount);
                    this.setMaximumSignificantDigits(sigDigitCount + digitRightCount);
                }
                else {
                    final int minInt = effectiveDecimalPos - digitLeftCount;
                    this.setMinimumIntegerDigits(minInt);
                    this.setMaximumIntegerDigits(this.useExponentialNotation ? (digitLeftCount + minInt) : 309);
                    this.setMaximumFractionDigits((decimalPos >= 0) ? (digitTotalCount - decimalPos) : 0);
                    this.setMinimumFractionDigits((decimalPos >= 0) ? (digitLeftCount + zeroDigitCount - decimalPos) : 0);
                }
                this.setGroupingUsed(groupingCount > 0);
                this.groupingSize = (byte)((groupingCount > 0) ? groupingCount : 0);
                this.groupingSize2 = (byte)((groupingCount2 > 0 && groupingCount2 != groupingCount) ? groupingCount2 : 0);
                this.multiplier = multpl;
                this.setDecimalSeparatorAlwaysShown(decimalPos == 0 || decimalPos == digitTotalCount);
                if (padPos >= 0) {
                    this.padPosition = padPos;
                    this.formatWidth = sub0Limit - sub0Start;
                    this.pad = padChar;
                }
                else {
                    this.formatWidth = 0;
                }
                if (incrementVal != 0L) {
                    final int scale = incrementPos - effectiveDecimalPos;
                    this.roundingIncrementICU = com.ibm.icu.math.BigDecimal.valueOf(incrementVal, (scale > 0) ? scale : 0);
                    if (scale < 0) {
                        this.roundingIncrementICU = this.roundingIncrementICU.movePointRight(-scale);
                    }
                    this.setRoundingDouble();
                    this.roundingMode = 6;
                }
                else {
                    this.setRoundingIncrement((com.ibm.icu.math.BigDecimal)null);
                }
                this.currencySignCount = currencySignCnt;
            }
            else {
                this.negPrefixPattern = prefix.toString();
                this.negSuffixPattern = suffix.toString();
                gotNegative = true;
            }
        }
        if (pattern.length() == 0) {
            final String s = "";
            this.posSuffixPattern = s;
            this.posPrefixPattern = s;
            this.setMinimumIntegerDigits(0);
            this.setMaximumIntegerDigits(309);
            this.setMinimumFractionDigits(0);
            this.setMaximumFractionDigits(340);
        }
        if (!gotNegative || (this.negPrefixPattern.equals(this.posPrefixPattern) && this.negSuffixPattern.equals(this.posSuffixPattern))) {
            this.negSuffixPattern = this.posSuffixPattern;
            this.negPrefixPattern = '-' + this.posPrefixPattern;
        }
        this.setLocale(null, null);
        this.formatPattern = pattern;
        if (this.currencySignCount > 0) {
            final Currency theCurrency = this.getCurrency();
            if (theCurrency != null) {
                this.setRoundingIncrement(theCurrency.getRoundingIncrement());
                final int d = theCurrency.getDefaultFractionDigits();
                this.setMinimumFractionDigits(d);
                this.setMaximumFractionDigits(d);
            }
            if (this.currencySignCount == 3 && this.currencyPluralInfo == null) {
                this.currencyPluralInfo = new CurrencyPluralInfo(this.symbols.getULocale());
            }
        }
    }
    
    private void setRoundingDouble() {
        if (this.roundingIncrementICU == null) {
            this.roundingDouble = 0.0;
            this.roundingDoubleReciprocal = 0.0;
        }
        else {
            this.roundingDouble = this.roundingIncrementICU.doubleValue();
            this.setRoundingDoubleReciprocal(1.0 / this.roundingDouble);
        }
    }
    
    private void patternError(final String msg, final String pattern) {
        throw new IllegalArgumentException(msg + " in pattern \"" + pattern + '\"');
    }
    
    @Override
    public void setMaximumIntegerDigits(final int newValue) {
        super.setMaximumIntegerDigits(Math.min(newValue, 309));
    }
    
    @Override
    public void setMinimumIntegerDigits(final int newValue) {
        super.setMinimumIntegerDigits(Math.min(newValue, 309));
    }
    
    public int getMinimumSignificantDigits() {
        return this.minSignificantDigits;
    }
    
    public int getMaximumSignificantDigits() {
        return this.maxSignificantDigits;
    }
    
    public void setMinimumSignificantDigits(int min) {
        if (min < 1) {
            min = 1;
        }
        final int max = Math.max(this.maxSignificantDigits, min);
        this.minSignificantDigits = min;
        this.maxSignificantDigits = max;
    }
    
    public void setMaximumSignificantDigits(int max) {
        if (max < 1) {
            max = 1;
        }
        final int min = Math.min(this.minSignificantDigits, max);
        this.minSignificantDigits = min;
        this.maxSignificantDigits = max;
    }
    
    public boolean areSignificantDigitsUsed() {
        return this.useSignificantDigits;
    }
    
    public void setSignificantDigitsUsed(final boolean useSignificantDigits) {
        this.useSignificantDigits = useSignificantDigits;
    }
    
    @Override
    public void setCurrency(final Currency theCurrency) {
        super.setCurrency(theCurrency);
        if (theCurrency != null) {
            final boolean[] isChoiceFormat = { false };
            final String s = theCurrency.getName(this.symbols.getULocale(), 0, isChoiceFormat);
            this.symbols.setCurrency(theCurrency);
            this.symbols.setCurrencySymbol(s);
        }
        if (this.currencySignCount > 0) {
            if (theCurrency != null) {
                this.setRoundingIncrement(theCurrency.getRoundingIncrement());
                final int d = theCurrency.getDefaultFractionDigits();
                this.setMinimumFractionDigits(d);
                this.setMaximumFractionDigits(d);
            }
            if (this.currencySignCount != 3) {
                this.expandAffixes(null);
            }
        }
    }
    
    @Deprecated
    @Override
    protected Currency getEffectiveCurrency() {
        Currency c = this.getCurrency();
        if (c == null) {
            c = Currency.getInstance(this.symbols.getInternationalCurrencySymbol());
        }
        return c;
    }
    
    @Override
    public void setMaximumFractionDigits(final int newValue) {
        super.setMaximumFractionDigits(Math.min(newValue, 340));
    }
    
    @Override
    public void setMinimumFractionDigits(final int newValue) {
        super.setMinimumFractionDigits(Math.min(newValue, 340));
    }
    
    public void setParseBigDecimal(final boolean value) {
        this.parseBigDecimal = value;
    }
    
    public boolean isParseBigDecimal() {
        return this.parseBigDecimal;
    }
    
    public void setParseMaxDigits(final int newValue) {
        if (newValue > 0) {
            this.PARSE_MAX_EXPONENT = newValue;
        }
    }
    
    public int getParseMaxDigits() {
        return this.PARSE_MAX_EXPONENT;
    }
    
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        this.attributes.clear();
        stream.defaultWriteObject();
    }
    
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        if (this.getMaximumIntegerDigits() > 309) {
            this.setMaximumIntegerDigits(309);
        }
        if (this.getMaximumFractionDigits() > 340) {
            this.setMaximumFractionDigits(340);
        }
        if (this.serialVersionOnStream < 2) {
            this.exponentSignAlwaysShown = false;
            this.setInternalRoundingIncrement(null);
            this.setRoundingDouble();
            this.roundingMode = 6;
            this.formatWidth = 0;
            this.pad = ' ';
            this.padPosition = 0;
            if (this.serialVersionOnStream < 1) {
                this.useExponentialNotation = false;
            }
        }
        if (this.serialVersionOnStream < 3) {
            this.setCurrencyForSymbols();
        }
        this.serialVersionOnStream = 3;
        this.digitList = new DigitList();
        if (this.roundingIncrement != null) {
            this.setInternalRoundingIncrement(new com.ibm.icu.math.BigDecimal(this.roundingIncrement));
            this.setRoundingDouble();
        }
    }
    
    private void setInternalRoundingIncrement(final com.ibm.icu.math.BigDecimal value) {
        this.roundingIncrementICU = value;
        this.roundingIncrement = ((value == null) ? null : value.toBigDecimal());
    }
    
    static {
        DecimalFormat.epsilon = 1.0E-11;
        dotEquivalents = new UnicodeSet(new int[] { 46, 46, 8228, 8228, 12290, 12290, 65042, 65042, 65106, 65106, 65294, 65294, 65377, 65377 }).freeze();
        commaEquivalents = new UnicodeSet(new int[] { 44, 44, 1548, 1548, 1643, 1643, 12289, 12289, 65040, 65041, 65104, 65105, 65292, 65292, 65380, 65380 }).freeze();
        strictDotEquivalents = new UnicodeSet(new int[] { 46, 46, 8228, 8228, 65106, 65106, 65294, 65294, 65377, 65377 }).freeze();
        strictCommaEquivalents = new UnicodeSet(new int[] { 44, 44, 1643, 1643, 65040, 65040, 65104, 65104, 65292, 65292 }).freeze();
        defaultGroupingSeparators = new UnicodeSet(new int[] { 32, 32, 39, 39, 44, 44, 46, 46, 160, 160, 1548, 1548, 1643, 1644, 8192, 8202, 8216, 8217, 8228, 8228, 8239, 8239, 8287, 8287, 12288, 12290, 65040, 65042, 65104, 65106, 65287, 65287, 65292, 65292, 65294, 65294, 65377, 65377, 65380, 65380 }).freeze();
        strictDefaultGroupingSeparators = new UnicodeSet(new int[] { 32, 32, 39, 39, 44, 44, 46, 46, 160, 160, 1643, 1644, 8192, 8202, 8216, 8217, 8228, 8228, 8239, 8239, 8287, 8287, 12288, 12288, 65040, 65040, 65104, 65104, 65106, 65106, 65287, 65287, 65292, 65292, 65294, 65294, 65377, 65377 }).freeze();
        NULL_UNIT = new Unit("", "");
    }
    
    private static final class AffixForCurrency
    {
        private String negPrefixPatternForCurrency;
        private String negSuffixPatternForCurrency;
        private String posPrefixPatternForCurrency;
        private String posSuffixPatternForCurrency;
        private final int patternType;
        
        public AffixForCurrency(final String negPrefix, final String negSuffix, final String posPrefix, final String posSuffix, final int type) {
            this.negPrefixPatternForCurrency = null;
            this.negSuffixPatternForCurrency = null;
            this.posPrefixPatternForCurrency = null;
            this.posSuffixPatternForCurrency = null;
            this.negPrefixPatternForCurrency = negPrefix;
            this.negSuffixPatternForCurrency = negSuffix;
            this.posPrefixPatternForCurrency = posPrefix;
            this.posSuffixPatternForCurrency = posSuffix;
            this.patternType = type;
        }
        
        public String getNegPrefix() {
            return this.negPrefixPatternForCurrency;
        }
        
        public String getNegSuffix() {
            return this.negSuffixPatternForCurrency;
        }
        
        public String getPosPrefix() {
            return this.posPrefixPatternForCurrency;
        }
        
        public String getPosSuffix() {
            return this.posSuffixPatternForCurrency;
        }
        
        public int getPatternType() {
            return this.patternType;
        }
    }
    
    static class Unit
    {
        private final String prefix;
        private final String suffix;
        
        public Unit(final String prefix, final String suffix) {
            this.prefix = prefix;
            this.suffix = suffix;
        }
        
        public void writeSuffix(final StringBuffer toAppendTo) {
            toAppendTo.append(this.suffix);
        }
        
        public void writePrefix(final StringBuffer toAppendTo) {
            toAppendTo.append(this.prefix);
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Unit)) {
                return false;
            }
            final Unit other = (Unit)obj;
            return this.prefix.equals(other.prefix) && this.suffix.equals(other.suffix);
        }
    }
}
