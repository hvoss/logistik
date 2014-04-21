package de.hsbremen.kss;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.Instant;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.ConfigurationParser;
import de.hsbremen.kss.configuration.JAXBConfigurationParserImpl;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.construction.Construction;
import de.hsbremen.kss.construction.FixMultipleRandomConstruction;
import de.hsbremen.kss.construction.MissAbortMultipleRandomConstruction;
import de.hsbremen.kss.construction.MultipleRadialConstruction;
import de.hsbremen.kss.construction.NearestNeighbor;
import de.hsbremen.kss.construction.RadialConstruction;
import de.hsbremen.kss.construction.RandomConstruction;
import de.hsbremen.kss.construction.SavingsContruction;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.timing.ConstructionTimeMeasuring;
import de.hsbremen.kss.validate.SimpleValidator;
import de.hsbremen.kss.validate.Validator;

/**
 * Hello world!
 * 
 */
public final class App {

    /** logging interface */
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    /** number of random plans to generate. */
    private static final int NUM_OF_RANDOM_PLANS = 2000;

    /** number of random plans to generate. */
    private static final int MAX_MISSES = 2000;

    /**
     * static class
     */
    private App() {

    }

    /**
     * the main functions
     * 
     * @param args
     *            the arguments
     */
    public static void main(final String[] args) {
        App.LOG.info("App started");

        final ConfigurationParser confParser = new JAXBConfigurationParserImpl();

        final File file = new File("conf.xml");

        final Instant start = new Instant();
        final Configuration configuration = confParser.parseConfiguration(file);
        final long durationMillis = new Interval(start, new Instant()).toDurationMillis();
        App.LOG.info("configuration parsing took " + durationMillis + " ms");

        App.LOG.info("got " + configuration.getStations().size() + " stations");
        App.LOG.info("got " + configuration.getVehicles().size() + " vehicles");
        App.LOG.info("got " + configuration.getOrders().size() + " orders");
        App.LOG.info("got " + configuration.getProducts().size() + " products");

        App.LOG.info("stations: " + configuration.getStations());
        App.LOG.info("vehicles: " + configuration.getVehicles());
        App.LOG.info("orders: " + configuration.getOrders());
        App.LOG.info("products: " + configuration.getProducts());

        Station.logDistancesBetweenStations(configuration.getStations());

        for (final Order order : configuration.getOrders()) {
            App.LOG.info(order.getName() + ": " + order.getProducts());
        }

        for (final Station station : configuration.getStations()) {
            if (!station.getSourceProducts().isEmpty()) {
                App.LOG.info(station.getName() + ": " + station.getSourceProducts().toString());
            }
        }

        final Validator validator = new SimpleValidator();

        final Construction nearestNeighbor = new NearestNeighbor();
        final Construction savingsContruction = new SavingsContruction();
        final Construction randomConstruction = new RandomConstruction();
        final Construction radialConstruction = new RadialConstruction();
        final Construction missAbortMultipleRandomConstruction = new MissAbortMultipleRandomConstruction(randomConstruction, App.MAX_MISSES);
        final Construction fixMultipleRandomConstruction = new FixMultipleRandomConstruction(randomConstruction, App.NUM_OF_RANDOM_PLANS);
        final Construction multipleRadialConstruction = new MultipleRadialConstruction();

        final List<Construction> allConstructions = Arrays.asList(nearestNeighbor, savingsContruction, radialConstruction,
                multipleRadialConstruction, randomConstruction, fixMultipleRandomConstruction, missAbortMultipleRandomConstruction);

        final ArrayList<ConstructionTimeMeasuring> timeMeasuringTasks = new ArrayList<>(allConstructions.size());

        for (final Construction construction : allConstructions) {
            final ConstructionTimeMeasuring timeMeasuring = new ConstructionTimeMeasuring(construction, configuration);
            timeMeasuring.runTask();
            timeMeasuringTasks.add(timeMeasuring);
        }

        for (final ConstructionTimeMeasuring timeMeasuring : timeMeasuringTasks) {
            App.LOG.info("");
            final Plan plan = timeMeasuring.getPlan();
            timeMeasuring.getPlan().logPlan();
            timeMeasuring.getConstruction().logStatistic();
            App.LOG.info("construction took " + timeMeasuring.duration() + " ms");
            App.LOG.info("plan is valid: " + validator.validate(configuration, plan));
            plan.logTours();
        }

    }
}
