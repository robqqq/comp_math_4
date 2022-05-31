package ru.itmo.robq.comp_math_4.approximation;

import org.springframework.stereotype.Component;
import ru.itmo.robq.comp_math_4.functions.Function;

import java.util.Arrays;

@Component
public class ApproximationAnalyzer {
    public double calculateR2 (double[] xValues, double[] yValues, Function function) {
        int n = Math.min(xValues.length, yValues.length);
        double upper = 0;
        double lowerLeft = 0;
        double lowerRight = 0;
        for (int i = 0; i < n; i++) {
            upper += Math.pow(yValues[i] - function.apply(xValues[i]), 2);
            lowerLeft += Math.pow(function.apply(xValues[i]), 2);
            lowerRight += function.apply(xValues[i]);
        }
        double result = 1 - upper / (lowerLeft - Math.pow(lowerRight, 2) / n);
        if (Double.isNaN(result)) {
            result = 0;
        }
        return result;
    }

    public double calculateCorrelation(double[] xValues, double[] yValues) {
        int n = Math.min(xValues.length, yValues.length);
        double averageX = 0;
        double averageY = 0;
        for (int i = 0; i < n; i++) {
            averageX += xValues[i];
            averageY += yValues[i];
        }
        averageX /= n;
        averageY /= n;
        double upper = 0;
        double lowerLeft = 0;
        double lowerRight = 0;
        for (int i = 0; i < n; i++) {
            upper += (xValues[i] - averageX) * (yValues[i] - averageY);
            lowerLeft += Math.pow(xValues[i] - averageX, 2);
            lowerRight += Math.pow(yValues[i] - averageY, 2);
        }
        return upper / Math.sqrt(lowerLeft * lowerRight);
    }

    public double calculateDeviation(double[] xValues, double[] yValues, Function function) {
        int n = Math.min(xValues.length, yValues.length);
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum += Math.pow(function.apply(xValues[i]) - yValues[i], 2);
        }
        return Math.sqrt(sum / n);
    }
}
