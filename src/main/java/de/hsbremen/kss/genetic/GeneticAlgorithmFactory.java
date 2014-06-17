package de.hsbremen.kss.genetic;

import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.EventBus;

import de.hsbremen.kss.fitness.CapacityFitnessTest;
import de.hsbremen.kss.fitness.FitnessTest;
import de.hsbremen.kss.fitness.FitnessTestBuilder;
import de.hsbremen.kss.fitness.LengthFitnessTest;
import de.hsbremen.kss.fitness.VehicleFitnessTest;
import de.hsbremen.kss.util.RandomUtils;
import de.hsbremen.kss.validate.SimpleValidator;
import de.hsbremen.kss.validate.Validator;

public class GeneticAlgorithmFactory {

    public static GeneticAlgorithm createGeneticAlgorithm(final EventBus eventBus, final RandomUtils randomUtils) {
        final int maxIterations = 1000;
        final double abortCriterion = 0.001;

        final FitnessTest fitnessTest = new FitnessTestBuilder().addFitnessTest(new LengthFitnessTest()).addFitnessTest(new VehicleFitnessTest())
                .addFitnessTest(new CapacityFitnessTest());

        MutationBuilder mutationBuilder = new MutationBuilder(randomUtils);
        
        //@formater:off
        final List<Mutation> mutationMethods = mutationBuilder
            .moveActionMutation(20)
            .moveSubrouteMutation(10)
            .swapOrderMutation(10)
            .allocateLongestRouteMutation(2)
            .allocateRandomRouteMutation(2)
            .allocateShortestRouteMutation(2)
            .nullMutation(10)
            .build();
        //@formater:on
        
        final List<Crossover> crossoverMethods = new ArrayList<>();
        // crossoverMethods.add(new ControlStringCrossoverImpl(randomUtils));

        final Validator validator = new SimpleValidator();
        final PlanComparator planComparator = new PlanComparator(fitnessTest);
        final Selection selectionMethod = new RandomSelection(randomUtils);
        // selectionMethod = new LinearDistributionSelectionImpl(randomUtils);

        final AbortionCheck abortionCheck = new AbortionCheckImpl(fitnessTest, maxIterations, abortCriterion);
        return new GeneticAlgorithmImpl(eventBus, fitnessTest, randomUtils, mutationMethods, crossoverMethods, validator, planComparator,
                selectionMethod, abortionCheck);
    }
}
