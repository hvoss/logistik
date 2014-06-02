package de.hsbremen.kss;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.ConfigurationGenerator;
import de.hsbremen.kss.configuration.ConfigurationParser;
import de.hsbremen.kss.configuration.ConfigurationValidator;
import de.hsbremen.kss.configuration.JAXBConfigurationParserImpl;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.construction.CloneableConstruction;
import de.hsbremen.kss.construction.Construction;
import de.hsbremen.kss.construction.FixMultipleConstruction;
import de.hsbremen.kss.construction.MissAbortMultipleConstruction;
import de.hsbremen.kss.construction.MultipleSavingsConstruction;
import de.hsbremen.kss.construction.MultipleSavingsTourConstruction;
import de.hsbremen.kss.construction.MultipleSweepConstruction;
import de.hsbremen.kss.construction.MultithreadingConstruction;
import de.hsbremen.kss.construction.NearestNeighbor;
import de.hsbremen.kss.construction.RandomConstruction;
import de.hsbremen.kss.construction.SavingsContruction;
import de.hsbremen.kss.construction.SavingsTourConstruction;
import de.hsbremen.kss.construction.SweepConstruction;
import de.hsbremen.kss.fitness.FitnessTest;
import de.hsbremen.kss.fitness.SimpleFitnessTest;
import de.hsbremen.kss.genetic.GeneticAlgorithm;
import de.hsbremen.kss.genetic.GeneticAlgorithmImpl;
import de.hsbremen.kss.gui.MainFrame;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.simpleconstruction.PerfectSimpleConstruction;
import de.hsbremen.kss.simpleconstruction.RandomSimpleConstruction;
import de.hsbremen.kss.simpleconstruction.SimpleConstruction;
import de.hsbremen.kss.timing.ConstructionTimeMeasuring;
import de.hsbremen.kss.util.RandomUtils;
import de.hsbremen.kss.validate.SimpleValidator;
import de.hsbremen.kss.validate.Validator;

/**
 * Starts the program
 * 
 * @author henrik
 */
public final class App {

    /** logging interface */
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    /** number of random plans to generate. */
    private static final int NUM_OF_RANDOM_PLANS = 2000;

    /** number of random plans to generate. */
    private static final int MAX_MISSES = 50;

    private static MainFrame mainFrame;

    private static RandomUtils randomUtils = new RandomUtils(0);;

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

        final Configuration configuration = loadConfiguration();

        final ConfigurationGenerator configurationGenerator = new ConfigurationGenerator(App.randomUtils);

        List<Station> stations = configurationGenerator.generateStations(-300, 300, -300, 300, 100);
        
        final Configuration genConfig = configurationGenerator.generateConfiguration(stations, configuration.getProducts(),
                configuration.getVehicles(), 50);

        // final Plan plan = startAlgorithms(genConfig);

        final List<Plan> randomPlans = generateRandomPlans(genConfig, 100);

        System.out.println(randomPlans);

        for (final Plan plan : randomPlans) {
            plan.logPlan();
            plan.logTours();
        }

        final GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithmImpl(configuration, randomPlans);

        final Plan plan = geneticAlgorithm.startOptimize();

        // final FitnessTest fitnessTest = new SimpleFitnessTest(configuration);
        //
        // fitnessTest.calculateFitness(plan);
        startGUI(configuration, plan);

    }

    /**
     * starts the GUI.
     * 
     * @param configuration
     *            parsed configuration
     */
    private static void startGUI(final Configuration configuration, final Plan plan) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                App.mainFrame = new MainFrame(configuration, plan);
            }
        });
    }

    /**
     * start the algorithms.
     * 
     * @param configuration
     *            parsed configuration
     * @return the best plan
     */
    private static Plan startAlgorithms(final Configuration configuration) {
        Plan bestPlan = null;

        App.LOG.info("got " + configuration.getStations().size() + " stations");
        App.LOG.info("got " + configuration.getVehicles().size() + " vehicles");
        App.LOG.info("got " + configuration.getOrders().size() + " orders");
        App.LOG.info("got " + configuration.getProducts().size() + " products");

        App.LOG.info("stations: " + configuration.getStations());
        App.LOG.info("vehicles: " + configuration.getVehicles());
        App.LOG.info("orders: " + configuration.getOrders());
        App.LOG.info("products: " + configuration.getProducts());

        final Vehicle firstVehicle = CollectionUtils.get(configuration.getVehicles(), 0);

        Station.logDistancesBetweenStations(firstVehicle, configuration.getStations());

        for (final Order order : configuration.getOrders()) {
            App.LOG.info(order.getName() + ": " + order.getProduct());
        }

        for (final Station station : configuration.getStations()) {
            if (!station.getSourceProducts().isEmpty()) {
                App.LOG.info(station.getName() + ": " + station.getSourceProducts().toString());
            }
        }

        final Validator validator = new SimpleValidator();
        final ConfigurationValidator configurationValidator = new ConfigurationValidator();

        configurationValidator.validate(configuration);

        final FitnessTest fitnessTest = new SimpleFitnessTest(configuration);

        final SimpleConstruction simpleConstruction = new PerfectSimpleConstruction();

        final Construction nearestNeighbor = new NearestNeighbor(simpleConstruction, App.randomUtils);
        final Construction savingsContruction = new SavingsContruction();
        final Construction savingsTourConstruction = new SavingsTourConstruction();
        final Construction randomConstruction = new RandomConstruction(simpleConstruction, App.randomUtils);
        final Construction radialConstruction = new SweepConstruction(simpleConstruction, App.randomUtils);
        final Construction missAbortMultipleRandomConstruction = new MissAbortMultipleConstruction(randomConstruction, App.MAX_MISSES);
        final CloneableConstruction fixMultipleRandomConstruction = new FixMultipleConstruction(randomConstruction, App.NUM_OF_RANDOM_PLANS);
        final Construction multipleRadialConstruction = new MultipleSweepConstruction(simpleConstruction, App.randomUtils);
        final Construction multipleSavingsConstruction = new MultipleSavingsConstruction();
        final Construction multipleSavingsTourConstruction = new MultipleSavingsTourConstruction();
        final MultithreadingConstruction multithreadingConstruction = new MultithreadingConstruction(fixMultipleRandomConstruction);

        final List<Construction> allConstructions = new ArrayList<>(Arrays.asList(nearestNeighbor, radialConstruction, multipleRadialConstruction,
                randomConstruction, fixMultipleRandomConstruction, missAbortMultipleRandomConstruction, multithreadingConstruction));
        final MultithreadingConstruction multiThreadAll = new MultithreadingConstruction(new ArrayList<>(allConstructions));

        allConstructions.add(multiThreadAll);

        final ArrayList<ConstructionTimeMeasuring> timeMeasuringTasks = new ArrayList<>(allConstructions.size());

        for (final Construction construction : allConstructions) {
            final ConstructionTimeMeasuring timeMeasuring = new ConstructionTimeMeasuring(construction, configuration);
            timeMeasuring.runTask();
            timeMeasuringTasks.add(timeMeasuring);
        }

        for (final ConstructionTimeMeasuring timeMeasuring : timeMeasuringTasks) {
            App.LOG.info("");
            final Plan plan = timeMeasuring.getPlan();
            plan.logPlan();
            timeMeasuring.getConstruction().logStatistic();
            App.LOG.info("construction took " + timeMeasuring.duration() + " ms");
            final boolean valid = validator.validate(configuration, plan);
            App.LOG.info("plan is valid: " + valid);
            final double totalFitness = fitnessTest.calculateFitness(plan);
            App.LOG.info("total fitness value: " + totalFitness);
            plan.logTours();

            if (valid && (bestPlan == null || bestPlan.length() > plan.length())) {
                bestPlan = plan;
            }
        }

        return bestPlan;
    }

    /**
     * loads the configuration from a XML-file.
     * 
     * @return parsed configuration
     */
    private static Configuration loadConfiguration() {
        final ConfigurationParser confParser = new JAXBConfigurationParserImpl();

        final File file = new File("conf.xml");

        final Instant start = new Instant();
        final Configuration configuration = confParser.parseConfiguration(file);
        final long durationMillis = new Interval(start, new Instant()).toDurationMillis();
        App.LOG.info("configuration parsing took " + durationMillis + " ms");
        return configuration;
    }

    private static List<Plan> generateRandomPlans(final Configuration configuration, final int numberOfPlans) {
        final List<Plan> plans = new ArrayList<>(numberOfPlans);

        final RandomSimpleConstruction randomSimpleConstruction = new RandomSimpleConstruction(App.randomUtils);

        final RandomConstruction randomConstruction = new RandomConstruction(randomSimpleConstruction, App.randomUtils);

        for (int i = 0; i < numberOfPlans; i++) {
            final Plan plan = randomConstruction.constructPlan(configuration);
            plans.add(plan);
        }

        return plans;
    }
}
