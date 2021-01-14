package moonx.ohare.client.utils.value.impl;

import moonx.ohare.client.utils.value.Value;
import moonx.ohare.client.utils.value.clamper.NumberClamper;
import moonx.ohare.client.utils.value.parse.NumberParser;

/**
 * made by Fricking for negro
 *
 * @since 11/29/2018
 **/
public class RangedValue<T extends Number> extends Value<T> {

    private T minimum, maximum, inc, leftVal, rightVal;

    public RangedValue(String label, T minimum, T maximum, T inc, T leftVal, T rightVal) {
        super(label, null);
        this.minimum = minimum;
        this.maximum = maximum;
        this.inc = inc;
        this.leftVal = leftVal;
        this.rightVal = rightVal;
    }

    public RangedValue(String label, T minimum, T maximum, T inc, T leftVal, T rightVal, Value parentValueObject, String parentValue) {
        super(label, null, parentValueObject, parentValue);
        this.minimum = minimum;
        this.maximum = maximum;
        this.inc = inc;
        this.leftVal = leftVal;
        this.rightVal = rightVal;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = NumberClamper.clamp(value, minimum, maximum);
    }

    @Override
    public void setValue(String value) {
        try {
            this.setValue(NumberParser.parse(value, (Class<T>) ((Number) this.value).getClass()));
        } catch (NumberFormatException e) {
        }
    }

    public T getMinimum() {
        return minimum;
    }

    public T getMaximum() {
        return maximum;
    }

    public T getLeftVal() {
        return leftVal;
    }

    public T getRightVal() {
        return rightVal;
    }

    public T getInc() {
        return inc;
    }

    public void setInc(T inc) {
        this.inc = inc;
    }

    public void setLeftVal(T val) {
        this.leftVal = NumberClamper.clamp(val, minimum, maximum);
    }

    public void setRightVal(T rightVal) {
        this.rightVal = NumberClamper.clamp(rightVal, minimum, maximum);
    }
}
