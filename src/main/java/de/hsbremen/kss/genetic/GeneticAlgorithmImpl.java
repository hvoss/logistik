package de.hsbremen.kss.genetic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.genetics.Population;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.org.apache.bcel.internal.generic.PopInstruction;

import de.hsbremen.kss.App;
import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.construction.NearestNeighbor;
import de.hsbremen.kss.construction.SweepConstruction;
import de.hsbremen.kss.fitness.FitnessTest;
import de.hsbremen.kss.fitness.LengthFitnessTest;
import de.hsbremen.kss.fitness.SimpleFitnessTest;
import de.hsbremen.kss.model.Action;
import de.hsbremen.kss.model.OrderAction;
import de.hsbremen.kss.model.OrderLoadAction;
import de.hsbremen.kss.model.OrderUnloadAction;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.RandomUtils;
import de.hsbremen.kss.validate.SimpleValidator;
import de.hsbremen.kss.validate.Validator;

/**
 * 
 * @author olli
 *
 */

public class GeneticAlgorithmImpl implements GeneticAlgorithm {
	
    /** logging interface */
    private static final Logger LOG = LoggerFactory.getLogger(GeneticAlgorithmImpl.class);
	
	//Constans
	private final int SELECTION_RANGE_POPULATION = 100; //Welche Eltern erzeugen Nachkommen (Die 10 Besten)
	private final double MUTATION_RATE = 1;
	private final double CROSSOVER_RATE = 1;
	private final double FIFTY_FIFTY = 0.5;
	
	private double durchschnittsFitness = Double.MAX_VALUE;
	
    private FitnessTest fitnessTest;
    private List<Plan> population;
    
    private RandomUtils randomUtils = new RandomUtils(0);

    public GeneticAlgorithmImpl(final Configuration configuration, final Collection<Plan> plans) {
        this.population = new ArrayList<Plan>(plans);
        this.fitnessTest = new LengthFitnessTest();
    }
    
    @Override
    public Plan startOptimize() {
        
    	population = rankPopulation(population);
    	Plan bestplan = population.get(0);

    	int count = 0;
    	while(Math.round(durchschnittsFitness) > Math.round(fitnessTest.calculateFitness(bestplan))) {
			GeneticAlgorithmImpl.LOG.info("Durchschnittsfitness: " + durchschnittsFitness);
			GeneticAlgorithmImpl.LOG.info("Beste Fitness: " + bestplan.length());
			bestplan = optimize().get(0);
			if (count++ > 1000) {
				break;
			}
		}
		GeneticAlgorithmImpl.LOG.info("ENDE Durchschnittsfitness: " + durchschnittsFitness);
		GeneticAlgorithmImpl.LOG.info("ENDE Beste Fitness: " + fitnessTest.calculateFitness(bestplan));
		return bestplan;
    }

    public List<Plan> optimize() {
        
    	durchschnittsFitness = 0;
    	
    	GeneticAlgorithmImpl.LOG.info("Durchschnittsfitness: " + durchschnittsFitness);
    	
    	List<Plan> nextPopulation = createNewPopulation(population);
    	nextPopulation = rankPopulation(nextPopulation);
    	population = rankPopulation(population);
    	List<Plan> newStartPopulation = new ArrayList<>(100);
    	newStartPopulation.addAll(population.subList(0, 50));
    	newStartPopulation.addAll(nextPopulation.subList(0, 50));
    	
    	logPopulation("old", population);
    	logPopulation("new", nextPopulation);

    	newStartPopulation = rankPopulation(newStartPopulation);
    	
    	population = newStartPopulation.subList(0, 100);
    	
    	
        for (Plan plan : population) {
			durchschnittsFitness += plan.length();
		}
        
        durchschnittsFitness = durchschnittsFitness / population.size();
        
        return population;
    }
    
    /**
     * 
     */
    private List<Plan> rankPopulation(List<Plan> populationToRank) {
    	Collections.sort(populationToRank, new Comparator<Plan>() {

			@Override
			public int compare(final Plan plan1, final Plan plan2) {
				if (plan1.length() < plan2.length()) {
					return -1;
				} else if (plan1.length() > plan2.length()){
					return 1;
				} else {
					return 0;
				}
			}
		});
    	return populationToRank;
    }
    
    /**
     * Wahrscheinlichkeit für ersten 50
     */
    private List<Plan> createNewPopulation(List<Plan> startPopulation) {
    	List<Plan> newGeneration = new ArrayList<Plan>();
    	for (int i = 0; i < startPopulation.size(); i++) {
        	Plan parent1 = this.randomUtils.randomElement(startPopulation);
        	Plan parent2 = this.randomUtils.randomElement(startPopulation);
        	newGeneration.add(createChild(parent1, parent2));
    	}
    	return newGeneration;
    }
    
    
    /**
     * 
     * @param parent1 Parent1
     * @param parent2 Parent2
     * @return child
     */
    private Plan createChild(final Plan parent1, final Plan parent2) {
    	
    	Plan child;
    	
    	if (Math.random() <= CROSSOVER_RATE) {
    		child = crossoverWithControlString(parent1, parent2);
    	} else if ((Math.random() * FIFTY_FIFTY) <= FIFTY_FIFTY) {
    		child = parent1;
    	} else {
    		child = parent2;
    	}
    	
    	if (Math.random() <= MUTATION_RATE) {
    		child = mutate(child);
    	}
    	
    	return child;
    }
    
   	/**
	 * 
	 * @param parent1
	 * @param parent2
	 * @return
	 */
	private Plan crossoverWithControlString(final Plan parent1,
			final Plan parent2) {

		Plan newPlan = new Plan(NearestNeighbor.class);
		Tour newTour;
		Collection<Order> ordersToIgnore = new ArrayList<Order>();

		List<Tour> parent1Tours = parent1.getTours();
		List<Tour> parent2Tours = parent2.getTours();

		int parentSize;
		List<Tour> firstCrossoverPartner, secondCrossoverPartner;

		if (parent1Tours.size() > parent2Tours.size()) {
			parentSize = parent2Tours.size();
			firstCrossoverPartner = parent2Tours;
			secondCrossoverPartner = parent1Tours;
		} else {
			parentSize = parent1Tours.size();
			firstCrossoverPartner = parent1Tours;
			secondCrossoverPartner = parent2Tours;
		}
		for (int i = 0; i < parentSize; i++) {
			newTour = newPlan
					.newTour(firstCrossoverPartner.get(i).getVehicle());
			newTour.leafSourceDepot();
			ordersToIgnore.addAll(newTour.getOrders());
			crossover(newTour, firstCrossoverPartner.get(i),
					secondCrossoverPartner.get(i), ordersToIgnore);
			newTour.gotoDestinationDepot();
			newPlan.addTour(newTour);
		}
		return newPlan;
	}
    
    private Tour crossover(Tour newTour, Tour firstTour, Tour secondTour, Collection<Order> orderToIgnore) {
    	List<OrderAction> firstActions = new ArrayList<>(firstTour.getOrderActions());
    	List<OrderAction> secondActions = new ArrayList<>(secondTour.getOrderActions());
    	
    	while (!firstActions.isEmpty() || !secondActions.isEmpty()) {
    		
    		Action action;
    		
    		if (firstActions.isEmpty()) {
    			action = secondActions.remove(0);
    		} else if (secondActions.isEmpty()) {
    			action = firstActions.remove(0);
    		} else if (Math.random() < FIFTY_FIFTY) {
    			action = firstActions.remove(0);
    			secondActions.remove(action);
    		} else {
    			action = secondActions.remove(0);
    			firstActions.remove(action);
    		}
    		
    		if (action instanceof OrderAction) {
    			OrderAction orderAction = (OrderAction) action;
    			if (orderToIgnore.contains(orderAction.getOrder())) {
    				continue;
    			}
    		}
    		
    		newTour.addOtherAction(action);		
    	}
    	
    	return newTour;
    }
    
    /**
     * 
     * @param tour
     * @returnlkl
     */
    private Plan mutate(final Plan plan) {
    	
    	Plan newPlan = new Plan(SweepConstruction.class);
    	
    	List<Tour> tours = new ArrayList<>(plan.getTours());
    	Tour tourToMutate = this.randomUtils.removeRandomElement(tours);
    	
    	List<OrderAction> actions = tourToMutate.getOrderActions();
    	OrderAction actionToMove = this.randomUtils.removeRandomElement(actions);
    	this.randomUtils.insertAtRandomPosition(actions, actionToMove);
    	
    	Tour newTour = newPlan.newTour(tourToMutate.getVehicle());
    	
    	newTour.addOtherActions(actions);
    	
    	plan.addTour(newTour);
    	
    	for (Tour tour : tours) {
    		plan.addTour(tour);
    	}
    	
    	return plan;
    }
    
    private void logPopulation(String name, List<Plan> population) {
    	Double best = this.fitnessTest.calculateFitness(population.get(0));
    	Double worst = this.fitnessTest.calculateFitness(population.get(population.size()-1));
    	GeneticAlgorithmImpl.LOG.info("Popuplation " + name + ": best: " +best + ", worst:"  +worst);
    }
}
