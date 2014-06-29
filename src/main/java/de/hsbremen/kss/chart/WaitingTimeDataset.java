package de.hsbremen.kss.chart;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import de.hsbremen.kss.events.NewPopulationEvent;

public class WaitingTimeDataset extends XYSeriesCollection {

    private final XYSeries bestPlan = new XYSeries("Best Waiting");

    private final XYSeries avgPlan = new XYSeries("Avarage Waiting");

    private final XYSeries worstPlan = new XYSeries("Worst Waiting");

    public WaitingTimeDataset(final EventBus eventBus) {
        eventBus.register(this);

        addSeries(this.bestPlan);
        addSeries(this.avgPlan);
        addSeries(this.worstPlan);
    }

    @Subscribe
    public void listenFitness(final NewPopulationEvent newPopulationEvent) {
        this.bestPlan.add(newPopulationEvent.iteration, newPopulationEvent.bestWaitingyTime());
        this.worstPlan.add(newPopulationEvent.iteration, newPopulationEvent.worstWaitingTime());
        this.avgPlan.add(newPopulationEvent.iteration, newPopulationEvent.avgWaitingTime());
    }

}
