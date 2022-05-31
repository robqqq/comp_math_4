package ru.itmo.robq.comp_math_4.functions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExponentialFunction implements Function{
    private double a;
    private double b;

    @Override
    public double apply(double x) {
        return a * Math.exp(b * x);
    }

    @Override
    public String toString() {
        return String.format("%.2f * e^(%.2f * x)", a, b);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExponentialFunction that = (ExponentialFunction) o;

        if (Double.compare(that.a, a) != 0) return false;
        return Double.compare(that.b, b) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(a);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(b);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
