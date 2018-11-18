package utils;

import org.apache.commons.lang3.StringUtils;

import static java.lang.Math.round;

/**
 * Chrono class manages the time and use it during a method execution allow to use a chronometer.
 */
public class Chrono {
    public static final String defaultInitialMessage = "Running...";
    public static final String defaultFinalMessage = "done (in %s %s)";

    private static int nestedChronos;
    private static int nestedNewLines;

    private int positionChrono;

    private String initialMessage;
    private String finalMessage;
    private boolean verbose;

    private long startTime;

    /**
     * The constructor
     * @param initialMessage is printed when a Chrono starts to count the time
     * @param finalMessage is printed when a Chrono finish to count the time
     * @param verbose  says if the Chrono should print something or not
     * @param start say if the Chrono should start togheter with its creation
     */
    public Chrono(String initialMessage, String finalMessage, boolean verbose, boolean start) {
        this.initialMessage = initialMessage;
        this.finalMessage = finalMessage;
        this.verbose = verbose;

        this.positionChrono = nestedChronos;

        if (start) {
            start();
        }
    }

    /**
     * Creates a Chrono which immediately starts and can print messages
     * @param initialMessage is printed when a Chrono finish to count the time
     */
    public Chrono(String initialMessage) {
        this(initialMessage, defaultFinalMessage, true, true);
    }

    /**
     * Creates a Chrono which immediately starts and can print messages
     */
    public Chrono() {
        this(defaultInitialMessage, defaultFinalMessage, true, true);
    }

    /**
     * The Chrono can't print anything anymore
     * @return
     */
    public Chrono quiet() {
        this.verbose = false;
        return this;
    }

    /**
     * The Chrono, from this point on, can print messages
     * @return
     */
    public Chrono verbose() {
        this.verbose = true;
        return this;
    }

    /**
     * The Chronos prints how many seconds has been active untill now and stops count
     * @param message is printed in coomunicating the time
     */
    public void seconds(String message) {
        printFinalMessage(message, getSeconds(), "seconds");

        stop();
    }

    /**
     * The Chronos prints how many milliseconds has been active untill now and stops count
     * @param message is printed in coomunicating the time
     */
    public void millis(String message) {
        printFinalMessage(message, getMillis(), "millis");

        stop();
    }

    /**
     * The Chronos prints how many seconds has been active untill now
     */
    public void seconds() {
        seconds(defaultFinalMessage);
    }

    /**
     * The Chronos prints how many milliseconds has been active untill now
     */
    public void millis() {
        millis(defaultFinalMessage);
    }

    /**
     * The Chrono starts counting
     */
    private void start() {
        printInitialMessage();

        this.startTime = System.currentTimeMillis();
        nestedChronos++;
        nestedNewLines++;
    }

    /**
     * The Chrono stops counting
     */
    private void stop() {
        nestedChronos--;
        if (nestedNewLines > 0) {
            nestedNewLines--;
        }
    }

    /**
     * The Chrono tells how many milliseconds has been pass from its start
     * @return the number of milliseconds
     */
    private long getMillis() {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * The Chrono tells how many seconds has been pass from its start
     * @return the number of seconds
     */
    private long getSeconds() {
        return round(getMillis() / 1000.0);
    }

    /**
     * Prints the start message
     */
    private void printInitialMessage() {
        if (verbose && initialMessage != null) {
            System.out.print(buildStartMessage());
        }
    }

    /**
     * Print the final message
     * @param customMessage the message to print
     * @param elapsed the amount of time that has been spent
     * @param unit the unit of measure time type
     */
    private void printFinalMessage(String customMessage, long elapsed, String unit) {
        if (verbose && finalMessage != null) {
            System.out.println(buildFinalMesage(customMessage, elapsed, unit));
        }
    }

    /**
     * builds the initial message according to the Chrono object characteristics
     * @return the built message
     */
    private String buildStartMessage() {
        return startPrefix() + initialMessage;
    }


    /**
     * builds the final message according to the Chrono object characteristics
     * @param message the desired message
     * @param elapsed te amount of time that has been spent
     * @param unit the unit of measure time type
     * @return the built message
     */
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
        return (nestedNewLines == 0) ? StringUtils.repeat("\t", positionChrono) : "\t";
    }

    private String finalPostfix() {
        return "";
    }
}
