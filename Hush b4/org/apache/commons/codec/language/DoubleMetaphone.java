// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class DoubleMetaphone implements StringEncoder
{
    private static final String VOWELS = "AEIOUY";
    private static final String[] SILENT_START;
    private static final String[] L_R_N_M_B_H_F_V_W_SPACE;
    private static final String[] ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER;
    private static final String[] L_T_K_S_N_M_B_Z;
    private int maxCodeLen;
    
    public DoubleMetaphone() {
        this.maxCodeLen = 4;
    }
    
    public String doubleMetaphone(final String value) {
        return this.doubleMetaphone(value, false);
    }
    
    public String doubleMetaphone(String value, final boolean alternate) {
        value = this.cleanInput(value);
        if (value == null) {
            return null;
        }
        final boolean slavoGermanic = this.isSlavoGermanic(value);
        int index = this.isSilentStart(value) ? 1 : 0;
        final DoubleMetaphoneResult result = new DoubleMetaphoneResult(this.getMaxCodeLen());
        while (!result.isComplete() && index <= value.length() - 1) {
            switch (value.charAt(index)) {
                case 'A':
                case 'E':
                case 'I':
                case 'O':
                case 'U':
                case 'Y': {
                    index = this.handleAEIOUY(result, index);
                    continue;
                }
                case 'B': {
                    result.append('P');
                    index = ((this.charAt(value, index + 1) == 'B') ? (index + 2) : (index + 1));
                    continue;
                }
                case '\u00c7': {
                    result.append('S');
                    ++index;
                    continue;
                }
                case 'C': {
                    index = this.handleC(value, result, index);
                    continue;
                }
                case 'D': {
                    index = this.handleD(value, result, index);
                    continue;
                }
                case 'F': {
                    result.append('F');
                    index = ((this.charAt(value, index + 1) == 'F') ? (index + 2) : (index + 1));
                    continue;
                }
                case 'G': {
                    index = this.handleG(value, result, index, slavoGermanic);
                    continue;
                }
                case 'H': {
                    index = this.handleH(value, result, index);
                    continue;
                }
                case 'J': {
                    index = this.handleJ(value, result, index, slavoGermanic);
                    continue;
                }
                case 'K': {
                    result.append('K');
                    index = ((this.charAt(value, index + 1) == 'K') ? (index + 2) : (index + 1));
                    continue;
                }
                case 'L': {
                    index = this.handleL(value, result, index);
                    continue;
                }
                case 'M': {
                    result.append('M');
                    index = (this.conditionM0(value, index) ? (index + 2) : (index + 1));
                    continue;
                }
                case 'N': {
                    result.append('N');
                    index = ((this.charAt(value, index + 1) == 'N') ? (index + 2) : (index + 1));
                    continue;
                }
                case '\u00d1': {
                    result.append('N');
                    ++index;
                    continue;
                }
                case 'P': {
                    index = this.handleP(value, result, index);
                    continue;
                }
                case 'Q': {
                    result.append('K');
                    index = ((this.charAt(value, index + 1) == 'Q') ? (index + 2) : (index + 1));
                    continue;
                }
                case 'R': {
                    index = this.handleR(value, result, index, slavoGermanic);
                    continue;
                }
                case 'S': {
                    index = this.handleS(value, result, index, slavoGermanic);
                    continue;
                }
                case 'T': {
                    index = this.handleT(value, result, index);
                    continue;
                }
                case 'V': {
                    result.append('F');
                    index = ((this.charAt(value, index + 1) == 'V') ? (index + 2) : (index + 1));
                    continue;
                }
                case 'W': {
                    index = this.handleW(value, result, index);
                    continue;
                }
                case 'X': {
                    index = this.handleX(value, result, index);
                    continue;
                }
                case 'Z': {
                    index = this.handleZ(value, result, index, slavoGermanic);
                    continue;
                }
                default: {
                    ++index;
                    continue;
                }
            }
        }
        return alternate ? result.getAlternate() : result.getPrimary();
    }
    
    @Override
    public Object encode(final Object obj) throws EncoderException {
        if (!(obj instanceof String)) {
            throw new EncoderException("DoubleMetaphone encode parameter is not of type String");
        }
        return this.doubleMetaphone((String)obj);
    }
    
    @Override
    public String encode(final String value) {
        return this.doubleMetaphone(value);
    }
    
    public boolean isDoubleMetaphoneEqual(final String value1, final String value2) {
        return this.isDoubleMetaphoneEqual(value1, value2, false);
    }
    
    public boolean isDoubleMetaphoneEqual(final String value1, final String value2, final boolean alternate) {
        return this.doubleMetaphone(value1, alternate).equals(this.doubleMetaphone(value2, alternate));
    }
    
    public int getMaxCodeLen() {
        return this.maxCodeLen;
    }
    
    public void setMaxCodeLen(final int maxCodeLen) {
        this.maxCodeLen = maxCodeLen;
    }
    
    private int handleAEIOUY(final DoubleMetaphoneResult result, final int index) {
        if (index == 0) {
            result.append('A');
        }
        return index + 1;
    }
    
    private int handleC(final String value, final DoubleMetaphoneResult result, int index) {
        if (this.conditionC0(value, index)) {
            result.append('K');
            index += 2;
        }
        else if (index == 0 && contains(value, index, 6, "CAESAR")) {
            result.append('S');
            index += 2;
        }
        else if (contains(value, index, 2, "CH")) {
            index = this.handleCH(value, result, index);
        }
        else if (contains(value, index, 2, "CZ") && !contains(value, index - 2, 4, "WICZ")) {
            result.append('S', 'X');
            index += 2;
        }
        else if (contains(value, index + 1, 3, "CIA")) {
            result.append('X');
            index += 3;
        }
        else {
            if (contains(value, index, 2, "CC") && (index != 1 || this.charAt(value, 0) != 'M')) {
                return this.handleCC(value, result, index);
            }
            if (contains(value, index, 2, "CK", "CG", "CQ")) {
                result.append('K');
                index += 2;
            }
            else if (contains(value, index, 2, "CI", "CE", "CY")) {
                if (contains(value, index, 3, "CIO", "CIE", "CIA")) {
                    result.append('S', 'X');
                }
                else {
                    result.append('S');
                }
                index += 2;
            }
            else {
                result.append('K');
                if (contains(value, index + 1, 2, " C", " Q", " G")) {
                    index += 3;
                }
                else if (contains(value, index + 1, 1, "C", "K", "Q") && !contains(value, index + 1, 2, "CE", "CI")) {
                    index += 2;
                }
                else {
                    ++index;
                }
            }
        }
        return index;
    }
    
    private int handleCC(final String value, final DoubleMetaphoneResult result, int index) {
        if (contains(value, index + 2, 1, "I", "E", "H") && !contains(value, index + 2, 2, "HU")) {
            if ((index == 1 && this.charAt(value, index - 1) == 'A') || contains(value, index - 1, 5, "UCCEE", "UCCES")) {
                result.append("KS");
            }
            else {
                result.append('X');
            }
            index += 3;
        }
        else {
            result.append('K');
            index += 2;
        }
        return index;
    }
    
    private int handleCH(final String value, final DoubleMetaphoneResult result, final int index) {
        if (index > 0 && contains(value, index, 4, "CHAE")) {
            result.append('K', 'X');
            return index + 2;
        }
        if (this.conditionCH0(value, index)) {
            result.append('K');
            return index + 2;
        }
        if (this.conditionCH1(value, index)) {
            result.append('K');
            return index + 2;
        }
        if (index > 0) {
            if (contains(value, 0, 2, "MC")) {
                result.append('K');
            }
            else {
                result.append('X', 'K');
            }
        }
        else {
            result.append('X');
        }
        return index + 2;
    }
    
    private int handleD(final String value, final DoubleMetaphoneResult result, int index) {
        if (contains(value, index, 2, "DG")) {
            if (contains(value, index + 2, 1, "I", "E", "Y")) {
                result.append('J');
                index += 3;
            }
            else {
                result.append("TK");
                index += 2;
            }
        }
        else if (contains(value, index, 2, "DT", "DD")) {
            result.append('T');
            index += 2;
        }
        else {
            result.append('T');
            ++index;
        }
        return index;
    }
    
    private int handleG(final String value, final DoubleMetaphoneResult result, int index, final boolean slavoGermanic) {
        if (this.charAt(value, index + 1) == 'H') {
            index = this.handleGH(value, result, index);
        }
        else if (this.charAt(value, index + 1) == 'N') {
            if (index == 1 && this.isVowel(this.charAt(value, 0)) && !slavoGermanic) {
                result.append("KN", "N");
            }
            else if (!contains(value, index + 2, 2, "EY") && this.charAt(value, index + 1) != 'Y' && !slavoGermanic) {
                result.append("N", "KN");
            }
            else {
                result.append("KN");
            }
            index += 2;
        }
        else if (contains(value, index + 1, 2, "LI") && !slavoGermanic) {
            result.append("KL", "L");
            index += 2;
        }
        else if (index == 0 && (this.charAt(value, index + 1) == 'Y' || contains(value, index + 1, 2, DoubleMetaphone.ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER))) {
            result.append('K', 'J');
            index += 2;
        }
        else if ((contains(value, index + 1, 2, "ER") || this.charAt(value, index + 1) == 'Y') && !contains(value, 0, 6, "DANGER", "RANGER", "MANGER") && !contains(value, index - 1, 1, "E", "I") && !contains(value, index - 1, 3, "RGY", "OGY")) {
            result.append('K', 'J');
            index += 2;
        }
        else if (contains(value, index + 1, 1, "E", "I", "Y") || contains(value, index - 1, 4, "AGGI", "OGGI")) {
            if (contains(value, 0, 4, "VAN ", "VON ") || contains(value, 0, 3, "SCH") || contains(value, index + 1, 2, "ET")) {
                result.append('K');
            }
            else if (contains(value, index + 1, 3, "IER")) {
                result.append('J');
            }
            else {
                result.append('J', 'K');
            }
            index += 2;
        }
        else if (this.charAt(value, index + 1) == 'G') {
            index += 2;
            result.append('K');
        }
        else {
            ++index;
            result.append('K');
        }
        return index;
    }
    
    private int handleGH(final String value, final DoubleMetaphoneResult result, int index) {
        if (index > 0 && !this.isVowel(this.charAt(value, index - 1))) {
            result.append('K');
            index += 2;
        }
        else if (index == 0) {
            if (this.charAt(value, index + 2) == 'I') {
                result.append('J');
            }
            else {
                result.append('K');
            }
            index += 2;
        }
        else if ((index > 1 && contains(value, index - 2, 1, "B", "H", "D")) || (index > 2 && contains(value, index - 3, 1, "B", "H", "D")) || (index > 3 && contains(value, index - 4, 1, "B", "H"))) {
            index += 2;
        }
        else {
            if (index > 2 && this.charAt(value, index - 1) == 'U' && contains(value, index - 3, 1, "C", "G", "L", "R", "T")) {
                result.append('F');
            }
            else if (index > 0 && this.charAt(value, index - 1) != 'I') {
                result.append('K');
            }
            index += 2;
        }
        return index;
    }
    
    private int handleH(final String value, final DoubleMetaphoneResult result, int index) {
        if ((index == 0 || this.isVowel(this.charAt(value, index - 1))) && this.isVowel(this.charAt(value, index + 1))) {
            result.append('H');
            index += 2;
        }
        else {
            ++index;
        }
        return index;
    }
    
    private int handleJ(final String value, final DoubleMetaphoneResult result, int index, final boolean slavoGermanic) {
        if (contains(value, index, 4, "JOSE") || contains(value, 0, 4, "SAN ")) {
            if ((index == 0 && this.charAt(value, index + 4) == ' ') || value.length() == 4 || contains(value, 0, 4, "SAN ")) {
                result.append('H');
            }
            else {
                result.append('J', 'H');
            }
            ++index;
        }
        else {
            if (index == 0 && !contains(value, index, 4, "JOSE")) {
                result.append('J', 'A');
            }
            else if (this.isVowel(this.charAt(value, index - 1)) && !slavoGermanic && (this.charAt(value, index + 1) == 'A' || this.charAt(value, index + 1) == 'O')) {
                result.append('J', 'H');
            }
            else if (index == value.length() - 1) {
                result.append('J', ' ');
            }
            else if (!contains(value, index + 1, 1, DoubleMetaphone.L_T_K_S_N_M_B_Z) && !contains(value, index - 1, 1, "S", "K", "L")) {
                result.append('J');
            }
            if (this.charAt(value, index + 1) == 'J') {
                index += 2;
            }
            else {
                ++index;
            }
        }
        return index;
    }
    
    private int handleL(final String value, final DoubleMetaphoneResult result, int index) {
        if (this.charAt(value, index + 1) == 'L') {
            if (this.conditionL0(value, index)) {
                result.appendPrimary('L');
            }
            else {
                result.append('L');
            }
            index += 2;
        }
        else {
            ++index;
            result.append('L');
        }
        return index;
    }
    
    private int handleP(final String value, final DoubleMetaphoneResult result, int index) {
        if (this.charAt(value, index + 1) == 'H') {
            result.append('F');
            index += 2;
        }
        else {
            result.append('P');
            index = (contains(value, index + 1, 1, "P", "B") ? (index + 2) : (index + 1));
        }
        return index;
    }
    
    private int handleR(final String value, final DoubleMetaphoneResult result, final int index, final boolean slavoGermanic) {
        if (index == value.length() - 1 && !slavoGermanic && contains(value, index - 2, 2, "IE") && !contains(value, index - 4, 2, "ME", "MA")) {
            result.appendAlternate('R');
        }
        else {
            result.append('R');
        }
        return (this.charAt(value, index + 1) == 'R') ? (index + 2) : (index + 1);
    }
    
    private int handleS(final String value, final DoubleMetaphoneResult result, int index, final boolean slavoGermanic) {
        if (contains(value, index - 1, 3, "ISL", "YSL")) {
            ++index;
        }
        else if (index == 0 && contains(value, index, 5, "SUGAR")) {
            result.append('X', 'S');
            ++index;
        }
        else if (contains(value, index, 2, "SH")) {
            if (contains(value, index + 1, 4, "HEIM", "HOEK", "HOLM", "HOLZ")) {
                result.append('S');
            }
            else {
                result.append('X');
            }
            index += 2;
        }
        else if (contains(value, index, 3, "SIO", "SIA") || contains(value, index, 4, "SIAN")) {
            if (slavoGermanic) {
                result.append('S');
            }
            else {
                result.append('S', 'X');
            }
            index += 3;
        }
        else if ((index == 0 && contains(value, index + 1, 1, "M", "N", "L", "W")) || contains(value, index + 1, 1, "Z")) {
            result.append('S', 'X');
            index = (contains(value, index + 1, 1, "Z") ? (index + 2) : (index + 1));
        }
        else if (contains(value, index, 2, "SC")) {
            index = this.handleSC(value, result, index);
        }
        else {
            if (index == value.length() - 1 && contains(value, index - 2, 2, "AI", "OI")) {
                result.appendAlternate('S');
            }
            else {
                result.append('S');
            }
            index = (contains(value, index + 1, 1, "S", "Z") ? (index + 2) : (index + 1));
        }
        return index;
    }
    
    private int handleSC(final String value, final DoubleMetaphoneResult result, final int index) {
        if (this.charAt(value, index + 2) == 'H') {
            if (contains(value, index + 3, 2, "OO", "ER", "EN", "UY", "ED", "EM")) {
                if (contains(value, index + 3, 2, "ER", "EN")) {
                    result.append("X", "SK");
                }
                else {
                    result.append("SK");
                }
            }
            else if (index == 0 && !this.isVowel(this.charAt(value, 3)) && this.charAt(value, 3) != 'W') {
                result.append('X', 'S');
            }
            else {
                result.append('X');
            }
        }
        else if (contains(value, index + 2, 1, "I", "E", "Y")) {
            result.append('S');
        }
        else {
            result.append("SK");
        }
        return index + 3;
    }
    
    private int handleT(final String value, final DoubleMetaphoneResult result, int index) {
        if (contains(value, index, 4, "TION")) {
            result.append('X');
            index += 3;
        }
        else if (contains(value, index, 3, "TIA", "TCH")) {
            result.append('X');
            index += 3;
        }
        else if (contains(value, index, 2, "TH") || contains(value, index, 3, "TTH")) {
            if (contains(value, index + 2, 2, "OM", "AM") || contains(value, 0, 4, "VAN ", "VON ") || contains(value, 0, 3, "SCH")) {
                result.append('T');
            }
            else {
                result.append('0', 'T');
            }
            index += 2;
        }
        else {
            result.append('T');
            index = (contains(value, index + 1, 1, "T", "D") ? (index + 2) : (index + 1));
        }
        return index;
    }
    
    private int handleW(final String value, final DoubleMetaphoneResult result, int index) {
        if (contains(value, index, 2, "WR")) {
            result.append('R');
            index += 2;
        }
        else if (index == 0 && (this.isVowel(this.charAt(value, index + 1)) || contains(value, index, 2, "WH"))) {
            if (this.isVowel(this.charAt(value, index + 1))) {
                result.append('A', 'F');
            }
            else {
                result.append('A');
            }
            ++index;
        }
        else if ((index == value.length() - 1 && this.isVowel(this.charAt(value, index - 1))) || contains(value, index - 1, 5, "EWSKI", "EWSKY", "OWSKI", "OWSKY") || contains(value, 0, 3, "SCH")) {
            result.appendAlternate('F');
            ++index;
        }
        else if (contains(value, index, 4, "WICZ", "WITZ")) {
            result.append("TS", "FX");
            index += 4;
        }
        else {
            ++index;
        }
        return index;
    }
    
    private int handleX(final String value, final DoubleMetaphoneResult result, int index) {
        if (index == 0) {
            result.append('S');
            ++index;
        }
        else {
            if (index != value.length() - 1 || (!contains(value, index - 3, 3, "IAU", "EAU") && !contains(value, index - 2, 2, "AU", "OU"))) {
                result.append("KS");
            }
            index = (contains(value, index + 1, 1, "C", "X") ? (index + 2) : (index + 1));
        }
        return index;
    }
    
    private int handleZ(final String value, final DoubleMetaphoneResult result, int index, final boolean slavoGermanic) {
        if (this.charAt(value, index + 1) == 'H') {
            result.append('J');
            index += 2;
        }
        else {
            if (contains(value, index + 1, 2, "ZO", "ZI", "ZA") || (slavoGermanic && index > 0 && this.charAt(value, index - 1) != 'T')) {
                result.append("S", "TS");
            }
            else {
                result.append('S');
            }
            index = ((this.charAt(value, index + 1) == 'Z') ? (index + 2) : (index + 1));
        }
        return index;
    }
    
    private boolean conditionC0(final String value, final int index) {
        if (contains(value, index, 4, "CHIA")) {
            return true;
        }
        if (index <= 1) {
            return false;
        }
        if (this.isVowel(this.charAt(value, index - 2))) {
            return false;
        }
        if (!contains(value, index - 1, 3, "ACH")) {
            return false;
        }
        final char c = this.charAt(value, index + 2);
        return (c != 'I' && c != 'E') || contains(value, index - 2, 6, "BACHER", "MACHER");
    }
    
    private boolean conditionCH0(final String value, final int index) {
        return index == 0 && (contains(value, index + 1, 5, "HARAC", "HARIS") || contains(value, index + 1, 3, "HOR", "HYM", "HIA", "HEM")) && !contains(value, 0, 5, "CHORE");
    }
    
    private boolean conditionCH1(final String value, final int index) {
        return contains(value, 0, 4, "VAN ", "VON ") || contains(value, 0, 3, "SCH") || contains(value, index - 2, 6, "ORCHES", "ARCHIT", "ORCHID") || contains(value, index + 2, 1, "T", "S") || ((contains(value, index - 1, 1, "A", "O", "U", "E") || index == 0) && (contains(value, index + 2, 1, DoubleMetaphone.L_R_N_M_B_H_F_V_W_SPACE) || index + 1 == value.length() - 1));
    }
    
    private boolean conditionL0(final String value, final int index) {
        return (index == value.length() - 3 && contains(value, index - 1, 4, "ILLO", "ILLA", "ALLE")) || ((contains(value, value.length() - 2, 2, "AS", "OS") || contains(value, value.length() - 1, 1, "A", "O")) && contains(value, index - 1, 4, "ALLE"));
    }
    
    private boolean conditionM0(final String value, final int index) {
        return this.charAt(value, index + 1) == 'M' || (contains(value, index - 1, 3, "UMB") && (index + 1 == value.length() - 1 || contains(value, index + 2, 2, "ER")));
    }
    
    private boolean isSlavoGermanic(final String value) {
        return value.indexOf(87) > -1 || value.indexOf(75) > -1 || value.indexOf("CZ") > -1 || value.indexOf("WITZ") > -1;
    }
    
    private boolean isVowel(final char ch) {
        return "AEIOUY".indexOf(ch) != -1;
    }
    
    private boolean isSilentStart(final String value) {
        boolean result = false;
        for (final String element : DoubleMetaphone.SILENT_START) {
            if (value.startsWith(element)) {
                result = true;
                break;
            }
        }
        return result;
    }
    
    private String cleanInput(String input) {
        if (input == null) {
            return null;
        }
        input = input.trim();
        if (input.length() == 0) {
            return null;
        }
        return input.toUpperCase(Locale.ENGLISH);
    }
    
    protected char charAt(final String value, final int index) {
        if (index < 0 || index >= value.length()) {
            return '\0';
        }
        return value.charAt(index);
    }
    
    protected static boolean contains(final String value, final int start, final int length, final String... criteria) {
        boolean result = false;
        if (start >= 0 && start + length <= value.length()) {
            final String target = value.substring(start, start + length);
            for (final String element : criteria) {
                if (target.equals(element)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
    
    static {
        SILENT_START = new String[] { "GN", "KN", "PN", "WR", "PS" };
        L_R_N_M_B_H_F_V_W_SPACE = new String[] { "L", "R", "N", "M", "B", "H", "F", "V", "W", " " };
        ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER = new String[] { "ES", "EP", "EB", "EL", "EY", "IB", "IL", "IN", "IE", "EI", "ER" };
        L_T_K_S_N_M_B_Z = new String[] { "L", "T", "K", "S", "N", "M", "B", "Z" };
    }
    
    public class DoubleMetaphoneResult
    {
        private final StringBuilder primary;
        private final StringBuilder alternate;
        private final int maxLength;
        
        public DoubleMetaphoneResult(final int maxLength) {
            this.primary = new StringBuilder(DoubleMetaphone.this.getMaxCodeLen());
            this.alternate = new StringBuilder(DoubleMetaphone.this.getMaxCodeLen());
            this.maxLength = maxLength;
        }
        
        public void append(final char value) {
            this.appendPrimary(value);
            this.appendAlternate(value);
        }
        
        public void append(final char primary, final char alternate) {
            this.appendPrimary(primary);
            this.appendAlternate(alternate);
        }
        
        public void appendPrimary(final char value) {
            if (this.primary.length() < this.maxLength) {
                this.primary.append(value);
            }
        }
        
        public void appendAlternate(final char value) {
            if (this.alternate.length() < this.maxLength) {
                this.alternate.append(value);
            }
        }
        
        public void append(final String value) {
            this.appendPrimary(value);
            this.appendAlternate(value);
        }
        
        public void append(final String primary, final String alternate) {
            this.appendPrimary(primary);
            this.appendAlternate(alternate);
        }
        
        public void appendPrimary(final String value) {
            final int addChars = this.maxLength - this.primary.length();
            if (value.length() <= addChars) {
                this.primary.append(value);
            }
            else {
                this.primary.append(value.substring(0, addChars));
            }
        }
        
        public void appendAlternate(final String value) {
            final int addChars = this.maxLength - this.alternate.length();
            if (value.length() <= addChars) {
                this.alternate.append(value);
            }
            else {
                this.alternate.append(value.substring(0, addChars));
            }
        }
        
        public String getPrimary() {
            return this.primary.toString();
        }
        
        public String getAlternate() {
            return this.alternate.toString();
        }
        
        public boolean isComplete() {
            return this.primary.length() >= this.maxLength && this.alternate.length() >= this.maxLength;
        }
    }
}
