package de.hsbremen.kss.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.construction.Construction;

/**
 * The Class Plan.
 */
public final class Plan {

    /** logging interface. */
    private static final Logger LOG = LoggerFactory.getLogger(Plan.class);

    /** the implementation which generates the plan */
    private final Class<? extends Construction> constructionClazz;

    /** The tours. */
    private final List<Tour> tours;

    /** counter of the tour ids */
    private int tourIdCounter = 1;

    /**
     * Instantiates a new plan.
     * 
     * @param constructionClazz
     *            the implementation which generates the plan
     */
    public Plan(final Class<? extends Construction> constructionClazz) {
        this.constructionClazz = constructionClazz;
        this.tours = new ArrayList<>();
    }

    /**
     * copies a plan.
     * 
     * @param constructionClazz
     *            the implementation which generates the plan
     * @param plan
     *            plan to copy
     */
    public Plan(final Class<? extends Construction> constructionClazz, final Plan plan) {
        this.constructionClazz = constructionClazz;
        this.tours = new ArrayList<>(plan.tours);
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
        Plan.LOG.info(this.constructionClazz.getSimpleName() + " plan:");
        for (final Tour tour : this.tours) {
            tour.logTour();
        }
    }

    /**
     * returns a set of all reached stations
     * 
     * @return a set of all reached stations
     */
    public Collection<Station> getStations() {
        final Collection<Station> allReachedStations = new HashSet<>();

        for (final Tour tour : this.tours) {
            allReachedStations.addAll(tour.getStations());
        }

        return allReachedStations;
    }

    /**
     * logs the tour.
     */
    public void logPlan() {
        Plan.LOG.info(this.constructionClazz.getSimpleName() + " length [km]: " + Math.round(length()));
    }

    /**
     * finds the tour which is associated with the given order.
     * 
     * @param order
     *            order to search for
     * @return the tour which is associated with the given order or null
     */
    public Tour associatedTour(final Order order) {
        for (final Tour tour : this.tours) {
            if (tour.contains(order)) {
                return tour;
            }
        }
        return null;
    }

    /**
     * creates a new tour.
     * 
     * @param vehicle
     *            vehicle which should be used.
     * @return the new tour.
     */
    public Tour newTour(final Vehicle vehicle) {
        return new Tour(vehicle, this.tourIdCounter++);
    }

    public void addTours(final List<Tour> tours2) {
        for (final Tour tour : tours2) {
            addTour(tour);
        }
    }

}
