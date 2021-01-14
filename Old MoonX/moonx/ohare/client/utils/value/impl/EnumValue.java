package moonx.ohare.client.utils.value.impl;


import moonx.ohare.client.utils.value.Value;

public class EnumValue<T extends Enum<T>> extends Value<T> {

    private final T[] constants;

    public EnumValue(String name, T value) {
        super(name, value);
        this.constants = extractConstantsFromEnumValue(value);
    }

    public EnumValue(String name, T value, Value parentValueObject, String parentValue) {
        super(name, value, parentValueObject, parentValue);
        this.constants = extractConstantsFromEnumValue(value);
    }

    public T[] extractConstantsFromEnumValue(T value) {
        return value.getDeclaringClass().getEnumConstants();
    }

    public String getFixedValue() {
        return getValue().toString();
    }

    public T[] getConstants() {
        return constants;
    }

    public Object[] getValues(EnumValue property) {
        return property.getConstants();
    }

    public void increment() {
        T currentValue = getValue();

        for (T constant : constants) {
            if (constant != currentValue) {
                continue;
            }

            T newValue;

            int ordinal = constant.ordinal();
            if (ordinal == constants.length - 1) {
                newValue = constants[0];
            } else {
                newValue = constants[ordinal + 1];
            }

            setValue(newValue);
            return;
        }
    }

    public void decrement() {
        T currentValue = getValue();

        for (T constant : constants) {
            if (constant != currentValue) {
                continue;
            }

            T newValue;

            int ordinal = constant.ordinal();
            if (ordinal == 0) {
                newValue = constants[constants.length - 1];
            } else {
                newValue = constants[ordinal - 1];
            }

            setValue(newValue);
            return;
        }
    }

    @Override
    public void setValue(String string) {
        for (T constant : constants) {
            if (constant.name().equalsIgnoreCase(string)) {
                setValue(constant);
            }
        }
    }
}