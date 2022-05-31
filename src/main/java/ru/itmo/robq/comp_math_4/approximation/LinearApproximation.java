package ru.itmo.robq.comp_math_4.approximation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itmo.robq.comp_math_4.functions.LinearFunction;

@Component
public class LinearApproximation implements Approximation{
    private ApproximationAnalyzer approximationAnalyzer;

    @Autowired
    public LinearApproximation(ApproximationAnalyzer approximationAnalyzer) {
        this.approximationAnalyzer = approximationAnalyzer;
    }


    @Override
    public ApproximationResults approximate(double[] xValues, double[] yValues) {
        double x = 0;
        double x2 = 0;
        double y = 0;
        double xy = 0;
        int n = Math.min(xValues.length, yValues.length);
        for (int i = 0; i < n; i++) {
            x += xValues[i];
            x2 += Math.pow(xValues[i], 2);
            y += yValues[i];
            xy += xValues[i] * yValues[i];
        }
        double[][] left = new double[][] {
                {x2, x},
                {x, n}};
        double[] right = new double[]{
                xy, y
        };
        DecompositionSolver solver = new LUDecomposition(new Array2DRowRealMatrix(left)).getSolver();
        double[] solution = solver.solve(new ArrayRealVector(right)).toArray();
        LinearFunction function = new LinearFunction();
        function.setA(solution[0]);
        function.setB(solution[1]);
        return ApproximationResults
                .builder()
                .function(function)
                .deviation(approximationAnalyzer.calculateDeviation(xValues, yValues, function))
                .r2(approximationAnalyzer.calculateR2(xValues, yValues, function))
                .correlation(approximationAnalyzer.calculateCorrelation(xValues, yValues))
                .build();
    }
}
