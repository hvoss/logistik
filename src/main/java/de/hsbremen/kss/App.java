package de.hsbremen.kss;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.ConfigurationParser;
import de.hsbremen.kss.configuration.JAXBConfigurationParserImpl;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.construction.Construction;
import de.hsbremen.kss.construction.NearestNeighbor;
import de.hsbremen.kss.construction.RadialConstruction;
import de.hsbremen.kss.construction.RandomConstruction;
import de.hsbremen.kss.construction.SavingsContruction;
import de.hsbremen.kss.construction.TestNearestNeighbor;
import de.hsbremen.kss.model.Plan;
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

        final Configuration configuration = confParser.parseConfiguration(file);

        App.LOG.info("got " + configuration.getStations().size() + " stations");
        App.LOG.info("got " + configuration.getVehicles().size() + " vehicles");
        App.LOG.info("got " + configuration.getOrders().size() + " orders");
        App.LOG.info("got " + configuration.getProducts().size() + " products");
        App.LOG.info("got " + configuration.getProductGroups().size() + " product groups");

        App.LOG.info("stations: " + configuration.getStations());
        App.LOG.info("vehicles: " + configuration.getVehicles());
        App.LOG.info("orders: " + configuration.getOrders());
        App.LOG.info("products: " + configuration.getProducts());
        App.LOG.info("product groups: " + configuration.getProductGroups());

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
        final Construction testNearestNeighbor = new TestNearestNeighbor();
        final Construction randomConstruction = new RandomConstruction();
        final Construction radialConstruction = new RadialConstruction();

        final Plan plan1 = nearestNeighbor.constructPlan(configuration);
        final Plan savingsPlan = savingsContruction.constructPlan(configuration);
        final Plan nearestNeighborPlan = testNearestNeighbor.constructPlan(configuration);
        final Plan radialPlan = radialConstruction.constructPlan(configuration);

        Plan bestRandomPlan = null;
        for (int i = 0; i < App.NUM_OF_RANDOM_PLANS; i++) {
            final Plan randomPlan = randomConstruction.constructPlan(configuration);
            if (bestRandomPlan == null || bestRandomPlan.length() > randomPlan.length()) {
                bestRandomPlan = randomPlan;
            }
        }
        final List<Plan> allPlans = Arrays.asList(savingsPlan, nearestNeighborPlan, radialPlan, bestRandomPlan);

        for (final Plan plan : allPlans) {
            plan.logPlan();
            App.LOG.info("plan is valid: " + validator.validate(configuration, plan));
            plan.logTours();
        }

    }

}
