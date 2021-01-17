// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public final class MessagePatternUtil
{
    private MessagePatternUtil() {
    }
    
    public static MessageNode buildMessageNode(final String patternString) {
        return buildMessageNode(new MessagePattern(patternString));
    }
    
    public static MessageNode buildMessageNode(final MessagePattern pattern) {
        final int limit = pattern.countParts() - 1;
        if (limit < 0) {
            throw new IllegalArgumentException("The MessagePattern is empty");
        }
        if (pattern.getPartType(0) != MessagePattern.Part.Type.MSG_START) {
            throw new IllegalArgumentException("The MessagePattern does not represent a MessageFormat pattern");
        }
        return buildMessageNode(pattern, 0, limit);
    }
    
    private static MessageNode buildMessageNode(final MessagePattern pattern, final int start, final int limit) {
        int prevPatternIndex = pattern.getPart(start).getLimit();
        final MessageNode node = new MessageNode();
        int i = start + 1;
        while (true) {
            MessagePattern.Part part = pattern.getPart(i);
            final int patternIndex = part.getIndex();
            if (prevPatternIndex < patternIndex) {
                node.addContentsNode(new TextNode(pattern.getPatternString().substring(prevPatternIndex, patternIndex)));
            }
            if (i == limit) {
                break;
            }
            final MessagePattern.Part.Type partType = part.getType();
            if (partType == MessagePattern.Part.Type.ARG_START) {
                final int argLimit = pattern.getLimitPartIndex(i);
                node.addContentsNode(buildArgNode(pattern, i, argLimit));
                i = argLimit;
                part = pattern.getPart(i);
            }
            else if (partType == MessagePattern.Part.Type.REPLACE_NUMBER) {
                node.addContentsNode(createReplaceNumberNode());
            }
            prevPatternIndex = part.getLimit();
            ++i;
        }
        return node.freeze();
    }
    
    private static ArgNode buildArgNode(final MessagePattern pattern, int start, final int limit) {
        final ArgNode node = createArgNode();
        MessagePattern.Part part = pattern.getPart(start);
        final MessagePattern.ArgType argType = node.argType = part.getArgType();
        part = pattern.getPart(++start);
        node.name = pattern.getSubstring(part);
        if (part.getType() == MessagePattern.Part.Type.ARG_NUMBER) {
            node.number = part.getValue();
        }
        ++start;
        switch (argType) {
            case SIMPLE: {
                node.typeName = pattern.getSubstring(pattern.getPart(start++));
                if (start < limit) {
                    node.style = pattern.getSubstring(pattern.getPart(start));
                    break;
                }
                break;
            }
            case CHOICE: {
                node.typeName = "choice";
                node.complexStyle = buildChoiceStyleNode(pattern, start, limit);
                break;
            }
            case PLURAL: {
                node.typeName = "plural";
                node.complexStyle = buildPluralStyleNode(pattern, start, limit, argType);
                break;
            }
            case SELECT: {
                node.typeName = "select";
                node.complexStyle = buildSelectStyleNode(pattern, start, limit);
                break;
            }
            case SELECTORDINAL: {
                node.typeName = "selectordinal";
                node.complexStyle = buildPluralStyleNode(pattern, start, limit, argType);
                break;
            }
        }
        return node;
    }
    
    private static ComplexArgStyleNode buildChoiceStyleNode(final MessagePattern pattern, int start, final int limit) {
        final ComplexArgStyleNode node = new ComplexArgStyleNode(MessagePattern.ArgType.CHOICE);
        while (start < limit) {
            final int valueIndex = start;
            final MessagePattern.Part part = pattern.getPart(start);
            final double value = pattern.getNumericValue(part);
            start += 2;
            final int msgLimit = pattern.getLimitPartIndex(start);
            final VariantNode variant = new VariantNode();
            variant.selector = pattern.getSubstring(pattern.getPart(valueIndex + 1));
            variant.numericValue = value;
            variant.msgNode = buildMessageNode(pattern, start, msgLimit);
            node.addVariant(variant);
            start = msgLimit + 1;
        }
        return node.freeze();
    }
    
    private static ComplexArgStyleNode buildPluralStyleNode(final MessagePattern pattern, int start, final int limit, final MessagePattern.ArgType argType) {
        final ComplexArgStyleNode node = new ComplexArgStyleNode(argType);
        final MessagePattern.Part offset = pattern.getPart(start);
        if (offset.getType().hasNumericValue()) {
            node.explicitOffset = true;
            node.offset = pattern.getNumericValue(offset);
            ++start;
        }
        while (start < limit) {
            final MessagePattern.Part selector = pattern.getPart(start++);
            double value = -1.23456789E8;
            final MessagePattern.Part part = pattern.getPart(start);
            if (part.getType().hasNumericValue()) {
                value = pattern.getNumericValue(part);
                ++start;
            }
            final int msgLimit = pattern.getLimitPartIndex(start);
            final VariantNode variant = new VariantNode();
            variant.selector = pattern.getSubstring(selector);
            variant.numericValue = value;
            variant.msgNode = buildMessageNode(pattern, start, msgLimit);
            node.addVariant(variant);
            start = msgLimit + 1;
        }
        return node.freeze();
    }
    
    private static ComplexArgStyleNode buildSelectStyleNode(final MessagePattern pattern, int start, final int limit) {
        final ComplexArgStyleNode node = new ComplexArgStyleNode(MessagePattern.ArgType.SELECT);
        while (start < limit) {
            final MessagePattern.Part selector = pattern.getPart(start++);
            final int msgLimit = pattern.getLimitPartIndex(start);
            final VariantNode variant = new VariantNode();
            variant.selector = pattern.getSubstring(selector);
            variant.msgNode = buildMessageNode(pattern, start, msgLimit);
            node.addVariant(variant);
            start = msgLimit + 1;
        }
        return node.freeze();
    }
    
    public static class Node
    {
        private Node() {
        }
    }
    
    public static class MessageNode extends Node
    {
        private List<MessageContentsNode> list;
        
        public List<MessageContentsNode> getContents() {
            return this.list;
        }
        
        @Override
        public String toString() {
            return this.list.toString();
        }
        
        private MessageNode() {
            this.list = new ArrayList<MessageContentsNode>();
        }
        
        private void addContentsNode(final MessageContentsNode node) {
            if (node instanceof TextNode && !this.list.isEmpty()) {
                final MessageContentsNode lastNode = this.list.get(this.list.size() - 1);
                if (lastNode instanceof TextNode) {
                    final TextNode textNode = (TextNode)lastNode;
                    textNode.text += ((TextNode)node).text;
                    return;
                }
            }
            this.list.add(node);
        }
        
        private MessageNode freeze() {
            this.list = Collections.unmodifiableList((List<? extends MessageContentsNode>)this.list);
            return this;
        }
    }
    
    public static class MessageContentsNode extends Node
    {
        private Type type;
        
        public Type getType() {
            return this.type;
        }
        
        @Override
        public String toString() {
            return "{REPLACE_NUMBER}";
        }
        
        private MessageContentsNode(final Type type) {
            this.type = type;
        }
        
        private static MessageContentsNode createReplaceNumberNode() {
            return new MessageContentsNode(Type.REPLACE_NUMBER);
        }
        
        public enum Type
        {
            TEXT, 
            ARG, 
            REPLACE_NUMBER;
        }
    }
    
    public static class TextNode extends MessageContentsNode
    {
        private String text;
        
        public String getText() {
            return this.text;
        }
        
        @Override
        public String toString() {
            return "«" + this.text + "»";
        }
        
        private TextNode(final String text) {
            super(Type.TEXT);
            this.text = text;
        }
    }
    
    public static class ArgNode extends MessageContentsNode
    {
        private MessagePattern.ArgType argType;
        private String name;
        private int number;
        private String typeName;
        private String style;
        private ComplexArgStyleNode complexStyle;
        
        public MessagePattern.ArgType getArgType() {
            return this.argType;
        }
        
        public String getName() {
            return this.name;
        }
        
        public int getNumber() {
            return this.number;
        }
        
        public String getTypeName() {
            return this.typeName;
        }
        
        public String getSimpleStyle() {
            return this.style;
        }
        
        public ComplexArgStyleNode getComplexStyle() {
            return this.complexStyle;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append('{').append(this.name);
            if (this.argType != MessagePattern.ArgType.NONE) {
                sb.append(',').append(this.typeName);
                if (this.argType == MessagePattern.ArgType.SIMPLE) {
                    if (this.style != null) {
                        sb.append(',').append(this.style);
                    }
                }
                else {
                    sb.append(',').append(this.complexStyle.toString());
                }
            }
            return sb.append('}').toString();
        }
        
        private ArgNode() {
            super(Type.ARG);
            this.number = -1;
        }
        
        private static ArgNode createArgNode() {
            return new ArgNode();
        }
    }
    
    public static class ComplexArgStyleNode extends Node
    {
        private MessagePattern.ArgType argType;
        private double offset;
        private boolean explicitOffset;
        private List<VariantNode> list;
        
        public MessagePattern.ArgType getArgType() {
            return this.argType;
        }
        
        public boolean hasExplicitOffset() {
            return this.explicitOffset;
        }
        
        public double getOffset() {
            return this.offset;
        }
        
        public List<VariantNode> getVariants() {
            return this.list;
        }
        
        public VariantNode getVariantsByType(final List<VariantNode> numericVariants, final List<VariantNode> keywordVariants) {
            if (numericVariants != null) {
                numericVariants.clear();
            }
            keywordVariants.clear();
            VariantNode other = null;
            for (final VariantNode variant : this.list) {
                if (variant.isSelectorNumeric()) {
                    numericVariants.add(variant);
                }
                else if ("other".equals(variant.getSelector())) {
                    if (other != null) {
                        continue;
                    }
                    other = variant;
                }
                else {
                    keywordVariants.add(variant);
                }
            }
            return other;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append('(').append(this.argType.toString()).append(" style) ");
            if (this.hasExplicitOffset()) {
                sb.append("offset:").append(this.offset).append(' ');
            }
            return sb.append(this.list.toString()).toString();
        }
        
        private ComplexArgStyleNode(final MessagePattern.ArgType argType) {
            this.list = new ArrayList<VariantNode>();
            this.argType = argType;
        }
        
        private void addVariant(final VariantNode variant) {
            this.list.add(variant);
        }
        
        private ComplexArgStyleNode freeze() {
            this.list = Collections.unmodifiableList((List<? extends VariantNode>)this.list);
            return this;
        }
    }
    
    public static class VariantNode extends Node
    {
        private String selector;
        private double numericValue;
        private MessageNode msgNode;
        
        public String getSelector() {
            return this.selector;
        }
        
        public boolean isSelectorNumeric() {
            return this.numericValue != -1.23456789E8;
        }
        
        public double getSelectorValue() {
            return this.numericValue;
        }
        
        public MessageNode getMessage() {
            return this.msgNode;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            if (this.isSelectorNumeric()) {
                sb.append(this.numericValue).append(" (").append(this.selector).append(") {");
            }
            else {
                sb.append(this.selector).append(" {");
            }
            return sb.append(this.msgNode.toString()).append('}').toString();
        }
        
        private VariantNode() {
            this.numericValue = -1.23456789E8;
        }
    }
}
