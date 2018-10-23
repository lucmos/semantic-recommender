package utils;

import org.apache.commons.lang3.StringUtils;

import static java.lang.Math.round;

public class Chrono {
    private static final String defaultInitialMessage = "Running...";
    private static final String defaultFinalMessage = "done (in %s %s)";

    private static int nestedChronos;
    private static int nestedNewLines;

    private int positionChrono;

    private String initialMessage;
    private String finalMessage;
    private boolean verbose;

    private long startTime;

    public Chrono(String initialMessage, String finalMessage, boolean verbose, boolean start) {
        this.initialMessage = initialMessage;
        this.finalMessage = finalMessage;
        this.verbose = verbose;

        this.positionChrono = nestedChronos;

        if (start) {
            start();
        }
    }

    public Chrono(String initialMessage) {
        this(initialMessage, defaultFinalMessage, true, true);
    }

    public Chrono() {
        this(defaultInitialMessage, defaultFinalMessage, true, true);
    }

    public Chrono quiet() {
        this.verbose = false;
        return this;
    }

    public Chrono verbose() {
        this.verbose = true;
        return this;
    }

    public void seconds(String message) {
        printFinalMessage(message, getSeconds(), "seconds");

        stop();
    }

    public void millis(String message) {
        printFinalMessage(message, getMillis(), "millis");

        stop();
    }


    public void seconds() {
        seconds(defaultFinalMessage);
    }

    public void millis() {
        millis(defaultFinalMessage);
    }

    private void start() {
        printInitialMessage();

        this.startTime = System.currentTimeMillis();
        nestedChronos++;
        nestedNewLines++;
    }

    private void stop() {
        nestedChronos--;
        if (nestedNewLines > 0) {
            nestedNewLines--;
        }
    }

    private long getMillis() {
        return System.currentTimeMillis() - startTime;
    }

    private long getSeconds() {
        return round(getMillis() / 1000.0);
    }

    private void printInitialMessage() {
        if (verbose && initialMessage != null) {
            System.out.print(buildStartMessage());
        }
    }

    private void printFinalMessage(String customMessage, long elapsed, String unit) {
        if (verbose && finalMessage != null) {
            System.out.println(buildFinalMesage(customMessage, elapsed, unit));
        }
    }

    private String buildStartMessage() {
        return startPrefix() + initialMessage;
    }


    private String buildFinalMesage(String message, long elapsed, String unit) {
        return finalPrefix() + String.format(message, elapsed, unit) + finalPostfix();
    }

    private String startPrefix() {
        StringBuilder prefix = new StringBuilder();
        if (nestedNewLines > 0) {
            prefix.append("\n");
            nestedNewLines--;
        }
        prefix.append(StringUtils.repeat("\t", nestedChronos));
        return prefix.toString();
    }

    private String finalPrefix() {
        return (positionChrono == nestedChronos) ? "\t" : StringUtils.repeat("\t", positionChrono);
    }

    private String finalPostfix() {
        return (positionChrono == 0 ? "\n" : "");
    }
}
