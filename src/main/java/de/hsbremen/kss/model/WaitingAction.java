package de.hsbremen.kss.model;

import org.apache.commons.lang3.Validate;

import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.TimeWindow;
import de.hsbremen.kss.util.TimeUtils;

public final class WaitingAction implements Action {

    private final Station station;

    private final double startTime;

    private final double endTime;

    private final TimeWindow timewindow;

    public WaitingAction(final Station station, final TimeWindow timewindow, final double startTime, final double endTime) {
        Validate.notNull(station, "station is null");
        Validate.notNull(timewindow, "timewindow is null");
        Validate.isTrue(startTime > 0, "start time must be greater than 0");
        Validate.isTrue(endTime > 0, "end time must be greater than 0");
        Validate.isTrue(endTime > startTime, "end time must be greater than start time");

        this.station = station;
        this.timewindow = timewindow;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public Station getStation() {
        return this.station;
    }

    @Override
    public double duration() {
        return this.endTime - this.startTime;
    }

    @Override
    public TimeWindow timewindow() {
        return this.timewindow;
    }

    @Override
    public String toString() {
        return "wainting in " + this.station + " from to " + TimeUtils.convertToClockString(this.startTime) + " to "
                + TimeUtils.convertToClockString(this.endTime);
    }
}
