package de.hsbremen.kss.chart;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import de.hsbremen.kss.events.NewPopulationEvent;

public class FittnessDataset extends XYSeriesCollection {

    private final XYSeries bestPlan = new XYSeries("Best Fittness");

    private final XYSeries avgPlan = new XYSeries("Avarage Fittness");

    private final XYSeries worstPlan = new XYSeries("Worst Fittness");

    public FittnessDataset(final EventBus eventBus) {
        eventBus.register(this);

        addSeries(this.bestPlan);
        addSeries(this.avgPlan);
        addSeries(this.worstPlan);
    }

    @Subscribe
    public void listenFitness(final NewPopulationEvent newPopulationEvent) {
        this.bestPlan.add(newPopulationEvent.iteration, newPopulationEvent.bestFitness());
        this.worstPlan.add(newPopulationEvent.iteration, newPopulationEvent.worstFitness());
        this.avgPlan.add(newPopulationEvent.iteration, newPopulationEvent.avgFitness());
    }
}
