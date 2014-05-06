package de.hsbremen.kss.configuration;

import java.util.Comparator;

/**
 * compares to stations by its angle.
 * 
 * @author henrik
 * 
 */
public final class StationAngleComparator implements Comparator<Station> {

    /** center station */
    private final Station center;

    /** start angle */
    private final Double startAngle;

    /** direction */
    private final boolean forward;

    /**
     * ctor.
     * 
     * @param center
     *            center station
     * @param startStation
     *            start station
     * @param forward
     *            direction
     */
    public StationAngleComparator(final Station center, final Station startStation, final boolean forward) {
        this.center = center;
        this.forward = forward;

        if (this.center.getCoordinates().equals(startStation.getCoordinates())) {
            this.startAngle = 0d;
        } else {
            this.startAngle = this.center.angle(startStation);
        }

    }

    @Override
    public int compare(final Station o1, final Station o2) {
        Double angle1;
        Double angle2;

        if (!this.center.equals(o1)) {
            angle1 = Double.valueOf(this.center.angle(o1));
            angle1 -= this.startAngle;
            if (angle1 < 0) {
                angle1 += Math.PI * 2;
            }
        } else {
            angle1 = 0d;
        }

        if (!this.center.equals(o2)) {
            angle2 = Double.valueOf(this.center.angle(o2));
            angle2 -= this.startAngle;
            if (angle2 < 0) {
                angle2 += Math.PI * 2;
            }
        } else {
            angle2 = 0d;
        }

        if (this.forward) {
            return angle1.compareTo(angle2);
        } else {
            return angle2.compareTo(angle1);
        }
    }

}
