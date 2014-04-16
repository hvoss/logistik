package de.hsbremen.kss;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.ConfigurationParser;
import de.hsbremen.kss.configuration.JAXBConfigurationParserImpl;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.construction.Construction;
import de.hsbremen.kss.construction.NearestNeighbor;
import de.hsbremen.kss.construction.RadialConstruction;
import de.hsbremen.kss.construction.RandomConstruction;
import de.hsbremen.kss.construction.SavingsContruction;
import de.hsbremen.kss.construction.NearestNeighbor;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.validate.SimpleValidator;
import de.hsbremen.kss.validate.Validator;

/**
 * Hello world!
 * 
 */
public class App {

	/** logging interface */
	private final static Logger LOG = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		LOG.info("App started");

		ConfigurationParser confParser = new JAXBConfigurationParserImpl();

		File file = new File("conf.xml");

		Configuration configuration = confParser.parseConfiguration(file);

		LOG.info("got " + configuration.getStations().size() + " stations");
		LOG.info("got " + configuration.getVehicles().size() + " vehicles");
		LOG.info("got " + configuration.getOrders().size() + " orders");
		LOG.info("got " + configuration.getProducts().size() + " products");
		LOG.info("got " + configuration.getProductGroups().size()
				+ " product groups");

		LOG.info("stations: " + configuration.getStations());
		LOG.info("vehicles: " + configuration.getVehicles());
		LOG.info("orders: " + configuration.getOrders());
		LOG.info("products: " + configuration.getProducts());
		LOG.info("product groups: " + configuration.getProductGroups());

		logDistancesBetweenStations(configuration.getStations());

		for (Order order : configuration.getOrders()) {
			LOG.info(order.getName() + ": " + order.getProducts());
		}

		for (Station station : configuration.getStations()) {
			if (!station.getSourceProducts().isEmpty()) {
				LOG.info(station.getName() + ": "
						+ station.getSourceProducts().toString());
			}
		}

		Construction savingsContruction = new SavingsContruction();
		Construction nearestNeighbor = new NearestNeighbor();
		Construction randomConstruction = new RandomConstruction();
		Construction radialConstruction = new RadialConstruction();

		Plan savingsPlan = savingsContruction.constructPlan(configuration);
		Plan nearestNeighborPlan = nearestNeighbor
				.constructPlan(configuration);
		Plan radialPlan = radialConstruction.constructPlan(configuration);

		Plan bestRandomPlan = null;
		for (int i = 0; i < 2000; i++) {
			Plan randomPlan = randomConstruction.constructPlan(configuration);
			if (bestRandomPlan == null
					|| bestRandomPlan.length() > randomPlan.length()) {
				bestRandomPlan = randomPlan;
			}
		}

		bestRandomPlan.logPlan(RandomConstruction.class);
		bestRandomPlan.logTours();
		nearestNeighborPlan.logPlan(NearestNeighbor.class);
		nearestNeighborPlan.logTours();
		savingsPlan.logPlan(SavingsContruction.class);
		savingsPlan.logTours();
		radialPlan.logPlan(RadialConstruction.class);
		radialPlan.logTours();

	}

	private static void logDistancesBetweenStations(Collection<Station> stations) {
		Set<Station> processedStations = new HashSet<>();
		for (Station station : stations) {
			processedStations.add(station);
			for (Station otherStation : stations) {
				if (!processedStations.contains(otherStation)) {
					LOG.debug("distance between " + station.getName() + " and "
							+ otherStation.getName() + ": "
							+ Math.round(station.distance(otherStation))
							+ " km");
				}
			}
		}
	}
}
