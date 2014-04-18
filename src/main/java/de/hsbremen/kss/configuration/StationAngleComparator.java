package de.hsbremen.kss.configuration;

import java.util.Comparator;

/**
 * compares to stations by its angle.
 * 
 * @author henrik
 * 
 */
public final class StationAngleComparator implements Comparator<Station> {

    /**
     * center station
     */
    private final Station center;

    /**
     * ctor.
     * 
     * @param center
     *            center station
     */
    public StationAngleComparator(final Station center) {
        this.center = center;
    }

    @Override
    public int compare(final Station o1, final Station o2) {
        final Double angle1 = Double.valueOf(this.center.angle(o1));
        final Double angle2 = Double.valueOf(this.center.angle(o2));
        return angle1.compareTo(angle2);
    }

}
