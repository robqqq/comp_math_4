package ru.itmo.robq.comp_math_4.approximation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itmo.robq.comp_math_4.functions.ExponentialFunction;
import ru.itmo.robq.comp_math_4.functions.LinearFunction;
import ru.itmo.robq.comp_math_4.functions.LogarithmicFunction;

@Component
public class ExponentialApproximation implements Approximation{
    private LinearApproximation linearApproximation;
    private ApproximationAnalyzer approximationAnalyzer;

    @Autowired
    public ExponentialApproximation(LinearApproximation linearApproximation, ApproximationAnalyzer approximationAnalyzer) {
        this.linearApproximation = linearApproximation;
        this.approximationAnalyzer = approximationAnalyzer;
    }
    @Override
    public ApproximationResults approximate(double[] xValues, double[] yValues) {
        int n = Math.min(xValues.length, yValues.length);
        double[] xValuesNew = new double[n];
        double[] yValuesNew = new double[n];
        for (int i = 0; i < n; i++) {
            yValuesNew[i] = yValues[i] > 0 ? Math.log(yValues[i]) : yValues[i];
            xValuesNew[i] = xValues[i];
        }
        ApproximationResults results = linearApproximation.approximate(xValuesNew, yValuesNew);
        LinearFunction linearFunction = (LinearFunction) results.getFunction();
        ExponentialFunction exponentialFunction = new ExponentialFunction();
        exponentialFunction.setA(Math.exp(linearFunction.getB()));
        exponentialFunction.setB(linearFunction.getA());
        results.setFunction(exponentialFunction);
        results.setDeviation(approximationAnalyzer.calculateDeviation(xValues, yValues, exponentialFunction));
        results.setR2(approximationAnalyzer.calculateR2(xValues, yValues, exponentialFunction));
        results.setCorrelation(null);
        return results;
    }
}
