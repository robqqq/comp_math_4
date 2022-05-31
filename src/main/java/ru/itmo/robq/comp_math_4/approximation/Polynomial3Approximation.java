package ru.itmo.robq.comp_math_4.approximation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itmo.robq.comp_math_4.functions.Polynomial2Function;
import ru.itmo.robq.comp_math_4.functions.Polynomial3Function;

@Component
public class Polynomial3Approximation implements Approximation{
    private ApproximationAnalyzer approximationAnalyzer;

    @Autowired
    public Polynomial3Approximation(ApproximationAnalyzer approximationAnalyzer) {
        this.approximationAnalyzer = approximationAnalyzer;
    }

    @Override
    public ApproximationResults approximate(double[] xValues, double[] yValues) {
        double x = 0;
        double x2 = 0;
        double x3 = 0;
        double x4 = 0;
        double x5 = 0;
        double x6 = 0;
        double y = 0;
        double xy = 0;
        double x2y = 0;
        double x3y = 0;
        int n = Math.min(xValues.length, yValues.length);
        for (int i = 0; i < n; i++) {
            x += xValues[i];
            x2 += Math.pow(xValues[i], 2);
            x3 += Math.pow(xValues[i], 3);
            x4 += Math.pow(xValues[i], 4);
            x5 += Math.pow(xValues[i], 5);
            x6 += Math.pow(xValues[i], 6);
            y += yValues[i];
            xy += xValues[i] * yValues[i];
            x2y += Math.pow(xValues[i], 2) * yValues[i];
            x3y += Math.pow(xValues[i], 3) * yValues[i];
        }
        double[][] left = new double[][]{
                {n, x, x2, x3},
                {x, x2, x3, x4},
                {x2, x3, x4, x5},
                {x3, x4, x5, x6}
        };
        double[] right = new double[]{
                y, xy, x2y, x3y
        };
        DecompositionSolver solver = new LUDecomposition(new Array2DRowRealMatrix(left)).getSolver();
        double[] solution = solver.solve(new ArrayRealVector(right)).toArray();
        Polynomial3Function function = new Polynomial3Function();
        function.setA0(solution[0]);
        function.setA1(solution[1]);
        function.setA2(solution[2]);
        function.setA3(solution[3]);
        return ApproximationResults
                .builder()
                .function(function)
                .deviation(approximationAnalyzer.calculateDeviation(xValues, yValues, function))
                .r2(approximationAnalyzer.calculateR2(xValues, yValues, function))
                .correlation(null)
                .build();
    }
}
