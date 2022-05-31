package ru.itmo.robq.comp_math_4.approximation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itmo.robq.comp_math_4.functions.ExponentialFunction;
import ru.itmo.robq.comp_math_4.functions.LinearFunction;
import ru.itmo.robq.comp_math_4.functions.PowerFunction;

@Component
public class PowerApproximation implements Approximation{
    private LinearApproximation linearApproximation;
    private ApproximationAnalyzer approximationAnalyzer;

    @Autowired
    public PowerApproximation(LinearApproximation linearApproximation, ApproximationAnalyzer approximationAnalyzer) {
        this.linearApproximation = linearApproximation;
        this.approximationAnalyzer = approximationAnalyzer;
    }
    @Override
    public ApproximationResults approximate(double[] xValues, double[] yValues) {
        int n = Math.min(xValues.length, yValues.length);
        double[] xValuesNew = new double[n];
        double[] yValuesNew = new double[n];
        for (int i = 0; i < n; i++) {
            if (yValues[i] > 0 && xValues[i] > 0) {
                xValuesNew[i] = Math.log(xValues[i]);
                yValuesNew[i] = Math.log(yValues[i]);
            } else {
                xValuesNew[i] = xValues[i];
                yValuesNew[i] = yValues[i];
            }
        }
        ApproximationResults results = linearApproximation.approximate(xValuesNew, yValuesNew);
        LinearFunction linearFunction = (LinearFunction) results.getFunction();
        PowerFunction powerFunction = new PowerFunction();
        powerFunction.setA(Math.exp(linearFunction.getB()));
        powerFunction.setB(linearFunction.getA());
        results.setFunction(powerFunction);
        results.setDeviation(approximationAnalyzer.calculateDeviation(xValues, yValues, powerFunction));
        results.setR2(approximationAnalyzer.calculateR2(xValues, yValues, powerFunction));
        results.setCorrelation(null);
        return results;
    }
}
