package de.hsbremen.kss.chart;

import java.util.HashMap;
import java.util.Map;

import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import de.hsbremen.kss.genetic.events.NewPopulationEvent;
import de.hsbremen.kss.genetic.fitness.FitnessTest;

public class FitnessDistributionDataset extends DefaultTableXYDataset {

    private final Map<Class<? extends FitnessTest>, XYSeries> series = new HashMap<>();

    public FitnessDistributionDataset(final EventBus eventBus) {
        eventBus.register(this);
    }

    @Subscribe
    public void listenFitness(final NewPopulationEvent newPopulationEvent) {
        final Map<Class<? extends FitnessTest>, Double> fitnessDistribution = newPopulationEvent.fitnessDistribution(newPopulationEvent.bestPlan());

        for (final Map.Entry<Class<? extends FitnessTest>, Double> entry : fitnessDistribution.entrySet()) {
            XYSeries xySeries = this.series.get(entry.getKey());
            if (xySeries == null) {
                xySeries = new XYSeries(entry.getKey().getSimpleName(), true, false);
                this.series.put(entry.getKey(), xySeries);
                addSeries(xySeries);
            }
            xySeries.addOrUpdate(new Double(newPopulationEvent.iteration), entry.getValue());
        }
    }
}
