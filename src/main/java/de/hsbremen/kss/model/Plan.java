package de.hsbremen.kss.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.construction.Construction;

/**
 * The Class Plan.
 */
public final class Plan {

    /** logging interface. */
    private static final Logger LOG = LoggerFactory.getLogger(Plan.class);

    /** The tours. */
    private final List<Tour> tours;

    /**
     * Instantiates a new plan.
     */
    public Plan() {
        this.tours = new ArrayList<>();
    }

    /**
     * adds a tour.
     * 
     * @param tour
     *            tour to add.
     */
    public void addTour(final Tour tour) {
        Validate.notNull(tour, "tour is null");
        this.tours.add(tour);
    }

    /**
     * Gets the tours.
     * 
     * @return the tours
     */
    public List<Tour> getTours() {
        return Collections.unmodifiableList(this.tours);
    }

    /**
     * calculates the length of the plan.
     * 
     * @return length of the plan.
     */
    public double length() {
        double length = 0;

        for (final Tour tour : this.tours) {
            length += tour.length();
        }

        return length;
    }

    /**
     * log all tours.
     */
    public void logTours() {
        for (final Tour tour : this.tours) {
            tour.logTour();
        }
    }

    /**
     * logs the tour.
     * 
     * @param constructionClazz
     *            algorithm which used for generating
     */
    public void logPlan(final Class<? extends Construction> constructionClazz) {
        Plan.LOG.info(constructionClazz.getSimpleName() + " length [km]: " + Math.round(length()));
    }

}
