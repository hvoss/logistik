package de.hsbremen.kss;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import de.hsbremen.kss.chart.PopulationDataset;
import de.hsbremen.kss.configuration.CircleConfigurationGenerator;
import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.construction.Construction;
import de.hsbremen.kss.construction.RandomConstruction;
import de.hsbremen.kss.events.NewPopulationEvent;
import de.hsbremen.kss.genetic.GeneticAlgorithm;
import de.hsbremen.kss.genetic.GeneticAlgorithmFactory;
import de.hsbremen.kss.genetic.PopulationGeneratorImpl;
import de.hsbremen.kss.gui.MainFrame;
import de.hsbremen.kss.gui.Map;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.simpleconstruction.RandomSimpleConstruction;
import de.hsbremen.kss.util.RandomUtils;

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
        app.test();
        app.start();
    }

    public void start() {

        final int diameter = 300;

        final List<Construction> constructionMethods = new ArrayList<>();
        final RandomSimpleConstruction randomSimpleConstruction = new RandomSimpleConstruction(this.randomUtils);
        final RandomConstruction randomConstruction = new RandomConstruction(randomSimpleConstruction, this.randomUtils);
        constructionMethods.add(randomConstruction);

        final PopulationGeneratorImpl populationGenerator = new PopulationGeneratorImpl(this.randomUtils);

        final GeneticAlgorithm geneticAlgorithm = GeneticAlgorithmFactory.createGeneticAlgorithm(this.eventBus, this.randomUtils);
        final CircleConfigurationGenerator circleConfigurationGenerator = new CircleConfigurationGenerator(this.randomUtils);
        final Configuration circleConfig = circleConfigurationGenerator.generateConfiguration(diameter, 40, 1);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                App.this.mainFrame = new MainFrame(Map.circle(diameter), circleConfig);

            }
        });

        final List<Plan> randomPlans = populationGenerator.createPopulation(circleConfig, constructionMethods, 1000);

        final Plan plan = geneticAlgorithm.startOptimize(circleConfig, randomPlans);

        plan.logPlan();
        plan.logTours();
    }

    private final int count = 0;

    @Subscribe
    public void listen(final NewPopulationEvent newPopulationEvent) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                App.this.mainFrame.setPlan(newPopulationEvent.sortedPopulation.get(0));
            }
        });
    }

    public void test() {
        final PopulationDataset dataset = new PopulationDataset(this.eventBus);
        final JFreeChart chart = ChartFactory.createXYLineChart("", "Iteration", "Fitness", dataset, PlotOrientation.VERTICAL, true, false, false);

        final ChartPanel panel = new ChartPanel(chart);
        final JFrame jFrame = new JFrame();
        jFrame.setContentPane(panel);
        jFrame.pack();
        jFrame.setVisible(true);
    }

}
