package de.hsbremen.kss.fitness;

import java.util.Set;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.model.Plan;

/**
 * This class calculates the fitness of a plan (individual of a population). The fitness value
 * is the sum of the length of a plan and the punishments. The plan with the least fitness is the
 * best plan. The punishments consists of different parts, e.g. number of vehicles, empty part of
 * tours. These parts handle the restrictions of a plan and can be weighted over constants. The sum of
 * these constants should be one.
 * 
 * @author david
 *
 */
public class SimpleFitnessTest implements FitnessTest {
	
	// TODO Constants in an extra File or Class
	
	/**
	 * factor for empty parts of a tour
	 */
	private static final double EMPTY_PART_OF_TOURS = 0.2;
	
	/**
	 * factor for the total number of used vehicles
	 */
	private static final double NUMBER_OF_VEHICLES = 0.2;
	
	/**
	 * factor for the total number of overloads
	 */
	private static final double NUMBER_OF_OVERLOADS = 0.1;
	
	/**
	 * factor for the sum of overloads
	 */
	private static final double SUM_OF_OVERLOADS = 0.1;
	
	/**
	 * factor for the total number of waiting times
	 */
	private static final double NUMBER_OF_WAITING_TIME = 0.1;
	
	/**
	 * factor for the sum of waiting time
	 */
	private static final double SUM_OF_WAITING_TIME = 0.1;
	
	/**
	 * factor for the total number of missed time windows
	 */
	private static final double NUMBER_OF_MISSED_TIME_WINDOWS = 0.1;
	
	/**
	 * factor for the sum of missed time windows
	 */
	private static final double SUM_OF_MISSED_TIME_WINDOWS = 0.1;

	@Override
	public Double calculateFitness(Configuration configuration, Plan plan) {
		return null;
	}
	
	/**
	 * Calculates the value for the length of a plan
	 * 
	 * @param orders
	 *            all orders of the configuration
	 * @param plan
	 *            plan for which the fitness should be calculated
	 * @return
	 */
	private Double calculateLength(Set<Order> orders, Plan plan) {
		return null;
	}
	
	private Double calculateNumberOfVehicles(Set<Order> orders, Plan plan) {
		return null;
	}
	
	private boolean checkSumOfConstants() {
		return true;
	}

}
