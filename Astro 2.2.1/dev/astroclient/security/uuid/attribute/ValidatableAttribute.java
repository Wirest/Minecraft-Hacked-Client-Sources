package dev.astroclient.security.uuid.attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public final class ValidatableAttribute<T> extends BaseAttribute<T> {

	protected List<AttributeValidator<T>> validators;

	public ValidatableAttribute(String id, T value, AttributeValidator<T>... validators) {
		super(id, value);
		this.validators =  Arrays.asList(validators);
	}

	public ValidatableAttribute(String id, Supplier<T> supplier, AttributeValidator<T>... validators) {
		super(id, supplier);
		this.validators = Arrays.asList(validators);
	}

	public List<AttributeValidator<T>> getValidators() {
		if (validators == null || validators.isEmpty()) {
			return Collections.emptyList();
		}

		return this.validators;
	}
}
