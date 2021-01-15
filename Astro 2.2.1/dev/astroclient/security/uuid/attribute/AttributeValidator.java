package dev.astroclient.security.uuid.attribute;

import java.util.function.Predicate;

public interface AttributeValidator<T> extends Predicate<Attribute<T>> {

    @Override
    boolean test(Attribute<T> attribute);
}
