package dev.astroclient.security.uuid.attribute;

public final class NullAttribute<T> implements Attribute<T> {

	private final static Object NULL = null;

	@Override
	public String getId() {
		return "NULL";
	}

	@Override
	public int getKey() {
		return -1;
	}

	@Override
	public T getValue() {
		return (T) NULL;
	}

	@Override
	public void updateValue() {
		// . . .
	}
}
