package de.hsbremen.kss;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer2;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import de.hsbremen.kss.chart.FitnessDistributionDataset;
import de.hsbremen.kss.chart.FittnessDataset;
import de.hsbremen.kss.chart.LengthDataset;
import de.hsbremen.kss.configuration.CircleConfigurationGenerator;
import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.ConfigurationGenerator;
import de.hsbremen.kss.configuration.JAXBConfigurationParserImpl;
import de.hsbremen.kss.construction.Construction;
import de.hsbremen.kss.construction.NearestNeighbor;
import de.hsbremen.kss.construction.SimpleRandomConstruction;
import de.hsbremen.kss.events.NewPopulationEvent;
import de.hsbremen.kss.fitness.VehicleMakespanFitnessTest;
import de.hsbremen.kss.genetic.GeneticAlgorithm;
import de.hsbremen.kss.genetic.GeneticAlgorithmFactory;
import de.hsbremen.kss.genetic.PopulationGeneratorImpl;
import de.hsbremen.kss.gui.MainFrame;
import de.hsbremen.kss.gui.Map;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.simpleconstruction.RandomSimpleConstruction;
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

    private final RandomUtils randomUtils = new RandomUtils(0);

    private MainFrame mainFrame;

    private final EventBus eventBus = new EventBus();

    private XYSeries xySeries;

    /**
     * static class
     */
    private App() {
        this.eventBus.register(this);
    }

    /**
     * the main functions
     * 
     * @param args
     *            the arguments
     */
    public static void main(final String[] args) {
        final App app = new App();
        app.lineGraph();
        app.fitnessGraph();
        app.germany();
    }

    public void germany() {
        final JAXBConfigurationParserImpl jaxbConfigurationParserImpl = new JAXBConfigurationParserImpl();
        final Configuration germanyConfiguration = jaxbConfigurationParserImpl.parseConfiguration(new File("experiment.xml"));

        final List<Construction> constructionMethods = new ArrayList<>();
        final RandomSimpleConstruction randomSimpleConstruction = new RandomSimpleConstruction(this.randomUtils);
        final NearestNeighbor nearestNeighbor = new NearestNeighbor(randomSimpleConstruction, this.randomUtils);
        final Construction randomConstruction = new SimpleRandomConstruction(this.randomUtils);
        constructionMethods.add(randomConstruction);
        // constructionMethods.add(nearestNeighbor);

        final ConfigurationGenerator configurationGenerator = new ConfigurationGenerator(this.randomUtils);

        final Configuration generateConfiguration = configurationGenerator.generateConfiguration(germanyConfiguration.getStations(),
                germanyConfiguration.getProducts(), germanyConfiguration.getVehicles(), 50);

        final GeneticAlgorithm geneticAlgorithm = GeneticAlgorithmFactory.createGeneticAlgorithm(this.eventBus, this.randomUtils);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                App.this.mainFrame = new MainFrame(Map.GERMANY, generateConfiguration);

            }
        });

        final PopulationGeneratorImpl populationGenerator = new PopulationGeneratorImpl(this.randomUtils);
        final List<Plan> randomPlans = populationGenerator.createPopulation(generateConfiguration, constructionMethods, 200);

        final Plan plan = geneticAlgorithm.startOptimize(generateConfiguration, randomPlans);

        final Validator validator = new SimpleValidator();
        validator.enableLogging(true);

        final VehicleMakespanFitnessTest vehicleMakespanFitnessTest = new VehicleMakespanFitnessTest(5);
        plan.logPlan();
        App.LOG.info("valid:" + validator.validate(generateConfiguration, plan));
        App.LOG.info("fitness: " + vehicleMakespanFitnessTest.calculateFitness(plan));
        plan.logTours();
    }

    public void start() {

        final int diameter = 300;

        final List<Construction> constructionMethods = new ArrayList<>();
        final Construction randomConstruction = new SimpleRandomConstruction(this.randomUtils);
        constructionMethods.add(randomConstruction);

        final PopulationGeneratorImpl populationGenerator = new PopulationGeneratorImpl(this.randomUtils);

        final GeneticAlgorithm geneticAlgorithm = GeneticAlgorithmFactory.createGeneticAlgorithm(this.eventBus, this.randomUtils);
        final CircleConfigurationGenerator circleConfigurationGenerator = new CircleConfigurationGenerator(this.randomUtils);
        final Configuration circleConfig = circleConfigurationGenerator.generateConfiguration(diameter, 40);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                App.this.mainFrame = new MainFrame(Map.circle(diameter), circleConfig);

            }
        });

        final List<Plan> randomPlans = populationGenerator.createPopulation(circleConfig, constructionMethods, 200);

        final Plan plan = geneticAlgorithm.startOptimize(circleConfig, randomPlans);

        final Validator validator = new SimpleValidator();
        validator.enableLogging(true);

        final VehicleMakespanFitnessTest vehicleMakespanFitnessTest = new VehicleMakespanFitnessTest(5);

        plan.logPlan();
        App.LOG.info("valid:" + validator.validate(circleConfig, plan));
        App.LOG.info("fitness: " + vehicleMakespanFitnessTest.calculateFitness(plan));
        plan.logTours();
    }

    @Subscribe
    public void listen(final NewPopulationEvent newPopulationEvent) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                App.this.mainFrame.setPlan(newPopulationEvent.sortedPopulation.get(0));
            }
        });
    }

    public void lineGraph() {
        final LengthDataset dataset = new LengthDataset(this.eventBus);
        final JFreeChart chart = ChartFactory.createXYLineChart("", "Iteration", "Length", dataset, PlotOrientation.VERTICAL, true, false, true);

        final XYPlot plot = chart.getXYPlot();

        plot.setDataset(1, new FittnessDataset(this.eventBus));
        plot.mapDatasetToRangeAxis(1, 1);

        final XYItemRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
        plot.setRenderer(1, renderer2);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);
        final ValueAxis axis2 = new NumberAxis("Fittness");
        plot.setRangeAxis(1, axis2);

        final ChartPanel panel = new ChartPanel(chart);
        final JFrame jFrame = new JFrame();
        jFrame.setContentPane(panel);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public void fitnessGraph() {
        final FitnessDistributionDataset dataset = new FitnessDistributionDataset(this.eventBus);
        final JFreeChart chart = ChartFactory.createStackedXYAreaChart("Fitness distribution", "iteration", "distribution", dataset,
                PlotOrientation.VERTICAL, true, true, false);

        final StackedXYAreaRenderer2 stackedxyarearenderer2 = new StackedXYAreaRenderer2();
        ((XYPlot) chart.getPlot()).setRenderer(0, stackedxyarearenderer2);

        final ChartPanel panel = new ChartPanel(chart);
        final JFrame jFrame = new JFrame();
        jFrame.setContentPane(panel);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
