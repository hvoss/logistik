package de.hsbremen.kss.genetic;

import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.EventBus;

import de.hsbremen.kss.genetic.abortion.AbortionCheck;
import de.hsbremen.kss.genetic.abortion.AbortionCheckImpl;
import de.hsbremen.kss.genetic.crossover.ControlStringCrossoverImpl;
import de.hsbremen.kss.genetic.crossover.Crossover;
import de.hsbremen.kss.genetic.fitness.CapacityFitnessTest;
import de.hsbremen.kss.genetic.fitness.FitnessTest;
import de.hsbremen.kss.genetic.fitness.FitnessTestBuilder;
import de.hsbremen.kss.genetic.fitness.LengthFitnessTest;
import de.hsbremen.kss.genetic.fitness.LoadingFitnessTest;
import de.hsbremen.kss.genetic.fitness.VehicleFitnessTest;
import de.hsbremen.kss.genetic.fitness.VehicleMakespanFitnessTest;
import de.hsbremen.kss.genetic.mutation.Mutation;
import de.hsbremen.kss.genetic.mutation.MutationBuilder;
import de.hsbremen.kss.genetic.selection.LinearDistributionSelectionImpl;
import de.hsbremen.kss.genetic.selection.RandomSelection;
import de.hsbremen.kss.genetic.selection.Selection;
import de.hsbremen.kss.util.RandomUtils;
import de.hsbremen.kss.validate.RightOrderValidatorImpl;
import de.hsbremen.kss.validate.Validator;

public class GeneticAlgorithmFactory {

    public static GeneticAlgorithm createGeneticAlgorithm(final EventBus eventBus, final RandomUtils randomUtils) {
        final int maxIterations = 5000;
        final double abortCriterion = 0.001;

        // @formatter:off
        final FitnessTest fitnessTest = new FitnessTestBuilder()
        .addFitnessTest(new LengthFitnessTest(2))
        .addFitnessTest(new VehicleFitnessTest(8, 1.5))
        .addFitnessTest(new CapacityFitnessTest(5))
        .addFitnessTest(new VehicleMakespanFitnessTest(1.2))
        .addFitnessTest(new LoadingFitnessTest(1.2))
        ;
        // @formatter:on

        //@formatter:off
        final List<Mutation> mutationMethods = new MutationBuilder(randomUtils)
        .moveActionMutation(1)
        .moveSubrouteMutation(1)
        .swapOrderMutation(1)
        .allocateRouteMutation(1)
        .combineTwoToursMutation(1)
        .splitTourMutation(1)
        //            .nullMutation(1)
        .build();
        //@formatter:on

        final List<Crossover> crossoverMethods = new ArrayList<>();
        crossoverMethods.add(new ControlStringCrossoverImpl(randomUtils));
        crossoverMethods.add(null);
        crossoverMethods.add(null);
        crossoverMethods.add(null);
        crossoverMethods.add(null);

        final Validator validator = new RightOrderValidatorImpl();
        validator.enableLogging(true);
        Selection selectionMethod = null;
        selectionMethod = new RandomSelection(randomUtils);
        selectionMethod = new LinearDistributionSelectionImpl(randomUtils);

        final AbortionCheck abortionCheck = new AbortionCheckImpl(fitnessTest, maxIterations, abortCriterion);
        return new GeneticAlgorithmImpl(eventBus, fitnessTest, randomUtils, mutationMethods, crossoverMethods, validator, selectionMethod,
                abortionCheck);
    }
}
