package moonx.ohare.client.utils.value.parse;


import moonx.ohare.client.utils.MathUtils;
import moonx.ohare.client.utils.value.impl.NumberValue;
import moonx.ohare.client.utils.value.impl.RangedValue;

public class NumberHelper {

    public static void decrecement(NumberValue value) {
        if (value.getValue() instanceof Integer) {
            int inc = (int) value.getValue();
            inc -= value.getInc().intValue();
            inc = (int) MathUtils.round(inc, 1);
            if (!(inc < value.getMinimum().intValue())) value.setValue(inc);
        } else if (value.getValue() instanceof Double) {
            double inc = (double) value.getValue();
            inc -= value.getInc().doubleValue();
            inc = MathUtils.round(inc, 1);
            if (!(inc < value.getMinimum().doubleValue())) value.setValue(inc);
        } else if (value.getValue() instanceof Float) {
            float inc = (float) value.getValue();
            inc -= value.getInc().floatValue();
            inc = (float) MathUtils.round(inc, 1);
            if (!(inc < value.getMinimum().floatValue())) value.setValue(inc);
        } else if (value.getValue() instanceof Long) {
            long inc = (long) value.getValue();
            inc -= value.getInc().longValue();
            inc = (long) MathUtils.round(inc, 1);
            if (!(inc < value.getMinimum().longValue())) value.setValue(inc);
        }
    }

    public static void increment(NumberValue value) {
        if (value.getValue() instanceof Integer) {
            int inc = (int) value.getValue();
            inc += value.getInc().intValue();
            inc = (int) MathUtils.round(inc, 1);
            if (!(inc > value.getMaximum().intValue())) value.setValue(inc);
        } else if (value.getValue() instanceof Double) {
            double inc = (double) value.getValue();
            inc += value.getInc().doubleValue();
            inc = MathUtils.round(inc, 1);
            if (!(inc > value.getMaximum().doubleValue())) value.setValue(inc);
        } else if (value.getValue() instanceof Float) {
            float inc = (float) value.getValue();
            inc += value.getInc().floatValue();
            inc = (float) MathUtils.round(inc, 1);
            if (!(inc > value.getMaximum().floatValue())) value.setValue(inc);
        } else if (value.getValue() instanceof Long) {
            long inc = (long) value.getValue();
            inc += value.getInc().longValue();
            inc = (long) MathUtils.round(inc, 1);
            if (!(inc > value.getMaximum().longValue())) value.setValue(inc);
        }
    }

    public static void decrecementRanged(RangedValue value, boolean left) {
        if (left) {
            if (value.getLeftVal() instanceof Integer) {
                int inc = (int) value.getLeftVal();
                inc -= value.getInc().intValue();
                inc = (int) MathUtils.round(inc, 1);
                if (!(inc < value.getMinimum().intValue())) value.setLeftVal(inc);
            } else if (value.getLeftVal() instanceof Double) {
                double inc = (double) value.getLeftVal();
                inc -= value.getInc().doubleValue();
                inc = MathUtils.round(inc, 1);
                if (!(inc < value.getMinimum().doubleValue())) value.setLeftVal(inc);
            } else if (value.getLeftVal() instanceof Float) {
                float inc = (float) value.getLeftVal();
                inc -= value.getInc().floatValue();
                inc = (float) MathUtils.round(inc, 1);
                if (!(inc < value.getMinimum().floatValue())) value.setLeftVal(inc);
            } else if (value.getLeftVal() instanceof Long) {
                long inc = (long) value.getLeftVal();
                inc -= value.getInc().longValue();
                inc = (long) MathUtils.round(inc, 1);
                if (!(inc < value.getMinimum().longValue())) value.setLeftVal(inc);
            }
        } else {
            if (value.getRightVal() instanceof Integer) {
                int inc = (int) value.getRightVal();
                inc -= value.getInc().intValue();
                inc = (int) MathUtils.round(inc, 1);
                if (!(inc < value.getLeftVal().intValue())) value.setRightVal(inc);
            } else if (value.getRightVal() instanceof Double) {
                double inc = (double) value.getRightVal();
                inc -= value.getInc().doubleValue();
                inc = MathUtils.round(inc, 1);
                if (!(inc < value.getLeftVal().doubleValue())) value.setRightVal(inc);
            } else if (value.getRightVal() instanceof Float) {
                float inc = (float) value.getRightVal();
                inc -= value.getInc().floatValue();
                inc = (float) MathUtils.round(inc, 1);
                if (!(inc < value.getLeftVal().floatValue())) value.setRightVal(inc);
            } else if (value.getRightVal() instanceof Long) {
                long inc = (long) value.getRightVal();
                inc -= value.getInc().longValue();
                inc = (long) MathUtils.round(inc, 1);
                if (!(inc < value.getLeftVal().longValue())) value.setRightVal(inc);
            }
        }
    }

    public static void increcementRanged(RangedValue value, boolean left) {
        if (left) {
            if (value.getLeftVal() instanceof Integer) {
                int inc = (int) value.getLeftVal();
                inc += value.getInc().intValue();
                inc = (int) MathUtils.round(inc, 1);
                if (!(inc > value.getRightVal().intValue())) value.setLeftVal(inc);
            } else if (value.getLeftVal() instanceof Double) {
                double inc = (double) value.getLeftVal();
                inc += value.getInc().doubleValue();
                inc = MathUtils.round(inc, 1);
                if (!(inc > value.getRightVal().doubleValue())) value.setLeftVal(inc);
            } else if (value.getLeftVal() instanceof Float) {
                float inc = (float) value.getLeftVal();
                inc += value.getInc().floatValue();
                inc = (float) MathUtils.round(inc, 1);
                if (!(inc > value.getRightVal().floatValue())) value.setLeftVal(inc);
            } else if (value.getLeftVal() instanceof Long) {
                long inc = (long) value.getLeftVal();
                inc += value.getInc().longValue();
                inc = (long) MathUtils.round(inc, 1);
                if (!(inc > value.getRightVal().longValue())) value.setLeftVal(inc);
            }
        } else {
            if (value.getRightVal() instanceof Integer) {
                int inc = (int) value.getRightVal();
                inc += value.getInc().intValue();
                inc = (int) MathUtils.round(inc, 1);
                if (!(inc > value.getMaximum().intValue())) value.setRightVal(inc);
            } else if (value.getRightVal() instanceof Double) {
                double inc = (double) value.getRightVal();
                inc += value.getInc().doubleValue();
                inc = MathUtils.round(inc, 1);
                if (!(inc > value.getMaximum().doubleValue())) value.setRightVal(inc);
            } else if (value.getRightVal() instanceof Float) {
                float inc = (float) value.getRightVal();
                inc += value.getInc().floatValue();
                inc = (float) MathUtils.round(inc, 1);
                if (!(inc > value.getMaximum().floatValue())) value.setRightVal(inc);
            } else if (value.getRightVal() instanceof Long) {
                long inc = (long) value.getRightVal();
                inc += value.getInc().longValue();
                inc = (long) MathUtils.round(inc, 1);
                if (!(inc > value.getMaximum().longValue())) value.setRightVal(inc);
            }
        }
    }
}