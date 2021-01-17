// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

class RBBIRuleParseTable
{
    static final short doCheckVarDef = 1;
    static final short doDotAny = 2;
    static final short doEndAssign = 3;
    static final short doEndOfRule = 4;
    static final short doEndVariableName = 5;
    static final short doExit = 6;
    static final short doExprCatOperator = 7;
    static final short doExprFinished = 8;
    static final short doExprOrOperator = 9;
    static final short doExprRParen = 10;
    static final short doExprStart = 11;
    static final short doLParen = 12;
    static final short doNOP = 13;
    static final short doOptionEnd = 14;
    static final short doOptionStart = 15;
    static final short doReverseDir = 16;
    static final short doRuleChar = 17;
    static final short doRuleError = 18;
    static final short doRuleErrorAssignExpr = 19;
    static final short doScanUnicodeSet = 20;
    static final short doSlash = 21;
    static final short doStartAssign = 22;
    static final short doStartTagValue = 23;
    static final short doStartVariableName = 24;
    static final short doTagDigit = 25;
    static final short doTagExpectedError = 26;
    static final short doTagValue = 27;
    static final short doUnaryOpPlus = 28;
    static final short doUnaryOpQuestion = 29;
    static final short doUnaryOpStar = 30;
    static final short doVariableNameExpectedErr = 31;
    static final short kRuleSet_default = 255;
    static final short kRuleSet_digit_char = 128;
    static final short kRuleSet_eof = 252;
    static final short kRuleSet_escaped = 254;
    static final short kRuleSet_name_char = 129;
    static final short kRuleSet_name_start_char = 130;
    static final short kRuleSet_rule_char = 131;
    static final short kRuleSet_white_space = 132;
    static RBBIRuleTableElement[] gRuleParseStateTable;
    
    static {
        RBBIRuleParseTable.gRuleParseStateTable = new RBBIRuleTableElement[] { new RBBIRuleTableElement((short)13, 0, 0, 0, true, null), new RBBIRuleTableElement((short)11, 254, 21, 8, false, "start"), new RBBIRuleTableElement((short)13, 132, 1, 0, true, null), new RBBIRuleTableElement((short)11, 36, 80, 90, false, null), new RBBIRuleTableElement((short)13, 33, 11, 0, true, null), new RBBIRuleTableElement((short)13, 59, 1, 0, true, null), new RBBIRuleTableElement((short)13, 252, 0, 0, false, null), new RBBIRuleTableElement((short)11, 255, 21, 8, false, null), new RBBIRuleTableElement((short)4, 59, 1, 0, true, "break-rule-end"), new RBBIRuleTableElement((short)13, 132, 8, 0, true, null), new RBBIRuleTableElement((short)18, 255, 95, 0, false, null), new RBBIRuleTableElement((short)13, 33, 13, 0, true, "rev-option"), new RBBIRuleTableElement((short)16, 255, 20, 8, false, null), new RBBIRuleTableElement((short)15, 130, 15, 0, true, "option-scan1"), new RBBIRuleTableElement((short)18, 255, 95, 0, false, null), new RBBIRuleTableElement((short)13, 129, 15, 0, true, "option-scan2"), new RBBIRuleTableElement((short)14, 255, 17, 0, false, null), new RBBIRuleTableElement((short)13, 59, 1, 0, true, "option-scan3"), new RBBIRuleTableElement((short)13, 132, 17, 0, true, null), new RBBIRuleTableElement((short)18, 255, 95, 0, false, null), new RBBIRuleTableElement((short)11, 255, 21, 8, false, "reverse-rule"), new RBBIRuleTableElement((short)17, 254, 30, 0, true, "term"), new RBBIRuleTableElement((short)13, 132, 21, 0, true, null), new RBBIRuleTableElement((short)17, 131, 30, 0, true, null), new RBBIRuleTableElement((short)13, 91, 86, 30, false, null), new RBBIRuleTableElement((short)12, 40, 21, 30, true, null), new RBBIRuleTableElement((short)13, 36, 80, 29, false, null), new RBBIRuleTableElement((short)2, 46, 30, 0, true, null), new RBBIRuleTableElement((short)18, 255, 95, 0, false, null), new RBBIRuleTableElement((short)1, 255, 30, 0, false, "term-var-ref"), new RBBIRuleTableElement((short)13, 132, 30, 0, true, "expr-mod"), new RBBIRuleTableElement((short)30, 42, 35, 0, true, null), new RBBIRuleTableElement((short)28, 43, 35, 0, true, null), new RBBIRuleTableElement((short)29, 63, 35, 0, true, null), new RBBIRuleTableElement((short)13, 255, 35, 0, false, null), new RBBIRuleTableElement((short)7, 254, 21, 0, false, "expr-cont"), new RBBIRuleTableElement((short)13, 132, 35, 0, true, null), new RBBIRuleTableElement((short)7, 131, 21, 0, false, null), new RBBIRuleTableElement((short)7, 91, 21, 0, false, null), new RBBIRuleTableElement((short)7, 40, 21, 0, false, null), new RBBIRuleTableElement((short)7, 36, 21, 0, false, null), new RBBIRuleTableElement((short)7, 46, 21, 0, false, null), new RBBIRuleTableElement((short)7, 47, 47, 0, false, null), new RBBIRuleTableElement((short)7, 123, 59, 0, true, null), new RBBIRuleTableElement((short)9, 124, 21, 0, true, null), new RBBIRuleTableElement((short)10, 41, 255, 0, true, null), new RBBIRuleTableElement((short)8, 255, 255, 0, false, null), new RBBIRuleTableElement((short)21, 47, 49, 0, true, "look-ahead"), new RBBIRuleTableElement((short)13, 255, 95, 0, false, null), new RBBIRuleTableElement((short)7, 254, 21, 0, false, "expr-cont-no-slash"), new RBBIRuleTableElement((short)13, 132, 35, 0, true, null), new RBBIRuleTableElement((short)7, 131, 21, 0, false, null), new RBBIRuleTableElement((short)7, 91, 21, 0, false, null), new RBBIRuleTableElement((short)7, 40, 21, 0, false, null), new RBBIRuleTableElement((short)7, 36, 21, 0, false, null), new RBBIRuleTableElement((short)7, 46, 21, 0, false, null), new RBBIRuleTableElement((short)9, 124, 21, 0, true, null), new RBBIRuleTableElement((short)10, 41, 255, 0, true, null), new RBBIRuleTableElement((short)8, 255, 255, 0, false, null), new RBBIRuleTableElement((short)13, 132, 59, 0, true, "tag-open"), new RBBIRuleTableElement((short)23, 128, 62, 0, false, null), new RBBIRuleTableElement((short)26, 255, 95, 0, false, null), new RBBIRuleTableElement((short)13, 132, 66, 0, true, "tag-value"), new RBBIRuleTableElement((short)13, 125, 66, 0, false, null), new RBBIRuleTableElement((short)25, 128, 62, 0, true, null), new RBBIRuleTableElement((short)26, 255, 95, 0, false, null), new RBBIRuleTableElement((short)13, 132, 66, 0, true, "tag-close"), new RBBIRuleTableElement((short)27, 125, 69, 0, true, null), new RBBIRuleTableElement((short)26, 255, 95, 0, false, null), new RBBIRuleTableElement((short)7, 254, 21, 0, false, "expr-cont-no-tag"), new RBBIRuleTableElement((short)13, 132, 69, 0, true, null), new RBBIRuleTableElement((short)7, 131, 21, 0, false, null), new RBBIRuleTableElement((short)7, 91, 21, 0, false, null), new RBBIRuleTableElement((short)7, 40, 21, 0, false, null), new RBBIRuleTableElement((short)7, 36, 21, 0, false, null), new RBBIRuleTableElement((short)7, 46, 21, 0, false, null), new RBBIRuleTableElement((short)7, 47, 47, 0, false, null), new RBBIRuleTableElement((short)9, 124, 21, 0, true, null), new RBBIRuleTableElement((short)10, 41, 255, 0, true, null), new RBBIRuleTableElement((short)8, 255, 255, 0, false, null), new RBBIRuleTableElement((short)24, 36, 82, 0, true, "scan-var-name"), new RBBIRuleTableElement((short)13, 255, 95, 0, false, null), new RBBIRuleTableElement((short)13, 130, 84, 0, true, "scan-var-start"), new RBBIRuleTableElement((short)31, 255, 95, 0, false, null), new RBBIRuleTableElement((short)13, 129, 84, 0, true, "scan-var-body"), new RBBIRuleTableElement((short)5, 255, 255, 0, false, null), new RBBIRuleTableElement((short)20, 91, 255, 0, true, "scan-unicode-set"), new RBBIRuleTableElement((short)20, 112, 255, 0, true, null), new RBBIRuleTableElement((short)20, 80, 255, 0, true, null), new RBBIRuleTableElement((short)13, 255, 95, 0, false, null), new RBBIRuleTableElement((short)13, 132, 90, 0, true, "assign-or-rule"), new RBBIRuleTableElement((short)22, 61, 21, 93, true, null), new RBBIRuleTableElement((short)13, 255, 29, 8, false, null), new RBBIRuleTableElement((short)3, 59, 1, 0, true, "assign-end"), new RBBIRuleTableElement((short)19, 255, 95, 0, false, null), new RBBIRuleTableElement((short)6, 255, 95, 0, true, "errorDeath") };
    }
    
    static class RBBIRuleTableElement
    {
        short fAction;
        short fCharClass;
        short fNextState;
        short fPushState;
        boolean fNextChar;
        String fStateName;
        
        RBBIRuleTableElement(final short a, final int cc, final int ns, final int ps, final boolean nc, final String sn) {
            this.fAction = a;
            this.fCharClass = (short)cc;
            this.fNextState = (short)ns;
            this.fPushState = (short)ps;
            this.fNextChar = nc;
            this.fStateName = sn;
        }
    }
}
