package ru.itmo.robq.comp_math_4.approximation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itmo.robq.comp_math_4.functions.Function;
import ru.itmo.robq.comp_math_4.functions.LinearFunction;
import ru.itmo.robq.comp_math_4.functions.LogarithmicFunction;

import java.util.Arrays;

@Component
public class LogarithmicApproximation implements Approximation {
    private LinearApproximation linearApproximation;
    private ApproximationAnalyzer approximationAnalyzer;

    @Autowired
    public LogarithmicApproximation(LinearApproximation linearApproximation, ApproximationAnalyzer approximationAnalyzer) {
        this.linearApproximation = linearApproximation;
        this.approximationAnalyzer = approximationAnalyzer;
    }

    @Override
    public ApproximationResults approximate(double[] xValues, double[] yValues) {
        int n = Math.min(xValues.length, yValues.length);
        double[] xValuesNew = new double[n];
        double[] yValuesNew = new double[n];
        for (int i = 0; i < n; i++) {
            xValuesNew[i] = xValues[i] > 0 ? Math.log(xValues[i]) : xValues[i];
            yValuesNew[i] = yValues[i];
        }
        ApproximationResults results = linearApproximation.approximate(xValuesNew, yValuesNew);
        LinearFunction linearFunction = (LinearFunction) results.getFunction();
        LogarithmicFunction logarithmicFunction = new LogarithmicFunction();
        logarithmicFunction.setA(linearFunction.getA());
        logarithmicFunction.setB(linearFunction.getB());
        results.setFunction(logarithmicFunction);
        results.setDeviation(approximationAnalyzer.calculateDeviation(xValues, yValues, logarithmicFunction));
        results.setR2(approximationAnalyzer.calculateR2(xValues, yValues, logarithmicFunction));
        results.setCorrelation(null);
        return results;
    }
}
