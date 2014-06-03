package de.hsbremen.kss;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import de.hsbremen.kss.configuration.CircleConfigurationGenerator;
import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.construction.Construction;
import de.hsbremen.kss.construction.NearestNeighbor;
import de.hsbremen.kss.construction.RandomConstruction;
import de.hsbremen.kss.events.NewPlanEvent;
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
        app.start();
    }

    public void start() {
        final int diameter = 300;

        final List<Construction> constructionMethods = new ArrayList<>();
        final RandomSimpleConstruction randomSimpleConstruction = new RandomSimpleConstruction(this.randomUtils);
        final RandomConstruction randomConstruction = new RandomConstruction(randomSimpleConstruction, this.randomUtils);
        final NearestNeighbor nearestNeighbor = new NearestNeighbor(randomSimpleConstruction, this.randomUtils);
        constructionMethods.add(randomConstruction);
        // constructionMethods.add(nearestNeighbor);

        final PopulationGeneratorImpl populationGenerator = new PopulationGeneratorImpl(this.randomUtils);

        final GeneticAlgorithm geneticAlgorithm = GeneticAlgorithmFactory.createGeneticAlgorithm(this.eventBus, this.randomUtils);
        final CircleConfigurationGenerator circleConfigurationGenerator = new CircleConfigurationGenerator(this.randomUtils);
        final Configuration circleConfig = circleConfigurationGenerator.generateConfiguration(diameter, 20, 1);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                App.this.mainFrame = new MainFrame(Map.circle(diameter), circleConfig);

            }
        });

        final List<Plan> randomPlans = populationGenerator.createPopulation(circleConfig, constructionMethods, 500);

        final Plan plan = geneticAlgorithm.startOptimize(circleConfig, randomPlans);

        plan.logPlan();
        plan.logTours();
    }

    @Subscribe
    public void listen(final NewPlanEvent newPlanEvent) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                App.this.mainFrame.setPlan(newPlanEvent.plan);
            }
        });
    }
}
