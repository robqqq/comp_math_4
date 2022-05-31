package ru.itmo.robq.comp_math_4.approximation;

public interface Approximation {
    ApproximationResults approximate(double[] xValues, double[] yValues);
}
