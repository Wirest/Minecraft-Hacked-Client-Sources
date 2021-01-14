package io.netty.util;

public abstract interface AttributeMap {
    public abstract <T> Attribute<T> attr(AttributeKey<T> paramAttributeKey);
}




