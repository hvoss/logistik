package de.hsbremen.kss.genetic;

import java.util.Collection;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.fitness.FitnessTest;
import de.hsbremen.kss.model.Plan;

public class GeneticAlgorithmImpl implements GeneticAlgorithm {

    private FitnessTest fitnessTest;

    public GeneticAlgorithmImpl(/*
                                 * fehlerfaktor für die Verletzung von
                                 * Restriktionen, Mutationsfaktor
                                 */) {
        // TODO Auto-generated constructor stub
    }

    @Override
    public Plan optimize(final Configuration configuration, final Collection<Plan> plans) {
        // TODO Auto-generated method stub
        return null;
    }

}
