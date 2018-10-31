package utils;

import java.util.ArrayList;
import java.util.Arrays;

public class Statistics {
    double[] data;
    int size;

    public Statistics(double[] data) {
        this.data = data;
        size = data.length;
    }

    public <T> Statistics(Counter<T> counter) {
        this(convert(counter.getMap().values().toArray(new Double[0])));
    }

    private static double[] convert(Double[] listamaledetta) {
        double[] arr = new double[listamaledetta.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = listamaledetta[i];
        }
        return arr;
    }

    public double max() {
        double max = data[0];
        for (double m : data) {
            if (m > max) max = m;
        }
        return max;
    }

    public double min() {
        double min = data[0];
        for (double m : data) {
            if (m < min) min = m;
        }
        return min;
    }

    public double sum() {
        double sum = 0.0;
        for(double a : data)
            sum += a;
        return sum;
    }

    public double mean() {
        return sum()/size;
    }

    public double variance() {
        double mean = mean();
        double temp = 0;
        for(double a :data)
            temp += (a-mean)*(a-mean);
        return temp/size;
    }

    public double stdDev() {
        return Math.sqrt(variance());
    }

    public double median() {
        Arrays.sort(data);
        if (data.length % 2 == 0)
            return (data[(data.length / 2) - 1] + data[data.length / 2]) / 2.0;
        return data[data.length / 2];
    }

    public String report() {
        return report("total",
                "unique",
                "max",
                "min",
                "mean",
                "median",
                "variance",
                "stddev");
    }

    public String report(
            String totalString,
            String uniqueString,
            String maxString,
            String minString,
            String meanString,
            String medianString,
            String varianceString,
            String stddevString) {
        return String.format("\t%s: %.3f\n", totalString, sum()) +
                String.format("\t%s: %d\n", uniqueString, size) +
                String.format("\t%s: %.3f\n", maxString, max()) +
                String.format("\t%s: %.3f\n", minString, min()) +
                String.format("\t%s: %.3f\n", meanString, mean()) +
                String.format("\t%s: %.3f\n", medianString, median()) +
                String.format("\t%s: %.3f\n", varianceString, variance()) +
                String.format("\t%s: %.3f\n", stddevString, stdDev());
    }
}