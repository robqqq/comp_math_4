package ru.itmo.robq.comp_math_4.functions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Polynomial2Function implements Function{
    private double a0;
    private double a1;
    private double a2;

    @Override
    public double apply(double x) {
        return a0 + a1 * x + a2 * Math.pow(x, 2);
    }

    @Override
    public String toString() {
        return String.format("%.2f + %.2f * x + %.2f * x^2", a0, a1, a2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Polynomial2Function that = (Polynomial2Function) o;

        if (Double.compare(that.a0, a0) != 0) return false;
        if (Double.compare(that.a1, a1) != 0) return false;
        return Double.compare(that.a2, a2) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(a0);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(a1);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(a2);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
