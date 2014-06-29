package de.hsbremen.kss.util;

/**
 * some time utils.
 * 
 * @author henrik
 * 
 */
public final class TimeUtils {

    /**
     * static class.
     */
    private TimeUtils() {

    }

    /**
     * convert a double time into a "real" clock string.
     * 
     * @param time
     *            time to convert
     * @return "real" clock string
     */
    public static String convertToClockString(final double time) {
        if (time == Double.MAX_VALUE) {
            return "inf";
        }

        if (time == Double.MIN_VALUE) {
            return "-inf";
        }

        final int hours = (int) time;
        final int minutes = (int) ((time - hours) * 60);
        return hours + ":" + minutes;
    }
}
