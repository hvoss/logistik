package de.hsbremen.kss.genetic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.org.apache.bcel.internal.generic.PopInstruction;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.fitness.FitnessTest;
import de.hsbremen.kss.fitness.SimpleFitnessTest;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;

/**
 * 
 * @author olli
 *
 */

public class GeneticAlgorithmImpl implements GeneticAlgorithm {
	
	private int testCounter = 0;
	
	//Constans
	private final int SELECTION_RANGE_POPULATION = 50; //Welche Eltern erzeugen Nachkommen (Die 50 Besten)
	private final double MUTATION_RATE = 0.9;
	private final double CROSSOVER_RATE = 1;
	private final int NUMBER_OF_NEW_CHILDS = 50;
	
	
    private FitnessTest fitnessTest;
    private List<Plan> population;

    public GeneticAlgorithmImpl(/*
                                 * fehlerfaktor für die Verletzung von
                                 * Restriktionen, Mutationsfaktor
                                 */) {
    	testCounter = 0;
    }

    @Override
    public Plan optimize(final Configuration configuration, final Collection<Plan> plans) {
    	
        this.population = new ArrayList<Plan>(plans);
        this.fitnessTest = new SimpleFitnessTest(configuration);
        
        rankPopulation();
        createNewPopulation();
        
        return null;
    }
    
    private void rankPopulation() {
    	Collections.sort(population, new Comparator<Plan>() {

			@Override
			public int compare(Plan plan1, Plan plan2) {
				if (fitnessTest.calculateFitness(plan1) < fitnessTest.calculateFitness(plan2)) {
					return -1;
				} else if (fitnessTest.calculateFitness(plan1) < fitnessTest.calculateFitness(plan2)) {
					return 1;
				} else {
					return 0;
				}
			}
		});
    }
    
    private void createNewPopulation() {
    	List<Plan> newGeneration = new ArrayList<Plan>();
    	for (int i = 0; i < NUMBER_OF_NEW_CHILDS; i++) {
        	Plan parent1 = population.get((int) (Math.random()*SELECTION_RANGE_POPULATION));
        	Plan parent2 = population.get((int) (Math.random()*SELECTION_RANGE_POPULATION));
        	newGeneration.add(createChild(parent1, parent2));
    	}
    }
    
    private Plan createChild(Plan parent1, Plan parent2) {
    	
    	Plan child;
    	
    	if (Math.random() <= CROSSOVER_RATE) {
    		child = crossoverWithControlString(parent1,parent2);
    	} else if ((Math.random()*0.5)<=0.5) {
    		child = parent1;
    	} else {
    		child = parent2;
    	}
    	
    	System.out.println(testCounter);
    	return null;
    }
    
   /**
    * 
    * @param parent1
    * @param parent2
    * @return
    */
    private Plan crossoverWithControlString(final Plan parent1, final Plan parent2) {
    	
    	List<Tour> toursToSwitch = new ArrayList<Tour>();
    	List<Tour> parent1Tours = parent1.getTours();
    	List<Tour> perent2Tours = parent2.getTours();
    	
    	return null;
    }

}
