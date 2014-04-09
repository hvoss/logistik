package de.hsbremen.kss;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.ConfigurationParser;
import de.hsbremen.kss.configuration.impl.JAXBConfigurationParserImpl;
import de.hsbremen.kss.model.Configuration;
import de.hsbremen.kss.model.Station;

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

		logDistancesBetweenStations(configuration.getStations());
	
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
