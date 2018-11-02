package utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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
                "size",
                "max",
                "min",
                "mean",
                "median",
                "variance",
                "stddev");
    }

    public String report(
            String totalString,
            String sizeString,
            String maxString,
            String minString,
            String meanString,
            String medianString,
            String varianceString,
            String stddevString) {
        NumberFormat nf = new DecimalFormat("##.####");

        return String.format("\t%s: %s\n", totalString, nf.format(sum())) +
                String.format("\t%s: %s\n", sizeString, nf.format(size)) +
                String.format("\t%s: %s\n", maxString, nf.format(max())) +
                String.format("\t%s: %s\n", minString, nf.format(min())) +
                String.format("\t%s: %s\n", meanString, nf.format(mean())) +
                String.format("\t%s: %s\n", medianString, nf.format(median())) +
                String.format("\t%s: %s\n", varianceString, nf.format(variance())) +
                String.format("\t%s: %s\n", stddevString, nf.format(stdDev()));
    }
}