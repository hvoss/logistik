package de.hsbremen.kss.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Product;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.construction.Construction;
import de.hsbremen.kss.genetic.fitness.FitnessTest;
import de.hsbremen.kss.genetic.mutation.MoveSubrouteMutation;
import de.hsbremen.kss.validate.Validator;

/**
 * The Class Plan.
 */
public final class Plan {

    /** logging interface. */
    private static final Logger LOG = LoggerFactory.getLogger(Plan.class);

    /** the implementation which generates the plan */
    private final Class<?> constructionClazz;

    /** The tours. */
    private final List<Tour> tours;

    /** counter of the tour ids */
    private int tourIdCounter = 1;

    private final Map<Validator, Boolean> valid = new IdentityHashMap<>();

    private final Map<FitnessTest, Double> fitness = new IdentityHashMap<>();

    /**
     * indicates whether the plan is locked or not. A locked plan can't be
     * modified.
     */
    private boolean locked = false;

    /**
     * Instantiates a new plan.
     * 
     * @param constructionClazz
     *            the implementation which generates the plan
     */
    public Plan(final Class<?> constructionClazz) {
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
        checkLocked();
        Validate.notNull(tour, "tour is null");

        if (tour.getOrderActions().isEmpty()) {
            throw new IllegalStateException();
        }

        this.tours.add(new Tour(tour, this.tourIdCounter++));

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
     * adds a collection of tours to the plan.
     * 
     * @param toursToAdd
     *            tours to add.
     */
    public void addTours(final Iterable<Tour> toursToAdd) {
        checkLocked();
        for (final Tour tour : toursToAdd) {
            addTour(tour);
        }
    }

    /**
     * locks the plan. no further modifying is possible.
     */
    public void lock() {
        checkLocked();
        this.locked = true;
    }

    /**
     * checks whether the plan is locked or not. if the plan is locked, a
     * exception will be thrown.
     */
    private void checkLocked() {
        if (this.locked) {
            throw new IllegalStateException("the plan is locked.");
        }
    }

    public Boolean getValid(final Validator validator) {
        return this.valid.get(validator);
    }

    public void setValid(final Validator validator, final Boolean valid) {
        this.valid.put(validator, valid);
    }

    public Double getFitness(final FitnessTest fitnessTest) {
        return this.fitness.get(fitnessTest);
    }

    public void setFitness(final FitnessTest fitnessTest, final Double fitness) {
        this.fitness.put(fitnessTest, fitness);
    }

    public double waitingTime() {
        double waitingTime = 0.0;

        for (final Tour tour : this.tours) {
            waitingTime += tour.waitingTime();
        }

        return waitingTime;
    }

    public double delayTime() {
        double delayTime = 0.0;

        for (final Tour tour : this.tours) {
            delayTime += tour.delayTime();
        }

        return delayTime;
    }

    public Set<Vehicle> usedVehicles() {
        final Set<Vehicle> usedVehicles = new HashSet<>();

        for (final Tour tour : this.tours) {
            usedVehicles.add(tour.getVehicle());
        }

        return usedVehicles;
    }

    public List<Tour> filterToursByProducts(final Set<Product> products) {
        final List<Tour> filteredTours = new ArrayList<>();

        for (final Tour tour : this.tours) {
            if (products.contains(tour.getVehicle().getProduct())) {
                filteredTours.add(tour);
            }
        }

        return filteredTours;
    }

    public Map<Product, List<Tour>> toursByProduct() {
        final Map<Product, List<Tour>> toursByProduct = new HashMap<>();
        for (final Tour tour : this.tours) {
            final Product product = tour.getVehicle().getProduct();
            List<Tour> list = toursByProduct.get(product);
            if (list == null) {
                list = new ArrayList<>();
                toursByProduct.put(product, list);
            }
            list.add(tour);
        }
        return toursByProduct;
    }

    public boolean maybeInvalid() {
        return this.constructionClazz == MoveSubrouteMutation.class;
    }

}
