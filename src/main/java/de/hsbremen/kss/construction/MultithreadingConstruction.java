package de.hsbremen.kss.construction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.Validate;
import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.model.Plan;

/**
 * construction method that runs constructions in multiple threads.
 * 
 * @author henrik
 * 
 */
public final class MultithreadingConstruction implements Construction {

    /** hundred percent */
    private static final int HUNDRED = 100;

    /** logging interface */
    private static final Logger LOG = LoggerFactory.getLogger(MultithreadingConstruction.class);

    /** number of threads to use */
    private final int numberOfThreads;

    /** construction method to use */
    private final CloneableConstruction construction;

    /** duration of the thread controller in ms */
    private long duration;

    /** duration of all threads */
    private long threadDuration;

    /**
     * ctor.
     * 
     * @param construction
     *            construction method to use
     */
    public MultithreadingConstruction(final CloneableConstruction construction) {
        this(construction, Runtime.getRuntime().availableProcessors());
    }

    /**
     * ctor.
     * 
     * @param construction
     *            construction method to use
     * @param numberOfThreads
     *            number of threads to use
     */
    public MultithreadingConstruction(final CloneableConstruction construction, final int numberOfThreads) {
        Validate.notNull(construction, "construction is null");
        Validate.isTrue(numberOfThreads > 0, "number of threads must be greater than zero");

        this.construction = construction;
        this.numberOfThreads = numberOfThreads;
    }

    @Override
    public Plan constructPlan(final Configuration configuration) {
        final ExecutorService executorService = Executors.newFixedThreadPool(this.numberOfThreads);
        final List<ConstructionRunnable> constructions = new ArrayList<>();
        Plan bestPlan = null;
        final long start = System.currentTimeMillis();

        for (int i = 0; i < this.numberOfThreads; i++) {
            final Construction constructionClone = this.construction.clone();
            final ConstructionRunnable runnable = new ConstructionRunnable(constructionClone, configuration);
            executorService.execute(runnable);
            constructions.add(runnable);
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.threadDuration = 0;
        for (final ConstructionRunnable constructionRunnable : constructions) {
            final Plan plan = constructionRunnable.getPlan();
            if (bestPlan == null || bestPlan.length() > plan.length()) {
                bestPlan = plan;
            }
            this.threadDuration += constructionRunnable.getDuration();
        }

        this.duration = System.currentTimeMillis() - start;

        return new Plan(MultithreadingConstruction.class, bestPlan);
    }

    @Override
    public void logStatistic() {
        final long timeSaving = this.threadDuration - this.duration;
        final double relativeTimeSaving = MultithreadingConstruction.HUNDRED - ((double) this.duration / (double) this.threadDuration)
                * MultithreadingConstruction.HUNDRED;

        MultithreadingConstruction.LOG.info("construction method " + this.construction.getClass().getSimpleName() + " runs on "
                + this.numberOfThreads + " threads");
        MultithreadingConstruction.LOG.info("real executing time: " + this.duration + " ms");
        MultithreadingConstruction.LOG.info("cumulated executing time: " + this.threadDuration + " ms");
        MultithreadingConstruction.LOG.info("time saving: " + timeSaving + " ms (" + Precision.round(relativeTimeSaving, 1) + "%)");
    }

    /**
     * Runnable to run a construction method in a thread.
     * 
     * @author henrik
     * 
     */
    public static final class ConstructionRunnable implements Runnable {

        /** construction method to use. */
        private final Construction construction;

        /** configuration to use. */
        private final Configuration configuration;

        /** constructed plan */
        private Plan plan;

        /** start time (ms) */
        private long duration;

        /**
         * ctor.
         * 
         * @param construction
         *            construction method to use.
         * @param configuration
         *            configuration to use.
         */
        public ConstructionRunnable(final Construction construction, final Configuration configuration) {
            this.construction = construction;
            this.configuration = configuration;
        }

        @Override
        public void run() {
            final long start = System.currentTimeMillis();
            this.plan = this.construction.constructPlan(this.configuration);
            this.duration = System.currentTimeMillis() - start;
        }

        /**
         * gets the constructed plan.
         * 
         * @return the constructed plan.
         */
        public Plan getPlan() {
            return this.plan;
        }

        /**
         * returns the duration in ms.
         * 
         * @return duration in ms
         */
        public long getDuration() {
            return this.duration;
        }
    }

}
