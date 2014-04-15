package de.hsbremen.kss.timing;

import org.joda.time.Instant;
import org.joda.time.Interval;

/**
 * class for measuring the runtime of a task.
 * 
 * @author henrik
 * 
 */
public abstract class TimeMeasuring {

    /**
     * the start time.
     */
    private Instant start;

    /**
     * the end time.
     */
    private Instant end;

    /**
     * ctor.
     * 
     */
    public TimeMeasuring() {
    }

    /**
     * runs the task.
     */
    public final void runTask() {
        if (this.start != null) {
            throw new IllegalStateException("task already started.");
        }

        this.start = new Instant();
        run();
        this.end = new Instant();
    }

    /**
     * returns the duration of the task.
     * 
     * @return the duration of the task
     */
    public final long duration() {
        final Interval interval = new Interval(this.start, this.end);
        return interval.toDurationMillis();
    }

    protected abstract void run();
}
