package de.hsbremen.kss.genetic;

import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.EventBus;

import de.hsbremen.kss.fitness.CapacityFitnessTest;
import de.hsbremen.kss.fitness.FitnessTest;
import de.hsbremen.kss.fitness.FitnessTestBuilder;
import de.hsbremen.kss.fitness.LengthFitnessTest;
import de.hsbremen.kss.fitness.LoadingFitnessTest;
import de.hsbremen.kss.fitness.VehicleFitnessTest;
import de.hsbremen.kss.fitness.VehicleMakespanFitnessTest;
import de.hsbremen.kss.util.RandomUtils;
import de.hsbremen.kss.validate.RightOrderValidatorImpl;
import de.hsbremen.kss.validate.Validator;

public class GeneticAlgorithmFactory {

    public static GeneticAlgorithm createGeneticAlgorithm(final EventBus eventBus, final RandomUtils randomUtils) {
        final int maxIterations = 5000;
        final double abortCriterion = 0.0001;

        //@formatter:off
        final FitnessTest fitnessTest = new FitnessTestBuilder()
                .addFitnessTest(new LengthFitnessTest())
                .addFitnessTest(new VehicleFitnessTest(1))
                .addFitnessTest(new CapacityFitnessTest(1.02))
                .addFitnessTest(new VehicleMakespanFitnessTest(1.02))
                .addFitnessTest(new LoadingFitnessTest(50));
        //@formatter:on

        //@formatter:off
        final List<Mutation> mutationMethods = new MutationBuilder(randomUtils)
            .moveActionMutation(20)
            .moveSubrouteMutation(10)
            .swapOrderMutation(10)
            .allocateLongestRouteMutation(2)
            .allocateRandomRouteMutation(2)
            .allocateShortestRouteMutation(2)
            .combineTwoToursMutation(1)
            .splitTourMutation(10)
            .nullMutation(10)
            .build();
        //@formatter:on

        final List<Crossover> crossoverMethods = new ArrayList<>();
        // crossoverMethods.add(new ControlStringCrossoverImpl(randomUtils));

        final Validator validator = new RightOrderValidatorImpl();
        validator.enableLogging(false);
        final Selection selectionMethod = new RandomSelection(randomUtils);
        // Selection selectionMethod = new
        // LinearDistributionSelectionImpl(randomUtils);

        final AbortionCheck abortionCheck = new AbortionCheckImpl(fitnessTest, maxIterations, abortCriterion);
        return new GeneticAlgorithmImpl(eventBus, fitnessTest, randomUtils, mutationMethods, crossoverMethods, validator, selectionMethod,
                abortionCheck);
    }
}
