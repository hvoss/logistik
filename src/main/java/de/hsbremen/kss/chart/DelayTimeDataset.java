package de.hsbremen.kss.chart;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import de.hsbremen.kss.events.NewPopulationEvent;

public class DelayTimeDataset extends XYSeriesCollection {

    private final XYSeries bestPlan = new XYSeries("Best Delay");

    private final XYSeries avgPlan = new XYSeries("Avarage Delay");

    private final XYSeries worstPlan = new XYSeries("Worst Delay");

    public DelayTimeDataset(final EventBus eventBus) {
        eventBus.register(this);

        addSeries(this.bestPlan);
        addSeries(this.avgPlan);
        addSeries(this.worstPlan);
    }

    @Subscribe
    public void listenLength(final NewPopulationEvent newPopulationEvent) {
        this.bestPlan.add(newPopulationEvent.iteration, newPopulationEvent.bestDelayTime());
        this.worstPlan.add(newPopulationEvent.iteration, newPopulationEvent.worstDelayTime());
        this.avgPlan.add(newPopulationEvent.iteration, newPopulationEvent.avgDelayTime());
    }

}
