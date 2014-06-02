package de.hsbremen.kss.fitness;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class SimpleFitnessTest extends AbstractFitnessTest {
	
	// TODO Constants in an extra file or class
	
	/** factor for empty parts of a tour */
	private static final double EMPTY_PART_OF_TOURS = 0.2;
	
	/** factor for the total number of used vehicles */
	private static final double NUMBER_OF_VEHICLES = 0.2;
	
	/** factor for the total number of overloads */
	private static final double NUMBER_OF_OVERLOADS = 0.1;
	
	/** factor for the sum of overloads */
	private static final double SUM_OF_OVERLOADS = 0.1;
	
	/** factor for the total number of waiting times */
	private static final double NUMBER_OF_WAITING_TIME = 0.1;
	
	/** factor for the sum of waiting time */
	private static final double SUM_OF_WAITING_TIME = 0.1;
	
	/** factor for the total number of missed time windows */
	private static final double NUMBER_OF_MISSED_TIME_WINDOWS = 0.1;
	
	/** factor for the sum of missed time windows */
	private static final double SUM_OF_MISSED_TIME_WINDOWS = 0.1;
	
	/** Sum of the distances of the orders */
	private double initialDistance = 0.0;
	
	/** the configuration */
	private Configuration configuration;
	
	/** logging interface */
    private static final Logger LOG = LoggerFactory.getLogger(SimpleFitnessTest.class);
		
	public SimpleFitnessTest(Configuration configuration) {
		
		this.configuration = configuration;
		
		Set<Order> allOrders = new HashSet<Order>(this.configuration.getOrders());
		for (Order order : allOrders) {
			this.initialDistance += order.getSourceStation().distance(order.getDestinationStation());
		}
		LOG.info("Length of all Orders: " + this.initialDistance);
	}

	@Override
	public Double calculateFitness(Plan plan) {
		
		double totalFitness = 0.0;
		
		totalFitness += calculateLengthFitness(plan);
		totalFitness += NUMBER_OF_VEHICLES * calculateNumberOfVehiclesFitness(plan);
//		LOG.info("Total Fitness Value: " + totalFitness);
		
		return totalFitness;
	}
	
	/**
	 * Calculates the value for the length of a plan
	 * 
	 * @param plan
	 *            plan for which the fitness should be calculated
	 * @return
	 */
	private double calculateLengthFitness(Plan plan) {
		double firstEdge = this.initialDistance;
		double secondEdge = this.initialDistance * 2;
		double thirdEdge = this.initialDistance * 5;
		double fourthEdge = this.initialDistance * 10;
		
		double lengthFitness = 0.0;
		
		double lengthFactor = 0.25 *  this.initialDistance;
		
		if (plan.length() <= firstEdge) {
			lengthFitness = 0.5 * plan.length() / lengthFactor;
//			LOG.info("Length Category: 1");
		}
		
		if (plan.length() > firstEdge && plan.length() <= secondEdge) {
			lengthFitness = 1 * plan.length() / lengthFactor;
//			LOG.info("Length Category: 2");
		}
		
		if (plan.length() > secondEdge && plan.length() <= thirdEdge) {
			lengthFitness = 2 * plan.length() / lengthFactor;
//			LOG.info("Length Category: 3");
		}
		
		if (plan.length() > thirdEdge && plan.length() <= fourthEdge) {
			lengthFitness = 4 * plan.length() / lengthFactor;
//			LOG.info("Length Category: 4");
		}
		
		if (plan.length() > fourthEdge) {
			lengthFitness = 8 * plan.length() / lengthFactor;
//			LOG.info("Length Category: 5");
		}
		
//		LOG.info("Length Fitness Value: " + lengthFitness);
		return lengthFitness;
	}
	
	private double calculateNumberOfVehiclesFitness(Plan plan) {
		
		int ordersSize = this.configuration.getOrders().size();
		
		int firstEdge = ordersSize / 5;
		int secondEdge = ordersSize / 2;
		int thirdEdge = ordersSize;
		
		// number of tours is equivalent with the number of vehicles
		int toursSize = plan.getTours().size();
		
		double vehicleFitness = 0.0;
		
		if (toursSize <= firstEdge) {
			vehicleFitness = toursSize;
//			LOG.info("Vehicle Number Category: 1");
		}
		
		if (toursSize > firstEdge && toursSize <= secondEdge) {
			vehicleFitness = 2 * toursSize;
//			LOG.info("Vehicle Number Category: 2");
		}
		
		if (toursSize > secondEdge && toursSize <= thirdEdge) {
			vehicleFitness = 4 * toursSize;
//			LOG.info("Vehicle Number Category: 3");
		}
		
		if (toursSize > thirdEdge) {
			vehicleFitness = 8 * toursSize;
//			LOG.info("Vehicle Number Category: 4");
		}
		
//		LOG.info("Vehicle Fitness Value: " + vehicleFitness);
		return vehicleFitness;
	}
	
	private double calculateWaitingTime(Plan plan) {
		return 0.0;
	}
	
	private boolean checkSumOfConstants() {
		return true;
	}

}
